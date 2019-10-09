package com.summer.demo.ui.fragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.Logs;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/14 10:56
 */
public class EmptyFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        Logs.i("view------------pause");
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_empty;
    }
}
