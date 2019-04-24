package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckTourTimeRespo {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("join")
    @Expose
    private Integer join;
    @SerializedName("endgame")
    @Expose
    private Integer endgame;
    @SerializedName("diff")
    @Expose
    private String  diff;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("matchstatus")
    @Expose
    private String matchstatus;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getJoin() {
        return join;
    }

    public void setJoin(Integer join) {
        this.join = join;
    }

    public Integer getEndgame() {
        return endgame;
    }

    public void setEndgame(Integer endgame) {
        this.endgame = endgame;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatchstatus() {
        return matchstatus;
    }

    public void setMatchstatus(String matchstatus) {
        this.matchstatus = matchstatus;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
