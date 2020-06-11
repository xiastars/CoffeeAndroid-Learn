package com.summer.demo.module.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.helper.PlayAudioHelper;
import com.summer.demo.module.base.swipe.SwipeBackActivity;
import com.summer.demo.constant.ApiConstants;
import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.BitmapUtils;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.ReceiverUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.utils.SViewUtils;
import com.summer.helper.view.NRecycleView;
import com.summer.helper.view.review.RRelativeLayout;
import com.summer.helper.web.ActivitysManager;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 * Created by xiaqiliang on 2017/3/24.
 */
public abstract class BaseFragmentActivity extends SwipeBackActivity {
    TextView tvTitle;
    View viewBack;
    View line1;
    RelativeLayout rlBaseTop;
    RRelativeLayout rlTitle;
    protected FrameLayout flContainer;
    public Context context;
    protected MyHandler myHandlder;
    public int pageIndex;
    boolean isRefresh;
    protected boolean isStop;

    MaterialRefreshLayout scrollView;

    protected FragmentManager fragmentManager;
    public BaseHelper baseHelper;
    ReceiverUtils receiverUtils;
    Resources resources;

    boolean isEmptyViewShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        SUtils.initScreenDisplayMetrics(this);
        ActivitysManager.Add(this.getClass().getSimpleName(), this);
        myHandlder = new MyHandler(this);
        baseHelper = new BaseHelper(context, myHandlder);
        baseHelper.setMIUIStatusBarDarkMode(this);
        fragmentManager = this.getSupportFragmentManager();
        setContentView(R.layout.activity_base);
        initTitleView();
        checkView();

    }

    protected void checkView() {
    /*    if (!SUtils.isNetworkAvailable(context)) {
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

        }*/
        initView();
        ButterKnife.bind(this);
        initData();
    }

    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    public void showEmptyView() {
        showEmptyView("");
    }

    public void showEmptyView(String content) {
        isEmptyViewShowing = true;
        flContainer.removeAllViews();
        View view = LayoutInflater.from(this).inflate(R.layout.view_empty, null);
        TextView tvEmpty = (TextView) view.findViewById(R.id.tv_hint_content);
        if (!TextUtils.isEmpty(content)) {
            tvEmpty.setText(content);
        }
        flContainer.addView(view);
    }

    public void stripEmptyView() {
        if (isEmptyViewShowing) {
            flContainer.removeAllViews();
            initView();
        }
    }

    protected void setBlankTitleView() {
        setBlankTitleView(true);
    }

    protected View setBlankTitleView(boolean isTopMargin) {
        removeTitle();
        RRelativeLayout view = (RRelativeLayout) LayoutInflater.from(context).inflate(R.layout.include_back, null);
        ImageView viewBack = (ImageView) view.findViewById(R.id.view_back);
        flContainer.addView(view);
        SViewUtils.setViewHeight(view, 49);
        setLayoutFullscreen();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.topMargin = SUtils.getStatusBarHeight(this) + (isTopMargin ? SUtils.getDip(context, 20) : 0);
        view.findViewById(R.id.ll_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
        return view;
    }

    /*    */

    /**
     * 初始化分享按钮
     *//*
    protected void initShareButton(final OnShareListener listener) {
        this.listener = listener;
        Button btnShare = (Button) findViewById(R.id.btn_share);
        btnShare.setVisibility(View.VISIBLE);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onShare();
                }
            }
        });
    }*/
    protected void hideShareButton() {
        Button btnShare = (Button) findViewById(R.id.btn_share);
        btnShare.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void changeHeaderStyleTrans(int color) {
        if (rlTitle != null) {
            rlTitle.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        if (viewBack != null) {
            //viewBack.setBackgroundResource(R.drawable.title_icon_return_black);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    /**
     * 更改View状态
     *
     * @param res
     * @return
     */
    protected View changeTitleView(int res) {
        rlTitle = (RRelativeLayout) findViewById(R.id.title);
        rlTitle.removeAllViews();
        View view = LayoutInflater.from(context).inflate(res, null);
        rlTitle.addView(view);
        return view;
    }

    private void initTitleView() {
        rlBaseTop = (RelativeLayout) findViewById(R.id.rl_base_parent);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        if (setTitleId() != 0 && tvTitle != null) {
            tvTitle.setText(getString(setTitleId()));
        }
        rlTitle = (RRelativeLayout) findViewById(R.id.title);
        flContainer = (FrameLayout) findViewById(R.id.fl_container);
        LinearLayout llBack = (LinearLayout) findViewById(R.id.ll_back);
        viewBack = findViewById(R.id.view_back);
        SUtils.clickTransColor(llBack);
        line1 = findViewById(R.id.line1);
        llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logs.i("-----------back");
                onBackClick();
            }
        });
    }

    protected void onBackClick() {
        //. CUtils.onClick(getClass().getSimpleName() + "_onback");
        PlayAudioHelper.getInstance().stopPlayingAudio();
        BaseFragmentActivity.this.finish();
    }

    protected void initBroadcast(String... action) {
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver();
        }
        receiverUtils = new ReceiverUtils(this);
        receiverUtils.setActionsAndRegister(action);
        receiverUtils.setOnReceiverListener(new ReceiverUtils.ReceiverListener() {
            @Override
            public void doSomething(String action, Intent intent) {
                if (context == null) {
                    return;
                }
                onMsgReceiver(action, intent);
            }
        });
    }

    protected void onMsgReceiver(String type, Intent intent) {

    }

    private void initView() {
        if (setContentView() != 0) {
            View view = LayoutInflater.from(this).inflate(setContentView(), null);
            flContainer.addView(view);
        }
    }

    public void requestData(Class className, SummerParameter params, final String url, boolean post) {
        requestData(0, className, params, url, post);
    }

    public void requestData(int requestCode, Class className, SummerParameter params, final String url, boolean post) {
        requestData(requestCode, className, params, url, post, false);
    }

    public void requestData(int requestCode, Class className, SummerParameter params, final String url, boolean post, boolean isArray) {
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode, 0, className, params, url, post, isArray);
    }

    public void requestData(int requestCode, int limiteTime, Class className, SummerParameter params, final String url, boolean post) {
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode, limiteTime, className, params, url, post);
    }

    public void requestData(int requestCode, int limiteTime, Class className, SummerParameter params, final String url, boolean post, boolean isArray) {
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode, limiteTime, className, params, url, post, isArray);
    }

    /**
     * 1.2接口put数据
     *
     * @param requestCode
     * @param className
     * @param params
     * @param url
     */
    public void getDataTwo(int requestCode, Class className, SummerParameter params, final String url) {
        if (baseHelper == null) {
            return;
        }
        baseHelper.requestData(requestCode, ApiConstants.getHostVersion2(), 0, className, params, url, 0, false);
    }

    /**
     * 1.2接口put数据
     *
     * @param requestCode
     * @param className
     * @param params
     * @param url
     */
    public void putDataTwo(int requestCode, Class className, SummerParameter params, final String url) {
        if (baseHelper == null) {
            return;
        }
        baseHelper.putData(requestCode, ApiConstants.getHostVersion2(), className, params, url);
    }

    /**
     * 1.2接口delete数据
     *
     * @param requestCode
     * @param className
     * @param params
     * @param url
     */
    public void deleteDataTwo(int requestCode, Class className, SummerParameter params, final String url) {
        if (baseHelper == null) {
            return;
        }

        baseHelper.deleteData(requestCode, ApiConstants.getHostVersion2(), className, params, url);
    }


    /**
     * 1.2接口get数据
     *
     * @param requestCode
     * @param className
     * @param params
     * @param url
     */
    public void postDataTwo(int requestCode, Class className, SummerParameter params, final String url) {
        if (baseHelper == null) {
            return;
        }
        baseHelper.requestData(requestCode, ApiConstants.getHostVersion2(), 0, className, params, url, 1, false);
    }

    /**
     * 设置沉浸式状态栏
     */
    public void setLayoutFullscreen() {
        baseHelper.setLayoutFullscreen(false);
    }


    /**
     * 设置沉浸式状态栏
     */
    public void setLayoutFullscreen(boolean fullscreen) {
        baseHelper.setLayoutFullscreen(fullscreen);
    }

    public static class MyHandler extends Handler {
        private final WeakReference<BaseFragmentActivity> mActivity;

        public MyHandler(BaseFragmentActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseFragmentActivity activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case BaseHelper.MSG_SUCCEED:
                        activity.handleData(msg.arg1, msg.obj);
                        activity.cancelLoading();
                        break;
                    case BaseHelper.MSG_FINISHLOAD:
                        activity.finishLoad();
                        break;
                    case BaseHelper.MSG_CACHE:
                        activity.handleData(msg.arg1, msg.obj);
                        break;
                    case BaseHelper.MSG_ERRO:
                        Logs.i("requestCode:" + msg.arg1 + ",,," + msg.arg2);
                        activity.dealErrors(msg.arg1, msg.arg2 + "", (String) msg.obj, false);
                        activity.finishLoad();
                        break;
                    default:
                        activity.handleMsg(msg.what, msg.obj);
                }
            }
        }
    }

    public void handleMsg(int position, Object object) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    private void handleData(int requestCode, Object object) {
        if (BaseFragmentActivity.this.isFinishing()) {
            return;
        }
        dealDatas(requestCode, object);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
        isStop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver();
        }
        BitmapUtils.getInstance().clearBitmaps(getClass().getSimpleName());
        context = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStop = false;
    /*    MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(this.getClass().getName());*/
        //CUtils.onClick(getClass().getSimpleName() + "_resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
  /*      MobclickAgent.onPause(this);
        CUtils.onClick(getClass().getSimpleName() + "_pause");
        MobclickAgent.onPageEnd(this.getClass().getName());*/
        if (tvTitle != null) {
            SUtils.hideSoftInpuFromWindow(tvTitle);
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

    /**
     * 获取颜色资源
     *
     * @param coloRes
     * @return
     */
    public Drawable getResDrawable(int coloRes) {
        if (resources == null) {
            resources = context.getResources();
        }
        return resources.getDrawable(coloRes);
    }

    /**
     * 处理错误
     *
     * @param requstCode  请求数据标识码
     * @param requestType 返回错误码，根据requestCode判断是返回的Code还是@ErroCode
     * @param errString   错误信息
     * @param requestCode 如果是返回的Code则为true
     */
    protected void dealErrors(int requstCode, String requestType, String errString, boolean requestCode) {
        if (baseHelper != null) {
            baseHelper.cancelLoading();
            finishLoad();
        }
    }

    public RelativeLayout getTitleParent() {
        return rlTitle;
    }

    private void cancelLoading() {
        baseHelper.cancelLoading();
    }

    protected abstract void loadData();

    /**
     * 分页加载时结束加载
     */
    protected abstract void finishLoad();

    /**
     * 处理返回的数据
     */
    protected abstract void dealDatas(int requestCode, Object obj);

    /**
     * 设置当前界面标题
     */
    protected abstract int setTitleId();

    /**
     * 设置当前界面主体内容
     */
    protected abstract int setContentView();

    /**
     * 初始化界面与数据
     */
    protected abstract void initData();


    protected void setTitleString(String titleStr) {
        if (tvTitle != null)
            tvTitle.setText(titleStr);
    }

    protected void setScrollView(MaterialRefreshLayout layout) {
        this.scrollView = layout;
    }

    public void handleViewData(Object obj, NRecycleView nRecycleView) {
        baseHelper.handleViewData(obj, nRecycleView, scrollView, pageIndex);
    }

    public void setLoadMore(boolean loadMore) {
        if (scrollView != null) {
            scrollView.setPullUpRefreshable(loadMore);
        }
    }

    public void handleViewData(Object obj) {
        baseHelper.handleViewData(obj, scrollView, pageIndex);
    }



    /**
     * 如果不想要默认的头部，则移除，自己在Activity里写
     */
    public void removeTitle() {
        rlBaseTop.removeView(rlTitle);
        //changeHeaderStyleTrans(getResColor(R.color.half_greye1));
        line1.setVisibility(View.GONE);
    }
}
