package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;

public class MemberInfo implements Serializable {

    private int black_count, count, admin_count;
    int watermark;

    public int getWatermark() {
        return watermark;
    }

    public void setWatermark(int watermark) {
        this.watermark = watermark;
    }

    private int userType;  // 0 表示星主  1 表示管理员  2 表示成员  3 表示自己

    private boolean blackViewFlag = false;

    public int getAdmin_count() {
        return admin_count;
    }

    public void setAdmin_count(int admin_count) {
        this.admin_count = admin_count;
    }

    public boolean isBlackViewFlag() {
        return blackViewFlag;
    }

    public void setBlackViewFlag(boolean blackViewFlag) {
        this.blackViewFlag = blackViewFlag;
    }

    private String user_id, user_name, user_avatar_url, identity, invitate_num, recode_id, remark_name, join_time, user_alias, is_black, pay_status, rank;


    public int getBlack_count() {
        return black_count;
    }

    public void setBlack_count(int black_count) {
        this.black_count = black_count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avatar_url() {
        return user_avatar_url;
    }

    public void setUser_avatar_url(String user_avatar_url) {
        this.user_avatar_url = user_avatar_url;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getInvitate_num() {
        return invitate_num;
    }

    public void setInvitate_num(String invitate_num) {
        this.invitate_num = invitate_num;
    }

    public String getRecode_id() {
        return recode_id;
    }

    public void setRecode_id(String recode_id) {
        this.recode_id = recode_id;
    }

    public String getRemark_name() {
        return remark_name;
    }

    public void setRemark_name(String remark_name) {
        this.remark_name = remark_name;
    }

    public String getJoin_time() {
        return join_time;
    }

    public void setJoin_time(String join_time) {
        this.join_time = join_time;
    }

    public String getUser_alias() {
        return user_alias;
    }

    public void setUser_alias(String user_alias) {
        this.user_alias = user_alias;
    }

    public String getIs_black() {
        return is_black;
    }

    public void setIs_black(String is_black) {
        this.is_black = is_black;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    private String end_id;

    public String getEnd_id() {
        return end_id;
    }

    public void setEnd_id(String end_id) {
        this.end_id = end_id;
    }


}
