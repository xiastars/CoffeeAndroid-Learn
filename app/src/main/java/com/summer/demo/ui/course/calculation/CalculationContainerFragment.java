package com.summer.demo.ui.course.calculation;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonGridAdapter;
import com.summer.demo.bean.ModuleInfo;
import com.summer.demo.ui.main.BaseMainFragment;
import com.summer.demo.ui.module.ModuleContainerActivity;
import com.summer.demo.ui.module.ModulePos;
import com.summer.demo.ui.module.colorpicker.AmbilWarnaDialog;
import com.summer.demo.ui.module.colorpicker.AmbilWarnaDialog.OnAmbilWarnaListener;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.view.NRecycleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Description: 计算模块
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2019/10/9 10:32
 */
public class CalculationContainerFragment extends BaseMainFragment {
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
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_animation, "帧动画", ModulePos.POS_FRAME));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_transition, "属性动画", ModulePos.POS_ANIM));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_dialog, "弹窗", ModulePos.POS_DIALOG));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_view_text, "视频裁剪", ModulePos.POS_VIDEO_CUTTER));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_navigatior, "Webview网页", ModulePos.POS_WEBVIEW));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_picker, "颜色选择器", ModulePos.POS_COLOR_PICKER));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_chat,"聊天",ModulePos.POS_CHAT));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_biaoqing,"表情",ModulePos.POS_EMOJI));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_view_list,"yasuo",ModulePos.POS_COMPRESS_IMG));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_audio,"音频播放",ModulePos.POS_AUDIO_PLAY));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_socket,"Socket通讯",ModulePos.POS_SOCKET));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_vibrate,"振动",ModulePos.POS_VIBRATE));
		adapter.notifyDataChanged(moduleInfos);
	}

	private void clickChild(int position) {
		switch (position) {
			case ModulePos.POS_COLOR_PICKER:
				AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(context, getResColor(R.color.red_d3), new OnAmbilWarnaListener() {
					@Override
					public void onOk(int color) {

					}
				});
				ambilWarnaDialog.show();
				break;
			default:
				JumpTo.getInstance().commonJump(context, ModuleContainerActivity.class, position);
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
