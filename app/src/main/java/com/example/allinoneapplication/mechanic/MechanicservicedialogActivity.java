package com.example.allinoneapplication.mechanic;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Service;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.CreateServiceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MechanicservicedialogActivity extends AppCompatActivity {

    TinyDB tinyDB;
    EditText edt_service_name, edt_services_charges, edt_discount;
    Button btn_create_service;
    Service service;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mechanicservicedialog);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        edt_service_name = findViewById(R.id.edt_service_name);
        edt_discount = findViewById(R.id.edt_discount);
        edt_services_charges = findViewById(R.id.edt_services_charges);
        btn_create_service = findViewById(R.id.btn_create_service);
        btn_create_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateService();
            }
        });
    }

    private void CreateService() {
        service = new Service();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        CreateServiceAPI serviceAPI = RetrofitClient.getClient().create(CreateServiceAPI.class);
        Call<Service> call = serviceAPI.createService(edt_service_name.getText().toString(),
                Integer.parseInt(edt_services_charges.getText().toString()),
                Integer.parseInt(edt_discount.getText().toString()),
                tinyDB.getInt("MECHANIC_ID"),
                "M");
        call.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    service = response.body();
                    if (!service.isError()) {
                        Toast.makeText(MechanicservicedialogActivity.this,
                                service.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MechanicServiceActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MechanicservicedialogActivity.this,
                                service.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MechanicservicedialogActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MechanicservicedialogActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MechanicServiceActivity.class));
        finish();
    }
}