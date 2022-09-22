package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Tracking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetMechLocService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.GET_MECHANIC_LOC_URL)
    Call<Tracking> getLocation(
            @Field("fk_mechanic_id") int fk_mechanic_id);
}
