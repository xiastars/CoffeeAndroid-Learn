package com.summer.demo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.summer.demo.R;
import com.summer.helper.dialog.BaseCenterDialog;

public class EasyLoadingDialog extends BaseCenterDialog {

    public EasyLoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_loading;
    }

    @Override
    public void initView(View view) {

    }
}
