package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.DualMatchListDatum;

import java.util.List;

public class MatchListDualResponse {


    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<DualMatchListDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<DualMatchListDatum> getData() {
        return data;
    }

    public void setData(List<DualMatchListDatum> data) {
        this.data = data;
    }
}
