package com.example.allinoneapplication.mechanic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.allinoneapplication.R;
import com.google.android.material.tabs.TabLayout;

public class MOrderTablayoutActivity extends AppCompatActivity {

    private TabLayout M_Order_tab_id;
    private ViewPager m_order_view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morder_tablayout);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        M_Order_tab_id=findViewById(R.id.M_Order_tab_id);
        m_order_view_pager=findViewById(R.id.m_order_view_pager);
        M_Order_tab_id.setupWithViewPager(m_order_view_pager);
       MOVPAdapter movpAdapter=new MOVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        movpAdapter.addFragment(new MOFragment1(),"CHATS");
        movpAdapter.addFragment(new MOFragment2(),"STATUS");
        movpAdapter.addFragment(new MOFragment3(),"CALLS");
        m_order_view_pager.setAdapter(movpAdapter);
    }
}