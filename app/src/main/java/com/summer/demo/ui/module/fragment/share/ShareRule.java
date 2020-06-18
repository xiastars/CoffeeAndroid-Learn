package com.summer.demo.ui.module.fragment.share;

/**
 * @Description: 分享规则
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/17 14:29
 */
public class ShareRule {

    //支持QQ好友分享
    boolean supportShareQQ = true;
    
    //支持QQ空间分享
    boolean supportShareQQZone = true;
    
    //支持微信好友分享
    boolean supportShareWechat = true;
    
    //支持微信朋友圈分享
    boolean supportShareWechatCircle = true;
    
    //支持微博分享
    boolean supportShareWeibo = true;
    
    //支持链接分享
    boolean  supportShareLink = true;

    //支持微信好友通过小程序分享
    boolean supportShareWechatMiniApp = false;

    //支持朋友圈为大图分享
    boolean supportShareWechatCircleWithBigPhoto = false;

    //支持一键系统分享
    boolean supportShareSystem = false;

    public boolean isSupportShareSystem() {
        return supportShareSystem;
    }

    public void setSupportShareSystem(boolean supportShareSystem) {
        this.supportShareSystem = supportShareSystem;
    }

    public boolean isSupportShareWechatCircleWithBigPhoto() {
        return supportShareWechatCircleWithBigPhoto;
    }

    public void setSupportShareWechatCircleWithBigPhoto(boolean supportShareWechatCircleWithBigPhoto) {
        this.supportShareWechatCircleWithBigPhoto = supportShareWechatCircleWithBigPhoto;
    }

    public boolean isSupportShareWechatMiniApp() {
        return supportShareWechatMiniApp;
    }

    public void setSupportShareWechatMiniApp(boolean supportShareWechatMiniApp) {
        this.supportShareWechatMiniApp = supportShareWechatMiniApp;
    }

    public boolean isSupportShareQQ() {
        return supportShareQQ;
    }

    public void setSupportShareQQ(boolean supportShareQQ) {
        this.supportShareQQ = supportShareQQ;
    }

    public boolean isSupportShareQQZone() {
        return supportShareQQZone;
    }

    public void setSupportShareQQZone(boolean supportShareQQZone) {
        this.supportShareQQZone = supportShareQQZone;
    }

    public boolean isSupportShareWechat() {
        return supportShareWechat;
    }

    public void setSupportShareWechat(boolean supportShareWechat) {
        this.supportShareWechat = supportShareWechat;
    }

    public boolean isSupportShareWechatCircle() {
        return supportShareWechatCircle;
    }

    public void setSupportShareWechatCircle(boolean supportShareWechatCircle) {
        this.supportShareWechatCircle = supportShareWechatCircle;
    }

    public boolean isSupportShareWeibo() {
        return supportShareWeibo;
    }

    public void setSupportShareWeibo(boolean supportShareWeibo) {
        this.supportShareWeibo = supportShareWeibo;
    }

    public boolean isSupportShareLink() {
        return supportShareLink;
    }

    public void setSupportShareLink(boolean supportShareLink) {
        this.supportShareLink = supportShareLink;
    }
}
