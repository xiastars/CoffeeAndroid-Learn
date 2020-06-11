package com.summer.demo.ui.mine.release.view;

import android.content.Context;
import android.util.AttributeSet;

import com.summer.demo.module.video.util.VideoCacheManager;

import cn.jzvd.JzvdStd;

public class NFVideoPlayer extends JzvdStd {

    private OnPlayListener mOnPlayListener;
    protected String mVideoUrl;
    private VideoCacheManager mVideoCacheManager;
    private long mCurrentCacheSize;
    private long mPreTimestamp;

    public NFVideoPlayer(Context context) {
        this(context, null);
    }

    public NFVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        topContainer.setVisibility(GONE);
        mVideoCacheManager = VideoCacheManager.getManager(context);
    }

    public void setup(String url) {
        setup("", url);
    }

    public void setup(String title, String url) {
        mVideoUrl = url;//这个才是真实的url
        if(mVideoUrl != null){
            setUp(mVideoUrl, title, JzvdStd.SCREEN_WINDOW_NORMAL);
        }
    }

    public void resume() {
        goOnPlayOnResume();
    }

    public void pause() {
        goOnPlayOnPause();
    }


    @Override
    public void release() {
        super.release();
        releaseAllVideos();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
        if (mOnPlayListener != null) {
            mOnPlayListener.onPlayPrepareing();
        }
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if (mOnPlayListener != null) {
            mOnPlayListener.onPlayResume();
        }
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        if (mOnPlayListener != null) {
            mOnPlayListener.onPlayPause();
        }
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        if (mOnPlayListener != null) {
            mOnPlayListener.onPlayComplete();
        }
    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        topContainer.setVisibility(VISIBLE);
    }

    public OnPlayListener getOnSimplePlayListener() {
        return mOnPlayListener;
    }

    public void setOnPlayListener(OnPlayListener listener) {
        this.mOnPlayListener = listener;
    }

    public interface OnPlayListener {
        void onPlayPrepareing();

        void onPlayResume();

        void onPlayPause();

        void onPlayComplete();
    }
}
