package com.summer.demo.ui.course;

import android.content.Context;

import com.summer.demo.R;
import com.summer.demo.ui.BaseTitleListActivity;
import com.summer.helper.utils.JumpTo;

import java.util.ArrayList;
import java.util.List;


/**
 * 一些常用工具
 *
 * @author xiastars@vip.qq.com
 */
public class ToolsActivity extends BaseTitleListActivity {

    @Override
    protected void initData() {
        super.initData();
        setTitle("常用工具");
    }

    @Override
    protected List<String> setData() {
        return getData(context);
    }

    @Override
    protected void clickChild(int pos) {
        switch (pos) {
            case 0:
                JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.NET_FIVE_STRUCTRURE);

                break;
            case 1:
                JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.NET_HANKSHAKE);
                break;
            case 2:
                JumpTo.getInstance().commonJump(context, CourseContainerActivity.class, MarkdownPos.NET_HTTPS);
                break;
        }
    }

    public static List<String> getData(Context context) {
        List<String> title = new ArrayList<String>();
        /* 从XML里获取String数组的方法*/
        String[] group = context.getResources().getStringArray(R.array.net);
        for (int i = 0; i < group.length; i++) {
            String ti = group[i];
            title.add(ti);
        }
        return title;
    }

}
