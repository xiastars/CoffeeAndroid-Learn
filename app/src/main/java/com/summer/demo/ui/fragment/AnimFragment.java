package com.summer.demo.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.summer.demo.R;
import com.summer.demo.transformer.RotateDownPageTransformer;
import com.summer.helper.view.CircleIndicator;
import com.summer.helper.view.CustomerViewPager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 广告形式的ViewPager
 * @author Administrator
 *
 */
public class AnimFragment extends BaseSimpleFragment {
	CustomerViewPager mBannerViewPager;
	//下面的小点
	CircleIndicator mCircleIndicator;
	int[] pics = {R.drawable.xiehou04,R.drawable.xiehou03,R.drawable.xiehou03};

	//当前广告页面
	int mCurrentItem;

	// 这是一个定时线程
	ScheduledExecutorService scheduledExecutorService;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_banner, null);
		initView(view);
		return view;
	}

	/**
	 * Fragment里面的findViewId由onCreateView返回的View来寻找
	 * @param view
	 */
	private void initView(View view) {
		mBannerViewPager = (CustomerViewPager) view.findViewById(R.id.banners_viewpager);
		mCircleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
		initBanners();
	}

	public void initBanners() {
		AdBanner adBanner = new AdBanner(context, pics);
		mBannerViewPager.setAdapter(adBanner);
		//必须在ViewPager设置Adapter后加
		mCircleIndicator.setViewPager(mBannerViewPager);
		//创建随机数，每次进入时，随机显示一页
		Random r = new Random();
		mCurrentItem = r.nextInt(pics.length);
		mBannerViewPager.setCurrentItem(mCurrentItem);
		//这是个动画效果，有多种动画效果，查看transformer这个包
		mBannerViewPager.setPageTransformer(true,new RotateDownPageTransformer());
	}

	public class AdBanner extends PagerAdapter {

		private Context context;
		private int[] datas;
		private Map<Integer, View> mListViews = new HashMap<Integer, View>();

		public AdBanner(Context context, int[] datas){
			this.datas = datas;
			this.context = context;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return datas != null ? datas.length: 0;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@SuppressLint("NewApi")
		@Override
		public Object instantiateItem(ViewGroup container, final int position) { // 这个方法用来实例化页卡
			View view = LayoutInflater.from(context).inflate(R.layout.item_banner, null);
			//如果自定义View的高度，首先要获取当前View的父布局类型，然后得到这个类型的LayoutParameter
			final ImageView ivImageView = (ImageView) view.findViewById(R.id.item_album);
			ivImageView.setBackgroundResource(datas[position]);
			mListViews.put(position, view);
			container.addView(mListViews.get(position));// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}
	}
	
}
