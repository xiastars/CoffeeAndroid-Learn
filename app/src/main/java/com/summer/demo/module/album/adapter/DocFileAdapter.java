package com.summer.demo.module.album.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.album.listener.OnItemClickListener;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.helper.adapter.SRecycleAdapter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 所有的文档
 *
 * @author xiastars
 */
public class DocFileAdapter extends SRecycleAdapter {

    private Context mContext;
    private ArrayList<ImageItem> dataList;
    int selectPos = -1;

    public DocFileAdapter(Context c) {
        super(c);
        mContext = c;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void notifyDatas(ArrayList<ImageItem> dataList) {
        this.dataList = dataList;
        Logs.i("-------" + dataList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doc_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        String path;
        if (dataList != null && dataList.size() > position)
            path = dataList.get(position).imagePath;
        else
            path = "camera_default";
        final ImageItem item = dataList.get(position);
        viewHolder.tvName.setText(item.getName());
        if (path.endsWith("doc") || path.endsWith("docx")) {
            SUtils.setPicResource(viewHolder.tvType, R.drawable.wrod);
        } else if (path.endsWith("ppt") || path.endsWith("pptx")) {
            SUtils.setPicResource(viewHolder.tvType, R.drawable.ppt);
        } else if (path.endsWith("pdf")) {
            SUtils.setPicResource(viewHolder.tvType, R.drawable.pdf);
        }
        Logs.i("imagePath:" + item.getImagePath());
        SUtils.setPicResource(viewHolder.choosedbt, R.drawable.watermark_select_icon);
        viewHolder.rlParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPos = position;
                mOnItemClickListener.onItemClick(null, position, true, viewHolder.choosedbt);
                notifyDataSetChanged();
            }
        });
        if (selectPos == position) {
            viewHolder.choosedbt.setVisibility(View.VISIBLE);
        } else {
            viewHolder.choosedbt.setVisibility(View.GONE);
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_type)
        ImageView tvType;
        @BindView(R.id.rl_parent)
        ConstraintLayout rlParent;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.choosedbt)
        ImageView choosedbt;
        @BindView(R.id.rl_select)
        RelativeLayout rlSelect;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
