package com.example.allinoneapplication.complain;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.ComplaintHistoryVPAdapter;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.customer.UserDrawerActivity;
import com.example.allinoneapplication.mechanic.MechanicDrawerActivity;
import com.google.android.material.tabs.TabLayout;

public class CusMechComplaintDetailActivity extends AppCompatActivity {

    ComplaintHistoryVPAdapter adapter;
    TabLayout comp_hisDetailsTabLayout;
    ViewPager comp_hisviewPager;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_mech_complaint_detail);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        comp_hisDetailsTabLayout = findViewById(R.id.comp_hisDetailsTabLayout);
        comp_hisviewPager = findViewById(R.id.comp_hisviewPager);
        tinyDB = new TinyDB(this);
        MakeTabLayout();
    }

    private void MakeTabLayout() {
        comp_hisDetailsTabLayout.addTab(comp_hisDetailsTabLayout.newTab().setText("Pending"));
        comp_hisDetailsTabLayout.addTab(comp_hisDetailsTabLayout.newTab().setText("Accepted and Rejected"));
        comp_hisDetailsTabLayout.addTab(comp_hisDetailsTabLayout.newTab().setText("Completed"));
        comp_hisDetailsTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new ComplaintHistoryVPAdapter(CusMechComplaintDetailActivity.this, getSupportFragmentManager(),
                comp_hisDetailsTabLayout.getTabCount());
        comp_hisviewPager.setAdapter(adapter);

        comp_hisviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(comp_hisDetailsTabLayout));

        comp_hisDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                comp_hisviewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (tinyDB.getInt("LOGIN_PREF") == 1) {
            startActivity(new Intent(CusMechComplaintDetailActivity.this, UserDrawerActivity.class));
            finish();
        } else if (tinyDB.getInt("LOGIN_PREF") == 2) {
            startActivity(new Intent(CusMechComplaintDetailActivity.this, MechanicDrawerActivity.class));
            finish();
        }
    }


}