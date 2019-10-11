package com.summer.demo.ui.view;

import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.view.customfragment.DanmakuFragment;
import com.summer.demo.ui.view.customfragment.GalleryFragment;

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
                //showFragment(new );
                break;
            case ElementPosition.POS:
                setTitle("Gallery");
                showFragment(new GalleryFragment());
                break;
            case ElementPosition.DANMAKU:
                setTitle("弹幕");
                showFragment(new DanmakuFragment());
                break;
        }
    }
}
