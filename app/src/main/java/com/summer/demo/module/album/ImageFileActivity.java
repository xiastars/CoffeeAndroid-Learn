package com.summer.demo.module.album;

import android.content.Intent;
import android.widget.GridView;

import com.summer.demo.R;
import com.summer.demo.base.BaseActivity;
import com.summer.demo.module.album.adapter.FolderAdapter;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.album.util.PublicWay;
import com.summer.helper.utils.JumpTo;

import java.util.ArrayList;


/**
 * 这个类主要是用来进行显示包含图片的文件夹
 *
 * @author zhangqian
 */
public class ImageFileActivity extends BaseActivity {

    private FolderAdapter folderAdapter;

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return R.string.title_photo;
    }

    @Override
    protected int setContentView() {
        return R.layout.plugin_camera_image_file;
    }

    @Override
    protected void initData() {
        PublicWay.activityList.add(this);
        GridView gridView = (GridView) findViewById(R.id.fileGridView);
        ArrayList<ImageItem> tempSelectBitmap = (ArrayList<ImageItem>) JumpTo.getObject(this);
        if (tempSelectBitmap == null) {
            tempSelectBitmap = new ArrayList<>();
        }
        folderAdapter = new FolderAdapter(this, tempSelectBitmap);
        gridView.setAdapter(folderAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //这里是从文件夹里面的所有图片时的界面跳过来的
        if (resultCode == 12) {
            Intent intent = new Intent();
            intent.putExtra(JumpTo.TYPE_OBJECT, data.getSerializableExtra(JumpTo.TYPE_OBJECT));
            setResult(12, intent);
            finish();
        }

    }

}
