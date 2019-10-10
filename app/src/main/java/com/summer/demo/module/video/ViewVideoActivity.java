package com.summer.demo.module.video;

import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.module.video.util.MyVideoView;
import com.summer.helper.server.PostData;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.LoadingDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 单纯浏览一个视频
 * Created by xiaqiliang on 2017/4/1.
 */
public class ViewVideoActivity extends BaseActivity {
    LoadingDialog loadingDialog;
    String path;

    @BindView(R.id.vd_play)
    MyVideoView mVideoView;
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void dealDatas(int rquestCode, Object obj) {

    }

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_show_video;
    }

    @Override
    protected void initData() {
        path = JumpTo.getString(this);
        if (path != null && path.startsWith("//")) {
            path = PostData.OOSHEAD + ":" + path;
        }
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(context);
        loadingDialog.startLoading();
        setLayoutFullscreen(true);
        changeHeaderStyleTrans(context.getResources().getColor(R.color.half_grey));
        removeTitle();
        final RelativeLayout parent = (RelativeLayout) findViewById(R.id.rl_parent);
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = SUtils.screenWidth;
        lp.height = SUtils.screenHeight;
        this.getWindow().setAttributes(lp);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ViewVideoActivity.this.finish();
            }
        });
        // 开始播放
        setVideoUrl(path);
    }

    public void setVideoUrl(final String url) {
        String filepath = SFileUtils.getVideoDirectory();
        String fileName = SUtils.getUrlHashCode(url) + SFileUtils.FileType.FILE_MP4;
        File file = new File(filepath + fileName);
        Logs.i("file:"+file.exists()+",,");
        if (file.exists()) {
            mVideoView.setVideoPath(file.getAbsolutePath());
        } else {
            mVideoView.setVideoPath(url);
            SUtils.downloadVideo(context,url);
        }
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                loadingDialog.cancelLoading();
                mVideoView.setLooping(true);
                mVideoView.start();
                float widthF = mVideoView.getVideoWidth();
                float heightF = mVideoView.getVideoHeight();
                ViewGroup.LayoutParams layoutParams = mVideoView.getLayoutParams();
                layoutParams.width = SUtils.screenWidth;
                layoutParams.height = (int) (SUtils.screenWidth / widthF * heightF);
                mVideoView.setLayoutParams(layoutParams);
            }
        });
    }

}
