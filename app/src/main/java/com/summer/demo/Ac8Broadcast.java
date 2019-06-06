package com.summer.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;


/**
 * 介绍Broadcast 跨Activity提醒更新，比如发帖什么的，提示首页更新
 * 这里点击一个按钮，让Html即上个页面弹出一个Dialog
 * @编者 夏起亮
 *
 */
public class Ac8Broadcast extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_8_broadcast);
		
		final Button button = (Button) findViewById(R.id.button);

		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
		
	}

}
