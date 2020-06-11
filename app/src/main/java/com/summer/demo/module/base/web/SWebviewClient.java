package com.summer.demo.module.base.web;


import android.graphics.Bitmap;
import android.net.http.SslError;
import android.text.TextUtils;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.summer.helper.utils.Logs;
import com.summer.helper.web.CustomWebView;

import java.util.Arrays;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/10 16:33
 */
class SWebviewClient extends WebViewClient {
    private WebFragment mActivity;

    public SWebviewClient(WebFragment activity) {
        super();
        mActivity = activity;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        /** 页面加载完成需要加载Bridge */
        ((CustomWebView) view).notifyPageFinished();
        mActivity.onPageFinished(url);
        super.onPageFinished(view, url);

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        ((CustomWebView) view).notifyPageStarted();
        mActivity.onPageStarted(url);
        Logs.i("xia","onPageStarted:"+url);
        super.onPageStarted(view, url, favicon);

    }

    public void onReceivedSslError(WebView view, final SslErrorHandler handler,
                                   SslError error) {

        StringBuilder sb = new StringBuilder();

        sb.append("访问错误");
        sb.append("\n\n");

    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (isExternalApplicationUrl(url)) {
            return true;
        }else{
            if(!url.startsWith("http") && !url.equals("https")){
                return false;
            }
        }
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {
        if(!TextUtils.isEmpty(failingUrl)){
            if(failingUrl.startsWith("http") || failingUrl.equals("https")){
                view.loadUrl("file:///android_asset/errorpage/errorpage.html?failingUrl="
                        + failingUrl);
            }
        }
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view,
                                          final HttpAuthHandler handler, final String host, final String realm) {

        boolean reuseHttpAuthUsernamePassword = handler
                .useHttpAuthUsernamePassword();

        if (reuseHttpAuthUsernamePassword && view != null) {
            String[] credentials = view
                    .getHttpAuthUsernamePassword(host, realm);
            if (credentials != null && credentials.length == 2) {
                Logs.i("xia","receiver:"+ Arrays.toString(credentials));
            }
        }
    }

    private boolean isExternalApplicationUrl(String url) {
        return url.startsWith("vnd.") || url.startsWith("rtsp://")
                || url.startsWith("itms://") || url.startsWith("itpc://");
    }
}
