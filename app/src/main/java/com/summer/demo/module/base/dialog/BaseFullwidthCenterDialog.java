package com.summer.demo.module.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.summer.demo.R;
import com.summer.helper.dialog.BaseDialog;
import com.summer.helper.utils.SUtils;

/**
 * 中间弹出框基本样式
 * Created by xiaqiliang on 2017/6/20.
 */

public abstract class BaseFullwidthCenterDialog extends BaseDialog {

    public BaseFullwidthCenterDialog(@NonNull Context context) {
        super(context, R.style.TagFullScreenDialog);
        this.context = context;
        SUtils.initScreenDisplayMetrics((Activity) context);
        setDialogCenterAndWidthFullscreen();
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected int showEnterAnim() {
        return R.anim.dialog_center_enter;
    }

    @Override
    protected int showQuitAnim() {
        return R.anim.dialog_center_quit;
    }

}
