package com.summer.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * WebView示例
 * @编者 夏起亮
 *
 */
public class Ac5WebView extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_5_webview);
		
		final Activity activity = this;
		RelativeLayout origin = (RelativeLayout)findViewById(R.id.origin);
		WebView webView = new WebView(this);
		//将WebView填到layout中
		origin.addView(webView);
		/*
		 * Setting是WebView的一个工具类，可以用来设置各种功能
		 */
		webView.getSettings().setSupportZoom(true);//支持缩放
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);//支持放大
	
		
		/*
		 * 当加载错误时
		 */
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(activity,"oh,no,网络崩溃了",Toast.LENGTH_LONG).show();
			}
		/*
		 * 当一个新的url即将被装载到当前WebView时，给主程序一个机会接管对此url的控制。
		 * 如果没有提供WebViewClient，WebView会请求Activity Manager选择一个合适的handler处理url.
		 * 如果提供了WebViewClient，返回true意味着主程序处理url，返回false意味着当前WebView处理url.
		 * 请求POST方法不会调用这个方法
		 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
//		webView.loadUrl("http://baidu.com/");
		webView.loadUrl("file:///android_asset/index.html");
	}
	

}
