package com.example.allinoneapplication.service;

import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Favourite;
import com.example.allinoneapplication.model.Tracking;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface InsertFavouriteService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.INSERT_FAVOURITE_URL)
    Call<Favourite> addintoFav(
            @Field("fav_fk_id") int fav_fk_id,
            @Field("fav_type") String fav_type,
            @Field("fav_user_id") int fav_user_id);
}
