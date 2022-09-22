package com.example.allinoneapplication.mechanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.allinoneapplication.R;
import com.google.android.material.tabs.TabLayout;

public class MHistoryTablayoutActivity extends AppCompatActivity {
    private TabLayout M_tab_id;
    private ViewPager m_view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhistory_tablayout);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        M_tab_id = findViewById(R.id.M_tab_id);
        m_view_pager = findViewById(R.id.m_view_pager);
        M_tab_id.setupWithViewPager(m_view_pager);
        MHVPAdapter mhvpAdapter = new MHVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mhvpAdapter.addFragment(new MHFragment1(), "Accepted");
        mhvpAdapter.addFragment(new MHFragment2(), "Rejected");
        mhvpAdapter.addFragment(new MHFragment3(), "Completed");
        m_view_pager.setAdapter(mhvpAdapter);
    }
}