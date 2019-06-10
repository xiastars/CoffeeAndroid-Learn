package com.summer.demo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class BaseFragment extends Fragment{
	protected Context context;
	protected FrameLayout llParent;
	
	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = getContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		llParent = (FrameLayout) container;
		initView();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	protected void initView() {
	}

}
