package com.summer.demo.ui.course.calculation;

import android.app.Activity;
import android.graphics.RectF;
import android.text.InputType;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.dialog.DialogModifyContent;
import com.summer.demo.listener.OnModifyContentListener;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description: 计算
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/6 17:44
 */
public class TrapezoidFragment extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.line_bottom)
    View lineBottom;
    @BindView(R.id.line_left)
    View lineLeft;
    @BindView(R.id.line_right)
    View lineRight;
    @BindView(R.id.line_top)
    View lineTop;

    RectF bottom, left, right, top;
    @BindView(R.id.tv_bottom)
    TextView tvBottom;
    @BindView(R.id.tv_left)
    TextView tvLeft;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_top)
    TextView tvTop;

    float scale = 0;

    @Override
    protected void initView(View view) {
        int height = 1;
        SUtils.initScreenDisplayMetrics((Activity) context);
        int marginTop = (int) (SUtils.screenHeight / 3f * 2);
        int width = SUtils.screenWidth / 3;
        bottom = new RectF();
        bottom.top = SUtils.getDip(context,20);
        bottom.left = (SUtils.screenWidth - width) / 2f;
        bottom.right = bottom.left + width+SUtils.getDip(context,50);
        bottom.bottom = marginTop + 1;
        Logs.i("lllllll---" + bottom);
        setLayout(bottom, lineBottom);

        left = new RectF();
        left.top = bottom.top - SUtils.getDip(context, 200);
        left.left = bottom.left;
        left.bottom = bottom.top;
        left.right = bottom.left + 1;
        setLayout(left, lineLeft);

        right = new RectF();
        right.top = bottom.top - SUtils.getDip(context, 200);
        right.left = bottom.right;
        right.right = bottom.right + 1;
        right.bottom = bottom.top;
        setLayout(right, lineRight);

        top = new RectF();
        top.top = left.top - 1;
        top.left = left.left;
        top.bottom = left.top;
        top.right = right.left;
        Logs.i("rect:" + top);
        setLayout(top, lineTop);

        tvBottom.setText((bottom.right - bottom.left) + "");
        tvLeft.setText((left.bottom - left.top) + "");
        tvRight.setText((right.bottom - right.top) + "");
        tvTop.setText((top.right - top.left) + "");
        ((RelativeLayout.LayoutParams) tvBottom.getLayoutParams()).leftMargin = (int) (bottom.left + (bottom.right - bottom.left) / 2);
        ((RelativeLayout.LayoutParams) tvLeft.getLayoutParams()).topMargin = (int) (left.bottom - (left.bottom - left.top) / 2);

        ((RelativeLayout.LayoutParams) tvTop.getLayoutParams()).leftMargin = (int) (top.left + (top.right - top.left) / 2);
        ((RelativeLayout.LayoutParams) tvRight.getLayoutParams()).topMargin = (int) (right.bottom - (right.bottom - right.top) / 2);


    }

    private void setLayout(RectF rectF, View view) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = (int) (rectF.right - rectF.left);
        params.height = (int) (rectF.bottom - rectF.top);
        params.leftMargin = (int) rectF.left;
        params.topMargin = (int) rectF.top;
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_cal;
    }

    @OnClick({R.id.tv_bottom})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bottom:
                DialogModifyContent dialogModifyContent = new DialogModifyContent(context, new OnModifyContentListener() {
                    @Override
                    public void returnContent(String content) {
                        float value = Float.parseFloat(content);
                        scale = value/bottom.width();
                        resetLayout();
                    }
                });
                dialogModifyContent.setInputType(InputType.TYPE_CLASS_NUMBER);
                dialogModifyContent.setMaxTextLength(100);
                dialogModifyContent.show();
                break;
        }
    }

    private void resetLayout(){
        float s = scale;
        if(s == 0){
            s = 1;
        }
        tvBottom.setText((bottom.right - bottom.left)*s + "");
        tvLeft.setText((left.bottom - left.top)*s + "");
        tvRight.setText((right.bottom - right.top)*s + "");
        tvTop.setText((top.right - top.left)*s + "");
    }
}
