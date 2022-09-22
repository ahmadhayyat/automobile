package com.example.allinoneapplication.admin;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.Workshop;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetWorkshopDetailsService;
import com.google.gson.JsonObject;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkshopReportActivity extends AppCompatActivity {
    PieChart pie_chartw;
    int pending = 0;
    int approved = 0;
    int blocked = 0;
    ProgressDialog progressDialog;
    List<Workshop> workshopList = new ArrayList<>();
    TextView tv_pending_ws, tv_approved_ws, tv_block_ws;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_report);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        pie_chartw = findViewById(R.id.pie_chartw);
        tv_pending_ws = findViewById(R.id.tv_pending_ws);
        tv_approved_ws = findViewById(R.id.tv_approved_ws);
        tv_block_ws = findViewById(R.id.tv_block_ws);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.show();
        GetManageWorkshopdetails("P");
        GetManageWorkshopdetails("B");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToPiechart();
            }
        }, 2000);
    }

    private void addToPiechart() {
        progressDialog.dismiss();
        pie_chartw.addPieSlice(new PieModel("Pending", pending, Color.parseColor("#494547")));
        pie_chartw.addPieSlice(new PieModel("Approved", approved, Color.parseColor("#00FF0A")));
        pie_chartw.addPieSlice(new PieModel("Blocked", blocked, Color.parseColor("#FF1100")));
        pie_chartw.startAnimation();
        tv_pending_ws.setText("Pending Workshop (" + pending + ")");
        tv_approved_ws.setText("Approved Workshop (" + approved + ")");
        tv_block_ws.setText("Blocked Workshop (" + blocked + ")");
    }

    private void GetManageWorkshopdetails(String status) {
        workshopList.clear();

        GetWorkshopDetailsService service = RetrofitClient.getClient().create(GetWorkshopDetailsService.class);

        Call<JsonObject> call = service.getManageWorkshopdetails();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
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
                            if (status.equals("A")) {
                                approved = workshopList.size();
                                workshopList.clear();
                            } else if (status.equals("B")) {
                                blocked = workshopList.size();
                                workshopList.clear();
                            } else if (status.equals("P")) {
                                pending = workshopList.size();
                                workshopList.clear();
                            }
                            GetManageWorkshopdetails("A");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(WorkshopReportActivity.this,
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(WorkshopReportActivity.this,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(WorkshopReportActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        pending = 0;
        approved = 0;
        blocked = 0;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pending = 0;
        approved = 0;
        blocked = 0;

    }
}