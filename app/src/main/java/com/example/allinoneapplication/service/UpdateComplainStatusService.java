package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Complaint;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateComplainStatusService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.UPDATE_COMPLAIN_STATUS)
    Call<Complaint> updateStatus(
            @Field("comp_id") int comp_id,
            @Field("comp_status") String comp_status
    );

}
