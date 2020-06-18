package com.summer.demo.ui.mine.contact.bean;

import java.io.Serializable;

/**
 * Created by xiastars on 2017/11/4.
 */

public class AskUserInfo implements Serializable {

    private int freeTimes;
    private int gradeCode;
    private String gradeName;
    private int needIntegral;
    private int rank;
    private String realName;
    private int totalIntegral;
    private String userImg;
    private int weekIntegral;
    private int winRate;
    String hangingImg;
    int qusIntegral;

    public int getQusIntegral() {
        return qusIntegral;
    }

    public void setQusIntegral(int qusIntegral) {
        this.qusIntegral = qusIntegral;
    }

    public String getHangingImg() {
        return hangingImg;
    }

    public void setHangingImg(String hangingImg) {
        this.hangingImg = hangingImg;
    }

    public int getFreeTimes() {
        return freeTimes;
    }

    public void setFreeTimes(int freeTimes) {
        this.freeTimes = freeTimes;
    }

    public int getGradeCode() {
        return gradeCode;
    }

    public void setGradeCode(int gradeCode) {
        this.gradeCode = gradeCode;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getNeedIntegral() {
        return needIntegral;
    }

    public void setNeedIntegral(int needIntegral) {
        this.needIntegral = needIntegral;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(int totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public int getWeekIntegral() {
        return weekIntegral;
    }

    public void setWeekIntegral(int weekIntegral) {
        this.weekIntegral = weekIntegral;
    }

    public int getWinRate() {
        return winRate;
    }

    public void setWinRate(int winRate) {
        this.winRate = winRate;
    }
}
