package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.RateFeedback;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InsertRatingService {

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.INSERT_RATING)
    Call<RateFeedback> SendRating(
            @Field("user_id") int user_id,
            @Field("fk_booking_id") int fk_booking_id,
            @Field("rf_title") String rf_title,
            @Field("rf_star") double rf_star
    );

}
