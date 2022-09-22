package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Mechanic;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MechanicPasswordService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.MECHANIC_CHANGE_PASSWORD)
    Call<Mechanic> updateMPass(
            @Field("mechanic_id") int mechanic_id,
            @Field("mechanic_password") String mechanic_password,
            @Field("new_password") String new_password
    );
}
