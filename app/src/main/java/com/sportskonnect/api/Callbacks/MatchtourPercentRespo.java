package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchtourPercentRespo {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("winner_per")
    @Expose
    private Integer winnerPer;
    @SerializedName("runnerup_per")
    @Expose
    private Integer runnerupPer;
    @SerializedName("company_per")
    @Expose
    private Integer companyPer;
    @SerializedName("host_per")
    @Expose
    private Integer hostPer;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getWinnerPer() {
        return winnerPer;
    }

    public void setWinnerPer(Integer winnerPer) {
        this.winnerPer = winnerPer;
    }

    public Integer getRunnerupPer() {
        return runnerupPer;
    }

    public void setRunnerupPer(Integer runnerupPer) {
        this.runnerupPer = runnerupPer;
    }

    public Integer getCompanyPer() {
        return companyPer;
    }

    public void setCompanyPer(Integer companyPer) {
        this.companyPer = companyPer;
    }

    public Integer getHostPer() {
        return hostPer;
    }

    public void setHostPer(Integer hostPer) {
        this.hostPer = hostPer;
    }

}
