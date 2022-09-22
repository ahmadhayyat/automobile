package com.example.allinoneapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.customer.CustomerMechBookingDetailActivity;
import com.example.allinoneapplication.model.MechanicBooking;

import java.util.List;

public class CustomerMechBookingAdapter extends BaseAdapter {

    List<MechanicBooking> mechanicBookingList;
    Context context;
    String status;
    LayoutInflater inflater;

    public CustomerMechBookingAdapter(List<MechanicBooking> mechanicBookingList, Context context, String status) {
        this.mechanicBookingList = mechanicBookingList;
        this.context = context;
        this.status = status;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mechanicBookingList.size();
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
        view = inflater.inflate(R.layout.item_cus_mec_booking, viewGroup, false);
        TextView cusmech_booking_num_item = view.findViewById(R.id.cusmech_booking_num_item);
        TextView cusmech_booking_date = view.findViewById(R.id.cusmech_booking_date);

        cusmech_booking_num_item.setText(mechanicBookingList.get(i).getBooking_num());
        cusmech_booking_date.setText(mechanicBookingList.get(i).getBooking_datetime());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, CustomerMechBookingDetailActivity.class)
                        .putExtra("MECHANIC_ID", mechanicBookingList.get(i).getMechanic_id())
                        .putExtra("MECHANIC_NAME", mechanicBookingList.get(i).getMechanic_name())
                        .putExtra("MECHANIC_CONTACT", mechanicBookingList.get(i).getMehanic_contact())
                        .putExtra("MECHANIC_PROFILE", mechanicBookingList.get(i).getMechanic_profile_img())
                        .putExtra("BOOKING_ID", mechanicBookingList.get(i).getBooking_id())
                        .putExtra("BOOKING_NUM", mechanicBookingList.get(i).getBooking_num())
                        .putExtra("BOOKING_DATE", mechanicBookingList.get(i).getBooking_datetime())
                        .putExtra("BOOKING_FEE", mechanicBookingList.get(i).getBooking_fee())
                        .putExtra("BOOKING_STATUS", status)
                        .putExtra("BOOKING_DES", mechanicBookingList.get(i).getBooking_description()));
            }
        });

        return view;
    }
}
