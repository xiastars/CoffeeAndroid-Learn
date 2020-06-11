package com.summer.demo.ui.mine;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.ui.SettingActivity;
import com.summer.demo.ui.main.BaseMainFragment;
import com.summer.demo.ui.mine.release.ReleaseTopicActivity;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.view.RoundAngleImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 个人页面
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 10:35
 */
public class MineFragment extends BaseMainFragment implements View.OnClickListener {
    @BindView(R.id.rl_goto_account)
    RelativeLayout rlGotoAccount;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_intro)
    TextView tvIntro;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.iv_avatar)
    RoundAngleImageView ivAvatar;
    @BindView(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @BindView(R.id.ll_edit)
    LinearLayout llEdit;
    @BindView(R.id.tv_real_status)
    TextView tvRealStatus;
    @BindView(R.id.tv_topic_count)
    TextView tvTopicCount;
    @BindView(R.id.ll_topic)
    LinearLayout llTopic;
    @BindView(R.id.tv_follow_count)
    TextView tvFollowCount;
    @BindView(R.id.ll_follow)
    LinearLayout llFollow;
    @BindView(R.id.tv_fan_count)
    TextView tvFanCount;
    @BindView(R.id.ll_fans)
    LinearLayout llFans;
    @BindView(R.id.ll_user_count)
    LinearLayout llUserCount;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.tv_mine_collect)
    TextView tvMineCollect;
    @BindView(R.id.rl_collect)
    RelativeLayout rlCollect;
    @BindView(R.id.tv_mine_ask)
    TextView tvMineAsk;
    @BindView(R.id.rl_ask)
    RelativeLayout rlAsk;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.rl_cash)
    RelativeLayout rlCash;
    @BindView(R.id.line4)
    View line4;
    @BindView(R.id.about_version)
    TextView aboutVersion;
    @BindView(R.id.rl_help)
    RelativeLayout rlHelp;

    @Override
    protected void initView(View view) {

    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_mine;
    }

    @OnClick({R.id.iv_setting,R.id.rl_collect})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_collect:
                JumpTo.getInstance().commonJump(context, ReleaseTopicActivity.class);
                break;
            case R.id.iv_setting:
                JumpTo.getInstance().commonJump(context, SettingActivity.class);
                break;
        }
    }
}
