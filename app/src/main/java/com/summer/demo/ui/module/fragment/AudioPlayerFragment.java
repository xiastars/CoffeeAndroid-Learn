package com.summer.demo.ui.module.fragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.module.base.helper.audio.PlayAudioHelper;
import com.summer.demo.module.base.BaseFragment;

import butterknife.OnClick;

/**
 * 音频播放示例
 *
 * @author xiastars@vip.qq.com
 */
public class AudioPlayerFragment extends BaseFragment implements View.OnClickListener {
    String demoUrl = "http://cdn.ishuidi.com.cn/xsd/%E5%88%9B%E8%AF%BE%E8%B5%84%E6%BA%90/%E5%A3%B0%E9%9F%B3/%E8%8B%B1%E6%96%87%E5%84%BF%E6%AD%8C/%E3%80%8AHead,%20Shoulders,%20Knees%20and%20Toes%E3%80%8B/%E3%80%8AHead,%20Shoulders,%20Knees%20and%20Toes%E3%80%8B.mp3";
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
                playAudioHelper.playRaw(R.raw.bi);
                break;
            case R.id.btn_common://播放
                playAudioHelper.playMediaFile(demoUrl);
                //playMediaFile(demoUrl);
                break;
            case R.id.btn_longer://暂停
                playAudioHelper.pausePlayingAudio();
                break;
            case R.id.btn_special://继续播放
                playAudioHelper.restartPlayingAudio();
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
