package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer {

    @SerializedName("customer_id")
    @Expose
    private int customer_id;

    @SerializedName("customer_name")
    @Expose
    private String customer_name;

    @SerializedName("customer_email")
    @Expose
    private String customer_email;

    @SerializedName("customer_password")
    @Expose
    private String customer_password;

    @SerializedName("customer_contact")
    @Expose
    private String customer_contact;

    @SerializedName("customer_lat")
    @Expose
    private double customer_lat;

    @SerializedName("customer_lng")
    @Expose
    private double customer_lng;

    @SerializedName("customer_address")
    @Expose
    private String customer_address;

    @SerializedName("customer_profile_img")
    @Expose
    private String customer_profile_img;

    @SerializedName("customer_status")
    @Expose
    private String customer_status;

    @SerializedName("created_datetime")
    @Expose
    private String created_datetime;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    public Customer() {
    }

    public Customer(int customer_id, String customer_name, String customer_email,
                    String customer_contact, double customer_lat, double customer_lng,
                    String customer_address, String customer_profile_img, String customer_status,
                    String created_datetime) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_email = customer_email;
        this.customer_contact = customer_contact;
        this.customer_lat = customer_lat;
        this.customer_lng = customer_lng;
        this.customer_address = customer_address;
        this.customer_profile_img = customer_profile_img;
        this.customer_status = customer_status;
        this.created_datetime = created_datetime;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_password() {
        return customer_password;
    }

    public void setCustomer_password(String customer_password) {
        this.customer_password = customer_password;
    }

    public String getCustomer_contact() {
        return customer_contact;
    }

    public void setCustomer_contact(String customer_contact) {
        this.customer_contact = customer_contact;
    }

    public double getCustomer_lat() {
        return customer_lat;
    }

    public void setCustomer_lat(double customer_lat) {
        this.customer_lat = customer_lat;
    }

    public double getCustomer_lng() {
        return customer_lng;
    }

    public void setCustomer_lng(double customer_lng) {
        this.customer_lng = customer_lng;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getCustomer_profile_img() {
        return customer_profile_img;
    }

    public void setCustomer_profile_img(String customer_profile_img) {
        this.customer_profile_img = customer_profile_img;
    }

    public String getCustomer_status() {
        return customer_status;
    }

    public void setCustomer_status(String customer_status) {
        this.customer_status = customer_status;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
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
