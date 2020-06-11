package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;
import java.util.List;

public class GroupUserAllInfo implements Serializable {
    private int total;
    private List<GroupUserInfo> list;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<GroupUserInfo> getList() {
        return list;
    }

    public void setList(List<GroupUserInfo> list) {
        this.list = list;
    }
}
