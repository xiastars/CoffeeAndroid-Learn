package com.summer.demo.ui.module.fragment.login;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/18 14:47
 */
public class OathConfig {

    public static String WEIBO_TOKEN = "";
    public static String WEIBO_UID = "";
    public static String WEIXIN_ID = "";//微信KEY
    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static String WEIBO_REDIRECT_URL = "";

    /**
     * WeiboSDKDemo 应用对应的权限，第三方开发者一般不需要这么多，可直接设置成空即可。
     * 详情请查看 Demo 中对应的注释。
     */
    public static final String WEIBO_SCOPE = "";

    /**
     * 当前 DEMO 应用的 APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY
     */
    public static String WEIBO_APP_KEY = "";
}
