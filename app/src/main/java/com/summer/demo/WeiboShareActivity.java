package com.summer.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.summer.demo.helper.EasyHttpHelper;
import com.summer.demo.ui.module.fragment.login.LoginOathInfo;
import com.summer.demo.ui.module.fragment.login.OathConfig;
import com.summer.demo.ui.module.fragment.login.OathType;
import com.summer.demo.ui.module.fragment.share.ShareInfo;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

/**
 * 这个类要在主包下
 * Created by xiaqiliang on 2017/5/12.
 */

public class WeiboShareActivity extends Activity implements WbShareCallback {
    ShareInfo shareEntity;
    WbShareHandler shareHandler;
    Bitmap resizeBitmap;
    Context context;
    Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        shareEntity = (ShareInfo) JumpTo.getObject(this);
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        try {
            WbSdk.checkInit();
            if (mAccessToken.isSessionValid()) {
                shareHandler = new WbShareHandler(this);
                shareHandler.registerApp();
                if (shareEntity == null) {
                    this.finish();
                    return;
                }
                sendMultiMessage(true, true);
                this.finish();
            } else {
                requestOath(OathType.WEIBO);
            }
        } catch (Exception e) {
            requestOath(OathType.WEIBO);
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        shareHandler.doResultIntent(intent, this);
        Logs.i("-------------------");
    }

    /**
     * 请求授权,这里的做法是从服务器拿微博的APP_KEY，也可以直接写在项目上
     *
     * @param fromType
     */
    private void requestOath(final String fromType) {
        SummerParameter parameter = new SummerParameter();
        parameter.put("platform", fromType);
        parameter.put("client", "app");
        parameter.put("state", OathType.WEIBO);
        parameter.put("redirectUrl", "回调地址，与微博后台填写保持一致");
        EasyHttpHelper easyHttpHelper = new EasyHttpHelper(context);
        easyHttpHelper.getData(EasyHttpHelper.REQUEST_OATH_WEIBO,LoginOathInfo.class,parameter,"url");
        easyHttpHelper.setOnEasyHttpResultListener(new EasyHttpHelper.OnEasyHttpResultListener() {
            @Override
            public void onResult(int requestCode, Object obj, boolean succeed) {
                if(requestCode != EasyHttpHelper.REQUEST_OATH_WEIBO){
                    return;
                }
                LoginOathInfo info = (LoginOathInfo) obj;
                if (info != null) {
                    String code = info.getAppid();
                    if (fromType.equals(OathType.WEIBO)) {
                        OathConfig.WEIBO_APP_KEY = code;
                        OathConfig.WEIBO_REDIRECT_URL = info.getRedirectUrl();
                        Logs.i("code," + code + ",,," + info.getRedirectUrl());
                        AppContext.getInstance().registToWeibo(code, info.getRedirectUrl());
                        shareHandler = new WbShareHandler(WeiboShareActivity.this);
                        shareHandler.registerApp();
                        sendMultiMessage(true, true);

                    }
                }
            }
        });

    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     */
    private void sendMultiMessage(boolean hasText, boolean hasImage) {
        if (shareEntity == null) {
            this.finish();
            return;
        }
        final WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        if (hasText) {
            weiboMessage.textObject = getTextObj();
        }
        if (shareEntity.getShareImg() != null) {
            SUtils.getPicBitmap(context, shareEntity.getShareImg(), new SimpleTarget() {
                @Override
                public void onResourceReady(Object o, GlideAnimation glideAnimation) {
                    if (o instanceof GlideBitmapDrawable) {
                        GlideBitmapDrawable b = (GlideBitmapDrawable) o;
                        resizeBitmap = b.getBitmap();
                    }
                    startShare(weiboMessage);
                }
            });
            return;
        } else {
            shareEntity.setDrawableId(R.drawable.ic_logo);
            resizeBitmap = getLocalImg();
        }

        startShare(weiboMessage);
    }

    private void startShare(WeiboMultiMessage weiboMessage) {
        //weiboMessage.mediaObject = getWebpageObj();
        if (resizeBitmap != null) {
            weiboMessage.imageObject = getImageObj();
        }
        shareHandler.shareMessage(weiboMessage, true);
        this.finish();
    }

    private Bitmap getLocalImg() {
        BitmapDrawable drawable = (BitmapDrawable) this.getResources().getDrawable(shareEntity.getDrawableId());
        Bitmap bitmap = drawable.getBitmap();
        return getWxShareBitmap(bitmap);
    }

    protected Bitmap getWxShareBitmap(Bitmap targetBitmap) {
        float scale = Math.min((float) 150 / targetBitmap.getWidth(), (float) 150 / targetBitmap.getHeight());
        Bitmap fixedBmp = Bitmap.createScaledBitmap(targetBitmap, (int) (scale * targetBitmap.getWidth()), (int) (scale * targetBitmap.getHeight()), false);
        return fixedBmp;
    }


    @Override
    public void onWbShareSuccess() {
        Logs.i("xia", "分享成功");

        this.finish();
        // Toast.makeText(this, R.string.weibosdk_demo_toast_share_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onWbShareFail() {
        this.finish();
     /*   Toast.makeText(this,
                getString(R.string.weibosdk_demo_toast_share_failed) + "Error Message: ",
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onWbShareCancel() {
        this.finish();
    }

    /**
     * 创建文本消息对象。
     *
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {

        TextObject textObject = new TextObject();
        if (shareEntity == null) return textObject;
        textObject.text = shareEntity.getShareTitle() + shareEntity.getUrl();
        textObject.title = shareEntity.getShareTitle() + shareEntity.getUrl();
        textObject.actionUrl = shareEntity.getUrl();
        return textObject;
    }

    /**
     * 创建图片消息对象。
     *
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(resizeBitmap);
        return imageObject;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = shareEntity.getShareTitle();
        String url = shareEntity.getShareImg();
        if (url == null) {
            url = "";
        }
        mediaObject.description = shareEntity.getShareContent() + url;
        if (resizeBitmap != null) {
            mediaObject.setThumbImage(resizeBitmap);
        }
        mediaObject.actionUrl = shareEntity.getUrl();
        mediaObject.defaultText = shareEntity.getShareContent();
        return mediaObject;
    }

}
