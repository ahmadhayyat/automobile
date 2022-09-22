package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Workshop;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface WorkshopLoginService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.WORKSHOP_LOGIN_URL)
    Call<Workshop> workshop_login(
            @Field("wmechanic_email") String wmechanic_email,
            @Field("wmechanic_password") String wmechanic_password);

}
