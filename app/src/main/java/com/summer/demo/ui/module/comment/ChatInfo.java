package com.summer.demo.ui.module.comment;

import com.summer.demo.bean.UserInfo;

import java.io.Serializable;

public class ChatInfo implements Serializable {
    String id;
    int type;
    String text;
    long send_time;
    UserInfo user;
    int requestCode;
    boolean sendError;
    boolean showTime;

    public boolean isShowTime() {
        return showTime;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public boolean isSendError() {
        return sendError;
    }

    public void setSendError(boolean sendError) {
        this.sendError = sendError;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getSend_time() {
        return send_time;
    }

    public void setSend_time(long send_time) {
        this.send_time = send_time;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }
}
