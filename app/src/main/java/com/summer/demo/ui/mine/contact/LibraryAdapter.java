package com.summer.demo.ui.mine.contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.ui.mine.contact.bean.LibraryInfo;
import com.summer.helper.adapter.SRecycleMoreAdapter;
import com.summer.helper.utils.SUtils;

/**
 * Created by xiaqiliang on 2017/3/22.
 */
public class LibraryAdapter extends SRecycleMoreAdapter {
    OnReturnLibraryListener listener;

    public LibraryAdapter(Context context, OnReturnLibraryListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder setContentView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ask_library, parent, false);
        return new TabViewHolder(view);
    }

    @Override
    public void bindContentView(RecyclerView.ViewHolder holder, final int position) {
        TabViewHolder hd = (TabViewHolder) holder;
        final LibraryInfo info = (LibraryInfo) items.get(position);

        SUtils.setNotEmptText( hd.tvTitle,info.getRepositoryName());
        SUtils.setPicWithHolder(hd.ivIcon,info.getRepositoryImg(),R.drawable.default_icon_triangle);
        hd.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(info,position);
                }
            }
        });
    }

    private class TabViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivIcon;
        private TextView tvTitle;
        private LinearLayout rlParent;

        public TabViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_nav);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            rlParent = (LinearLayout) itemView.findViewById(R.id.ll_parent);
        }
    }

}
