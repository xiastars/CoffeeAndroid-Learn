package com.summer.demo.ui.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.ui.view.commonfragment.viewpager.bean.HsizeInfo;
import com.summer.helper.utils.SUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeightSizeAdapter extends GalleryAdapter {

    List<HsizeInfo> mLibraryInfos;


    public HeightSizeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCreateViewHolderId() {
        return R.layout.item_height_size;
    }

    @Override
    public ViewHolder onCreateItemViewHolder(ViewGroup parent, View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        final ViewHolder vh = (ViewHolder) holder;
        HsizeInfo info = mLibraryInfos.get(position);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) vh.viewSize.getLayoutParams();
        if(info.isBig()){
            if(info.getSize()% 10 == 0){
                vh.tvSize.setText(info.getSize()/10+"");
            }else{
                vh.tvSize.setText("");
            }

            params.height = SUtils.getDip(mContext,45);
        }else{
            vh.tvSize.setText("");
            params.height = SUtils.getDip(mContext,30);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemRealCount() {
        return mLibraryInfos == null ? 0 : mLibraryInfos.size();
    }

    public void notifyDataSetChanged(List<?> infos) {
        mLibraryInfos = (List<HsizeInfo>) infos;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_size)
        View viewSize;
        @BindView(R.id.tv_size)
        TextView tvSize;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}