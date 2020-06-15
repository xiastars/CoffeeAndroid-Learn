package com.summer.demo.module.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.summer.demo.R;

public class CommonSureView5 extends TextView {

    boolean needBackgroud = true;
    int enableColor;

    public CommonSureView5(Context context) {
        super(context);
        init(false);
    }

    private void init(boolean b) {
        changeStyle(b);
    }

    public CommonSureView5(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public CommonSureView5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    public void changeStyle(boolean enable) {
        if (enable) {
            this.setEnabled(true);
            if (needBackgroud) {
                this.setBackgroundResource(R.drawable.so_blue56_5);
            }
            this.setTextColor(getContext().getResources().getColor(enableColor != 0 ? enableColor : R.color.white));
        } else {
            this.setEnabled(false);
            if (needBackgroud) {
                this.setBackgroundResource(R.drawable.so_greyd2_5);
            }
            this.setTextColor(getContext().getResources().getColor(R.color.grey_95));
        }
        if (!needBackgroud) {
            this.setBackgroundResource(R.drawable.trans);
        }
    }

    public boolean isNeedBackgroud() {
        return needBackgroud;
    }

    public void setNeedBackgroud(boolean needBackgroud) {
        this.needBackgroud = needBackgroud;
    }

    public int getEnableColor() {
        return enableColor;
    }

    public void setEnableColor(int enableColor) {
        this.enableColor = enableColor;
    }
}
