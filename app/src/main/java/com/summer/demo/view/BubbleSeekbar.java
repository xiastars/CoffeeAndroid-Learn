package com.summer.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/8/11 17:54
 */
public class BubbleSeekbar extends BubbleDragLayer {
    View vieTop;
    View viewBottom;
    LinearLayout llBubble;

    public BubbleSeekbar(Context context) {
        super(context);
        init();
    }
    public BubbleSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        Logs.i("---------------");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_bubble_seekbar,null);
        addView(view);
        vieTop = view.findViewById(R.id.view_top);
        viewBottom = view.findViewById(R.id.view_bottom);
        llBubble = view.findViewById(R.id.ll_bubble);
        BubbleDragView bubbleDragView = new BubbleDragView(getContext());
        bubbleDragView.setLayoutPosition(100,SUtils.getDip(getContext(),50), SUtils.getDip(getContext(),40),SUtils.getDip(getContext(),40));
        bubbleDragView.setBackground(getResources().getDrawable(R.drawable.so_red));
        addView(bubbleDragView);
        setmDragItem(bubbleDragView);

    }

    @Override
    protected void onMoveX(float x) {
        super.onMoveX(x);
        Logs.i("x:::"+x);
        viewBottom.getLayoutParams().width = getWidth()-(int) x - SUtils.getDip(getContext(),10);
        viewBottom.requestLayout();
        viewBottom.invalidate();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llBubble.getLayoutParams();
        params.leftMargin = (int) x - SUtils.getDip(getContext(),40);
        llBubble.requestLayout();
        llBubble.invalidate();
    }
}
