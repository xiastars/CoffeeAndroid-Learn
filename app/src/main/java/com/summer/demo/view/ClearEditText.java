package com.summer.demo.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

import com.summer.demo.R;


/**
 * 可以一键清除文本
 */
public class ClearEditText extends DrawableEditText implements View.OnFocusChangeListener, TextWatcher {

    public ClearEditText(Context context) {
        super(context);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init() {
        super.init();
        Drawable clear = getResources().getDrawable(R.drawable.remarks_close_icon);
        clear.setBounds(0, 0, clear.getIntrinsicWidth(), clear.getIntrinsicHeight());
        setRightDrawable(clear);
        setCompoundDrawablePadding((int) getContext().getResources().getDimension(R.dimen.dp_12));
        this.addTextChangedListener(this);
        this.setOnFocusChangeListener(this);
    }

    @Override
    protected void clickRight() {
        super.clickRight();
        setText("");
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setRightDrawableVisible(getText().length() > 0);
        } else {
            setRightDrawableVisible(false);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setRightDrawableVisible(s.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
