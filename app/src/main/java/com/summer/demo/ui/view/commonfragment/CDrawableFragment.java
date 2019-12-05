package com.summer.demo.ui.view.commonfragment;

import android.view.View;
import android.widget.ImageView;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.SViewUtils;

import butterknife.BindView;

/**
 * 自定义Drawable的一些用法，看layout布局
 * @author Administrator
 *
 */
public class CDrawableFragment extends BaseFragment {
	@BindView(R.id.iv_gradient)
	ImageView ivGradient;

	@Override
	protected void initView(View view) {
		SViewUtils.setShadowView(ivGradient);
	}

	@Override
	protected void dealDatas(int requestType, Object obj) {

	}

	@Override
	protected int setContentView() {
		return R.layout.fragment_cdrawable;
	}

}
