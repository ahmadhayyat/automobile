package com.example.allinoneapplication.mechanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.GetServiceAdapter;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Service;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetServiceRecordAPI;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MechanicServiceActivity extends AppCompatActivity {

    Button btn_service;
    TextView tv_no_skill_added;
    ListView manage_skills_LV;
    TinyDB tinyDB;
    ProgressDialog progressDialog;
    List<Service> serviceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanic_service);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        btn_service = findViewById(R.id.btn_service);
        tv_no_skill_added = findViewById(R.id.tv_no_skill_added);
        manage_skills_LV = findViewById(R.id.manage_skills_LV);
        tinyDB = new TinyDB(this);
        btn_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MechanicServiceActivity.this, MechanicservicedialogActivity.class));
                finish();
            }
        });
        getMechanicServices();
    }

    public void getMechanicServices() {
        serviceList.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetServiceRecordAPI service = RetrofitClient.getClient().create(GetServiceRecordAPI.class);

        Call<JsonObject> call = service.getServices(tinyDB.getInt("MECHANIC_ID"),
                "M");
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

                                serviceList.add(new Service(
                                        data.getInt("service_id"),
                                        data.getString("service_name"),
                                        data.getInt("service_price"),
                                        data.getInt("service_discount"),
                                        data.getString("service_status")
                                ));
                            }
                            if (serviceList.size() != 0) {
                                tv_no_skill_added.setVisibility(View.GONE);
                                GetServiceAdapter adapter = new GetServiceAdapter(serviceList,
                                        MechanicServiceActivity.this, 1);
                                manage_skills_LV.setAdapter(adapter);
                            } else {
                                tv_no_skill_added.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            tv_no_skill_added.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Toast.makeText(MechanicServiceActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MechanicServiceActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MechanicServiceActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MechanicDrawerActivity.class));
        finish();
    }
}