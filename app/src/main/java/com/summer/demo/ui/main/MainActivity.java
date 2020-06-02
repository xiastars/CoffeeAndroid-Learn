package com.summer.demo.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;

import com.summer.demo.R;
import com.summer.demo.constant.SharePreConst;
import com.summer.demo.module.base.BaseFragmentActivity;
import com.summer.demo.module.view.CustomViewAbove;
import com.summer.demo.module.view.CustomViewBehind;
import com.summer.demo.module.view.SlidingMenu;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;

/**
 * 主界面
 *
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 9:50
 */
public class MainActivity extends BaseFragmentActivity {


    @BindView(R.id.ll_menu)
    FrameLayout llMenu;
    @BindView(R.id.above)
    CustomViewBehind above;
    @BindView(R.id.ll_home)
    FrameLayout llHome;
    @BindView(R.id.behind)
    CustomViewAbove behind;
    @BindView(R.id.menu)
    SlidingMenu sm;
    @BindView(R.id.view_bg)
    View viewBg;

    FragmentManager fragmentManager;
    MenuFragment menuFragment;
    MainFragment mainFragment;
    BaseMainFragment mFragment;
    boolean isDrag;

    private long mBackPressedTime;


    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        removeTitle();
        initMainView();
    }

    private void initMainView() {
        fragmentManager = this.getSupportFragmentManager();
        if (menuFragment == null) {
            menuFragment = new MenuFragment();
        }
        mainFragment = new MainFragment();
        showMenu(menuFragment);
        showHome(mainFragment);
        initSlidingMenu();
        sm.showContent();
    }

    private void initSlidingMenu() {
        View menuView = findViewById(R.id.behind);
        View view = findViewById(R.id.above);
        sm = (SlidingMenu) findViewById(R.id.menu);
        sm.setMenu(view);
        sm.setContent(menuView);
        sm.setMode(SlidingMenu.LEFT);
        sm.setFadeDegree(0.2f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.toggle();//动态断定主动封闭或开启SlidingMenu
        sm.showMenu();//显示SlidingMenu
        sm.showContent();//显示内容
        sm.setBackgroundColor(Color.WHITE);
        sm.setBehindCanvasTransformer((canvas, percentOpen) -> viewBg.setBackgroundColor(Color.argb((int) (percentOpen * 255 / 3), 30, 30, 32)));

        sm.setOnMenuDragListener((x, y) -> {
            if (x > 0 && !isDrag) {
                sm.setBehindWidth(SUtils.screenWidth);
                isDrag = true;
            } else if (x < 0 && isDrag) {
                sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
                isDrag = false;
            }
        });
        toggleMenu();


    }

    public void toggleMenu() {
        sm.toggle();
    }

    public void toggleToHome() {
        sm.toggle();
        viewBg.setBackgroundColor(Color.TRANSPARENT);

    }

    public void setSmEnable(boolean enable) {
        sm.setSlidingEnabled(enable);
    }

    /**
     * 添加Fragment
     *
     * @param fragment
     */
    private void showMenu(BaseMainFragment fragment) {
        mFragment = fragment;
        fragmentManager.beginTransaction().add(R.id.ll_menu, fragment).commit();
    }

    /**
     * 添加Fragment
     *
     * @param fragment
     */
    private void showHome(BaseMainFragment fragment) {
        removeFragment();
        mFragment = fragment;
        fragmentManager.beginTransaction().add(R.id.ll_home, fragment).commit();
    }


    /**
     * 销毁Fragment最适用的方法是将它替换成一个空的
     */
    private void removeFragment() {
        mFragment = null;
        Fragment fragment = new Fragment();
        fragmentManager.beginTransaction().replace(R.id.ll_home, fragment).commit();
    }

    @Override
    protected void loadData() {

    }

    public void refreshMenuFragment() {
        menuFragment.refresh();
    }

    @Override
    protected void finishLoad() {

    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    public static void show(Context context) {
        MainFragment.curFragmentIndex = 0;
        context.startActivity(new Intent(context, MainActivity.class));
    }


    public void showTrendsDot(boolean isShowDot) {
        mainFragment.showRedDot(isShowDot);
    }


    @Override
    public void onBackPressed() {
        boolean isDoubleClick = SUtils.getBooleanData(context, SharePreConst.DOUBLE_CLICK);
        if (isDoubleClick) {
            long curTime = SystemClock.uptimeMillis();
            if ((curTime - mBackPressedTime) < (3 * 1000)) {
                finish();
            } else {
                mBackPressedTime = curTime;
                SUtils.makeToast(context, "再次点击退出应用！");
            }
        } else {
            finish();
        }
    }
}

