package com.example.allinoneapplication.customer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class CHVPAdapter extends FragmentPagerAdapter {
    private final ArrayList<Fragment> FragmentArrayList = new ArrayList<>();
    private final ArrayList<String> FragmentTitle = new ArrayList<>();

    public CHVPAdapter(@NonNull FragmentManager fm, int behaviorResumeOnlyCurrentFragment) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return FragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        FragmentArrayList.add(fragment);
        FragmentTitle.add(title);
    }
    @NonNull
    @Override
    public CharSequence getPageTitle(int position)
    {
        return FragmentTitle.get(position);
    }
}
