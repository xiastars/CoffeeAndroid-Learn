package com.summer.demo.ui;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonAdapter;
import com.summer.demo.base.BaseActivity;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.view.NRecycleView;

import java.util.List;

import butterknife.BindView;

/**
 * @Description:纯文本列表页面
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/10 18:00
 */
public abstract class BaseTitleListActivity extends BaseActivity {
    @BindView(R.id.nv_container)
    NRecycleView nvContainer;

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.ac_main;
    }

    @Override
    protected void initData() {
        nvContainer.setList();
        CommonAdapter adapter = new CommonAdapter(context, new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                clickChild(position);
            }
        });
        nvContainer.setAdapter(adapter);
        adapter.notifyDataChanged(setData());
    }


    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    protected abstract List<String> setData();
    protected abstract void clickChild(int pos);
}
