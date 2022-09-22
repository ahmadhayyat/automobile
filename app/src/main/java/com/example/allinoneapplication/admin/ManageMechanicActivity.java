package com.example.allinoneapplication.admin;

import android.app.ProgressDialog;
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
import com.example.allinoneapplication.adapter.ManageMechanicDetailsAdapter;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetManageMechanicDetailsServices;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMechanicActivity extends AppCompatActivity {

    ListView manage_mechanic_LV;
    ProgressDialog progressDialog;
    List<Mechanic> mechanicList = new ArrayList<>();
    Spinner check_status_spinner;
    String status[] = {"Pending", "Approved", "Block"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_mechanic);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        manage_mechanic_LV = findViewById(R.id.manage_mechanic_LV);
        check_status_spinner = findViewById(R.id.check_status_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, status);
        check_status_spinner.setAdapter(adapter);
        check_status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    GetManageMechanicdetails("P");
                } else if (i == 1) {
                    GetManageMechanicdetails("A");
                } else if (i == 2) {
                    GetManageMechanicdetails("B");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        GetManageMechanicdetails("P");
    }

    private void GetManageMechanicdetails(String status) {
        mechanicList.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetManageMechanicDetailsServices service = RetrofitClient.getClient().create(GetManageMechanicDetailsServices.class);

        Call<JsonObject> call = service.getManageMechanicdetails();
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
                                if (data.getString("mechanic_status").equals(status)) {
                                    mechanicList.add(new Mechanic(
                                            data.getInt("mechanic_id"),
                                            data.getString("mechanic_name"),
                                            data.getString("mechanic_address"),
                                            data.getString("mechanic_email"),
                                            data.getDouble("mechanic_lng"),
                                            data.getDouble("mechanic_lat"),
                                            data.getString("mechanic_profile_img"),
                                            data.getString("mehanic_contact"),
                                            data.getString("mechanic_status"),
                                            data.getString("mechanic_datetime"),
                                            data.getString("mechanic_cnic"),
                                            data.getString("vehicle_type"),
                                            data.getInt("mechanic_price")
                                    ));
                                }

                            }
                            ManageMechanicDetailsAdapter adapter = new ManageMechanicDetailsAdapter(mechanicList, ManageMechanicActivity.this);
                            manage_mechanic_LV.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            manage_mechanic_LV.setAdapter(null);
                        }

                    } else {
                        Toast.makeText(ManageMechanicActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ManageMechanicActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ManageMechanicActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

}