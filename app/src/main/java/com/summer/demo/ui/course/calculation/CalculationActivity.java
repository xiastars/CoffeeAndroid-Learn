package com.summer.demo.ui.course.calculation;

import android.content.Context;

import com.summer.demo.R;
import com.summer.demo.ui.BaseTitleListActivity;
import com.summer.helper.utils.Logs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/12 10:26
 */
public class CalculationActivity extends BaseTitleListActivity {

    @Override
    protected void initData() {
        super.initData();
        setTitle("工程计算");
    }


    @Override
    protected List<String> setData() {
        return getData(context);
    }

    @Override
    protected void clickChild(int pos) {
        switch (pos) {
            case 0:
               showFragment(new TrapezoidFragment());
                break;
        }
    }

    public static List<String> getData(Context context) {
        List<String> title = new ArrayList<String>();
        /* 从XML里获取String数组的方法*/
        String[] group = context.getResources().getStringArray(R.array.calculation);
        Logs.i("group:"+group);
        for (int i = 0; i < group.length; i++) {
            String ti = group[i];
            title.add(ti);
        }
        return title;
    }

}
