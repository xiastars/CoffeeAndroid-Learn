package com.summer.demo.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.summer.demo.R;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片使用综合示例
 * @author Administrator
 *
 */
public class PictureUseFragment extends BaseFragment implements View.OnClickListener{
	ImageView ivBg;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_picuse, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		ivBg = (ImageView) view.findViewById(R.id.iv_bg);
		Button btnCommon = (Button) view.findViewById(R.id.btn_common);
		btnCommon.setOnClickListener(this);
		
		Button btnLonger = (Button) view.findViewById(R.id.btn_longer);
		btnLonger.setOnClickListener(this);
		
		Button btnSpecial = (Button) view.findViewById(R.id.btn_special);
		btnSpecial.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_common:
			/* 图片设置一般使用框架，这里使用的是Glide框架 */
			String path = getSDPath()+"/temp.jpg";
			/* File是文件管理类，File的生成方式一般是new File(文件的绝对路径)*/
			File file = new File(path);
			Logs.i("xia","检查文件是否存在的方法:"+file.exists());
			Logs.i("xia","获取文件的绝对路径"+file.getAbsolutePath());
			Logs.i("xia","判断文件是不是文件夹:"+file.isDirectory());
			if(!file.exists()){
				SUtils.makeToast(context,"请在内存卡的根目录添加图片<temp.jpg>");
				return;
			}
			Glide.with(context).load(file).into(ivBg);
			/* 注：SUtils里有封装好的方法 */
			//SUtils.setPic(ivBg, path);
			break;
		case R.id.btn_special://asset里的图片
			InputStream in;
			try {
				in = context.getAssets().open("pic/xiehou06.jpg");
				Bitmap bitmap = BitmapFactory.decodeStream(in);
				ivBg.setImageBitmap(bitmap);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			break;
		case R.id.btn_longer:
			Glide.with(context).load("http://img.idol001.com/middle/2016/11/27/e3992502d3267e681279c4191058c7ad1480222068.jpg").into(ivBg);
			/* 这里最好使用SUtil.setPic(),将图片缓存到了本地*/
			break;
		}
	}
	
	/**
	 * 获取内存卡主路径
	 * @return
	 */
	private String getSDPath() {
		File sdDir = null;
		try {
			boolean sdCardExist = Environment.getExternalStorageState()
					.equals(Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
			if (sdCardExist) {
				sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			} else {
				File file = new File(Environment.getDataDirectory() + "/sdcard");
				if (file.canRead()) {
					return file.toString();
				} else {
					return "";
				}
			}
			if (sdDir != null) {
				return sdDir.toString();
			}
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
		}
		return "";
	}

}
