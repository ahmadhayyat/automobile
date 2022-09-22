package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.model.NearBy;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NearByService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.NEARBY_AREA_URL)
    Call<NearBy> nearBy(
            @Field("na_title") String na_title);
}
