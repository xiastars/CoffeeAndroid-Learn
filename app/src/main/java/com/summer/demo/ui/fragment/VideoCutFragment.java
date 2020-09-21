package com.summer.demo.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.module.album.bean.SelectAlumbType;
import com.summer.demo.module.album.bean.SelectOptions;
import com.summer.demo.module.album.listener.AlbumCallback;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.video.VideoEditActivity;
import com.summer.helper.utils.JumpTo;

import java.util.List;

/**
 * @Description: 视频裁剪
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/5/21 16:27
 */
public class VideoCutFragment extends BaseFragment {

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void initView(View view) {
        SelectOptions.Builder selectOptions = new SelectOptions.Builder();
        selectOptions.setCallback(new AlbumCallback() {
            @Override
            public void doSelected(List<ImageItem> images) {
                JumpTo.getInstance().commonJump(context, VideoEditActivity.class,images.get(0));
            }

        });
        selectOptions.setSlectAlbumType(SelectAlumbType.Video);


        AlbumActivity.show(context, selectOptions.build());
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_empty;
    }
}
