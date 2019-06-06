package com.summer.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.summer.demo.adapter.PagerAdapter;
import com.summer.demo.adapter.VerticalViewPager;

import java.util.Random;


public class Ac14VerticalViewPagerActivity extends Activity {
	private VerticalViewPager mViewPager;
	private String tag="Ac14VerticalViewPagerActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_14_vertical_viewpager);
        mViewPager = (VerticalViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter());
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
            Log.d(TAG, "instantiateItem:" + position);
            TextView tv = new TextView(Ac14VerticalViewPagerActivity.this);
            tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(60);
            tv.setBackgroundColor(Color.rgb(mRandom.nextInt(255),
                    mRandom.nextInt(255), mRandom.nextInt(255)));
            tv.setTextColor(Color.WHITE);
            switch(position){
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
            Log.d(TAG, "destroyItem:" + position);
            container.removeView((View) object);
        }

    }

}
