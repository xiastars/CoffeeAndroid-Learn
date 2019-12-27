package com.summer.demo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.dialog.BaseBottomDialog;
import com.summer.helper.listener.OnSimpleClickListener;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectPhotoDialog extends BaseBottomDialog implements OnClickListener {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    OnSimpleClickListener listener;

    public SelectPhotoDialog(@NonNull Context context, OnSimpleClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_select_photo;
    }

    @Override
    public void initView(View view) {

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog();
            }
        });
    }


    @OnClick({R.id.tv_local,R.id.tv_camera})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_camera:
                cancelDialog();
                listener.onClick(0);
                break;
            case R.id.tv_local:
                cancelDialog();
                listener.onClick(1);
                break;
        }

    }
}