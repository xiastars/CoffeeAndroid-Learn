package com.summer.demo.ui.fragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.base.BaseFragment;
import com.summer.helper.view.NRecycleView;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * @Description: 属性动画
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/10 12:10
 */
public class ObjectAnimFragment extends BaseFragment {
    @BindView(R.id.sv_container)
    NRecycleView svContainer;
    Unbinder unbinder;

    @Override
    protected void initView(View view) {

    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_nrecyleview;
    }

}
