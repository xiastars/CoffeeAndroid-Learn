package com.summer.demo.module.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.summer.demo.R;
import com.summer.demo.bean.BaseResp;
import com.summer.demo.constant.ApiConstants;
import com.summer.demo.helper.PlayAudioHelper;
import com.summer.demo.utils.CUtils;
import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.MaterialRefreshListener;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.ReceiverUtils;
import com.summer.helper.view.NRecycleView;
import com.summer.helper.view.SRecycleView;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xiaqiliang on 2018/7/31.
 */

public abstract class BaseFragment extends Fragment {
    protected MaterialRefreshLayout sRecycleView;
    public MyHandler myHandlder;
    public long fromId;
    public int pageIndex;
    public Context context;
    public FragmentActivity activity;
    boolean isRefresh;
    public String lastId;
    public static int PAGE_FROM = 0;
    public BaseHelper baseHelper;

    boolean isOnRefresh;
    Resources resources;
    private ReceiverUtils receiverUtils;
    protected View mView;
    protected int DEFAULT_LIMIT = 10;

    Unbinder unbinder;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = getContext();
        this.activity = getActivity();
        myHandlder = new MyHandler(this);
        baseHelper = new BaseHelper(context, myHandlder);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = LayoutInflater.from(context).inflate(setContentView() == 0 ? R.layout.view_empty:setContentView(), null);
        unbinder = ButterKnife.bind(this, mView);
        initView(mView);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void refresh() {
        pageIndex = 0;
        lastId = null;
        loadData();
    }

    public void setSRecyleView(final SRecycleView svContainer) {
        setSRecyleView(svContainer, true);
    }

    public void showBackTop(int last, LinearLayout llBackTop, final SRecycleView svContainer) {
        showBackTop(last, llBackTop, svContainer.getRefreshViewForTypeRecycleView());
    }

