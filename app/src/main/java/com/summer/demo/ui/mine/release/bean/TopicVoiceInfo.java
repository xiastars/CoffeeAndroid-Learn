package com.summer.demo.ui.mine.release.bean;

import android.widget.SeekBar;
import android.widget.TextView;

import java.io.Serializable;

public class TopicVoiceInfo implements Serializable {

    private String url;
    private int length;
    boolean localPlaying;//是否在播放
    int localPosition;
    int localPlayingTime;//当前播放时间
    int localPlayingProgress;
    transient SeekBar localSeekBar;
    transient TextView tvTime;

    public TextView getTvTime() {
        return tvTime;
    }

    public void setTvTime(TextView tvTime) {
        this.tvTime = tvTime;
    }

    public SeekBar getLocalSeekBar() {
        return localSeekBar;
    }

    public void setLocalSeekBar(SeekBar localSeekBar) {
        this.localSeekBar = localSeekBar;
    }

    public int getLocalPlayingProgress() {
        return localPlayingProgress;
    }

    public void setLocalPlayingProgress(int localPlayingProgress) {
        this.localPlayingProgress = localPlayingProgress;
    }

    public int getLocalPlayingTime() {
        if(localPlayingTime == 0){
            return length;
        }
        return localPlayingTime;
    }

    public void setLocalPlayingTime(int localPlayingTime) {
        this.localPlayingTime = localPlayingTime;
    }

    public int getLocalPosition() {
        return localPosition;
    }

    public void setLocalPosition(int localPosition) {
        this.localPosition = localPosition;
    }

    public boolean isLocalPlaying() {
        return localPlaying;
    }

    public void setLocalPlaying(boolean localPlaying) {
        this.localPlaying = localPlaying;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
