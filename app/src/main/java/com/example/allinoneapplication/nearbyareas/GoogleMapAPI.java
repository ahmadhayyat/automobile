package com.example.allinoneapplication.nearbyareas;


import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.nearbyareas.mapmodel.PlacesResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapAPI {
    @GET(EndPoint.NEARBY_PLACES)
    Call<PlacesResults> getNearBy(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("keyword") String keyword,
            @Query("key") String key
    );
}
