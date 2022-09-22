package com.example.allinoneapplication.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.rating.RateUsActivity;

public class CustomerBillActivity extends AppCompatActivity {

    TextView tvcust_cname_id, tvcust_bookingid, tvcust_payment, tvcust_additional_payment,
            tvcust_total, tvcust_discount, tvcust_sub_total;
    TinyDB tinyDB;
    Button btn_rate_us;
    int getMID, getBID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        setContentView(R.layout.activity_customer_bill);
        tinyDB = new TinyDB(this);
        tvcust_cname_id = findViewById(R.id.tvcust_cname_id);
        tvcust_bookingid = findViewById(R.id.tvcust_bookingid);
        tvcust_total = findViewById(R.id.tvcust_total);
        tvcust_payment = findViewById(R.id.tvcust_payment);
        tvcust_additional_payment = findViewById(R.id.tvcust_additional_payment);
        tvcust_discount = findViewById(R.id.tvcust_discount);
        tvcust_sub_total = findViewById(R.id.tvcust_sub_total);
        btn_rate_us = findViewById(R.id.btn_rate_us);
        getMID = getIntent().getIntExtra("MECHANIC_ID", 0);
        getBID = getIntent().getIntExtra("BOOKING_ID", 0);
        tvcust_cname_id.setText(tinyDB.getString("CUSTOMER_NAME"));
        tvcust_bookingid.setText(getIntent().getStringExtra("BOOKING_NUM"));
        tvcust_payment.setText(String.valueOf(getIntent().getIntExtra("BOOKING_FEE", 0)));
        tvcust_discount.setText(String.valueOf(getIntent().getIntExtra("DISCOUNT", 0)));
//        tvcust_total.setText(String.valueOf(getIntent().getIntExtra("TOTAL_PRICE", 0)));
        tvcust_additional_payment.setText(String.valueOf(getIntent().getIntExtra("ADDITIONAL_PRICE", 0)));

        tvcust_total.setText(String.valueOf((getIntent().getIntExtra("ADDITIONAL_PRICE", 0)
                + getIntent().getIntExtra("BOOKING_FEE", 0))));

        double total = Double.parseDouble(tvcust_total.getText().toString());
        double calculatePercent = total - (total * (Double.parseDouble(tvcust_discount.getText().toString())) / 100);
        tvcust_sub_total.setText(String.valueOf((calculatePercent)));
        btn_rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RateUsActivity.class)
                        .putExtra("FK_ID", getMID)
                        .putExtra("BOOKING_ID", getBID));

            }
        });
    }
}