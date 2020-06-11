package com.summer.demo.module.base.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.SUtils;

/**
 * Created by xiastars on 2017/11/4.
 */

public class HintEditTextView extends RelativeLayout {
    TextView tvLimit;
    EditText edtContent;

    OnSimpleClickListener listener;
    OnTextChangedListener onTextChangedListener;

    int curPosition;
    int maxLength;


    public HintEditTextView(Context context) {
        super(context);
        init();
    }

    public HintEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttri(context, attrs, 0);
    }

    public HintEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttri(context, attrs, defStyleAttr);
    }

    private void initAttri(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SummerHintEdit, defStyleAttr, 0);
        int textColor = a.getColor(
                R.styleable.SummerHintEdit_summer_text_color, Color.parseColor("#4A4A4A"));
        edtContent.setTextColor(textColor);
        maxLength = a.getInt(R.styleable.SummerHintEdit_summer_maxLength, 10000);
        STextUtils.setSpliceText(tvLimit, "(" + 0 + "", "/" + maxLength + ")");
        InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(maxLength)};
        edtContent.setFilters(filters);
        curPosition = a.getInt(R.styleable.SummerHintEdit_summer_position, 0);
        String hintContent = a.getString(R.styleable.SummerHintEdit_summer_hint);
        edtContent.setHint(hintContent);
        a.recycle();
    }

    public void setMaxLine() {

    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            edtContent.setEnabled(false);
            tvLimit.setVisibility(View.GONE);
        }
    }

    public void showKeyBoard() {
        SUtils.setSelection(edtContent);
        edtContent.postDelayed(new Runnable() {
            @Override
            public void run() {
                SUtils.showSoftInpuFromWindow(edtContent, edtContent.getContext());
            }
        }, 300);
    }

    public void fortbitEditMode() {
        tvLimit.setVisibility(View.GONE);
        edtContent.setEnabled(false);
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_hint_editview, this, true);
        tvLimit = (TextView) view.findViewById(R.id.tv_limit);
        edtContent = (EditText) view.findViewById(R.id.edt_hint_sss_content);

        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString();
                STextUtils.setSpliceText(tvLimit, "(" + content.length() + "", "/" + maxLength + ")");
                if (onTextChangedListener != null) {
                    onTextChangedListener.onChanged(content);
                }
            }
        });

    }

    /**
     * 是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        String content = edtContent.getText().toString().trim();
        return TextUtils.isEmpty(content);
    }

    public void setContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            edtContent.setText(content);
            SUtils.setSelection(edtContent);
            STextUtils.setSpliceText(tvLimit, "(" + content.length() + "", "/" + maxLength + ")");
        }
    }

    public void setContent(String content, String replaceContent) {
        if (!TextUtils.isEmpty(content)) {
            edtContent.setText(content);
        } else {
            edtContent.setText(replaceContent);
        }

    }

    public OnSimpleClickListener getListener() {
        return listener;
    }

    public void setListener(OnSimpleClickListener listener) {
        this.listener = listener;
    }


    public String getContent() {
        return edtContent.getText().toString().trim();
    }

    public void setTextHint(String textHint) {
        edtContent.setHint(textHint);
    }

    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    public interface OnTextChangedListener {
        void onChanged(String text);
    }
}
