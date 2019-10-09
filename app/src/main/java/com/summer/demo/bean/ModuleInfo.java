package com.summer.demo.bean;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 11:39
 */
public class ModuleInfo {

    public ModuleInfo(int res, String title) {
        this.res = res;
        this.title = title;
    }

    int res;
    String title;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
