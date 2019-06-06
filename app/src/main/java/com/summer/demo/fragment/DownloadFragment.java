package com.summer.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summer.demo.R;
import com.summer.demo.adapter.DownloadAdapter;
import com.summer.demo.bean.BookBean;
import com.summer.demo.bean.RequestBook;
import com.summer.demo.server.CommonDBType;
import com.summer.helper.db.CommonService;
import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.MaterialRefreshListener;
import com.summer.helper.server.EasyHttp;
import com.summer.helper.server.RequestCallback;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.SRecycleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiastars@vip.qq.com on 2016年12月12日 14:08.
 */

public class DownloadFragment extends BaseFragment implements View.OnClickListener {
    String url = "http://appstore.kidspad.zuoyegou.com/search?qs=a";

    SRecycleView mGridView;

    DownloadAdapter mAdapter;
    CommonService mService;

    List<BookBean> mBooks;

    String lasttime;
    boolean mFreshing;
    int pageNum = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, null);
        initView(view);
        return view;
    }

    public void initView(View view) {
        mService = new CommonService(context);
        mGridView = (SRecycleView) view.findViewById(R.id.rv_books);
        mGridView.setList();
        mAdapter = new DownloadAdapter(context);
        mGridView.setAdapter(mAdapter);

        //下拉刷新与上拉加载回调
        mGridView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                refresh();
            }

            @Override
            public void onfinish() {
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                onListLoad();
            }
        });

        //从数据库获取缓存，如果有，则先刷新界面，然后下载
        mBooks = (List<BookBean>) mService.getListData(CommonDBType.DOWNLOAD_DATA);
        if (null != mBooks && 0 < mBooks.size()) {
            mAdapter.notifyDataChanged(mBooks);
        }
        requestData();
    }


    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mService != null) {
            mService.closeDB();
        }
    }

    private void refresh() {
        lasttime = "";
        pageNum = 1;
        requestData();
    }

    public void notifyData() {
        lasttime = "";
        pageNum = 1;
        requestData();
    }

    private void requestData() {
        SummerParameter params = new SummerParameter();
        params.putLog("请求下载数据");
        EasyHttp.get(context, url, RequestBook.class, params,
                new RequestCallback<RequestBook>() {

                    @Override
                    public void done(RequestBook t) {
                        Logs.i("xia", t + ",,");
                        if (null != t && null != t.getData()) {
                            List<BookBean> infos = t.getData();
                            Logs.i("xia", infos + ",,");
                            if (null != infos && 0 < infos.size()) {
                                if (null != mBooks && !TextUtils.isEmpty(lasttime)) {
                                    mBooks.addAll(infos);
                                } else {
                                    mBooks = infos;
                                }
                                mService.insert(CommonDBType.DOWNLOAD_DATA, mBooks);
                                mFreshing = false;
                                notifyDatas(mBooks);
                            } else {
                                if (pageNum > 1) {
                                    SUtils.makeToast(context, "没有更多数据了");
                                }
                            }
                        } else {
                            notifyDatas(mBooks);
                        }
                        if (pageNum > 1) {
                            mGridView.finishRefreshLoadMore();
                        } else {
                            mGridView.finishRefresh();
                        }
                        pageNum++;
                    }
                });
    }

    public void onListLoad() {
        requestData();
    }

    private void notifyDatas(List<BookBean> mBooks) {
        Logs.i("xia", mBooks + ",,,");
        if (mBooks != null && mBooks.size() > 0) {
            mAdapter.notifyDataChanged(mBooks);
        } else {
            mAdapter.notifyDataChanged(new ArrayList<BookBean>());
//            empty.setVisibility(View.VISIBLE);
        }
    }

    public void onRefresh() {
        mBooks = null;
        lasttime = "";
        pageNum = 1;
        requestData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        }

    }

}
