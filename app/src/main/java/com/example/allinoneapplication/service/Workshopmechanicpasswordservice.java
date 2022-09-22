package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Workshop;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Workshopmechanicpasswordservice {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.WORKSHOP_MECHANIC_CHANGE_PASSWORD)
    Call<Workshop> updateWMPass(
            @Field("wmechanic_id") int wmechanic_id,
            @Field("wmechanic_password") String wmechanic_password,
            @Field("new_password") String new_password
    );
}
