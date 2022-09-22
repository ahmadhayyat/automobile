package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Customer;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CustomerSignupService {

    @Headers({"Accept: application/json"})
    @Multipart
    @POST(EndPoint.CUSTOMER_SIGNUP_URL)
    Call<Customer> customer_signup(
            @Part("customer_name") RequestBody customer_name,
            @Part("customer_email") RequestBody customer_email,
            @Part("customer_password") RequestBody customer_password,
            @Part("customer_contact") RequestBody customer_contact,
            @Part("customer_lat") RequestBody customer_lat,
            @Part("customer_lng") RequestBody customer_lng,
            @Part("customer_address") RequestBody customer_address,
            @Part MultipartBody.Part customer_profile_img);
}
