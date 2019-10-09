package com.summer.demo.ui.module;

import android.app.Activity;
import android.content.Intent;

import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.fragment.MyDialogFragment;
import com.summer.demo.ui.fragment.ObjectAnimFragment;
import com.summer.demo.ui.module.fragment.FrameAnimFragment;
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
        switch (type){
            case 0:
                setTitle("帧动画");
                showFragment(new FrameAnimFragment());
                break;
            case 1:
                setTitle("属性动画");
                showFragment(new ObjectAnimFragment());
                break;
            case 2:
                setTitle("弹窗");
                showFragment(new MyDialogFragment());
                break;
            case 3:
                Intent intent = new Intent(context, AlbumActivity.class);
                intent.putExtra(JumpTo.TYPE_INT, 1);
                intent.putExtra(JumpTo.TYPE_STRING, SFileUtils.FileType.FILE_MP4);
                ((Activity) context).startActivityForResult(intent, 12);
                break;
        }
    }

}
