package com.summer.demo.module.album;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.module.album.adapter.AlbumGridViewAdapter;
import com.summer.demo.module.album.adapter.FolersAdapter;
import com.summer.demo.module.album.util.AlbumHelper;
import com.summer.demo.module.album.util.ImageBucket;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.album.util.PublicWay;
import com.summer.demo.module.video.VideoEditActivity;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SThread;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.NRecycleView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 这个是进入相册显示所有图片的界面
 *
 * @author zhangqian
 */
public class AlbumActivity extends BaseActivity {

    //获取图片的code
    public static final int REQUEST_CODE = 12;
    @BindView(R.id.myGrid)
    NRecycleView myGrid;
    @BindView(R.id.nv_list)
    NRecycleView nvList;
    @BindView(R.id.rl_list_cover)
    RelativeLayout rlListCover;
    @BindView(R.id.preview)
    Button preview;
    @BindView(R.id.ok_button)
    Button okButton;
    @BindView(R.id.bottom_layout)
    RelativeLayout bottomLayout;
    @BindView(R.id.tv_finish)
    TextView tvFinish;
    //显示手机里的所有图片的列表控件
    private NRecycleView gridView;
    //gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    private Intent intent;
    private ArrayList<ImageItem> dataList;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;
    ArrayList<ImageItem> tempSelectBitmap = new ArrayList<>();

