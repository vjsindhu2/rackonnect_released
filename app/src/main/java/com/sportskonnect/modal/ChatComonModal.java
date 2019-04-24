package com.sportskonnect.modal;

public class ChatComonModal {

    String msg;
    String time;
    String mid;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }


    public ChatComonModal(String msg, String time, String mid) {
        this.msg = msg;
        this.time = time;
        this.mid = mid;
    }
}
