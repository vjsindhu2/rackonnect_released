package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FetchSavedLocationRespo {

    @SerializedName("error")
    @Expose
    private Boolean error;
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
    @SerializedName("minage")
    @Expose
    private String minage;
    @SerializedName("maxage")
    @Expose
    private String maxage;
    @SerializedName("minfees")
    @Expose
    private String minfees;

    @SerializedName("onlineflag")
    @Expose
    private String onlineflag;

    @SerializedName("maxfees")
    @Expose
    private String maxfees;
    @SerializedName("citynamearray")
    @Expose
    private List<Citynamearray> citynamearray = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
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

    public String getMinage() {
        return minage;
    }

    public void setMinage(String minage) {
        this.minage = minage;
    }

    public String getMaxage() {
        return maxage;
    }

    public void setMaxage(String maxage) {
        this.maxage = maxage;
    }

    public String getMinfees() {
        return minfees;
    }

    public void setMinfees(String minfees) {
        this.minfees = minfees;
    }

    public String getMaxfees() {
        return maxfees;
    }

    public void setMaxfees(String maxfees) {
        this.maxfees = maxfees;
    }

    public List<Citynamearray> getCitynamearray() {
        return citynamearray;
    }

    public void setCitynamearray(List<Citynamearray> citynamearray) {
        this.citynamearray = citynamearray;
    }

    public String getOnlineflag() {
        return onlineflag;
    }

    public void setOnlineflag(String onlineflag) {
        this.onlineflag = onlineflag;
    }
}
