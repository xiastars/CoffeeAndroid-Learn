package com.summer.demo.ui.mine.release.bean;

public class MarUser {

    public static String USER_ID = null;
    public static String USER_AVATAR = null;//用户头像
    public static String USER_NAME = null;//用户名称
    public static String USER_INTRO = null;//用户简介
    public static String CER_NO = null;//身份证
    public static String REAL_NAME = null;//真实姓名
    public static String BANK_NO = null;//银行卡号码
    public static String BANK_BRANCH = null;//银行卡支行
    public static String BANK_NAME = null;//银行卡名称
    public static String BANK_USER_NAME = null;//银行卡用户名
    public static String USER_PHONE = null;//用户手机号码
    public static boolean isTourist = true;
    public static int TIMES_FOR_IDENTIFY_ONE_DAY;//每天实名验证次数
    public static int TIMES_FOR_IDENTIFY;//总共实名验证次数
    public static String DEFAULT_GROUP_ID;//当前默认的星球ID
    public static String MAIN_GROUP_ID;//不可退出的星球
    public static int WATER_POS = 0;//水印位置

    public static void init(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        isTourist = false;
        WATER_POS = userInfo.getWatermark();
        MAIN_GROUP_ID = userInfo.getMain_id();
        CER_NO = userInfo.getIdentity_no();
        REAL_NAME = userInfo.getReal_name();
        USER_PHONE = userInfo.getMobile();
        USER_INTRO = userInfo.getUser_introduction();
        USER_NAME = userInfo.getName();
        USER_AVATAR = userInfo.getAvatar();
        USER_ID = userInfo.getId();
        TIMES_FOR_IDENTIFY = userInfo.getTimes_for_identify();
        TIMES_FOR_IDENTIFY_ONE_DAY = userInfo.getTimes_for_identify_one_day();
    }
}
