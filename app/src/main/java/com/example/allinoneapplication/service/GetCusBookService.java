package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetCusBookService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.GET_CUSTOMER_BOOK_URL)
    Call<JsonObject> getBookings(
            @Field("fk_customer_id") int fk_customer_id,
            @Field("booking_status") String booking_status);
}
