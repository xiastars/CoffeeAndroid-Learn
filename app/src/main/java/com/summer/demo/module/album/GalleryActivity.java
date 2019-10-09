package com.summer.demo.module.album;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.module.album.util.AlbumHelper;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.album.util.PublicWay;
import com.summer.demo.module.album.zoom.PhotoView;
import com.summer.demo.module.album.zoom.ViewPagerFixed;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.SUtils;

import java.util.ArrayList;

/**
 * 这个是用于进行图片浏览时的界面
 *
 * @author zhangqian
 */
public class GalleryActivity extends BaseActivity {
    private Intent intent;
    // 发送按钮
    private Button send_bt;
    // 删除按钮
    private Button del_bt;
    // 顶部显示预览图片位置的textview
    private TextView positionTextView;
    // 获取前一个activity传过来的position
    private int position;
    // 当前的位置
    private int location = 0;

    private ArrayList<View> listViews = null;
    private ViewPagerFixed pager;
    private MyPageAdapter adapter;
    private Context mContext;

    ArrayList<ImageItem> tempSelectBitmap = new ArrayList<>();

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.plugin_camera_gallery;
    }

    @Override
    protected void initData() {
        PublicWay.activityList.add(this);
        mContext = this;
        send_bt = (Button) findViewById( R.id.send_button );
        del_bt = (Button) findViewById( R.id.btn_right );
        send_bt.setOnClickListener(new GallerySendListener());
        del_bt.setOnClickListener(new DelListener());
        intent = getIntent();
        position = Integer.parseInt(intent.getStringExtra("position"));
        tempSelectBitmap = (ArrayList<ImageItem>) JumpTo.getObject(this);
        setTitle("预览");
        isShowOkBt();
        // 为发送按钮设置文字
        pager = (ViewPagerFixed) findViewById( R.id.gallery01 );
        pager.setOnPageChangeListener(pageChangeListener);
        if(tempSelectBitmap != null){
            int size = tempSelectBitmap.size();
            for (int i = 0; i < size; i++) {
                initListViews(tempSelectBitmap.get(i).getImagePath());
            }
        }
        adapter = new MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        pager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.size_10));
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }

    private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void initListViews(String bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        SUtils.setPic(img,bm);
        img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }

    // 返回按钮添加的监听器
    private class BackListener implements OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

    // 删除按钮添加的监听器
    private class DelListener implements OnClickListener {

        public void onClick(View v) {
            if (listViews.size() == 1) {
                tempSelectBitmap.clear();
                send_bt.setText( getString(R.string.finish)  + "("
                        + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT
                        + ")");
                Intent intent = new Intent("data.broadcast.action");
                sendBroadcast(intent);
                finish();

            } else {
                tempSelectBitmap.remove(location);
                pager.removeAllViews();
                listViews.remove(location);
                adapter.setListViews(listViews);
                send_bt.setText(  getString(R.string.finish) + "("
                        + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT
                        + ")");
                adapter.notifyDataSetChanged();
            }
        }
    }

    // 完成按钮的监听
    private class GallerySendListener implements OnClickListener {
        public void onClick(View v) {

            AlbumHelper.MAX_SELECT = tempSelectBitmap.size();
            Intent intent = new Intent();
            setResult(12, intent);

            for (int i = 0; i < PublicWay.activityList.size(); i++) {
                if (null != PublicWay.activityList.get(i)) {
                    PublicWay.activityList.get(i).finish();
                }
            }
        }

    }

    public void isShowOkBt() {
        if (tempSelectBitmap.size() > 0) {
            send_bt.setText(  getString(R.string.finish) + "("
                    + tempSelectBitmap.size() + "/" + PublicWay.MAX_SELECT_COUNT + ")");
            send_bt.setPressed(true);
            send_bt.setClickable(true);
            send_bt.setTextColor(Color.WHITE);
        } else {
            send_bt.setPressed(false);
            send_bt.setClickable(false);
            send_bt.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}
