package com.summer.demo.ui.module.fragment.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.adapter.SRecycleAdapter;
import com.summer.helper.listener.OnSimpleClickListener;


public class GroupUserManageAdapter extends SRecycleAdapter {

    OnSimpleClickListener listener;

    public GroupUserManageAdapter(Context context, OnSimpleClickListener listener) {
        super(context);
        this.listener = listener;
        init();
    }


    public void init() {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group_user_manager, parent, false);
        return new TabViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TabViewHolder hd = (TabViewHolder) holder;
        final GroupManageInfo info = (GroupManageInfo) items.get(position);
       //hd.tvName.setText(info.getName());
        hd.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(info.getType());
            }
        });
        if (position == 1) {
            hd.tvName.setTextColor(Color.parseColor("#FF556A"));
        } else {
            hd.tvName.setTextColor(Color.parseColor("#007AFF"));
        }
        if (position == items.size() - 1) {
            hd.bottomLine.setVisibility(View.GONE);
        } else {
            hd.bottomLine.setVisibility(View.VISIBLE);
        }
    }/**/

    private class TabViewHolder extends RecyclerView.ViewHolder {

        private View bottomLine;
        private TextView tvName;
        private RelativeLayout llParent;

        public TabViewHolder(View itemView) {
            super(itemView);
            bottomLine = itemView.findViewById(R.id.bottom_line);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            llParent = (RelativeLayout) itemView.findViewById(R.id.ll_parent);
        }
    }

}