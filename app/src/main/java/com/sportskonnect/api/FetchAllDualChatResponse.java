package com.sportskonnect.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.DualAllChatDatum;

import java.util.List;

public class FetchAllDualChatResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<DualAllChatDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<DualAllChatDatum> getData() {
        return data;
    }

    public void setData(List<DualAllChatDatum> data) {
        this.data = data;
    }
}
