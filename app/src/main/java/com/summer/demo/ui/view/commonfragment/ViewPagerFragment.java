package com.summer.demo.ui.view.commonfragment;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.summer.demo.ui.BaseTitleListFragment;
import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.view.commonfragment.viewpager.BannerFragment;
import com.summer.demo.ui.view.commonfragment.viewpager.CommonVPFragment;
import com.summer.demo.ui.view.commonfragment.viewpager.LeftDotViewPager;
import com.summer.demo.ui.view.commonfragment.viewpager.VerticalViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager的用法
 *
 * @author xiastars
 */
public class ViewPagerFragment extends BaseTitleListFragment implements View.OnClickListener {

    FragmentContainerActivity activity;
    final int POS_COMMON = 0;
    final int POS_LEFTDOT = 1;
    final int POS_VERTICAL = 2;
    final int POS_Banner = 3;

    @Override
    protected List<String> setData() {
        activity = (FragmentContainerActivity) getActivity();
        List<String> datas = new ArrayList<>();
        datas.add("基本的ViewPager");
        datas.add("标题自定义位置的ViewPager");

        datas.add("垂直翻页的ViewPager");
        datas.add("广告栏");
        return datas;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void clickChild(int pos) {
        switch (pos) {
            case POS_COMMON:
                showFragment(new CommonVPFragment());
                break;
            case POS_LEFTDOT:
                showFragment(new LeftDotViewPager());
                break;
            case POS_VERTICAL:
                showFragment(new VerticalViewPagerFragment());
                break;
            case POS_Banner:
                showFragment(new BannerFragment());
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }


}
