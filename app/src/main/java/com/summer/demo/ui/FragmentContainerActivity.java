package com.summer.demo.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragmentActivity;
import com.summer.demo.ui.fragment.views.TextViewFragment;
import com.summer.demo.ui.ui.CDrawableFragment;
import com.summer.helper.utils.JumpTo;

import butterknife.BindView;

/**
 * @Description: Fragment容器
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 11:44
 */
public class FragmentContainerActivity extends BaseFragmentActivity {
    @BindView(R.id.rl_container)
    FrameLayout rlContainer;

    Fragment mFragment;
    FragmentManager fragmentManager;

    @Override
    protected void loadData() {

    }

    @Override
    protected void finishLoad() {

    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_fragment_container;
    }

    @Override
    protected void initData() {
        fragmentManager = this.getSupportFragmentManager();
        int type = JumpTo.getInteger(this);
        showViews(type);
    }

    protected void showViews(int type){
        switch (type){
            case 0:
                setTitle("Drawable");
                showFragment(new CDrawableFragment());
                break;
            case 1:
                setTitle("文本");
                showFragment(new TextViewFragment());
                break;
        }
    }

    public void showFragment(Fragment fragment) {
        //销毁已显示的Fragment
        removeFragment();
        beginTransation(fragment);
    }

    /**
     * 添加Fragment
     *
     * @param fragment
     */
    private void beginTransation(Fragment fragment) {
        mFragment = fragment;
        rlContainer.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().add(R.id.rl_container, fragment).commit();
    }

    /**
     * 销毁Fragment最适用的方法是将它替换成一个空的
     */
    private void removeFragment() {
        mFragment = null;
        rlContainer.setVisibility(View.GONE);
        Fragment fragment = new Fragment();
        fragmentManager.beginTransaction().replace(R.id.rl_container, fragment).commit();
    }


}
