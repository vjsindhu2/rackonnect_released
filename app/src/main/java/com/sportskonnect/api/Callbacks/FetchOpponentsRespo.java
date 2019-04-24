package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.OpponetsDatum;

import java.util.List;

public class FetchOpponentsRespo {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<OpponetsDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<OpponetsDatum> getData() {
        return data;
    }

    public void setData(List<OpponetsDatum> data) {
        this.data = data;
    }
}
