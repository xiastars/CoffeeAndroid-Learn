package com.summer.demo.module.album.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.album.AlbumActivity;
import com.summer.demo.module.album.ShowAllPhotoActivity;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.helper.adapter.SRecycleMoreAdapter;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.SUtils;

import java.util.ArrayList;

/**
 * Created by xiaqiliang on 2017/3/22.
 */
public class FolersAdapter extends SRecycleMoreAdapter {
    ArrayList<ImageItem> tempSelectBitmap;

    public FolersAdapter(Context context, ArrayList<ImageItem> tempSelectBitmap) {
        super(context);
        this.tempSelectBitmap = tempSelectBitmap;
    }

    @Override
    public RecyclerView.ViewHolder setContentView(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_area_exclusive, parent, false);
        return new TabViewHolder(view);
    }

    @Override
    public void bindContentView(RecyclerView.ViewHolder holder, final int position) {
        TabViewHolder hd = (TabViewHolder) holder;
        hd.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AlbumActivity)context).hideFolderView();
                ShowAllPhotoActivity.dataList = (ArrayList<ImageItem>) AlbumActivity.contentList.get(position).imageList;
                Intent intent = new Intent();
                String folderName = AlbumActivity.contentList.get(position).bucketName;
                intent.putExtra("folderName", folderName);
                intent.putExtra(JumpTo.TYPE_OBJECT, tempSelectBitmap);
                intent.setClass(context, ShowAllPhotoActivity.class);
                ((Activity) context).startActivityForResult(intent, 5);
            }
        });
        String path;
        if (AlbumActivity.contentList.get(position).imageList != null) {
            path = AlbumActivity.contentList.get(position).imageList.get(0).imagePath;
            hd.tvTitle.setText(AlbumActivity.contentList.get(position).bucketName);
            hd.tvJoin.setText("" + AlbumActivity.contentList.get(position).count);

        } else {
            path = "android_hybrid_camera_default";
        }
        if (path.contains("android_hybrid_camera_default"))
            hd.ivIcon.setImageResource(R.drawable.plugin_camera_no_pictures);
        else {
            final ImageItem item = AlbumActivity.contentList.get(position).imageList.get(0);
            SUtils.setPicWithHolder(hd.ivIcon, item.imagePath, R.drawable.default_icon_triangle);
        }
    }

    private class TabViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivIcon;
        private TextView tvTitle, tvJoin;
        private LinearLayout rlParent;

        public TabViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_nav);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvJoin = (TextView) itemView.findViewById(R.id.tv_join);
            rlParent = (LinearLayout) itemView.findViewById(R.id.rl_parent);
        }
    }

}
