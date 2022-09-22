package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Service {

    @SerializedName("service_id")
    @Expose
    private int service_id;

    @SerializedName("service_name")
    @Expose
    private String service_name;

    @SerializedName("service_price")
    @Expose
    private int service_price;

    @SerializedName("service_discount")
    @Expose
    private int service_discount;

    @SerializedName("fk_id")
    @Expose
    private int fk_id;

    @SerializedName("user_type")
    @Expose
    private String user_type;

    @SerializedName("service_status")
    @Expose
    private String service_status;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    public Service() {
    }

    public Service(int service_id, String service_name, int service_price,
                   int service_discount, String service_status) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.service_price = service_price;
        this.service_discount = service_discount;
        this.service_status = service_status;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public int getService_price() {
        return service_price;
    }

    public void setService_price(int service_price) {
        this.service_price = service_price;
    }

    public int getService_discount() {
        return service_discount;
    }

    public void setService_discount(int service_discount) {
        this.service_discount = service_discount;
    }

    public int getFk_id() {
        return fk_id;
    }

    public void setFk_id(int fk_id) {
        this.fk_id = fk_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getService_status() {
        return service_status;
    }

    public void setService_status(String service_status) {
        this.service_status = service_status;
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