    boolean showVideo = false;//是否为纯视频

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            gridImageAdapter.notifyDataSetChanged();

        }
    };

    public void onSelectItem(View v) {
        if (tempSelectBitmap != null) {
            if (showVideo) {
                JumpTo.getInstance().commonJump(this, VideoEditActivity.class, tempSelectBitmap.get(0));
            } else {
                Intent intent = new Intent();
                intent.putExtra(JumpTo.TYPE_OBJECT, tempSelectBitmap);
                setResult(RESULT_OK, intent);
            }
        }
        for (int i = 0; i < PublicWay.activityList.size(); i++) {
            if (null != PublicWay.activityList.get(i)) {
                PublicWay.activityList.get(i).finish();
            }
        }
    }

    // 返回按钮监听
    private class BackListener implements OnClickListener {
        public void onClick(View v) {
            if (tempSelectBitmap.size() > 0) {
                intent.putExtra("position", "1");
                intent.putExtra(JumpTo.TYPE_OBJECT, tempSelectBitmap);
                intent.setClass(AlbumActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        }
    }

    // 初始化，给一些对象赋值
    private void init() {
        setTitle("最近照片");
        Button btnAlbum = (Button) findViewById(R.id.btn_right);
        btnAlbum.setOnClickListener(new BackListener());
        btnAlbum.setVisibility(View.VISIBLE);
        btnAlbum.setText("预览");
        preview = (Button) findViewById(R.id.preview);
        preview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Logs.i(nvList.getVisibility() + ",,,,");
                if (rlListCover.getVisibility() == View.VISIBLE) {
                    hideFolderView();
                } else {
                    rlListCover.setVisibility(View.VISIBLE);
                    Animation anim = AnimationUtils.loadAnimation(context,
                            R.anim.slide_up);
                    nvList.startAnimation(anim);
                }
            }
        });
        intent = getIntent();
        gridView = (NRecycleView) findViewById(R.id.myGrid);
        gridView.setGridView(3);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setText(getString(R.string.finish) + "(" + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
        if (showVideo) {
            preview.setVisibility(View.GONE);
            setTitle("最近视频");
            btnAlbum.setVisibility(View.GONE);
            SThread.getIntances().submit(new Runnable() {
                @Override
                public void run() {
                    getAllVideoInfos();
                }
            });
            return;
        }
        nvList.setList();
        FolersAdapter folderAdapter = new FolersAdapter(context, tempSelectBitmap);
        nvList.setAdapter(folderAdapter);


        helper = AlbumHelper.getHelper();
        helper.init(this);
        contentList = helper.getImagesBucketList(false);
        folderAdapter.notifyDataChanged(contentList);
        dataList = new ArrayList<>();
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList, tempSelectBitmap);
        gridView.setAdapter(gridImageAdapter);
        int contentSize = contentList.size();
        for (int i = 0; i < contentSize; i++) {
            dataList.addAll(contentList.get(i).imageList);
            gridImageAdapter.notifyDataSetChanged();
        }
        Comparator<ImageItem> comparator = new Comparator<ImageItem>() {
            public int compare(ImageItem s1, ImageItem s2) {
                //根据图片的ID进行判断
                if (Integer.parseInt(s1.getImageId()) != Integer.parseInt(s2.getImageId())) {
                    return Integer.parseInt(s2.getImageId()) - Integer.parseInt(s1.getImageId());
                }
                return 0;
            }
        };
        //这里就会自动根据规则进行排序
        Collections.sort(dataList, comparator);
        try {
            boolean isDraft = this.getIntent().getExtras().getBoolean("isDraft");
            if (isDraft) {
                //过滤之前有保存的稿子图片
                for (int i = 0; i < tempSelectBitmap.size(); i++) {
                    for (int j = 0; j < dataList.size(); j++) {
                        String imageId = tempSelectBitmap.get(i).getImageId();
                        ImageItem item = dataList.get(j);
                        if (item.getImageId().equals(imageId)) {
                            tempSelectBitmap.set(i, item);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        if (tempSelectBitmap != null) {
            ArrayList<ImageItem> datas = new ArrayList<>();
            for (int i = 0; i < tempSelectBitmap.size(); i++) {
                for (int j = 0; j < dataList.size(); j++) {
                    String imageId = tempSelectBitmap.get(i).getImageId();
                    ImageItem item = dataList.get(j);
                    if (item.getImageId().equals(imageId)) {
                        item.setSelected(true);
                        datas.add(item);
                        break;
                    }
                }
            }
            tempSelectBitmap.clear();
            tempSelectBitmap.addAll(datas);
        }
        gridImageAdapter.notifyDataSetChanged();

        initListener();
    }

    public void hideFolderView() {
        Animation anim = AnimationUtils.loadAnimation(context,
                R.anim.slide_bottom);
        rlListCover.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rlListCover.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 获取手机中所有视频的信息
     */
    private void getAllVideoInfos() {
        dataList = new ArrayList<>();
        Uri mImageUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] proj = {MediaStore.Video.Thumbnails._ID
                , MediaStore.Video.Thumbnails.DATA
                , MediaStore.Video.Media.DURATION
                , MediaStore.Video.Media.SIZE
                , MediaStore.Video.Media.DISPLAY_NAME
                , MediaStore.Video.Media.DATE_MODIFIED};
        Cursor mCursor = getContentResolver().query(mImageUri,
                proj,
                MediaStore.Video.Media.MIME_TYPE + "=?",
                new String[]{"video/mp4"},
                MediaStore.Video.Media.DATE_MODIFIED + " desc");
        if (mCursor != null) {
            while (mCursor.moveToNext()) {
                // 获取视频的路径
                long time = System.currentTimeMillis();
                int videoId = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media._ID));
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                int duration = mCursor.getInt(mCursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                //提前生成缩略图，再获取：http://stackoverflow.com/questions/27903264/how-to-get-the-video-thumbnail-path-and-not-the-bitmap
                String thumbPath = getVideoThumbPath(videoId);
                // 获取该视频的父路径名
                String dirPath = new File(path).getParentFile().getAbsolutePath();
                ImageItem item = new ImageItem();
                item.setDuration(duration);
                item.setVideoPath(path);
                Logs.i("xia", dirPath + ",," + path + ",,," + thumbPath);
                if (TextUtils.isEmpty(thumbPath)) {

                }
                item.setImagePath(thumbPath);
                dataList.add(item);
                if (dataList.size() % 3 == 0) {
                    notifyAdapter();
                }
            }
            mCursor.close();
        }
        notifyAdapter();
    }

    private String getVideoThumbPath(int videoId) {
        String thumbPath = cursorVideoThumbPath(videoId);
        if (TextUtils.isEmpty(thumbPath)) {
            MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(), videoId, MediaStore.Video.Thumbnails.MICRO_KIND, null);
            thumbPath = cursorVideoThumbPath(videoId);
        }
        return thumbPath;
    }

    private String cursorVideoThumbPath(int videoId) {
        String[] projection = {MediaStore.Video.Thumbnails._ID, MediaStore.Video.Thumbnails.DATA};
        Cursor cursor = getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI
                , projection
                , MediaStore.Video.Thumbnails.VIDEO_ID + "=?"
                , new String[]{videoId + ""}
                , null);
        String thumbPath = "";
        if (cursor != null) {
            while (cursor.moveToNext()) {
                thumbPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA));
            }
            cursor.close();
        }
        return thumbPath;
    }

    private void notifyAdapter() {
        //更新界面
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (gridImageAdapter == null) {
                    gridImageAdapter = new AlbumGridViewAdapter(AlbumActivity.this, dataList, tempSelectBitmap);
                    gridView.setAdapter(gridImageAdapter);
                    initListener();
                } else {
                    gridImageAdapter.notifyDatas(dataList);
                }
            }
        });
    }

    private void initListener() {
        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, ImageView chooseBt) {
                if (tempSelectBitmap.size() >= PublicWay.MAX_SELECT_COUNT) {
                    toggleButton.setChecked(false);
                    chooseBt.setVisibility(View.GONE);
                    if (!removeOneData(dataList.get(position))) {
                        if (PublicWay.MAX_SELECT_COUNT == 1) {
                            tempSelectBitmap.clear();
                            gridImageAdapter.notifyDataSetChanged();
                            toggleButton.setChecked(true);
                        } else {
                            Toast.makeText(AlbumActivity.this, getString(R.string.only_choose_num), Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                }
                if (isChecked) {//如果选中
                    chooseBt.setVisibility(View.VISIBLE);
                    tempSelectBitmap.add(dataList.get(position));
                    okButton.setText(getString(R.string.finish) + "(" + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
                } else {
                    dataList.get(position).setSelected(false);
                    tempSelectBitmap.remove(dataList.get(position));
                    chooseBt.setVisibility(View.GONE);
                    okButton.setText(getString(R.string.finish) + "(" + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
                }
                isShowOkBt();
            }
        });
    }

    private boolean removeOneData(ImageItem imageItem) {
        if (tempSelectBitmap.contains(imageItem)) {
            tempSelectBitmap.remove(imageItem);
            okButton.setText(getString(R.string.finish) + "(" + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (tempSelectBitmap.size() > 0) {
            okButton.setText(getString(R.string.finish) + "(" + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.parseColor("#FFFFFF"));
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText(getString(R.string.finish) + "(" + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
            preview.setPressed(true);
            preview.setClickable(true);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#FFFFFF"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //如果是相册列表跳过来的
        if (resultCode == 12) {

            Intent intent = new Intent();
            ArrayList<ImageItem> datas = (ArrayList<ImageItem>) data.getSerializableExtra(JumpTo.TYPE_OBJECT);
            if (datas != null) {
                //tempSelectBitmap.addAll(datas);
                tempSelectBitmap = datas;
                gridImageAdapter.notifyDataSetChanged();
                setResult(RESULT_OK, intent);
            }
            if(!showVideo && PublicWay.MAX_SELECT_COUNT == 1){
                intent = new Intent();
                intent.putExtra(JumpTo.TYPE_OBJECT, tempSelectBitmap);
                setResult(RESULT_OK, intent);
                this.finish();
            }
           /* for (int i = 0; i < PublicWay.activityList.size(); i++) {
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowOkBt();
        if (gridImageAdapter != null && dataList != null) {
            gridImageAdapter.notifyDataSetChanged();
        }
        Logs.i("请求=========", " ==========执行了吗执行了吗执行了吗执行了吗执行了吗执行了吗执行了吗执行了吗执行了吗执行了吗执行了吗执行了吗");
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.plugin_camera_album;
    }

    @Override
    protected void initData() {
        ButterKnife.bind(this);
        SUtils.initScreenDisplayMetrics(this);
        PublicWay.activityList.add(this);
        //注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plugin_camera_no_pictures);
        ArrayList<ImageItem> datas = (ArrayList<ImageItem>) JumpTo.getObject(this);
        if (datas != null) {
            tempSelectBitmap = datas;
        }
        String selectType = JumpTo.getString(this);
        if (selectType != null) {
            showVideo = selectType.equals(SFileUtils.FileType.FILE_MP4);
        }
        int count = JumpTo.getInteger(this);
        if (count != 0) {
            PublicWay.MAX_SELECT_COUNT = count;
        } else {
            PublicWay.MAX_SELECT_COUNT = 9;
        }
        init();
        //这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
