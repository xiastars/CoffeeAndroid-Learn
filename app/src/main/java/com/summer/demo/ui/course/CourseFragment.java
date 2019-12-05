package com.summer.demo.ui.course;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonGridAdapter;
import com.summer.demo.bean.ModuleInfo;
import com.summer.demo.ui.course.calculation.CalculationActivity;
import com.summer.demo.ui.main.BaseMainFragment;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.view.NRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: 教程页面
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/12 11:01
 */
public class CourseFragment extends BaseMainFragment {
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
        moduleInfos.add(new ModuleInfo(R.drawable.ic_course_computer, "常用工具", CoursePos.POS_TOOL));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_course_java, "JAVA基础", CoursePos.POS_JAVA));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_course_computer, "计算机网络", CoursePos.POS_NET));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_course_computer, "工程计算", CoursePos.POS_CALCULATION));
        moduleInfos.add(new ModuleInfo(R.drawable.ic_course_computer,"后端架构师技术图谱",CoursePos.POS_BACK));
        adapter.notifyDataChanged(moduleInfos);
    }

    private void clickChild(int position) {
        switch (position) {
            case CoursePos.POS_TOOL:
                JumpTo.getInstance().commonJump(context, LearnJavaActivity.class, CoursePos.POS_TOOL);
                break;
            case CoursePos.POS_JAVA:
                JumpTo.getInstance().commonJump(context, LearnJavaActivity.class, CoursePos.POS_JAVA);
                break;
            case CoursePos.POS_BACK:
                JumpTo.getInstance().commonJump(context, LearnJavaActivity.class, CoursePos.POS_BACK);
                break;
            case CoursePos.POS_NET:
                JumpTo.getInstance().commonJump(context, LearnNetActivity.class, CoursePos.POS_NET);
                break;
            case CoursePos.POS_CALCULATION:
                JumpTo.getInstance().commonJump(context, CalculationActivity.class);
                break;
            default:
                JumpTo.getInstance().commonJump(context, LearnJavaActivity.class);
                break;
        }

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

