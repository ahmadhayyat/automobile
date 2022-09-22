package com.example.allinoneapplication.complain;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.ComplaintHistoryAdapter;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetComplaintService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccRejComplaintFragment extends Fragment {

    View view;
    TextView tv_no_ip_comp;
    RecyclerView ip_comp_Recycler;
    int code;
    List<Complaint> complaintList = new ArrayList<>();
    ProgressDialog progressDialog;
    ComplaintHistoryAdapter adapter;
    TinyDB tinyDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_acc_rej_complaint, container, false);
        tv_no_ip_comp = view.findViewById(R.id.tv_no_ip_comp);
        ip_comp_Recycler = view.findViewById(R.id.ip_comp_Recycler);
        tinyDB = new TinyDB(getContext());
        return view;
    }

    private void GetCompalint(int id, String s_type, String status) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (complaintList != null) {
            complaintList.clear();
        }

        GetComplaintService service = RetrofitClient.getClient().create(GetComplaintService.class);

        Call<JsonObject> call = service.getComplaint(id, s_type, status);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    code = response.code();

                    if (code == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                complaintList.add(new Complaint(
                                        data.getString("name"),
                                        data.getInt("comp_id"),
                                        data.getString("comp_sub"),
                                        data.getString("comp_msg"),
                                        data.getString("comp_datetime"),
                                        data.getString("comp_num"),
                                        data.getString("comp_status")));

                            }


                            if (complaintList.size() != 0) {
                                tv_no_ip_comp.setVisibility(View.GONE);
                                adapter = new ComplaintHistoryAdapter(complaintList, getActivity());
                                ip_comp_Recycler.setAdapter(adapter);
                                ip_comp_Recycler.setHasFixedSize(true);
                                ip_comp_Recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter.notifyDataSetChanged();
                            } else {
                                tv_no_ip_comp.setVisibility(View.VISIBLE);
                                ip_comp_Recycler.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (code == 404) {
                        Toast.makeText(getActivity(), "Server connectivity error!", Toast.LENGTH_SHORT).show();
                    } else if (code == 500) {
                        Toast.makeText(getActivity(), "Internal Server error!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),
                            "Response issue, Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        int loginPref = tinyDB.getInt("LOGIN_PREF");
        if (loginPref == 1) {
            int id = tinyDB.getInt("CUSTOMERID");
            GetCompalint(id, "c", "IP");
        } else if (loginPref == 2) {
            int id = tinyDB.getInt("MECHANIC_ID");
            GetCompalint(id, "m", "IP");
        }
    }

}