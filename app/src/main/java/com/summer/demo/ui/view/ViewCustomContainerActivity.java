package com.summer.demo.ui.view;

import com.summer.demo.R;
import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.mine.BattleFragment;
import com.summer.demo.ui.mine.BattleInfo;
import com.summer.demo.ui.view.customfragment.CalendarFragment;
import com.summer.demo.ui.view.customfragment.DanmakuFragment;
import com.summer.demo.ui.view.customfragment.GalleryFragment;
import com.summer.demo.ui.view.customfragment.ListItemFragment;

/**
 * @Description: Fragment容器
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 11:44
 */
public class ViewCustomContainerActivity extends FragmentContainerActivity {

    @Override
    protected void showViews(int type) {
        switch (type) {
            case ElementPosition.POS_ITEM:
                setTitle("ITEM收集");
                showFragment(new ListItemFragment());
                break;
            case ElementPosition.POS:
                setTitle("Gallery");
                showFragment(new GalleryFragment());
                break;
            case ElementPosition.DANMAKU:
                setTitle("弹幕");
                showFragment(new DanmakuFragment());
                break;
            case ElementPosition.CAL:
                setTitle("CAL");
                //  showFragment(new CalCuWEbFragment());
                break;
            case ElementPosition.CALENDAR:
                setTitle("日历");
                showFragment(new CalendarFragment());
                break;
            case ElementPosition.BYD:
                setTitle("巅峰对决");
                BattleInfo battleInfo = new BattleInfo();
                battleInfo.setFromTime("20200623");
                battleInfo.setLeftAvatar(R.drawable.ic_ya);
                battleInfo.setLeftName("牙子");
                battleInfo.setLeftShare("比亚迪");
                battleInfo.setLeftSharePreValue(71.03f);
                battleInfo.setLeftShareValue(79.35f);
                battleInfo.setRightAvatar(R.drawable.ic_margin);
                battleInfo.setRightName("Martin");
                battleInfo.setRightShare("紫光国微");
                battleInfo.setRightSharePreValue(69.03f);
                battleInfo.setRightShareValue(96.83f);
                BattleFragment battleFragment = BattleFragment.create(battleInfo);
                showFragment(battleFragment);
                break;
        }
    }
}
