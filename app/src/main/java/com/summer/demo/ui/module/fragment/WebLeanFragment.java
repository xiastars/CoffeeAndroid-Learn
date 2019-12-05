package com.summer.demo.ui.module.fragment;

import android.view.View;
import android.webkit.WebView;

import com.summer.demo.R;
import com.summer.demo.module.album.MediaHandleJavascriptInterface;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.web.CustomWebView;
import com.summer.helper.web.SWebviewClient;
import com.summer.helper.web.WebContainerActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: Webview访问测试
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/10 11:29
 */
public class WebLeanFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.webview)
    CustomWebView customWebView;

    @Override
    protected void initView(View view) {
        String data = "<p>31231111111</p>\\n<table style=\\\"border-collapse: collapse; width: 100%;\\\" border=\\\"1\\\">\\n<tbody>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">" +
                "&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"" +
                "width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\"" +
                ">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>" +
                "\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n<tr>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">" +
                "<img src='static/js/plugins/tinymce4.7.5/plugins/emoticons/img/smiley-innocent.gif' alt=\\\"innocent\\\" /></td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n<td style=\\\"width: 14.2857%;\\\">&nbsp;</td>\\n</tr>\\n</tbody>\\n</table>";
        setWebViewContent(customWebView, data);
    }

    /**
     * 设置统一富文本显示
     *
     * @param wbContainer
     * @param content
     */
    public void setWebViewContent(WebView wbContainer, String content) {
        wbContainer.addJavascriptInterface(new MediaHandleJavascriptInterface(context), "imagelistner");
        wbContainer.setWebViewClient(new SWebviewClient(wbContainer, true, null));
        String finalContent = "<html><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />" +
                "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">" +
                "<style>img{max-width:100%;height:auto;};video{width:100%;};</style> " + content.replaceAll("(\r\n|\r|\n|\n\r)", "<br>") + "</html>";
        wbContainer.loadDataWithBaseURL("http://120.79.56.152:8010/", finalContent, "text/html", "utf-8", "");
    }

    @Override
    public void loadData() {

    }


    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_learn_web;
    }

    @OnClick({R.id.tv_local, R.id.tv_url})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_url:
                String testUrl = "http://www.polayoutu.com/collections";
                JumpTo.getInstance().commonJump(context, WebContainerActivity.class, testUrl);
                break;
            case R.id.tv_local:

                String url = "file:///android_asset/home.html";
                JumpTo.getInstance().commonJump(context, WebContainerActivity.class, url);
                break;
        }
    }
}
