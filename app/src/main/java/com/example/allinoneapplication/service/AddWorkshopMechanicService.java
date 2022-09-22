package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Mechanic;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AddWorkshopMechanicService {
    @Headers({"Accept: application/json"})
    @Multipart
    @POST(EndPoint.ADD_WORKSHOP_MECHANIC)
    Call<Mechanic> add_workshop_mechanic(
            @Part("mechanic_name") RequestBody mechanic_name,
            @Part("mechanic_password") RequestBody mechanic_password,
            @Part("mechanic_email") RequestBody mechanic_email,
            @Part MultipartBody.Part mechanic_profile_img,
            @Part("mechanic_contact") RequestBody mechanic_contact,
            @Part("mechanic_cnic") RequestBody mechanic_cnic,
            @Part("vehicle_type") RequestBody vehicle_type,
            @Part("mechanic_status") RequestBody mechanic_status,
            @Part("fk_workshop_id") RequestBody fk_workshop_id);
}
