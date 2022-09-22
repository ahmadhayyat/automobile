package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateMechServiceAPI {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.UPDATE_SERVICE_URL)
    Call<Service> updateService(
            @Field("service_id") int service_id,
            @Field("service_name") String service_name,
            @Field("service_price") int service_price,
            @Field("service_discount") int service_discount);

}
