package com.summer.demo.module.album.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

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
 * 这个是显示一个文件夹里面的所有图片时用的适配器
 *
 * @author zhangqian
 */
public class AlbumGridViewAdapter extends SRecycleAdapter {
    final String TAG = getClass().getSimpleName();
    private Context mContext;
    private ArrayList<ImageItem> dataList;
    private ArrayList<ImageItem> selectedDataList;

    public AlbumGridViewAdapter(Context c, ArrayList<ImageItem> dataList, ArrayList<ImageItem> selectedDataList) {
        super(c);
        mContext = c;
        this.dataList = dataList;
        this.selectedDataList = selectedDataList;
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void notifyDatas(ArrayList<ImageItem> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plugin_camera_select_imageview, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String path;
        if (dataList != null && dataList.size() > position)
            path = dataList.get(position).imagePath;
        else
            path = "camera_default";
        final ImageItem item = dataList.get(position);
        if (path.contains("camera_default")) {
            viewHolder.imageView.setImageResource(R.drawable.plugin_camera_no_pictures);
        } else {
            Logs.i(item.thumbnailPath+",,"+item.getImagePath());
            SUtils.setPic(viewHolder.imageView, item.thumbnailPath != null ? item.thumbnailPath : item.getImagePath());
        }

        viewHolder.toggleButton.setTag(position);
        viewHolder.choosedbt.setTag(position);
        viewHolder.toggleButton.setOnClickListener(new ToggleClickListener(viewHolder.choosedbt));
        if (selectedDataList.contains(dataList.get(position))) {
            viewHolder.toggleButton.setChecked(true);
            viewHolder.choosedbt.setVisibility(View.VISIBLE);
        } else {
            viewHolder.toggleButton.setChecked(false);
            viewHolder.choosedbt.setVisibility(View.GONE);
        }
    }

    private class ToggleClickListener implements OnClickListener {
        ImageView chooseBt;

        public ToggleClickListener(ImageView choosebt) {
            this.chooseBt = choosebt;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof ToggleButton) {
                ToggleButton toggleButton = (ToggleButton) view;
                int position = (Integer) toggleButton.getTag();
                if (dataList != null && mOnItemClickListener != null && position < dataList.size()) {
                    mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(), chooseBt);
                }
            }
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener l) {
        mOnItemClickListener = l;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_view)
        ImageView imageView;
        @BindView(R.id.toggle_button)
        ToggleButton toggleButton;
        @BindView(R.id.choosedbt)
        ImageView choosedbt;
        @BindView(R.id.toggle)
        RelativeLayout toggle;
        @BindView(R.id.rl_parent)
        RelativeLayout rlParent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.width = (SUtils.screenWidth - SUtils.getDip(mContext, 12)) / 3;
            params.height = params.width;
        }
    }
}
