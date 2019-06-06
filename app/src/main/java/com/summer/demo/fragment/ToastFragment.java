package com.summer.demo.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.summer.demo.R;

/**
 * Toast的用法 
 * @author Administrator
 *
 */
public class ToastFragment extends BaseFragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_toast, null);
		initView(view);
		return view;
	}

	/**
	 * Fragment里面的findViewId由onCreateView返回的View来寻找
	 * @param view
	 */
	private void initView(View view) {
		Button btnCommon = (Button) view.findViewById(R.id.btn_common);
		btnCommon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/* 第三个参数是时间长短，Toast.LENGTH_SHORT为300毫秒，Toast.LENGTH_LONG为1000毫秒 
				 * 这里的时间可以自定义 ，比如直接传100*/
				Toast.makeText(context, "这是一个Toast", Toast.LENGTH_SHORT).show();
			}
		});
		
		Button btnLonger = (Button) view.findViewById(R.id.btn_longer);
		btnLonger.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "这是一个Toast", Toast.LENGTH_LONG).show();
			}
		});
		
		Button btnSpecial = (Button) view.findViewById(R.id.btn_special);
		btnSpecial.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				makeSpeciaToast("这是自定义的Toast");
			}
		});
	}
	
	@SuppressLint("NewApi")
	private void makeSpeciaToast(String content){
		Toast toast = new Toast(context);
		TextView textView = new TextView(context);
		textView.setBackground(context.getResources().getDrawable(R.drawable.so_rede5_90));
		textView.setText(content);
		textView.setPadding(40, 15, 40, 15);
		toast.setView(textView);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.show();
	}

}
