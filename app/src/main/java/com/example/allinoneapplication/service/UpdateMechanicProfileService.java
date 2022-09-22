package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Mechanic;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateMechanicProfileService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.UPDATE_MECHANIC_PROFILE)
    Call<Mechanic> updateMprofile(
            @Field("mechanic_id") int mechanic_id,
            @Field("mechanic_name") String mechanic_name,
            @Field("mechanic_email") String mechanic_email,
            @Field("mehanic_contact") String mehanic_contact
    );
}
