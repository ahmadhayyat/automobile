package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chat {

    @SerializedName("chat_id")
    @Expose
    private int chat_id;

    @SerializedName("chat_message")
    @Expose
    private String chat_message;

    @SerializedName("sender_id")
    @Expose
    private int sender_id;

    @SerializedName("receiver_id")
    @Expose
    private int receiver_id;

    @SerializedName("sender_type")
    @Expose
    private String sender_type;

    @SerializedName("chat_datetime")
    @Expose
    private String chat_datetime;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    public Chat() {
    }

    public Chat(int chat_id, String chat_message, int sender_id, int receiver_id, String sender_type, String chat_datetime) {
        this.chat_id = chat_id;
        this.chat_message = chat_message;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.sender_type = sender_type;
        this.chat_datetime = chat_datetime;
    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public String getChat_message() {
        return chat_message;
    }

    public void setChat_message(String chat_message) {
        this.chat_message = chat_message;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getChat_datetime() {
        return chat_datetime;
    }

    public void setChat_datetime(String chat_datetime) {
        this.chat_datetime = chat_datetime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
