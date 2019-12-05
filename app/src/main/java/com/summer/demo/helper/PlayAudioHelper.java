package com.summer.demo.helper;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.TextUtils;

import com.summer.demo.AppContext;
import com.summer.demo.listener.OnAudioPlayListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

/**
 * 播放音频
 */
public class PlayAudioHelper {
    MediaPlayer mMediaPlayer;
    int mAudioState;
    OnAudioPlayListener onAudioPlayListener;
    static PlayAudioHelper playAudioHelper;
    //强制关闭状态；音频有可能在onPrepare回调前关闭，从而导致没有真正地关闭
    int FORCE_STOP_STATE = 0;

    public static synchronized PlayAudioHelper getInstance() {
        if (playAudioHelper != null) {
            playAudioHelper.stopPlayingAudio();
        }
        playAudioHelper = new PlayAudioHelper();

        return playAudioHelper;
    }

    public PlayAudioHelper() {
    }

    public PlayAudioHelper(OnAudioPlayListener onAudioPlayListener) {
        this.onAudioPlayListener = onAudioPlayListener;
    }

    /**
     * 开始播放文件
     *
     * @param fileName
     */
    public void playMediaFile(String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        //根据链接，查找本地数据库，如果缓存过，就取本地的文件，如果没有，则把文件下载下来
        String local = SUtils.downloadAudio(AppContext.getInstance(), fileName, true);
        if (null != local) {
            fileName = local;
        }
        stopPlayingAudio();
        FORCE_STOP_STATE = 0;
        try {
            if (null == mMediaPlayer) {
                mMediaPlayer = new MediaPlayer();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(fileName);
            //设置播放音频流类型，经测试，发现本地的文件用STREAM_MUSIC会卡顿一点时间，所以在这里区分开
           /* if (fileName.startsWith("http")) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            } else {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
            }*/
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer paramMediaPlayer) {
                    Logs.i("开始播放");
                    if (FORCE_STOP_STATE == 1) {
                        stopPlayingAudio();
                        return;
                    }
                    mMediaPlayer.start();
                    mAudioState = 1;
                    if (onAudioPlayListener != null) {
                        onAudioPlayListener.onStart();
                    }

                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (onAudioPlayListener == null) {
                        return;
                    }
                    onAudioPlayListener.onComplete();
                    stopPlayingAudio();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logs.i("播放失败");
        }
    }

    public void playRaw(int rawid) {
        if (rawid == 0) {
            return;
        }
        stopPlayingAudio();
        FORCE_STOP_STATE = 0;
        try {
            if (null == mMediaPlayer) {
                mMediaPlayer = new MediaPlayer();
            }
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(AppContext.getInstance(), Uri.parse("android.resource://" + AppContext.getInstance().getPackageName() + "/" + rawid));
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer paramMediaPlayer) {
                    Logs.i("开始播放");
                    if (FORCE_STOP_STATE == 1) {
                        stopPlayingAudio();
                        return;
                    }
                    mMediaPlayer.start();
                    mAudioState = 1;
                    if (onAudioPlayListener != null) {
                        onAudioPlayListener.onStart();
                    }

                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (onAudioPlayListener == null) {
                        return;
                    }
                    onAudioPlayListener.onComplete();
                    stopPlayingAudio();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logs.i("播放失败");
        }
    }


    /**
     * 暂停播放音频
     */
    public void pausePlayingAudio() {
        if (null == mMediaPlayer)
            return;
        if (0 == mAudioState)
            return;
        mMediaPlayer.pause();
        mAudioState = 2;
    }

    /**
     * 继续播放音频
     */
    public void restartPlayingAudio() {
        if (null == mMediaPlayer)
            return;
        if (0 == mAudioState)
            return;
        mMediaPlayer.start();
        mAudioState = 1;
    }

    /**
     * 停止播放，activity onStop时需调用
     */
    public void stopPlayingAudio() {
        FORCE_STOP_STATE = 1;
        Logs.i("停止播放");
        if (null == mMediaPlayer)
            return;
        if (0 == mAudioState)
            return;
        try {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
            mAudioState = 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMediaPlayer = null;
            mAudioState = 0;
        }
        if (onAudioPlayListener != null) {
            onAudioPlayListener.onComplete();
        }
        playAudioHelper = null;
    }

    public void setOnAudioPlayListener(OnAudioPlayListener onAudioPlayListener) {
        this.onAudioPlayListener = onAudioPlayListener;
    }
}
