package com.summer.demo.module.base.dialog;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.helper.dialog.BaseCenterDialog;

public class TipDialog extends BaseCenterDialog {
	private Context context;
	private ImageView image_load;
	private TextView tv_load_content;
	private AnimationDrawable animationDrawable;

	String loadContent;
	
	public TipDialog(Context context) {
		super(context , R.style.dialog_loading_style);
		this.context = context;
	}

	@Override
	public int setContainerView() {
		return R.layout.tips_layout;
	}

	@Override
	public void initView(View view) {
		image_load = (ImageView) findViewById(R.id.img_load);
		tv_load_content = (TextView) findViewById(R.id.tv_load_content);
		TextPaint tp = tv_load_content.getPaint();
		tp.setFakeBoldText(true);
		image_load.setImageResource(R.drawable.load_animation);
		animationDrawable = (AnimationDrawable) image_load.getDrawable();
		animationDrawable.start();
		tv_load_content.setText(loadContent);
	}


	public void setLoadContent(String content){
		this.loadContent = content;

	}

	
}