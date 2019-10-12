package com.summer.demo.ui.course;

import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.view.RoundAngleImageView;
import com.summer.helper.web.CustomWebView;

import butterknife.BindView;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/12 14:26
 */
public class CourseWebFragment extends BaseFragment {

    @BindView(R.id.webview_container)
    CustomWebView webviewContainer;
    @BindView(R.id.iv_loading_icon)
    RoundAngleImageView ivLoadingIcon;
    @BindView(R.id.loading_container)
    LinearLayout loadingContainer;
    @BindView(R.id.rl_container_layout)
    RelativeLayout rlContainerLayout;

    private String loadPageUrl;

    public static CourseWebFragment show(String url) {
        CourseWebFragment markDownFragment = new CourseWebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        markDownFragment.setArguments(bundle);
        return markDownFragment;
    }


    @Override
    protected void initView(View view) {
        initializeCurrentWebView();
        setData();
    }

    /**
     * 其它应用跳转监听
     */
    private void setData() {
        loadPageUrl = getArguments().getString("url");
        navigateToUrl(loadPageUrl);

    }

    /**
     * 设置跳转Url显示路径
     */
    public void navigateToUrl(String url) {
        if ((url != null) && (url.length() > 0)) {
            webviewContainer.loadUrl(url);

        }
    }

    /**
     * 初始化当前WebView
     */
    public void initializeCurrentWebView() {
        webviewContainer.setWebViewClient(new WebViewClient());
        webviewContainer.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(final String url,
                                        final String userAgent, final String contentDisposition,
                                        final String mimetype, final long contentLength) {

            }

        });
    }


    private long lastTime = System.currentTimeMillis();


    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_webview;
    }
}
