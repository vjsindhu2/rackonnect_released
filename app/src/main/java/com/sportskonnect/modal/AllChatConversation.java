package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllChatConversation {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("fid")
    @Expose
    private String fid;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("status")
    @Expose
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AllChatConversation(String msg, String mid, String fid, String time, String status) {
        this.msg = msg;
        this.mid = mid;
        this.fid = fid;
        this.time = time;
        this.status = status;
    }
}
