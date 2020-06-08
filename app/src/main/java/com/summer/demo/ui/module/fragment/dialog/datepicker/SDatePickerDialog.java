package com.summer.demo.ui.module.fragment.dialog.datepicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.dialog.BaseCenterDialog;
import com.summer.helper.listener.OnReturnStringContentListener;
import com.summer.helper.utils.STimeUtils;
import com.summer.helper.utils.SUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Description: 时间选择器
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/8 16:48
 */
public class SDatePickerDialog extends BaseCenterDialog {
    DatePicker datePicker;
    OnReturnStringContentListener listener;

    String defaultDate = "1990-01-01";

    public SDatePickerDialog(@NonNull Context context, OnReturnStringContentListener listener) {
        super(context,R.style.AiTheme);
        this.listener = listener;
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_datepicker;
    }

    @Override
    public void initView(View view) {
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        setDefaultDate();
        TextView tvTime = view.findViewById(R.id.tv_time);
        datePicker.receiveListener(() -> {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, datePicker.getYear());
            c.set(Calendar.MONTH, datePicker.getMonth());
            c.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
            String time = SUtils.getDays(c.getTime());
            tvTime.setText(time);
        });
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog();
            }
        });


        tvTime.setText(SUtils.getDays(new Date()));
        TextView tvFinished = (TextView) view.findViewById(R.id.tv_sure);
        tvFinished.setOnClickListener(arg0 -> {
            /*
             * 判断，当用户设置年月日大于当天时
             */
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, datePicker.getYear());
            c.set(Calendar.MONTH, datePicker.getMonth());
            c.set(Calendar.DAY_OF_MONTH, datePicker.getDay());
            if (System.currentTimeMillis() - c.getTimeInMillis() < 0) {
                SUtils.makeToast(context, "你选择的时间超出了限制！");
                return;
            } else {
                String userBirthday = STimeUtils.getDayWithFormat("yyyy-MM-dd", c.getTime());
                listener.returnContent(userBirthday);
            }
            cancelDialog();
        });
    }

    /**
     * 显示默认的时间点
     */
    private void setDefaultDate() {
        if (TextUtils.isEmpty(defaultDate)) {
            return;
        }
        String[] days = defaultDate.split("-");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, Integer.parseInt(days[0]));
        c.set(Calendar.MONTH, Integer.parseInt(days[1]) - 1);
        c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(days[2]));
        String time = SUtils.getDays(c.getTime());
        datePicker.setCalendar(c);
    }

    /**
     * 设置默认的时间点
     *
     * @param defaultDate
     */
    public void setDefaultDate(String defaultDate) {
        this.defaultDate = defaultDate;
    }
}
