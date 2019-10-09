package com.summer.demo.ui.fragment.viewpager;

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
 * @Description: 标题栏自定义位置，标题栏有消息提醒
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/14 9:44
 */
public class LeftDotViewPager extends BaseFragment {

    @BindView(R.id.pagerStrip)
    PagerSlidingTabStrip pagerStrip;
    @BindView(R.id.viewPager)
    CustomerViewPager viewPager;
    List<Fragment> fragments = new ArrayList<>();

    int currentPage = -1;

    private List<NFPagerTabView> mTabViews;

    public static LeftDotViewPager newInstance() {
        return new LeftDotViewPager();
    }

    @Override
    protected void initView(View view) {
        Logs.i("pause");
        Logs.i("pause" + Runtime.getRuntime().totalMemory() / 1024);
        pagerStrip.setPadding(0, 15, 0, 15);
        pagerStrip.setTabWidth(SUtils.getSWidth(activity, 15));
        fragments.add(new EmptyFragment());
        fragments.add(new EmptyFragment());
        fragments.add(new EmptyFragment());
        mTabViews = new ArrayList<>();
        pagerStrip.setAssitTextColor(getResColor(R.color.grey_cd));
        pagerStrip.setIndicatorColor(getResColor(R.color.transparent));
        pagerStrip.setTextColor(getResColor(R.color.grey_4a));
        mTabViews.add(new NFPagerTabView(context, "天", 22));
        mTabViews.add(new NFPagerTabView(context, "地", 22));
        mTabViews.add(new NFPagerTabView(context, "人", 22));
        VFragmentPagerAdapter adapter = new VFragmentPagerAdapter(activity.getSupportFragmentManager(), fragments, mTabViews);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setCurrentItem(0);
        pagerStrip.setViewPager(viewPager);
        pagerStrip.setTabWidth(SUtils.getDip(context, 32));
        //埋点
        final String[] clickMark = {"sky", "earth", "people"};
        pagerStrip.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                CUtils.onClick(context, clickMark[position]);

            }
        });
        //显示新消息提醒
        showDots(0, true);
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
        return R.layout.fragment_leftdot_viewpager;
    }
}

