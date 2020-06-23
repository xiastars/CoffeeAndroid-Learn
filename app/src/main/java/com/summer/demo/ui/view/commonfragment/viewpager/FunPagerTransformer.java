package com.summer.demo.ui.view.commonfragment.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.summer.helper.utils.SUtils;

/**
 * Created by xiaqiliang on 2017/4/13.
 */
public class FunPagerTransformer implements ViewPager.PageTransformer {

    private int maxTranslateOffsetX;
    private ViewPager viewPager;

    public FunPagerTransformer(Context context) {
        this.maxTranslateOffsetX = SUtils.getDip(context, 180);
    }

    public void transformPage(View view, float position) {
        if (viewPager == null) {
            viewPager = (ViewPager) view.getParent();
        }
        int leftInScreen = view.getLeft() - viewPager.getScrollX();
        int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
        int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
        float offsetRate = (float) offsetX * 0.05f / viewPager.getMeasuredWidth();
        float scaleFactor = 1 - Math.abs(offsetRate);
        if (scaleFactor > 0) {
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            if (position == 2.0f) {
                view.setTranslationX(maxTranslateOffsetX * 0.4f);
            } else {
                if (position < -0.1f) {
                    view.setTranslationX(-maxTranslateOffsetX * offsetRate * 4.5f);
                } else {
                    view.setTranslationX(-maxTranslateOffsetX * offsetRate);
                }
            }
        }
    }

}
