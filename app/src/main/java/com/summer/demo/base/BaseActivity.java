package com.summer.demo.base;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.summer.helper.adapter.SRecycleMoreAdapter;
import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.MaterialRefreshListener;
import com.summer.helper.recycle.ScollViewRefreshLayout;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.ReceiverUtils;
import com.summer.helper.view.NRecycleView;
import com.summer.helper.view.SRecycleView;

/**
 * Created by xiaqiliang on 2017/3/24.
 */
public abstract class BaseActivity extends BaseRequestActivity {
    protected SRecycleView sRecycleView;
    ScollViewRefreshLayout scrollView;

    ReceiverUtils receiverUtils;

    public void setSRecyleViewForGrid(final SRecycleView svContainer, int spanCount) {
        this.sRecycleView = svContainer;
        svContainer.setGridView(spanCount);
        svContainer.setLoadMore();
        svContainer.setIsOverLay(false);
        svContainer.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                fromId = 0;
                pageIndex = 0;
                isRefresh = true;
                svContainer.setLoadMore();
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                pageIndex++;
                isRefresh = true;
                loadData();
            }
        });
    }

    public void showBackTop(int last, LinearLayout llBackTop, final SRecycleView svContainer ){
        showBackTop(last,llBackTop,svContainer.getRefreshViewForTypeRecycleView());
    }

    public void showBackTop(int last, LinearLayout llBackTop, final RecyclerView svContainer ){
        if(last > BaseHelper.DEFAULT_LOAD_COUNT){
            llBackTop.setVisibility(View.VISIBLE);
            llBackTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    svContainer.scrollToPosition(0);
                }
            });
        }else{
            llBackTop.setVisibility(View.GONE);
        }
    }

    @Override
    public void initPresenter() {

    }

    public void setSRecyleView(final SRecycleView svContainer) {
        this.sRecycleView = svContainer;
        svContainer.setList();
        svContainer.setLoadMore();
        svContainer.setIsOverLay(false);
        svContainer.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                fromId = 0;
                pageIndex = 0;
                lastId = null;
                isRefresh = true;
                svContainer.setLoadMore();
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                pageIndex++;
                isRefresh = true;
                loadData();
            }
        });
    }

    public void refreshData() {
        pageIndex = 0;
        lastId = null;
        loadData();
    }

    public void setLoadMore(boolean loadMore) {
        Logs.i("loadMOre:" + scrollView + ",,," + loadMore);
        if (scrollView != null) {
            scrollView.setPullUpRefreshable(loadMore);
        }
        if (sRecycleView != null) {
            sRecycleView.setPullUpRefreshable(loadMore);
        }
    }

    protected void setsRecycleViewAdapter(SRecycleMoreAdapter adapter) {
        sRecycleView.setAdapter(adapter);
    }

    public void handleViewData(Object obj) {
        if (sRecycleView == null) {
            return;
        }
        if (sRecycleView.getRefreshViewForTypeRecycleView() == null) {
            return;
        }
        baseHelper.handleViewData(obj, sRecycleView, pageIndex);
    }

    public void handleViewData(Object obj, NRecycleView nRecycleView) {
        baseHelper.handleViewData(obj, nRecycleView, sRecycleView == null ? scrollView : sRecycleView, pageIndex);
    }


    public void handleViewData(Object obj, MaterialRefreshLayout nRecycleView) {
        baseHelper.handleViewData(obj, nRecycleView, pageIndex);
    }

    public long getHandleTime() {
        return baseHelper.getHandleTime();
    }

    public void showLoadingDialogWithRequest(boolean show) {
        baseHelper.setShowLoading(show);
    }

    /**
     * 注册广播
     *
     * @param action
     */
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

    protected void sendBroadcast(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        sendBroadcast(intent);
    }

    public void setScrollView(final ScollViewRefreshLayout scrollView) {
        this.scrollView = scrollView;
        scrollView.setOverLay();
        scrollView.setLoadMore();
        scrollView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                fromId = 0;
                isRefresh = true;
                pageIndex = 0;
                scrollView.setLoadMore();
                loadData();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                pageIndex++;
                isRefresh = true;
                loadData();
            }
        });
    }

    public void setOnlyScrollView(final ScollViewRefreshLayout scrollView) {
        this.scrollView = scrollView;
    }

    protected void onMsgReceiver(String action, Intent intent) {
        if (context == null) {
            return;
        }
    }

    @Override
    protected void loadData() {
    }

    @Override
    protected void finishLoad() {
        if (sRecycleView != null) {
            finishLoad(sRecycleView);
        }

        if (scrollView != null) {
            finishLoad(scrollView);
        }
    }

    protected void finishLoad(MaterialRefreshLayout scrollView) {
        isRefresh = false;
        if (scrollView != null) {
            if (pageIndex == 0) {
                scrollView.finishPullDownRefresh();
            } else {
                scrollView.finishPullUpRefresh();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context = null;
        if (receiverUtils != null) {
            receiverUtils.unRegisterReceiver();
        }
    }
}