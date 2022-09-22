package com.example.allinoneapplication.customer;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.mechanic.MHFragment1;
import com.example.allinoneapplication.mechanic.MHFragment2;
import com.example.allinoneapplication.mechanic.MHFragment3;
import com.google.android.material.tabs.TabLayout;

public class CustomerHistoryTabLayout extends AppCompatActivity {
    private TabLayout C_tab_id;
    private ViewPager C_View_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_history_tab_layout);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        C_tab_id = findViewById(R.id.C_tab_id);
        C_View_pager = findViewById(R.id.C_View_pager);
        C_tab_id.setupWithViewPager(C_View_pager);
        CHVPAdapter chvpAdapter = new CHVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        chvpAdapter.addFragment(new CHFragnment1(), "Pending");
        chvpAdapter.addFragment(new CHFragnment2(), "Accepted");
        chvpAdapter.addFragment(new CHFragnment3(), "Completed");
        C_View_pager.setAdapter(chvpAdapter);
    }
}