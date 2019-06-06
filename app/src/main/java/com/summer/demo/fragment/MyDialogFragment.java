package com.summer.demo.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.summer.demo.R;
import com.summer.demo.dialog.DialogWeixin;
import com.summer.demo.dialog.LoadingDialog;
import com.summer.helper.utils.SUtils;
import com.summer.helper.utils.TipDialog;

/**
 * Dialog的用法
 * @author Administrator
 *
 */
public class MyDialogFragment extends BaseFragment implements View.OnClickListener{
	Dialog dialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_dialog, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		Button btnCommon = (Button) view.findViewById(R.id.btn_common);
		btnCommon.setOnClickListener(this);

		Button btnLonger = (Button) view.findViewById(R.id.btn_longer);
		btnLonger.setOnClickListener(this);

		Button btnSpecial = (Button) view.findViewById(R.id.btn_special);
		btnSpecial.setOnClickListener(this);
		//监听View的长按事件
		view.findViewById(R.id.btn5).setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				int[] pos = new int[2];
				v.getLocationInWindow(pos);
				showPupDialog(pos[0],pos[1],v);
				return false;
			}
		});
		view.findViewById(R.id.btn4).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.btn_common:
				Dialog dialog = new Dialog(context,R.style.TagFullScreenDialog);
				//设置样式，注意布局里indeterminateDrawable这个属性，而且5.0版本及以上与5.0以下写法不一样，注意看SummerHelper里的drawable-v21,对比差别
				dialog.setContentView(R.layout.dialog_loading);
				dialog.show();
				break;
			case R.id.btn_special:
				//弹出提示dialog，这是经常使用的功能,这里封装了一个简单的，传入想要提示的内容
				TipDialog tipsDialog = new TipDialog(context, "没有网络哦!", new TipDialog.DialogAfterClickListener() {
					@Override
					public void onSure() {
						SUtils.makeToast(context,"去设置里设置网络吧");
					}

					@Override
					public void onCancel() {
						SUtils.makeToast(context,"算了");
					}
				});
				tipsDialog.show();
				break;
			case R.id.btn_longer:
				//通常为了方便，我们把加载的Dialog封装出来
				LoadingDialog dialog1 = new LoadingDialog(context);
				dialog1.startLoading();
				break;
			case R.id.btn4:
				DialogWeixin weixin = new DialogWeixin(context);
				weixin.show();
				break;
		}
	}

	/**
	 * 长按一个按钮，显示一个Dialog在这个按钮正上方
	 */
	private void showPupDialog(float left,float top,View view) {
		if(dialog != null )setCancelDialog();
		dialog = new Dialog(context,R.style.dialog_pup);
		dialog.setContentView(R.layout.dialog_pup);
		Window window = dialog.getWindow();
		//设置Dialog动画
		window.setWindowAnimations(R.anim.scale_with_alpha);
		window.setGravity(Gravity.BOTTOM);
		ImageView ivdelte = (ImageView) dialog.findViewById(R.id.iv_delete);
		SUtils.clickTransColor(ivdelte);
		int dialogWidth = (int) context.getResources().getDimension(R.dimen.size_80);
		left = left +(view.getWidth()-dialogWidth)/2;
		top = top - dialogWidth*2;
		dialog.findViewById(R.id.ll_parent).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setCancelDialog();
			}
		});
		ivdelte.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				setCancelDialog();
			}
		});
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		ivdelte.setLayoutParams(mParams);
		mParams.leftMargin = (int) left;
		mParams.topMargin = (int) top ;
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	private void setCancelDialog(){
		if(null != dialog){
			dialog.cancel();
			dialog = null;
		}
	}

}
