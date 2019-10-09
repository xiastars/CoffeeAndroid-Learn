package com.summer.demo.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonAdapter;
import com.summer.demo.module.base.BaseActivity;
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
public abstract class BaseTitleListActivity extends BaseActivity {
    @BindView(R.id.nv_container)
    NRecycleView nvContainer;
    FragmentManager fragmentManager;
    /* 当前显示的Fragment */
    BaseFragment mFragment;

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

    protected abstract List<String> setData();
    protected abstract void clickChild(int pos);
}
