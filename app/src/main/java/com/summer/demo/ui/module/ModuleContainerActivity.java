package com.summer.demo.ui.module;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.SDKActivity;
import com.summer.demo.ui.fragment.ObjectAnimFragment;
import com.summer.demo.ui.fragment.VideoCutFragment;
import com.summer.demo.ui.module.comment.ChatFragment;
import com.summer.demo.ui.module.fragment.AudioPlayerFragment;
import com.summer.demo.ui.module.fragment.CompressImgFragment;
import com.summer.demo.ui.module.fragment.EmojiFragment;
import com.summer.demo.ui.module.fragment.FrameAnimFragment;
import com.summer.demo.ui.module.fragment.ListenerAlbumListener;
import com.summer.demo.ui.module.fragment.OfficePreviewFragment;
import com.summer.demo.ui.module.fragment.PermissionFragment;
import com.summer.demo.ui.module.fragment.RXJavaFragment;
import com.summer.demo.ui.module.fragment.UploadFileFragment;
import com.summer.demo.ui.module.fragment.VibratorFragment;
import com.summer.demo.ui.module.fragment.VideoPlayerFragment;
import com.summer.demo.ui.module.fragment.WebLeanFragment;
import com.summer.demo.ui.module.fragment.dialog.MyDialogFragment;
import com.summer.demo.ui.module.fragment.login.LoginActivity;
import com.summer.demo.ui.module.fragment.nfc.NFCActivity;
import com.summer.demo.ui.module.fragment.share.ShareFragment;
import com.summer.demo.ui.module.fragment.socket.SocketFragment;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;

/**
 * @Description: Fragment容器
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 11:44
 */
public class ModuleContainerActivity extends FragmentContainerActivity {

    @Override
    protected void showViews(int type) {
        switch (type) {
            case ModulePos.POS_SDK:

                Logs.i("0-----00000000000");
                JumpTo.getInstance().commonJump(context, SDKActivity.class);
                finish();
                break;
            case ModulePos.POS_FRAME:
                setTitle("帧动画");
                showFragment(new FrameAnimFragment());
                break;
            case ModulePos.POS_ANIM:
                setTitle("属性动画");
                showFragment(new ObjectAnimFragment());
                break;
            case ModulePos.POS_DIALOG:
                setTitle("弹窗");
                showFragment(new MyDialogFragment());
                break;
            case ModulePos.POS_VIDEO_CUTTER:
                setTitle("视频裁剪");
                showFragment(new VideoCutFragment());

                break;
            case ModulePos.POS_WEBVIEW:
                setTitle("Webview网页");
                showFragment(new WebLeanFragment());
                break;
            case ModulePos.POS_CHAT:
                setTitle("周杰伦");
                showFragment(new ChatFragment());
                break;
            case ModulePos.POS_EMOJI:
                setTitle("自定义表情");
                showFragment(new EmojiFragment());
                break;
            case ModulePos.POS_COMPRESS_IMG:
                setTitle("图片压缩");
                showFragment(new CompressImgFragment());
                break;
            case ModulePos.POS_AUDIO_PLAY:
                setTitle("音频播放");
                showFragment(new AudioPlayerFragment());
                break;
            case ModulePos.POS_SOCKET:
                setTitle("原生Socket");
                showFragment(new SocketFragment());
                break;
            case ModulePos.POS_VIBRATE:
                setTitle("振动");
                showFragment(new VibratorFragment());
                break;
            case ModulePos.POS_ALBUM_LISTENER:
                setTitle("相册监听");
                showFragment(new ListenerAlbumListener());
                break;
            case ModulePos.POS_UPLOAD:
                setTitle("上传文件");
                showFragment(new UploadFileFragment());
                break;
            case ModulePos.POS_PERMISSION:
                setTitle("权限管理");
                showFragment(new PermissionFragment());
                break;
            case ModulePos.POS_RXJAVA:
                setTitle("RXJava");
                showFragment(new RXJavaFragment());
                break;
            case ModulePos.POS_OFFICE:
                setTitle("文档预览");
                showFragment(new OfficePreviewFragment());
                break;
            case ModulePos.POS_NFC:
                setTitle("NFC");
                JumpTo.getInstance().commonJump(context, NFCActivity.class);
                break;
            case ModulePos.POS_SHARE:
                setTitle("社交分享");
                showFragment(new ShareFragment());
                break;
            case ModulePos.POS_AUTH_LOGIN:
                setTitle("社交登录");
                JumpTo.getInstance().commonJump(context, LoginActivity.class);
                break;
                case ModulePos.POS_VIDEO_PLAY:
                    showFragment(new VideoPlayerFragment());
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logs.i("reqest:" + requestCode);
        if (mFragment != null) {
            mFragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
