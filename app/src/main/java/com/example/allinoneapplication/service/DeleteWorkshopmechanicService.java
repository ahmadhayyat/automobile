package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface DeleteWorkshopmechanicService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.DELETE_WORKSHOP_MECHANIC_URL)
    Call<Mechanic> deleteMechanic(
            @Field("mechanic_id") int mechanic_id
    );

}
