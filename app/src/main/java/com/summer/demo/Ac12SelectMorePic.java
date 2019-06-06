package com.summer.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.summer.demo.selectmorepic.ImageInfo;
import com.summer.demo.selectmorepic.SelectedAdapter;

import java.util.ArrayList;
import java.util.List;


public class Ac12SelectMorePic extends Activity {

	private ListView listView;
	private GridView addPic ;
	private Context context ;
	private SelectedAdapter adapter;
	private List<ImageInfo> list ;
	private List<ImageInfo> selectedImages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		context = Ac12SelectMorePic.this;
		
		
		addPic = (GridView) findViewById(R.id.grid_view);
		adapter = new SelectedAdapter(context);
		addPic.setAdapter(adapter);
		
		list = new ArrayList<ImageInfo>();
		ImageInfo info = new ImageInfo();
		list.add(0,info);

		
		addPic.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("点到哪里了",position+"--------------");
				if(position == 0){
					addPic();
				}
			}
		});
		selectedImages = (List<ImageInfo>) getIntent().getSerializableExtra("selected");
		if(selectedImages != null){
			list.addAll(selectedImages);
		}
		adapter.notifyDataChanged(list);	
	}
	
	private void addPic(){
		new AlertDialog.Builder(context)
		.setTitle("选择图片")
		.setItems(context.getResources().getStringArray(R.array.choice_item),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						if (which == 0) {
//								cameraPath = CameraOrLibrary.jumpToCamera(
//										AlbumAddActivity.this,
//										CameraOrLibrary.CAMERA);

						} else if (which == 1) {
							Intent intent = new Intent();
							intent.setClass(context,AcSelectAblum.class);
							startActivity(intent);

						}
					}
				})
		.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

					}
				}).show();
	}

}

