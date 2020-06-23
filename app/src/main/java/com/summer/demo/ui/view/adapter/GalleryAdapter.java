package com.summer.demo.ui.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summer.demo.ui.view.commonfragment.recyclerview.GalleryRecyclerView;

public abstract class GalleryAdapter extends RecyclerView.Adapter {

    private static final short TYPE_REAL = 0;
    private static final short TYPE_SPAN = 1;

    protected LayoutInflater mInflater;
    protected Context mContext;
    private int mGalleryWidth;
    private int mItemRawWidth;
    private GalleryRecyclerView galleryView;
    private boolean mStartCalculate;

    public GalleryAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        galleryView = (GalleryRecyclerView) recyclerView;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mGalleryWidth == 0) {
            mGalleryWidth = parent.getWidth();
        }
        View view = null;
        if (viewType == TYPE_REAL) {
            view = mInflater.inflate(getCreateViewHolderId(), parent, false);
            return onCreateItemViewHolder(parent, view, viewType);
        } else {
            view = new View(mContext);
            view.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup
                    .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return new RecyclerView.ViewHolder(view) {

            };
        }
    }

    @Override
    public final void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (mItemRawWidth == 0 && getItemViewType(position) == TYPE_REAL && !mStartCalculate) {
            mStartCalculate = true;
            holder.itemView.post(new Runnable() {
                @Override
                public void run() {
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
                    mItemRawWidth = params.width + params.leftMargin + params.rightMargin;
                    notifyItemChanged(0);
                    notifyItemChanged(getItemCount() - 1);
                }
            });
        }
        if (getItemViewType(position) == TYPE_REAL) {
            onBindItemViewHolder(holder, position - 1);
        } else {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            if (params.width <= 0 && mGalleryWidth > 0 && mItemRawWidth > 0) {
                params.width = (mGalleryWidth - mItemRawWidth) / 2;
                holder.itemView.setLayoutParams(params);
            }
            /*int w = (mGalleryWidth - mItemRawWidth)/2;
            if (params.width != w){
                params.width = w;
                holder.itemView.setLayoutParams(params);
            }*/
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 || position == getItemCount() - 1) ? TYPE_SPAN : TYPE_REAL;
    }

    @Override
    public final int getItemCount() {
        return getItemRealCount() + 2;
    }

    public abstract RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, View itemView, int viewType);

    public abstract void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * 获取item布局id
     */
    public abstract int getCreateViewHolderId();

    /**
     * 获取item数量
     */
    public abstract int getItemRealCount();
}