package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tracking {

    @SerializedName("cl_id")
    @Expose
    private int cl_id;

    @SerializedName("cl_latitude")
    @Expose
    private double cl_latitude;

    @SerializedName("cl_longitude")
    @Expose
    private double cl_longitude;

    @SerializedName("fk_mechanic_id")
    @Expose
    private int fk_mechanic_id;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    public Tracking() {
    }

    public int getCl_id() {
        return cl_id;
    }

    public void setCl_id(int cl_id) {
        this.cl_id = cl_id;
    }

    public double getCl_latitude() {
        return cl_latitude;
    }

    public void setCl_latitude(double cl_latitude) {
        this.cl_latitude = cl_latitude;
    }

    public double getCl_longitude() {
        return cl_longitude;
    }

    public void setCl_longitude(double cl_longitude) {
        this.cl_longitude = cl_longitude;
    }

    public int getFk_mechanic_id() {
        return fk_mechanic_id;
    }

    public void setFk_mechanic_id(int fk_mechanic_id) {
        this.fk_mechanic_id = fk_mechanic_id;
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
