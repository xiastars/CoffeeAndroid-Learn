package com.summer.demo.ui.module;

import android.app.Activity;
import android.content.Intent;

import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.fragment.MyDialogFragment;
import com.summer.demo.ui.fragment.ObjectAnimFragment;
import com.summer.demo.ui.module.comment.ChatFragment;
import com.summer.demo.ui.module.fragment.AudioPlayerFragment;
import com.summer.demo.ui.module.fragment.CompressImgFragment;
import com.summer.demo.ui.module.fragment.EmojiFragment;
import com.summer.demo.ui.module.fragment.FrameAnimFragment;
import com.summer.demo.ui.module.fragment.ListenerAlbumListener;
import com.summer.demo.ui.module.fragment.VibratorFragment;
import com.summer.demo.ui.module.fragment.WebLeanFragment;
import com.summer.demo.ui.module.fragment.socket.SocketFragment;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.SFileUtils;

/**
 * @Description: Fragment容器
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 11:44
 */
public class ModuleContainerActivity extends FragmentContainerActivity {

    @Override
    protected void showViews(int type) {
        switch (type) {
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
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(JumpTo.TYPE_INT, 1);
                intent.putExtra(JumpTo.TYPE_STRING, SFileUtils.FileType.FILE_MP4);
                ((Activity) context).startActivityForResult(intent, 12);
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
                showFragment(new EmojiFragment());
                break;
            case ModulePos.POS_COMPRESS_IMG:
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
        }
    }

}
