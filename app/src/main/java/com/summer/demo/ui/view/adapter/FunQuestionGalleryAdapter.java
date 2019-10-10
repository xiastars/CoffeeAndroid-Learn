package com.summer.demo.ui.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.bean.BookBean;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.RoundAngleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunQuestionGalleryAdapter extends GalleryAdapter {

    List<BookBean> mLibraryInfos;

    public FunQuestionGalleryAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCreateViewHolderId() {
        return R.layout.item_fun_question;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final ViewHolder vh = (ViewHolder) holder;
        SUtils.setPicWithHolder(vh.ivNav, "http://b.hiphotos.baidu.com/image/pic/item/908fa0ec08fa513db777cf78376d55fbb3fbd9b3.jpg",
                 R.drawable.default_icon_triangle);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemRealCount() {
        return mLibraryInfos==null ? 0 : mLibraryInfos.size();
    }

    public void notifyDataSetChanged(List<?> infos){
        mLibraryInfos = (List<BookBean>) infos;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_nav)
        RoundAngleImageView ivNav;
        @BindView(R.id.ll_parent)
        RelativeLayout llParent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}