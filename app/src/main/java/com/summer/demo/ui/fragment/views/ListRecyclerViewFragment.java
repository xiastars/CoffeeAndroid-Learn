package com.summer.demo.ui.fragment.views;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.recycle.NewSRecycleView;

import butterknife.BindView;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/17 11:16
 */
public class ListRecyclerViewFragment extends BaseFragment {
    @BindView(R.id.nv_container)
    NewSRecycleView nvContainer;

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
        return R.layout.fragment_list_rec;
    }

}
