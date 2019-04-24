package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameAddDatum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("catid")
    @Expose
    private String catid;
    @SerializedName("levelid")
    @Expose
    private String levelid;
    @SerializedName("userid")
    @Expose
    private String userid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public GameAddDatum(String id, String catid, String levelid, String userid) {
        this.id = id;
        this.catid = catid;
        this.levelid = levelid;
        this.userid = userid;
    }
}
