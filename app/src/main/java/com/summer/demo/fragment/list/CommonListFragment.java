package com.summer.demo.fragment.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonListAdapter;
import com.summer.demo.fragment.BaseFragment;

/**
 * 一般的ListView
 *
 * @author xiastars@vip.qq.com
 */
public class CommonListFragment extends BaseFragment implements View.OnClickListener {
    ListView refreshView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ac_main, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        refreshView = (ListView)view.findViewById(R.id.listview);
        //设置Adapter，List相当于一个楼梯的空间，告诉别人，这里是一个楼梯，而Adapter是具体的一层层的阶梯，第一阶被称为item
        refreshView.setAdapter(new CommonListAdapter(getActivity()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

}