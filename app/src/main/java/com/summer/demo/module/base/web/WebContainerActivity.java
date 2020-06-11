package com.summer.demo.module.base.web;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.malata.summer.helper.R;
import com.summer.demo.module.base.BaseFragmentActivity;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.web.ActivitysManager;

/**
 * 显示网页
 */
public class WebContainerActivity extends BaseFragmentActivity {
	
	private WebContainerActivity INSTANCE;
	FrameLayout llContainerLayout;

	/* 当前显示的Fragment */
	WebFragment mFragment;
	//暂时不做同个页面保留多个页面的功能
	SparseArray<Fragment> fragments;

	public String title;
	public int isFromMain = 0;
	private boolean isHomeKey = false;

    /**
     * 直接跳转
	 * @param context
     * @param url
     * @param title
	 */
	public static void show(Context context,String url,String title){
		Intent intent = new Intent(context, WebContainerActivity.class);
		intent.putExtra(JumpTo.ShortcutJump.TYPE_NAME, title);
		intent.putExtra(JumpTo.ShortcutJump.TYPE_URL, url);
		context.startActivity(intent);
	}
	
	private void initView() {
		llContainerLayout = (FrameLayout) findViewById(R.id.container_layout);
		Intent intent = getIntent();
		WebFragment webFragment = new WebFragment();
		webFragment.setArguments(intent.getExtras());
		showFragment(webFragment);
		setTitle(intent.getStringExtra(JumpTo.ShortcutJump.TYPE_NAME));
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

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
		return R.layout.activity_webview_container;
	}

	@Override
	protected void initData() {
		initView();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(isHomeKey && isFromMain == 1){
			isHomeKey = false;
			finish();
		}
	}

	private void navigatePrevious() {
		if (mFragment.getCurrentWebView().canGoBack()) {
			mFragment.getCurrentWebView().goBack();
		} else {
			ActivitysManager.finish("WebContainerActivity");
			finish();
			isFromMain = 0;
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			navigatePrevious();
			return true;
		}
		return super.dispatchKeyEvent(event);
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
		mFragment = (WebFragment) fragment;
		findViewById(R.id.container_layout).setVisibility(View.VISIBLE);
		fragmentManager.beginTransaction().add(R.id.container_layout, fragment).commit();
	}

	/**
	 * 销毁Fragment最适用的方法是将它替换成一个空的
	 */
	private void removeFragment() {
		mFragment = null;
		findViewById(R.id.container_layout).setVisibility(View.GONE);
		Fragment fragment = new Fragment();
		fragmentManager.beginTransaction().replace(R.id.container_layout, fragment).commit();
	}
	
}
