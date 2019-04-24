package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.TourDatum;

import java.util.List;

public class FetchTourMatchRespo {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("data")
    @Expose
    private List<TourDatum> data = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public List<TourDatum> getData() {
        return data;
    }

    public void setData(List<TourDatum> data) {
        this.data = data;
    }
}
