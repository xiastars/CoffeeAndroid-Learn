package com.summer.demo.ui.mine.release.bean;

import java.io.Serializable;
import java.util.List;

public class SubjectTopInfo implements Serializable {
    List<SubjectInfo> list;
    List<SubjectInfo> default_tag;
    List<SubjectInfo> recent;

    public List<SubjectInfo> getRecent() {
        return recent;
    }

    public void setRecent(List<SubjectInfo> recent) {
        this.recent = recent;
    }

    public List<SubjectInfo> getDefault_tag() {
        return default_tag;
    }

    public void setDefault_tag(List<SubjectInfo> default_tag) {
        this.default_tag = default_tag;
    }

    public List<SubjectInfo> getList() {
        return list;
    }

    public void setList(List<SubjectInfo> list) {
        this.list = list;
    }
}
