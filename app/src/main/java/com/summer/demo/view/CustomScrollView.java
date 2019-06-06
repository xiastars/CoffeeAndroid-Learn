package com.summer.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.summer.demo.R;

public class CustomScrollView extends ScrollView{
    private LinearLayout llTop;
    private int mTopHeight;
    private int downX;
    private int downY;
    private int mTouchSlop;


    public CustomScrollView(Context context) {
        super(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CustomScrollView(Context context, AttributeSet attr) {
        super(context,attr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(null != llTop){
            if(mTopHeight > t || oldt < 0){
                llTop.setAlpha(1);
                llTop.setVisibility(View.VISIBLE);
            }else{
                if(t > mTopHeight){
                    float alpha = 1- (t - mTopHeight)/(float)mTopHeight;
                    if(alpha <= 0){
                        alpha = 0;
                        llTop.setVisibility(View.GONE);
                    }else{
                        llTop.setVisibility(View.VISIBLE);
                    }
                    llTop.setAlpha(alpha);
                }
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setTopView(LinearLayout llTop) {
        this.llTop = llTop;
        this.mTopHeight = (int)getContext().getResources().getDimension(R.dimen.size_100);
    }

}
