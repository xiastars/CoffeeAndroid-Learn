package com.summer.demo.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nufang.fsxq.R;
import com.nufang.fsxq.base.activity.swipe.SwipeBackActivityHelper;
import com.nufang.fsxq.interf.OnScrollDirectionListener;
import com.nufang.nfhelper.recycle.MaterialRefreshLayout;
import com.nufang.nfhelper.recycle.MaterialRefreshListener;
import com.nufang.nfhelper.recycle.NestefreshLayout;
import com.nufang.nfhelper.utils.SUtils;
import com.nufang.nfhelper.utils.SViewUtils;
import com.nufang.nfhelper.view.PagerSlidingTabStrip;
import com.nufang.nfhelper.view.ScrollableLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiastars on 2018/3/16.
 */

public abstract class BaseNestedFragmentActivity extends BaseFragmentActivity {
    protected FrameLayout flContainer;

    public int pageIndex;


    MaterialRefreshLayout scrollView;

    Resources resources;

    boolean isEmptyViewShowing;
    private SwipeBackActivityHelper mHelper;
    @BindView(R.id.ll_nested_container)
    LinearLayout llNestedContainer;
    @BindView(R.id.pagerStrip)
    protected PagerSlidingTabStrip pagerStrip;
    @BindView(R.id.rl_pager)
    RelativeLayout rlPager;
    @BindView(R.id.viewpager)
    protected ViewPager viewpager;
    @BindView(R.id.scrollableLayout)
    protected ScrollableLayout scrollableLayout;
    @BindView(R.id.refreshlayout)
    NestefreshLayout refreshlayout;
    @BindView(R.id.iv_nav)
    ImageView ivNav;
    @BindView(R.id.tv_hint_content)
    TextView tvHintContent;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.btn_edit_profile)
    public Button btnEditProfile;
    @BindView(R.id.btn_share)
    Button btnShare;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.rl_main_top)
    RelativeLayout rlMainTop;
    @BindView(R.id.line_title)
    View lineTitle;
    @BindView(R.id.tv_nest_title)
    TextView tvNestTitle;


    ScrollableLayout.OnScrollListener onScrollListener;
    public OnScrollDirectionListener onScrollDirectionListener;

    int preY;
    protected int headerHeight;
    boolean showCustomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(setContentView());
        //checkView();
        removeTitleAndFullscreen();
    }
/*

    protected void checkView() {
        if (!SUtils.isNetworkAvailable(context)) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_network_broken, null);
            flContainer.addView(view);
            TextView tvReload = view.findViewById(R.id.tv_reload);
            tvReload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    flContainer.removeAllViews();
                    checkView();
                }
            });
        } else {
            ButterKnife.bind(this);
            initData();
        }
    }
*/


    protected void loadData() {

    }

    protected void finishLoad() {
        refreshlayout.finishPullDownRefresh();
    }

    protected void dealDatas(int requestCode, Object obj) {
    }

    protected int setTitleId() {
        return 0;
    }

    protected int setContentView() {
        return R.layout.activity_base_nested;
    }

    protected void initData() {
        refreshlayout.addRefreshView(scrollableLayout);
        llNestedContainer.addView(getContainerView());
        onRefresh();
        init();
        initFragmentPager(viewpager, pagerStrip, scrollableLayout);
        removeTitleAndFullscreen();
        if (showCustomTitle) {
            SViewUtils.setViewMargin(rlBack, SUtils.getStatusBarHeight(this), SViewUtils.SDirection.TOP);
            handleTitleView();
        }


    }

    protected void removeTitleAndFullscreen() {
        showCustomTitle = true;
        removeTitle();
        setLayoutFullscreen();
    }

    protected void initFragmentPager(ViewPager viewpager, PagerSlidingTabStrip pagerStrip, ScrollableLayout scrollableLayout) {
    }

    protected void init() {
    }

    private void onRefresh() {
        refreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }
        });
    }

    protected void refresh() {

    }

    private void handleTitleView() {
        headerHeight = SUtils.getDip(context, 42) + SUtils.getStatusBarHeight(this);
        scrollableLayout.setDefaultMarginTop(headerHeight);
        handleScroll();


        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });

    }

    protected void handleScroll() {
        scrollableLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onScroll(int currentY, int maxY) {
                if (preY == 0) {
                    preY = currentY;
                }
                if (preY < currentY) {//向上移动
                    if (currentY > headerHeight) {
                        showTitleStyle();
                    }
                    if (onScrollDirectionListener != null) {
                        //onScrollDirectionListener.onScrollDown(true);
                    }
                } else {
                    if (currentY < headerHeight) {
                        hideTitleStyle();
                    }
                    if (onScrollDirectionListener != null) {
                        //onScrollDirectionListener.onScrollDown(false);
                    }
                }
                if (onScrollListener != null) {
                    onScrollListener.onScroll(currentY, maxY);
                }
                preY = currentY;
            }
        });
    }

    protected void onBackClick() {
        //. CUtils.onClick(getClass().getSimpleName() + "_onback");
        BaseNestedFragmentActivity.this.finish();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void hideTitleStyle() {
        rlBack.setBackgroundColor(getResColor(R.color.transparent));
        changeHeaderStyleTrans(getResColor(R.color.transparent));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            btnEditProfile.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.white)));
            btnShare.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.white)));
            /*       ivBack.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.white)));*/
        }
        lineTitle.setVisibility(View.GONE);
        if (setTitleId() != 0) {
            tvNestTitle.setText("");
        }
        tvNestTitle.setTextColor(getResColor(R.color.white));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void showTitleStyle() {
        changeHeaderStyleTrans(getResColor(R.color.grey_ba));
        rlBack.setBackgroundColor(getResColor(R.color.trans));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            ivBack.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.blue_56)));
            btnEditProfile.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.blue_56)));
            btnShare.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.blue_56)));
        }
        lineTitle.setVisibility(View.VISIBLE);
        tvNestTitle.setTextColor(getResColor(R.color.black));
        if (setTitleId() != 0) {
            tvNestTitle.setText(context.getResources().getString(setTitleId()));
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void changeHeaderStyleTrans(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }



    /**
     * 获取颜色资源
     *
     * @param coloRes
     * @return
     */
    public int getResColor(int coloRes) {
        if (resources == null) {
            resources = context.getResources();
        }
        return resources.getColor(coloRes);
    }

    protected abstract  View getContainerView();

    /**
     * 显示编辑按钮
     *
     * @param visible
     */
    public void showBtnEidt(int visible) {
        btnEditProfile.setVisibility(visible);
    }

    public void setOnScrollListener(ScrollableLayout.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    /**
     * 显示分享按钮
     *
     * @param visible
     */
    public void showBtnShare(int visible) {
        btnShare.setVisibility(visible);
    }

    public void setOnScrollDirectionListener(OnScrollDirectionListener onScrollDirectionListener) {
        this.onScrollDirectionListener = onScrollDirectionListener;
    }




}
