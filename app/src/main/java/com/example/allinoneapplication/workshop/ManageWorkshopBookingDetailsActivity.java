package com.example.allinoneapplication.workshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.ComplaintHistoryAdapter;
import com.example.allinoneapplication.adapter.UpcomingBookingAdapter;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.mechanic.MechanicDrawerActivity;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.model.Complaint;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetComplaintService;
import com.example.allinoneapplication.service.GetMechanicBookService;
import com.example.allinoneapplication.service.GetWorkshopMechCompService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageWorkshopBookingDetailsActivity extends AppCompatActivity {

    TextView tv_pending, tv_active, tv_completed, tv_mcomplains;
    ProgressDialog progressDialog;
    TinyDB tinyDB;
    List<Complaint> complaintList = new ArrayList<>();
    List<Booking> bookingList = new ArrayList<>();
    int code;
    int totalComplaint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_workshop_booking_details);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        tv_pending = findViewById(R.id.tv_pending);
        tv_active = findViewById(R.id.tv_active);
        tv_completed = findViewById(R.id.tv_completed);
        tv_mcomplains = findViewById(R.id.tv_mcomplains);
        GetMechBookings(getIntent().getIntExtra("MECHANIC_ID", 0), "P");
        GetMechBookings(getIntent().getIntExtra("MECHANIC_ID", 0), "A");
        GetMechBookings(getIntent().getIntExtra("MECHANIC_ID", 0), "C");
        GetCompalint(getIntent().getIntExtra("MECHANIC_ID", 0));
    }

    private void GetCompalint(int id) {

        GetWorkshopMechCompService service = RetrofitClient.getClient().create(GetWorkshopMechCompService.class);

        Call<JsonObject> call = service.getComp(id);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    code = response.code();

                    if (code == 200) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                complaintList.add(new Complaint(
                                        data.getString("comp_status")));

                            }


                            if (complaintList.size() != 0) {
                                tv_mcomplains.setText(complaintList.size()+ " Complaints");
                            } else {
                                totalComplaint = totalComplaint + 0;
                                tv_mcomplains.setText("No Complaints Yet");
                            }
                        } catch (JSONException e) {
                            tv_mcomplains.setText("No Complaints Yet");

                            e.printStackTrace();
                        }
                    } else if (code == 404) {
                        Toast.makeText(getApplicationContext(), "Server connectivity error!", Toast.LENGTH_SHORT).show();
                    } else if (code == 500) {
                        Toast.makeText(getApplicationContext(), "Internal Server error!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Response issue, Try Again Later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetMechBookings(int ID, String status) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetMechanicBookService service = RetrofitClient.getClient().create(GetMechanicBookService.class);
        Call<JsonObject> call = service.getBookings(ID, "M", status);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                        JSONArray jsonArray = jsonObject.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);

                            bookingList.add(new Booking(data.getInt("customer_id"),
                                    data.getString("customer_name"),
                                    data.getString("customer_contact"),
                                    data.getString("customer_profile_img"),
                                    data.getInt("booking_id"),
                                    data.getString("booking_num"),
                                    data.getString("booking_datetime"),
                                    data.getInt("booking_fee"),
                                    data.getString("booking_description"),
                                    data.getString("booking_status")
                            ));

                        }
                        if (bookingList.size() == 0) {
                            if (status.equals("P")) {
                                tv_pending.setText("No Pending Bookings Yet");
                            } else if (status.equals("A")) {
                                tv_active.setText("No Active Bookings Yet");
                            } else if (status.equals("C")) {
                                tv_completed.setText("No Completed Bookings Yet");
                            }

                        } else {
                            if (status.equals("P")) {
                                tv_pending.setText(bookingList.size() + " Pending Bookings");
                            } else if (status.equals("A")) {
                                tv_active.setText(bookingList.size() + " Active Bookings");
                            } else if (status.equals("C")) {
                                tv_completed.setText(bookingList.size() + " Completed Bookings");
                            }


                        }

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        if (status.equals("P")) {
                            tv_pending.setText("No Pending Bookings Yet");
                        } else if (status.equals("A")) {
                            tv_active.setText("No Active Bookings Yet");
                        } else if (status.equals("C")) {
                            tv_completed.setText("No Completed Bookings Yet");
                        }

                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
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