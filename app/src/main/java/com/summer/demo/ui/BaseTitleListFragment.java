package com.summer.demo.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonAdapter;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.view.NRecycleView;

import java.util.List;

import butterknife.BindView;

/**
 * @Description:纯文本列表页面
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/10 18:00
 */
public abstract class BaseTitleListFragment extends BaseFragment {
    @BindView(R.id.nv_container)
    NRecycleView nvContainer;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;


    FragmentManager fragmentManager;
    /* 当前显示的Fragment */
    Fragment mFragment;


    @Override
    protected int setContentView() {
        return R.layout.ac_main;
    }

    @Override
    protected void initView(View view) {
        fragmentManager = getFragmentManager();
        nvContainer.setList();
        nvContainer.setDivider();
        CommonAdapter adapter = new CommonAdapter(context, new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                clickChild(position);
            }
        });
        nvContainer.setAdapter(adapter);
        adapter.notifyDataChanged(setData());
        Logs.i("---------------");
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    public void showFragment(Fragment fragment) {
        //销毁已显示的Fragment
        removeFragment();

        beginTransation(fragment);
    }

    @Override
    public void removeChildFragment() {
        super.removeChildFragment();
        removeFragment();
    }

    /**
     * 添加Fragment
     *
     * @param fragment
     */
    private void beginTransation(Fragment fragment) {
        mFragment = fragment;
        setHasChildFragment(true);
        flContainer.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().add(R.id.fl_container, fragment).commit();
    }

    /**
     * 销毁Fragment最适用的方法是将它替换成一个空的
     */
    private void removeFragment() {
        mFragment = null;
        setHasChildFragment(false);
        flContainer.setVisibility(View.GONE);
        Fragment fragment = new Fragment();
        fragmentManager.beginTransaction().replace(R.id.fl_container, fragment).commit();
    }


    protected abstract List<String> setData();

    protected abstract void clickChild(int pos);
}
