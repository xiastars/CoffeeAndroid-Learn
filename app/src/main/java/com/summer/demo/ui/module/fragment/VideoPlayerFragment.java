package com.summer.demo.ui.module.fragment;

import android.os.Environment;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.base.helper.audio.PlayAudioHelper;
import com.summer.demo.module.video.ViewVideoActivity;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;

import java.io.File;

import butterknife.OnClick;

/**
 * 音频播放示例
 *
 * @author xiastars@vip.qq.com
 */
public class VideoPlayerFragment extends BaseFragment implements View.OnClickListener {
   PlayAudioHelper playAudioHelper;

    @Override
    protected int setContentView() {
        return R.layout.fragment_mediaplayer;
    }

    @OnClick({R.id.btn_common_raw, R.id.btn_common, R.id.btn_longer, R.id.btn_special})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_common_raw:
                File paths = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES);
                Logs.i("paths:"+paths.getAbsolutePath());
                String path ="/storage/emulated/0/integralems/video/Burpee.mp4";
                JumpTo.getInstance().commonJump(context, ViewVideoActivity.class,path);
                break;
        }
    }

    @Override
    protected void initView(View view) {
        playAudioHelper = PlayAudioHelper.getInstance();
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    /**
     * 当前Activity关闭时，停止音乐播放
     */
    @Override
    public void onStop() {
        super.onStop();
        playAudioHelper.stopPlayingAudio();
    }


}
