package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Customer;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CustomerLoginService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.CUSTOMER_LOGIN_URL)
    Call<Customer> customer_login(
            @Field("customer_email") String customer_email,
            @Field("customer_password") String customer_password);
}
