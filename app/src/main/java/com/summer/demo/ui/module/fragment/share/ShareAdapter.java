package com.summer.demo.ui.module.fragment.share;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.adapter.SRecycleAdapter;
import com.summer.helper.listener.OnSimpleClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareAdapter extends SRecycleAdapter {

    OnSimpleClickListener listener;
    boolean isAskStyle;

    public ShareAdapter(Context context, OnSimpleClickListener listener) {
        super(context);
        this.listener = listener;
        init();
    }


    public void init() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_share, parent, false);
        return new TabViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TabViewHolder hd = (TabViewHolder) holder;
        final ShareItemInfo info = (ShareItemInfo) items.get(position);

        if(isAskStyle){
            hd.ivBg.setBackgroundResource(info.getResId());
            //hd.ivNav.setVisibility(View.GONE);
            hd.ivBg.setBackgroundResource(R.drawable.so_white45);
        }else{
            hd.ivNav.setBackgroundResource(info.getResId());
            hd.ivNav.setVisibility(View.VISIBLE);
            hd.ivBg.setBackgroundResource(R.drawable.so_greyf1_45);
        }
        hd.tvName.setText(info.getName());
        hd.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(info.getType());
            }
        });
    }

    static class TabViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_bg)
        ImageView ivBg;
        @BindView(R.id.iv_nav)
        ImageView ivNav;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ll_parent)
        RelativeLayout llParent;

        TabViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}