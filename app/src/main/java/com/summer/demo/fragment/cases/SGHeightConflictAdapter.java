package com.summer.demo.fragment.cases;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.utils.SUtils;
import com.summer.helper.utils.TipDialog;

/**
 * Created by summer on 2016年12月14日 16:24.
 */

public class SGHeightConflictAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    boolean deleteState = false;

    public SGHeightConflictAdapter(Context context) {
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder viewHodler = (MyViewHolder) holder;
        if (deleteState) {
            viewHodler.ivDelete.setVisibility(View.VISIBLE);
        } else {
            viewHodler.ivDelete.setVisibility(View.GONE);
        }
        viewHodler.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TipDialog dialog = new TipDialog(context, "删除该项", new TipDialog.DialogAfterClickListener() {
                    @Override
                    public void onSure() {

                    }

                    @Override
                    public void onCancel() {
                        cancelDeleteState(false);
                    }
                });
                dialog.show();

            }
        });
    }


    public void cancelDeleteState(boolean state) {
        this.deleteState = state;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout rlParentLayout;
        ImageView ivNav;
        RelativeLayout rlDownload;
        ImageView ivDelete;
        RelativeLayout rlIcon;
        TextView tvName;

        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_book_name);
            ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            ivNav = (ImageView) view.findViewById(R.id.iv_book_icon);
            rlDownload = (RelativeLayout) view.findViewById(R.id.rl_download);
            rlIcon = (RelativeLayout) view.findViewById(R.id.item_left_rl);
            SUtils.clickTransColor(rlIcon);
        }
    }

    public boolean getDeleteState() {
        return deleteState;
    }

    @Override
    public int getItemCount() {
        return 102;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_sgheight, parent,
                false));
        return holder;
    }

}