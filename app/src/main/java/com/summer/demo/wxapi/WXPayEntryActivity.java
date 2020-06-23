package com.summer.demo.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.summer.demo.AppContext;
import com.summer.demo.constant.BroadConst;
import com.summer.demo.ui.module.fragment.pay.PayHelper;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * 微信支付回调
 * Created by xiastars on 2017/7/12.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    Context context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        AppContext.getInstance().getWxApi().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        AppContext.getInstance().getWxApi().handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        PayHelper.OnPayResultListener listener = PayHelper.getOnPayResultListener();
        if (resp.errCode == 0) {
            Intent intent = new Intent(BroadConst.NOITFY_PAY_RETURN_RESULT);
            intent.putExtra(BroadConst.NOTIFY_NAME_PAY_RESULT ,"yes"); //
            context.sendBroadcast( intent );
            if (listener != null)
                listener.onPayResult(true);
            finish();
        } else {
            Intent intent = new Intent(BroadConst.NOITFY_PAY_RETURN_RESULT);
            intent.putExtra(BroadConst.NOTIFY_NAME_PAY_RESULT ,"no"); //
            context.sendBroadcast( intent );
            if (listener != null)
                listener.onPayResult(false);
            finish();
        }
    }
}
