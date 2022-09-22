package com.example.allinoneapplication.customer;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.allinoneapplication.R;
import com.example.allinoneapplication.adapter.CustomerMechBookingAdapter;
import com.example.allinoneapplication.constant.Constant;
import com.example.allinoneapplication.constant.TinyDB;
import com.example.allinoneapplication.model.MechanicBooking;
import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.GetCusBookService;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CHFragnment2 extends Fragment {

    View view;
    ListView rejected_ch_LV;
    List<MechanicBooking> mechanicBookingList = new ArrayList<>();
    ProgressDialog progressDialog;
    TinyDB tinyDB;
    CustomerMechBookingAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_chfragnment2, container, false);
        rejected_ch_LV = view.findViewById(R.id.rejected_ch_LV);
        tinyDB = new TinyDB(getContext());
        GetBookings(tinyDB.getInt("CUSTOMERID"), "A");
        Constant.STATUS = "A";
        return view;
    }

    private void GetBookings(int ID, String status) {
        if (mechanicBookingList != null) {
            mechanicBookingList.clear();
        }

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("please wait...");
        progressDialog.show();

        GetCusBookService service = RetrofitClient.getClient().create(GetCusBookService.class);
        Call<JsonObject> call = service.getBookings(ID, status);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    if (response.code() == 200) {

                        try {
                            JSONObject jsonObject = new JSONObject(response.body().getAsJsonObject().toString());

                            JSONArray jsonArray = jsonObject.getJSONArray("records");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject data = jsonArray.getJSONObject(i);

                                mechanicBookingList.add(new MechanicBooking(
                                        data.getInt("mechanic_id"),
                                        data.getString("mechanic_name"),
                                        data.getString("mehanic_contact"),
                                        data.getString("mechanic_profile_img"),
                                        data.getInt("booking_id"),
                                        data.getString("booking_num"),
                                        data.getString("booking_datetime"),
                                        data.getInt("booking_fee"),
                                        data.getString("booking_description")
                                ));

                                adapter = new CustomerMechBookingAdapter(mechanicBookingList,
                                        requireContext(), status);
                                rejected_ch_LV.setAdapter(adapter);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}