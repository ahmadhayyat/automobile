package com.example.allinoneapplication.admin;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.ManageMechanicDetailsAdapter;
import com.example.allinoneapplication.adapter.ManageWorkshopDetailsAdapter;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetManageMechanicDetailsServices;
import com.example.allinoneapplication.service.GetWorkshopDetailsService;
import com.google.gson.JsonObject;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageWorkshopActivity extends AppCompatActivity {
    ListView manage_workshop_LV;
    ProgressDialog progressDialog;
    List<Workshop> workshopList = new ArrayList<>();
    Spinner check_mstatus_spinner;
    String status[] = {"Pending", "Approved", "Block"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workshop);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        manage_workshop_LV = findViewById(R.id.manage_workshop_LV);
        check_mstatus_spinner = findViewById(R.id.check_mstatus_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, status);
        check_mstatus_spinner.setAdapter(adapter);
        check_mstatus_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    GetManageWorkshopdetails("P");
                } else if (i == 1) {
                    GetManageWorkshopdetails("A");
                } else if (i == 2) {
                    GetManageWorkshopdetails("B");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        GetManageWorkshopdetails("P");
    }

    private void GetManageWorkshopdetails(String status) {
        workshopList.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetWorkshopDetailsService service = RetrofitClient.getClient().create(GetWorkshopDetailsService.class);

        Call<JsonObject> call = service.getManageWorkshopdetails();
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
                                if (data.getString("wmechanic_status").equals(status)) {
                                    workshopList.add(new Workshop(
                                            data.getInt("wmechanic_id"),
                                            data.getString("wmechanic_name"),
                                            data.getString("wmechanic_address"),
                                            data.getString("wmechanic_email"),
                                            data.getDouble("wmechanic_lng"),
                                            data.getDouble("wmechanic_lat"),
                                            data.getString("wmechanic_profile_img"),
                                            data.getString("wmechanic_contact"),
                                            data.getString("wmechanic_status"),
                                            data.getString("wmechanic_datetime"),
                                            data.getString("wmechanic_cnic"),
                                            data.getString("vehicle_type")
                                    ));
                                }
                            }
                            ManageWorkshopDetailsAdapter adapter = new ManageWorkshopDetailsAdapter(workshopList, ManageWorkshopActivity.this);
                            manage_workshop_LV.setAdapter(adapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(ManageWorkshopActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ManageWorkshopActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ManageWorkshopActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}