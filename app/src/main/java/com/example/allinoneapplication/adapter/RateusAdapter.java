package com.example.allinoneapplication.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.allinoneapplication.model.RateFeedback;

import java.util.List;

public class RateusAdapter extends BaseAdapter {
    List<RateFeedback> rateFeedbackList;
    Context context;
    LayoutInflater inflater;

    public RateusAdapter(List<RateFeedback> rateFeedbackList, Context context) {
        this.rateFeedbackList = rateFeedbackList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return rateFeedbackList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
