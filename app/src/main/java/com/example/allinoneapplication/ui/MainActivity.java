package com.example.allinoneapplication.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.admin.AdminDashboardActivity;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.customer.UserDrawerActivity;
import com.example.allinoneapplication.mechanic.MechanicDrawerActivity;
import com.example.allinoneapplication.workshop.WorkshopActivity;

public class MainActivity extends AppCompatActivity {
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tinyDB = new TinyDB(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tinyDB.getInt("LOGIN_PREF") == 1) {
                    startActivity(new Intent(MainActivity.this, UserDrawerActivity.class));
                    finish();
                } else if (tinyDB.getInt("LOGIN_PREF") == 2) {
                    startActivity(new Intent(MainActivity.this, MechanicDrawerActivity.class));
                    finish();
                } else if (tinyDB.getInt("LOGIN_PREF") == 3) {
                    startActivity(new Intent(MainActivity.this, WorkshopActivity.class));
                    finish();
                } else if(tinyDB.getInt("LOGIN_PREF")==4){
                    startActivity(new Intent(MainActivity.this, AdminDashboardActivity.class));
                    finish();

                }else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000);
    }
}