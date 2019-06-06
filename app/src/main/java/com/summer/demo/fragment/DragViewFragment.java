package com.summer.demo.fragment;


import android.view.GestureDetector;

import com.summer.demo.R;
import com.summer.demo.view.DragLayer;
import com.summer.demo.view.DragView;
import com.summer.helper.utils.SUtils;


/**
 * View的拖动示例
 */
public class DragViewFragment extends BaseFragment {
    DragLayer dragLayer;

    @Override
    protected void initView(){
        dragLayer = new DragLayer(context);
        llParent.addView(dragLayer);
        dragLayer.addBackgroundView();
        dragLayer.setBackgroundResource(R.drawable.background1);
        creageBig();
        for(int i = 0;i < 10;i++){
            createLittle();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dragLayer.removeAllViews();
    }

    private void createLittle() {
        DragView controlLayout = new DragView(context);
        controlLayout.setLayoutPosition(500,300,100,100);
        controlLayout.setDefalultIcon(R.drawable.so_grayca90);
        dragLayer.addView(controlLayout);
        controlLayout.setOnSingleClickListener(new DragView.SingleClickListener() {
            @Override
            public void onSClick() {
                SUtils.makeToast(context,"single click");
            }
        });
        controlLayout.setOnDoubleClickListener(new DragView.DoubleClickListener() {
            @Override
            public void onDClick() {
                SUtils.makeToast(context,"double click");
            }
        });
        controlLayout.setOnLongClickListener(new DragView.LongClickListener() {
            @Override
            public void longClick() {
                SUtils.makeToast(context,"long click");
            }
        });
    }

    private void creageBig(){
        DragView controlLayout = new DragView(context);
        controlLayout.setLayoutPosition(200,300,300,300);
        controlLayout.setDefalultIcon(R.drawable.xiehou04);
        dragLayer.addView(controlLayout);
    }

}
