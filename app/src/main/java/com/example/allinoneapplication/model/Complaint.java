package com.example.allinoneapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Complaint {

    @SerializedName("name")
    @Expose
    private String d_name;

    @SerializedName("p_name")
    @Expose
    private String p_name;

    @SerializedName("comp_id")
    @Expose
    private int comp_id;

    @SerializedName("comp_sub")
    @Expose
    private String comp_sub;

    @SerializedName("comp_msg")
    @Expose
    private String comp_msg;

    @SerializedName("fk_c_id")
    @Expose
    private int fk_c_id;

    @SerializedName("fk_m_id")
    @Expose
    private int fk_m_id;

    @SerializedName("comp_stype")
    @Expose
    private String comp_stype;

    @SerializedName("comp_datetime")
    @Expose
    private String comp_datetime;

    @SerializedName("comp_num")
    @Expose
    private String comp_num;

    @SerializedName("comp_status")
    @Expose
    private String comp_status;

    @SerializedName("code")
    @Expose
    private int code;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("message")
    @Expose
    private String message;

    public Complaint() {
    }

    public Complaint(String comp_stype) {
        this.comp_stype = comp_stype;
    }

    public Complaint(int comp_id, String comp_sub) {
        this.comp_id = comp_id;
        this.comp_sub = comp_sub;
    }

    public Complaint(int comp_id, String comp_stype, String comp_status) {
        this.comp_id = comp_id;
        this.comp_stype = comp_stype;
        this.comp_status = comp_status;
    }

    public Complaint(String d_name, int comp_id, String comp_sub, String comp_msg, String comp_datetime, String comp_num, String comp_status) {
        this.d_name = d_name;
        this.comp_id = comp_id;
        this.comp_sub = comp_sub;
        this.comp_msg = comp_msg;
        this.comp_datetime = comp_datetime;
        this.comp_num = comp_num;
        this.comp_status = comp_status;
    }

    public Complaint(String d_name, String p_name, int comp_id, String comp_sub,
                     String comp_msg, String comp_datetime, String comp_num, String comp_status) {
        this.d_name = d_name;
        this.p_name = p_name;
        this.comp_id = comp_id;
        this.comp_sub = comp_sub;
        this.comp_msg = comp_msg;
        this.comp_datetime = comp_datetime;
        this.comp_num = comp_num;
        this.comp_status = comp_status;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public int getComp_id() {
        return comp_id;
    }

    public void setComp_id(int comp_id) {
        this.comp_id = comp_id;
    }

    public String getComp_sub() {
        return comp_sub;
    }

    public void setComp_sub(String comp_sub) {
        this.comp_sub = comp_sub;
    }

    public String getComp_msg() {
        return comp_msg;
    }

    public void setComp_msg(String comp_msg) {
        this.comp_msg = comp_msg;
    }

    public int getFk_c_id() {
        return fk_c_id;
    }

    public void setFk_c_id(int fk_c_id) {
        this.fk_c_id = fk_c_id;
    }

    public int getFk_m_id() {
        return fk_m_id;
    }

    public void setFk_m_id(int fk_m_id) {
        this.fk_m_id = fk_m_id;
    }

    public String getComp_stype() {
        return comp_stype;
    }

    public void setComp_stype(String comp_stype) {
        this.comp_stype = comp_stype;
    }

    public String getComp_datetime() {
        return comp_datetime;
    }

    public void setComp_datetime(String comp_datetime) {
        this.comp_datetime = comp_datetime;
    }

    public String getComp_num() {
        return comp_num;
    }

    public void setComp_num(String comp_num) {
        this.comp_num = comp_num;
    }

    public String getComp_status() {
        return comp_status;
    }

    public void setComp_status(String comp_status) {
        this.comp_status = comp_status;
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
