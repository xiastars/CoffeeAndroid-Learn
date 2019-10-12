package com.summer.demo.ui.module.comment;

import com.summer.demo.bean.UserInfo;

import java.io.Serializable;
import java.util.List;

public class ChatTopInfo implements Serializable {
    int total;
    int unread;
    List<ChatInfo> list;
    UserInfo receiver;
    UserInfo sender;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public List<ChatInfo> getList() {
        return list;
    }

    public void setList(List<ChatInfo> list) {
        this.list = list;
    }

    public UserInfo getReceiver() {
        return receiver;
    }

    public void setReceiver(UserInfo receiver) {
        this.receiver = receiver;
    }

    public UserInfo getSender() {
        return sender;
    }

    public void setSender(UserInfo sender) {
        this.sender = sender;
    }
}