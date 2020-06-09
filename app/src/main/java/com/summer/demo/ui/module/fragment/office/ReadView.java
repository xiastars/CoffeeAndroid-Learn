package com.summer.demo.ui.module.fragment.office;

import android.content.Context;
import android.view.MotionEvent;

import com.summer.helper.utils.Logs;
import com.tencent.smtt.sdk.TbsReaderView;

public class ReadView extends TbsReaderView {


    public ReadView(Context context, ReaderCallback readerCallback) {
        super(context, readerCallback);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
