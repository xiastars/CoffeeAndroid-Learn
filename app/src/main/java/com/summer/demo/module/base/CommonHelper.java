package com.summer.demo.module.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.summer.helper.listener.OnReturnDataResultListener;
import com.summer.helper.server.SummerParameter;

import java.lang.ref.WeakReference;

/**
 * Created by xiastars on 2017/10/31.
 */

public abstract class CommonHelper {
    protected BaseHelper baseHelper;
    protected MyHandler myHandler;
    protected Context context;
    OnReturnDataResultListener listener;


    public CommonHelper(Context context) {
        myHandler = new MyHandler(this);
        this.context = context;
        baseHelper = new BaseHelper(context, myHandler);
    }

    public class MyHandler extends Handler {
        private final WeakReference<CommonHelper> mActivity;

        public MyHandler(CommonHelper activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CommonHelper activity = mActivity.get();
            if (null != activity) {
                activity.baseHelper.cancelLoading();
                switch (msg.what) {
                    case BaseHelper.MSG_SUCCEED:
                        activity.dealDatas(msg.arg1, msg.obj);
                        if(activity.listener != null){
                            activity.listener.onClick(msg.arg1,msg.obj);
                        }
                        break;
                    case BaseHelper.MSG_CACHE:
                        activity.dealDatas(msg.arg1, msg.obj);
                        if(activity.listener != null){
                            activity.listener.onClick(msg.arg1,msg.obj);
                        }
                        break;
                    case BaseHelper.MSG_ERRO:
                        activity.baseHelper.cancelLoading();
                        activity.dealErrors(msg.arg1, msg.arg2 + "", (String) msg.obj, false);
                        break;
                    default:
                        activity.handleMsg(msg.what, msg.obj);
                }
            }
        }
    }

    public void putData(int requestCode, Class className, SummerParameter params, final String url) {
        baseHelper.putData(requestCode, className, params, url);
    }


    public void delete(int requestCode, Class className, SummerParameter params, final String url) {
        baseHelper.deleteData(requestCode, className, params, url);
    }

    public void postData(int requestCode, Class className, SummerParameter params, final String url) {
        baseHelper.getData(requestCode, className, params, url);
    }


    public void getData(int requestCode, Class className, SummerParameter params, final String url) {
        baseHelper.getData(requestCode, className, params, url);
    }


    public void setListener(OnReturnDataResultListener listener) {
        this.listener = listener;
    }

    protected abstract void handleMsg(int position, Object object);

    /**
     * 处理返回的数据
     */
    protected abstract void dealDatas(int requestCode, Object obj);

    protected abstract void dealErrors(int requstCode, String requestType, String errString, boolean requestCode);
}
