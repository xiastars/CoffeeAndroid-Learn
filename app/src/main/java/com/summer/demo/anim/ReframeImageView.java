package com.summer.demo.anim;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
/**
 * 拥有四边框的ImageView
 * @author @xiastars@vip.qq.com
 *
 */
public class ReframeImageView extends View{
	
	ReframeView reframeView;
	
	public ReframeImageView(Context context) {
		super(context);
	}
	
	public ReframeImageView(Context context,AttributeSet atri) {
		super(context,atri);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if(reframeView == null){
			reframeView = new ReframeView(getContext());
			reframeView.setData(getLeft(),getRight(),getTop(),getBottom());
			((RelativeLayout) getParent()).addView(reframeView);
		}
		super.onDraw(canvas);
	}
	
}
