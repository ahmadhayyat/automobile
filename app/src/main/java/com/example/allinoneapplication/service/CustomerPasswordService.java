package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Customer;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CustomerPasswordService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.CUSTOMER_CHANGE_PASSWORD)
    Call<Customer> updatePass(
            @Field("customer_id") int customer_id,
            @Field("customer_password") String customer_password,
            @Field("new_password") String new_password
    );

}
