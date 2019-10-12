package com.summer.demo.ui.main;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.summer.demo.R;
import com.summer.demo.dialog.LoadingDialog;
import com.summer.demo.module.view.NavigationButton;
import com.summer.demo.ui.course.CourseFragment;
import com.summer.demo.ui.mine.MineFragment;
import com.summer.demo.ui.module.ModuleFragment;
import com.summer.demo.ui.view.HomePagerFragment;
import com.summer.demo.utils.CUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.view.transformer.MoveAnimation;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 主Fragment
 */
public class MainFragment extends BaseMainFragment implements View.OnClickListener {

    @BindView(R.id.fl_ui)
    FrameLayout flUi;
    @BindView(R.id.fl_course)
    FrameLayout flCourse;
    @BindView(R.id.home_content2)
    FrameLayout minLayout;
    @BindView(R.id.fl_module)
    FrameLayout flModule;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.nav_view)
    NavigationButton navView;
    @BindView(R.id.nav_item_dynamic)
    NavigationButton navItemDynamic;
    @BindView(R.id.nav_item_explore)
    NavigationButton nvItemExplore;
    @BindView(R.id.nav_item_release)
    NavigationButton nvItemRelease;
    @BindView(R.id.nav_item_me)
    NavigationButton navItemMe;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

    List<FrameLayout> frameLayouts = new ArrayList<>();
    List<BaseMainFragment> fragments = new ArrayList<>();
    HomePagerFragment homePagerFragment;
    ModuleFragment moduleFragment;
    MineFragment mineFragment;
    FragmentManager fgManager;
    public static int curFragmentIndex;//当前所在位置

    boolean firstAnimDisable;//第一次游客跳发现不需要动画
    boolean firstShowLoading;

    @OnClick({R.id.nav_view, R.id.nav_item_dynamic, R.id.nav_item_me, R.id.nav_item_explore, R.id.nav_item_release})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nav_item_release:
                //发表页面
                CUtils.onClick(context, "home_bottom_release");
                //ReleaseTopicActivity.show(context, MarUser.DEFAULT_GROUP_ID);
                break;
            case R.id.nav_item_explore:
                switchToFragment(1);
                break;
            case R.id.nav_view:
                switchToFragment(0);
                break;
            case R.id.nav_item_me:
                switchToFragment(3);
                break;
            case R.id.nav_item_dynamic:
                switchToFragment(2);
                break;
        }


    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_nav;
    }

    @Override
    protected void initView(View view) {
        initMainView();
    }

    @Override
    public void loadData() {

    }

    private void initMainView() {
        Logs.i(firstShowLoading + "vivo firstShowLoading" + context);
        if (!firstShowLoading) {
            firstShowLoading = true;
            final LoadingDialog loadingDialog = new LoadingDialog(context);
            loadingDialog.startLoading();
            //给充足的时间让页面渲染
            myHandlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.cancelLoading();
                }
            }, 1500);
        }

        navView.init(R.drawable.tab_icon_view,
                R.string.tab_view,
                HomePagerFragment.class);
        nvItemExplore.init(R.drawable.tab_icon_discover,
                R.string.tab_module,
                ModuleFragment.class);
        navItemDynamic.init(R.drawable.tab_icon_discover,
                R.string.tab_course,
                CourseFragment.class);
        navItemMe.init(R.drawable.tab_icon_mine,
                R.string.tab_mine,
                MineFragment.class);
        initFragment();
    }

    private void initFragment() {
        fgManager = activity.getSupportFragmentManager();
        frameLayouts.add(flUi);
        frameLayouts.add(flModule);
        frameLayouts.add(flCourse);
        frameLayouts.add(minLayout);
        homePagerFragment = new HomePagerFragment();
        fragments.add(homePagerFragment);
        moduleFragment = new ModuleFragment();
        fragments.add(moduleFragment);
        fragments.add(new CourseFragment());
        mineFragment = new MineFragment();
        fragments.add(mineFragment);
        for (int i = 0; i < fragments.size(); i++) {
            try {
                BaseMainFragment frament = fragments.get(i);
                if (frament.isAdded()) {

                    fgManager.beginTransaction().show(frament).commitAllowingStateLoss();
                } else {
                    fgManager.beginTransaction().add(frameLayouts.get(i).getId(), frament).commitAllowingStateLoss();
                }

            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        //1.3.2 游客跳到发现
        myHandlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                firstAnimDisable = true;
                switchToFragment(0);
            }
        }, 1000);
    }

    /**
     * 刷新单个Fragment数据
     */
    public void refreshTabData(int position) {
        if (fragments == null || fragments.size() < position + 1) {
            return;
        }
        BaseMainFragment tabFragment = fragments.get(position);
        tabFragment.refresh();
    }

    /**
     * 显示动态小红点
     *
     * @param isShowDot
     */
    public void showRedDot(boolean isShowDot) {
        navItemDynamic.showRedDot(isShowDot);
    }


    /**
     * 显示当前模块
     *
     * @param position
     */
    public void switchToFragment(int position) {
        if (position == 3) {
            activity.setSmEnable(true);
        } else {
            activity.setSmEnable(false);
        }
        if (position == -1) {
            return;
        }
        onTabSelect(position);
        boolean anim = true;
        if (position == curFragmentIndex || firstAnimDisable) {
            anim = false;
        }
        firstAnimDisable = false;
        if (fragments.size() > curFragmentIndex && fragments.get(curFragmentIndex) != null) {
            int direction = setDirection(position);
            setFragmentAnim(direction, View.GONE, false, anim);
        }

        int direction = setDirection(position);
        curFragmentIndex = position;
        setFragmentAnim(direction, View.VISIBLE, true, anim);
    }

    private void onTabSelect(int position) {
        navView.setSelected(position == 0 ? true : false);
        nvItemExplore.setSelected(position == 1 ? true : false);
        navItemDynamic.setSelected(position == 2 ? true : false);
        navItemMe.setSelected(position == 3 ? true : false);
    }


    /**
     * 为当前模块配置转场动画
     *
     * @param direction
     * @param visible
     * @param enter
     */
    private void setFragmentAnim(int direction, final int visible, final boolean enter, boolean anim) {
        if (frameLayouts == null || curFragmentIndex >= frameLayouts.size()) {
            return;
        }
        final FrameLayout curLayout = frameLayouts.get(curFragmentIndex);
        curLayout.setVisibility(View.VISIBLE);
        if (!anim) {
            curLayout.setVisibility(visible);
            return;
        }
        Animation animation = MoveAnimation.create(direction, enter, 400);
        curLayout.setAnimation(animation);
        animation.start();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                curLayout.setVisibility(visible);
                if (enter) {
                    //fragments.get(curFragmentIndex).onShow();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private int setDirection(int position) {
        if (curFragmentIndex != position) {
            if (curFragmentIndex > position) {
                return MoveAnimation.RIGHT;
            } else {
                return MoveAnimation.LEFT;
            }
        } else {
            return -1;
        }
    }

    @Override
    protected void finishLoad() {

    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //Jzvd.releaseAllVideos();
        }
    }

    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

}
