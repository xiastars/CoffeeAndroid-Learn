package com.summer.demo;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.summer.demo.fragment.AnimFragment;
import com.summer.demo.fragment.BannerFragment;
import com.summer.demo.fragment.MediaPlayerFragment;
import com.summer.helper.view.CustomerViewPager;
import com.summer.helper.view.PagerSlidingTabStrip;


/**
 * ViewPager的一般用法
 *
 * @author xiastars@vip.qq.com
 */
public class AcViewPager extends FragmentActivity implements View.OnClickListener {
    PagerSlidingTabStrip tabStrip;
    CustomerViewPager mViewPager;
    PagerAdapter mPagerAdapter;
    Context context;
    BannerFragment horFragment;
    AnimFragment picFragment;
    MediaPlayerFragment mediaFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ac_viewpager);
        context = AcViewPager.this;
        initView();
    }

    private void initView() {
        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (CustomerViewPager) findViewById(R.id.content_pager);
        //当前选中的页面的标题颜色
        tabStrip.setTextColor(context.getResources().getColor(R.color.white));
        //未选中的页面的标题颜色
        tabStrip.setAssitTextColor(context.getResources().getColor(R.color.grey_31));
        //标题字体大小
        tabStrip.setTextSize((int) context.getResources().getDimension(R.dimen.text_30));
        //跟着滚动的下划线的高度
        tabStrip.setTabStrokeWidth(5);
        tabStrip.setUnderlineHeight(0);
        //标题中间那竖线的颜色
        tabStrip.setDividerColor(Color.parseColor("#313235"));
        //下面那根线的颜色
        tabStrip.setIndicatorColor(context.getResources().getColor(R.color.red_d3));
        //缓冲个数，一般为3个
        mViewPager.setOffscreenPageLimit(3);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        tabStrip.setViewPager(mViewPager);
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        String[] sorts = {"自动播放广告","有切换动画效果"};

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos){
                case 0:
                    if(horFragment == null){
                        horFragment = new BannerFragment();
                    }
                    return horFragment;
                case 1:
                    if(picFragment == null){
                        picFragment = new AnimFragment();
                    }
                    return picFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return sorts.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sorts[position];
        }

        @Override
        public int getItemPosition(Object object)
        {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //得到缓存的fragment
            Fragment fragment = (Fragment)super.instantiateItem(container,position);
            return fragment;
        }


    }


}
