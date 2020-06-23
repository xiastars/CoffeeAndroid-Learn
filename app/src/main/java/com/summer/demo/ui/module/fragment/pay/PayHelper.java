
package com.summer.demo.ui.module.fragment.pay;

import android.app.Activity;
import android.os.Handler;

import com.alipay.sdk.app.PayTask;
import com.summer.demo.AppContext;
import com.summer.demo.helper.EasyHttpHelper;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * 微信支付与支付宝支付的帮助类
 * @author xiastars@vip.qq.com
 */
public class PayHelper {

    public static final int ALI_PAY_CODE = 1001;
    private static OnPayResultListener mOnPayResultListener;

    public static void wechatPay(Activity activity, WechatPayInfo payInfo, OnPayResultListener listener){
        mOnPayResultListener = listener;
        PayReq request = new PayReq();
        request.appId = payInfo.getAppid();
        request.prepayId = payInfo.getPrepayid();
        request.nonceStr = payInfo.getNoncestr();
        request.timeStamp = payInfo.getTimestamp();
        request.partnerId = payInfo.getPartnerid();
        request.packageValue = payInfo.getPackageX();
        request.sign = payInfo.getSign();
        request.signType = "MD5";
        boolean result = AppContext.getInstance().getWxApi().sendReq(request);
    }

    /**
     * 从服务器请求令牌，然后调用微信支付
     * @param activity
     * @param info
     */
    public static void wxPay(final Activity activity, GoodInfo info) {
        if (info == null) {
            return;
        }
        SummerParameter parameter = new SummerParameter();
        parameter.put("clientType", "app");
        parameter.put("goodsId", info.getId());
        parameter.put("goodsName", info.getXingValue() +"星币" );
        // parameter.put("");
        EasyHttpHelper helper = new EasyHttpHelper(activity);
        helper.postData(EasyHttpHelper.REQUEST_WECHAT_PAY,WXPayResp.class,parameter,"url");
        helper.setOnEasyHttpResultListener(new EasyHttpHelper.OnEasyHttpResultListener() {
            @Override
            public void onResult(int requestCode, Object obj, boolean succeed) {
                if(!succeed){
                    SUtils.makeToast(activity, (String) obj);
                    return;
                }
                WXBean wxBean = (WXBean) obj;

                PayReq request = new PayReq();
                request.appId = wxBean.getAppid();
                request.prepayId = wxBean.getPrepayid();
                request.nonceStr = wxBean.getNoncestr();
                request.timeStamp = wxBean.getTimestamp();
                request.partnerId = wxBean.getPartnerid();
                request.packageValue = "Sign=WXPay";
                request.sign = wxBean.getSign();
                request.signType = "MD5";
                boolean result = AppContext.getInstance().getWxApi().sendReq(request);
                Logs.i("result:" + result);
            }

        });


    }

    /**
     * 从服务器请求令牌，然后调用支付宝支付
     * @param activity
     * @param info
     * @param handler
     */
    public static void aliPay(final Activity activity, GoodInfo info, final Handler handler) {
        Logs.i("请求==="+ info );
        if (info == null) {
            return;
        }
        SummerParameter parameter = new SummerParameter();
        parameter.put("clientType", "app");
        parameter.put("goodsId", info.getId());
        parameter.put("goodsName", info.getXingValue() +"星币" );
        EasyHttpHelper helper = new EasyHttpHelper(activity);
        helper.postData(EasyHttpHelper.REQUEST_ALI_PAY,PayInfo.class,parameter,"ali pay url");
        helper.setOnEasyHttpResultListener(new EasyHttpHelper.OnEasyHttpResultListener() {
            @Override
            public void onResult(int requestCode, Object obj, boolean succeed) {
                if (!succeed) {
                    SUtils.makeToast(activity, (String) obj);
                    return;
                }
                PayInfo info = (PayInfo) obj;
                final String orderStr = info.getOrderStr();
                PayTask payTask = new PayTask(activity);
                handler.obtainMessage(ALI_PAY_CODE, payTask.payV2(orderStr, true)).sendToTarget();


            }

        });

    }

    public static void setOnPayResultListener(OnPayResultListener listener) {
        mOnPayResultListener = listener;
    }

    public static OnPayResultListener getOnPayResultListener() {
        return mOnPayResultListener;
    }


    public interface OnPayResultListener{
        void onPayResult(boolean isSuccess);
    }
}
