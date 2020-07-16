package com.summer.demo.ui.module.fragment.login;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.summer.demo.AppContext;
import com.summer.demo.R;
import com.summer.demo.helper.EasyHttpHelper;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.ui.module.fragment.dialog.EasyLoading;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    private static final short REQUEST_CODE_REGISTER = 1;
    Button btnWechat;
    @BindView(R.id.btn_weibo)
    Button btnWeibo;


    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    int fromMain = 0;

    public static String WEIBO_TOKEN = "";
    public static String WEIBO_UID = "";

    public static boolean isBind;
//    Tencent mTencent;//QQ用的

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        changeHeaderStyleTrans(context.getResources().getColor(R.color.half_grey));
        fromMain = JumpTo.getInteger(this);
        removeTitle();
        SUtils.clickTransColor(btnWeibo);
        SUtils.clickTransColor(btnWechat);
        int fromWhere = JumpTo.getInteger(this);
        isBind = false;
        if (fromWhere == 1) {
            isBind = true;
            setContentView(R.layout.view_empty);
            AccessTokenKeeper.clear(getApplicationContext());
            //requestOath(OathType.WEIBO);
        } else if (fromWhere == 2) {
            isBind = true;
            setContentView(R.layout.view_empty);
            AppContext.getInstance().getWxApi().unregisterApp();
            //requestOath(OathType.WEIXIN);
        }
    }


    @OnClick({R.id.btn_wechat, R.id.btn_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wechat:
                AppContext.getInstance().unregisterWxApi();
                //直接登录
                onClickLoginButton();
                //requestOath(OathType.WEIXIN);
                break;
            case R.id.btn_weibo:
                AccessTokenKeeper.clear(getApplicationContext());
                requestOath(OathType.WEIBO);
                break;
        }
    }

    /**
     * 请求授权,从接口获取APPKEY，也可以跳过这一步直接授权
     *
     * @param fromType
     */
    private void requestOath(final String fromType) {
        SummerParameter parameter = new SummerParameter();
        parameter.put("platform", fromType);
        parameter.put("client", "app");
        parameter.put("redirectUrl", "");
        parameter.put("state", fromType);
        parameter.putLog("社交登录");
        parameter.setShowVirtualData();
        EasyHttpHelper easyHttpHelper = new EasyHttpHelper(context);
        easyHttpHelper.postData(EasyHttpHelper.REQUEST_OATH_LOGIN, LoginOathInfo.class, parameter, "url");
        easyHttpHelper.setOnEasyHttpResultListener(new EasyHttpHelper.OnEasyHttpResultListener() {
            @Override
            public void onResult(int requestCode, Object obj, boolean succeed) {
                if (!succeed) {
                    return;
                }
                if (requestCode != EasyHttpHelper.REQUEST_OATH_LOGIN) {
                    return;
                }
                if (!succeed || obj == null) {
                    SUtils.makeToast(context, "授权登录失败!");
                    finish();
                }
                LoginOathInfo oathInfo = (LoginOathInfo) obj;
                String code = oathInfo.getAppid();
                if (fromType.equals(OathType.WEIBO)) {
                    OathConfig.WEIBO_APP_KEY = code;
                    OathConfig.WEIBO_REDIRECT_URL = oathInfo.getRedirectUrl();
                    AppContext.getInstance().registToWeibo(code, oathInfo.getRedirectUrl());
                    mAccessToken = AccessTokenKeeper.readAccessToken(context);
                    mSsoHandler = new SsoHandler(LoginActivity.this);
                    mSsoHandler.authorize(new LoginActivity.SelfWbAuthListener());
                } else if (fromType.equals(OathType.WEIXIN)) {
                    OathConfig.WEIXIN_ID = code;
                    boolean result = AppContext.getInstance().registToWX(code);
                    if (result) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                EasyLoading.get(context).showLoadingWithContent("努力授权中");
                                onClickLoginButton();
                            }
                        });

                    }
                }
            }
        });

    }

    public void onClickLoginButton() {
        if (!AppContext.getInstance().isWxInstall()) {
            SUtils.makeToast(context, "没有安装微信，无法进行微信登录！");
            EasyLoading.get(context).cancelLoading();
            this.finish();
            return;
        }
        boolean registResult = AppContext.getInstance().registToWX(OathConfig.WEIXIN_ID);
        if(!registResult){
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        boolean result = AppContext.getInstance().getWxApi().sendReq(req);
        if (!result) {
            SUtils.makeToast(context, "授权失败，请稍后重试!");
            EasyLoading.get(context).cancelLoading();
        } else {
            this.finish();
        }
    }

    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     *
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REGISTER) {
            if (resultCode == RESULT_OK)
                finish();
        } else {
            // SSO 授权回调
            // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
            if (mSsoHandler != null) {
                mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
            }
//        Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyLoading.get(context).cancelLoading();
    }


    public class SelfWbAuthListener implements WbAuthListener {
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            Logs.i("xia", token.getPhoneNum() + ",,," + token.getToken() + "," + token.isSessionValid());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
                        // updateTokenView(false);
                        // 保存 Token 到 SharedPreferences
                        AccessTokenKeeper.writeAccessToken(context, mAccessToken);
                        WEIBO_UID = mAccessToken.getUid();
                        WEIBO_TOKEN = mAccessToken.getToken();
                        LoginHelper helper = new LoginHelper(OathType.WEIBO, context);
                        if (isBind) {
                            helper.startBind();
                        } else {
                            helper.startLogin();
                        }
                        finish();
                    }
                }
            });
        }

        @Override
        public void cancel() {
            SUtils.makeToast(context, "取消授权");
            Logs.i("xia", mAccessToken.getPhoneNum() + ",,," + mAccessToken.getToken() + "," + mAccessToken.isSessionValid());
            finish();
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Logs.i("xia", mAccessToken.getPhoneNum() + ",,," + mAccessToken.getToken() + "," + mAccessToken.isSessionValid());
            SUtils.makeToast(context, errorMessage.getErrorMessage());
            finish();
        }
    }
}
