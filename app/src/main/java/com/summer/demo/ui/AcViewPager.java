package com.summer.demo.ui;

import android.content.Context;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.ui.view.commonfragment.viewpager.CommonVPFragment;
import com.summer.demo.ui.view.commonfragment.viewpager.LeftDotViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * ViewPager的一般用法
 *
 * @author xiastars@vip.qq.com
 */
public class AcViewPager extends BaseTitleListActivity implements View.OnClickListener {

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected List<String> setData() {
        return getData(context);
    }

    @Override
    protected void clickChild(int pos) {
        switch (pos) {
            case 0:
                showFragment(CommonVPFragment.newInstance());
                break;
            case 1:
                showFragment(LeftDotViewPager.newInstance());
                break;
        }
    }


    public static List<String> getData(Context context) {
        List<String> title = new ArrayList<String>();
        /* 从XML里获取String数组的方法*/
        String[] group = context.getResources().getStringArray(R.array.title_viewpager);
        for (int i = 0; i < group.length; i++) {
            String ti = group[i];
            title.add(ti);
        }
        return title;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
