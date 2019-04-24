package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GlobalRankDatum {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("profileimage")
    @Expose
    private String profileimage;
    @SerializedName("level")
    @Expose
    private String level;
@SerializedName("gender")
    @Expose
    private String gender_opp;
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
    @SerializedName("scoretotal")
    @Expose
    private Integer scoretotal;
    @SerializedName("turnup")
    @Expose
    private Integer turnup;

    public GlobalRankDatum(String name, String profileimage, Integer games, Integer loss, Integer win, Integer per, Integer scoretotal, Integer turnup, String level, String userid,String gender_opp) {
        this.name = name;
        this.profileimage = profileimage;
        this.games = games;
        this.loss = loss;
        this.win = win;
        this.per = per;
        this.scoretotal = scoretotal;
        this.turnup = turnup;
        this.level = level;
        this.userid = userid;
        this.gender_opp = gender_opp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getGender_opp() {
        return gender_opp;
    }

    public void setGender_opp(String gender_opp) {
        this.gender_opp = gender_opp;
    }
}
