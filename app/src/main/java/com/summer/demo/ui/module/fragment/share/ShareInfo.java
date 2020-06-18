package com.summer.demo.ui.module.fragment.share;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ShareInfo implements Serializable {
    private String topic_id;
    private String url, origin_url;
    private Bitmap shareBitMap;
    private int shareType; //1星球 2主题,3邀请嘉宾
    String shareTitle;
    String shareContent;
    String shareImg;
    private int drawableId;
    boolean supportShareToWxApp;
    String wxAppPath;

    public String getWxAppPath() {
        return wxAppPath;
    }

    public void setWxAppPath(String wxAppPath) {
        this.wxAppPath = wxAppPath;
    }

    public boolean isSupportShareToWxApp() {
        return supportShareToWxApp;
    }

    public void setSupportShareToWxApp(boolean supportShareToWxApp) {
        this.supportShareToWxApp = supportShareToWxApp;
    }

    public boolean isShareBig() {
        return shareType == ShareType.WECHAT_BIG_PHOTO;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public int getShareType() {
        return shareType;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOrigin_url() {
        return origin_url;
    }

    public void setOrigin_url(String origin_url) {
        this.origin_url = origin_url;
    }

    public String getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(String topic_id) {
        this.topic_id = topic_id;
    }

    public Bitmap getShareBitMap() {
        return shareBitMap;
    }

    public void setShareBitMap(Bitmap shareBitMap) {
        this.shareBitMap = shareBitMap;
    }
}
