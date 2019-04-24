package com.sportskonnect.api.Callbacks;

/*
-----------------------------------com.sportskonnect.MainTextMatchedSubstring.java-----------------------------------
*/



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.Prediction;

import java.util.List;

public class PlaceAPIRESPO {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}




