package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.MatchDatum;

import java.util.List;

public class MatchListResponse {


    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<MatchDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<MatchDatum> getData() {
        return data;
    }

    public void setData(List<MatchDatum> data) {
        this.data = data;
    }
}
