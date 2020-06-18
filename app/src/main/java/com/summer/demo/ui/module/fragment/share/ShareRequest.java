package com.summer.demo.ui.module.fragment.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.ghnor.flora.callback.Callback;
import com.summer.demo.AppContext;
import com.summer.demo.R;
import com.summer.demo.helper.ImgHelper;
import com.summer.demo.ui.module.fragment.dialog.EasyLoading;
import com.summer.helper.utils.BitmapUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMiniProgramObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.File;
import java.io.FileOutputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class ShareRequest {

    private Context mContext;
    private ShareInfo mShareInfo;
    private int SHARE_TYPE = 0;

    public ShareRequest(Context context, ShareInfo shareInfo) {
        mContext = context;
        this.mShareInfo = shareInfo;
        initShareInfo(mShareInfo);
        if (!(mContext instanceof Activity)) {
            throw new InvalidParameterException("context must from activity !");
        }

    }

    public void shareImageToSystem(int imageId) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imageId);
        shareImageToSystem(bitmap);
    }

    public void shareImageToSystem(Bitmap bitmap) {
        if (bitmap == null)
            return;

        File file = new File(SFileUtils.getCacheDirectory(), "share.jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            if (file.exists())
                file.delete();
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//            bitmap.recycle();
            out.flush();
            out.close();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.getApplicationContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareImageToSystem(String localPath) {
        File file = new File(localPath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.getApplicationContext().startActivity(intent);
    }

    public void shareToWechat() {

        shareToWeChat(false);
    }

    public void shareToWechatCircle(ShareInfo ShareInfo) {
        initShareInfo(ShareInfo);
        shareToWeChat(true);
    }

    public void shareToWXApp(ShareInfo ShareInfo) {
        initShareInfo(ShareInfo);
        shareToWXapp();
    }

    private void shareToWeChat(final boolean isCircle) {
        if (!AppContext.getInstance().isWxInstall()) {
            SUtils.makeToast(mContext, "请下载微信后重试!");
            return;
        }
        final IWXAPI api = WXAPIFactory.createWXAPI(mContext, ShareConfig.WEIXIN_ID,
                false);
        final String title = mShareInfo.getShareTitle();
        final String content = mShareInfo.getShareContent();
        final String url = mShareInfo.getUrl();
        Bitmap shareBitmap = mShareInfo.getShareBitMap();
        final String imageUrl = mShareInfo.getShareImg();
        int drawableId = mShareInfo.getDrawableId() == 0 ? R.drawable.ic_logo : mShareInfo.getDrawableId();
        boolean isBigPhoto = mShareInfo.isShareBig();
        if (isBigPhoto) {
            File file = new File(SUtils.getAndSaveCurrentImage(mContext, shareBitmap));
            ImgHelper.compressImgOnly(mContext, file.getPath(), new Callback<String>() {
                @Override
                public void callback(String s) {
               /*     File compressFile = new File(s);
                    ArrayList<Uri> list = new ArrayList<>();
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI"));
                    intent.setAction("android.intent.action.SEND_MULTIPLE");
                    intent.setType("images/*");
                    Uri uri = Uri.fromFile(compressFile);
                    list.add(uri);
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,list);
                    mContext.startActivity(intent);*/

                    WXImageObject imgObj = new WXImageObject();
                    imgObj.setImagePath(s);
                    WXMediaMessage msg = new WXMediaMessage();
                    msg.mediaObject = imgObj;

              /*      Bitmap thumbBmp = getWxShareBitmap500(shareBitmap);
                    msg.thumbData = SUtils.getBitmapArrays(thumbBmp);// 设置缩略图
*/
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("img");
                    req.message = msg;
                    if (isCircle) {
                        req.scene = SendMessageToWX.Req.WXSceneTimeline;//判断是否发送到朋友圈
                    } else {

                        req.scene = SendMessageToWX.Req.WXSceneSession;

                    }

                    api.sendReq(req);
                }
            });

  /*          File file = new File(SUtils.getAndSaveCurrentImage(mContext, shareBitmap));
            ImgHelper.compressImgOnly(mContext, file.getPath(), new Callback<String>() {
                @Override
                public void callback(String s) {
                    File compressFile = new File(s);
                    ArrayList<Uri> list = new ArrayList<>();
                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareToTimeLineUI"));
                    intent.setAction("android.intent.action.SEND_MULTIPLE");
                    intent.setType("images/*");
                    Uri uri = Uri.fromFile(compressFile);
                    list.add(uri);
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,list);
                    mContext.startActivity(intent);
                }
            });*/

            return;
        }
        if (SHARE_TYPE == ShareType.WECHAT_APP) {//判断把好友改为小程序
            shareToWXapp();
            return;
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(mContext).load(imageUrl).override(150, 150).dontAnimate().into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (glideDrawable != null && glideDrawable instanceof GlideBitmapDrawable) {
                        GlideBitmapDrawable drawable = (GlideBitmapDrawable) glideDrawable;
                        Bitmap bitmap = drawable.getBitmap();
                        if (bitmap != null) {
                            Bitmap resizeBitmap = getWxShareBitmap(bitmap);
                            sendWechatRequest(title, content, url, resizeBitmap, isCircle);
                        }
                    }
                }
            });
        } else {
            BitmapDrawable drawable = (BitmapDrawable) mContext.getResources().getDrawable(drawableId);
            Bitmap bitmap = drawable.getBitmap();
            Bitmap resizeBitmap = getWxShareBitmap(bitmap);
            sendWechatRequest(title, content, url, resizeBitmap, isCircle);
        }
    }

    public void shareBigPhoto(boolean isCircle) {
        Logs.i("----------");
        if (!AppContext.getInstance().isWxInstall()) {
            SUtils.makeToast(mContext, "请下载微信后重试!");
            return;
        }
        final IWXAPI api = WXAPIFactory.createWXAPI(mContext, ShareConfig.WEIXIN_ID,
                false);
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(mShareInfo.getShareImg());
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        if (isCircle) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;//判断是否发送到朋友圈
        } else {

            req.scene = SendMessageToWX.Req.WXSceneSession;

        }

        api.sendReq(req);
    }

    /**
     * 分享给微信好友
     */
    public void shareToWXapp() {
        if (!AppContext.getInstance().isWxInstall()) {
            SUtils.makeToast(mContext, "请下载微信后重试!");
            return;
        }
        final String title = mShareInfo.getShareTitle();
        final String content = mShareInfo.getShareContent();
        final String url = mShareInfo.getUrl();
        String imageUrl = mShareInfo.getShareImg();
        int drawableId = mShareInfo.getDrawableId() == 0 ? R.drawable.ic_logo : mShareInfo.getDrawableId();
        if (!TextUtils.isEmpty(imageUrl)) {
            Logs.i("imageUrl:" + imageUrl);
            Glide.with(mContext).load(imageUrl).dontAnimate().into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (glideDrawable != null && glideDrawable instanceof GlideBitmapDrawable) {
                        GlideBitmapDrawable drawable = (GlideBitmapDrawable) glideDrawable;
                        Bitmap bitmap = drawable.getBitmap();
                        if (bitmap != null) {
                            Bitmap resizeBitmap = getWxappShareBitmap(bitmap);
                            shareToWXminiApp(title, content, url, resizeBitmap);
                        }
                    }
                }
            });
        } else {
            BitmapDrawable drawable = (BitmapDrawable) mContext.getResources().getDrawable(drawableId);
            Bitmap bitmap = mShareInfo.getShareBitMap();
            Bitmap resizeBitmap = getWxappShareBitmap(bitmap == null ? drawable.getBitmap() : mShareInfo.getShareBitMap());
            shareToWXminiApp(title, content, url, resizeBitmap);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private void sendWechatRequest(String title, String content, String url, Bitmap icon, boolean isCircle) {
        // 获取IWXAPI实例，IWXAPI是第三方app和微信通信的openapi接口
        final IWXAPI api = WXAPIFactory.createWXAPI(mContext, ShareConfig.WEIXIN_ID,
                false);
        // 将应用的appId注册到微信
        api.registerApp(ShareConfig.WEIXIN_ID);
        // 网页
        WXWebpageObject webPageObj = new WXWebpageObject();
        webPageObj.webpageUrl = url;
        // 用WXTextObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.setThumbImage(icon);
        // msg.mediaObject = textObj;
        msg.mediaObject = webPageObj;

        // 发送文本类型的消息时，title字段不起作用
        msg.title = title;
        if (content != null) {
            content = SUtils.HtmlText(content);
            if (content.length() > 140) {
                content = content.substring(0, 139);
            }
            msg.description = content;
        }
        // 构造一个Req
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis()); // transaction字段用于唯一标识一个请求
        req.message = msg;
        //WXSceneTimeline 朋友圈
        //WXSceneSession 微信好友
        req.scene = isCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        // 调用api接口发送数据到微信
        boolean result = api.sendReq(req);
  /*      if (result == true) {
            // shareResult();
            SUtils.makeToast(AppContext.getInstance(),"分享成功");
        }else{
            SUtils.makeToast(AppContext.getInstance(),"分享失败");
        }*/

    }

    /**
     * 分享到微信小程序
     *
     * @param title
     * @param content
     * @param url
     * @param icon
     */
    private void shareToWXminiApp(String title, String content, String url, Bitmap icon) {
        // 获取IWXAPI实例，IWXAPI是第三方app和微信通信的openapi接口
        final IWXAPI api = WXAPIFactory.createWXAPI(mContext, ShareConfig.WEIXIN_ID,
                false);
        // 将应用的appId注册到微信
        api.registerApp(ShareConfig.WEIXIN_ID);
        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXMiniProgramObject miniProgramObject = new WXMiniProgramObject();
        miniProgramObject.webpageUrl = url;
        miniProgramObject.withShareTicket = true;
        Logs.i("url:" + url);
        miniProgramObject.path = mShareInfo.getWxAppPath();
        if (AppContext.SERVER_MODE == 2) {
            miniProgramObject.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
        } else if (AppContext.SERVER_MODE == 3) {
            miniProgramObject.miniprogramType = WXMiniProgramObject.MINIPTOGRAM_TYPE_RELEASE;
        } else {
            miniProgramObject.miniprogramType = WXMiniProgramObject.MINIPROGRAM_TYPE_PREVIEW;
        }
        WXMediaMessage msgs = new WXMediaMessage(miniProgramObject);
        msgs.title = title;
        msgs.setThumbImage(icon);
        msgs.description = content;
        req.message = msgs;
        miniProgramObject.userName = ShareConfig.WECHAT_MINIAPP_ID;//小程序的注册ID
        req.transaction = buildTransaction("webpage");
        req.scene = SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);

    }


    private void initShareInfo(ShareInfo entity) {
        mShareInfo = entity;
        SHARE_TYPE = mShareInfo.getShareType();
    }

    /**
     * 分享成功后，发消息给服务器
     */
    private void sendSuccessResultToServer() {
        //sendSuccessResultToServer(mContext, mShareInfo.getShareType(), mShareInfo.getContentId());
    }

    /**
     * 分享成功后，发消息给服务器
     */
    public static void sendSuccessResultToServer(Context context, int shareType, String contentId) {
      /*  if (shareType != 0 && shareType < 6) {
            SummerParameter parameter = Const.getPostParameters();
            parameter.put("type", shareType);
            parameter.put("targetId", contentId);
            EasyHttp.post(context, Server.SHARE_TOPIC, BaseResp.class, parameter, new RequestCallback<BaseResp>() {
                @Override
                public void done(BaseResp baseResp) {

                }

                @Override
                public void onError(int errorCode, String errorStr) {

                }
            });
        }*/
    }

    /**
     * 分享到QQ空间,注意manifest上要添加的内容
     */
    public void shareToQZone() {
        Tencent mTencent = Tencent.createInstance(ShareConfig.QQ_KEY, AppContext.getInstance());
        if (mTencent == null) {
            return;
        }
        EasyLoading.get(mContext).showNormalLoading();
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mShareInfo.getShareTitle());//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mShareInfo.getShareContent());//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, mShareInfo.getUrl());//必填
        ArrayList<String> arrayList = new ArrayList<>();
        mShareInfo.setDrawableId(R.drawable.ic_logo);
        //需要分享的图片链接，一般可以设置为Logo
        if (!TextUtils.isEmpty(mShareInfo.getShareImg())) {
            arrayList.add(mShareInfo.getShareImg());
            params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, arrayList);
        }
        mTencent.shareToQzone((Activity) mContext, params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                EasyLoading.get(mContext).cancelLoading();
                //如果需要统计或发到服务器，逻辑写在这里
                Logs.i("onComplete");
            }

            @Override
            public void onError(UiError uiError) {
                EasyLoading.get(mContext).cancelLoading();
                Logs.i("onComplete");
            }

            @Override
            public void onCancel() {
                EasyLoading.get(mContext).cancelLoading();
                Logs.i("onComplete");
            }
        });
    }

    /**
     * 分享到QQ
     */
    public void shareToQQ() {
        Tencent mTencent = Tencent.createInstance(ShareConfig.QQ_KEY, AppContext.getInstance());
        if (mTencent == null) {
            return;
        }
        EasyLoading.get(mContext).showNormalLoading();
        IUiListener callBack = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                EasyLoading.get(mContext).cancelLoading();
                //SUtils.makeToast(context, R.string.share_success);
                Logs.i("onComplete");
            }

            @Override
            public void onError(UiError uiError) {
                Logs.i("分享失败" + uiError.errorMessage);
                EasyLoading.get(mContext).cancelLoading();
            }

            @Override
            public void onCancel() {
                EasyLoading.get(mContext).cancelLoading();
            }
        };

        if (!TextUtils.isEmpty(mShareInfo.getShareImg()) && !mShareInfo.getShareImg().startsWith("http")) {
            Bundle params = new Bundle();
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, mShareInfo.getShareImg());
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
            mTencent.shareToQQ((Activity) mContext, params, callBack);
        } else {
            if (!TextUtils.isEmpty(mShareInfo.getUrl()) && !TextUtils.isEmpty(mShareInfo.getShareTitle())) {
                if (!(mContext instanceof Activity)) {
                    return;
                }
                mShareInfo.setDrawableId(R.drawable.ic_logo);
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, mShareInfo.getShareTitle());//必填
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mShareInfo.getShareContent());//选填
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mShareInfo.getUrl());//必填
                if (!TextUtils.isEmpty(mShareInfo.getShareImg())) {
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareInfo.getShareImg());
                }
                Logs.i("xia", mShareInfo.getShareImg() + ",," + mShareInfo.getDrawableId());
                mTencent.shareToQQ((Activity) mContext, params, callBack);
            } else {
                String activityName = "";
                Intent intentSend = new Intent(Intent.ACTION_SEND);
                intentSend.setType("text/plain");
                List<ResolveInfo> listActivity = mContext.getPackageManager().queryIntentActivities(intentSend, 0);
                String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
                for (ResolveInfo resolveInfo : listActivity) {
                    if (TextUtils.equals(QQ_PACKAGE_NAME, resolveInfo.activityInfo.packageName)) {
                        activityName = resolveInfo.activityInfo.name;
                        break;
                    }
                }
                Intent qqIntent = new Intent(Intent.ACTION_SEND);
                qqIntent.setType("text/plain");
                qqIntent.putExtra(Intent.EXTRA_SUBJECT, mShareInfo.getShareTitle());
                qqIntent.setClassName(QQ_PACKAGE_NAME, activityName);
                qqIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                qqIntent.putExtra(Intent.EXTRA_TEXT, mShareInfo.getShareContent());

            }
        }
    }

    private Bitmap getWxappShareBitmap(Bitmap targetBitmap) {
        float scale = Math.min((float) 300 / targetBitmap.getWidth(), (float) 300 / targetBitmap.getHeight());
        Bitmap fixedBmp = Bitmap.createBitmap(targetBitmap, 0, 0, targetBitmap.getWidth(), targetBitmap.getHeight());
        fixedBmp = Bitmap.createScaledBitmap(fixedBmp, 500, 500, false);
        BitmapUtils.getInstance().addBitmap(fixedBmp, mContext);
        return fixedBmp;
    }

    private Bitmap getWxShareBitmap(Bitmap targetBitmap) {
        float scale = Math.min((float) 120 / targetBitmap.getWidth(), (float) 120 / targetBitmap.getHeight());
        Bitmap fixedBmp = Bitmap.createScaledBitmap(targetBitmap, (int) (scale * targetBitmap.getWidth()), (int) (scale * targetBitmap.getHeight()), false);
        return fixedBmp;
    }

    private Bitmap getWxShareBitmap500(Bitmap targetBitmap) {
        float scale = Math.min((float) 500 / targetBitmap.getWidth(), (float) 500 / targetBitmap.getHeight());
        Bitmap fixedBmp = Bitmap.createScaledBitmap(targetBitmap, (int) (scale * targetBitmap.getWidth()), (int) (scale * targetBitmap.getHeight()), false);
        return fixedBmp;
    }
}
