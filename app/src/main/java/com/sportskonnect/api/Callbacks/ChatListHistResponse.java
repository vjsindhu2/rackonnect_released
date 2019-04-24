package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.ChatHistDatum;

import java.util.List;

public class ChatListHistResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<ChatHistDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<ChatHistDatum> getData() {
        return data;
    }

    public void setData(List<ChatHistDatum> data) {
        this.data = data;
    }
}
