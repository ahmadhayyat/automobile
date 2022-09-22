package com.example.allinoneapplication.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.mechanic.MechanicServiceActivity;
import com.example.allinoneapplication.mechanic.UpdateServiceActivity;
import com.example.allinoneapplication.model.Service;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.UpdateServiceStatusService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetServiceAdapter extends BaseAdapter {
    List<Service> serviceList;
    Context context;
    int check;
    LayoutInflater inflater;
    Service service;
    ProgressDialog progressDialog;

    public GetServiceAdapter(List<Service> serviceList, Context context, int check) {
        this.serviceList = serviceList;
        this.context = context;
        this.check = check;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.skill_item_adapter, viewGroup, false);
        TextView tv_skills_item = view.findViewById(R.id.tv_skills_item);
        TextView tv_skills_item2 = view.findViewById(R.id.tv_skills_item2);
        TextView tv_skills_item3 = view.findViewById(R.id.tv_skills_item3);
        ImageView service_update_img = view.findViewById(R.id.service_update_img);
        LinearLayout mechanic_service_LL = view.findViewById(R.id.mechanic_service_LL);
        SwitchCompat service_switch = view.findViewById(R.id.service_switch);
        if (check == 2) {
            mechanic_service_LL.setVisibility(View.GONE);
            tv_skills_item.setText(serviceList.get(i).getService_name());
            tv_skills_item2.setText(String.valueOf(serviceList.get(i).getService_price()));
            tv_skills_item3.setText(String.valueOf(serviceList.get(i).getService_discount()));
        } else if (check == 1) {
            tv_skills_item.setText(serviceList.get(i).getService_name());
            tv_skills_item2.setText(String.valueOf(serviceList.get(i).getService_price()));
            tv_skills_item3.setText(String.valueOf(serviceList.get(i).getService_discount()));
            if (serviceList.get(i).getService_status().equals("A")) {
                service_switch.setChecked(true);
            } else {
                service_switch.setChecked(false);
            }
            mechanic_service_LL.setVisibility(View.VISIBLE);
            service_update_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, UpdateServiceActivity.class)
                            .putExtra("SERVICE_ID", serviceList.get(i).getService_id())
                            .putExtra("SERVICE_NAME", serviceList.get(i).getService_name())
                            .putExtra("SERVICE_PRICE", serviceList.get(i).getService_price())
                            .putExtra("SERVICE_DISCOUNT", serviceList.get(i).getService_discount()));
                }
            });

            service_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        UpdateStatus(serviceList.get(i).getService_id(), "A");
                    } else {
                        UpdateStatus(serviceList.get(i).getService_id(), "NA");
                    }
                }
            });

        }
        return view;
    }

    private void UpdateStatus(int id, String status) {
        service = new Service();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateServiceStatusService _service = RetrofitClient.getClient().create(UpdateServiceStatusService.class);
        Call<Service> call = _service.updateServiceStatus(id, status);
        call.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    service = response.body();
                    if (!service.isError()) {
                        Toast.makeText(context, service.getMessage(), Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, MechanicServiceActivity.class));
                    } else {
                        Toast.makeText(context, service.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
