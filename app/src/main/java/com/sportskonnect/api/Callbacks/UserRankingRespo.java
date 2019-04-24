package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRankingRespo {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("per")
    @Expose
    private Integer per;
    @SerializedName("scoretotal")
    @Expose
    private Integer scoretotal;
    @SerializedName("turnup")
    @Expose
    private Integer turnup;
    @SerializedName("games")
    @Expose
    private Integer games;
    @SerializedName("loss")
    @Expose
    private Integer loss;
    @SerializedName("win")
    @Expose
    private Integer win;
    @SerializedName("scoreuser")
    @Expose
    private String scoreuser;
@SerializedName("msg")
    @Expose
    private String msg;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getPer() {
        return per;
    }

    public void setPer(Integer per) {
        this.per = per;
    }

    public Integer getScoretotal() {
        return scoretotal;
    }

    public void setScoretotal(Integer scoretotal) {
        this.scoretotal = scoretotal;
    }

    public Integer getTurnup() {
        return turnup;
    }

    public void setTurnup(Integer turnup) {
        this.turnup = turnup;
    }

    public Integer getGames() {
        return games;
    }

    public void setGames(Integer games) {
        this.games = games;
    }

    public Integer getLoss() {
        return loss;
    }

    public void setLoss(Integer loss) {
        this.loss = loss;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }

    public String getScoreuser() {
        return scoreuser;
    }

    public void setScoreuser(String scoreuser) {
        this.scoreuser = scoreuser;
    }
}
