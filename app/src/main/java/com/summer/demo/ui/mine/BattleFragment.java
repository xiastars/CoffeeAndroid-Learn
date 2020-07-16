package com.summer.demo.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.view.VerticalProgressBar;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.STextUtils;
import com.summer.helper.utils.STimeUtils;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;

/**
 * @Description: 比赛
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/7/3 17:04
 */
public class BattleFragment extends BaseFragment {
    @BindView(R.id.iv_top_cover)
    ImageView ivTopCover;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.iv_other_avatar)
    ImageView ivOtherAvatar;
    @BindView(R.id.tv_other_name)
    TextView tvOtherName;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.iv_time)
    ImageView ivTime;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rl_time)
    RelativeLayout rlTime;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.pb_left)
    VerticalProgressBar pbLeft;
    @BindView(R.id.tv_mine_score)
    TextView tvMineScore;
    @BindView(R.id.ll_progress)
    LinearLayout llProgress;
    @BindView(R.id.pb_right)
    VerticalProgressBar pbRight;
    @BindView(R.id.tv_other_score)
    TextView tvOtherScore;
    @BindView(R.id.ll_right_progress)
    LinearLayout llRightProgress;
    @BindView(R.id.iv_finish)
    ImageView ivFinish;
    @BindView(R.id.tv_rule)TextView tvRule;


    public static BattleFragment create(BattleInfo battleInfo){
        BattleFragment battleFragment = new BattleFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("info",battleInfo);
        battleFragment.setArguments(bundle);
        return battleFragment;
    }

    @Override
    protected void initView(View view) {
        BattleInfo info = (BattleInfo) getArguments().get("info");
        STextUtils.setNotEmptText(tvName,info.getLeftName());
        STextUtils.setNotEmptText(tvOtherName,info.getRightName());
        SUtils.setPicResource(ivAvatar,info.getLeftAvatar());
        SUtils.setPicResource(ivOtherAvatar,info. getRightAvatar());
        pbLeft.setMax(100);
        tvTitle.setText(info.getLeftShare()+" vs "+info.getRightShare());
        tvRule.setText("规则:以起始日起，一月内，涨幅高者胜。");
        int progress = (int) ((info.getLeftShareValue() - info.getLeftSharePreValue())/info.getLeftSharePreValue() * 100);
        pbLeft.setProgress(progress);
        String time = info.getFromTime();
        Logs.i("time:"+time);
        long timeL = STimeUtils.parseStringDate("yyyy-MM-dd",time);
        Logs.i("nextTime:"+STimeUtils.getDayWithFormat("yyyy-MM-dd",timeL));
        long nextTime = STimeUtils.getCurYearAndMonthByTime(timeL);
        Logs.i("nextTime:"+STimeUtils.getDayWithFormat("yyyy-MM-dd",nextTime));
        int day = (int) ((nextTime - System.currentTimeMillis())/1000/60/60/24);
        tvTime.setText(20+"");
        pbRight.setMax(100);
        tvMineScore.setText(progress+"");

        int progressRight = (int) ((info.getRightShareValue() - info.getRightSharePreValue())/info.getRightSharePreValue() * 100);
        pbRight.setProgress(progressRight);
        tvOtherScore.setText(progressRight+"");
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_battle;
    }
}
