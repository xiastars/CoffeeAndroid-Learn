package com.summer.demo.ui.mine.release;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.constant.BroadConst;
import com.summer.demo.module.album.bean.SelectOptions;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.album.util.SelectPhotoHelper;
import com.summer.demo.module.base.viewpager.CBPageAdapter;
import com.summer.demo.module.base.viewpager.CBViewHolderCreator;
import com.summer.demo.module.base.viewpager.ConvenientBottomBanner;
import com.summer.demo.module.base.viewpager.Holder;
import com.summer.demo.module.video.util.VideoCacheManager;
import com.summer.demo.ui.mine.release.view.NFVideoPlayer;
import com.summer.helper.listener.OnReturnObjectClickListener;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

/**
 * 发布时的图片，视频预览
 */
public class ShowMediaActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.cb_container)
    ConvenientBottomBanner cbContainer;
    @BindView(R.id.rl_img)
    RelativeLayout rlImg;
    @BindView(R.id.iv_video_cover)
    ImageView ivVideoCover;
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    @BindView(R.id.vd_play)
    NFVideoPlayer vdPlay;
    @BindView(R.id.iv_video_play)
    ImageView ivVideoPlay;
    @BindView(R.id.rl_video_content)
    RelativeLayout rlVideoContent;
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    Context context;
    ArrayList<ImageItem> images;
    boolean isVideo;
    boolean isAskMode;
    private static SelectOptions mOption;

    int curImgPos;

    public static void show(Context context, Object object, boolean isVideo) {
        Intent intent = new Intent(context, ShowMediaActivity.class);
        intent.putExtra(JumpTo.TYPE_OBJECT, (Serializable) object);
        intent.putExtra(JumpTo.TYPE_BOOLEAN, isVideo);
        ((Activity) context).startActivityForResult(intent, ReleaseTopicActivity.REQUEST_HANDLE_MEDIA);
    }


    public static void show(Context context, Object object, SelectOptions option) {
        mOption = option;
        Intent intent = new Intent(context, ShowMediaActivity.class);
        intent.putExtra(JumpTo.TYPE_OBJECT, (Serializable) object);
        intent.putExtra(JumpTo.TYPE_BOOLEAN, false);
        ((Activity) context).startActivityForResult(intent, ReleaseTopicActivity.REQUEST_HANDLE_MEDIA);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isVideo = JumpTo.getBoolean(this);
        setContentView(R.layout.activity_show_media);

        ButterKnife.bind(this);
        context = this;
        initView();
    }

    private void initView() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cbContainer.getViewPager().getLayoutParams();
        int defaultHeight = SUtils.screenWidth;
        params.width = defaultHeight;
        params.height = defaultHeight;

        images = (ArrayList<ImageItem>) JumpTo.getObject(this);
        if (isVideo) {
            rlVideo.setVisibility(View.VISIBLE);
        } else {
            rlVideo.setVisibility(View.GONE);
        }

        initImgs(images);

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (images.size() != 0) {
                    Logs.i("images::" + images.size() + "curPos:" + curImgPos);
                    if (images.get(images.size() - 1).isFake()) {
                        images.remove(images.size() - 1);
                        Logs.i("images::" + images.size());
                    }
                    images.remove(curImgPos);
                    notifyReleaseImgs();
                    curImgPos = curImgPos > 0 ? curImgPos - 1 : 0;
                    initImgs(images);
                    cbContainer.setcurrentitem(curImgPos);
                }
            }
        });

    }

    @OnClick({R.id.rl_parent, R.id.iv_video_play})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_parent:
                finish();
                break;
            case R.id.iv_video_play:
                if (SUtils.isEmptyArrays(images)) {
                    return;
                }
                String videoUrl = images.get(0).getVideoPath();
                if (TextUtils.isEmpty(videoUrl)) {
                    return;
                }
                if (videoUrl.startsWith("http")) {
                    vdPlay.setup(VideoCacheManager.getManager(context).getCacheVideoUrl(videoUrl));
                } else {
                    vdPlay.setup(videoUrl);
                }
                ivVideoPlay.setVisibility(View.GONE);
                try{
                    vdPlay.startVideo();
                }catch (Exception e){
                    e.printStackTrace();
                }

                vdPlay.setVisibility(View.VISIBLE);
                vdPlay.setOnPlayListener(new NFVideoPlayer.OnPlayListener() {
                    @Override
                    public void onPlayPrepareing() {
           /*             vh.myVideoView.setLooping(false);
                        vh.myVideoView.start();*/
                        ivVideoPlay.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPlayResume() {

                    }

                    @Override
                    public void onPlayPause() {
                        vdPlay.setVisibility(View.GONE);
                        ivVideoCover.setVisibility(View.VISIBLE);
                        ivVideoPlay.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPlayComplete() {
                        vdPlay.setVisibility(View.GONE);
                        ivVideoCover.setVisibility(View.VISIBLE);
                        ivVideoPlay.setVisibility(View.VISIBLE);
                    }
                });
                break;
        }
    }

     class BannerHolderView implements Holder<ImageItem> {
        private ImageView imageView;
        private ImageView ivAdd;
        View view;

        @Override
        public View createView(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.item_show_media, null);
            imageView = view.findViewById(R.id.iv_pic);
            ivAdd = view.findViewById(R.id.iv_add);
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, final ImageItem data) {
            final ViewGroup.LayoutParams params = imageView.getLayoutParams();
            Logs.i("data:" + data.getImageWidth() + ",,," + data.getImageHeight());
            if (data.isFake()) {
                ivAdd.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                view.setBackgroundColor(getResources().getColor(R.color.grey_d8));
                ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqeustPhotos(isAskMode ? 1 : 9);
                    }
                });
            } else {
                view.setBackgroundColor(getResources().getColor(R.color.black));
                ivAdd.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                int width = data.getImageWidth();
                int height = data.getImageHeight();
                if (width != 0) {
                    if (width > height) {
                        params.width = SUtils.screenWidth;
                        params.height = (int) (params.width / (float) width * height);
                    } else {
                        params.height = SUtils.screenWidth;
                        params.width = (int) (params.height / (float) height * width);
                    }
                }
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowMediaActivity.this.finish();
                    }
                });
                SUtils.setPic(imageView, data.getImagePath());
            }
        }
    }

    protected void reqeustPhotos(int count) {
        if (PermissionUtils.checkReadPermission(context)) {
            if(isVideo){
                //BaseUtils.selectVideo(context);
                return;
            }
            images.remove(images.size() - 1);

            SelectPhotoHelper selectPhotoHelper = new SelectPhotoHelper(context, new OnReturnObjectClickListener() {
                @Override
                public void onClick(Object object) {
                    images = (ArrayList<ImageItem>) object;
                    if (!SUtils.isEmptyArrays(images)) {
                        Logs.i("imagtes:"+images);
                        if (!SUtils.isEmptyArrays(images)) {
                            curImgPos = 0;
                            initImgs(images);
                            notifyReleaseImgs();
                        }
                    }
                }
            });
            selectPhotoHelper.enterToAlbumForMore(count,images);
        }
    }

    private void initImgs(final List<ImageItem> imgs) {
        if (SUtils.isEmptyArrays(imgs)) {
            tvDelete.setVisibility(View.GONE);
            ivVideoPlay.setVisibility(View.GONE);
        } else {
            tvDelete.setVisibility(View.VISIBLE);
            ivVideoPlay.setVisibility(View.VISIBLE);
        }
        if (imgs.size() < 9) {
            ImageItem imageItem = new ImageItem();
            imageItem.setFake(true);
            imgs.add(imageItem);
        }

        CBPageAdapter cbPageAdapter = null;
        CBViewHolderCreator creator = (CBViewHolderCreator<BannerHolderView>) () -> new BannerHolderView();
        cbPageAdapter = new CBPageAdapter(creator);

        cbContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logs.i("imageItem"+position);
            }

            @Override
            public void onPageSelected(int position) {
                Logs.i("imageItem"+position);
                curImgPos = position;
                if (!SUtils.isEmptyArrays(imgs)) {
                    if (position == imgs.size() - 1) {
                        ImageItem imageItem = imgs.get(imgs.size() - 1);
                        Logs.i("imageItem:"+imageItem.getImagePath()+",,,"+imageItem.isFake());
                        if (imageItem.isFake()) {
                            tvDelete.setVisibility(View.GONE);
                            ivVideoPlay.setVisibility(View.GONE);
                        } else {
                            if(isVideo){
                                ivVideoPlay.setVisibility(View.VISIBLE);
                            }
                            tvDelete.setVisibility(View.VISIBLE);
                        }
                    } else {
                        tvDelete.setVisibility(View.VISIBLE);
                        if(isVideo){
                            ivVideoPlay.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        cbContainer.setPages(cbPageAdapter, imgs).setOnItemClickListener(new OnSimpleClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
        cbContainer.setCanLoop(false);
        cbContainer.setPageIndicator(new int[]{R.drawable.so_grey93_45, R.drawable.so_blue56_45});
        cbContainer.setcurrentitem(curImgPos);
        //cbContainer.setTextPageIndicator(new int[]{R.color.white, R.color.white});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logs.i("resultCode:" + requestCode + ",,," + resultCode + ",,data" + data);
        if (resultCode == RESULT_OK) {
            if (requestCode ==101) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(JumpTo.TYPE_OBJECT);
                if (images != null && images.size() > 0) {
                    curImgPos = 0;
                    initImgs(images);
                    notifyReleaseImgs();
                }
            }
        }else if(resultCode == RESULT_CANCELED){
            if (images.size() < 9) {
                ImageItem imageItem = new ImageItem();
                imageItem.setFake(true);
                images.add(imageItem);
            }
            cbContainer.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            Jzvd.releaseAllVideos();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void notifyReleaseImgs() {
        Intent intent = new Intent(BroadConst.NOTIFY_MEDIA_CHANGED);
        intent.putExtra(JumpTo.TYPE_OBJECT, images);
        intent.putExtra(JumpTo.TYPE_BOOLEAN, isVideo);
        sendBroadcast(intent);
        if (mOption != null) {
            String content = null;
            if (!SUtils.isEmptyArrays(images)) {
                ImageItem imageItem = images.get(0);
                if (!imageItem.isFake()) {
                    content = images.get(0).getImagePath();
                }
            }
           // mOption.getCallback().doSelected(new String[]{content});
        }
    }


}
