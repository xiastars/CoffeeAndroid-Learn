package com.summer.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.bean.ModuleInfo;
import com.summer.helper.adapter.SRecycleMoreAdapter;
import com.summer.helper.listener.OnSimpleClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 为首页的标题设置adapter
 *
 * @编者 xiastars
 */
public class CommonGridAdapter extends SRecycleMoreAdapter {

    OnSimpleClickListener onSimpleClickListener;

    public CommonGridAdapter(Context context) {
        super(context);
    }


    public CommonGridAdapter(Context context, OnSimpleClickListener onSimpleClickListener) {
        super(context);
        this.onSimpleClickListener = onSimpleClickListener;
    }

    @Override
    public RecyclerView.ViewHolder setContentView(ViewGroup parent) {
        return new ViewHolder(createHolderView(R.layout.item_grid, parent));
    }

    @Override
    public void bindContentView(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder vh = (ViewHolder) holder;
        ModuleInfo moduleInfo = (ModuleInfo) items.get(position);
        vh.content.setText(moduleInfo.getTitle());
        vh.ivBg.setBackgroundResource(moduleInfo.getRes());
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSimpleClickListener != null){
                    onSimpleClickListener.onClick(position);
                }
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView content;
        @BindView(R.id.iv_bg)
        ImageView ivBg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}