package com.summer.demo.module.video.util;

import android.net.Uri;

/**
 * Created by dell on 2017/6/28.
 */

public interface OnTrimVideoListener {
    void onTrimStarted();

    void getResult(final Uri uri);

    void cancelAction();

    void onError(final String message);
}
