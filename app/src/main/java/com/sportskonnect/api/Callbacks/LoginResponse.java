package com.sportskonnect.api.Callbacks;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sportskonnect.modal.Gameinfo;
import com.sportskonnect.modal.SelectedGameModal;

import java.util.List;

public class LoginResponse {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("otp")
    @Expose
    private Integer otp;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("myreferal")
    @Expose
    private String myreferal;


    @SerializedName("gamenotipush")
    @Expose
    private String gamenotipush;
    @SerializedName("cancelnotipush")
    @Expose
    private String cancelnotipush;

    @SerializedName("scorenotipush")
    @Expose
    private String scorenotipush;

    @SerializedName("gamenotiemail")
    @Expose
    private String gamenotiemail;


    @SerializedName("scorenotiemail")
    @Expose
    private String scorenotiemail;

    @SerializedName("cancelnotiemail")
    @Expose
    private String cancelnotiemail;


    @SerializedName("sound")
    @Expose
    private String sound;
    @SerializedName("chat")
    @Expose
    private String chat;


    @SerializedName("gameinfo")
    @Expose
    private List<SelectedGameModal> gameinfo = null;


    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getOtp() {
        return otp;
    }

    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public List<SelectedGameModal> getGameinfo() {
        return gameinfo;
    }

    public void setGameinfo(List<SelectedGameModal> gameinfo) {
        this.gameinfo = gameinfo;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGamenotipush() {
        return gamenotipush;
    }

    public void setGamenotipush(String gamenotipush) {
        this.gamenotipush = gamenotipush;
    }

    public String getCancelnotipush() {
        return cancelnotipush;
    }

    public void setCancelnotipush(String cancelnotipush) {
        this.cancelnotipush = cancelnotipush;
    }

    public String getScorenotipush() {
        return scorenotipush;
    }

    public void setScorenotipush(String scorenotipush) {
        this.scorenotipush = scorenotipush;
    }

    public String getGamenotiemail() {
        return gamenotiemail;
    }

    public void setGamenotiemail(String gamenotiemail) {
        this.gamenotiemail = gamenotiemail;
    }

    public String getScorenotiemail() {
        return scorenotiemail;
    }

    public void setScorenotiemail(String scorenotiemail) {
        this.scorenotiemail = scorenotiemail;
    }

    public String getCancelnotiemail() {
        return cancelnotiemail;
    }

    public void setCancelnotiemail(String cancelnotiemail) {
        this.cancelnotiemail = cancelnotiemail;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getMyreferal() {
        return myreferal;
    }

    public void setMyreferal(String myreferal) {
        this.myreferal = myreferal;
    }
}
