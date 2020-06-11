package com.summer.demo.ui.mine.release.audio;

public interface OnAudioRecordListener {
    void onRecorded(String path, int length);
    void onCancel();
}
