package com.summer.demo.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonGridAdapter;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.view.NRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description:格子列表页面
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/10 18:00
 */
public abstract class BaseGridListActivity extends BaseActivity {
    @BindView(R.id.nv_container)
    NRecycleView nvContainer;
    FragmentManager fragmentManager;
    /* 当前显示的Fragment */
    BaseFragment mFragment;

    CommonGridAdapter adapter;

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
        fragmentManager = this.getSupportFragmentManager();
        nvContainer.setGridView(3);
        nvContainer.setDivider();
        adapter = new CommonGridAdapter(context, new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                clickChild(position);
            }
        });
        nvContainer.setAdapter(adapter);
    }

    protected void setData(String[] titles ,int[] bgs){
        adapter.setBackgroundImgs(bgs);
        List<String> title = new ArrayList<String>();
        /* 从XML里获取String数组的方法*/
        for (int i = 0; i < titles.length; i++) {
            String ti = titles[i];
            title.add(ti);
        }
        adapter.notifyDataChanged(title);
    }


    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }


    public void showFragment(BaseFragment fragment) {
        //销毁已显示的Fragment
        removeFragment();
        beginTransation(fragment);
    }

    /**
     * 销毁Fragment最适用的方法是将它替换成一个空的
     */
    private void removeFragment() {
        Logs.i("removeFragment"+mFragment);
        mFragment = null;
        findViewById(R.id.ll_container).setVisibility(View.GONE);
        Fragment fragment = new Fragment();
        fragmentManager.beginTransaction().replace(R.id.ll_container, fragment).commit();
    }

    /**
     * 监听系统的返回键，当处于Fragment时，点击返回，则回到主界面
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFragment != null) {
                removeFragment();
                return true;
            } else {
                finish();
            }
            return false;
        }
        return false;
    }

    /**
     * 添加Fragment
     *
     * @param fragment
     */
    private void beginTransation(BaseFragment fragment) {
        mFragment = fragment;
        findViewById(R.id.ll_container).setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().add(R.id.ll_container, fragment).commit();
    }

    protected abstract void clickChild(int pos);
}
