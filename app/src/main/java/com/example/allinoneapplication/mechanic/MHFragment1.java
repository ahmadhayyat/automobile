package com.example.allinoneapplication.mechanic;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.MechanicBookingAdapter;
import com.example.allinoneapplication.adapter.UpcomingBookingAdapter;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetMechanicBookService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MHFragment1 extends Fragment {

    View view;
    ListView pending_mh_LV;
    ProgressDialog progressDialog;
    TinyDB tinyDB;
    MechanicBookingAdapter adapter;
    List<Booking> bookingList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_m_h1, container, false);
        pending_mh_LV = view.findViewById(R.id.pending_mh_LV);
        tinyDB = new TinyDB(getActivity());
        GetBookings();
        return view;
    }

    private void GetBookings() {
        if (bookingList != null) {
            bookingList.clear();
        }
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetMechanicBookService service = RetrofitClient.getClient().create(GetMechanicBookService.class);
        Call<JsonObject> call = service.getBookings(tinyDB.getInt("MECHANIC_ID"), "M", "A");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                        JSONArray jsonArray = jsonObject.getJSONArray("records");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);

                            bookingList.add(new Booking(data.getInt("customer_id"),
                                    data.getString("customer_name"),
                                    data.getString("customer_contact"),
                                    data.getString("customer_profile_img"),
                                    data.getInt("booking_id"),
                                    data.getString("booking_num"),
                                    data.getString("booking_datetime"),
                                    data.getInt("booking_fee"),
                                    data.getString("booking_description"),
                                    data.getString("booking_status")
                            ));

                            adapter = new MechanicBookingAdapter(bookingList, getActivity());
                            pending_mh_LV.setAdapter(adapter);
                        }

                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}