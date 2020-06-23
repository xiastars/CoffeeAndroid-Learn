package com.summer.demo.helper;

import android.content.Context;

import com.summer.demo.bean.BaseResp;
import com.summer.demo.module.base.CommonHelper;
import com.summer.helper.server.PostData;
import com.summer.helper.server.SummerParameter;

/**
 * 简单的请求，不需要知道结果
 */
public class EasyHttpHelper extends CommonHelper {

    OnEasyHttpResultListener onEasyHttpResultListener;

    public static int CREAT_SUBJECT = 1002;//创建话题
    public static int REQUEST_WECHAT_PAY = 1003;//微信支付
    public static int REQUEST_ALI_PAY = 1004;//支付宝支付
    public static int REQUEST_OATH_LOGIN = 1005;//第三方登录
    public static int REQUEST_OATH_WEIBO = 1006;//微博授权

    public EasyHttpHelper(Context context) {
        super(context);
    }

    /**
     * 公用的，一项项修改
     */
    public void startEasyModify(String url, String key, String value) {
        final SummerParameter params = PostData.getPostParameters(context);
        params.setDisableCache();
        params.put(key, value);
        params.putLog("一项项修改");
        putData(0, BaseResp.class, params, url);
    }

    /**
     * 公用的，删除对象,基于接口1.2
     */
    public void startEasyDelete(int requestCode, String url) {
        startEasyDelete(requestCode, url, null, null);
    }

    /**
     * 公用的，删除对象,基于接口1.2
     */
    public void startEasyDelete(int requestCode, String url, String key, String value) {
        EasyLoadingHelper.showLoading(context);
        final SummerParameter params = PostData.getPostParameters(context);
        params.setDisableCache();
        if (key != null) {
            params.put(key, value);
        }
        params.putLog("一项项修改");
        delete(requestCode, BaseResp.class, params, url);
    }

    /**
     * 公用的，post对象,基于接口1.2
     */
    public void startEasyPost(int requestCode, String url, String key, String value) {
        startEasyPost(requestCode, url, key, value, null);
    }

    /**
     * 公用的，post对象,基于接口1.2
     */
    public void startEasyPost(int requestCode, String url, String key, String value, Class returnData) {
        EasyLoadingHelper.showLoading(context);
        final SummerParameter params = PostData.getPostParameters(context);
        params.setDisableCache();
        if (key != null) {
            params.put(key, value);
        }
        params.putLog("一项项修改");
        postData(requestCode, returnData == null ? BaseResp.class : returnData, params, url);
    }

    /**
     * 公用的，post对象,基于接口1.2
     */
    public void startEasyGet(int requestCode, String url, SummerParameter parameter, Class returnData) {
        startEasyGet(requestCode, url, parameter, returnData, true);
    }

    /**
     * 公用的，post对象,基于接口1.2
     */
    public void startEasyGet(int requestCode, String url, SummerParameter parameter, Class returnData, boolean showDialog) {
        if (showDialog) {
            EasyLoadingHelper.showLoading(context);
        }
        parameter.setDisableCache();
        parameter.putLog("一项项修改");
        getData(requestCode, returnData == null ? BaseResp.class : returnData, parameter, url);
    }

    @Override
    protected void handleMsg(int position, Object object) {

    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {
        EasyLoadingHelper.cancelLoading();
        if (onEasyHttpResultListener != null) {
            onEasyHttpResultListener.onResult(requestCode, obj, true);
        }
    }

    @Override
    protected void dealErrors(int requstCode, String requestType, String errString, boolean requestCode) {
        EasyLoadingHelper.cancelLoading();
        if (onEasyHttpResultListener != null) {
            onEasyHttpResultListener.onResult(requstCode, errString, false);
        }
    }

    public void setOnEasyHttpResultListener(OnEasyHttpResultListener onEasyHttpResultListener) {
        this.onEasyHttpResultListener = onEasyHttpResultListener;
    }

    public interface OnEasyHttpResultListener {
        void onResult(int requestCode, Object obj, boolean succeed);
    }
}
