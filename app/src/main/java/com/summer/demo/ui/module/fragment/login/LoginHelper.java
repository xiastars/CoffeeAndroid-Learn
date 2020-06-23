package com.summer.demo.ui.module.fragment.login;

import android.content.Context;

import com.summer.demo.bean.BaseResp;
import com.summer.demo.helper.EasyHttpHelper;
import com.summer.demo.wxapi.WXEntryActivity;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.SUtils;

/**
 * Created by xiaqiliang on 2017/5/17.
 */

public class LoginHelper {
    String oathType;
    Context context;
    /*
     * 用户登录平台
     */
    public static final String LOGIN_STATE = "LOGIN_STATE";

    public LoginHelper(String oathType, Context context) {
        SUtils.saveStringData(context, LOGIN_STATE, oathType);
        this.oathType = oathType;
        this.context = context;
    }

    /**
     * 绑定登录
     */
    public void startBind() {
        SummerParameter params = new SummerParameter();
        if (oathType.equals(OathType.WEIXIN)) {
            params.put("code", WXEntryActivity.token);
        } else if (oathType.equals(OathType.WEIBO)) {
            params.put("uid", OathConfig.WEIBO_UID);
            params.put("accessToken", OathConfig.WEIBO_TOKEN);
        } else if (oathType.equals(OathType.QQ)) {

        }
        params.put("client", "app");
        params.put("state", SUtils.getStringData(context, LOGIN_STATE));
        params.putLog("绑定SNS");
        EasyHttpHelper easyHttpHelper = new EasyHttpHelper(context);
        easyHttpHelper.postData(EasyHttpHelper.REQUEST_OATH_LOGIN, BaseResp.class, params, "url");
        easyHttpHelper.setOnEasyHttpResultListener(new EasyHttpHelper.OnEasyHttpResultListener() {
            @Override
            public void onResult(int requestCode, Object obj, boolean succeed) {
                if (requestCode == EasyHttpHelper.REQUEST_OATH_LOGIN && succeed) {
                    BaseResp resp = (BaseResp) obj;
                    int code = resp.getCode();
                    if (code == 0) {
                        //登录成功，刷新界面
                    } else {

                    }
                }

            }
        });

    }

    /**
     * 如果已经绑定，直接登录获取用户信息
     */
    public void startLogin(){

    }

}