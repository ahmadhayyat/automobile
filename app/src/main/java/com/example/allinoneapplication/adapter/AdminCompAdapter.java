package com.example.allinoneapplication.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.admin.AddnearbyAreasActivity;
import com.example.allinoneapplication.admin.AdminAllComplainActivity;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.UpdateComplainStatusService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCompAdapter extends BaseAdapter {

    List<Complaint> complaintList;
    Context context;
    LayoutInflater inflater;
    ProgressDialog progressDialog;
    Complaint complaint;


    public AdminCompAdapter(List<Complaint> complaintList, Context context) {
        this.complaintList = complaintList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return complaintList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_admin_complaint, viewGroup, false);
        TextView tv_admin_comp_num = view.findViewById(R.id.tv_admin_comp_num);
        TextView tv_admincomp_sub = view.findViewById(R.id.tv_admincomp_sub);
        TextView tv_admincomp_dname = view.findViewById(R.id.tv_admincomp_dname);
        TextView tv_admincomp_pname = view.findViewById(R.id.tv_admincomp_pname);
        TextView tv_againstcomp_date = view.findViewById(R.id.tv_againstcomp_date);
        TextView tv_againstcmp_status = view.findViewById(R.id.tv_againstcmp_status);
        tv_admin_comp_num.setText(complaintList.get(i).getComp_num());
        tv_admincomp_sub.setText(complaintList.get(i).getComp_sub());
        tv_admincomp_dname.setText(complaintList.get(i).getD_name());
        tv_admincomp_pname.setText(complaintList.get(i).getP_name());
        tv_againstcomp_date.setText(complaintList.get(i).getComp_datetime());
        if (complaintList.get(i).getComp_status().equals("P")) {
            tv_againstcmp_status.setText("Pending");
            tv_againstcmp_status.setTextColor(ContextCompat.getColor(context, R.color.quantum_googred));
        } else if (complaintList.get(i).getComp_status().equals("IP")) {
            tv_againstcmp_status.setText("In Process");
            tv_againstcmp_status.setTextColor(ContextCompat.getColor(context, R.color.quantum_orange));
        } else if (complaintList.get(i).getComp_status().equals("C")) {
            tv_againstcmp_status.setText("Completed");
            tv_againstcmp_status.setTextColor(ContextCompat.getColor(context, R.color.quantum_googgreen));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateComplaint(complaintList.get(i).getComp_id(),
                        complaintList.get(i).getComp_msg(),
                        complaintList.get(i).getComp_status());
            }
        });


        return view;
    }

    private void UpdateComplaint(int ID, String comp_msg, String comp_status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.comp_dialog, null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        TextView tv_comp_des = dialogView.findViewById(R.id.tv_comp_des);
        Button btn_complete = dialogView.findViewById(R.id.btn_complete);
        Button btn_accept = dialogView.findViewById(R.id.btn_accept);

        if (comp_status.equals("P")) {
            btn_complete.setVisibility(View.GONE);
        } else {
            btn_complete.setVisibility(View.VISIBLE);
        }
        if (comp_status.equals("IP")) {
            btn_accept.setVisibility(View.GONE);
        } else {
            btn_complete.setVisibility(View.VISIBLE);
        }
        if (comp_status.equals("C")) {
            btn_accept.setVisibility(View.GONE);
            btn_complete.setVisibility(View.GONE);
        }
        final AlertDialog alertDialognearby = builder.create();
        alertDialognearby.show();

        tv_comp_des.setText(comp_msg);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatus(ID, "IP");
            }
        });

        btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateStatus(ID, "C");
            }
        });


    }

    private void UpdateStatus(int id, String status) {
        complaint = new Complaint();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateComplainStatusService service = RetrofitClient.getClient().create(UpdateComplainStatusService.class);
        Call<Complaint> call = service.updateStatus(id, status);
        call.enqueue(new Callback<Complaint>() {
            @Override
            public void onResponse(Call<Complaint> call, Response<Complaint> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    complaint = response.body();
                    if (!complaint.isError()) {
                        Toast.makeText(context, complaint.getMessage(), Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, AdminAllComplainActivity.class));
                    } else {
                        Toast.makeText(context, complaint.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Complaint> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
