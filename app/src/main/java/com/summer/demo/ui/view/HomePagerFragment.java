package com.summer.demo.ui.view;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.viewpager.NFPagerTabView;
import com.summer.demo.module.base.viewpager.VFragmentPagerAdapter;
import com.summer.demo.ui.main.BaseMainFragment;
import com.summer.demo.utils.CUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.CustomerViewPager;
import com.summer.helper.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页两个页面 - 普通与自定义
 */
public class HomePagerFragment extends BaseMainFragment implements View.OnClickListener {

    @BindView(R.id.pagerStrip)
    PagerSlidingTabStrip pagerStrip;
    @BindView(R.id.viewPager)
    CustomerViewPager viewPager;

    boolean isFirstEnter;
    int currentPage = -1;

    List<NFPagerTabView> mTabViews;
    List<Fragment> fragments = new ArrayList<>();

    public static HomePagerFragment newInstance() {
        return new HomePagerFragment();
    }

    @Override
    protected void initView(View view) {
        pagerStrip.setPadding(0, 15, 0, 15);

        fragments.add(new CommonViewFragment());
        fragments.add(new CustomViewFragment());
        mTabViews = new ArrayList<>();
        //未选中时的文字颜色
        pagerStrip.setAssitTextColor(getResColor(R.color.grey_cd));
        pagerStrip.setIndicatorColor(getResColor(R.color.transparent));
        //选中时的文字颜色
        pagerStrip.setTextColor(getResColor(R.color.grey_4a));
        NFPagerTabView homeTab = new NFPagerTabView(context, "视图");
        homeTab.setmTextSize(22);
        mTabViews.add(homeTab);
        NFPagerTabView trendsTab = new NFPagerTabView(context, "组件");
        trendsTab.setmTextSize(22);
        mTabViews.add(trendsTab);
        VFragmentPagerAdapter adapter = new VFragmentPagerAdapter(activity.getSupportFragmentManager(), fragments, mTabViews);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setCurrentItem(0);

        pagerStrip.setViewPager(viewPager);
        //单个Tab的宽度
        pagerStrip.setTabWidth(SUtils.getDip(context, 52));
        //埋点
        final String[] clickMark = {"homepager_home", "homepager_trends"};
        pagerStrip.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                CUtils.onClick(context, clickMark[position]);

            }
        });
        //显示新消息提示
        showDots(0,true);
    }

    @Override
    public void loadData() {
        if (pagerStrip == null) {
            return;
        }
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @OnClick({R.id.rl_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_search:
                CUtils.onClick(context, "home_search");
                //跳转搜索
                break;
        }
    }

    @Override
    public void refresh() {

    }

    private void showDots(int pos, boolean show) {
        mTabViews.get(pos).setShowIndicate(show);
    }

    private BaseMainFragment getFragment(int pos) {
        return (BaseMainFragment) fragments.get(pos);
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_home_pager;
    }

}
