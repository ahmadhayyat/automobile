package com.example.allinoneapplication.admin;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.ComplainReportService;
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

public class ViewComplainReportActivity extends AppCompatActivity {

    List<Complaint> mechaniccomplaintList = new ArrayList<>();
    List<Complaint> customercomplaintList = new ArrayList<>();
    ProgressDialog progressDialog;
    int mechanicPending = 0;
    int mechanicApproved = 0;
    int mechanicComplete = 0;

    int customerPending = 0;
    int customerApproved = 0;
    int customerComplete = 0;
    PieChart pie_chart4, pie_chart5;
    TextView tv_mec_pending_com, tv_mec_approved_com, tv_mec_complete_com,
            tv_cus_pen_com, tv_cus_approved_com, tv_cus_complete_com;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complain_report);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        GetMechComplaint();
        GetCustComplaint();
        pie_chart4 = findViewById(R.id.pie_chart4);
        pie_chart5 = findViewById(R.id.pie_chart5);
        tv_mec_pending_com = findViewById(R.id.tv_mec_pending_com);
        tv_mec_approved_com = findViewById(R.id.tv_mec_approved_com);
        tv_mec_complete_com = findViewById(R.id.tv_mec_complete_com);
        tv_cus_pen_com = findViewById(R.id.tv_cus_pen_com);
        tv_cus_approved_com = findViewById(R.id.tv_cus_approved_com);
        tv_cus_complete_com = findViewById(R.id.tv_cus_complete_com);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToPiechartmechanic();
                addToPiechartcustomer();
            }
        }, 2000);

    }

    private void addToPiechartmechanic() {
        progressDialog.dismiss();
        if (mechanicPending == 0 && mechanicApproved == 0 && mechanicComplete == 0) {
            pie_chart4.setVisibility(View.GONE);
            tv_mec_pending_com.setVisibility(View.GONE);
            tv_mec_complete_com.setVisibility(View.GONE);
            tv_mec_approved_com.setVisibility(View.GONE);
        }
        pie_chart4.addPieSlice(new PieModel("Pending", mechanicPending, Color.parseColor("#00FF0A")));
        pie_chart4.addPieSlice(new PieModel("Accepted", mechanicApproved, Color.parseColor("#FF1100")));
        pie_chart4.addPieSlice(new PieModel("Completed", mechanicComplete, Color.parseColor("#FFC107")));
        pie_chart4.startAnimation();
        tv_mec_pending_com.setText("Pending Complain (" + mechanicPending + ")");
        tv_mec_approved_com.setText("Accepted Complain (" + mechanicApproved + ")");
        tv_mec_complete_com.setText("Completed complain (" + mechanicComplete + ")");
    }

    private void addToPiechartcustomer() {
        if (customerPending == 0 && customerApproved == 0 && customerComplete == 0) {
            pie_chart5.setVisibility(View.GONE);
            tv_cus_pen_com.setVisibility(View.GONE);
            tv_cus_approved_com.setVisibility(View.GONE);
            tv_cus_complete_com.setVisibility(View.GONE);
        }
        pie_chart5.addPieSlice(new PieModel("Pending", customerPending, Color.parseColor("#03A9F4")));
        pie_chart5.addPieSlice(new PieModel("Accepted", customerApproved, Color.parseColor("#9C27B0")));
        pie_chart5.addPieSlice(new PieModel("Completed", customerComplete, Color.parseColor("#F17BA3")));
        pie_chart5.startAnimation();
        tv_cus_pen_com.setText("Pending Complain (" + customerPending + ")");
        tv_cus_approved_com.setText("Accepted Complain (" + customerApproved + ")");
        tv_cus_complete_com.setText("Completed complain (" + customerComplete + ")");
    }

    private void GetMechComplaint() {
        customercomplaintList.clear();
        mechaniccomplaintList.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        ComplainReportService service = RetrofitClient.getClient().create(ComplainReportService.class);
        Call<JsonObject> call = service.getComplain();
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
                                if (data.getString("comp_stype").equals("c")) {
                                    if (data.getString("comp_status").equals("P")) {
                                        mechanicPending++;
                                    } else if (data.getString("comp_status").equals("IP")) {
                                        mechanicApproved++;
                                    } else if (data.getString("comp_status").equals("C")) {
                                        mechanicComplete++;
                                    }
                                    mechaniccomplaintList.add(new Complaint(
                                            data.getInt("comp_id"),
                                            data.getString("comp_stype"),
                                            data.getString("comp_status")
                                    ));

                                }
                            }


                        } catch (JSONException e) {
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

    private void GetCustComplaint() {
        customercomplaintList.clear();
        mechaniccomplaintList.clear();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        ComplainReportService service = RetrofitClient.getClient().create(ComplainReportService.class);
        Call<JsonObject> call = service.getComplain();
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
                                if (data.getString("comp_stype").equals("m")) {
                                    if (data.getString("comp_status").equals("P")) {
                                        customerPending++;
                                    } else if (data.getString("comp_status").equals("IP")) {
                                        customerApproved++;
                                    } else if (data.getString("comp_status").equals("C")) {
                                        customerComplete++;
                                    }
                                    customercomplaintList.add(new Complaint(
                                            data.getInt("comp_id"),
                                            data.getString("comp_stype"),
                                            data.getString("comp_status")
                                    ));

                                }


                            }

                        } catch (JSONException e) {
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

}