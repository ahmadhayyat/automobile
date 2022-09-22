package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Complaint;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegisterComplainService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.REGISTER_COMPLAIN_URL)
    Call<Complaint> registerComp(
            @Field("comp_sub") String comp_sub,
            @Field("comp_msg") String comp_msg,
            @Field("fk_c_id") int fk_c_id,
            @Field("fk_m_id") int fk_m_id,
            @Field("comp_stype") String comp_stype);
}
