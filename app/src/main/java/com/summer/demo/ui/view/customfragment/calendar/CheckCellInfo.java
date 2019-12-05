package com.summer.demo.ui.view.customfragment.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import com.summer.demo.R;
import com.summer.helper.utils.SUtils;

/**
 * Created by xiastars on 2017/9/8.
 */

public class CheckCellInfo {
    String content;//显示内容
    boolean isValided;//是否可以点击
    boolean isToday;//是否是今天
    boolean isWeekend;//是否星期头部
    String originDay;
    Context context;
    boolean isChecked;//是否选中

    Rect cellRecct;
    Canvas canvas;
    Paint paint;

    int paddingLeft;
    int paddingTop;
    int ovalWidth;

    public CheckCellInfo(Context context) {
        this.context = context;
        ovalWidth = SUtils.getDip(context, 24);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isValided() {
        return isValided;
    }

    public void setValided(boolean valided) {
        isValided = valided;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public String getOriginDay() {
        return originDay;
    }

    public void setOriginDay(String originDay) {
        this.originDay = originDay;
    }

    public Rect getCellRecct() {
        return cellRecct;
    }

    public void setCellRecct(Rect cellRecct) {
        this.cellRecct = cellRecct;
        paddingLeft = (cellRecct.right - cellRecct.left - ovalWidth) / 2;
        paddingTop = (cellRecct.bottom - cellRecct.top - ovalWidth) / 2;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onDraw(Canvas canvas, Paint paint) {
        this.canvas = canvas;
        this.paint = paint;
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (cellRecct.bottom + cellRecct.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()

        if (isChecked) {
            try {
                paint.setColor(context.getResources().getColor(R.color.red_d4));
                canvas.drawOval(cellRecct.left + paddingLeft, cellRecct.top + paddingTop, cellRecct.right - paddingLeft, cellRecct.bottom - paddingTop, paint);
            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            }
            paint.setColor(Color.WHITE);
        } else {

            if (isValided) {
                paint.setColor(Color.BLACK);
            } else {
                paint.setColor(context.getResources().getColor(R.color.grey_99));
            }
        }
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(content, cellRecct.centerX(), baseline, paint);
        if (isToday) {
            paint.setColor(context.getResources().getColor(R.color.red_d4));
            float left = cellRecct.left + paddingLeft + ovalWidth / 2 - SUtils.getDip(context, 1.5f);
            float top = cellRecct.top + paddingTop + ovalWidth;
            canvas.drawOval(left, top, left + SUtils.getDip(context, 3f), top + SUtils.getDip(context, 3), paint);
        }
        canvas.save();
    }

}
