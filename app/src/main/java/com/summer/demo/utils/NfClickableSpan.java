package com.summer.demo.utils;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class NfClickableSpan extends ClickableSpan {
    int DEFAULT_COLOR = Color.parseColor("#617AB5");

    public NfClickableSpan(int colorRes) {
        this.DEFAULT_COLOR = colorRes;
    }

    @Override
    public void onClick(View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(DEFAULT_COLOR);
        ds.setUnderlineText(false);
    }
}
