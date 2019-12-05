package com.summer.demo.ui.view.customfragment.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.summer.demo.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiastars on 2017/9/8.
 */

public class CalendarView extends FrameLayout implements ViewGroup.OnHierarchyChangeListener {
    Context activity;
    Paint mPaint;
    Map<String, CheckCellInfo> cells = new HashMap<>();

    boolean ismOnDraw;
    ViewGroup.LayoutParams layoutParam;

    Point downPoint;
    int cellWith,cellHeight;
    int mMotionDownX,mMotionDownY;

    int curMonth;//当前月

    @SuppressLint("NewApi")
    public CalendarView(Context context) {
        super(context);
        this.activity = context;
        setMotionEventSplittingEnabled(false);
        setChildrenDrawingOrderEnabled(true);
        setOnHierarchyChangeListener(this);
        init();
    }

    public void setActivity(Context activity) {
        this.activity = activity;
    }

    @SuppressLint("NewApi")
    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMotionEventSplittingEnabled(false);
        setChildrenDrawingOrderEnabled(true);
        setOnHierarchyChangeListener(this);
        init();
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        layoutParam = getLayoutParams();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(getContext().getResources().getDimension(R.dimen.text_24));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (cells != null && !ismOnDraw) {
            ismOnDraw = true;
            drawCells(canvas);
        }
    }

    private void drawCells(Canvas canvas) {

        for (Map.Entry<String, CheckCellInfo> entry : cells.entrySet()) {
            final CheckCellInfo cellInfo = entry.getValue();
            cellInfo.onDraw(canvas, mPaint);
        }
        ismOnDraw = false;
    }

    /**
     * 创建所有格子
     */
    public void creatCells(int[] yearAndMonth,int curMonth) {
        this.curMonth = curMonth;
        int year = yearAndMonth[0];
        int month = yearAndMonth[1];
        Calendar calendar = null;
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(new Date(SUtils.getTime(year + "" + month)));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Logs.i("--------");
        int lastDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        cellWith = SUtils.screenWidth / 7;
        cellHeight = (int) (cellWith * 0.7f);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int weekend = calendar.get(Calendar.DAY_OF_WEEK);
        int shouldAdd = weekend - 1;//需要添加几个空白页面
        String[] days = {"日", "一", "二", "三", "四", "五", "六"};
        int index = 0;
        for (int y = 0; y < 7; y++) {
            for (int x = 0; x < 7; x++) {
                CheckCellInfo info = new CheckCellInfo(getContext());
                if (y == 0) {
                    info.setWeekend(true);
                    info.setContent(days[x] + "");
                    info.setValided(false);
                } else {
                    index++;
                    if (index > shouldAdd) {
                        info.setContent((index - shouldAdd) + "");
                    } else {
                        info.setContent("");
                    }
                    if (index - shouldAdd == today && curMonth == month) {//设置为今天
                        info.setToday(true);
                    }
                    if((curMonth == month && index -shouldAdd > today) || TextUtils.isEmpty(info.getContent())){
                        info.setValided(false);
                    }else{
                        info.setValided(true);
                    }
                }
                if (index > lastDay + shouldAdd) {
                    break;
                }
                Rect rect = new Rect();
                rect.left = cellWith * x;
                rect.right = rect.left + cellWith;
                rect.top = cellHeight * y;
                rect.bottom = rect.top + cellHeight;
                info.setCellRecct(rect);
                cells.put(x + "-" + y, info);
            }
        }
        invalidate();
        requestLayout();
    }


    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        Logs.i("xia", event.getAction() + ",,," + x + ",,," + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downPoint = new Point(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (0 == mMotionDownX && 0 == mMotionDownY) {
                    mMotionDownX = x;
                    mMotionDownY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                x = (int) event.getX();
                y = (int) event.getY();
                handlerClick(x, y);
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    private void handlerClick(int x, int y) {

        for(Map.Entry<String,CheckCellInfo> s : cells.entrySet()){
            CheckCellInfo info = s.getValue();
            Logs.i("info:"+info);
            if (info != null) {
                if(!info.isValided){
                    continue;
                }
                Logs.i("x,,"+x+",,"+y+",,"+info.getCellRecct());
                if(info.getCellRecct().contains(x,y)){
                    info.setChecked(!info.isChecked);
                    invalidate();
                    requestLayout();
                }

            }
        }

    }

    public CheckCellInfo getCellWithId(String id) {
        return cells.get(id);
    }

    @Override
    public void onChildViewAdded(View parent, View child) {

    }

    @Override
    public void onChildViewRemoved(View parent, View child) {

    }
}
