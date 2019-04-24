package com.sportskonnect.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WinnerArray {

    @SerializedName("fid")
    @Expose
    private String fid;
    @SerializedName("fidname")
    @Expose
    private String fidname;
    @SerializedName("fidimage")
    @Expose
    private String fidimage;
    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("memberid")
    @Expose
    private String memberid;
@SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("fidlevel")
    @Expose
    private String fidlevel;
 @SerializedName("verify")
    @Expose
    private String verify;

@SerializedName("type")
    @Expose
    private String type;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFidname() {
        return fidname;
    }

    public void setFidname(String fidname) {
        this.fidname = fidname;
    }

    public String getFidimage() {
        return fidimage;
    }

    public void setFidimage(String fidimage) {
        this.fidimage = fidimage;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getFidlevel() {
        return fidlevel;
    }

    public void setFidlevel(String fidlevel) {
        this.fidlevel = fidlevel;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
