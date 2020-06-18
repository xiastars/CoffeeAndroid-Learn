package com.summer.demo.ui.module.fragment.share;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.utils.CUtils;
import com.summer.helper.dialog.BaseBottomDialog;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.view.NRecycleView;

import java.util.List;

import butterknife.BindView;

/**
 * 分享弹窗
 */
public class ShareDialog extends BaseBottomDialog {
    @BindView(R.id.nv_share)
    NRecycleView nvShare;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    OnSimpleClickListener listener;
    List<ShareItemInfo> infos;
    boolean isAskStyle;

    public ShareDialog(@NonNull Context context, List<ShareItemInfo> infos, OnSimpleClickListener listener) {
        super(context);
        this.listener = listener;
        this.infos = infos;
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_share;
    }

    @Override
    public void initView(View view) {
        ShareAdapter adapter = new ShareAdapter(context, new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                if (listener != null) {
                    listener.onClick(position);
                    cancelDialog();
                }
            }
        });
        nvShare.setGridView(infos.size() >= 4 ? 4 : infos.size());
        nvShare.setAdapter(adapter);
        adapter.notifyDataChanged(infos);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog();
                CUtils.onClick(context,"topics_edit_cancel");
            }
        });
        if(isAskStyle){
            nvShare.setBackgroundColor(context.getResources().getColor(R.color.grey_f5));
            tvRight.setTextColor(context.getResources().getColor(R.color.blue_56));
        }
    }

    public boolean isAskStyle() {
        return isAskStyle;
    }

    public void setAskStyle(boolean askStyle) {
        isAskStyle = askStyle;
    }
}
