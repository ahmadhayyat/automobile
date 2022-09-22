package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Mechanic;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MechanicLoginService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.MECHANIC_LOGIN_URL)
    Call<Mechanic> mechanic_login(
            @Field("mechanic_email") String mechanic_email,
         @Field("mechanic_password") String mechanic_password);
}
