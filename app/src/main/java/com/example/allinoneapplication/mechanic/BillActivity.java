package com.example.allinoneapplication.mechanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.UpdateBookService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {

    Booking booking;
    ProgressDialog progressDialog;
    Button btn_submit_bill;
    EditText edt_discount;
    int discount = 0;
    TextView tv_cname_id, tv_bookingid, tv_payment, tv_additional_payment, tv_total, tv_sub_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tv_cname_id = findViewById(R.id.tv_cname_id);
        tv_bookingid = findViewById(R.id.tv_bookingid);
        tv_payment = findViewById(R.id.tv_payment);
        tv_total = findViewById(R.id.tv_total);
        edt_discount = findViewById(R.id.edt_discount);
        tv_additional_payment = findViewById(R.id.tv_additional_payment);
        tv_sub_total = findViewById(R.id.tv_sub_total);
        btn_submit_bill = findViewById(R.id.btn_submit_bill);

        tv_cname_id.setText(getIntent().getStringExtra("CUSTOMER_NAME"));
        tv_bookingid.setText(getIntent().getStringExtra("BOOKING_NUM"));
        tv_payment.setText(String.valueOf(getIntent().getIntExtra("BOOKING_FEE", 0)));
        tv_additional_payment.setText(String.valueOf(getIntent().getIntExtra("ADDITIONAL_PRICE", 0)));

        tv_total.setText(String.valueOf((getIntent().getIntExtra("BOOKING_FEE", 0) + getIntent().getIntExtra("ADDITIONAL_PRICE", 0))));

        btn_submit_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_discount.getText().toString().isEmpty()) {
                    discount = 0;
                } else {
                    discount = Integer.parseInt(edt_discount.getText().toString());
                }
                UpdateBook(getIntent().getIntExtra("BOOKING_ID", 0),
                        "C",
                        getIntent().getIntExtra("ADDITIONAL_PRICE", 0),
                        getIntent().getStringExtra("ADDITIONAL_SERVICE"),
                        discount);
            }
        });

        tv_sub_total.setText(String.valueOf((tv_total.getText().toString())));

        edt_discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (!tv_total.getText().toString().equals("0")) {
                        double total = Double.parseDouble(tv_total.getText().toString());
                        double calculatePercent = (total - (total * (Double.parseDouble(editable.toString()) / 100)));
                        tv_sub_total.setText(String.valueOf((calculatePercent)));
                    } else {
                        tv_sub_total.setText(String.valueOf((tv_total.getText().toString())));
                    }
                }
            }
        });

    }

    private void UpdateBook(int BID, String status, int additionalpay, String serviceName, int Dis) {
        booking = new Booking();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        int totalamount = additionalpay + 150;

        UpdateBookService service = RetrofitClient.getClient().create(UpdateBookService.class);
        Call<Booking> call = service.updateBooking(BID, status, additionalpay, totalamount, serviceName, Dis);
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    booking = response.body();
                    if (!booking.isError()) {
                        Toast.makeText(getApplicationContext(),
                                booking.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MechanicDrawerActivity.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                booking.getMessage(), Toast.LENGTH_SHORT).show();
                    }
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