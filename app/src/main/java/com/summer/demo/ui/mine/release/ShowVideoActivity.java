package com.summer.demo.ui.mine.release;

import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.dialog.EasyLoadingDialog;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.module.video.util.MyVideoView;
import com.summer.demo.module.video.util.VideoCacheManager;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;

/**
 * Created by xiastars on 2017/4/1.
 */
public class ShowVideoActivity extends BaseActivity {
    ViewGroup view;
    String path;
    @BindView(R.id.vd_play)
    MyVideoView mVideoView;
    @BindView(R.id.rl_parent)
    RelativeLayout rlParent;
    EasyLoadingDialog easyLoadingDialog;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void dealDatas(int rquestCode,Object obj) {

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
        setLayoutFullscreen();
        changeHeaderStyleTrans(context.getResources().getColor(R.color.white));
        removeTitle();
        easyLoadingDialog = new EasyLoadingDialog(context);
        easyLoadingDialog.show();
        final RelativeLayout parent = (RelativeLayout) findViewById(R.id.rl_parent);
/*        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.width = SUtils.screenWidth;
        lp.height = SUtils.screenHeight;
        this.getWindow().setAttributes(lp);*/
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        // 开始播放
        setVideoUrl(path);
    }

    public void setVideoUrl(final String url) {
        if(url.startsWith("http")){
            mVideoView.setVideoPath( VideoCacheManager.getManager(context).getCacheVideoUrl(url));
        }else{
            mVideoView.setVideoPath(url);
        }


        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.setLooping(true);
                mVideoView.start();
                easyLoadingDialog.cancelDialog();
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
