package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetMechanicBookService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.GET_MECHANIC_BOOK_URL)
    Call<JsonObject> getBookings(
            @Field("fk_id") int fk_id,
            @Field("user_type") String user_type,
            @Field("booking_status") String booking_status);
}
