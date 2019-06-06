package com.summer.demo.fragment;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.summer.demo.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.io.File;

/**
 * 音频播放示例
 *
 * @author xiastars@vip.qq.com
 */
public class MediaPlayerFragment extends BaseFragment implements View.OnClickListener {
    MediaPlayer mMediaPlayer = null;
    //这个状态用来确认是否在播放状态,0表示MediaPlayer没有被占用，1表示正在占用,2表示暂停
    int mAudioState;
    String demoUrl = "http://cdn.ishuidi.com.cn/xsd/%E5%88%9B%E8%AF%BE%E8%B5%84%E6%BA%90/%E5%A3%B0%E9%9F%B3/%E8%8B%B1%E6%96%87%E5%84%BF%E6%AD%8C/%E3%80%8AHead,%20Shoulders,%20Knees%20and%20Toes%E3%80%8B/%E3%80%8AHead,%20Shoulders,%20Knees%20and%20Toes%E3%80%8B.mp3";
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mediaplayer, null);
        initView(view);
        mMediaPlayer = new MediaPlayer();
        return view;
    }

    private void initView(View view) {
        Button btnCommon = (Button) view.findViewById(R.id.btn_common);
        btnCommon.setOnClickListener(this);

        Button btnLonger = (Button) view.findViewById(R.id.btn_longer);
        btnLonger.setOnClickListener(this);

        Button btnSpecial = (Button) view.findViewById(R.id.btn_special);
        btnSpecial.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_common://播放
                playMediaFile(demoUrl);
                break;
            case R.id.btn_longer://暂停
                pausePlayingAudio();
                break;
            case R.id.btn_special://继续播放
                restartPlayingAudio();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();
        }
    }

    /**
     * 开始播放文件
     *
     * @param fileName
     */
    public void playMediaFile(String fileName) {
        //检查传进来的文件是否为空
        if (TextUtils.isEmpty(fileName)) {
            return;
        }
        //根据链接，查找本地数据库，如果缓存过，就取本地的文件，如果没有，则把文件下载下来
        String local = SUtils.downloadAudio(context, fileName, true);
        if (null != local) {
            fileName = local;
        }
        //存在播放过程中，切换音乐的情况，先停止先前的播放
        stopPlayingAudio();
        Logs.i("xia", "播放:" + fileName);
        try {
            if (null == mMediaPlayer) {
                mMediaPlayer = new MediaPlayer();
            }
            //reset ,setDataSource,setAudioSgtreamType,prepareAsync,这四个必填，顺序不可乱
            mMediaPlayer.reset();
            //将音频路径传入进去，可以是网络链接，可以是本地链接
            mMediaPlayer.setDataSource(fileName);
            //设置播放音频流类型，经测试，发现本地的文件用STREAM_MUSIC会卡顿一点时间，所以在这里区分开
            if (fileName.startsWith("http")) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            } else {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
            }
            mMediaPlayer.prepareAsync();
            //是否循环播放
            //mMediaPlayer.setLooping(true);
            //音乐加载回调，回调后，马上start,开始播放
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer paramMediaPlayer) {
                    Logs.i("xia", "开始播放");
                    mMediaPlayer.start();
                    mAudioState = 1;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayingAudio();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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


    /**
     * 停止播放
     */
    public void stopPlayingAudio() {
        if (null == mMediaPlayer)
            return;
        if (0 == mAudioState)
            return;
        try {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            Logs.i("xia", "销毁");
            mMediaPlayer = null;
            mAudioState = 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mMediaPlayer = null;
            mAudioState = 0;
        }
    }

    /**
     * 当前Activity关闭时，停止音乐播放
     */
    @Override
    public void onStop() {
        super.onStop();
        stopPlayingAudio();
    }

    /**
     * 获取内存卡主路径
     *
     * @return
     */
    private String getSDPath() {
        File sdDir = null;
        try {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
            if (sdCardExist) {
                sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            } else {
                File file = new File(Environment.getDataDirectory() + "/sdcard");
                if (file.canRead()) {
                    return file.toString();
                } else {
                    return "";
                }
            }
            if (sdDir != null) {
                return sdDir.toString();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return "";
    }

}
