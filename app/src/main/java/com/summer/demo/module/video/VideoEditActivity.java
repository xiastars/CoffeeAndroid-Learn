package com.summer.demo.module.video;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.summer.demo.R;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.module.video.util.CompressListener;
import com.summer.demo.module.video.util.Compressor;
import com.summer.demo.module.video.util.EditSpacingItemDecoration;
import com.summer.demo.module.video.util.ExtractFrameWorkThread;
import com.summer.demo.module.video.util.ExtractVideoInfoUtil;
import com.summer.demo.module.video.util.InitListener;
import com.summer.demo.module.video.util.MyVideoView;
import com.summer.demo.module.video.util.OnTrimVideoListener;
import com.summer.demo.module.video.util.PictureUtils;
import com.summer.demo.module.video.util.RangeSeekBar;
import com.summer.demo.module.video.util.TrimVideoUtils;
import com.summer.demo.module.video.util.VideoEditInfo;
import com.summer.demo.module.base.view.CommonSureView5;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.STimeUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.LoadingDialog;
import com.summer.helper.view.review.RRelativeLayout;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 视频编辑界面
 */
public class VideoEditActivity extends BaseActivity {
    private static final String TAG = VideoEditActivity.class.getSimpleName();
    private static final long MIN_CUT_DURATION = 3 * 1000L;// 最小剪辑时间3s
    private static final long MAX_CUT_DURATION = 115 * 1000L;//视频最多剪切多长时间
    private static final int MAX_COUNT_RANGE = 10;//seekBar的区域内一共有多少张图片
    public static final String TRIM_VIDEO = "TRIM_VIDEO";
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_right)
    CommonSureView5 btnRight;
    @BindView(R.id.title)
    RRelativeLayout title;
    @BindView(R.id.uVideoView)
    MyVideoView mVideoView;
    @BindView(R.id.id_rv_id)
    RecyclerView mRecyclerView;
    @BindView(R.id.positionIcon)
    ImageView positionIcon;
    @BindView(R.id.id_seekBarLayout)
    LinearLayout seekBarLayout;
    @BindView(R.id.layout_bottom)
    FrameLayout layoutBottom;
    @BindView(R.id.tv_edit_video)
    TextView tvEditVideo;
    @BindView(R.id.rl_edtview)
    RelativeLayout rlEdtview;
    @BindView(R.id.tv_video_time)
    TextView tvVideoTime;

    private ExtractVideoInfoUtil mExtractVideoInfoUtil;
    LoadingDialog loadingDialog;

    private RangeSeekBar seekBar;
    private VideoEditAdapter videoEditAdapter;
    private float averageMsPx;//每毫秒所占的px
    private float averagePxMs;//每px所占用的ms毫秒
    private String OutPutFileDirPath;
    private ExtractFrameWorkThread mExtractFrameWorkThread;
    private String path;
    private long leftProgress, rightProgress;
    private long scrollPos = 0;
    private int mScaledTouchSlop;
    private int lastScrollX;
    private boolean isSeeking;
    ImageItem videoItem;
    Compressor mCompressor;
    Context context;

    boolean finishEnable;//完成是否可以点击
    private long duration;//视频时长
    private int mMaxWidth;
    int mVideoWidth = 1080;
    int mVideoHeight = 720;
    float mScale = 1L;

    @Override
    protected void dealDatas(int requestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return R.string.edit_video;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_video_edit;
    }

    @Override
    protected void initData(){
        ButterKnife.bind(this);
        context = this;
        btnRight.setVisibility(View.VISIBLE);
        btnRight.changeStyle(true);
        SUtils.initScreenDisplayMetrics(this);
        PermissionUtils.checkReadPermission(this);
        videoItem = (ImageItem) JumpTo.getObject(this);
        if (videoItem != null) {
            path = videoItem.getVideoPath();
            duration = videoItem.getDuration();
        }
        Logs.i("xia", "duration:" + duration);
        if (!new File(path).exists()) {
            SUtils.makeToast(this, "视频文件不存在");
            finish();
            return;
        }
        initVideo();
        initView();
        initEditVideo();
        initPlay();
    }

    private void initVideo() {
        if (!new File(path).exists()) {
            Toast.makeText(this, "视频文件不存在", Toast.LENGTH_LONG).show();
            finish();
        }
        mExtractVideoInfoUtil = new ExtractVideoInfoUtil(path);
        duration = Long.valueOf(mExtractVideoInfoUtil.getVideoLength());
        mMaxWidth = SUtils.screenWidth - SUtils.getDip(this, 70);
        mScaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
    }

    private void initView() {

        changeFinishStyle();
        if (finishEnable) {
            rlEdtview.setVisibility(View.GONE);
            layoutBottom.setVisibility(View.VISIBLE);
        } else {
            rlEdtview.setVisibility(View.VISIBLE);
            layoutBottom.setVisibility(View.GONE);

        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        videoEditAdapter = new VideoEditAdapter(this,
                (SUtils.screenWidth - SUtils.getDip(this, 70)) / 10);
        mRecyclerView.setAdapter(videoEditAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    /**
     * 改变完成的颜色
     */
    private void changeFinishStyle() {
        finishEnable = duration <= MAX_CUT_DURATION;
        if (finishEnable) {
            btnRight.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            btnRight.setTextColor(context.getResources().getColor(R.color.black33));
        }
        tvVideoTime.setText("视频时长:" + duration / 1000 + "s");
    }

    private void initEditVideo() {
        //for video edit
        long startPosition = 0;
        long endPosition = duration;
        int thumbnailsCount;
        int rangeWidth;
        boolean isOver_60_s;
        if (endPosition <= MAX_CUT_DURATION) {
            isOver_60_s = false;
            thumbnailsCount = MAX_COUNT_RANGE;
            rangeWidth = mMaxWidth;
        } else {
            isOver_60_s = true;
            thumbnailsCount = (int) (endPosition * 1.0f / (MAX_CUT_DURATION * 1.0f) * MAX_COUNT_RANGE);
            rangeWidth = mMaxWidth / MAX_COUNT_RANGE * thumbnailsCount;
        }
        mRecyclerView.addItemDecoration(new EditSpacingItemDecoration(SUtils.getDip(this, 35), thumbnailsCount));

        if (isOver_60_s) {
            seekBar = new RangeSeekBar(this, 0L, MAX_CUT_DURATION);
            seekBar.setSelectedMinValue(0L);
            seekBar.setSelectedMaxValue(MAX_CUT_DURATION);
        } else {
            seekBar = new RangeSeekBar(this, 0L, endPosition);
            seekBar.setSelectedMinValue(0L);
            seekBar.setSelectedMaxValue(endPosition);
        }
        seekBar.setMin_cut_time(MIN_CUT_DURATION);//设置最小裁剪时间
        seekBar.setNotifyWhileDragging(true);
        seekBar.setOnRangeSeekBarChangeListener(mOnRangeSeekBarChangeListener);
        seekBarLayout.addView(seekBar);

        Log.d(TAG, "-------thumbnailsCount--->>>>" + thumbnailsCount);
        averageMsPx = duration * 1.0f / rangeWidth * 1.0f;
        Log.d(TAG, "-------rangeWidth--->>>>" + rangeWidth);
        Log.d(TAG, "-------localMedia.getDuration()--->>>>" + duration);
        Log.d(TAG, "-------averageMsPx--->>>>" + averageMsPx);
        OutPutFileDirPath = PictureUtils.getSaveEditThumbnailDir(this);
        int extractW = (SUtils.screenWidth - SUtils.getDip(this, 70)) / MAX_COUNT_RANGE;
        int extractH = SUtils.getDip(this, 55);
        mExtractFrameWorkThread = new ExtractFrameWorkThread(extractW, extractH, mUIHandler, path, OutPutFileDirPath, startPosition, endPosition, thumbnailsCount);
        mExtractFrameWorkThread.start();

        leftProgress = 0;
        if (isOver_60_s) {
            rightProgress = MAX_CUT_DURATION;
        } else {
            rightProgress = endPosition;
        }
        averagePxMs = (mMaxWidth * 1.0f / (rightProgress - leftProgress));
        duration = rightProgress - leftProgress;
        changeFinishStyle();
        Log.d(TAG, "------averagePxMs----:>>>>>" + averagePxMs);
    }

    private void initPlay() {
        mVideoView.setVideoPath(path);
        //设置videoview的OnPrepared监听
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoWidth = mVideoView.getVideoWidth();
                mVideoHeight = mVideoView.getVideoHeight();
                mScale = 320 / (float) mVideoWidth;
                float scale = SUtils.screenWidth / (float) mVideoWidth;
                if (mVideoHeight > mVideoWidth) {
                    scale /= 1.5;
                }
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mVideoView.getLayoutParams();
                params.height = (int) (mVideoHeight * scale);
                params.width = (int) (mVideoWidth * scale);
                mVideoView.invalidate();
                mVideoView.requestLayout();
                mVideoView.start();
                //设置MediaPlayer的OnSeekComplete监听
                mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        if (!isSeeking) {
                            videoStart();
                        }
                    }
                });
            }
        });
        //first
        videoStart();
    }

    private boolean isOverScaledTouchSlop;

    private final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.d(TAG, "-------newState:>>>>>" + newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                isSeeking = false;
//                videoStart();
            } else {
                isSeeking = true;
                if (isOverScaledTouchSlop && mVideoView != null && mVideoView.isPlaying()) {
                    videoPause();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isSeeking = false;
            int scrollX = getScrollXDistance();
            //达不到滑动的距离
            if (Math.abs(lastScrollX - scrollX) < mScaledTouchSlop) {
                isOverScaledTouchSlop = false;
                return;
            }
            isOverScaledTouchSlop = true;
            Log.d(TAG, "-------scrollX:>>>>>" + scrollX);
            //初始状态,why ? 因为默认的时候有35dp的空白！
            if (scrollX == -SUtils.getDip(VideoEditActivity.this, 35)) {
                scrollPos = 0;
            } else {
                // why 在这里处理一下,因为onScrollStateChanged早于onScrolled回调
                if (mVideoView != null && mVideoView.isPlaying()) {
                    videoPause();
                }
                isSeeking = true;
                scrollPos = (long) (averageMsPx * (SUtils.getDip(VideoEditActivity.this, 35) + scrollX));
                Log.d(TAG, "-------scrollPos:>>>>>" + scrollPos);
                leftProgress = seekBar.getSelectedMinValue() + scrollPos;
                rightProgress = seekBar.getSelectedMaxValue() + scrollPos;
                Log.d(TAG, "-------leftProgress:>>>>>" + leftProgress);
                mVideoView.seekTo((int) leftProgress);
            }
            lastScrollX = scrollX;
        }
    };

    /**
     * 水平滑动了多少px
     *
     * @return int px
     */
    private int getScrollXDistance() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemWidth = firstVisibleChildView.getWidth();
        return (position) * itemWidth - firstVisibleChildView.getLeft();
    }

    private ValueAnimator animator;

    private void anim() {
        Log.d(TAG, "--anim--onProgressUpdate---->>>>>>>" + mVideoView.getCurrentPosition());
        if (positionIcon.getVisibility() == View.GONE) {
            positionIcon.setVisibility(View.VISIBLE);
        }
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) positionIcon.getLayoutParams();
        int start = (int) (SUtils.getDip(this, 35) + (leftProgress/*mVideoView.getCurrentPosition()*/ - scrollPos) * averagePxMs);
        int end = (int) (SUtils.getDip(this, 35) + (rightProgress - scrollPos) * averagePxMs);
        animator = ValueAnimator
                .ofInt(start, end)
                .setDuration((rightProgress - scrollPos) - (leftProgress/*mVideoView.getCurrentPosition()*/ - scrollPos));
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.leftMargin = (int) animation.getAnimatedValue();
                positionIcon.setLayoutParams(params);
            }
        });
        animator.start();
    }

    private final MainHandler mUIHandler = new MainHandler(this);

    @OnClick({R.id.ll_back, R.id.tv_edit_video, R.id.btn_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                this.finish();
                break;
            case R.id.tv_edit_video:
                rlEdtview.setVisibility(View.GONE);
                layoutBottom.setVisibility(View.VISIBLE);
                tvVideoTime.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_right:
                if (!finishEnable) {
                    SUtils.makeToast(context, "请先编辑视频!");
                    return;
                }
                compressVideo();
                break;
        }
    }

    /**
     * 裁剪和压缩视频
     */
    private void compressVideo() {
        loadingDialog = new LoadingDialog(context);
        loadingDialog.startLoading("正在压缩中");
        final String saveCutPath = SFileUtils.getVideoDirectory() + System.currentTimeMillis() + "_cut" + SFileUtils.FileType.FILE_MP4;
        try {
            TrimVideoUtils.startTrim(new File(path), saveCutPath, leftProgress, rightProgress, new OnTrimVideoListener() {
                @Override
                public void onTrimStarted() {

                }

                @Override
                public void getResult(Uri uri) {
                    //JumpTo.getInstance().commonJump(context, ShowVideoDialog.class,saveCutPath);
                    mCompressor = new Compressor();
                    mCompressor.loadBinary(new InitListener() {
                        @Override
                        public void onLoadSuccess() {
                            //cutCommand(path, saveCutPath, leftProgress, rightProgress);
                            Logs.i("裁剪后的大小" + new File(saveCutPath).length() / 1024);
                            String saveCompressPath = SFileUtils.getVideoDirectory() + System.currentTimeMillis() + "_compress" + SFileUtils.FileType.FILE_MP4;
                            execCommand(saveCutPath, saveCompressPath);
                        }

                        @Override
                        public void onLoadFail(String reason) {

                        }
                    });
                }

                @Override
                public void cancelAction() {

                }

                @Override
                public void onError(String message) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void cutCommand(String inputVideo, final String outPutVideo, long startTime, long endTime) {
        String startTimeFormat = STimeUtils.getOverTimeString(startTime);
        String endTimeFormat = STimeUtils.getOverTimeString(endTime);

        final String cmd = "-i " + inputVideo + " -ss " + startTimeFormat + " -to " + endTimeFormat + " -acodec aac -ar 8000 -ac 2 -b:a 5k " + outPutVideo;

        Logs.i("cmd:" + cmd);
        mCompressor = new Compressor();
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                mCompressor.execCommand(cmd, new CompressListener() {
                    @Override
                    public void onExecSuccess(String message) {

                        String saveCompressPath = SFileUtils.getVideoDirectory() + System.currentTimeMillis() + "_compress" + SFileUtils.FileType.FILE_MP4;
                        execCommand(outPutVideo, saveCompressPath);
                    }

                    @Override
                    public void onExecFail(String reason) {
                        cancelLoading(true);
                        Logs.i("结束视频裁剪。。。。。。。。。" + reason);
                    }

                    @Override
                    public void onExecProgress(String message) {
                        Logs.i("结束视频裁剪。。。。。。。。。" + message);
                    }
                });
            }

            @Override
            public void onLoadFail(String reason) {
                cancelLoading(true);
            }
        });

    }

    private void cancelLoading(boolean failure) {
        if (loadingDialog != null) {
            loadingDialog.cancelLoading();
        }
        if (failure) {
            SUtils.makeToast(context, "处理视频失败");
        }
    }

    /**
     * 开始压缩
     *
     * @param currentInputVideoPath
     * @param currentOutputVideoPath
     */
    private void execCommand(final String currentInputVideoPath, final String currentOutputVideoPath) {
        int measureWidth = 0;
        int measureHeight = 0 ;
        if (mVideoWidth > mVideoHeight) {
            measureWidth = 640;
            measureHeight = 360;
        }else{
            measureWidth = 360;
            measureHeight = 640;
        }
        String mesureSize = measureWidth + "x" + measureHeight;
        Logs.i("scale:" + mScale + ",,,," + measureWidth + ",,,," + measureHeight);
        String cmd = "-threads 2 -y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 26 -acodec aac -ar 8000 -ac 2 -b:a 5k -s " + mesureSize + " " + currentOutputVideoPath;
    /*    String cmd = "-threads 2 -y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 28 -acodec aac -ar 44100 -ac 2 -b:a 5k -qscale 200 " + currentOutputVideoPath;*/
        Logs.i("视频压缩 :"+cmd);
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                SFileUtils.deleteFile(currentInputVideoPath);
                cancelLoading(false);
                Intent intent = new Intent(TRIM_VIDEO);
                ArrayList<ImageItem> items = new ArrayList<>();
                Logs.i("file::::" + new File(currentOutputVideoPath).length() / 1024);
                videoItem.setVideoPath(currentOutputVideoPath);
                items.add(videoItem);
                intent.putExtra(JumpTo.TYPE_OBJECT, items);
                context.sendBroadcast(intent);
                VideoEditActivity.this.finish();
            }

            @Override
            public void onExecFail(String reason) {
                cancelLoading(true);
                Logs.i("结束视频压缩。。。。。。。。。" + reason);
            }

            @Override
            public void onExecProgress(String message) {
                Logs.i("结束视频压缩。。。。。。。。。" + message);
            }
        });
    }

    private static class MainHandler extends Handler {
        private final WeakReference<VideoEditActivity> mActivity;

        MainHandler(VideoEditActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            VideoEditActivity activity = mActivity.get();
            if (activity != null) {
                if (msg.what == ExtractFrameWorkThread.MSG_SAVE_SUCCESS) {
                    if (activity.videoEditAdapter != null) {
                        VideoEditInfo info = (VideoEditInfo) msg.obj;
                        activity.videoEditAdapter.addItemVideoInfo(info);
                    }
                }
            }
        }
    }

    private final RangeSeekBar.OnRangeSeekBarChangeListener mOnRangeSeekBarChangeListener = new RangeSeekBar.OnRangeSeekBarChangeListener() {
        @Override
        public void onRangeSeekBarValuesChanged(RangeSeekBar bar, long minValue, long maxValue, int action, boolean isMin, RangeSeekBar.Thumb pressedThumb) {
            Log.d(TAG, "-----minValue----->>>>>>" + minValue);
            Log.d(TAG, "-----maxValue----->>>>>>" + maxValue);
            leftProgress = minValue + scrollPos;
            rightProgress = maxValue + scrollPos;
            Log.d(TAG, "-----leftProgress----->>>>>>" + leftProgress);
            Log.d(TAG, "-----rightProgress----->>>>>>" + rightProgress);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.d(TAG, "-----ACTION_DOWN---->>>>>>");
                    isSeeking = false;
                    videoPause();
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d(TAG, "-----ACTION_MOVE---->>>>>>");
                    isSeeking = true;
                    mVideoView.seekTo((int) (pressedThumb == RangeSeekBar.Thumb.MIN ?
                            leftProgress : rightProgress));
                    break;
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "-----ACTION_UP--leftProgress--->>>>>>" + leftProgress);
                    isSeeking = false;
                    //从minValue开始播
                    mVideoView.seekTo((int) leftProgress);
//                    videoStart();
                    break;
                default:
                    break;
            }
            duration = rightProgress - leftProgress;
            changeFinishStyle();
        }
    };


    private void videoStart() {
        Log.d(TAG, "----videoStart----->>>>>>>");
        mVideoView.start();
        positionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        anim();
        handler.removeCallbacks(run);
        handler.post(run);
    }

    private void videoProgressUpdate() {
        long currentPosition = mVideoView.getCurrentPosition();
        Log.d(TAG, "----onProgressUpdate-cp---->>>>>>>" + currentPosition);
        if (currentPosition >= (rightProgress)) {
            mVideoView.seekTo((int) leftProgress);
            positionIcon.clearAnimation();
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
            anim();
        }
    }

    private void videoPause() {
        isSeeking = false;
        if (mVideoView != null && mVideoView.isPlaying()) {
            mVideoView.pause();
            handler.removeCallbacks(run);
        }
        Log.d(TAG, "----videoPause----->>>>>>>");
        if (positionIcon.getVisibility() == View.VISIBLE) {
            positionIcon.setVisibility(View.GONE);
        }
        positionIcon.clearAnimation();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null) {
            mVideoView.seekTo((int) leftProgress);
//            videoStart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null && mVideoView.isPlaying()) {
            videoPause();
        }
    }

    private Handler handler = new Handler();
    private Runnable run = new Runnable() {

        @Override
        public void run() {
            videoProgressUpdate();
            handler.postDelayed(run, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (animator != null) {
            animator.cancel();
        }
        if (mVideoView != null) {
            mVideoView.stop();
        }
        if (mExtractVideoInfoUtil != null) {
            mExtractVideoInfoUtil.release();
        }
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
        if (mExtractFrameWorkThread != null) {
            mExtractFrameWorkThread.stopExtract();
        }
        mUIHandler.removeCallbacksAndMessages(null);
        handler.removeCallbacksAndMessages(null);
        if (!TextUtils.isEmpty(OutPutFileDirPath)) {
            PictureUtils.deleteFile(new File(OutPutFileDirPath));
        }
    }
}
