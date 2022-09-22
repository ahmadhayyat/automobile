package com.example.allinoneapplication.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.allinoneapplication.R;

public class ReportsActivity extends AppCompatActivity {

    CardView manage_Mechanic_reports_CV, manage_customer_reports_CV,
            manage_booking_reports_CV, manage_complain_reports_CV, manage_workshop_reports_CV,
            manage_feedback_reports_CV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        manage_Mechanic_reports_CV = findViewById(R.id.manage_Mechanic_reports_CV);
        manage_customer_reports_CV = findViewById(R.id.manage_customer_reports_CV);
        manage_booking_reports_CV = findViewById(R.id.manage_booking_reports_CV);
        manage_feedback_reports_CV = findViewById(R.id.manage_feedback_reports_CV);
        manage_complain_reports_CV = findViewById(R.id.manage_complain_reports_CV);
        manage_workshop_reports_CV = findViewById(R.id.manage_workshop_reports_CV);
        manage_workshop_reports_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WorkshopReportActivity.class));
            }
        });
        manage_Mechanic_reports_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MechanicReportsActivity.class));
            }
        });

        manage_customer_reports_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewCustomerReportActivity.class));
            }
        });

        manage_booking_reports_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewBookingReportActivity.class));
            }
        });

        manage_complain_reports_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewComplainReportActivity.class));
            }
        });

        manage_feedback_reports_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ViewFeedbackReportActivity.class));
            }
        });
    }
}