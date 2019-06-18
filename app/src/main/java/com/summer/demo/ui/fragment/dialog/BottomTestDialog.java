package com.summer.demo.ui.fragment.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.summer.demo.R;
import com.summer.helper.dialog.BaseBottomDialog;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/17 17:19
 */
public class BottomTestDialog extends BaseBottomDialog {
    public BottomTestDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_bottom_test;
    }

    @Override
    public void initView(View view) {

    }
}
