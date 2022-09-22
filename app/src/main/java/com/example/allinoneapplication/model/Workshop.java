package com.example.allinoneapplication.model;

import android.app.ProgressDialog;
import android.widget.Toast;

import com.example.allinoneapplication.retrofit.RetrofitClient;
import com.example.allinoneapplication.service.MechanicSignupService;
import com.example.allinoneapplication.service.WorkshopSignupService;
import com.example.allinoneapplication.ui.SignUpActivity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Workshop {
    @SerializedName("wmechanic_id")
    @Expose
    private int wmechanic_id;

    @SerializedName("wmechanic_name")
    @Expose
    private String wmechanic_name;

    @SerializedName("wmechanic_password")
    @Expose
    private String wmechanic_password;

    @SerializedName("wmechanic_address")
    @Expose
    private String wmechanic_address;

    @SerializedName("wmechanic_email")
    @Expose
    private String wmechanic_email;

    @SerializedName("wmechanic_lng")
    @Expose
    private double wmechanic_lng;

    @SerializedName("wmechanic_lat")
    @Expose
    private double wmechanic_lat;

    @SerializedName("wmechanic_profile_img")
    @Expose
    private String wmechanic_profile_img;

    @SerializedName("wmechanic_contact")
    @Expose
    private String wmechanic_contact;

    @SerializedName("wmechanic_status")
    @Expose
    private String wmechanic_status;

    @SerializedName("wmechanic_datetime")
    @Expose
    private String wmechanic_datetime;

    @SerializedName("wmechanic_cnic")
    @Expose
    private String wmechanic_cnic;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    public Workshop(){

    }

    public Workshop(int wmechanic_id, String wmechanic_name,
                    String wmechanic_address, String wmechanic_email,
                    double wmechanic_lng, double wmechanic_lat, String wmechanic_profile_img,
                    String wmechanic_contact, String wmechanic_status, String wmechanic_datetime,
                    String wmechanic_cnic, String vehicle_type) {
        this.wmechanic_id = wmechanic_id;
        this.wmechanic_name = wmechanic_name;
        this.wmechanic_address = wmechanic_address;
        this.wmechanic_email = wmechanic_email;
        this.wmechanic_lng = wmechanic_lng;
        this.wmechanic_lat = wmechanic_lat;
        this.wmechanic_profile_img = wmechanic_profile_img;
        this.wmechanic_contact = wmechanic_contact;
        this.wmechanic_status = wmechanic_status;
        this.wmechanic_datetime = wmechanic_datetime;
        this.wmechanic_cnic = wmechanic_cnic;
        this.vehicle_type = vehicle_type;
    }

    public int getWmechanic_id() {
        return wmechanic_id;
    }

    public void setWmechanic_id(int wmechanic_id) {
        this.wmechanic_id = wmechanic_id;
    }

    public String getWmechanic_name() {
        return wmechanic_name;
    }

    public void setWmechanic_name(String wmechanic_name) {
        this.wmechanic_name = wmechanic_name;
    }

    public String getWmechanic_password() {
        return wmechanic_password;
    }

    public void setWmechanic_password(String wmechanic_password) {
        this.wmechanic_password = wmechanic_password;
    }

    public String getWmechanic_address() {
        return wmechanic_address;
    }

    public void setWmechanic_address(String wmechanic_address) {
        this.wmechanic_address = wmechanic_address;
    }

    public String getWmechanic_email() {
        return wmechanic_email;
    }

    public void setWmechanic_email(String wmechanic_email) {
        this.wmechanic_email = wmechanic_email;
    }

    public double getWmechanic_lng() {
        return wmechanic_lng;
    }

    public void setWmechanic_lng(double wmechanic_lng) {
        this.wmechanic_lng = wmechanic_lng;
    }

    public double getWmechanic_lat() {
        return wmechanic_lat;
    }

    public void setWmechanic_lat(double wmechanic_lat) {
        this.wmechanic_lat = wmechanic_lat;
    }

    public String getWmechanic_profile_img() {
        return wmechanic_profile_img;
    }

    public void setWmechanic_profile_img(String wmechanic_profile_img) {
        this.wmechanic_profile_img = wmechanic_profile_img;
    }

    public String getWmechanic_contact() {
        return wmechanic_contact;
    }

    public void setWmechanic_contact(String wmechanic_contact) {
        this.wmechanic_contact = wmechanic_contact;
    }

    public String getWmechanic_status() {
        return wmechanic_status;
    }

    public void setWmechanic_status(String wmechanic_status) {
        this.wmechanic_status = wmechanic_status;
    }

    public String getWmechanic_datetime() {
        return wmechanic_datetime;
    }

    public void setWmechanic_datetime(String wmechanic_datetime) {
        this.wmechanic_datetime = wmechanic_datetime;
    }

    public String getWmechanic_cnic() {
        return wmechanic_cnic;
    }

    public void setWmechanic_cnic(String wmechanic_cnic) {
        this.wmechanic_cnic = wmechanic_cnic;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
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


