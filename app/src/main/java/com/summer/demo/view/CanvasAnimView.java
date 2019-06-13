package com.summer.demo.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/13 15:50
 */
public class CanvasAnimView extends View {

    Bitmap showBitmap;
    Canvas showCanvas;

    int radius;

    int index;

    //圆心坐标
    private int centerX;
    private int centerY;

    Paint circlePaint;
    Paint showPaint;

    public CanvasAnimView(Context context) {
        super(context);
    }

    public CanvasAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        radius = Math.min(width, height) / 2;
        centerX = width / 2;
        centerY = height / 2;
        //秒针
        showBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        showCanvas = new Canvas(showBitmap);




    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(null == circlePaint){
            circlePaint = new Paint();
            circlePaint.setAntiAlias(true);
            circlePaint.setStyle(Paint.Style.FILL);
            circlePaint.setColor(Color.BLUE);
        }
        if(null == showPaint){
            showPaint = new Paint();
            showPaint.setAntiAlias(true);
            showPaint.setColor(Color.RED);
            showPaint.setStyle(Paint.Style.FILL);
            showPaint.setStrokeCap(Paint.Cap.ROUND);
            showPaint.setStrokeWidth(5);
        }
        showCanvas.save();
        //绘制圆
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        showCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        showCanvas.rotate(index * 30, centerX, centerY);
        showCanvas.drawLine(centerX, centerY,
                centerX, centerY - 100, showPaint);
        showCanvas.restore();

        canvas.drawBitmap(showBitmap, 0, 0, null);

    }

    public void setIndex(){
        index ++;
        if(index >= 60){
            index = 0;
        }
        invalidate();
    }
}
