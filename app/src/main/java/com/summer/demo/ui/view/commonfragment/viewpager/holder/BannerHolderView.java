package com.summer.demo.ui.view.commonfragment.viewpager.holder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.summer.demo.R;
import com.summer.demo.module.base.viewpager.Holder;
import com.summer.demo.ui.view.commonfragment.viewpager.bean.BannerInfo;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.RoundAngleImageView;

public class BannerHolderView implements Holder<BannerInfo> {
    private RoundAngleImageView imageView;

    @Override
    public View createView(Context context) {
        //你可以通过layout文件来创建，也可以像我一样用代码创建，不一定是Image，任何控件都可以进行翻页
        imageView = new RoundAngleImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (SUtils.screenWidth * 0.48f));
        imageView.setLayoutParams(params);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, BannerInfo data) {
        SUtils.setPicWithHolder(imageView, data.getImg(), R.drawable.default_icon_linear);
    }
}