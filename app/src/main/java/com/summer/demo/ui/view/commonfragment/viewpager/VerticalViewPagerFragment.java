package com.summer.demo.ui.view.commonfragment.viewpager;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;

import java.util.Random;


/**
 * 垂直的ViewPager
 */
public class VerticalViewPagerFragment extends BaseFragment {
    private VerticalViewPager mViewPager;
    private String tag = "Ac14VerticalViewPagerActivity";

    @Override
    protected void initView(View view) {
        mViewPager = (VerticalViewPager) view.findViewById(R.id.viewpager);
        MyPagerAdapter pagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                pagerAdapter.notifyDataSetChanged();
            }
        },100);

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_vertical_viewpager;
    }


    private class MyPagerAdapter extends PagerAdapter {

        private static final String TAG = "PagerAdapter";
        private Random mRandom = new Random();

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TextView tv = new TextView(context);
            tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(60);
            tv.setBackgroundColor(Color.rgb(mRandom.nextInt(255),
                    mRandom.nextInt(255), mRandom.nextInt(255)));
            tv.setTextColor(Color.WHITE);
            switch (position) {
                case 0:
                    tv.setText("南");
                    break;
                case 1:
                    tv.setText("无");
                    break;
                case 2:
                    tv.setText("阿");
                    break;
                case 3:
                    tv.setText("弥");
                    break;
                case 4:
                    tv.setText("陀");
                    break;
                case 5:
                    tv.setText("佛");
                    break;
            }
            container.addView(tv);

            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

}
