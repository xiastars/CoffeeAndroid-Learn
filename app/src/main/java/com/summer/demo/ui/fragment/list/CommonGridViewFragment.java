package com.summer.demo.ui.fragment.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonListAdapter;
import com.summer.demo.ui.fragment.BaseFragment;

/**
 * 一般的GridView
 *
 * @author xiastars@vip.qq.com
 */
public class CommonGridViewFragment extends BaseFragment implements View.OnClickListener {
    GridView refreshView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ac_grid, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        refreshView = (GridView)view.findViewById(R.id.grid_view);
        refreshView.setAdapter(new CommonListAdapter(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}