package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Customer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateCustomerProfileService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.UPDATE_CUSTOMER_PROFILE)
    Call<Customer> updateprofile(
            @Field("customer_id") int customer_id,
            @Field("customer_name") String customer_name,
            @Field("customer_email") String customer_email,
            @Field("customer_contact") String customer_contact
    );
}
