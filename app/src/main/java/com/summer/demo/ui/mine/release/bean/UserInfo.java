package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {


    private String id;
    private String avatar;
    private String name;
    private String mobile;
    private int money;
    private int apple_balance;
    private String real_name;
    private String identity_no;
    private int times_for_identify_one_day;
    private int times_for_identify;
    String user_introduction;
    private String main_id;
    String cover_url;
    GroupUserInfo current_user;
    PolicyInfo group_policy;
    String group_id;
    boolean is_self;
    int watermark;

    public int getWatermark() {
        return watermark;
    }

    public void setWatermark(int watermark) {
        this.watermark = watermark;
    }

    public boolean isIs_self() {
        return is_self;
    }

    public void setIs_self(boolean is_self) {
        this.is_self = is_self;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public PolicyInfo getGroup_policy() {
        if(group_policy == null){
            return new PolicyInfo();
        }
        return group_policy;
    }

    public void setGroup_policy(PolicyInfo group_policy) {
        this.group_policy = group_policy;
    }

    public GroupUserInfo getCurrent_user() {
        if(current_user == null){
            current_user = new GroupUserInfo();
        }
        return current_user;
    }

    public void setCurrent_user(GroupUserInfo current_user) {
        this.current_user = current_user;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getMain_id() {
        return main_id;
    }

    public void setMain_id(String main_id) {
        this.main_id = main_id;
    }

    public String getUser_introduction() {
        return user_introduction;
    }

    public void setUser_introduction(String user_introduction) {
        this.user_introduction = user_introduction;
    }

    public int getTimes_for_identify_one_day() {
        return times_for_identify_one_day;
    }

    public void setTimes_for_identify_one_day(int times_for_identify_one_day) {
        this.times_for_identify_one_day = times_for_identify_one_day;
    }

    public int getTimes_for_identify() {
        return times_for_identify;
    }

    public void setTimes_for_identify(int times_for_identify) {
        this.times_for_identify = times_for_identify;
    }

    public String getIdentity_no() {
        return identity_no;
    }

    public void setIdentity_no(String identity_no) {
        this.identity_no = identity_no;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getApple_balance() {
        return apple_balance;
    }

    public void setApple_balance(int apple_balance) {
        this.apple_balance = apple_balance;
    }

}
