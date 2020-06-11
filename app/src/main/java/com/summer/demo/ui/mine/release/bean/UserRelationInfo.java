package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;

public class UserRelationInfo implements Serializable {
    int identity;
    boolean is_valid;

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public boolean isIs_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }
}
