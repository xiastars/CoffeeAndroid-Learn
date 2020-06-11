package com.summer.demo.ui.mine.release;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.helper.adapter.SRecycleAdapter;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiastars on 2017/8/7.
 */

public class ReleaseAlbumAdapter extends SRecycleAdapter {

    OnSimpleClickListener listener;

    public ReleaseAlbumAdapter(Context context, OnSimpleClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_release, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder hd = (ViewHolder) holder;
        if (position == items.size()) {
            hd.ima.setBackgroundResource(R.drawable.so_greyf1_5);
            SUtils.setPicResource(hd.ima, R.drawable.trans);
            hd.ima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(-1);

                }
            });
            hd.imaDelete.setVisibility(View.GONE);
            hd.ivAdd.setVisibility(View.VISIBLE);
        } else {
            ImageItem info = (ImageItem) items.get(position);
            SUtils.setPic(hd.ima, info.getImagePath());
            hd.ivAdd.setVisibility(View.GONE);
            hd.imaDelete.setVisibility(View.VISIBLE);
            hd.imaDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < items.size()) {
                        items.remove(position);
                    }
                    if (listener != null) {
                        listener.onClick(position);
                    }
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items != null ? (items.size() == 9 ? 9 : items.size() + 1) : 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ima)
        ImageView ima;
        @BindView(R.id.ima_delete)
        ImageView imaDelete;
        @BindView(R.id.iv_add)
        ImageView ivAdd;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            int width = (SUtils.screenWidth - SUtils.getDip(context, 40)) / 3;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
            ima.setLayoutParams(layoutParams);
        }

    }
}
