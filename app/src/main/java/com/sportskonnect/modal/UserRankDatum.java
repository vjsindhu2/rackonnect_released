package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRankDatum {

    @SerializedName("catname")
    @Expose
    private String catname;
    @SerializedName("catid")
    @Expose
    private String catid;
    @SerializedName("games")
    @Expose
    private Integer games;
    @SerializedName("loss")
    @Expose
    private Integer loss;
    @SerializedName("win")
    @Expose
    private Integer win;
    @SerializedName("per")
    @Expose
    private Integer per;
@SerializedName("score")
    @Expose
    private String score;

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
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

    public Integer getPer() {
        return per;
    }

    public void setPer(Integer per) {
        this.per = per;
    }


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public UserRankDatum(String catname, String catid, Integer games, Integer loss, Integer win, Integer per,String score) {
        this.catname = catname;
        this.catid = catid;
        this.games = games;
        this.loss = loss;
        this.win = win;
        this.per = per;
        this.score = score;
    }
}
