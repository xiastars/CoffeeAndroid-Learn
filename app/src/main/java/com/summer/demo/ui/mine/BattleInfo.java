package com.summer.demo.ui.mine;

import java.io.Serializable;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/7/3 17:07
 */
public class BattleInfo implements Serializable {

    String leftName;
    int leftAvatar;
    String leftShare;
    float leftShareValue;
    float leftSharePreValue;
    String rightName;
    int rightAvatar;
    String rightShare;
    float rightShareValue;
    float rightSharePreValue;
    String fromTime;

    public String getLeftName() {
        return leftName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    public int getLeftAvatar() {
        return leftAvatar;
    }

    public void setLeftAvatar(int leftAvatar) {
        this.leftAvatar = leftAvatar;
    }

    public String getLeftShare() {
        return leftShare;
    }

    public void setLeftShare(String leftShare) {
        this.leftShare = leftShare;
    }

    public float getLeftShareValue() {
        return leftShareValue;
    }

    public void setLeftShareValue(float leftShareValue) {
        this.leftShareValue = leftShareValue;
    }

    public float getLeftSharePreValue() {
        return leftSharePreValue;
    }

    public void setLeftSharePreValue(float leftSharePreValue) {
        this.leftSharePreValue = leftSharePreValue;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public int getRightAvatar() {
        return rightAvatar;
    }

    public void setRightAvatar(int rightAvatar) {
        this.rightAvatar = rightAvatar;
    }

    public String getRightShare() {
        return rightShare;
    }

    public void setRightShare(String rightShare) {
        this.rightShare = rightShare;
    }

    public float getRightShareValue() {
        return rightShareValue;
    }

    public void setRightShareValue(float rightShareValue) {
        this.rightShareValue = rightShareValue;
    }

    public float getRightSharePreValue() {
        return rightSharePreValue;
    }

    public void setRightSharePreValue(float rightSharePreValue) {
        this.rightSharePreValue = rightSharePreValue;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }
}
