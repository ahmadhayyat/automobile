package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Favourite {

    @SerializedName("fav_id")
    @Expose
    private int fav_id;

    @SerializedName("fav_fk_id")
    @Expose
    private int fav_fk_id;

    @SerializedName("fav_type")
    @Expose
    private String fav_type;

    @SerializedName("fav_user_id")
    @Expose
    private int fav_user_id;

    @SerializedName("mechanic_id")
    @Expose
    private int mechanic_id;

    @SerializedName("mechanic_name")
    @Expose
    private String mechanic_name;

    @SerializedName("mechanic_email")
    @Expose
    private String mechanic_email;

    @SerializedName("mehanic_contact")
    @Expose
    private String mehanic_contact;

    @SerializedName("mechanic_cnic")
    @Expose
    private String mechanic_cnic;

    @SerializedName("mechanic_profile_img")
    @Expose
    private String mechanic_profile_img;


    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    public Favourite() {
    }

    public Favourite(int mechanic_id, String mechanic_name, String mechanic_email, String mehanic_contact,
                     String mechanic_cnic, String mechanic_profile_img) {
        this.mechanic_id = mechanic_id;
        this.mechanic_name = mechanic_name;
        this.mechanic_email = mechanic_email;
        this.mehanic_contact = mehanic_contact;
        this.mechanic_cnic = mechanic_cnic;
        this.mechanic_profile_img = mechanic_profile_img;
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

    public String getMechanic_email() {
        return mechanic_email;
    }

    public void setMechanic_email(String mechanic_email) {
        this.mechanic_email = mechanic_email;
    }

    public String getMehanic_contact() {
        return mehanic_contact;
    }

    public void setMehanic_contact(String mehanic_contact) {
        this.mehanic_contact = mehanic_contact;
    }

    public String getMechanic_cnic() {
        return mechanic_cnic;
    }

    public void setMechanic_cnic(String mechanic_cnic) {
        this.mechanic_cnic = mechanic_cnic;
    }

    public String getMechanic_profile_img() {
        return mechanic_profile_img;
    }

    public void setMechanic_profile_img(String mechanic_profile_img) {
        this.mechanic_profile_img = mechanic_profile_img;
    }

    public int getFav_id() {
        return fav_id;
    }

    public void setFav_id(int fav_id) {
        this.fav_id = fav_id;
    }

    public int getFav_fk_id() {
        return fav_fk_id;
    }

    public void setFav_fk_id(int fav_fk_id) {
        this.fav_fk_id = fav_fk_id;
    }

    public String getFav_type() {
        return fav_type;
    }

    public void setFav_type(String fav_type) {
        this.fav_type = fav_type;
    }

    public int getFav_user_id() {
        return fav_user_id;
    }

    public void setFav_user_id(int fav_user_id) {
        this.fav_user_id = fav_user_id;
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
