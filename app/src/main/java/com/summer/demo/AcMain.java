package com.summer.demo;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.summer.demo.adapter.CommonAdapter;
import com.summer.demo.base.BaseFragmentActivity;
import com.summer.demo.fragment.CDrawableFragment;
import com.summer.demo.fragment.DownloadFragment;
import com.summer.demo.fragment.DragViewFragment;
import com.summer.demo.fragment.MediaPlayerFragment;
import com.summer.demo.fragment.MyDialogFragment;
import com.summer.demo.fragment.PictureUseFragment;
import com.summer.demo.fragment.RXJavaPractice;
import com.summer.demo.fragment.ToastFragment;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.web.WebContainerActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 首页
 *
 * @编者 夏起亮
 */
public class AcMain extends BaseFragmentActivity implements OnItemClickListener, View.OnClickListener {
    ListView titles;
    Context context;
    FragmentManager fragmentManager;
    /* 当前显示的Fragment */
    Fragment mFragment;
    Button btnConceal;

    private void initView() {
        titles = (ListView) findViewById(R.id.listview);
        titles.setOnItemClickListener(this);
        CommonAdapter adapter = new CommonAdapter(context);
        titles.setAdapter(adapter);
        adapter.notifyDataChanged(getData(context));

        btnConceal = (Button) findViewById(R.id.btn_conceal);
        btnConceal.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void finishLoad() {

    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.ac_main;
    }

    @Override
    protected void initData() {
        fragmentManager = this.getSupportFragmentManager();
        context = AcMain.this;
        initView();
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
            }
            return false;
        }
        return false;
    }

    public static List<String> getData(Context context) {
        List<String> title = new ArrayList<String>();
        /* 从XML里获取String数组的方法*/
        String[] group = context.getResources().getStringArray(R.array.titles);
        for (int i = 0; i < group.length; i++) {
            String ti = group[i];
            title.add(ti);
        }
        return title;
    }

    /**
     * 点击每个子项跳转
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        switch (position) {
            case 0:
                JumpTo.getInstance().commonJump(context,AcJava.class);
               // JumpTo.getInstance().commonJump(context, WebContainerActivity.class, "https://java.quanke.name/");
                break;
            case 1:
                JumpTo.getInstance().commonJump(context, AcCases.class);
                break;
            case 2:
                JumpTo.getInstance().commonJump(context, AcCases.class);
                break;
            case 3:
                JumpTo.getInstance().commonJump(context, AcSelfDemo.class);
                break;
            case 4:
                showFragment(new ToastFragment());
                break;
            case 5:
                showFragment(new PictureUseFragment());
                break;
            case 6:
                showFragment(new MediaPlayerFragment());
                break;
            case 7:
                JumpTo.getInstance().commonJump(context, AcListAndGrid.class);
                break;
            case 8:
                JumpTo.getInstance().commonJump(context, WebContainerActivity.class, "https://m.mzmoney.com/topic/recruit/app-recruit.html");
                break;
            case 9:
                JumpTo.getInstance().commonJump(context, AcViewPager.class);
                break;
            case 10:
                showFragment(new MyDialogFragment());
                break;
            case 11:
                showFragment(new CDrawableFragment());
                break;
            case 12:
                showFragment(new DownloadFragment());
                break;
            case 13:
                JumpTo.getInstance().commonJump(context,AcWXApp.class);
                break;
            case 14:
                showFragment(new DragViewFragment());
                break;
            case 15:
                showFragment(new RXJavaPractice());
                break;
            case 16:
                showFragment(new AcAnim());
                break;
        }
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
