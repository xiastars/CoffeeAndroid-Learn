package com.summer.demo.ui.module;

import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.fragment.MyDialogFragment;
import com.summer.demo.ui.fragment.ObjectAnimFragment;
import com.summer.demo.ui.module.fragment.FrameAnimFragment;

/**
 * @Description: Fragment容器
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 11:44
 */
public class ModuleContainerActivity extends FragmentContainerActivity {

    @Override
    protected void showViews(int type) {
        switch (type){
            case 0:
                setTitle("帧动画");
                showFragment(new FrameAnimFragment());
                break;
            case 1:
                setTitle("属性动画");
                showFragment(new ObjectAnimFragment());
                break;
            case 2:
                setTitle("弹窗");
                showFragment(new MyDialogFragment());
                break;
        }
    }
}
