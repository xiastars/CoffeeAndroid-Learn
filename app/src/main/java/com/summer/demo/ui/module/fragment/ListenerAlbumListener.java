package com.summer.demo.ui.module.fragment;

import android.content.Intent;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.Logs;

/**
 * @Description: 相册监听
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/20 10:32
 */
public class ListenerAlbumListener extends BaseFragment {

    final String NEW_PICTRUE = "com.android.camera.NEW_PICTURE";

    @Override
    protected void initView(View view) {
        initBroadcast(NEW_PICTRUE);
    }

    @Override
    protected void onMsgReceiver(String action, Intent intent) {
        super.onMsgReceiver(action, intent);
        switch (action){
            case NEW_PICTRUE:
                Logs.i(intent+"");
                break;
        }
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_empty;
    }
}
