package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StartScoringResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("startflag")
    @Expose
    private String startflag;
    @SerializedName("diff")
    @Expose
    private Integer diff;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getStartflag() {
        return startflag;
    }

    public void setStartflag(String startflag) {
        this.startflag = startflag;
    }

    public Integer getDiff() {
        return diff;
    }

    public void setDiff(Integer diff) {
        this.diff = diff;
    }
}
