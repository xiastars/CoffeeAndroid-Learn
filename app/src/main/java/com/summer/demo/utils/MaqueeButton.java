package com.summer.demo.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.Button;
/**
 * 滚动的字符
 * 
 * @author 无名英雄
 *
 */
public class MaqueeButton extends Button {
	public MaqueeButton(Context context,AttributeSet attrs){
		super(context,attrs);
	}
	
	protected void onFocusChanged(boolean focused,int direction,Rect previouslyFoRect) {
		if(focused)
			super.onFocusChanged(focused,direction,previouslyFoRect);

	}
	
	public void onWindowFocusChanged(boolean hasWindowFocus){
		if(hasWindowFocus)
			super.onWindowFocusChanged(hasWindowFocus);
	}
	
	public boolean isFocused(){
		return true;
	}

}