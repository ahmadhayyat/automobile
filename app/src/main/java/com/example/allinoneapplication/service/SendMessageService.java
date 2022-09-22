package com.example.allinoneapplication.service;


import com.example.allinoneapplication.constant.EndPoint;
import com.example.allinoneapplication.model.Chat;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SendMessageService {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST(EndPoint.SEND_CHAT_MESSAGE)
    Call<Chat> sendMessage(
            @Field("chat_message") String chat_message,
            @Field("sender_id") int sender_id,
            @Field("receiver_id") int receiver_id,
            @Field("sender_type") String sender_type);

}
