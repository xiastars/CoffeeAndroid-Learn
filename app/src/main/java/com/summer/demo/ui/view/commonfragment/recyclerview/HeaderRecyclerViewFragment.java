package com.summer.demo.ui.view.commonfragment.recyclerview;


import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.ui.view.adapter.ListRefreshAdapter;
import com.summer.helper.recycle.SmartRecyclerView;
import com.summer.helper.utils.Logs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Description: 带了头部栏的RecyclerView
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/6/16 14:40
 */
public class HeaderRecyclerViewFragment extends BaseFragment {
    @BindView(R.id.nv_container)
    SmartRecyclerView nvContainer;

    ListRefreshAdapter commonAdapter;
    ViewHolder vh;

    @Override
    protected void initView(View view) {
        //设置为List样式
        nvContainer.setList();
        View headerView = LayoutInflater.from(context).inflate(R.layout.view_recycler_header, null);
        //通过Holder方式来处理HeaderView的逻辑
        vh = new ViewHolder(headerView);
        commonAdapter = new ListRefreshAdapter(context, headerView);
        nvContainer.setAdapter(commonAdapter);
        //开启自动加载功能（非必须）
        nvContainer.setEnableAutoLoadMore(true);
        nvContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                Logs.i("---------");
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> datas = new ArrayList<>();
                        for (int i = 0; i < 30; i++) {
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
                            List<String> datas = new ArrayList<>();
                            for (int i = 0; i < 30; i++) {
                                datas.add("萍水相逢萍水散，各自天涯各自安。");
                            }
                            commonAdapter.notifyDataChanged(datas);
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

    static class ViewHolder {
        @BindView(R.id.iv_nav)
        ImageView ivNav;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.imgArrow)
        ImageView imgArrow;
        @BindView(R.id.track_line)
        View trackLine;
        @BindView(R.id.btn_more)
        TextView btnMore;
        @BindView(R.id.rl_parent)
        RelativeLayout rlParent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
