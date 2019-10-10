package com.summer.demo.ui.view.customfragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.view.NRecycleView;

import butterknife.BindView;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/10 16:38
 */
public class ListItemFragment extends BaseFragment {
    @BindView(R.id.sv_container)
    NRecycleView nvContainer;

    @Override
    protected void initView(View view) {
        nvContainer.setList();
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
