package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpponetsDatum {
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("levelid")
    @Expose
    private int levelid;
    @SerializedName("catid")
    @Expose
    private int catid;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("radius")
    @Expose
    private String radius;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("statusflag")
    @Expose
    private String statusflag;
    @SerializedName("time")
    @Expose
    private String time;
@SerializedName("points")
    @Expose
    private String points;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getLevelid() {
        return levelid;
    }

    public void setLevelid(int levelid) {
        this.levelid = levelid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public OpponetsDatum(String userid, int levelid, int catid, String name, String email, String mobileNumber, String gender, String lat, String lng, String address, String radius, String image, String statusflag,String time,String points ) {
        this.userid = userid;
        this.levelid = levelid;
        this.catid = catid;
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.radius = radius;
        this.image = image;
        this.statusflag = statusflag;
        this.time = time;
        this.points = points;
    }

    public String getStatusflag() {
        return statusflag;
    }

    public void setStatusflag(String statusflag) {
        this.statusflag = statusflag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
