package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.UserRankDatum;

import java.util.List;

public class UserRankingResponse {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("scoretotal")
    @Expose
    private String scoretotal;


    @SerializedName("data")
    @Expose
    private List<UserRankDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<UserRankDatum> getData() {
        return data;
    }

    public void setData(List<UserRankDatum> data) {
        this.data = data;
    }

    public String getScoretotal() {
        return scoretotal;
    }

    public void setScoretotal(String scoretotal) {
        this.scoretotal = scoretotal;
    }
}
