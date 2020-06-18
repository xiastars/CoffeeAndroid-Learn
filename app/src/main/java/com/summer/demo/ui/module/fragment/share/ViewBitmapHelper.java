package com.summer.demo.ui.module.fragment.share;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

import com.ghnor.flora.callback.Callback;
import com.summer.demo.helper.ImgHelper;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewBitmapHelper {
    private Bitmap mBitmap;
    View view;
    Context context;
    ShareInfo shareInfo;
    OnReturnBitmapListener listener;

    public ViewBitmapHelper(View view) {
        this.view = view;
        context = view.getContext();

    }

    public static void close(Closeable... closeables) {
        if (closeables == null || closeables.length == 0)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 分享操作
     */
    public void share(OnReturnBitmapListener listener) {
        this.listener = listener;
        if (!PermissionUtils.checkWritePermission(context)) {
            return;
        }
        recycle();
        mBitmap = create(view);
        final String path = SFileUtils.getImageViewDirectory() + System.currentTimeMillis() + ".png";
        SUtils.getandSaveCurrentImage(context, mBitmap, path);
        if(shareInfo != null){
            shareInfo.setShareBitMap(mBitmap);
        }
        ImgHelper.compressImgOnly(context, path, new Callback<String>() {
            @Override
            public void callback(String s) {
                listener.onBitmap(s);
                /**
                SUtils.getPicBitmap(context, path, new SimpleTarget() {
                    @Override
                    public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
                         if (resource instanceof GlideBitmapDrawable) {
                            GlideBitmapDrawable gifDrawable = (GlideBitmapDrawable) resource;

                        }

                    }
                });
                 */
            }
        });
    }

    /**
     * 保存操作
     */
    public void save() {
        if (!PermissionUtils.checkWritePermission(context)) {
            return;
        }
        recycle();
        mBitmap = create(view);
        FileOutputStream os = null;
        try {
            String url = SFileUtils.SOURCE_PATH;
            String path = String.format("%s%s.jpg", url, System.currentTimeMillis());
            os = new FileOutputStream(path);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
            SUtils.notifyLocalAlbum(context, path);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(os);
            recycle();
        }
    }

    public static Bitmap create(View v) {
        try {
            int w = v.getWidth();
            int h = v.getHeight();
            if (w == 0 || h == 0) {
                return null;
            }
            Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            if (bmp.isRecycled()) {
                return null;
            }
            Canvas c = new Canvas(bmp);
            c.drawColor(Color.WHITE);
            v.layout(0, 0, w, h);
            v.draw(c);
            return bmp;
        } catch (Exception error) {
            error.printStackTrace();
            return null;
        }
    }

    private void recycle() {
        if (mBitmap != null && mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
    }

}
