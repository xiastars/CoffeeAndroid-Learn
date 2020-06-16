package com.summer.demo.ui.module.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.AppContext;
import com.summer.demo.R;
import com.summer.demo.dialog.EasyLoadingDialog;
import com.summer.demo.helper.DialogHelper;
import com.summer.helper.utils.Logs;

import java.lang.ref.WeakReference;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/16 16:09
 */
public class EasyLoading {
    static EasyLoading easyLoading;
    Context context;

    EasyLoadingDialog easyLoadingDialog;

    TextView loadingText;
    RelativeLayout rlLoading;
    int mLoadingIndex = 0;
    String loadingWord = "加载中";
    Dialog mLoadingDialog;
    MyHandler myHandler;

    public EasyLoading(Context context) {
        this.context = context;
        myHandler = new MyHandler(this);
    }

    public static EasyLoading get(Context context) {
        if (easyLoading == null) {
            easyLoading = new EasyLoading(context);
        }
        return easyLoading;
    }

    /**
     * 九宫格样式
     */
    public void showNormalLoadingSudoku() {
        mLoadingDialog = new Dialog(context, R.style.TagFullScreenDialog);
        mLoadingDialog.setContentView(R.layout.dialog_loading1);
        //是否点击空白处关掉Dialog,一般设置为true
        mLoadingDialog.setCanceledOnTouchOutside(true);
        mLoadingDialog.show();
    }

    /**
     * 显示一个菊花在中间转的Loading
     */
    public void showNormalLoading() {
        cancelLoading();
        easyLoadingDialog = new EasyLoadingDialog(context);
        easyLoadingDialog.show();
    }

    public void showLoadingWithContent() {
        showLoadingWithContent("加载中");
    }

    public void showLoadingWithContent(String text) {
        mLoadingDialog = new Dialog(context, R.style.TagFullScreenDialog);
        mLoadingDialog.setContentView(R.layout.dialog_loading);
        rlLoading = (RelativeLayout) mLoadingDialog.findViewById(R.id.rl_loading);
        loadingText = (TextView) mLoadingDialog.findViewById(R.id.tv_loading);
        rlLoading.setVisibility(View.VISIBLE);
        mLoadingDialog.show();
        loadingWord = text;
        rlLoading.setVisibility(View.VISIBLE);
        circleTextLoading();
    }


    /**
     * 显示加载对话框
     *
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public void showLoadingFull(String msg, boolean cancelable) {

        mLoadingDialog = DialogHelper.getProgressDialog(context, msg, cancelable);

        mLoadingDialog.setCancelable(cancelable);
        try {
            mLoadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLoadingFull() {
        showLoadingFull(AppContext.getInstance().getString(R.string.loading), true);
    }

    public int getVisibility() {
        return rlLoading.getVisibility();
    }

    /**
     * 设置loading的文字变动
     */
    private void circleTextLoading() {

        myHandler.sendEmptyMessageDelayed(0, 1000);
    }

    /**
     * 取消
     */
    public void cancelLoading() {
        if (easyLoadingDialog != null) {
            easyLoadingDialog.cancelDialog();
            easyLoadingDialog = null;
        }
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
        }
        easyLoading = null;
    }

    private void handleMsg(int what, Object object) {
        final String sPot = ".";
        switch (what) {
            case 0:

                if (mLoadingIndex == 3) {
                    mLoadingIndex = 0;
                }
                if (mLoadingIndex == 0) {
                    loadingText.setText(loadingWord + sPot);
                } else if (mLoadingIndex == 1) {
                    Logs.i("text:" + loadingWord + sPot + sPot);
                    loadingText.setText(loadingWord + sPot + sPot);
                } else if (mLoadingIndex == 2) {
                    loadingText.setText(loadingWord + sPot + sPot + sPot);
                }
                mLoadingIndex++;
                if (rlLoading.getVisibility() == View.VISIBLE) {
                    circleTextLoading();
                }
                break;
        }
    }

    public static class MyHandler extends Handler {
        private final WeakReference<EasyLoading> mActivity;

        public MyHandler(EasyLoading activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            EasyLoading activity = mActivity.get();
            if (null != activity) {
                activity.handleMsg(msg.what, msg.obj);
            }
        }
    }
}
