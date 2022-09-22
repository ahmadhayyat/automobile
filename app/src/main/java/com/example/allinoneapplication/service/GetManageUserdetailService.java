package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface GetManageUserdetailService {
    @Headers({"Accept: applicaton/json"})
    @GET(EndPoint.GET_MANAGE_USER_DETAILS_URL)
    Call<JsonObject> getManageUserdetails();
}
