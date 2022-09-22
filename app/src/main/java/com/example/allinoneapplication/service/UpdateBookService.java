package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Booking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateBookService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.UPDATE_BOOK_URL)
    Call<Booking> updateBooking(
            @Field("booking_id") int booking_id,
            @Field("booking_status") String booking_status,
            @Field("payment_additional") int payment_additional,
            @Field("payment_total") int payment_total,
            @Field("additional_service_name") String additional_service_name,
            @Field("payment_discount") int payment_discount);

}
