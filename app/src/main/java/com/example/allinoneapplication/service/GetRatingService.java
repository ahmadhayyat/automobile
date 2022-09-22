package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetRatingService {
    @Headers({"Accept: applicaton/json"})
    @FormUrlEncoded
    @POST(EndPoint.GET_RATING_URL)
    Call<JsonObject> getRating(
            @Field("user_id") int user_id
    );
}
