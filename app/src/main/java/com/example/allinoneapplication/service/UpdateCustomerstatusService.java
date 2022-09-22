package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Customer;
import com.example.allinoneapplication.model.Workshop;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateCustomerstatusService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.UPDATE_CUSTOMER_STATUS_URL)
    Call<Customer> updateStatus(
            @Field("customer_id") int customer_id,
            @Field("customer_status") String customer_status
    );
}
