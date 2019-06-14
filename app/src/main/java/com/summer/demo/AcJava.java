package com.summer.demo;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.summer.demo.ui.BaseTitleListActivity;
import com.summer.demo.ui.MarkDownActivity;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.web.WebContainerActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Java基础
 *
 * @author xiastars@vip.qq.com
 */
public class AcJava extends BaseTitleListActivity implements View.OnClickListener {
    private ListView titles;
    private Context context;

    Button btnConceal;

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
                JumpTo.getInstance().commonJump(context, MarkDownActivity.class);
                //jump("https://github.com/xiastars/CoffeeAndroid-Learn/blob/master/document/java/1.%E5%85%B3%E4%BA%8E%E5%AF%B9%E8%B1%A1.md");
                break;
            case 1:
                jump("https://xiastars.gitbooks.io/java/content/20-%E5%AD%90%E7%B1%BB%E4%B8%8E%E7%88%B6%E7%B1%BB.html");
                break;
            case 2:
                jump("https://java.quanke.name/");
               // showFragment(new RecycleListFragment());
                break;
            case 3:
                //showFragment(new RecycleGridFragment());
                break;
            case 4:
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

    private void jump(String url){
        JumpTo.getInstance().commonJump(context, WebContainerActivity.class,url);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}
