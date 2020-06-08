package com.summer.helper.utils;

import android.content.Context;

public class TimePickerDialog {
	Context context;
	
	String disireContent = "";
	
	String formatContent = "yyyy-MM-dd";
	
	public TimePickerDialog(Context context){
		this.context = context;
	}
	
	public void setDisireContent(String content){
		this.disireContent = content;
	}
	
	public void setFormatContent(String content){
		this.formatContent = content;
	}

	
	public interface OnTimePickerListener{
		void receiveContent(String content);
	}

}
