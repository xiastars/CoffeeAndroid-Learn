package com.summer.demo.module.base.constant;

import com.summer.demo.AppContext;

public class ApiConstants {

    public static String FSXQ_DEV = "dev";

    public static String FSXQ_HOST = "https://" + FSXQ_DEV + ".fensixingqiu.com";

    public static String FSXQ_MOVIE_HOST = "https://" + FSXQ_DEV + ".fensixingqiu.com";

    public static String FSXQ_IMG_HOST = "https://" + FSXQ_DEV + ".fensixingqiu.com";


    public static final String API_VERSION_1 = "/v1.0/";
    public static final String API_VERSION_2 = "/v1.2/";

    public static String WEB_URL = "";
    public static String SHARE_URL = "";

    /**
     * 七牛云的图片拼接前缀
     */
    public static final String FILE_BASE_PATH = "";


    static {
        String HTTPTYPE = null;
        if (AppContext.SERVER_MODE == 0) {
            FSXQ_DEV = "dev";
            HTTPTYPE = "http:";
            WEB_URL = "http://web.";
            SHARE_URL = "";
        } else if (AppContext.SERVER_MODE == 1) {
            FSXQ_DEV = "testa";
            HTTPTYPE = "http:";
            WEB_URL = "http://web.";
            SHARE_URL = "";
        } else if (AppContext.SERVER_MODE == 2) {
            FSXQ_DEV = "api";
            HTTPTYPE = "https:";
            WEB_URL = "https://w.";
            SHARE_URL = "";
        } else if (AppContext.SERVER_MODE == 3) {
            FSXQ_DEV = "prea";
            HTTPTYPE = "https:";
            WEB_URL = "https://prew2.";
            SHARE_URL = "";
        }
        FSXQ_HOST = HTTPTYPE + "//" + FSXQ_DEV ;
        FSXQ_MOVIE_HOST = "https://" + FSXQ_DEV + ".";
        FSXQ_IMG_HOST = "https://" + FSXQ_DEV + ".";
    }

    private static final String FSXQ_SHARE_GROUP_PATH = "/dist/star/index";
    private static final String FSXQ_SHARE_TOPIC_PATH = "/dist/star/post_detail";


    public static String shareFsxqUrl(int type) {
        StringBuffer buffer = new StringBuffer(FSXQ_HOST);
        if (type == 1) {
            buffer.append(FSXQ_SHARE_GROUP_PATH);
        } else {
            buffer.append(FSXQ_SHARE_TOPIC_PATH);
        }

        return buffer.toString();
    }


    /**
     * 获取对应的host
     *
     * @return host
     */
    public static String getHost() {
        return getHost(API_VERSION_1);
    }

    /**
     * 获取对应的host
     *
     * @return host
     */
    public static String getHost(String version) {
        String host = FSXQ_HOST;
        String apiVersion = API_VERSION_1;
        if (version != null) {
            apiVersion = version;
        }
        return host + apiVersion;
    }

    public static String getBaseHost() {
        return FSXQ_HOST;
    }

    /**
     * 获取对应的host
     *
     * @return host
     */
    public static String getHostVersion2() {
        return API_VERSION_2;
    }

    public static String withWeb(String url) {
        return WEB_URL + url;
    }

    public static String withShare(String url) {
        return SHARE_URL + "/" + url;
    }

    public static String withHost(String url) {
        return FSXQ_HOST + url;
    }

    /**
     * 分享有奖协议
     */
    public static String SHARE_REWRD_PROTOCAL = withWeb("fensixingqiu.com/dist/user/rule?type=share_award_agreement");

    /**
     * 分享有奖规则
     */
    public static String SHARE_REWRD_RULE = withWeb("fensixingqiu.com/dist/user/rule?type=share_award_rule");
}
