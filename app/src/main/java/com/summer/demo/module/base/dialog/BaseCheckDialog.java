package com.summer.demo.module.base.dialog;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.base.listener.DialogAfterClickListener;
import com.summer.helper.dialog.BaseCenterDialog;
import com.summer.helper.listener.OnAnimEndListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.SThread;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;

/**
 * @Description: 常用的默认通知弹窗
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/4/30 16:20
 */
public class BaseCheckDialog extends BaseCenterDialog {

    @BindView(R.id.iv_top)
    ImageView ivTop;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tips_cancel_tv)
    TextView tvCancel;
    @BindView(R.id.tips_ok_tv)
    TextView tipsOkTv;
    @BindView(R.id.tv_title)TextView tvTitle;
    private DialogAfterClickListener listener;

    private int layoutid;

    String name;
    String okContent;
    String content;
    String cancelContent;
    String titleContent;

    boolean showTitle = true;

    TimerCount timerCount;

    public BaseCheckDialog(Context context, String name, DialogAfterClickListener listener) {
        super(context, R.style.TagFullScreenDialog);
        this.listener = listener;
        this.name = name;
        layoutid = R.layout.dialog_base_check;
    }

    @Override
    public int setContainerView() {
        return layoutid;
    }

    @Override
    public void initView(View view) {
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        if (tvContent != null) {
            if (!TextUtils.isEmpty(name)) {
                SUtils.setHtmlText(name, tvContent);
            }
            if (!TextUtils.isEmpty(content)) {
                SUtils.setHtmlText(content, tvContent);
            }
        }
        if(!TextUtils.isEmpty(titleContent)){
            tvTitle.setText(titleContent);
            tvTitle.setVisibility(View.VISIBLE);
        }

        TextView tvOK = (TextView) view.findViewById(R.id.tips_ok_tv);
        if (!TextUtils.isEmpty(okContent)) {
            tvOK.setText(okContent);
        }
        SUtils.clickTransColor(tvOK);
        tvOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onSure();
                }
                cancelDialog();
            }

        });
        if (tvCancel != null) {
            if (!TextUtils.isEmpty(cancelContent)) {
                tvCancel.setText(cancelContent);
            }
            SUtils.clickTransColor(tvCancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cancelDialog();
                }
            });
        }
    }

    /**
     * 设置自动关闭，用户在一定时间内未点击时，自动关闭
     * @param timeSecond 秒钟
     */
    public void setAutoClose(int timeSecond) {
        timerCount = new TimerCount((int) timeSecond * 1000, 1000, new OnAnimEndListener() {
            @Override
            public void onEnd() {
                SThread.getIntances().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cancelDialogNoListener();
                    }
                });
            }
        });

        timerCount.start();
    }

    @Override
    public void cancelDialog() {
        super.cancelDialog();
        if (timerCount != null) {
            timerCount.cancel();
        }
        if (listener != null) {
            listener.onCancel();
        }
    }

    /**
     * 关闭时，没有回调
     */
    public void cancelDialogNoListener() {
        super.cancelDialog();
        if (timerCount != null) {
            timerCount.cancel();
        }
    }

    /**
     * 设置标题
     * @param content
     */
    public void setTitleContent(String content){
        this.titleContent = content;
    }

    /**
     * 设置取消文案
     * @param content
     */
    public void setCancelContent(String content) {
        this.cancelContent = content;
    }

    /**
     * 设置确定文案
     * @param content
     */
    public void setOkContent(String content) {
        this.okContent = content;
    }

    /**
     * 不显示标题
     */
    public void hideTitle() {
        showTitle = false;
    }

    /**
     * 设置显示的文本内容
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    class TimerCount extends CountDownTimer {
        OnAnimEndListener onAnimEndListener;

        public TimerCount(long millisInFuture, long countDownInterval, OnAnimEndListener onAnimEndListener) {
            super(millisInFuture, countDownInterval);
            this.onAnimEndListener = onAnimEndListener;
        }

        @Override
        public void onFinish() {
            /*结束后的处理*/
            onAnimEndListener.onEnd();
            Logs.i("==============onFinish");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            /*这里在屏幕上显示倒计时时间*/
            final int second = (int) (millisUntilFinished / 1000);
            SThread.getIntances().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (okContent != null && tipsOkTv != null) {
                        tipsOkTv.setText(STextUtils.spliceText(okContent, "(", second + "", ")"));
                    }
                }
            });
        }
    }


}