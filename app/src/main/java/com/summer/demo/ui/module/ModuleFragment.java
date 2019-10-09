package com.summer.demo.ui.module;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonGridAdapter;
import com.summer.demo.bean.ModuleInfo;
import com.summer.demo.ui.main.BaseMainFragment;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.view.NRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: 模块Fragment
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 10:32
 */
public class ModuleFragment extends BaseMainFragment {
    @BindView(R.id.sv_container)
    NRecycleView svContainer;

    CommonGridAdapter adapter;

    @Override
    protected void initView(View view) {
        svContainer.setGridView(3);
        svContainer.setDivider();
        adapter = new CommonGridAdapter(context, new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {
                clickChild(position);
            }
        });
        svContainer.setAdapter(adapter);
        List<ModuleInfo> moduleInfos = new ArrayList<>();
        moduleInfos.add(new ModuleInfo(R.drawable.ic_module_animation,"帧动画"));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_module_transition,"属性动画"));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_module_dialog,"弹窗"));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_view_text,"视频裁剪"));
        adapter.notifyDataChanged(moduleInfos);
    }

    private void clickChild(int position) {
        JumpTo.getInstance().commonJump(context, ModuleContainerActivity.class,position);
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
