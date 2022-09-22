package com.example.allinoneapplication.customer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.WorkHistoryAdapter;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetMechanicBookService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPreviousWorkActivity extends AppCompatActivity {

    ListView previous_work_hostory_LV;
    ProgressDialog progressDialog;
    List<Booking> bookingList = new ArrayList<>();
    TextView tv_no_previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_previous_work);
        previous_work_hostory_LV = findViewById(R.id.previous_work_hostory_LV);
        tv_no_previous = findViewById(R.id.tv_no_previous);
        GetBookings();
    }

    private void GetBookings() {
        if (bookingList != null) {
            bookingList.clear();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetMechanicBookService service = RetrofitClient.getClient().create(GetMechanicBookService.class);
        Call<JsonObject> call = service.getBookings(getIntent().getIntExtra("MECHANIC_ID", 0),
                "M", "C");
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

                        if (bookingList.size() != 0) {
                            tv_no_previous.setVisibility(View.GONE);
                            WorkHistoryAdapter adapter = new WorkHistoryAdapter(bookingList,
                                    ViewPreviousWorkActivity.this);
                            previous_work_hostory_LV.setAdapter(adapter);
                        } else {
                            tv_no_previous.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ViewPreviousWorkActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}