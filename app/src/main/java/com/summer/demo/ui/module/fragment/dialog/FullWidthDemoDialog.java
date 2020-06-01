package com.summer.demo.ui.module.fragment.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.dialog.BaseFullwidthCenterDialog;

/**
 * @Description: 一个空的，全屏对话框展示
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/1 15:54
 */
public class FullWidthDemoDialog extends BaseFullwidthCenterDialog {
    public FullWidthDemoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int setContainerView() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView(View view) {

    }
}
