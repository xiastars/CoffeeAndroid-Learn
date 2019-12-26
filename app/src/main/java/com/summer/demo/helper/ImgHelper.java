package com.summer.demo.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;

import com.ghnor.flora.Flora;
import com.ghnor.flora.callback.Callback;
import com.summer.demo.AppContext;
import com.summer.demo.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;

public class ImgHelper {

    public static int WATER_POS = 0;//1是左对齐，2是右对齐，3是居中

    /**
     * 压缩图片并且加水印
     *
     * @param context
     * @param imagePath
     * @param callback
     */
    public static void compressImg(final Context context, boolean needWatermark,String imagePath, final Callback<String> callback) {
        if (imagePath.endsWith(".gif")) {
            callback.callback(imagePath);
        } else {
            //水印处理
            if (WATER_POS > 0 && needWatermark) {
                Bitmap lastBitmap = SUtils.createScaleBitmap(imagePath);

                Matrix matrix = new Matrix();
                int degree = getExifOrientation(imagePath);
                if (degree != 0) {
                    matrix.postRotate(degree);
                }
                lastBitmap = Bitmap.createBitmap(lastBitmap, 0, 0, lastBitmap.getWidth(), lastBitmap.getHeight(), matrix, true);
                lastBitmap = createBitmap(lastBitmap, "常量：用户名称");
                imagePath = SUtils.getAndSaveCurrentImage(context, lastBitmap);
            }
            //压缩处理
            Flora.with()
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    // 安全内存，设置为2，表示此次压缩任务需要的内存小于1/2可用内存才进行压缩任务
                    // 压缩完成的图片在磁盘的存储目录
                    .load(imagePath)

                    .compress(new Callback<String>() {
                        @Override
                        public void callback(String s) {
                            Logs.i("fileSize::::" + new File(s).length() / 1024);
                            callback.callback(s);
                        }
                    });
        }

    }

    // 给图片添加水印
    public static Bitmap createBitmap(Bitmap src, String groupName) {
        groupName = "@" + groupName;
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap bmpTemp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpTemp);
        Paint p = new Paint();
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.BOLD);
        p.setColor(Color.WHITE);
        p.setTypeface(font);


        canvas.drawBitmap(src, 0, 0, p);
        SoftReference<Bitmap> softBitmap = new SoftReference<>(SUtils.decodeBackgoundBitmapFromResource(AppContext.getInstance().getResources(), R.drawable.ic_logo, w / 5, h / 5));
        Bitmap b = softBitmap.get();
        b = Bitmap.createScaledBitmap(b, w / 18, w / 18, false);
        int textSize = w / 12 / 2;
        p.setTextSize(textSize);
        Logs.i("dd" + (w / 7 / 2));
        int text_width = (int) p.measureText(groupName);// 得到总体长度
        int left = 0, top = 0;
        int imgWidth = b.getWidth();
        int textLeft = 0, textTop = 0;
        top = h - b.getHeight() - 20;
        int textHeight = b.getHeight() / 4;

        Logs.i("b:::" + b.getHeight() + ",,,top:" + textHeight + ",,,," + top);
        if (WATER_POS == 1) {
            left = w - imgWidth - text_width - 20;
        } else if (WATER_POS == 2) {
            left = (w - (imgWidth + text_width)) / 2;
        } else {
            left = (w - (imgWidth + text_width)) / 2;
            top = (h - imgWidth) / 2;
        }
        textTop = top + imgWidth / 2 + textSize / 4;
        textLeft = left + imgWidth + 5;
        canvas.drawBitmap(b, left, top, p);
        p.setShadowLayer(2, 1, 3, AppContext.getInstance().getResources().getColor(R.color.grey_93));
        canvas.drawText(groupName, textLeft, textTop, p);
        canvas.save();
        canvas.restore();
        return bmpTemp;
    }

    private static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            if (orientation != -1) {
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                    default:
                        break;
                }
            }
        }
        Logs.i("degress:" + degree);

        return degree;

    }

    /**
     * 压缩图片并且加水印
     *
     * @param context
     * @param imagePath
     * @param callback
     */
    public static void compressImgOnly(final Context context, String imagePath, final Callback<String> callback) {
        if (imagePath.endsWith(".gif")) {
            callback.callback(imagePath);
        } else {
            Flora.with()
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    // 安全内存，设置为2，表示此次压缩任务需要的内存小于1/2可用内存才进行压缩任务
                    // 压缩完成的图片在磁盘的存储目录
                    .load(imagePath)

                    .compress(new Callback<String>() {
                        @Override
                        public void callback(String s) {
                            Logs.i("fileSize::::" + new File(s).length() / 1024);
                            callback.callback(s);
                        }
                    });
        }

    }
}
