package com.summer.demo.ui.mine.release;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.ui.mine.release.bean.GroupDetInfo;
import com.summer.demo.ui.mine.release.bean.GroupUserInfo;
import com.summer.demo.utils.CUtils;

public class GroupHelper {

    /**
     * 用户是否是星主
     *
     * @param info
     * @return
     */
    public static boolean isSelfMaster(GroupDetInfo info) {
        if (info == null) {
            return false;
        }
        GroupUserInfo userInfo = info.getCurrent_user();
        if (userInfo == null) {
            return false;
        }
        int identity =Integer.parseInt(userInfo.getMember_info().getIdentity());
        if (identity == 2) {
            return true;
        }
        return false;
    }

    public static void setAudioTime(int time, TextView tvTime) {
        tvTime.setText(time + "\"");
    }


    /**
     * 获取星球内水印状态
     *
     * @param groupDetInfo
     * @return
     */
    public static int isSupportWaterMark(GroupDetInfo groupDetInfo) {
        if (groupDetInfo == null) {
            return 0;
        }
        GroupUserInfo specificInfo = groupDetInfo.getCurrent_user();
        if (specificInfo == null) {
            return 0;
        }
        int watermark = specificInfo.getMember_info().getWatermark();
        WaterMarkHelper.setSupportWaterMark(watermark, groupDetInfo.getId());
        return watermark;
    }

    /**
     * 显示续费提醒
     *
     * @param detailInfo
     */
    public static void showRenewView(final GroupDetInfo detailInfo, RelativeLayout rlRenew, TextView tvRenewDetail, TextView tvRenew) {
        if (detailInfo == null) {
            return;
        }
        final Context context = rlRenew.getContext();
        GroupUserInfo specificInfo = detailInfo.getCurrent_user();
        if (specificInfo != null) {
            tvRenewDetail.setText("你的会员快到期了，仅剩" + 12 + "天");
            rlRenew.setVisibility(View.VISIBLE);
            if (!specificInfo.isIs_member()) {
                rlRenew.setVisibility(View.GONE);
            }
            tvRenew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CUtils.onClick(context, "other_renew");
                   // PayHelper payHelper = new PayHelper(context);
                    //payHelper.startPlayRenew(detailInfo.getId());
                }
            });
        }
    }

}
