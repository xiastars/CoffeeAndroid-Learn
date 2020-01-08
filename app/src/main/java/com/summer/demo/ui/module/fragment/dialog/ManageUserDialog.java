package com.summer.demo.ui.module.fragment.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.utils.CUtils;
import com.summer.helper.dialog.BaseBottomDialog;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.STimeUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.NRecycleView;
import com.summer.helper.view.RoundAngleImageView;

import java.util.List;

import butterknife.BindView;

/**
 * 用户管理
 */
public class ManageUserDialog extends BaseBottomDialog {
    @BindView(R.id.nv_share)
    NRecycleView nvShare;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    OnSimpleClickListener listener;
    List<GroupManageInfo> infos;
    @BindView(R.id.iv_avatar)
    RoundAngleImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_join_time)
    TextView tvJoinTime;

    public ManageUserDialog(@NonNull Context context, List<GroupManageInfo> infos, OnSimpleClickListener listener) {
        super(context);
        this.listener = listener;
        this.infos = infos;
    }

    @Override
    public int setContainerView() {
        return R.layout.dialog_user_manage;
    }

    @Override
    public void initView(View view) {
        GroupUserManageAdapter adapter = new GroupUserManageAdapter(context, new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                if (listener != null) {
                    listener.onClick(position);
                    cancelDialog();
                }
            }
        });
        SUtils.setPic(ivAvatar,"https://img9.doubanio.com/view/photo/m/public/p2579628515.webp");
        tvName.setText("半山人");
        nvShare.setList();
        nvShare.setAdapter(adapter);
        adapter.notifyDataChanged(infos);
        String timeContent = STimeUtils.getDayWithFormat("yyyy年MM月dd号加入");
        tvJoinTime.setText(timeContent);
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(-1);
                }
                cancelDialog();
                CUtils.onClick(context,"member_edit_cancel");
            }
        });
    }
}
