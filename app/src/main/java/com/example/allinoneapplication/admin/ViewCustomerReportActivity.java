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
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetManageUserdetailService;
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

public class ViewCustomerReportActivity extends AppCompatActivity {

    PieChart pie_chart;
    int approved = 0;
    int blocked = 0;
    ProgressDialog progressDialog;
    TextView tv_approved_cust, tv_block_customer;
    List<Customer> customerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_customer_report);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        pie_chart = findViewById(R.id.pie_chart1);
        tv_approved_cust = findViewById(R.id.tv_approved_cust);
        tv_block_customer = findViewById(R.id.tv_block_customer);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.show();
        GetManageUserdetails("A");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToPiechart();
            }
        }, 2000);
    }


    private void addToPiechart() {
        progressDialog.dismiss();
        pie_chart.addPieSlice(new PieModel("Approved", approved, Color.parseColor("#00FF0A")));
        pie_chart.addPieSlice(new PieModel("Blocked", blocked, Color.parseColor("#FF1100")));
        pie_chart.startAnimation();
        tv_approved_cust.setText("Approved Customer (" + approved + ")");
        tv_block_customer.setText("Blocked Customer (" + blocked + ")");
    }

    private void GetManageUserdetails(String status) {
        customerList.clear();

        GetManageUserdetailService service = RetrofitClient.getClient().create(GetManageUserdetailService.class);

        Call<JsonObject> call = service.getManageUserdetails();
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
                                if (data.getString("customer_status").equals(status)) {
                                    customerList.add(new Customer(
                                            data.getInt("customer_id"),
                                            data.getString("customer_name"),
                                            data.getString("customer_email"),
                                            data.getString("customer_contact"),
                                            data.getDouble("customer_lat"),
                                            data.getDouble("customer_lng"),
                                            data.getString("customer_address"),
                                            data.getString("customer_profile_img"),
                                            data.getString("customer_status"),
                                            data.getString("created_datetime")
                                    ));
                                }
                            }
                            if (status.equals("A")) {
                                approved = customerList.size();
                                customerList.clear();
                            } else if (status.equals("B")) {
                                blocked = customerList.size();
                                customerList.clear();
                            }
                            GetManageUserdetails("B");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                response.message(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        approved = 0;
        blocked = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        approved = 0;
        blocked = 0;

    }
}