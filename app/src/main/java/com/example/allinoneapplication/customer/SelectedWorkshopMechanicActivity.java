package com.example.allinoneapplication.customer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.WorkShopMechanicAdapter;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetworkshopMechanicService;
import com.example.allinoneapplication.workshop.ManageMechanicworkshopActivity;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedWorkshopMechanicActivity extends AppCompatActivity {

    ListView view_mechanic_workshop;
    ProgressDialog progressDialog;
    List<Mechanic> mechanicArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        setContentView(R.layout.activity_selected_workshop_mechanic);
        view_mechanic_workshop=findViewById(R.id.view_mechanic_workshop);
        getWorkshopMechanic();
    }
    public void getWorkshopMechanic() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetworkshopMechanicService service = RetrofitClient.getClient().create(GetworkshopMechanicService.class);

        Call<JsonObject> call = service.getWorkshopMechanic(getIntent().getIntExtra("WORKSHOP_ID",0));
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
                                    SelectedWorkshopMechanicActivity.this,1);
                            view_mechanic_workshop.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            view_mechanic_workshop.setAdapter(null);
                        }

                    } else {
                        Toast.makeText(SelectedWorkshopMechanicActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SelectedWorkshopMechanicActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectedWorkshopMechanicActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}