package com.summer.demo.ui.view.commonfragment;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.STextUtils;

/**
 * @Description: 介绍SViewUtils
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/11/8 11:38
 */
public class SViewUtilsFragment extends BaseFragment {
    @Override
    protected void initView(View view) {
        TextView tvContent = new TextView(context);
        SpannableStringBuilder builder = new SpannableStringBuilder();
       // STextUtils.getSpannableView("SViewUtils的使用讲解:",0,-1,R.color.grey_22,1,5f,true);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_empty;
    }
}
