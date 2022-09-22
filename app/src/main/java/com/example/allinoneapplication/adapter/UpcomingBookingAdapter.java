package com.example.allinoneapplication.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.mechanic.MechanicDrawerActivity;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.UpdateBookingService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingBookingAdapter extends BaseAdapter {

    List<Booking> bookingList;
    Context context;
    LayoutInflater inflater;
    Booking booking;
    ProgressDialog progressDialog;

    public UpcomingBookingAdapter(List<Booking> bookingList, Context context) {
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
        view = inflater.inflate(R.layout.upcoming_booking_item, viewGroup, false);
        TextView upcoming_booking_num_item, upcoming_booking_date, tv_upcoming_accept, tv_upcoming_reject;
        upcoming_booking_num_item = view.findViewById(R.id.upcoming_booking_num_item);
        upcoming_booking_date = view.findViewById(R.id.upcoming_booking_date);
        tv_upcoming_accept = view.findViewById(R.id.tv_upcoming_accept);
        tv_upcoming_reject = view.findViewById(R.id.tv_upcoming_reject);
        upcoming_booking_num_item.setText(bookingList.get(i).getBooking_num());
        upcoming_booking_date.setText(bookingList.get(i).getBooking_datetime());

        tv_upcoming_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateBooking("A", bookingList.get(i).getBooking_id(), bookingList.get(i).getBooking_fee());
            }
        });

        tv_upcoming_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateBooking("R", bookingList.get(i).getBooking_id(), bookingList.get(i).getBooking_fee());
            }
        });

        return view;
    }

    private void UpdateBooking(String status, int BID, int amount) {
        booking = new Booking();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("please wait....");
        progressDialog.show();
        UpdateBookingService service = RetrofitClient.getClient().create(UpdateBookingService.class);
        Call<Booking> call = service.updateBooking(BID, status, amount);
        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    booking = response.body();
                    if (!booking.isError()) {
                        Toast.makeText(context,
                                booking.getMessage(), Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, MechanicDrawerActivity.class));
                    } else {
                        Toast.makeText(context,
                                booking.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(context,
                            response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(context,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
