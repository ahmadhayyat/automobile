package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Complaint;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetCustomerComplainDetailService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.GET_CUSTOMER_COMPLAIN_DETAIL_URL)
    Call<JsonObject> getCustomerComplain(
            @Field("fk_c_id") int fk_c_id,
            @Field("comp_stype") String comp_stype,
            @Field("comp_status") String comp_status
    );
}
