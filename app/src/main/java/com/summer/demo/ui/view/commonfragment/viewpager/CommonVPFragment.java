package com.summer.demo.ui.view.commonfragment.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.base.viewpager.NFPagerTabView;
import com.summer.demo.module.base.viewpager.VFragmentPagerAdapter;
import com.summer.demo.ui.fragment.EmptyFragment;
import com.summer.demo.utils.CUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.CustomerViewPager;
import com.summer.helper.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: 最普通的一个
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/14 9:44
 */
public class CommonVPFragment extends BaseFragment {

    @BindView(R.id.pagerStrip)
    PagerSlidingTabStrip pagerStrip;
    @BindView(R.id.viewPager)
    CustomerViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();

    int currentPage = -1;

    private List<NFPagerTabView> mTabViews;

    public static CommonVPFragment newInstance() {
        return new CommonVPFragment();
    }

    @Override
    protected void initView(View view) {
        pagerStrip.setTabWidth(SUtils.getSWidth(activity, 15));
        Logs.i("-------"+new EmptyFragment());
        fragments.add(new EmptyFragment());
        fragments.add(new EmptyFragment());
        mTabViews = new ArrayList<>();
        //设置未选中的标题颜色
        pagerStrip.setAssitTextColor(getResColor(R.color.grey_cd));
        //底部滑动的线
        pagerStrip.setIndicatorColor(getResColor(R.color.red_d3));
        //底部滑动的线的高度
        pagerStrip.setIndicatorHeight(1);
        //中间的标题分割线
        pagerStrip.setDividerColor(getResColor(R.color.blue_0a));
        //设置选中的标题颜色
        pagerStrip.setTextColor(getResColor(R.color.grey_4a));
        NFPagerTabView homeTab = new NFPagerTabView(context, "帧动画");
        homeTab.setmTextSize(22);
        mTabViews.add(homeTab);
        NFPagerTabView trendsTab = new NFPagerTabView(context, "属性动画");
        trendsTab.setmTextSize(22);
        mTabViews.add(trendsTab);
        VFragmentPagerAdapter adapter = new VFragmentPagerAdapter(activity.getSupportFragmentManager(), fragments, mTabViews);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setCurrentItem(0);
        pagerStrip.setViewPager(viewPager);
        pagerStrip.setTabWidth(SUtils.getDip(context, 32));
        //埋点
        final String[] clickMark = {"frame_anim", "object_anim"};
        pagerStrip.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                CUtils.onClick(context, clickMark[position]);

            }
        });
        //显示新消息提醒
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

    @Override
    public void onPause() {
        super.onPause();
        //警告：Fragment里写Viewpager，Viewpager里的Fragment必须清除掉，否则下次进来，无法显示View
        for (int i = 0; i < fragments.size(); i++) {
            activity.getSupportFragmentManager().beginTransaction().remove(fragments.get(i)).commit();
        }
    }

    @Override
    public void refresh() {
        pageIndex = 0;
        lastId = null;
        loadData();
    }

    private void showDots(int pos, boolean show) {
        mTabViews.get(pos).setShowIndicate(show);
    }


    @Override
    protected int setContentView() {
        return R.layout.fragment_common_viewpager;
    }
}

