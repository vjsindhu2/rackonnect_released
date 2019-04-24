package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.GlobalRankDatum;

import java.util.List;

public class GlobalRankingRespo {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<GlobalRankDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<GlobalRankDatum> getData() {
        return data;
    }

    public void setData(List<GlobalRankDatum> data) {
        this.data = data;
    }
}
