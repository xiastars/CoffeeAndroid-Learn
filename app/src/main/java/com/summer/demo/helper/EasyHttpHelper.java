package com.summer.demo.helper;

import android.content.Context;

import com.summer.demo.bean.BaseResp;
import com.summer.demo.module.base.CommonHelper;
import com.summer.helper.listener.OnReturnObjectClickListener;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.server.PostData;
import com.summer.helper.server.SummerParameter;

/**
 * 简单的请求，不需要知道结果
 */
public class EasyHttpHelper extends CommonHelper {

    OnSimpleClickListener onEasyReturnListener;
    OnReturnObjectClickListener onReturnObjectClickListener;
    OnEasyHttpResultListener onEasyHttpResultListener;

    public static int DELETE_COMMENT = 1001;//删除评论
    public static int CREAT_SUBJECT = 1002;//创建话题
    public static int DELETE_SUBJECT = 1003;//删除话题
    public static int EDIT_GROUP_INTRO = 1004;//提问里的信息修改
    public final static int CHANGE_GROUP_SHARE = 1005;//星球管理的分享
    public final static int CHANGE_GROUP_MEMER_VISIBLE = 1006;//星球管理的成员公开
    public final static int GROUP_SET_ANONY = 1007;//星球里是否匿名
    public final static int GROUP_SET_ONE_DAY = 1008;//星球里是否一天才能发表
    public final static int GROUP_SET_TOPIC_LIMIT = 1009;//星球里发表主题权限
    public final static int REQUEST_PAY_TOKEN = 1010;//获取支付令牌
    public final static int REQUEST_PAY_RESULT = 1011;//获取支付结果
    public final static int REQUEST_UPDATE_PUSH = 1012;//修改PUSH状态
    public final static int REQUEST_GUEST_ID = 1013;//获取GuestID
    public final static int REQUEST_COMMENT_REPLYS = 1015;//获取评论里的回复
    public final static int REQUEST_SWITCH_GROUP = 1016;//切换账号
    public final static int TOPIC_VIEW_PERMISSION = 1017;//主题查看权限
    public final static int SEARCH_CONTENT = 1018;//搜索的反馈

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
        if (onEasyReturnListener != null) {
            onEasyReturnListener.onClick(requestCode);
        }
        if (onReturnObjectClickListener != null) {
            onReturnObjectClickListener = (OnReturnObjectClickListener) obj;
        }
        if (onEasyHttpResultListener != null) {
            onEasyHttpResultListener.onResult(requestCode, obj, true);
        }
    }

    @Override
    protected void dealErrors(int requstCode, String requestType, String errString, boolean requestCode) {
        EasyLoadingHelper.cancelLoading();
        if (onEasyHttpResultListener != null) {
            onEasyHttpResultListener.onResult(requstCode, null, false);
        }
    }

    public void setOnEasyReturnListener(OnSimpleClickListener onEasyReturnListener) {
        this.onEasyReturnListener = onEasyReturnListener;
    }

    public void setOnReturnObjectClickListener(OnReturnObjectClickListener onReturnObjectClickListener) {
        this.onReturnObjectClickListener = onReturnObjectClickListener;
    }

    public void setOnEasyHttpResultListener(OnEasyHttpResultListener onEasyHttpResultListener) {
        this.onEasyHttpResultListener = onEasyHttpResultListener;
    }

    public interface OnEasyHttpResultListener {
        void onResult(int requestCode, Object obj, boolean succeed);
    }
}
