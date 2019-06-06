package com.summer.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.summer.demo.bean.BaseResp;
import com.summer.helper.utils.SUtils;
/**
 * 根据返回状态码弹出相应提示
 *
 * @author xiastars@vip.qq.com
 */
public class CodeRespondUtils {

    Context context;
    String msg;

    public CodeRespondUtils(final Context context, final BaseResp baseResp) {
        this.context = context;
        if (baseResp == null) {
            return;
        }
        msg = baseResp.getMsg();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dealWithCode(context, baseResp.getError());
            }
        });
    }

    private void dealWithCode(Context context, int code) {
        String toastMsg = "操作失败，请稍后重试！";
        if (!TextUtils.isEmpty(msg)) {
            toastMsg = msg;
        }
/*        if(code == 40219){//绑定手机
            BaseUtils.needPhoneCheck(context);
            return;
        }else if(40101 == code){
            FSXQSharedPreference.getInstance().setTokenEable("");
            context.sendBroadcast(new Intent(BroadConst.AFTER_LOGIN_REFRESH_ALL));
            BaseUtils.showLoginDialog(context);
            return;
        }*/
        SUtils.makeToast(context, toastMsg);
    }
}
