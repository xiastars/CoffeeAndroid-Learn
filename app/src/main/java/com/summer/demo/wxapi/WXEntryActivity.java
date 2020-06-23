package com.summer.demo.wxapi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.summer.demo.AppContext;
import com.summer.demo.ui.module.fragment.login.LoginHelper;
import com.summer.demo.ui.module.fragment.login.OathType;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by xiaqiliang on 2017/3/30.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    public static String token;
    private Context context;

    /**
     * 微信支付KEY
     */
    public static final String WXPAY_KEY = "wx61d86382a077b578";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        if( AppContext.getInstance().getWxApi() != null){
            AppContext.getInstance().getWxApi().handleIntent(getIntent(), this);
        }
        finish();

    }

    @Override
    public void onReq(BaseReq req) {
        Logs.i("xia", req.openId + ",,,,fdsfd");
    }

    @Override
    public void onResp(BaseResp resp) {
        Logs.i("resp.errCode"+resp.errCode+",,,"+resp.errStr);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if(resp.getType() == 2){//分享类型
                    //分享成功回调
                    return;
                }
                try {
                    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    WXEntryActivity.token = sendResp.code;
                    LoginHelper helper = new LoginHelper(OathType.WEIXIN, context);
                    helper.startBind();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                SUtils.makeToast(this, "用户取消授权");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                SUtils.makeToast(this, "用户拒绝授权");
                break;
        }

    }

}
