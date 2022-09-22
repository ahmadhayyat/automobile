package com.example.allinoneapplication.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.allinoneapplication.R;
import com.example.allinoneapplication.complain.ComplainActivity;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.model.Tracking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetMechLocService;
import com.example.allinoneapplication.service.UpdateCancelBookingService;
import com.example.allinoneapplication.tracking.MapDirectionActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerMechBookingDetailActivity extends AppCompatActivity {

    CircleImageView cusmechbooking_det_picture;
    TextView cusmechtv_name, cusmechtv_contact, cusmechtv_booking_num, cusmechtv_booking_datetime,
            cusmechtv_booking_fee, cusmechtv_booking_description;
    Button cusmechbtn_complain, cusmechbtn_chat, cusmechbtn_tracking, cusmechbtn_cancelbooking;
    int getMID, getBID;
    Tracking tracking;
    double latitude, longitude;
    Booking booking;
    ProgressDialog progressDialog;
ImageButton whatsApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mech_booking_detail);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        cusmechbooking_det_picture = findViewById(R.id.cusmechbooking_det_picture);
        cusmechtv_name = findViewById(R.id.cusmechtv_name);
        cusmechtv_booking_num = findViewById(R.id.cusmechtv_booking_num);
        cusmechtv_booking_datetime = findViewById(R.id.cusmechtv_booking_datetime);
        cusmechtv_booking_fee = findViewById(R.id.cusmechtv_booking_fee);
        cusmechtv_contact = findViewById(R.id.cusmechtv_contact);
        cusmechtv_booking_description = findViewById(R.id.cusmechtv_booking_description);
        cusmechbtn_complain = findViewById(R.id.cusmechbtn_complain);
        cusmechbtn_chat = findViewById(R.id.cusmechbtn_chat);
        cusmechbtn_cancelbooking = findViewById(R.id.cusmechbtn_cancelbooking);
        cusmechbtn_tracking = findViewById(R.id.cusmechbtn_tracking);
        getMID = getIntent().getIntExtra("MECHANIC_ID", 0);
        getBID = getIntent().getIntExtra("BOOKING_ID", 0);
        Glide.with(this)
                .load(EndPoint.IMAGE_URL + getIntent().getStringExtra("MECHANIC_PROFILE"))
                .placeholder(R.drawable.account)
                .into(cusmechbooking_det_picture);
        cusmechtv_name.setText(getIntent().getStringExtra("MECHANIC_NAME"));
        cusmechtv_contact.setText(getIntent().getStringExtra("MECHANIC_CONTACT"));
        cusmechtv_booking_num.setText(getIntent().getStringExtra("BOOKING_NUM"));
        cusmechtv_booking_datetime.setText(getIntent().getStringExtra("BOOKING_DATE"));
        cusmechtv_booking_description.setText(getIntent().getStringExtra("BOOKING_DES"));
        cusmechtv_booking_fee.setText(String.valueOf(getIntent().getIntExtra("BOOKING_FEE", 0)));
        GetLocation(getMID);

        if (getIntent().getStringExtra("BOOKING_STATUS").equals("P")) {
            cusmechbtn_chat.setVisibility(View.GONE);
            cusmechbtn_complain.setVisibility(View.GONE);
            cusmechbtn_tracking.setVisibility(View.GONE);
            cusmechbtn_cancelbooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateBooking("CA", getBID);
                }
            });
        } else if (getIntent().getStringExtra("BOOKING_STATUS").equals("A")) {
            cusmechbtn_chat.setVisibility(View.VISIBLE);
            cusmechbtn_complain.setVisibility(View.VISIBLE);
            cusmechbtn_tracking.setVisibility(View.VISIBLE);
            cusmechbtn_cancelbooking.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("BOOKING_STATUS").equals("C")) {
            cusmechbtn_chat.setVisibility(View.GONE);
            cusmechbtn_complain.setVisibility(View.GONE);
            cusmechbtn_tracking.setVisibility(View.GONE);
            cusmechbtn_cancelbooking.setVisibility(View.GONE);
        }


        cusmechbtn_tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent seeDirection = new Intent(CustomerMechBookingDetailActivity.this,
                        MapDirectionActivity.class);
                seeDirection.putExtra("MECHANIC_LATITUDE", latitude);
                seeDirection.putExtra("MECHANIC_LONGITUDE", longitude);
                seeDirection.putExtra("MECHANIC_NAME",
                        getIntent().getStringExtra("MECHANIC_NAME"));
                seeDirection.putExtra("TRACK_USER_ID", getMID);
                seeDirection.putExtra("CHECK", 1);
                startActivity(seeDirection);
            }
        });

        cusmechbtn_complain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),
                        ComplainActivity.class)
                        .putExtra("MECHANIC_ID", getMID)
                        .putExtra("MECHANIC_NAME", getIntent().getStringExtra("MECHANIC_NAME"))
                );

            }
        });
    whatsApp = findViewById(R.id.cusmechbtn_whatssapp);
    whatsApp.setOnClickListener(view -> {
        String number = getIntent().getStringExtra("MECHANIC_CONTACT");
        String url = "https://api.whatsapp.com/send?phone="+number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    });
    }

    public void GetLocation(int mID) {
        tracking = new Tracking();
        GetMechLocService service = RetrofitClient.getClient().create(GetMechLocService.class);
        Call<Tracking> call = service.getLocation(mID);
        call.enqueue(new Callback<Tracking>() {
            @Override
            public void onResponse(Call<Tracking> call, Response<Tracking> response) {
                if (response.isSuccessful()) {
                    tracking = response.body();
                    if (!tracking.isError()) {
                        Toast.makeText(getApplicationContext(),
                                tracking.getMessage(), Toast.LENGTH_SHORT).show();
                        latitude = tracking.getCl_latitude();
                        longitude = tracking.getCl_longitude();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                tracking.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Tracking> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void UpdateBooking(String status, int BID) {
        booking = new Booking();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait....");
        progressDialog.show();
        UpdateCancelBookingService service = RetrofitClient.getClient().create(UpdateCancelBookingService.class);
        Call<Booking> call = service.updatecancelbookingStatus(BID, status);
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    booking = response.body();
                    if (!booking.isError()) {
                        Toast.makeText(getApplicationContext(),
                                booking.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), CustomerHistoryTabLayout.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                booking.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}