package com.summer.demo.module.album;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.video.ViewVideoActivity;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.SUtils;
import com.summer.helper.web.WebContainerActivity;

import java.util.List;

/**
 * 用于打开网页中的视频与图片
 * Created by xiastars on 2017/8/26.
 */

public class MediaHandleJavascriptInterface {
    private Context context;
    public static List<ImageItem> webImsg;//暂时没有合适的方法处理WEBIVEW里的图片大图浏览轮滑，先这样处理

    public MediaHandleJavascriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void openImage(String img) {
        if(!SUtils.isEmptyArrays(webImsg)){
            int count = webImsg.size();
            for(int i = 0;i < count;i++){
                ImageItem item = webImsg.get(i);
                String url = item.getImg();
                if(url != null && url.equals(img)){
                    ViewBigPhotoActivity.show(context,webImsg,i);
                    return;
                }
            }
        }
        JumpTo.getInstance().commonJump(context, ViewBigPhotoActivity.class, img);
    }

    @JavascriptInterface
    public void openVideo(String img) {
        JumpTo.getInstance().commonJump(context, ViewVideoActivity.class, img);
    }

    @JavascriptInterface
    public void openUrl(String url){
        JumpTo.getInstance().commonJump(context, WebContainerActivity.class, url);
    }

}
