package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Booking {
    @SerializedName("booking_id")
    @Expose
    private int booking_id;

    @SerializedName("booking_num")
    @Expose
    private String booking_num;

    @SerializedName("fk_id")
    @Expose
    private int fk_id;

    @SerializedName("fk_customer_id")
    @Expose
    private int fk_customer_id;

    @SerializedName("user_type")
    @Expose
    private String user_type;

    @SerializedName("booking_datetime")
    @Expose
    private String booking_datetime;

    @SerializedName("booking_status")
    @Expose
    private String booking_status;

    @SerializedName("booking_fee")
    @Expose
    private int booking_fee;
    @SerializedName("error")

    @Expose
    private boolean error;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("booking_description")
    @Expose
    private String booking_description;

    @SerializedName("customer_id")
    @Expose
    private int customer_id;

    @SerializedName("customer_name")
    @Expose
    private String customer_name;

    @SerializedName("customer_contact")
    @Expose
    private String customer_contact;

    @SerializedName("customer_profile_img")
    @Expose
    private String customer_profile_img;

    public Booking() {
    }

    public Booking(int customer_id,
                   String customer_name, String customer_contact, String customer_profile_img,
                   int booking_id, String booking_num, String booking_datetime,
                   int booking_fee, String booking_description, String booking_status) {
        this.booking_id = booking_id;
        this.booking_num = booking_num;
        this.booking_datetime = booking_datetime;
        this.booking_fee = booking_fee;
        this.booking_description = booking_description;
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.customer_contact = customer_contact;
        this.customer_profile_img = customer_profile_img;
        this.booking_status = booking_status;
    }

    public Booking(String booking_status) {
        this.booking_status = booking_status;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public String getBooking_num() {
        return booking_num;
    }

    public void setBooking_num(String booking_num) {
        this.booking_num = booking_num;
    }

    public int getFk_id() {
        return fk_id;
    }

    public void setFk_id(int fk_id) {
        this.fk_id = fk_id;
    }

    public int getFk_customer_id() {
        return fk_customer_id;
    }

    public void setFk_customer_id(int fk_customer_id) {
        this.fk_customer_id = fk_customer_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getBooking_datetime() {
        return booking_datetime;
    }

    public void setBooking_datetime(String booking_datetime) {
        this.booking_datetime = booking_datetime;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public int getBooking_fee() {
        return booking_fee;
    }

    public void setBooking_fee(int booking_fee) {
        this.booking_fee = booking_fee;
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

    public String getBooking_description() {
        return booking_description;
    }

    public void setBooking_description(String booking_description) {
        this.booking_description = booking_description;
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

    public String getCustomer_contact() {
        return customer_contact;
    }

    public void setCustomer_contact(String customer_contact) {
        this.customer_contact = customer_contact;
    }

    public String getCustomer_profile_img() {
        return customer_profile_img;
    }

    public void setCustomer_profile_img(String customer_profile_img) {
        this.customer_profile_img = customer_profile_img;
    }
}
