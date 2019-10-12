package com.summer.demo.ui.module.comment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.bean.UserInfo;
import com.summer.demo.dialog.BaseTipsDialog;
import com.summer.helper.adapter.SRecycleAdapter;
import com.summer.helper.adapter.SRecycleMoreAdapter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.STimeUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.RoundAngleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatAdapter extends SRecycleAdapter {

    private static final int VIEW_TYPE_SELF = SRecycleMoreAdapter.ViewType.INSERT_TYPE2;//自己
    private static final int VIEW_TYPE_OTHER = SRecycleMoreAdapter.ViewType.INSERT_TYPE3;//别人

    UserInfo selfInfo;
    UserInfo talkerInfo;

    public ChatAdapter(Context context, UserInfo selfInfo, UserInfo talkerInfo) {
        super(context);
        this.selfInfo = selfInfo;
        this.talkerInfo = talkerInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SELF) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_self, parent, false);
            return new SelfHolder(view);
        } else if (viewType == VIEW_TYPE_OTHER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_chat_other, parent, false);
            return new OtherHolder(view);
        }

        return super.onCreateViewHolder(parent, viewType);
    }

    public void notifyDataChanged(List<?> comments) {
        this.items = comments;
        List<ChatInfo> infos = (List<ChatInfo>) items;
        long time = 0;
        for (ChatInfo info : infos) {
            long curTime = info.getSend_time();
            if (curTime - time > 60 * 2) {
                info.setShowTime(true);
            }
            Logs.i("()" + (curTime - time) + ",,," + info.isShowTime());
            time = curTime;
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatInfo info = (ChatInfo) items.get(position);
        if (holder instanceof SelfHolder) {
            SelfHolder hd = (SelfHolder) holder;
            setUserInfo(hd.ivAvatar, selfInfo);
            setContentView(hd.tvContent, info);
            setSendStatus(hd.pbLoading, hd.ivError, info);
            showTimeView(hd.tvTime, info);
        } else if (holder instanceof OtherHolder) {
            OtherHolder hd = (OtherHolder) holder;
            setUserInfo(hd.ivAvatar, talkerInfo);
            setContentView(hd.tvContent, info);
            setSendStatus(hd.pbLoading, hd.ivError, info);
            showTimeView(hd.tvTime, info);
        }
    }

    private void showTimeView(TextView tvTime, ChatInfo info) {
        if (info.isShowTime()) {
            tvTime.setVisibility(View.VISIBLE);
            String time = STimeUtils.parseChatTime(info.getSend_time());
            tvTime.setText(time);
        } else {
            tvTime.setVisibility(View.GONE);
        }
    }

    private void setSendStatus(ProgressBar pbLoading, ImageView ivError, final ChatInfo info) {
        int requestCode = info.getRequestCode();
        if (requestCode > 0) {
            pbLoading.setVisibility(View.VISIBLE);
        } else {
            pbLoading.setVisibility(View.INVISIBLE);
        }
        Logs.i("isEndError" + info.isSendError());
        ivError.setVisibility(info.isSendError() ? View.VISIBLE : View.GONE);
        ivError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseTipsDialog baseTipsDialog = new BaseTipsDialog(context, "重发该消息", new BaseTipsDialog.DialogAfterClickListener() {
                    @Override
                    public void onSure() {
                        items.remove(info);
                        //((ChatActivity) context).sendChatMsg(info.getText());
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                baseTipsDialog.hideTitle();
                baseTipsDialog.setOkContent("重发");
                baseTipsDialog.show();
            }
        });
    }

    private void setContentView(TextView tvContent, ChatInfo info) {
        STextUtils.setNotEmptText(tvContent, info.getText());
    }

    private void setUserInfo(RoundAngleImageView ivAvatar, final UserInfo info) {
        SUtils.setPic(ivAvatar, info.getAvatar());
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof ChatInfo) {
            ChatInfo info = (ChatInfo) item;
            UserInfo userInfo = info.getUser();
            if (userInfo.getId() != null && userInfo.getId().equals(selfInfo.getId())) {
                return VIEW_TYPE_SELF;
            } else {
                return VIEW_TYPE_OTHER;
            }
        }
        return SRecycleMoreAdapter.ViewType.TYPE_CONTENT;
    }

    static class SelfHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        RoundAngleImageView ivAvatar;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.pb_loading)
        ProgressBar pbLoading;
        @BindView(R.id.iv_error)
        ImageView ivError;
        @BindView(R.id.tv_time)
        TextView tvTime;

        SelfHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class OtherHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        RoundAngleImageView ivAvatar;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.pb_loading)
        ProgressBar pbLoading;
        @BindView(R.id.iv_error)
        ImageView ivError;
        @BindView(R.id.rl_left)
        RelativeLayout rlLeft;
        @BindView(R.id.tv_time)
        TextView tvTime;

        OtherHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
