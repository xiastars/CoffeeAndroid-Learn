package com.summer.demo.helper;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import com.summer.demo.AppContext;
import com.summer.demo.listener.OnAudioPlayListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.lang.ref.WeakReference;

/**
 * 播放音频
 */
public class PlayAudioHelper {
    MediaPlayer mMediaPlayer;
    int mAudioState;
    OnAudioPlayListener onAudioPlayListener;
    static PlayAudioHelper playAudioHelper;
    //TopicVoiceInfo anwserAudio;//纯粹是为了控制列表里的播放状态
    SeekBar seekBar;
    TextView tvTime;
    MyHandler myHandler;
    //强制关闭状态；音频有可能在onPrepare回调前关闭，从而导致没有真正地关闭
    int FORCE_STOP_STATE = 0;

    public static int LOCAL_PLAY_POSITION = -1;//主题列表在播放状态音频的标记
    public static int CURRENT_PLAY_POS = 0;//当前播放位置
    public static int CURRENT_PLAY_TIME = 0;

    public static synchronized PlayAudioHelper getInstance() {
        if (playAudioHelper != null) {
            playAudioHelper.stopPlayingAudio();
        }
        playAudioHelper = new PlayAudioHelper();

        return playAudioHelper;
    }

    public static synchronized PlayAudioHelper getInstanceWithoutStop() {
        playAudioHelper = new PlayAudioHelper();
        return playAudioHelper;
    }

    public PlayAudioHelper() {
        myHandler = new MyHandler(this);
    }

    public PlayAudioHelper(OnAudioPlayListener onAudioPlayListener) {
        this.onAudioPlayListener = onAudioPlayListener;
        myHandler = new MyHandler(this);
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
        Logs.i("播放:" + fileName);
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
                 /*   if(anwserAudio != null){
                        //LOCAL_PLAY_POSITION = anwserAudio.getLocalPosition();
                    }*/
                    myHandler.sendEmptyMessage(0);
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
            // playMediaFile(fileName,listener);
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
                 /*   if(anwserAudio != null){
                        //LOCAL_PLAY_POSITION = anwserAudio.getLocalPosition();
                    }*/
                    myHandler.sendEmptyMessage(0);
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
            // playMediaFile(fileName,listener);
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



    public boolean checkEnable() {
        return mAudioState == 0;
    }

    public void stopPlayingAndClearStatus() {
        tvTime = null;
        seekBar = null;
        stopPlayingAudio();
    }

    public void stopPlayingAudio() {
        FORCE_STOP_STATE = 1;
        myHandler.removeMessages(0);
        if (mMediaPlayer != null && seekBar != null) {
            seekBar.setProgress(0);
        }
        Logs.i("停止播放");
        CURRENT_PLAY_TIME = 0;
        LOCAL_PLAY_POSITION = -1;
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
        seekBar = null;
        tvTime = null;
        playAudioHelper = null;
    }

    public void setOnAudioPlayListener(OnAudioPlayListener onAudioPlayListener) {
        this.onAudioPlayListener = onAudioPlayListener;
    }

    public void setSeekBarAndiTimeView(SeekBar seekBar, TextView tvTime) {
        this.seekBar = seekBar;
        this.tvTime = tvTime;
        if (seekBar == null) {
            return;
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mMediaPlayer == null) {
                    return;
                }
                int dest = seekBar.getProgress();
                int time = mMediaPlayer.getDuration();
                int max = seekBar.getMax();
                mMediaPlayer.seekTo(time * dest / max);
            }
        });
    }

    private void updateSeekBar() {
        if (mMediaPlayer == null || tvTime == null || mAudioState == 0) {
            myHandler.removeMessages(0);
            return;
        }
        int position = mMediaPlayer.getCurrentPosition();
        int time = mMediaPlayer.getDuration();
        CURRENT_PLAY_TIME = (time - position) / 1000;
        if (seekBar == null) {
            tvTime.setText(" " + CURRENT_PLAY_TIME + "\"");
            myHandler.sendEmptyMessageDelayed(0, 500);
            return;
        }

        int max = seekBar.getMax();
        Logs.i("position::" + position + ",," + time + ",,," + max);
        CURRENT_PLAY_TIME = (time - position) / 1000;
        if (time == 0) {
            return;
        }
        int progress = (int) ((float) max * position / time);
        CURRENT_PLAY_POS = progress;
        if (tvTime != null) {
           /* if(seekBar.getTag() != null){
                int pos = (int) seekBar.getTag();
                Logs.i("pos::::"+anwserAudio.getLocalPosition()+",,"+anwserAudio.isLocalPlaying() +",,anwserAudio"+seekBar+"<,,"+CURRENT_PLAY_POS);
                if(anwserAudio != null && pos == anwserAudio.getLocalPosition() && anwserAudio.isLocalPlaying()){
                    if(anwserAudio != null){
                        anwserAudio.getLocalSeekBar().setProgress(CURRENT_PLAY_POS);
                        anwserAudio.getTvTime().setText(CURRENT_PLAY_TIME+"\"");
                        anwserAudio.setLocalPlayingTime(CURRENT_PLAY_TIME);
                        anwserAudio.setLocalPlayingProgress(CURRENT_PLAY_POS);
                    }
                }else{
                    seekBar.setProgress(0);
                }
            }else{
                seekBar.setProgress(0);
            }*/
        }

        myHandler.sendEmptyMessageDelayed(0, 500);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<PlayAudioHelper> mActivity;

        public MyHandler(PlayAudioHelper activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            PlayAudioHelper activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 0:
                        activity.updateSeekBar();
                        break;
                }
            }
        }
    }

   /* public TopicVoiceInfo getAnwserAudio() {
        return anwserAudio;
    }

    public void setAnwserAudio(TopicVoiceInfo anwserAudio) {
        this.anwserAudio = anwserAudio;
    }*/
}
