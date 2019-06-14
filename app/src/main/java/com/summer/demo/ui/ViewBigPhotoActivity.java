package com.summer.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.base.BaseActivity;
import com.summer.demo.constant.BroadConst;
import com.summer.demo.view.SupportScrollEventWebView;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.DownloadPhotoHelper;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 帖子里的照片打开后的横向浏览页面
 *
 * @编者 xiastars
 */
public class ViewBigPhotoActivity extends BaseActivity implements
        View.OnClickListener {
    @BindView(R.id.count)
    protected TextView tvItemCount;
    private static final String STATE_POSITION = "STATE_POSITION";
    protected List<String> albumItem = null;
    private ViewPager pager;
    private SupportScrollEventWebView imageView;
    private SparseArray<View> views = new SparseArray<>();
    private String photo;

    boolean isMine;//是不是我的

    @Override
    protected void dealDatas(int requestCode, Object obj) {
        if (requestCode == 0) {
            SUtils.makeToast(context, "删除成功");
            Intent intent = new Intent(BroadConst.NOTIFY_ALBUM_DELETE);
            intent.putExtra(JumpTo.TYPE_OBJECT, (Serializable) albumItem);
            context.sendBroadcast(intent);
            if (albumItem == null || albumItem.size() == 0) {
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
    public void initPresenter() {

    }

    @Override
    protected void initData() {
        isMine = JumpTo.getBoolean(this);
        removeViewTitle();
        initView();
        context = ViewBigPhotoActivity.this;
    }

    protected void removeViewTitle() {
        removeTitle();
        changeHeaderStyleTrans(context.getResources().getColor(R.color.half_grey));
        setLayoutFullscreen();
    }

    private void initView() {
        albumItem = (List<String>) JumpTo.getObject(this);
        if (albumItem == null) {
            String path = JumpTo.getString(this);
            if (!TextUtils.isEmpty(path)) {
                albumItem = new ArrayList<>();
                albumItem.add(path);
            }
        }

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        Logs.i("xx:" + JumpTo.getString(this));
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
            String path = photo;
            String data;
            if (path != null && path.startsWith("http")) {
                data = "<table width='100%' height='100%'><tr><td  style='vertical-align:middle'><img src='" + path + "'" +
                        " width='100%'/></td></tr></table>";
            } else {
                data = "<table width='100%' height='100%'><tr><td  style='vertical-align:middle'><img src='" + "file://" + path + "'" +
                        " width='100%'/></td></tr></table>";
            }
            Logs.i("path:::" + data);
            imageView.loadData(data, "text/html", "utf-8");

            final String finalPath = path;
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SUtils.makeToast(context,"正在保存中");
                    DownloadPhotoHelper.getInstance().download(ViewBigPhotoActivity.this,finalPath);

                    return true;
                }
            });
            view.addView(imageLayout, 0);
            views.append(position, imageView);
            //bindWebViewOnTouchListener(imageView);
            return imageLayout;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
        }

    }


}