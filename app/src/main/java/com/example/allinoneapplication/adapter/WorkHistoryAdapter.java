package com.example.allinoneapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.model.RateFeedback;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetBookRateService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkHistoryAdapter extends BaseAdapter {

    List<Booking> bookingList;
    Context context;
    RateFeedback rateFeedback;
    LayoutInflater inflater;

    public WorkHistoryAdapter(List<Booking> bookingList, Context context) {
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
        view = inflater.inflate(R.layout.item_workhistory, viewGroup, false);
        TextView mechanic_booking_num_item, mechanic_booking_date, workhistory_cust_name, workhistory_cust_fee,
                workhistory_cust_description;
        RatingBar workhistory_cust_rating;

        mechanic_booking_num_item = view.findViewById(R.id.mechanic_booking_num_item);
        mechanic_booking_date = view.findViewById(R.id.mechanic_booking_date);
        workhistory_cust_name = view.findViewById(R.id.workhistory_cust_name);
        workhistory_cust_fee = view.findViewById(R.id.workhistory_cust_fee);
        workhistory_cust_description = view.findViewById(R.id.workhistory_cust_description);
        workhistory_cust_rating = view.findViewById(R.id.workhistory_cust_rating);

        mechanic_booking_num_item.setText(bookingList.get(i).getBooking_num());
        mechanic_booking_date.setText(bookingList.get(i).getBooking_datetime());
        workhistory_cust_name.setText(bookingList.get(i).getCustomer_name());
        workhistory_cust_description.setText(bookingList.get(i).getBooking_description());
        workhistory_cust_fee.setText(String.valueOf(bookingList.get(i).getBooking_fee()));

        getMechanicRatingFeedback(bookingList.get(i).getBooking_id(), workhistory_cust_rating);


        return view;
    }

    public void getMechanicRatingFeedback(int BID, RatingBar ratingBar) {
        GetBookRateService service = RetrofitClient.getClient().create(GetBookRateService.class);
        Call<RateFeedback> call = service.ratefeedback(BID);
        call.enqueue(new Callback<RateFeedback>() {
            @Override
            public void onResponse(Call<RateFeedback> call, Response<RateFeedback> response) {
                if (response.isSuccessful()) {
                    rateFeedback = response.body();
                    if (!rateFeedback.isError()) {
                        ratingBar.setRating((float) rateFeedback.getRf_star());
                    }
                }
            }

            @Override
            public void onFailure(Call<RateFeedback> call, Throwable t) {
                Toast.makeText(context,
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
