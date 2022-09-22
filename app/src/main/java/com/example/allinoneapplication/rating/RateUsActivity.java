package com.example.allinoneapplication.rating;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.customer.UserDrawerActivity;
import com.example.allinoneapplication.model.RateFeedback;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.InsertRatingService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateUsActivity extends AppCompatActivity {

    TextView tv_skip;
    RateFeedback rateFeedback;
    ProgressDialog progressDialog;
    RatingBar rateBar;
    Button btn_send_rating;
    EditText edt_rating_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tv_skip = findViewById(R.id.tv_skip);
        rateBar = findViewById(R.id.rateBar);
        btn_send_rating = findViewById(R.id.btn_send_rating);
        edt_rating_body = findViewById(R.id.edt_rating_body);
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserDrawerActivity.class));
                finishAffinity();
            }
        });

        btn_send_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertRating();
            }
        });


    }

    private void InsertRating() {
        rateFeedback = new RateFeedback();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        InsertRatingService service = RetrofitClient.getClient().create(InsertRatingService.class);
        Call<RateFeedback> call = service.SendRating(getIntent().getIntExtra("FK_ID", 0),
                getIntent().getIntExtra("BOOKING_ID", 0),
                edt_rating_body.getText().toString(),
                rateBar.getRating());
        call.enqueue(new Callback<RateFeedback>() {
            @Override
            public void onResponse(Call<RateFeedback> call, Response<RateFeedback> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    rateFeedback = response.body();
                    if (!rateFeedback.isError()) {
                        Toast.makeText(RateUsActivity.this, rateFeedback.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), UserDrawerActivity.class));
                        finishAffinity();
                    } else {
                        Toast.makeText(RateUsActivity.this, rateFeedback.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RateFeedback> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(RateUsActivity.this,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
    }
}