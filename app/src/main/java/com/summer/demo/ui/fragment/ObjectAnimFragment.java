package com.summer.demo.ui.fragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.base.BaseFragment;
import com.summer.helper.listener.OnAnimEndListener;
import com.summer.helper.utils.SAnimUtils;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;

/**
 * @Description: 属性动画
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/6/10 12:10
 */
public class ObjectAnimFragment extends BaseFragment {

    @BindView(R.id.view_left)
    View viewLeft;
    @BindView(R.id.view_top)
    View viewTop;
    @BindView(R.id.view_right)
    View viewRight;
    @BindView(R.id.view_bottom)
    View viewBottom;

    @Override
    protected void initView(View view) {
        showLeftAnim();
    }

    private void showLeftAnim() {
        viewLeft.postDelayed(new Runnable() {
            @Override
            public void run() {
                SAnimUtils.fromLeftToShow(viewLeft, SUtils.getDip(context, 100), new OnAnimEndListener() {
                    @Override
                    public void onEnd() {
                        hideLeft();
                    }
                });
            }


        },1000);

    }

    private void hideLeft() {
        SAnimUtils.fromLeftToHide(viewLeft, SUtils.getDip(context, 100), new OnAnimEndListener() {
            @Override
            public void onEnd() {
                showTop();
            }

        });
    }

    private void showTop() {
        SAnimUtils.fromTopMoveToShow(viewTop, SUtils.getDip(context, 100), new OnAnimEndListener() {
            @Override
            public void onEnd() {
                hideTop();
            }

        });
    }

    private void hideTop(){
        SAnimUtils.fromTopMoveToHide(viewTop, SUtils.getDip(context, 100), new OnAnimEndListener() {
            @Override
            public void onEnd() {

            }

        });
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.ac_object_anim;
    }

}
