package com.summer.demo.ui.view.commonfragment.recyclerview;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonListAdapter;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.view.NRecycleView;

import butterknife.BindView;

/**
 * 学习GridView静态数据的写法
 *
 * @author xiastars@vip.qq.com
 */
public class CommonGridFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.sv_container)
    NRecycleView svContainer;


    @Override
    protected void initView(View view) {
        //设置每行几列
        svContainer.setGridView(3);
        svContainer.setDivider();
        svContainer.setAdapter(new CommonListAdapter(context));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}