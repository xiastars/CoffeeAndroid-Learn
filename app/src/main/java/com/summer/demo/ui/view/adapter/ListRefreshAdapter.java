package com.summer.demo.ui.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.summer.demo.R;
import com.summer.helper.adapter.SRecycleMoreAdapter;
import com.summer.helper.listener.OnSimpleClickListener;

import butterknife.ButterKnife;

/**
 * 为首页的标题设置adapter
 *
 * @编者 xiastars
 */
public class ListRefreshAdapter extends SRecycleMoreAdapter {

    OnSimpleClickListener onSimpleClickListener;

    public ListRefreshAdapter(Context context) {
        super(context);
    }

    public ListRefreshAdapter(Context context,View headerView) {
        super(context,headerView);
    }

    public ListRefreshAdapter(Context context, OnSimpleClickListener onSimpleClickListener) {
        super(context);
        this.onSimpleClickListener = onSimpleClickListener;
    }

    @Override
    public RecyclerView.ViewHolder setContentView(ViewGroup parent) {
        return new ViewHolder(createHolderView(R.layout.item_usergroup, parent));
    }

    @Override
    public void bindContentView(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder vh = (ViewHolder) holder;
        /*
         * 根据奇偶数设置不同的颜色
         */
/*        if (position % 2 == 0) {
            vh.content.setBackgroundColor(getResourceColor(R.color.pink));
        } else {
            vh.content.setBackgroundColor(getResourceColor(R.color.green));
        }*/

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSimpleClickListener != null) {
                    onSimpleClickListener.onClick(position);
                }
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}