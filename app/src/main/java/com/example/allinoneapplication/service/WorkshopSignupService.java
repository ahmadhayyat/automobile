package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Mechanic;
import com.example.allinoneapplication.model.Workshop;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface WorkshopSignupService {
    @Headers({"Accept: application/json"})
    @Multipart
    @POST(EndPoint.WORKSHOP_SIGNUP_URL)
    Call<Workshop> workshop_signup(
            @Part("wmechanic_name") RequestBody wmechanic_name,
            @Part("wmechanic_email") RequestBody wmechanic_email,
            @Part("wmechanic_password") RequestBody wmechanic_password,
            @Part("wmechanic_address") RequestBody wmechanic_address,
            @Part("wmechanic_contact") RequestBody wmechanic_contact,
            @Part("wmechanic_lat") RequestBody wmechanic_lat,
            @Part("wmechanic_lng") RequestBody wmechanic_lng,
            @Part("wmechanic_cnic") RequestBody wmechanic_cnic,
            @Part("vehicle_type") RequestBody vehicle_type,
            @Part MultipartBody.Part wmechanic_profile_img);
}
