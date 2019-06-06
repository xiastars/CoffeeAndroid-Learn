package com.summer.demo.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.summer.demo.R;

public class DialogUtils {
	
	/**
	 * 用于给一段文字进行提示的Dialog
	 */
	public static void startConfirm(int titleText, Context context, final DialogClickListener listener){
		try {
			final Dialog mDialog = new Dialog(context, R.style.MyDialog);
			mDialog.show();
			mDialog.setContentView(R.layout.dialog_common);		
			TextView title = (TextView) mDialog.findViewById(R.id.title);
			title.setText(titleText);
		    LinearLayout llCancel =(LinearLayout)mDialog.findViewById(R.id.ll_cancel);
		    /* 取消 */
		    llCancel.setOnClickListener(new View.OnClickListener(){

				 @Override
				 public void onClick(View v) {
					mDialog.cancel();
					listener.doNegative();
				 }
		    });	    
		    /* 确定 */
		    LinearLayout llLogout =(LinearLayout)mDialog.findViewById(R.id.ll_sure);
		    llLogout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					listener.doPositive();
					mDialog.cancel();
				}
			});
		    mDialog.setCanceledOnTouchOutside(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public interface DialogClickListener{
		void doPositive();
		void doNegative();		 
	 }

}
