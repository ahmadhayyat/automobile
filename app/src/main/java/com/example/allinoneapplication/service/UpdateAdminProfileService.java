package com.example.allinoneapplication.service;
import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Admin;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface UpdateAdminProfileService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.UPDATE_ADMIN_PROFILE)
    Call<Admin> update_admin_profile(
            @Field("admin_id") int admin_id,
            @Field("admin_email") String admin_email,
            @Field("admin_password") String admin_password
    );
}
