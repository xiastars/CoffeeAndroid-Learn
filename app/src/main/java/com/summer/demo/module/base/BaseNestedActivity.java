package com.summer.demo.module.base;

import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.MaterialRefreshListener;
import com.summer.helper.recycle.NestefreshLayout;
import com.summer.helper.utils.SUtils;
import com.summer.helper.utils.SViewUtils;
import com.summer.helper.view.PagerSlidingTabStrip;
import com.summer.helper.view.ScrollableLayout;

import butterknife.BindView;

/**
 * Created by xiastars on 2018/3/16.
 */

public abstract class BaseNestedActivity extends BaseRequestActivity {

    @BindView(R.id.ll_nested_container)
    LinearLayout llNestedContainer;
    @BindView(R.id.pagerStrip)
    protected PagerSlidingTabStrip pagerStrip;
    @BindView(R.id.rl_pager)
    RelativeLayout rlPager;
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
    Button btnEditProfile;
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

    int preY;
    int headerHeight;
    boolean showCustomTitle;

    @Override
    protected void finishLoad() {
        refreshlayout.finishPullDownRefresh();
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
        return R.layout.activity_base_nested;
    }

    @Override
    protected void initData() {
        refreshlayout.addRefreshView(scrollableLayout);
        llNestedContainer.addView(getContainerView());
        onRefresh();
        init();
        initFragmentPager(pagerStrip, scrollableLayout);
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

    protected void initFragmentPager(PagerSlidingTabStrip pagerStrip, ScrollableLayout scrollableLayout) {
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
        scrollableLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onScroll(int currentY, int maxY) {
                if (preY == 0) {
                    preY = currentY;
                }
                if (preY < currentY) {//向上移动
                    if (currentY > headerHeight) {
                        changeHeaderStyleTrans(getResColor(R.color.grey_ba));
                        rlBack.setBackgroundColor(getResColor(R.color.white));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            ivBack.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.black)));
                            btnEditProfile.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.black)));
                            btnShare.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.black)));
                        }
                        lineTitle.setVisibility(View.VISIBLE);
                        tvNestTitle.setTextColor(getResColor(R.color.black));
                    }else{
                        rlBack.setAlpha(1);
                    }
                } else {
                    if (currentY < headerHeight) {
                        rlBack.setBackgroundColor(getResColor(R.color.transparent));
                        changeHeaderStyleTrans(getResColor(R.color.half_greye1));
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                            btnEditProfile.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.white)));
                            btnShare.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.white)));
                            ivBack.setBackgroundTintList(ColorStateList.valueOf(getResColor(R.color.white)));
                        }
                        lineTitle.setVisibility(View.GONE);
                        tvNestTitle.setTextColor(getResColor(R.color.white));
                    }
                }
                preY = currentY;
            }
        });
        if (setTitleId() != 0) {
            tvNestTitle.setText(context.getResources().getString(setTitleId()));
        }
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });

    }

    /**
     * 显示编辑按钮
     *
     * @param visible
     */
    public void showBtnEidt(int visible) {
        btnEditProfile.setVisibility(visible);
    }


    /**
     * 显示分享按钮
     *
     * @param visible
     */
    public void showBtnShare(int visible) {
        btnShare.setVisibility(visible);
    }

    public abstract View getContainerView();


}
