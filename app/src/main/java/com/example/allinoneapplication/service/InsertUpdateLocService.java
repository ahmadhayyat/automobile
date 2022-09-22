package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Tracking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InsertUpdateLocService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.INSERT_UPDATE_LOC_URL)
    Call<Tracking> updateLoc(
            @Field("cl_latitude") double cl_latitude,
            @Field("cl_longitude") double cl_longitude,
            @Field("fk_mechanic_id") int fk_mechanic_id);
}
