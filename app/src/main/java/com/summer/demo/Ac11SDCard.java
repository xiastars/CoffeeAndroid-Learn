package com.summer.demo;

import java.io.File;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.widget.TextView;
/**
 * 获取SD卡相关信息
 * @编者 夏起亮
 *
 */
public class Ac11SDCard extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_11_sdcard);
		
		/*
		 * 判断SD是否安装好
		 */
		if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){
			//取得SDCARD的路径
			File path = Environment.getExternalStorageDirectory();
			//StatFs返回给定路径空间的所有信息
			StatFs statfs = new StatFs(path.getPath());
			//获取Block的大小
			long blocSize = statfs.getBlockSize();
			//获取Block的数目
			long totalBlocks = statfs.getBlockCount();
			//空闲的Block的数量
			long spaceBlock = statfs.getAvailableBlocks();
			
			//获取空闲空间大小
			long freeSpace = spaceBlock * blocSize ;
			//获取总空间大小
			long totalSize = blocSize * totalBlocks;
			
			TextView text1 = (TextView) findViewById(R.id.text1);
			text1.setText("还有"+freeSpace/1024/1024 +"MB的空间");
			TextView text2 = (TextView) findViewById(R.id.text2);
			text2.setText("总共有"+totalSize/1024/1024 +"MB的空间");
		}		
		
	}

}
