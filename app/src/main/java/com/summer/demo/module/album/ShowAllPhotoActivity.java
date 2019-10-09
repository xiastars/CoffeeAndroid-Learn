package com.summer.demo.module.album;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.module.album.adapter.AlbumGridViewAdapter;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.album.util.PublicWay;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.view.NRecycleView;

import java.util.ArrayList;

/**
 * 这个是显示一个文件夹里面的所有图片时的界面
 *
 * @author zhangqian
 */
public class ShowAllPhotoActivity extends BaseActivity {
    private NRecycleView gridView;
    private ProgressBar progressBar;
    private AlbumGridViewAdapter gridImageAdapter;
    // 完成按钮
    private Button okButton;
    // 预览按钮
    private Button preview;
    private Intent intent;
    public static ArrayList<ImageItem> dataList = new ArrayList<ImageItem>();

    public static ArrayList<ImageItem> mList = new ArrayList<ImageItem>();

    ArrayList<ImageItem> tempSelectBitmap;

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.plugin_camera_show_all_photo;
    }

    @Override
    protected void initData() {

        mList.clear();
        tempSelectBitmap = (ArrayList<ImageItem>) JumpTo.getObject(this);
        Logs.i("tem:" + tempSelectBitmap);
        for (int i = 0; i < tempSelectBitmap.size(); i++) {
            if (mList.size() > 0) {
                // 过滤相同的数据
                for (int j = 0; j < mList.size(); j++) {
                    if (mList.get(j).getImageId().equals(tempSelectBitmap.get(i).getImageId())) {
                        break;
                    } else if (j == mList.size() - 1) {
                        mList.add(tempSelectBitmap.get(i));
                    }
                }
            } else {
                mList.add(tempSelectBitmap.get(i));
            }
        }

        PublicWay.activityList.add(this);
        preview = (Button) findViewById(R.id.showallphoto_preview);
        okButton = (Button) findViewById(R.id.showallphoto_ok_button);
        this.intent = getIntent();
        String folderName = intent.getStringExtra("folderName");
        if (folderName.length() > 14) {
            folderName = folderName.substring(0, 15) + "...";
        }
        setTitle(folderName);
        preview.setOnClickListener(new PreviewListener());
        init();
        initListener();
        isShowOkBt();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    private class PreviewListener implements OnClickListener {
        public void onClick(View v) {
            if (mList.size() > 0) {
                intent.putExtra("position", "2");
                intent.setClass(ShowAllPhotoActivity.this, GalleryActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }

    private void init() {
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        progressBar = (ProgressBar) findViewById(R.id.showallphoto_progressbar);
        progressBar.setVisibility(View.GONE);
        gridView = (NRecycleView) findViewById(R.id.showallphoto_myGrid);
        gridView.setGridView(3);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList, mList);
        Logs.i("dataList:" + dataList);
        gridView.setAdapter(gridImageAdapter);
        okButton = (Button) findViewById(R.id.showallphoto_ok_button);
    }

    private void initListener() {
        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, ImageView button) {

                System.out.println("请求============" + mList.size() + "  ,  " + isChecked);

                if (mList.size() >= PublicWay.MAX_SELECT_COUNT && isChecked) {
                    button.setVisibility(View.GONE);
                    toggleButton.setChecked(false);
                    Toast.makeText(ShowAllPhotoActivity.this, getString(R.string.only_choose_num), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (isChecked) {
                    button.setVisibility(View.VISIBLE);
                    mList.add(dataList.get(position));
                    tempSelectBitmap.add(dataList.get(position));
                    okButton.setText(getString(R.string.finish) + "(" + mList.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
                } else {
                    button.setVisibility(View.GONE);
                    mList.remove(dataList.get(position));
                    tempSelectBitmap.remove(dataList.get(position));
                    okButton.setText(getString(R.string.finish) + "(" + mList.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
                }
                isShowOkBt();
            }
        });

        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(JumpTo.TYPE_OBJECT, tempSelectBitmap);
                setResult(12, intent);
                finish();
            }
        });
    }

    public void isShowOkBt() {
        if (mList.size() > 0) {
            okButton.setText(getString(R.string.finish) + "(" + mList.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText(getString(R.string.finish) + "(" + mList.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
            preview.setPressed(false);
            preview.setClickable(false);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }


    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        isShowOkBt();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
