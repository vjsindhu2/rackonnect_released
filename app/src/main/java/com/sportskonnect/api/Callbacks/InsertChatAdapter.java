package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InsertChatAdapter {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("lid")
    @Expose
    private String lid;

    @SerializedName("mid")
    @Expose
    private String mid;

    @SerializedName("fid")
    @Expose
    private String fid;

    @SerializedName("time")
    @Expose
    private String time;



    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public InsertChatAdapter(Boolean error, String errorMsg, String status, String msg, String lid, String mid, String fid, String time) {
        this.error = error;
        this.errorMsg = errorMsg;
        this.status = status;
        this.msg = msg;
        this.lid = lid;
        this.mid = mid;
        this.fid = fid;
        this.time = time;
    }


}
