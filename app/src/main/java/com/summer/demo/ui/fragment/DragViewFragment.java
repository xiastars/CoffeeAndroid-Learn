package com.summer.demo.ui.fragment;


import com.summer.demo.R;
import com.summer.demo.view.DragLayer;
import com.summer.demo.view.DragView;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;


/**
 * View的拖动示例
 */
public class DragViewFragment extends BaseSimpleFragment {
    DragLayer dragLayer;

    @Override
    protected void initView(){
        dragLayer = new DragLayer(context);
        llParent.addView(dragLayer);
        dragLayer.addBackgroundView();
        dragLayer.setmBackgroundImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1560148003785&di=964660715cfbc2f53f9042a023a35c40&imgtype=0&src=http%3A%2F%2Fimg0.sc115.com%2Fuploads%2Fallimg%2F101119%2F20101119143144255.jpg");
        creageBig();
        for(int i = 0;i < 10;i++){
            createLittle(i);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dragLayer.removeAllViews();
    }

    private void createLittle(int i) {
        DragView controlLayout = new DragView(context);
        controlLayout.setLayoutPosition(500,300+i*30,100,100);
        controlLayout.setDefalultIcon(R.drawable.so_redf8_oval);
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
                Logs.i("onDClick:");
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
        controlLayout.setDefalultIcon(R.drawable.drag);
        dragLayer.addView(controlLayout);
    }

}
