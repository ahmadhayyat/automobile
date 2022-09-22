package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Workshop;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UpdateWorkshopMechanicimgservice {
    @Headers({"Accept: application/json"})
    @Multipart
    @POST(EndPoint.UPDATE_WORKSHOP_MECHANIC_IMAGE)
    Call<Workshop> wmechanic_profile(
            @Part("wmechanic_id") RequestBody wmechanic_id,
            @Part MultipartBody.Part wmechanic_profile_img);
}
