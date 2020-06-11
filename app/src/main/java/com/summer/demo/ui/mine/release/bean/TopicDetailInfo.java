package com.summer.demo.ui.mine.release.bean;

import android.text.SpannableStringBuilder;

import java.io.Serializable;

public class TopicDetailInfo implements Serializable {

    private int category;//主题分类：0=普通主题，1=提问主题
    private String id;
    private GroupUserInfo user;
    private int sendStatus;
    private String groupId;
    private long topic_add_time;
    private boolean localSticky;
    private GroupUserInfo current_user;
    private GroupUserInfo answerer;
    private String localSubjectId;
    String sort_time;
    private GroupUserInfo group_owner;
    boolean is_read;
    transient SpannableStringBuilder builder;//本地保存
    transient String formateTime;//本地格式化时间
    private PolicyInfo group_policy;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    boolean localVideoPlayStatus = false;//本地视频播放状态

    public boolean isLocalVideoPlayStatus() {
        return localVideoPlayStatus;
    }

    public void setLocalVideoPlayStatus(boolean localVideoPlayStatus) {
        this.localVideoPlayStatus = localVideoPlayStatus;
    }

    public String getFormateTime() {
        return formateTime;
    }

    public void setFormateTime(String formateTime) {
        this.formateTime = formateTime;
    }

    public SpannableStringBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(SpannableStringBuilder builder) {
        this.builder = builder;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }

    public GroupUserInfo getGroup_owner() {
        return group_owner;
    }

    public void setGroup_owner(GroupUserInfo group_owner) {
        this.group_owner = group_owner;
    }

    public String getSort_time() {
        return sort_time;
    }

    public void setSort_time(String sort_time) {
        this.sort_time = sort_time;
    }

    public String getLocalSubjectId() {
        return localSubjectId;
    }

    public void setLocalSubjectId(String localSubjectId) {
        this.localSubjectId = localSubjectId;
    }

    public GroupUserInfo getAnswerer() {
        if(answerer == null){
            return new GroupUserInfo();
        }
        return answerer;
    }

    public void setAnswerer(GroupUserInfo answerer) {
        this.answerer = answerer;
    }

    public GroupUserInfo getCurrent_user() {
        return current_user;
    }

    public void setCurrent_user(GroupUserInfo current_user) {
        this.current_user = current_user;
    }


    public boolean isLocalSticky() {
        return localSticky;
    }

    public void setLocalSticky(boolean localSticky) {
        this.localSticky = localSticky;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTopic_add_time() {
        return topic_add_time;
    }

    public void setTopic_add_time(long topic_add_time) {
        this.topic_add_time = topic_add_time;
    }


    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }


    public GroupUserInfo getUser() {
        return user;
    }

    public void setUser(GroupUserInfo user) {
        this.user = user;
    }



}
