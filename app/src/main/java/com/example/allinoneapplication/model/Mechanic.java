package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Mechanic {
    @SerializedName("mechanic_id")
    @Expose
    private int mechanic_id;

    @SerializedName("mechanic_name")
    @Expose
    private String mechanic_name;

    @SerializedName("mechanic_password")
    @Expose
    private String mechanic_password;

    @SerializedName("mechanic_address")
    @Expose
    private String mechanic_address;

    @SerializedName("mechanic_email")
    @Expose
    private String mechanic_email;

    @SerializedName("mechanic_lng")
    @Expose
    private double mechanic_lng;

    @SerializedName("mechanic_lat")
    @Expose
    private double mechanic_lat;

    @SerializedName("mechanic_profile_img")
    @Expose
    private String mechanic_profile_img;

    @SerializedName("mehanic_contact")
    @Expose
    private String mechanic_contact;

    @SerializedName("mechanic_status")
    @Expose
    private String mechanic_status;

    @SerializedName("mechanic_datetime")
    @Expose
    private String mechanic_datetime;

    @SerializedName("mechanic_cnic")
    @Expose
    private String mechanic_cnic;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;

    @SerializedName("mechanic_price")
    @Expose
    private int mechanic_price;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("message")
    @Expose
    private String message;

    public Mechanic(int mechanic_id, String mechanic_name, String mechanic_email, String mechanic_profile_img, String mechanic_contact, String mechanic_status,
                    String mechanic_cnic, String vehicle_type) {
        this.mechanic_id = mechanic_id;
        this.mechanic_name = mechanic_name;
        this.mechanic_email = mechanic_email;
        this.mechanic_profile_img = mechanic_profile_img;
        this.mechanic_contact = mechanic_contact;
        this.mechanic_status = mechanic_status;
        this.mechanic_cnic = mechanic_cnic;
        this.vehicle_type = vehicle_type;
    }

    public Mechanic(int mechanic_id, String mechanic_name,
                    String mechanic_address, String mechanic_email, double mechanic_lng,
                    double mechanic_lat, String mechanic_profile_img, String mechanic_contact,
                    String mechanic_status, String mechanic_datetime, String mechanic_cnic,
                    String vehicle_type, int mechanic_price) {
        this.mechanic_id = mechanic_id;
        this.mechanic_name = mechanic_name;
        this.mechanic_address = mechanic_address;
        this.mechanic_email = mechanic_email;
        this.mechanic_lng = mechanic_lng;
        this.mechanic_lat = mechanic_lat;
        this.mechanic_profile_img = mechanic_profile_img;
        this.mechanic_contact = mechanic_contact;
        this.mechanic_status = mechanic_status;
        this.mechanic_datetime = mechanic_datetime;
        this.mechanic_cnic = mechanic_cnic;
        this.vehicle_type = vehicle_type;
        this.mechanic_price = mechanic_price;
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

    public String getMechanic_password() {
        return mechanic_password;
    }

    public void setMechanic_password(String mechanic_password) {
        this.mechanic_password = mechanic_password;
    }

    public String getMechanic_address() {
        return mechanic_address;
    }

    public void setMechanic_address(String mechanic_address) {
        this.mechanic_address = mechanic_address;
    }

    public String getMechanic_email() {
        return mechanic_email;
    }

    public void setMechanic_email(String mechanic_email) {
        this.mechanic_email = mechanic_email;
    }

    public double getMechanic_lng() {
        return mechanic_lng;
    }

    public void setMechanic_lng(double mechanic_lng) {
        this.mechanic_lng = mechanic_lng;
    }

    public double getMechanic_lat() {
        return mechanic_lat;
    }

    public void setMechanic_lat(double mechanic_lat) {
        this.mechanic_lat = mechanic_lat;
    }

    public String getMechanic_profile_img() {
        return mechanic_profile_img;
    }

    public void setMechanic_profile_img(String mechanic_profile_img) {
        this.mechanic_profile_img = mechanic_profile_img;
    }

    public String getMechanic_contact() {
        return mechanic_contact;
    }

    public void setMechanic_contact(String mechanic_contact) {
        this.mechanic_contact = mechanic_contact;
    }

    public String getMechanic_status() {
        return mechanic_status;
    }

    public void setMechanic_status(String mechanic_status) {
        this.mechanic_status = mechanic_status;
    }

    public String getMechanic_datetime() {
        return mechanic_datetime;
    }

    public void setMechanic_datetime(String mechanic_datetime) {
        this.mechanic_datetime = mechanic_datetime;
    }

    public String getMechanic_cnic() {
        return mechanic_cnic;
    }

    public void setMechanic_cnic(String mechanic_cnic) {
        this.mechanic_cnic = mechanic_cnic;
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

    public Mechanic() {

    }

    public int getMechanic_price() {
        return mechanic_price;
    }

    public void setMechanic_price(int mechanic_price) {
        this.mechanic_price = mechanic_price;
    }
}
