package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TourDatum {


    @SerializedName("matchid")
    @Expose
    private String matchid;
    @SerializedName("mid")
    @Expose
    private String mid;
    @SerializedName("midname")
    @Expose
    private String midname;
 @SerializedName("gender")
    @Expose
    private String gender;
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
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("midlevel")
    @Expose
    private String midlevel;
    @SerializedName("startflag")
    @Expose
    private String startflag;
    @SerializedName("catid")
    @Expose
    private String catid;
    @SerializedName("status")
    @Expose
    private String status;
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
    @SerializedName("matchstatus")
    @Expose
    private String matchstatus;
    @SerializedName("matchtype")
    @Expose
    private String matchtype;
    @SerializedName("groupid")
    @Expose
    private String groupid;
    @SerializedName("fidarray")
    @Expose
    private List<TourFidarray> fidarray = null;

    @SerializedName("winnerarray")
    @Expose
    private List<WinnerArray> winnerarray = null;

    @SerializedName("group_member")
    @Expose
    private String groupMember;
    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("endgameflag")
    @Expose
    private String endgame;

    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("txn_id")
    @Expose
    private String txnId;
    @SerializedName("runnerup_amount")
    @Expose
    private String runnerupAmount;
    @SerializedName("winner_amount")
    @Expose
    private String winnerAmount;
    @SerializedName("company_amount")
    @Expose
    private String companyAmount;
    @SerializedName("host_amount")
    @Expose
    private String hostAmount;

    public TourDatum(String matchid, String mid, String midname, String midimage, String matchname, String lat, String lng, String address, String date, String time, String midlevel, String startflag, String catid, String status, String midpoint, String fidpoint, String type, String validatetime, String matchstatus, String matchtype, String groupid, List<TourFidarray> fidarray, String groupMember, String amount, String totalAmount, String txnId, String runnerupAmount, String winnerAmount, String companyAmount, String hostAmount, List<WinnerArray> winnerArrayList, String endgame,String gender) {
        this.matchid = matchid;
        this.mid = mid;
        this.midname = midname;
        this.midimage = midimage;
        this.matchname = matchname;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.date = date;
        this.time = time;
        this.midlevel = midlevel;
        this.startflag = startflag;
        this.catid = catid;
        this.status = status;
        this.midpoint = midpoint;
        this.fidpoint = fidpoint;
        this.type = type;
        this.validatetime = validatetime;
        this.matchstatus = matchstatus;
        this.matchtype = matchtype;
        this.groupid = groupid;
        this.fidarray = fidarray;
        this.groupMember = groupMember;
        this.amount = amount;
        this.totalAmount = totalAmount;
        this.txnId = txnId;
        this.runnerupAmount = runnerupAmount;
        this.winnerAmount = winnerAmount;
        this.companyAmount = companyAmount;
        this.hostAmount = hostAmount;
        this.winnerarray = winnerArrayList;
        this.endgame = endgame;
        this.gender =gender;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMidlevel() {
        return midlevel;
    }

    public void setMidlevel(String midlevel) {
        this.midlevel = midlevel;
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

    public List<WinnerArray> getWinnerarray() {
        return winnerarray;
    }

    public void setWinnerarray(List<WinnerArray> winnerarray) {
        this.winnerarray = winnerarray;
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

    public String getMatchstatus() {
        return matchstatus;
    }

    public void setMatchstatus(String matchstatus) {
        this.matchstatus = matchstatus;
    }

    public String getMatchtype() {
        return matchtype;
    }

    public void setMatchtype(String matchtype) {
        this.matchtype = matchtype;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public List<TourFidarray> getFidarray() {
        return fidarray;
    }

    public void setFidarray(List<TourFidarray> fidarray) {
        this.fidarray = fidarray;
    }

    public String getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(String groupMember) {
        this.groupMember = groupMember;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getRunnerupAmount() {
        return runnerupAmount;
    }

    public void setRunnerupAmount(String runnerupAmount) {
        this.runnerupAmount = runnerupAmount;
    }

    public String getWinnerAmount() {
        return winnerAmount;
    }

    public void setWinnerAmount(String winnerAmount) {
        this.winnerAmount = winnerAmount;
    }

    public String getCompanyAmount() {
        return companyAmount;
    }

    public void setCompanyAmount(String companyAmount) {
        this.companyAmount = companyAmount;
    }

    public String getHostAmount() {
        return hostAmount;
    }

    public void setHostAmount(String hostAmount) {
        this.hostAmount = hostAmount;
    }

    public String getEndgame() {
        return endgame;
    }

    public void setEndgame(String endgame) {
        this.endgame = endgame;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
