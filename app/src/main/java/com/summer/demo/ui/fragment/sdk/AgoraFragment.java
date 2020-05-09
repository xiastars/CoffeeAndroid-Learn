package com.summer.demo.ui.fragment.sdk;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseFragment;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * @Description:
 * @Author: xiastars@vip.qq.com
 * @CreateDate: 2020/5/6 10:04
 */
public class AgoraFragment extends BaseFragment implements View.OnClickListener {


    private static final int PERMISSION_REQ_ID = 22;

    boolean mCallEnd;
    // App 运行时确认麦克风和摄像头设备的使用权限。
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    @BindView(R.id.remote_video_view_container)
    RelativeLayout remoteVideoViewContainer;
    @BindView(R.id.remote_video_view_container2)
    RelativeLayout remoteVideoViewContainer2;
    @BindView(R.id.local_video_view_container)
    FrameLayout localVideoViewContainer;
    @BindView(R.id.btn_call)
    ImageView btnCall;
    @BindView(R.id.btn_switch_camera)
    ImageView btnSwitchCamera;
    @BindView(R.id.btn_mute)
    ImageView btnMute;
    @BindView(R.id.control_panel)
    RelativeLayout controlPanel;
    @BindView(R.id.activity_video_chat_view)
    RelativeLayout activityVideoChatView;

    SurfaceView mLocalView;

    int streamId;

    @Override
    protected void initView(View view) {
        // 获取权限后，初始化 RtcEngine，并加入频道。
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
    }

    @Override
    protected void dealDatas(int requestType, Object obj) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_agora;
    }


    // Java
    private RtcEngine mRtcEngine;
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onStreamMessage(int uid, int streamId, byte[] data) {
            super.onStreamMessage(uid, streamId, data);
            final String content = new String(data);
            Logs.i("streamid:"+content);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SUtils.makeToast(context,content);
                }
            });

        }

        @Override
        // 注册 onJoinChannelSuccess 回调。
        // 本地用户成功加入频道时，会触发该回调。
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    Logs.i( "Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        @Override
        // 注册 onFirstRemoteVideoDecoded 回调。
        // SDK 接收到第一帧远端视频并成功解码时，会触发该回调。
        // 可以在该回调中调用 setupRemoteVideo 方法设置远端视图。
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Logs.i("First remote video decoded, uid: " + (uid & 0xFFFFFFFFL));
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        // 注册 onUserOffline 回调。
        // 远端用户离开频道或掉线时，会触发该回调。
        public void onUserOffline(final int uid, int reason) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Logs.i( "User offline, uid: " + (uid & 0xFFFFFFFFL));
                    //onRemoteUserLeft();
                }
            });
        }
    };

    // 初始化 RtcEngine 对象。
    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(activity, getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Logs.i(Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupLocalVideo() {

        // 启用视频模块。
        mRtcEngine.enableVideo();

        localVideoViewContainer.removeAllViews();
        Logs.i("----------------");
        mLocalView = RtcEngine.CreateRendererView(context);
        mLocalView.setZOrderMediaOverlay(true);
        mLocalView.setZOrderOnTop(true);
        localVideoViewContainer.addView(mLocalView);
        // 设置本地视图。
        VideoCanvas localVideoCanvas = new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_FIT, 0);
        mRtcEngine.setupLocalVideo(localVideoCanvas);
    }

    // Java
    private void joinChannel() {

        // 调用 joinChannel 方法 加入频道。
       int result =  mRtcEngine.joinChannel("0064d4e032fe69b4eefb86ba5b0da225e0cIACteBJJF6mgAIHXvQD+YrLDrzXiEFo7Nde4fv6ZYHfa7IlBxGkAAAAAEADoGr0IgJ23XgEAAQB/nbde",
               "balanx1", "Extra Optional Data", 0);
       Logs.i("result:"+result);
    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        setupLocalVideo();
        joinChannel();
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    private void setupRemoteVideo(int uid) {
        if(remoteVideoViewContainer.getChildCount()>0){
            SurfaceView mRemoteView = RtcEngine.CreateRendererView(activity);
            remoteVideoViewContainer2.addView(mRemoteView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) remoteVideoViewContainer2.getLayoutParams();
            params.width = SUtils.getDip(context,300);
            params.height = SUtils.getDip(context,500);
            params.leftMargin = SUtils.getDip(context,100)* remoteVideoViewContainer2.getChildCount();
            // 设置远端视图。
            mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            String content = "this is a test";
            mRtcEngine.sendStreamMessage(101,content.getBytes());

        }else{
            SurfaceView mRemoteView = RtcEngine.CreateRendererView(activity);
            remoteVideoViewContainer.addView(mRemoteView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) remoteVideoViewContainer.getLayoutParams();
            params.width = SUtils.getDip(context,300);
            params.height = SUtils.getDip(context,500);
            params.leftMargin = SUtils.getDip(context,100)* remoteVideoViewContainer.getChildCount();
            // 设置远端视图。
            mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            String content = "this is a test";
            mRtcEngine.sendStreamMessage(101,content.getBytes());
        }


    }

    private void leaveChannel() {
        // 离开当前频道。
        mRtcEngine.leaveChannel();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!mCallEnd) {
            leaveChannel();
        }
        RtcEngine.destroy();
    }

    // Java
    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }


    int sendIndex;
    @OnClick({R.id.btn_mute})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mute:
                if(streamId == 0){
                    streamId = mRtcEngine.createDataStream(true,true);
                }


                sendMsg();
                break;
        }
    }

    private void sendMsg(){
        myHandlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendIndex++;
                String content = "这是一条测试消息"+sendIndex;
                mRtcEngine.sendStreamMessage(streamId,content.getBytes());
                sendMsg();
            }
        },200);
    }
}
