package com.summer.demo.ui.view.commonfragment.recyclerview;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.summer.demo.R;
import com.summer.demo.adapter.CommonAdapter;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.recycle.SmartRecyclerView;
import com.summer.helper.utils.Logs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: ListView
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/17 11:16
 */
public class GridRecyclerFragment extends BaseFragment {
    @BindView(R.id.nv_container)
    SmartRecyclerView nvContainer;

    CommonAdapter commonAdapter;

    @Override
    protected void initView(View view) {
        //设置为List样式
        nvContainer.setGridView(3);
        commonAdapter = new CommonAdapter(context);
        nvContainer.setAdapter(commonAdapter);
        //开启自动加载功能（非必须）
        nvContainer.setEnableAutoLoadMore(false);
        nvContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {

                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> datas = new ArrayList<>();
                        for(int i = 0;i < 30;i++){
                            datas.add("萍水相逢萍水散，各自天涯各自安。");
                        }
                        Logs.i("-----");
                        commonAdapter.notifyDataChanged(datas);
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 2000);
            }
        });
        nvContainer.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (commonAdapter.getItemCount() > 30) {
                            Toast.makeText(context, "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {

                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 2000);
            }
        });

        //触发自动刷新
        nvContainer.autoRefresh();
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_list_rec;
    }

}
