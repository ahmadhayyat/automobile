package com.example.allinoneapplication.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.allinoneapplication.complain.AccRejComplaintFragment;
import com.example.allinoneapplication.complain.CompletedComplaintFragment;
import com.example.allinoneapplication.complain.PendingComplaintFragment;


public class ComplaintHistoryVPAdapter extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public ComplaintHistoryVPAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PendingComplaintFragment pendingComplaintFragment=new PendingComplaintFragment();
                return  pendingComplaintFragment;
            case 1:
                AccRejComplaintFragment accRejComplaintFragment=new AccRejComplaintFragment();
                return accRejComplaintFragment;
            case 2:
                CompletedComplaintFragment completedComplaintFragment=new CompletedComplaintFragment();
                return completedComplaintFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

}

