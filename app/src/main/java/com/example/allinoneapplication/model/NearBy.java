package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearBy {

    @SerializedName("na_id")
    @Expose
    private int na_id;

    @SerializedName("na_title")
    @Expose
    private String na_title;

    @SerializedName("na_status")
    @Expose
    private String na_status;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    public NearBy() {
    }

    public NearBy(int na_id, String na_title, String na_status) {
        this.na_id = na_id;
        this.na_title = na_title;
        this.na_status = na_status;
    }

    public int getNa_id() {
        return na_id;
    }

    public void setNa_id(int na_id) {
        this.na_id = na_id;
    }

    public String getNa_title() {
        return na_title;
    }

    public void setNa_title(String na_title) {
        this.na_title = na_title;
    }

    public String getNa_status() {
        return na_status;
    }

    public void setNa_status(String na_status) {
        this.na_status = na_status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
