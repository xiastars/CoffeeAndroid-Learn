package com.summer.demo.ui.mine.contact.bean;

import java.io.Serializable;

/**
 * Created by xiastars on 2017/11/4.
 */

public class LibraryInfo implements Serializable {

    private long id;
    private String repositoryImg;
    private String repositoryName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRepositoryImg() {
        return repositoryImg;
    }

    public void setRepositoryImg(String repositoryImg) {
        this.repositoryImg = repositoryImg;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }
}
