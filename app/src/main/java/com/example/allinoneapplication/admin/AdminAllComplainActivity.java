package com.example.allinoneapplication.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.AdminCompAdapter;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetAdminComplaintService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminAllComplainActivity extends AppCompatActivity {

    Spinner admin_comp_status;
    ListView admin_comp_LV;
    String cat_array[] = {"Select One Category", "Pending", "InProcess", "Completed"};
    List<Complaint> complaintList = new ArrayList<>();
    AdminCompAdapter adapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_complain);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        admin_comp_status = findViewById(R.id.admin_comp_status);
        admin_comp_LV = findViewById(R.id.admin_comp_LV);
        ArrayAdapter<String> arrayadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, cat_array);
        admin_comp_status.setAdapter(arrayadapter);
        admin_comp_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    GetComplaint("P");
                } else if (i == 2) {
                    GetComplaint("IP");
                } else if (i == 3) {
                    GetComplaint("C");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void GetComplaint(String status) {
        complaintList.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetAdminComplaintService service = RetrofitClient.getClient().create(GetAdminComplaintService.class);
        Call<JsonObject> call = service.getComplaints(status);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                complaintList.add(new Complaint(
                                        data.getString("name"),
                                        data.getString("p_name"),
                                        data.getInt("comp_id"),
                                        data.getString("comp_sub"),
                                        data.getString("comp_msg"),
                                        data.getString("comp_datetime"),
                                        data.getString("comp_num"),
                                        data.getString("comp_status")
                                ));

                            }

                            adapter = new AdminCompAdapter(complaintList, AdminAllComplainActivity.this);
                            admin_comp_LV.setAdapter(adapter);


                        } catch (JSONException e) {
                            admin_comp_LV.setAdapter(null);
                            e.printStackTrace();

                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), AdminDashboardActivity.class));
        finish();
    }
}