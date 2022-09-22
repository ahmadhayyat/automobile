package com.example.allinoneapplication.workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.WorkShopMechanicAdapter;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetworkshopMechanicService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageMechanicworkshopActivity extends AppCompatActivity {

    Button btn_add_mechanic;
    ListView added_mechanic_workshop;
    TinyDB tinyDB;
    ProgressDialog progressDialog;
    List<Mechanic> mechanicArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_mechanicworkshop);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        btn_add_mechanic = findViewById(R.id.btn_add_mechanic);
        added_mechanic_workshop = findViewById(R.id.added_mechanic_workshop);
        tinyDB = new TinyDB(this);
        btn_add_mechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManageMechanicworkshopActivity.this,
                        AddMechanicActivity.class));
            }
        });
        getWorkshopMechanic();
    }

    public void getWorkshopMechanic() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetworkshopMechanicService service = RetrofitClient.getClient().create(GetworkshopMechanicService.class);

        Call<JsonObject> call = service.getWorkshopMechanic(tinyDB.getInt("WMECHANIC_ID"));
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

                                mechanicArrayList.add(new Mechanic(
                                        data.getInt("mechanic_id"),
                                        data.getString("mechanic_name"),
                                        data.getString("mechanic_email"),
                                        data.getString("mechanic_profile_img"),
                                        data.getString("mehanic_contact"),
                                        data.getString("mechanic_status"),
                                        data.getString("mechanic_cnic"),
                                        data.getString("vehicle_type")
                                ));
                            }

                            WorkShopMechanicAdapter adapter = new WorkShopMechanicAdapter(mechanicArrayList,
                                    ManageMechanicworkshopActivity.this,2);
                            added_mechanic_workshop.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(ManageMechanicworkshopActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ManageMechanicworkshopActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ManageMechanicworkshopActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}