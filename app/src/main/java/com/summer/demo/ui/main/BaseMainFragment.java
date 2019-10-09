package com.summer.demo.ui.main;

import android.content.Context;

import com.summer.demo.module.base.BaseFragment;

public abstract class BaseMainFragment extends BaseFragment {
    protected MainActivity activity;
    protected Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = getContext();
        activity = (MainActivity) getActivity();
    }

    public boolean showTouristView(){
        return false;
    }

    public void firstRefreshView(){}

}
