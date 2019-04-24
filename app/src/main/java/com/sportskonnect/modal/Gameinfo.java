package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gameinfo {

    @SerializedName("catid")
    @Expose
    private String catid;
    @SerializedName("levelid")
    @Expose
    private String levelid;

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getLevelid() {
        return levelid;
    }

    public void setLevelid(String levelid) {
        this.levelid = levelid;
    }

    public Gameinfo(String catid, String levelid) {
        this.catid = catid;
        this.levelid = levelid;
    }
}