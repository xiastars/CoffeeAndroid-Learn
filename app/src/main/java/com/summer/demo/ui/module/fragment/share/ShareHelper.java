package com.summer.demo.ui.module.fragment.share;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.summer.demo.R;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 社交分享帮助
 */
public class ShareHelper {
    Context context;
    //分享规则
    ShareRule shareRule;
    ShareInfo shareInfo;

    OnSimpleClickListener onSimpleClickListener;

    private String TAGS_PRE = "";

    //埋点TAG
    public static String SHARE_TAG_PRE = "";

    int shareType;

    public ShareHelper(Context context, ShareInfo shareInfo) {
        this.context = context;
        this.shareInfo = shareInfo;
        this.shareRule = new ShareRule();
    }

    public ShareHelper(Context context, ShareInfo shareInfo, ShareRule shareRule) {
        this.context = context;
        this.shareInfo = shareInfo;
        this.shareRule = shareRule;
    }

    public void showDialog() {
        Logs.i("----------");
        if(shareRule.isSupportShareSystem()){
            ShareRequest shareRequest = new ShareRequest(context, shareInfo);
            shareRequest.shareImageToSystem(shareInfo.getShareImg());
            return;
        }
        ShareDialog manageDIalog = new ShareDialog(context, getShareItem(), new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                handleClick(position);
            }

        });
        manageDIalog.show();
    }

    /**
     * 处理点击
     *
     * @param position
     */
    public void handleClick(int position) {
        shareInfo.setShareType(position);
        Logs.i("position:"+position);
        ShareRequest shareRequest = new ShareRequest(context, shareInfo);
        switch (position) {
            case ShareType.WECHAT:

                shareRequest.shareToWechat();
                break;
            case ShareType.LINK:
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null, shareInfo.getUrl());
                cm.setPrimaryClip(clipData);
                SUtils.makeToast(context, "复制成功");
                break;
            case ShareType.QQFRIENDS:
                shareRequest.shareToQQ();
                break;
            case ShareType.WECHAT_CIRCLE:
                shareRequest.shareToWechatCircle(shareInfo);
                break;
            case ShareType.WECHAT_BIG_PHOTO:
                Logs.i("---------");
                shareRequest.shareBigPhoto(true);
                break;
            case ShareType.WECHAT_APP:
                shareRequest.shareToWXapp();
                break;
            case ShareType.QQZONE:
                shareRequest.shareToQZone();
                break;
        }
    }

    private List<ShareItemInfo> getShareItem() {
        List<ShareItemInfo> items = new ArrayList<>();
        if (shareRule.isSupportShareWechat()) {
            if (shareRule.isSupportShareWechatMiniApp()) {
                items.add(createWXFriendsWithMiniApp());
            } else {
                items.add(createWXFriends());
            }
        }
        if (shareRule.isSupportShareWechatCircle()) {
            Logs.i("bigphoto:"+shareRule.isSupportShareWechatCircleWithBigPhoto());
            if (shareRule.isSupportShareWechatCircleWithBigPhoto()) {
                items.add(createWXCircleBig());
            } else {
                items.add(createWXCircle());
            }

        }
        if (shareRule.isSupportShareWeibo()) {
            items.add(createWeibo());
        }
        if (shareRule.isSupportShareQQ()) {
            items.add(createQQFriends());
        }
        if (shareRule.isSupportShareQQZone()) {
            items.add(createQQZone());
        }

        if (shareRule.isSupportShareLink()) {
            items.add(createLink());
        }

        return items;
    }

    /**
     * 创建微信好友
     *
     * @return
     */
    private ShareItemInfo createWXFriends() {
        ShareItemInfo info = new ShareItemInfo("微信好友", R.drawable.icon_fenxiang_weixing, ShareType.WECHAT);
        return info;
    }

    /**
     * 创建微信好友,分享到小程序
     *
     * @return
     */
    private ShareItemInfo createWXFriendsWithMiniApp() {
        ShareItemInfo info = new ShareItemInfo("微信好友", R.drawable.icon_fenxiang_pengyouquan, ShareType.WECHAT_APP);
        return info;
    }

    /**
     * 创建微信朋友圈
     *
     * @return
     */
    private ShareItemInfo createWXCircle() {
        ShareItemInfo info = new ShareItemInfo("朋友圈", R.drawable.icon_fenxiang_pengyouquan, ShareType.WECHAT_CIRCLE);
        return info;
    }

    /**
     * 创建微信朋友圈
     *
     * @return
     */
    private ShareItemInfo createWXCircleBig() {
        ShareItemInfo info = new ShareItemInfo("朋友圈", R.drawable.icon_fenxiang_pengyouquan, ShareType.WECHAT_BIG_PHOTO);
        return info;
    }

    /**
     * 创建微博
     *
     * @return
     */
    private ShareItemInfo createWeibo() {
        ShareItemInfo info = new ShareItemInfo("微博", R.drawable.icon_fenxiang_weibo, ShareType.WEIBO);
        return info;
    }

    /**
     * 创建QQ好友分享
     *
     * @return
     */
    private ShareItemInfo createQQFriends() {
        ShareItemInfo info = new ShareItemInfo("QQ好友", R.drawable.icon_fenxiang_qq, ShareType.QQFRIENDS);
        return info;
    }

    /**
     * 创建QQ空间分享
     *
     * @return
     */
    private ShareItemInfo createQQZone() {
        ShareItemInfo info = new ShareItemInfo("QQ空间", R.drawable.icon_fenxiang_qqkongjian, ShareType.QQZONE);
        return info;
    }

    /**
     * 创建链接分享
     *
     * @return
     */
    private ShareItemInfo createLink() {
        ShareItemInfo info = new ShareItemInfo("复制链接", R.drawable.icon_fenxiang_fuzhi, ShareType.LINK);
        return info;
    }


    public void setOnSimpleClickListener(OnSimpleClickListener onSimpleClickListener) {
        this.onSimpleClickListener = onSimpleClickListener;
    }

    public interface OnExitGroupListener {
        void exitSucceed();
    }

}
