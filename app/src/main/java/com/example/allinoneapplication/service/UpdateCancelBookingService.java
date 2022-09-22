package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Booking;
import com.example.allinoneapplication.model.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateCancelBookingService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.UPDATE_CUSTOMER_CANCEL_BOOKING_URL)
    Call<Booking> updatecancelbookingStatus(
            @Field("booking_id") int booking_id,
            @Field("booking_status") String booking_status
    );
}
