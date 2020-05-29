package com.summer.demo.ui.fragment;

import android.content.Intent;

import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.module.album.bean.SelectOptions;
import com.summer.demo.module.album.listener.AlbumCallback;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.video.VideoEditActivity;
import com.summer.helper.utils.JumpTo;

import java.util.List;

/**
 * @Description: 视频裁剪
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/5/21 16:27
 */
public class VideoCutFragment extends BaseSimpleFragment{
    @Override
    protected void initView() {
        super.initView();
        SelectOptions.Builder selectOptions = new SelectOptions.Builder();
        selectOptions.setCallback(new AlbumCallback() {
            @Override
            public void doSelected(List<ImageItem> images) {
                JumpTo.getInstance().commonJump(context, VideoEditActivity.class,images.get(0));
            }

        });
        selectOptions.setVideoMode(true);


        AlbumActivity.show(context, selectOptions.build());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
