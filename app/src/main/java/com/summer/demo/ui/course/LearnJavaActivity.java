package com.summer.demo.ui.course;

import android.content.Context;

import com.summer.demo.R;
import com.summer.demo.ui.BaseTitleListActivity;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;

import java.util.ArrayList;
import java.util.List;


/**
 * Java基础
 *
 * @author xiastars@vip.qq.com
 */
public class LearnJavaActivity extends BaseTitleListActivity {
    int type;

    @Override
    protected void initData() {
        type = JumpTo.getInteger(this);
        super.initData();
        if (type == CoursePos.POS_BACK) {
            setTitle("后端架构师技术图谱");
        } else {
            setTitle("Java零基础教程");
        }

    }

    @Override
    protected List<String> setData() {
        return getData(context);
    }

    @Override
    protected void clickChild(int pos) {
        String title = getData(context).get(pos);
        if (type == CoursePos.POS_BACK) {
            switch (pos) {
                case 0:
                    JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.BACK_QUEUE,title);
                    break;
                case 1:
                    JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.BACK_QUEUE1,title);
                    break;
                case 2:
                    JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.BACK_QUEUE2,title);
                    break;
                case 3:
                    JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.BACK_QUEUE3,title);
                    break;
            }
        } else {
            switch (pos) {
                case 0:
                    JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.JAVA_OBJECT,title);
                    break;
                case 1:
                    JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.JAVA_CHILD,title);
                    break;
            }
        }

    }

    public List<String> getData(Context context) {
        List<String> title = new ArrayList<String>();
        int res = 0;
        Logs.i("type:" + type);
        switch (type) {
            case CoursePos.POS_BACK:
                res = R.array.back_titles;
                break;
        }
        /* 从XML里获取String数组的方法*/
        String[] group = context.getResources().getStringArray(res);
        for (int i = 0; i < group.length; i++) {
            String ti = group[i];
            title.add(ti);
        }
        return title;
    }

}
