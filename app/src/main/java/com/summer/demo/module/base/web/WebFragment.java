package com.summer.demo.module.base.web;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.malata.summer.helper.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.JumpTo.ShortcutJump;
import com.summer.helper.utils.Logs;
import com.summer.helper.web.CustomWebView;

import java.io.File;
import butterknife.BindView;

public class WebFragment extends BaseFragment {

    @BindView(R.id.webview_container)
    CustomWebView webviewContainer;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    @BindView(R.id.rl_container_layout)
    RelativeLayout rlContainerLayout;

    private String loadPageUrl;
    private String title;
    private boolean isLoadingEnd = false;

    @Override
    protected void initView(View view) {
        initView();
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_webview;
    }

    private void initView() {
        setLoadingVisible();
        initializeCurrentWebView();
        Bundle bundle = getArguments();
        title = bundle.getString(ShortcutJump.TYPE_NAME);
        loadPageUrl = bundle.getString(ShortcutJump.TYPE_URL);
        navigateToUrl(loadPageUrl);
        if (!TextUtils.isEmpty(loadPageUrl)) {
            webviewContainer.setLoadedUrl(loadPageUrl);
        }
        if (!TextUtils.isEmpty(title)) {
            webviewContainer.setOriginalTitle(title);
        }
    }

    /**
     * 初始化当前WebView
     */
    public void initializeCurrentWebView() {
        webviewContainer.setWebViewClient(new SWebviewClient(this));
        webviewContainer.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(final String url,
                                        final String userAgent, final String contentDisposition,
                                        final String mimetype, final long contentLength) {

            }

        });
    }


    /**
     * 设置跳转Url显示路径
     */
    public void navigateToUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            Logs.i("跳转URL有误");
            return;
        }
        //file 为asset里的文件
        if (url.startsWith("http") || url.startsWith("file")|| new File(url).exists()) {
            webviewContainer.loadUrl(url);
        } else {
            webviewContainer.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
        }
    }

    public CustomWebView getCurrentWebView() {
        return webviewContainer;
    }

    private void setLoadingVisible() {
        if (isLoadingEnd) return;
        progressBar.setVisibility(View.VISIBLE);
    }

    public void onPageStarted(String url) {
    }

    public void onPageFinished(String url) {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

    }


}
