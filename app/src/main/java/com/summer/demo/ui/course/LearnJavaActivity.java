package com.summer.demo.ui.course;

import android.content.Context;

import com.summer.demo.R;
import com.summer.demo.ui.BaseTitleListActivity;
import com.summer.helper.utils.JumpTo;

import java.util.ArrayList;
import java.util.List;


/**
 * Java基础
 *
 * @author xiastars@vip.qq.com
 */
public class LearnJavaActivity extends BaseTitleListActivity{

    @Override
    protected void initData() {
        super.initData();
        setTitle("Java零基础教程");
    }

    @Override
    protected List<String> setData() {
        return getData(context);
    }

    @Override
    protected void clickChild(int pos) {
        switch (pos) {
            case 0:
                JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.JAVA_OBJECT);
                break;
            case 1:
                JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.JAVA_CHILD);
                break;
        }
    }

    public static List<String> getData(Context context) {
        List<String> title = new ArrayList<String>();
        /* 从XML里获取String数组的方法*/
        String[] group = context.getResources().getStringArray(R.array.java_titles);
        for (int i = 0; i < group.length; i++) {
            String ti = group[i];
            title.add(ti);
        }
        return title;
    }

}
