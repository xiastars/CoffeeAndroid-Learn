package com.summer.demo.ui;

import android.content.Context;
import android.view.View;

import com.summer.demo.R;
import com.summer.demo.ui.view.commonfragment.viewpager.LeftDotViewPager;
import com.summer.demo.ui.fragment.views.TextViewFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 常见View的一些用法
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/14 16:19
 */
public class AcViews extends BaseTitleListActivity implements View.OnClickListener {

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
                showFragment(new TextViewFragment());
                break;
            case 1:
                showFragment(LeftDotViewPager.newInstance());
                break;
        }
    }


    public static List<String> getData(Context context) {
        List<String> title = new ArrayList<String>();
        /* 从XML里获取String数组的方法*/
        String[] group = context.getResources().getStringArray(R.array.titl_view);
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
