package com.summer.demo.module.video.framepicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.Map;

public interface IMediaMetadataRetriever {

    void setDataSource(File inputFile) throws FileNotFoundException;

    void setDataSource(String uri, Map<String, String> headers);

    void setDataSource(FileDescriptor fd, long offset, long length);

    void setDataSource(FileDescriptor fd);

    void setDataSource(Context context, Uri uri);

    Bitmap getFrameAtTime();

    Bitmap getFrameAtTime(long timeUs, int option);

    Bitmap getScaledFrameAtTime(long timeUs, int width, int height);

    Bitmap getScaledFrameAtTime(long timeUs, int option, int width, int height);

    byte[] getEmbeddedPicture();

    String extractMetadata(int keyCode);

    void release();
}