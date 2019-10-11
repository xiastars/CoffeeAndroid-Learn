package com.summer.demo.ui.view.customfragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.bean.DanmakuInfo;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.view.SimpleDanmakuView;
import com.summer.helper.utils.Logs;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/11 10:26
 */
public class DanmakuFragment extends BaseFragment {
    @BindView(R.id.rl_danmaku)
    SimpleDanmakuView rlDanmaku;

    @Override
    protected void initView(View view) {
        List<DanmakuInfo> danmakuInfos = new ArrayList<>();
        for(int i = 0;i < 100;i++){
            DanmakuInfo danmakuInfo = new DanmakuInfo();
            danmakuInfo.setImg("https://img02.sogoucdn.com/app/a/100520021/34dc6e45a0908f6d5554ab655748bafb");
            danmakuInfo.setName("丁香空结雨中愁");
            danmakuInfos.add(danmakuInfo);
        }
        rlDanmaku.addDatas(danmakuInfos,0);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_danmaku;
    }

    @Override
    public void onStop() {
        super.onStop();
        rlDanmaku.stopPlay();
        Logs.i("停止0--");
    }
}
