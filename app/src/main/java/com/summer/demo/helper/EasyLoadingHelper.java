package com.summer.demo.helper;

import android.app.Dialog;
import android.content.Context;

import com.summer.demo.dialog.EasyLoadingDialog;

public class EasyLoadingHelper {
    private static EasyLoadingDialog mLoadingDialog;


    /**
     * 显示加载对话框
     */
    public static Dialog showLoading(Context context) {
        if(mLoadingDialog == null){
            mLoadingDialog = new EasyLoadingDialog(context);
        }
        mLoadingDialog.cancelDialog();
        mLoadingDialog.setCancelable(true);
        try {
            mLoadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public static void cancelLoading() {
        if (mLoadingDialog != null) {
            try {
                mLoadingDialog.cancelDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
