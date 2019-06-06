package com.summer.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.summer.demo.selectmorepic.AlbumAdapter;
import com.summer.demo.selectmorepic.CommonFile;
import com.summer.demo.selectmorepic.ImageInfo;
import com.summer.demo.selectmorepic.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AcSelectAblum extends Activity implements OnClickListener{

	private GridView grid;
	private AlbumAdapter adapter;
	private Util util;
	private List<CommonFile> locallist;
	private List<ImageInfo> list;
	private Context context;
	private Button select;
	private RelativeLayout back;
	private List<ImageInfo> selectedImages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.ac_select_album);
		context = AcSelectAblum.this;
		
		util=new Util(this);
		Util.initScreenDisplayMetrics(this);
		initView();
	}
	
	private void initView(){
		grid = (GridView) findViewById(R.id.grid_view);
		select = (Button) findViewById(R.id.inclue_edit);
		select.setOnClickListener(this);
		adapter = new AlbumAdapter(this);
		back = (RelativeLayout)findViewById(R.id.btnLayout);
		back.setOnClickListener(this);
		grid.setAdapter(adapter);
		
		locallist=util.getFileList(context);
		if (locallist!=null) {
			list = new ArrayList<ImageInfo>();
			
			for (int i = 0; i < locallist.size(); i++) {
				List<ImageInfo> pathlist = locallist.get(i).filecontent;
				list.addAll(0,pathlist);
			}
		}
		adapter.notifyDataChanged(list);		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.inclue_edit:
			selectedImages = adapter.getSelectedImage();
			Intent intent = new Intent();
			intent.setClass(context,AcMain.class);
			intent.putExtra("selected",(Serializable) selectedImages);
			startActivity(intent);
			break;
		case R.id.btnLayout:
			finish();
			break;
		}
	}

}
