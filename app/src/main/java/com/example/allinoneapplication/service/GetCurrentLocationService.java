package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Tracking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetCurrentLocationService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.GET_CURRENT_LOCATION_URL)
    Call<Tracking> getcurrentLocation(
            @Field("fk_mechanic_id") int fk_mechanic_id
    );
}
