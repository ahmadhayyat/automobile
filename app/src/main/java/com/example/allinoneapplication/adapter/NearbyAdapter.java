package com.example.allinoneapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.NearBy;

import java.util.List;

public class NearbyAdapter extends BaseAdapter {

    List<NearBy> nearByList;
    Context context;
    LayoutInflater inflater;

    public NearbyAdapter(List<NearBy> nearByList, Context context) {
        this.nearByList = nearByList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return nearByList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.nearby_list_item, viewGroup, false);
        TextView tv_nearby_list = view.findViewById(R.id.tv_nearby_list);
        tv_nearby_list.setText(nearByList.get(i).getNa_title());
        return view;
    }
}
