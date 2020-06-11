package com.summer.demo.ui.mine.release.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.widget.CircularProgressDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.summer.demo.R;
import com.summer.helper.utils.SUtils;

public class CircleProgressImageView extends ImageView {
    private int mProgress;
    private int mPaddingProgress;
    Paint paint;
    int maxCount;

    CircularProgressDrawable mProgressDrawable;

    public CircleProgressImageView(Context context) {
        super(context);
    }

    public CircleProgressImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleProgressImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setColor(getContext().getResources().getColor(R.color.blue_56));
            paint.setStrokeWidth(SUtils.getDip(getContext(), 2));
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
        }
        //屏幕宽度
        int width = getMeasuredWidth();
        RectF rectF = new RectF();
        int padding = SUtils.getDip(getContext(), 1);
        rectF.left = padding;//左上角X
        rectF.top = padding;//左上角Y
        rectF.right = (width - SUtils.getDip(getContext(), 104)) / 2 + SUtils.getDip(getContext(), 104) - padding;//右上角X
        rectF.bottom = SUtils.getDip(getContext(), 104) * 1f - padding;//右上角Y
        if ((rectF.right - rectF.left) > (rectF.bottom - rectF.top)) {//正方形矩形,保证画出的圆不会变成椭圆
            float space = (rectF.right - rectF.left) - (rectF.bottom - rectF.top);
            rectF.left += space / 2;
            rectF.right -= space / 2;
        }
        canvas.drawArc(rectF, -90, (360f / maxCount) * mProgress, false, paint);
    }

    public void setProgress(int mProgress) {
        this.mProgress = mProgress;
        invalidate();
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
