package com.sportskonnect.modal;

public class ChatDualModal {

    private String name;
    private String message;
    private String Date;
    private String mid;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public ChatDualModal(String mid,String name, String message, String date) {
        this.mid = mid;
        this.name = name;
        this.message = message;
        Date = date;
    }
}
