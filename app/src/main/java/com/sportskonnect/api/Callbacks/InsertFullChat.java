package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.AllChatConversation;

import java.util.List;

public class InsertFullChat {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<AllChatConversation> data = null;
    @SerializedName("onlineflag")
    @Expose
    private String onlineflag;
    @SerializedName("time")
    @Expose
    private String time;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<AllChatConversation> getData() {
        return data;
    }

    public void setData(List<AllChatConversation> data) {
        this.data = data;
    }

    public String getOnlineflag() {
        return onlineflag;
    }

    public void setOnlineflag(String onlineflag) {
        this.onlineflag = onlineflag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public InsertFullChat() {
    }
}
