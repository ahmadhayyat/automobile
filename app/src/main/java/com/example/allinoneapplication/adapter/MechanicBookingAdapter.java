package com.example.allinoneapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.mechanic.SelectBookingDetailActivity;
import com.example.allinoneapplication.model.Booking;

import java.util.List;

public class MechanicBookingAdapter extends BaseAdapter {

    List<Booking> bookingList;
    Context context;
    LayoutInflater inflater;

    public MechanicBookingAdapter(List<Booking> bookingList, Context context) {
        this.bookingList = bookingList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return bookingList.size();
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
        view = inflater.inflate(R.layout.mechanic_bookitem_history, viewGroup, false);
        TextView mechanic_booking_num_item, mechanic_booking_date;
        mechanic_booking_num_item = view.findViewById(R.id.mechanic_booking_num_item);
        mechanic_booking_date = view.findViewById(R.id.mechanic_booking_date);
        mechanic_booking_num_item.setText(bookingList.get(i).getBooking_num());
        mechanic_booking_date.setText(bookingList.get(i).getBooking_datetime());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SelectBookingDetailActivity.class);
                intent.putExtra("CUSTOMER_ID", bookingList.get(i).getCustomer_id());
                intent.putExtra("BOOKING_ID", bookingList.get(i).getBooking_id());
                intent.putExtra("CUSTOMER_NAME", bookingList.get(i).getCustomer_name());
                intent.putExtra("CUSTOMER_CONTACT", bookingList.get(i).getCustomer_contact());
                intent.putExtra("CUSTOMER_PROFILE", bookingList.get(i).getCustomer_profile_img());
                intent.putExtra("BOOKING_NUM", bookingList.get(i).getBooking_num());
                intent.putExtra("BOOKING_DATETIME", bookingList.get(i).getBooking_datetime());
                intent.putExtra("BOOKING_FEE", bookingList.get(i).getBooking_fee());
                intent.putExtra("BOOKING_DESCRIPTION", bookingList.get(i).getBooking_description());
                intent.putExtra("BOOKING_STATUS", bookingList.get(i).getBooking_status());
                context.startActivity(intent);
            }
        });
        return view;
    }
}
