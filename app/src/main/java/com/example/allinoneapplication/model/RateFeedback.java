package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RateFeedback {

    @SerializedName("rf_id")
    @Expose
    private int rf_id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    @SerializedName("fk_booking_id")
    @Expose
    private int fk_booking_id;

    @SerializedName("rf_title")
    @Expose
    private String rf_title;

    @SerializedName("rf_star")
    @Expose
    private double rf_star;

    @SerializedName("payment_amount")
    @Expose
    private int payment_amount;

    @SerializedName("payment_additional")
    @Expose
    private int payment_additional;

    @SerializedName("payment_total")
    @Expose
    private int payment_total;

    @SerializedName("payment_datetime")
    @Expose
    private String payment_datetime;

    @SerializedName("payment_discount")
    @Expose
    private int payment_discount;

    @SerializedName("booking_num")
    @Expose
    private String booking_num;

    @SerializedName("fk_id")
    @Expose
    private int fk_id;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    public RateFeedback() {
    }

    public RateFeedback(int fk_booking_id, int payment_amount, int payment_additional, int payment_total,
                        String payment_datetime, int payment_discount, String booking_num, int fk_id) {
        this.fk_booking_id = fk_booking_id;
        this.payment_amount = payment_amount;
        this.payment_additional = payment_additional;
        this.payment_total = payment_total;
        this.payment_datetime = payment_datetime;
        this.payment_discount = payment_discount;
        this.booking_num = booking_num;
        this.fk_id = fk_id;
    }

    public RateFeedback(String rf_title, double rf_star) {
        this.rf_title = rf_title;
        this.rf_star = rf_star;
    }

    public int getFk_id() {
        return fk_id;
    }

    public void setFk_id(int fk_id) {
        this.fk_id = fk_id;
    }

    public int getRf_id() {
        return rf_id;
    }

    public void setRf_id(int rf_id) {
        this.rf_id = rf_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getFk_booking_id() {
        return fk_booking_id;
    }

    public void setFk_booking_id(int fk_booking_id) {
        this.fk_booking_id = fk_booking_id;
    }

    public String getRf_title() {
        return rf_title;
    }

    public void setRf_title(String rf_title) {
        this.rf_title = rf_title;
    }

    public double getRf_star() {
        return rf_star;
    }

    public void setRf_star(double rf_star) {
        this.rf_star = rf_star;
    }

    public int getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(int payment_amount) {
        this.payment_amount = payment_amount;
    }

    public int getPayment_additional() {
        return payment_additional;
    }

    public void setPayment_additional(int payment_additional) {
        this.payment_additional = payment_additional;
    }

    public int getPayment_total() {
        return payment_total;
    }

    public void setPayment_total(int payment_total) {
        this.payment_total = payment_total;
    }

    public String getPayment_datetime() {
        return payment_datetime;
    }

    public void setPayment_datetime(String payment_datetime) {
        this.payment_datetime = payment_datetime;
    }

    public int getPayment_discount() {
        return payment_discount;
    }

    public void setPayment_discount(int payment_discount) {
        this.payment_discount = payment_discount;
    }

    public String getBooking_num() {
        return booking_num;
    }

    public void setBooking_num(String booking_num) {
        this.booking_num = booking_num;
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
