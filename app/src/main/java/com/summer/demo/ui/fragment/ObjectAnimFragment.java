package com.summer.demo.ui.fragment;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.base.BaseFragment;
import com.summer.demo.view.CanvasAnimView;
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
    @BindView(R.id.view_center)
    View viewCenter;
    @BindView(R.id.canvas_anim)
    CanvasAnimView canvasAnimView;
    @BindView(R.id.view_rotate)View vRotate;

    @Override
    protected void initView(View view) {
        showLeftAnim();
        SAnimUtils.rotationRepeat(vRotate);
        cirlce();
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


        }, 1000);


    }

    private void cirlce(){
        canvasAnimView.postDelayed(new Runnable() {
            @Override
            public void run() {
                canvasAnimView.setIndex();
                cirlce();
            }
        },50);
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

    private void hideTop() {
        SAnimUtils.fromTopMoveToHide(viewTop, SUtils.getDip(context, 100), new OnAnimEndListener() {
            @Override
            public void onEnd() {
                showRight();
            }

        });
    }

    private void showRight(){
        SAnimUtils.fromRightToShow(viewRight, SUtils.getDip(context, 100), new OnAnimEndListener() {
            @Override
            public void onEnd() {
                hideRight();
            }

        });
    }

    /**
     * 在右侧，从出现到隐藏
     */
    private void hideRight(){
        SAnimUtils.fromRightToHide(viewRight, SUtils.getDip(context, 100), new OnAnimEndListener() {
            @Override
            public void onEnd() {
                showBottom();
            }

        });
    }

    private void showBottom(){
        SAnimUtils.fromBottomToShow(viewBottom, SUtils.getDip(context, 100), new OnAnimEndListener() {
            @Override
            public void onEnd() {
                hideBottom();
            }

        });
    }

    private void hideBottom(){
        SAnimUtils.fromBottomToHide(viewBottom, SUtils.getDip(context, 100), new OnAnimEndListener() {
            @Override
            public void onEnd() {
                showLeftAnim();
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
