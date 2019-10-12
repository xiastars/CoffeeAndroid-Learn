package com.summer.demo.ui.module.comment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.LoadingDialog;

import java.lang.ref.WeakReference;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/11 14:06
 */
public class CommentDialog extends Dialog {
    Context context;
    String topicId;
    String replyName;
    long replyId;

    MyHandler myHandler;
    OnCommentedListener listener;
    LoadingDialog loadingDialog;


    public CommentDialog(Context context, String topicId, OnCommentedListener listener) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.topicId = topicId;
        myHandler = new MyHandler(this);
        this.listener = listener;
    }

    public void setReplyInfo(String replyName, long replyId) {
        this.replyName = replyName;
        this.replyId = replyId;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_comment);
        final RelativeLayout parent = (RelativeLayout) findViewById(R.id.ll_parent);
        Window window = getWindow();
        if(window != null){
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = SUtils.screenWidth;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
        }
        final EditText etContent = (EditText) findViewById(R.id.edt_comment);
        if(replyName != null){
            etContent.setHint("回复@" + replyName);
        }
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CommentDialog.this.cancel();
                SUtils.hideSoftInpuFromWindow(etContent);
            }
        });
        this.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                SUtils.makeToast(context,"关闭");
                SUtils.hideSoftInpuFromWindow(etContent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        SUtils.hideSoftInpuFromWindow(etContent);
                    }
                }, 150);
            }
        });

        Button cancel = (Button) findViewById(R.id.btn_send);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sendComment(replyId, etContent.getText().toString());
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SUtils.showSoftInpuFromWindow(etContent, getContext());
            }
        }, 100);
    }

    private void sendComment(long replyId, String content) {
        if (TextUtils.isEmpty(content)) {
            SUtils.makeToast(context, context.getString(R.string.hint_empty_comment));
            return;
        }
        loadingDialog = new LoadingDialog(context);
        //发表评论
 /*       SummerParameter params = Const.getPostParameters();
        params.put("topicId", topicId);
        params.put("content", content);
        params.putLog("星战评论");
        if(replyId != 0){
            params.put("replyId", replyId);
        }
        EasyHttp.post(context, Server.STAR_COMMENT_SEND, CommentedResp.class, params, new RequestCallback<CommentedResp>() {
            @Override
            public void done(CommentedResp moveResp) {
                if (moveResp != null) {
                    new CodeRespondUtils(context, moveResp.getCode());
                    StarCommentInfo infos = moveResp.getDatas();
                    if (infos != null) {
                        myHandler.obtainMessage(0,infos).sendToTarget();
                    }
                    myHandler.sendEmptyMessage(1);
                }
            }

            @Override
            public void onError(int errorCode, String errorStr) {

            }
        });*/
    }

    private void commentSuccess(StarCommentInfo info) {
        listener.onSucceed(info);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<CommentDialog> mActivity;

        public MyHandler(CommentDialog activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            CommentDialog activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 0:
                        StarCommentInfo info = (StarCommentInfo) msg.obj;
                        activity.commentSuccess(info);
                        break;
                    case 1:
                        activity.cancelDialog();
                        break;
                }
            }
        }
    }

    private void cancelDialog(){
        this.cancel();
        if(loadingDialog != null){
            loadingDialog.cancelLoading();
        }
    }

}