    public void showBackTop(int last, LinearLayout llBackTop, final RecyclerView svContainer) {
        if (last > BaseHelper.DEFAULT_LOAD_COUNT) {
            llBackTop.setVisibility(View.VISIBLE);
            llBackTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    svContainer.scrollToPosition(0);
                    CUtils.onClick(context,"more_page_return");
                }
            });
        } else {
            llBackTop.setVisibility(View.GONE);
        }
    }

    public void setsRecycleViewOnly(final SRecycleView svContainer) {
        this.sRecycleView = svContainer;
    }

    public void setSRecyleView(final SRecycleView svContainer, final boolean loadMore) {
        this.sRecycleView = svContainer;
        svContainer.setList();
        if (loadMore) {
            svContainer.setLoadMore();
        } else {
            svContainer.setPullUpRefreshable(false);
        }
        svContainer.setIsOverLay(false);
        svContainer.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                Logs.i("刷新");
                if (isOnRefresh) {
                    return;
                }
                isOnRefresh = true;
                PlayAudioHelper.getInstance().stopPlayingAudio();
                fromId = 0;
                lastId = "";
                pageIndex = PAGE_FROM;
                if (loadMore) {
                    svContainer.setLoadMore();
                }
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                if (isOnRefresh) {
                    return;
                }
                isOnRefresh = true;
                pageIndex++;
                loadData();
            }
        });
    }

    public void setSRecyleView(final MaterialRefreshLayout svContainer) {
        this.sRecycleView = svContainer;
        svContainer.setPullUpRefreshable(true);
        svContainer.setIsOverLay(false);
        svContainer.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                if (isOnRefresh) {
                    return;
                }
                fromId = 0;
                pageIndex = PAGE_FROM;
                svContainer.setPullUpRefreshable(true);
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                if (isOnRefresh) {
                    return;
                }
                pageIndex++;
                loadData();
            }
        });
    }

    protected void initBroadcast(String... action) {
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver();
        }
        receiverUtils = new ReceiverUtils(getActivity());
        receiverUtils.setActionsAndRegister(action);
        receiverUtils.setOnReceiverListener(new ReceiverUtils.ReceiverListener() {
            @Override
            public void doSomething(String action, Intent intent) {
                onMsgReceiver(action, intent);
            }

        });
    }

    protected void onMsgReceiver(String action, Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver();
        }
    }

    public void handleViewData(Object obj) {
        baseHelper.handleViewData(obj, sRecycleView, pageIndex);
    }

    public void handleViewData(Object obj, int otherItemCount) {
        baseHelper.handleViewData(obj, sRecycleView, pageIndex, otherItemCount);
    }

    public void handleViewData(Object obj, NRecycleView view) {
        baseHelper.handleViewData(obj, view, sRecycleView, pageIndex);
    }

    public void setLoadMore(boolean loadMore) {
        if (sRecycleView != null) {
            sRecycleView.setPullUpRefreshable(loadMore);
        }
    }

    public static class MyHandler extends Handler {
        private final WeakReference<BaseFragment> mActivity;

        public MyHandler(BaseFragment activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            BaseFragment activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case BaseHelper.MSG_SUCCEED:
                        activity.handleData(msg.arg1, msg.obj, false);
                        activity.finishLoad();
                        break;
                    case BaseHelper.MSG_FINISHLOAD:
                        activity.finishLoad();
                        break;
                    case BaseHelper.MSG_CACHE:
                        activity.handleData(msg.arg1, msg.obj, true);
                        break;
                    case BaseHelper.MSG_ERRO:
                        activity.dealErrors(msg.arg1, msg.arg2 + "", (String) msg.obj);
                        break;
                    default:
                        activity.handleMsg(msg.what, msg.obj);
                }
            }
        }
    }

    protected void handleMsg(int position, Object object) {

    }

    private void handleData(int requestType, Object object, boolean fromCache) {
        myHandlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                isOnRefresh = false;
            }
        }, 100);
        if (this.getActivity() == null || this.getActivity().isFinishing()) {
            return;
        }
        if (object == null) {
            return;
        }
        if (object instanceof BaseResp) {
            BaseResp resp = (BaseResp) object;
            boolean isResult = resp.isResult();
            //如果不是成功状态不返回
            if ((!isResult)) {
                if (fromCache) {
                    return;
                }
                dealErrors(requestType, resp.getError() + "", resp.getMsg());
                return;
            }
        }
        isOnRefresh = false;
        dealDatas(requestType, object);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
        unbinder.unbind();
    }

    protected void finishLoad(MaterialRefreshLayout svContainer) {
        if (svContainer != null) {
            if (pageIndex == PAGE_FROM) {
                svContainer.finishPullDownRefresh();
            } else {
                svContainer.finishPullUpRefresh();
            }
        }
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
    }

    protected void finishLoad() {
        if (sRecycleView != null) {
            if (pageIndex == PAGE_FROM) {
                sRecycleView.finishPullDownRefresh();
            } else {
                sRecycleView.finishPullUpRefresh();
            }
        }
        if (baseHelper != null) {
            baseHelper.cancelLoading();
        }
    }

    public long getHandleTime() {
        return baseHelper.getHandleTime();
    }

    public void requestData(Class className, SummerParameter params, final String url, boolean post) {
        requestData(0, className, params, url, post);
    }

    /**
     * 1.2接口get数据
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
        baseHelper.setIsRefresh(isRefresh);
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
        baseHelper.setIsRefresh(isRefresh);
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
        baseHelper.setIsRefresh(isRefresh);
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
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode, ApiConstants.getHostVersion2(), 0, className, params, url, 1, false);
    }

    public void requestData(int requestCode, Class className, SummerParameter params, final String url, boolean post) {
        if (baseHelper == null) {
            return;
        }
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode, className, params, url, post);
    }

    public void requestData(int requestCode, int limitTime, Class className, SummerParameter params, final String url, boolean post) {
        requestData(requestCode, className, params, url, post, false);
    }

    public void requestData(int requestCode, Class className, SummerParameter params, final String url, boolean post, boolean isArray) {
        if (baseHelper == null) {
            return;
        }
        baseHelper.setIsRefresh(isRefresh);
        baseHelper.requestData(requestCode, 0, className, params, url, post, isArray);
    }

    @Override
    public void onResume() {
        super.onResume();
        //MobclickAgent.onPageStart(this.getClass().getName());
    }

    /**
     * 处理错误
     *
     * @param requestType
     * @param errString
     */
    protected void dealErrors(int requstCode, String requestType, String errString) {
        isOnRefresh = false;
        if (baseHelper != null) {
            baseHelper.cancelLoading();
            finishLoad();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //MobclickAgent.onPageEnd(this.getClass().getName());
    }

    public void onHide(){

    }

    /**
     * 添加一个子View
     * @param view
     */
    private void addView(View view){
        ((ViewGroup)mView).addView(view);
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

    protected void loadData(){

    }

    protected abstract void initView(View view);

    protected abstract void dealDatas(int requestType, Object obj);

    protected abstract int setContentView();

}
