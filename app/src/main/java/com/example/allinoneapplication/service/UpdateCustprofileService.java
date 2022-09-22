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

public interface UpdateCustprofileService {

    @Headers({"Accept: application/json"})
    @Multipart
    @POST(EndPoint.UPDATE_CUSTOMER_IMAGE)
    Call<Customer> customer_profile(
            @Part("customer_id") RequestBody customer_id,
            @Part MultipartBody.Part customer_profile_img);
}
