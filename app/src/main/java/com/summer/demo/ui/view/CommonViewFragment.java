package com.summer.demo.ui.view;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonGridAdapter;
import com.summer.demo.bean.ModuleInfo;
import com.summer.demo.ui.FragmentContainerActivity;
import com.summer.demo.ui.main.BaseMainFragment;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.view.NRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: 普通View
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 10:29
 */
public class CommonViewFragment extends BaseMainFragment {
    @BindView(R.id.sv_container)
    NRecycleView svContainer;

    CommonGridAdapter adapter;

    @Override
    protected void initView(View view) {
        svContainer.setGridView(3);
        svContainer.setDivider();


        final List<ModuleInfo> moduleInfos = new ArrayList<>();
        moduleInfos.add(new ModuleInfo(R.drawable.so_gradient_redffe_blued8,"Drawable",UiPosition.POS_DRAWABLE));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_text,"文本",UiPosition.POS_TEXT));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_danmake,"ConstarintLayout",UiPosition.POS_CONSTRAINT));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_list,"ListView",UiPosition.POS_LIST_REC));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_grid,"GridView",UiPosition.POS_GRID_REC));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_list,"可刷新List",UiPosition.POS_REFRESH_LIST));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_grid,"可刷新Grid",UiPosition.POS_REFRESH_GRID));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_calendar,"进度条",UiPosition.PROGRESS));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_calendar,"EditText",UiPosition.EDITTEXT));

        adapter = new CommonGridAdapter(context, new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                clickChild(moduleInfos.get(position).getPos());
            }
        });
        svContainer.setAdapter(adapter);
        adapter.notifyDataChanged(moduleInfos);
    }

    private void clickChild(int position) {
        JumpTo.getInstance().commonJump(context, FragmentContainerActivity.class,position);
    }

    @Override
    public void loadData() {

    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.view_nrecyleview;
    }


}
