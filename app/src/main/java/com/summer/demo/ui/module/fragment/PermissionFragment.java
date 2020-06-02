package com.summer.demo.ui.module.fragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.permission.PermissionUtils;
import com.yanzhenjie.permission.Permission;

/**
 * @Description: 权限使用
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/2 16:09
 */
public class PermissionFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        PermissionUtils.showPermissionDialog(activity, Permission.ACCESS_COARSE_LOCATION,Permission.CAMERA,Permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_empty;
    }
}
