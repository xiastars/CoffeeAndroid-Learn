package com.summer.demo.utils;

import android.content.Context;

import com.summer.demo.dialog.BaseTipsDialog;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/17 10:11
 */
public class BaseUtils {

    /**
     * 显示简单的对话框
     *
     * @param context
     * @param content
     * @param listener
     */
    public static void showEasyDialog(Context context, String content, BaseTipsDialog.DialogAfterClickListener listener) {
        BaseTipsDialog baseTipsDialog = new BaseTipsDialog(context, content, listener);
        baseTipsDialog.hideTitle();
        baseTipsDialog.show();
    }
}
