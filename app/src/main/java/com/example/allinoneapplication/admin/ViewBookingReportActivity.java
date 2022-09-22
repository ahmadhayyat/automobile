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
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetBookReportService;
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

public class ViewBookingReportActivity extends AppCompatActivity {

    PieChart pie_chart;
    int approved = 0;
    int reject = 0;
    int pending = 0;
    int complete = 0;
    ProgressDialog progressDialog;
    List<Booking> bookingList = new ArrayList<>();
    TextView tv_approved_book, tv_pen_book, tv_rej_book, tv_comp_book;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_report);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        pie_chart = findViewById(R.id.pie_chart2);
        tv_approved_book = findViewById(R.id.tv_approved_book);
        tv_pen_book = findViewById(R.id.tv_pen_book);
        tv_rej_book = findViewById(R.id.tv_rej_book);
        tv_comp_book = findViewById(R.id.tv_comp_book);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetBookingdetails("A");
        GetBookingdetails("P");
        GetBookingdetails("R");
        GetBookingdetails("C");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addToPiechart();
            }
        }, 2000);


    }

    private void addToPiechart() {
        progressDialog.dismiss();
        pie_chart.addPieSlice(new PieModel("Approved", approved, Color.parseColor("#FF9800")));
        pie_chart.addPieSlice(new PieModel("Reject", reject, Color.parseColor("#FF1100")));
        pie_chart.addPieSlice(new PieModel("Pending", pending, Color.parseColor("#FFE500")));
        pie_chart.addPieSlice(new PieModel("Complete", complete, Color.parseColor("#00FF0A")));
        pie_chart.startAnimation();
        tv_approved_book.setText("IN-Process Bookings (" + approved + ")");
        tv_comp_book.setText("Complete Bookings (" + complete + ")");
        tv_pen_book.setText("Pending Bookings (" + pending + ")");
        tv_rej_book.setText("Reject Bookings (" + reject + ")");
    }

    private void GetBookingdetails(String status) {
        bookingList.clear();

        GetBookReportService service = RetrofitClient.getClient().create(GetBookReportService.class);

        Call<JsonObject> call = service.getBookReports();
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
                                if (data.getString("booking_status").equals(status)) {
                                    bookingList.add(new Booking(
                                            data.getString("booking_status")
                                    ));
                                }
                            }
                            if (status.equals("A")) {
                                approved = bookingList.size();
                                bookingList.clear();
                            } else if (status.equals("P")) {
                                pending = bookingList.size();
                                bookingList.clear();
                            } else if (status.equals("R")) {
                                reject = bookingList.size();
                                bookingList.clear();
                            } else if (status.equals("C")) {
                                complete = bookingList.size();
                                bookingList.clear();
                            }


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

}