package com.summer.demo.ui.module;

import android.view.View;

import com.summer.demo.R;
import com.summer.demo.adapter.CommonGridAdapter;
import com.summer.demo.bean.ModuleInfo;
import com.summer.demo.ui.main.BaseMainFragment;
import com.summer.demo.ui.module.colorpicker.AmbilWarnaDialog;
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
		adapter = new CommonGridAdapter(context, position -> clickChild(position));
		svContainer.setAdapter(adapter);
		List<ModuleInfo> moduleInfos = new ArrayList<>();
		moduleInfos.add(new ModuleInfo(R.drawable.open, "SDK", ModulePos.POS_SDK));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_animation, "帧动画", ModulePos.POS_FRAME));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_transition, "属性动画", ModulePos.POS_ANIM));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_dialog, "弹窗", ModulePos.POS_DIALOG));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_cut_video, "视频裁剪", ModulePos.POS_VIDEO_CUTTER));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_navigatior, "Webview网页", ModulePos.POS_WEBVIEW));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_picker, "颜色选择器", ModulePos.POS_COLOR_PICKER));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_chat,"聊天",ModulePos.POS_CHAT));
		moduleInfos.add(new ModuleInfo(R.drawable.input_emoji_2,"表情",ModulePos.POS_EMOJI));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_compress,"图片压缩",ModulePos.POS_COMPRESS_IMG));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_audio,"音频播放",ModulePos.POS_AUDIO_PLAY));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_socket,"Socket通讯",ModulePos.POS_SOCKET));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_module_vibrate,"振动",ModulePos.POS_VIBRATE));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_upload,"上传文件",ModulePos.POS_UPLOAD));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_permission,"系统权限",ModulePos.POS_PERMISSION));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_permission,"RXJava",ModulePos.POS_RXJAVA));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_office,"文档预览",ModulePos.POS_OFFICE));
		moduleInfos.add(new ModuleInfo(R.drawable.ic_office,"NFC",ModulePos.POS_NFC));
		moduleInfos.add(new ModuleInfo(R.drawable.icon_module_share,"社交分享",ModulePos.POS_SHARE));
		adapter.notifyDataChanged(moduleInfos);
	}

	private void clickChild(int position) {
		switch (position) {
			case ModulePos.POS_COLOR_PICKER:
				AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(context, getResColor(R.color.red_d3), color -> {

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
