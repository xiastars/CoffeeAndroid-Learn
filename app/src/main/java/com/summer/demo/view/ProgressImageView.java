package com.summer.demo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.summer.helper.utils.BitmapUtils;
import com.summer.helper.utils.Logs;

/**
 * @Description: 图片进度条
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/22 17:58
 */
@SuppressLint("AppCompatCustomView")
public class ProgressImageView extends ImageView {
    Bitmap preBitmap;
    Bitmap mutedBitmap;

    public ProgressImageView(Context context) {
        super(context);
    }

    public ProgressImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        Logs.i(":prebit"+preBitmap);
        if(preBitmap == null || preBitmap.isRecycled()){
            if(preBitmap != null){
                Logs.i("是同一个吗："+preBitmap.isRecycled());
            }

            if(drawable != null){
                if(drawable instanceof BitmapDrawable){
                    this.preBitmap = ((BitmapDrawable)drawable).getBitmap();
                }else if(drawable instanceof GlideBitmapDrawable){
                    this.preBitmap = ((GlideBitmapDrawable)drawable).getBitmap();
                }
                muteBitmap();
            }
        }
        //BitmapUtils.getInstance().addBitmap(preBitmap,getContext());
    }

    public void muteBitmap(){
        if(preBitmap == null || preBitmap.isRecycled()){
            return;
        }
        // 得到图片的长和宽
        int width = preBitmap.getWidth();
        int height = preBitmap.getHeight();
        // 创建目标灰度图像
        mutedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 创建画布
        Canvas c = new Canvas(mutedBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(preBitmap, 0, 0, paint);
        this.setImageBitmap(mutedBitmap);
        //BitmapUtils.getInstance().addBitmap(mutedBitmap,getContext());
    }

    public void setProgress(int progress) {
        if(mutedBitmap == null || mutedBitmap.isRecycled()){
            return;
        }
        if(preBitmap == null || preBitmap.isRecycled()){
            return;
        }

        Logs.i("------------------");
        int bitmapHeight = preBitmap.getHeight();
        int bitmapWidth = preBitmap.getWidth();

        Bitmap lastBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.RGB_565);
        // 创建画布
        Canvas c = new Canvas(lastBitmap);
        Paint paint = new Paint();
        int divider = bitmapHeight / 100;
        Bitmap topBitmap = Bitmap.createBitmap(mutedBitmap, 0, 0, mutedBitmap.getWidth(), bitmapHeight - divider * progress);
        Bitmap bottomBitmap = Bitmap.createBitmap(preBitmap, 0, bitmapHeight - divider * progress, preBitmap.getWidth(),  divider * progress);

        c.drawBitmap(topBitmap, 0, 0, paint);
        c.drawBitmap(bottomBitmap, 0, bitmapHeight - divider * progress, paint);
        BitmapUtils.getInstance().addBitmap(lastBitmap,getContext());
        setImageBitmap(lastBitmap);
        requestLayout();
        invalidate();

    }

}
