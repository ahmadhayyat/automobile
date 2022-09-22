package com.example.allinoneapplication.mechanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.Service;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.UpdateMechServiceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateServiceActivity extends AppCompatActivity {

    EditText edt_update_service_name, edt_update_service_price, edt_update_service_discount;
    Button btn_update_service;
    int getServiceID;
    Service service;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        edt_update_service_name = findViewById(R.id.edt_update_service_name);
        edt_update_service_price = findViewById(R.id.edt_update_service_price);
        edt_update_service_discount = findViewById(R.id.edt_update_service_discount);
        getServiceID = getIntent().getIntExtra("SERVICE_ID", 0);
        edt_update_service_name.setText(getIntent().getStringExtra("SERVICE_NAME"));
        edt_update_service_price.setText(String.valueOf(getIntent().getIntExtra("SERVICE_PRICE", 0)));
        edt_update_service_discount.setText(String.valueOf(getIntent().getIntExtra("SERVICE_DISCOUNT", 0)));
        btn_update_service = findViewById(R.id.btn_update_service);

        btn_update_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateService();
            }
        });

    }

    private void UpdateService() {
        service = new Service();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        UpdateMechServiceAPI _service = RetrofitClient.getClient().create(UpdateMechServiceAPI.class);
        Call<Service> call = _service.updateService(getServiceID, edt_update_service_name.getText().toString(),
                Integer.parseInt(edt_update_service_price.getText().toString()),
                Integer.parseInt(edt_update_service_discount.getText().toString()));
        call.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    service = response.body();
                    if (!service.isError()) {
                        Toast.makeText(getApplicationContext(),
                                service.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MechanicServiceActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                service.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MechanicServiceActivity.class));
        finish();
    }
}