package com.summer.demo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.summer.demo.ui.BaseTitleListActivity;
import com.summer.demo.ui.fragment.cases.SGHeightConflictFragment;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.web.WebContainerActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 遇到的一些问题
 *
 * @author xiastars@vip.qq.com
 */
public class AcCases extends BaseTitleListActivity implements View.OnClickListener {
    private ListView titles;
    private Context context;
    FragmentManager fragmentManager;
    /* 当前显示的Fragment */
    Fragment mFragment;
    Button btnConceal;

    @Override
    protected void initData() {
        super.initData();
        fragmentManager = this.getSupportFragmentManager();
    }

    @Override
    protected List<String> setData() {
        return getData(context);
    }

    @Override
    protected void clickChild(int pos) {
        switch (pos) {
            case 0:
                showFragment(new SGHeightConflictFragment());
                break;
            case 1:
                jump("https://xiastars.gitbooks.io/java/content/20-%E5%AD%90%E7%B1%BB%E4%B8%8E%E7%88%B6%E7%B1%BB.html");
                break;
            case 4:
                //showFragment(new HorListFragment());
                break;
        }
    }


    /**
     * 监听系统的返回键，当处于Fragment时，点击返回，则回到主界面
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mFragment != null) {
                removeFragment();
                return true;
            } else {
                finish();
            }
            return false;
        }
        return false;
    }

    public static List<String> getData(Context context) {
        List<String> title = new ArrayList<String>();
        /* 从XML里获取String数组的方法*/
        String[] group = context.getResources().getStringArray(R.array.cases_titles);
        for (int i = 0; i < group.length; i++) {
            String ti = group[i];
            title.add(ti);
        }
        return title;
    }


    private void jump(String url){
        JumpTo.getInstance().commonJump(context, WebContainerActivity.class,url);
    }

    public void showFragment(Fragment fragment) {
        //销毁已显示的Fragment
        removeFragment();
        beginTransation(fragment);
    }

    /**
     * 添加Fragment
     *
     * @param fragment
     */
    private void beginTransation(Fragment fragment) {
        mFragment = fragment;
        findViewById(R.id.ll_container).setVisibility(View.VISIBLE);
        btnConceal.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().add(R.id.ll_container, fragment).commit();
    }

    /**
     * 销毁Fragment最适用的方法是将它替换成一个空的
     */
    private void removeFragment() {
        mFragment = null;
        findViewById(R.id.ll_container).setVisibility(View.GONE);
        Fragment fragment = new Fragment();
        fragmentManager.beginTransaction().replace(R.id.ll_container, fragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_conceal:
                removeFragment();
                btnConceal.setVisibility(View.GONE);
                break;
        }
    }

}
