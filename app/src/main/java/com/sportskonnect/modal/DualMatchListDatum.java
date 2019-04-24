package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DualMatchListDatum {

    @SerializedName("matchid")
    @Expose
    private String matchid;
    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("midname")
    @Expose
    private String midname;
    @SerializedName("midimage")
    @Expose
    private String midimage;
    @SerializedName("matchname")
    @Expose
    private String matchname;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("midlevel")
    @Expose
    private String midlevel;
    @SerializedName("fidlevel")
    @Expose
    private Object fidlevel;
    @SerializedName("startflag")
    @Expose
    private String startflag;
    @SerializedName("catid")
    @Expose
    private String catid;
    @SerializedName("status")
    @Expose
    private String status;
  @SerializedName("matchstatus")
    @Expose
    private String matchstatus;
    @SerializedName("midpoint")
    @Expose
    private String midpoint;
    @SerializedName("fidpoint")
    @Expose
    private String fidpoint;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("validatetime")
    @Expose
    private String validatetime;
    @SerializedName("groupid")
    @Expose
    private String groupid;
    @SerializedName("matchtype")
    @Expose
    private String matchtype;
@SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("fidarray")
    @Expose
    private List<Fidarray> fidarray = null;

    public DualMatchListDatum(String matchid, String mid, String midname, String midimage, String matchname, String lat, String lng, String address, String date, String startTime, String endTime, String midlevel, Object fidlevel, String startflag, String catid, String status, String midpoint, String fidpoint, String type, String validatetime, String groupid, List<Fidarray> fidarray,String matchtype,String matchstatus,String gender) {
        this.matchid = matchid;
        this.mid = mid;
        this.midname = midname;
        this.midimage = midimage;
        this.matchname = matchname;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.midlevel = midlevel;
        this.fidlevel = fidlevel;
        this.startflag = startflag;
        this.catid = catid;
        this.status = status;
        this.midpoint = midpoint;
        this.fidpoint = fidpoint;
        this.type = type;
        this.validatetime = validatetime;
        this.groupid = groupid;
        this.fidarray = fidarray;
        this.matchtype = matchtype;
        this.matchstatus = matchstatus;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMatchstatus() {
        return matchstatus;
    }

    public void setMatchstatus(String matchstatus) {
        this.matchstatus = matchstatus;
    }

    public String getMatchid() {
        return matchid;
    }

    public void setMatchid(String matchid) {
        this.matchid = matchid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMidname() {
        return midname;
    }

    public void setMidname(String midname) {
        this.midname = midname;
    }

    public String getMidimage() {
        return midimage;
    }

    public void setMidimage(String midimage) {
        this.midimage = midimage;
    }

    public String getMatchname() {
        return matchname;
    }

    public void setMatchname(String matchname) {
        this.matchname = matchname;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMidlevel() {
        return midlevel;
    }

    public void setMidlevel(String midlevel) {
        this.midlevel = midlevel;
    }

    public Object getFidlevel() {
        return fidlevel;
    }

    public void setFidlevel(Object fidlevel) {
        this.fidlevel = fidlevel;
    }

    public String getStartflag() {
        return startflag;
    }

    public void setStartflag(String startflag) {
        this.startflag = startflag;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMidpoint() {
        return midpoint;
    }

    public void setMidpoint(String midpoint) {
        this.midpoint = midpoint;
    }

    public String getFidpoint() {
        return fidpoint;
    }

    public void setFidpoint(String fidpoint) {
        this.fidpoint = fidpoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidatetime() {
        return validatetime;
    }

    public void setValidatetime(String validatetime) {
        this.validatetime = validatetime;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public List<Fidarray> getFidarray() {
        return fidarray;
    }

    public void setFidarray(List<Fidarray> fidarray) {
        this.fidarray = fidarray;
    }

    public String getMatchtype() {
        return matchtype;
    }

    public void setMatchtype(String matchtype) {
        this.matchtype = matchtype;
    }
}
