package com.summer.demo.ui.view.commonfragment.viewpager.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.summer.demo.R;
import com.summer.demo.ui.view.commonfragment.viewpager.bean.BannerInfo;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.RoundAngleImageView;

public class HomeBannerHolder extends BannerHolderView {
    private RoundAngleImageView imageView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundResource(R.drawable.so_white);
        imageView = new RoundAngleImageView(context);
        imageView.setShowTouchAnim(false);
        layout.addView(imageView);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SUtils.getDip(context,230)));
        imageView.setCornerRadius(10);
        int dip5 = SUtils.getDip(context,7);
        int dip10 = SUtils.getDip(context,10);
        imageView.setPadding(dip5,0,dip5,0);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return layout;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerInfo data) {
        SUtils.setPicWithHolder(imageView, data.getImg(), R.drawable.default_icon_linear);
    }
}
