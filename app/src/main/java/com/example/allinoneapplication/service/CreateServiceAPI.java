package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CreateServiceAPI {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.CREATE_SERVICE_URL)
    Call<Service> createService(
            @Field("service_name") String service_name,
            @Field("service_price") int service_price,
            @Field("service_discount") int service_discount,
            @Field("fk_id") int fk_id,
            @Field("user_type") String user_type
    );
}
