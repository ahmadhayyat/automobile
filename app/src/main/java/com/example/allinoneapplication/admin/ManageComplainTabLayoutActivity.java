package com.example.allinoneapplication.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.allinoneapplication.R;
import com.google.android.material.tabs.TabLayout;

public class ManageComplainTabLayoutActivity extends AppCompatActivity {
private TabLayout tab_id;
private ViewPager view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_complain_tab_layout);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        tab_id=findViewById(R.id.tab_id);
        view_pager=findViewById(R.id.view_pager);

        tab_id.setupWithViewPager(view_pager);
        VPAdapter vpAdapter=new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new Frangment1(),"CHATS");
        vpAdapter.addFragment(new Fragment2(),"STATUS");
        vpAdapter.addFragment(new Fragment3(),"CALLS");
        view_pager.setAdapter(vpAdapter);
    }

}