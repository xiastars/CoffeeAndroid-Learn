package com.summer.demo.module.video.framepicker;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapRotateTransform {


    public static Bitmap transform(Bitmap bitmap, float rotate) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        int frameWidth = bitmap.getWidth();
        int frameHeight = bitmap.getHeight();
        return Bitmap.createBitmap(bitmap, 0, 0, frameWidth, frameHeight, matrix, true);
    }


}