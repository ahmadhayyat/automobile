package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MechanicBooking {

    @SerializedName("mechanic_id")
    @Expose
    private int mechanic_id;

    @SerializedName("mechanic_name")
    @Expose
    private String mechanic_name;

    @SerializedName("mehanic_contact")
    @Expose
    private String mehanic_contact;

    @SerializedName("mechanic_profile_img")
    @Expose
    private String mechanic_profile_img;

    @SerializedName("booking_id")
    @Expose
    private int booking_id;

    @SerializedName("booking_num")
    @Expose
    private String booking_num;

    @SerializedName("booking_datetime")
    @Expose
    private String booking_datetime;

    @SerializedName("booking_fee")
    @Expose
    private int booking_fee;

    @SerializedName("booking_description")
    @Expose
    private String booking_description;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    public MechanicBooking() {
    }

    public MechanicBooking(int mechanic_id, String mechanic_name, String mehanic_contact,
                           String mechanic_profile_img, int booking_id, String booking_num, String booking_datetime, int booking_fee, String booking_description) {
        this.mechanic_id = mechanic_id;
        this.mechanic_name = mechanic_name;
        this.mehanic_contact = mehanic_contact;
        this.mechanic_profile_img = mechanic_profile_img;
        this.booking_id = booking_id;
        this.booking_num = booking_num;
        this.booking_datetime = booking_datetime;
        this.booking_fee = booking_fee;
        this.booking_description = booking_description;
    }

    public int getMechanic_id() {
        return mechanic_id;
    }

    public void setMechanic_id(int mechanic_id) {
        this.mechanic_id = mechanic_id;
    }

    public String getMechanic_name() {
        return mechanic_name;
    }

    public void setMechanic_name(String mechanic_name) {
        this.mechanic_name = mechanic_name;
    }

    public String getMehanic_contact() {
        return mehanic_contact;
    }

    public void setMehanic_contact(String mehanic_contact) {
        this.mehanic_contact = mehanic_contact;
    }

    public String getMechanic_profile_img() {
        return mechanic_profile_img;
    }

    public void setMechanic_profile_img(String mechanic_profile_img) {
        this.mechanic_profile_img = mechanic_profile_img;
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

    public String getBooking_datetime() {
        return booking_datetime;
    }

    public void setBooking_datetime(String booking_datetime) {
        this.booking_datetime = booking_datetime;
    }

    public int getBooking_fee() {
        return booking_fee;
    }

    public void setBooking_fee(int booking_fee) {
        this.booking_fee = booking_fee;
    }

    public String getBooking_description() {
        return booking_description;
    }

    public void setBooking_description(String booking_description) {
        this.booking_description = booking_description;
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
