package com.summer.demo.ui.view.customfragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.ui.view.customfragment.calendar.CalendarView;
import com.summer.helper.utils.STimeUtils;

import butterknife.BindView;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/11 16:45
 */
public class CalendarFragment extends BaseFragment {
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;

    @Override
    protected void initView(View view) {
        CalendarView calendarView = new CalendarView(context);
        calendarView.setClickable(true);
        calendarView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rlContainer.addView(calendarView);
        int[] yearAndMonth = STimeUtils.getCurYearAndMonth();
        int curYear = yearAndMonth[0];
        int curMonth = yearAndMonth[1];
        loadData();
        calendarView.creatCells(yearAndMonth,curMonth);
        calendarView.invalidate();
        calendarView.requestLayout();
    }


    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_container;
    }
}
