package com.summer.demo.ui.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summer.demo.R;
import com.summer.demo.ui.fragment.BaseSimpleFragment;

/**
 * 自定义Drawable的一些用法，看layout布局
 * @author Administrator
 *
 */
public class CDrawableFragment extends BaseSimpleFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cdrawable, null);
		return view;
	}

}
