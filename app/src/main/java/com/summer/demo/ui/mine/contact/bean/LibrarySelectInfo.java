package com.summer.demo.ui.mine.contact.bean;

import java.util.List;

/**
 * Created by xiastars on 2017/11/21.
 */

public class LibrarySelectInfo {

    int type;
    String character;
    List<LibraryInfo> infos;

    public LibrarySelectInfo(){
    }

    public String getCharacter() {
        return character == null ? "" : character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public LibrarySelectInfo(String character, int type){
        this.type = type;
        this.character = character;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<LibraryInfo> getInfos() {
        return infos;
    }

    public void setInfos(List<LibraryInfo> infos) {
        this.infos = infos;
    }
}
