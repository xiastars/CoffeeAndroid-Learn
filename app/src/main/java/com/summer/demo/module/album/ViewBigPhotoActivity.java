package com.summer.demo.module.album;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.constant.BroadConst;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.view.SupportScrollEventWebView;
import com.summer.helper.dialog.BottomListDialog;
import com.summer.helper.downloader.DownloadStatus;
import com.summer.helper.downloader.DownloadTask;
import com.summer.helper.downloader.DownloadTaskListener;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.server.EasyHttp;
import com.summer.helper.server.PostData;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 帖子里的照片打开后的横向浏览页面
 *
 * @编者 夏起亮
 */
public class ViewBigPhotoActivity extends BaseActivity implements OnClickListener {
    @BindView(R.id.count)
    protected TextView tvItemCount;
    private static final String STATE_POSITION = "STATE_POSITION";
    protected List<ImageItem> albumItem = null;
    private ViewPager pager;
    private SupportScrollEventWebView imageView;
    private SparseArray<View> views = new SparseArray<>();
    private ImageItem photo;

    boolean isMine;//是不是我的

    /**
     * 跳转
     * @param items
     * @param curPos 显示的位置
     */
    public static void show(Context context, List<ImageItem> items, int curPos){
        Intent intent = new Intent(context, ViewBigPhotoActivity.class);
        intent.putExtra(JumpTo.TYPE_INT, curPos);
        intent.putExtra(JumpTo.TYPE_OBJECT, (Serializable) items);
        context.startActivity(intent);
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {
        if(requestCode == 0){
            SUtils.makeToast(context,"删除成功");
            Intent intent = new Intent(BroadConst.NOTIFY_ALBUM_DELETE);
            intent.putExtra(JumpTo.TYPE_OBJECT, (Serializable) albumItem);
            context.sendBroadcast(intent);
            if(albumItem == null || albumItem.size() == 0){
                finish();
            }
        }
    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_view_photos;
    }

    @Override
    protected void initData() {
        isMine = JumpTo.getBoolean(this);
        removeViewTitle();
        initView();
        context = ViewBigPhotoActivity.this;
    }

    protected void removeViewTitle() {
        changeHeaderStyleTrans(context.getResources().getColor(R.color.half_grey));
        setLayoutFullscreen(true);
        removeTitle();
    }

    private void initView() {
        albumItem = (List<ImageItem>) JumpTo.getObject(this);
        if (albumItem == null) {
            String path = JumpTo.getString(this);
            if (!TextUtils.isEmpty(path)) {
                albumItem = new ArrayList<>();
                ImageItem item = new ImageItem();
                item.setImagePath(path);
                albumItem.add(item);
            }
        }

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        Logs.i("xx:"+ JumpTo.getInteger(this));
        pager.setAdapter(new ImagePagerAdapter());
        pager.setCurrentItem(JumpTo.getInteger(this));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, pager.getCurrentItem());
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container
                    .removeView(views.get(pager.getCurrentItem()));
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return albumItem != null ? albumItem.size() : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = getLayoutInflater().inflate(
                    R.layout.big_photo_item_page_image, view, false);
            imageView = (SupportScrollEventWebView) imageLayout
                    .findViewById(R.id.btn_drag_view);
            final RelativeLayout loading = (RelativeLayout) imageLayout
                    .findViewById(R.id.loading);
            imageView.setBackgroundColor(0); // 设置背景色
            imageView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
            setWebViewSetings(loading);
            bindCloseClickListener(imageView);
            photo = albumItem.get(position);
            String path = photo.getImagePath();
            if (path != null && path.startsWith("//")) {
                path = PostData.OOSHEAD + ":" + path;
            }
            String data;
            if (path.startsWith("http")) {
                data = "<table width='100%' height='100%'><tr><td  style='vertical-align:middle'><img src='" + path + "'" +
                        " width='100%'/></td></tr></table>";
            } else {
                data = "<table width='100%' height='100%'><tr><td  style='vertical-align:middle'><img src='" + "file://"+path + "'" +
                        " width='100%'/></td></tr></table>";
            }
            Logs.i("path:::"+data);
            imageView.loadData(data, "text/html", "utf-8");

            final String finalPath = path;
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final BottomListDialog selectTypeDialog = new BottomListDialog(context);
                    String[] titles = {"保存", "取消" };
                    selectTypeDialog.setDatas(titles);
                    selectTypeDialog.setStringType();
                    selectTypeDialog.showTopContent(View.GONE);
                    selectTypeDialog.showBottomContent(View.GONE);
                    selectTypeDialog.show();
                    selectTypeDialog.setListener(new OnSimpleClickListener() {
                        @Override
                        public void onClick(int position) {
                            if (position == 0) {
                                downloadImg(finalPath);
                            }
                            selectTypeDialog.cancelDialog();
                        }
                    });

                    return true;
                }
            });

      /*      Logs.i("path:"+path);
            String data = "<table width='100%' height='100%'><tr><td  style='vertical-align:middle'><img src='" + path+ "'" +
                    " width='100%'/></td></tr></table>";
            if(path.endsWith("mp4")){
                data = "<table width='100%' height='100%'><tr><td  style='vertical-align:middle'><video src='" + path+ "'" +
                        " width='100%' autoplay='autoplay'></video>" + "</td></tr></table>";
            }
            imageView.requestRankData(data, "text/html", "utf-8");*/
            view.addView(imageLayout, 0);
            views.append(position, imageView);
            //bindWebViewOnTouchListener(imageView);
            dealLikeView(imageLayout, photo);
            return imageLayout;
        }

        private void downloadImg(String path) {
            final String fileName = System.currentTimeMillis() + "_hxq.png";
            EasyHttp.download(context, path, SFileUtils.getImageViewDirectory(), fileName, new DownloadTaskListener() {
                @Override
                public void onDownloading(DownloadTask downloadTask) {
                    Logs.i(downloadTask.getPercent() + ",,");
                    if (downloadTask.getDownloadStatus() == DownloadStatus.DOWNLOAD_STATUS_COMPLETED) {
                        ViewBigPhotoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SUtils.makeToast(context, "保存成功，位置：" + SFileUtils.getImageViewDirectory() + fileName);
                            }
                        });

                    }
                }

                @Override
                public void onPause(DownloadTask downloadTask) {

                }

                @Override
                public void onError(DownloadTask downloadTask, int errorCode) {

                }
            });
        }

        private void bindWebViewOnTouchListener(final WebView webView) {
            webView.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        finish();
                    }
                    return false;
                }
            });
        }

        private void setWebViewSetings(final RelativeLayout loading) {
            WebSettings webSettings = imageView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setSupportZoom(true);
            imageView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                    view.loadUrl(url);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    loading.setVisibility(View.GONE);
                }
            });
        }

        private void bindCloseClickListener(SupportScrollEventWebView view) {
            if (view == null)
                return;
            view.setOnSingleTabListener(new OnSimpleClickListener() {

                @Override
                public void onClick(int position) {
                    finish();
                }
            });
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public void finishUpdate(View container) {
            super.finishUpdate(container);
            // 显示页码
            if (albumItem != null && albumItem.size() > 0) {
                tvItemCount.setText(pager.getCurrentItem() + 1 + "/"
                        + albumItem.size());
            }
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

    }

    /**
     * 处理点赞
     *
     * @param view
     * @param imageItem
     */
    protected void dealLikeView(View view, ImageItem imageItem) {

    }

    /**
     * 获取当前对象
     * @return
     */
    protected ImageItem getCurItem(){
        if(albumItem == null){
            return null;
        }
        return albumItem.get(pager.getCurrentItem());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
        }

    }

    public void deleteItem(ImageItem item){
        if(albumItem == null){
            return;
        }
        int index = pager.getCurrentItem();
        albumItem.remove(item);
        pager.setAdapter(new ImagePagerAdapter());
        pager.setCurrentItem(index > 0 ? index -1 : 0);
        //do delete
    }

}