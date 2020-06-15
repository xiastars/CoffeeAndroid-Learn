package com.summer.demo.ui.mine.release.audio;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.dialog.BaseTipsDialog;
import com.summer.demo.module.base.helper.audio.OnAudioPlayListener;
import com.summer.demo.ui.mine.release.view.CircleProgressImageView;
import com.summer.demo.utils.BaseUtils;
import com.summer.demo.utils.CUtils;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 音频录制
 */
public class AudioHelper implements View.OnClickListener {
    ViewHolder vh;
    Context context;
    MediaPlayer mMediaPlayer;
    String filePath;//录音保存地址
    public boolean isRecording;//是否在录音
    MediaRecorder mRecorder;
    int secondsRecorded;//录音时间
    Timer timer;

    boolean isOnAudioHelper;//是否在这个界面上
    CountDownTimer countDownTimer;
    MyHandler myHandler;
    OnAudioRecordListener onAudioRecordListener;
    PlayAudioHelper playAudioHelper;


    public void init(View view) {
        vh = new ViewHolder(view);
        context = view.getContext();
        vh.tvRecord.setText("点击录音，语音最长可录制120秒");
        vh.llRecordUnset.setVisibility(View.VISIBLE);
        vh.rlRecording.setVisibility(View.GONE);
        vh.rlRecoded.setVisibility(View.GONE);
        vh.ivRecord.setOnClickListener(this);
        vh.ivAudioPlay.setOnClickListener(this);
        vh.ivRecording.setOnClickListener(this);
        vh.tvCancel.setOnClickListener(this);
        vh.tvSend.setOnClickListener(this);
        mMediaPlayer = new MediaPlayer();
        myHandler = new MyHandler(this);
        playAudioHelper = new PlayAudioHelper(new OnAudioPlayListener() {
            @Override
            public void onStart() {
                myHandler.sendEmptyMessage(0);
            }

            @Override
            public void onComplete() {
                myHandler.sendEmptyMessageDelayed(1, 300);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_record:
                CUtils.onClick(context,"record_recording");
                changeToRecordingMode();
                break;
            case R.id.iv_recording:
                CUtils.onClick(context,"record_stop");
                endRecord();
                changeToRecoredMode();
                break;
            case R.id.iv_audio_play:

                if (playAudioHelper.checkEnable()) {
                    CUtils.onClick(context,"record_play");
                    vh.ivAudioPlay.setMaxCount(secondsRecorded * 1000);
                    playAudioHelper.playMediaFile(filePath);
                    SUtils.setPicResource(vh.ivAudioPlay, R.drawable.questions_voice_recording_icon);
                } else {
                    if(countDownTimer != null){
                        countDownTimer.cancel();
                        myHandler.removeMessages(2);
                    }
                    SUtils.setPicResource(vh.ivAudioPlay, R.drawable.questions_voice_record_stop_icon);
                    vh.ivAudioPlay.setProgress(0);
                    playAudioHelper.stopPlayingAudio();
                }

                break;
            case R.id.tv_cancel:
                isOnAudioHelper = false;
                BaseUtils.showEasyDialog(context, "选择取消，将会清空已录制的内容", new BaseTipsDialog.DialogAfterClickListener() {
                    @Override
                    public void onSure() {
                        CUtils.onClick(context,"record_back_sure");
                        if (onAudioRecordListener != null) {
                            onAudioRecordListener.onCancel();
                        }
                    }

                    @Override
                    public void onCancel() {
                        CUtils.onClick(context,"record_back_cancel");
                    }
                });

                break;
            case R.id.tv_send:
                isOnAudioHelper = false;
                CUtils.onClick(context,"record_send");
                if (onAudioRecordListener != null) {
                    onAudioRecordListener.onRecorded(filePath,secondsRecorded);
                }
                break;
        }
    }

    private void changeToRecordingMode() {
        toggleRecord();
        if (!isRecording) {
            if (PermissionUtils.checkRecordPermission(context)) {
                vh.llRecordUnset.setVisibility(View.GONE);
                vh.rlRecording.setVisibility(View.VISIBLE);
                filePath = SFileUtils.getAudioDirectory() + SUtils.getAudioNameByTime() + ".mp3";
                startRecoding();
            }
        }
    }

    private void toggleRecord() {

    }

    private void startRecoding() {
        isRecording = true;
        isOnAudioHelper = true;
        mRecorder = new MediaRecorder();
        mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        /* 设置输出格式 */
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        /* 设置编码格式 */
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        /* 设置输出文件 */
        mRecorder.setOutputFile(filePath);
        /* 设置声道，此处为单声道 */
        mRecorder.setAudioChannels(1);
        mRecorder.setAudioSamplingRate(8000);
        mRecorder.setAudioEncodingBitRate(8);
        try {
            /* 如果文件打开失败，此步将会出错 以上设置不可以在prepare之后 */
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            SUtils.makeToast(context,"录音功能已被占用");
            Logs.i("prepare() failed");
            isRecording = false;
        }
        Logs.i("开始录音");
        secondsRecorded = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                updateTimer();
            }
        }, 0, 1000);
    }

    private void updateTimer() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                secondsRecorded++;

                if (secondsRecorded < 60) {
                    vh.tvRecordTime.setText( secondsRecorded+"\"");
           /*         if (secondsRecorded < 10) {
                        vh.tvRecordTime.setText("00:0" + secondsRecorded);
                    } else {
                        vh.tvRecordTime.setText("00:" + secondsRecorded);
                    }*/
                } else if (secondsRecorded >= 60 && secondsRecorded <= 120) {
                    vh.tvRecordTime.setText( secondsRecorded+"\"");
          /*          int minite = secondsRecorded / 60;
                    int seconds = secondsRecorded % 60;
                    if (seconds < 10) {
                        vh.tvRecordTime.setText("0" + minite + ":0" + seconds);
                    } else {
                        vh.tvRecordTime.setText("0" + minite + ":" + seconds);
                    }*/

                } else {
                    endRecord();
                }
            }
        });
    }

    private void updateTime(int time) {
        vh.tvAutioTime.setText( time+"\"");
/*        Logs.i("time:" + time);
        if (time < 60) {
            if (time < 10) {
                vh.tvAutioTime.setText("00:0" + time);
            } else {
                vh.tvAutioTime.setText("00:" + time);
            }
        } else if (time >= 60 && time <= 120) {
            int minite = time / 60;
            int seconds = time % 60;
            if (time < seconds) {
                vh.tvAutioTime.setText("0" + minite + ":0" + time);
            } else {
                vh.tvAutioTime.setText("0" + minite + ":" + time);
            }

        }*/
    }

    private void endRecord() {
        changeToRecoredMode();
        stopRecoding();
        timer.cancel();
    }

    private void changeToRecoredMode() {
        vh.llRecordUnset.setVisibility(View.GONE);
        vh.rlRecording.setVisibility(View.GONE);
        vh.rlRecoded.setVisibility(View.VISIBLE);
        vh.tvAutioTime.setText(vh.tvRecordTime.getText().toString());
        SUtils.setPicResource(vh.ivAudioPlay, R.drawable.questions_voice_record_stop_icon);
        vh.ivAudioPlay.setProgress(0);
    }


    public void stopRecoding() {

        if (mRecorder != null && isRecording) {
            try{
                mRecorder.stop();
            }catch (Exception e){
                e.printStackTrace();
            }
            mRecorder = null;
        }
        if (timer != null) {
            timer.cancel();
        }
        isRecording = false;
    }

    public void resureRecording(){

        filePath = null;
        vh.llRecordUnset.setVisibility(View.VISIBLE);
        vh.rlRecording.setVisibility(View.GONE);
        vh.rlRecoded.setVisibility(View.GONE);
        vh.tvAutioTime.setText(vh.tvRecordTime.getText().toString());
        SUtils.setPicResource(vh.ivAudioPlay, R.drawable.questions_voice_record_stop_icon);
        vh.ivAudioPlay.setProgress(0);
        stopRecoding();
    }

    public static class MyHandler extends Handler {
        private final WeakReference<AudioHelper> mActivity;

        public MyHandler(AudioHelper activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AudioHelper activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 0:
                        activity.startPlaying();
                        break;
                    case 1:
                        activity.restPlayAudio();
                        break;
                    case 2:
                        long time = (long) msg.obj;
                        activity.onAudioPlaying(time);
                        break;
                }
            }
        }
    }

    private void restPlayAudio() {
        updateTime(secondsRecorded);
        changeToRecoredMode();
    }

    private void onAudioPlaying(long millisUntilFinished) {
        int time = (int) ((secondsRecorded * 1000 - millisUntilFinished) / 1000);
        updateTime(time);
        myHandler.sendEmptyMessage(3);
        Logs.i("mil" + millisUntilFinished);
        vh.ivAudioPlay.setProgress((int) (secondsRecorded * 1000 - millisUntilFinished));
    }

    private void startPlaying() {
        countDownTimer = new CountDownTimer(secondsRecorded * 1000, 1) {

            public void onTick(long millisUntilFinished) {
                myHandler.obtainMessage(2, millisUntilFinished).sendToTarget();
                Logs.i("mil" + millisUntilFinished);
            }

            public void onFinish() {
                countDownTimer.cancel();
                countDownTimer = null;
            }
        };
        countDownTimer.start();
    }

    public void setOnAudioRecordListener(OnAudioRecordListener onAudioRecordListener) {
        this.onAudioRecordListener = onAudioRecordListener;
    }

    public boolean isOnAudioHelper() {
        return isOnAudioHelper;
    }

    public void setOnAudioHelper(boolean onAudioHelper) {
        isOnAudioHelper = onAudioHelper;
    }

    static class ViewHolder {
        @BindView(R.id.iv_record)
        ImageView ivRecord;
        @BindView(R.id.ll_record_unset)
        LinearLayout llRecordUnset;
        @BindView(R.id.tv_record_time)
        TextView tvRecordTime;
        @BindView(R.id.iv_audio_play)
        CircleProgressImageView ivAudioPlay;
        @BindView(R.id.rl_recording)
        RelativeLayout rlRecording;
        @BindView(R.id.tv_autio_time)
        TextView tvAutioTime;
        @BindView(R.id.iv_recording)
        ImageView ivRecording;
        @BindView(R.id.tv_cancel)
        TextView tvCancel;
        @BindView(R.id.tv_send)
        TextView tvSend;
        @BindView(R.id.ll_audio_bottom)
        LinearLayout llAudioBottom;
        @BindView(R.id.rl_recoded)
        RelativeLayout rlRecoded;
        @BindView(R.id.tv_record)
        TextView tvRecord;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
