package com.summer.demo.ui.mine.release;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;

import com.summer.demo.AppContext;
import com.summer.demo.bean.BaseResp;
import com.summer.demo.constant.ApiConstants;
import com.summer.demo.constant.BroadConst;
import com.summer.demo.module.video.util.CompressListener;
import com.summer.demo.module.video.util.Compressor;
import com.summer.demo.module.video.util.InitListener;
import com.summer.demo.ui.mine.release.bean.SendStatus;
import com.summer.demo.ui.mine.release.bean.TopicDetailInfo;
import com.summer.helper.db.CommonService;
import com.summer.helper.db.DBType;
import com.summer.helper.server.EasyHttp;
import com.summer.helper.server.PostData;
import com.summer.helper.server.RequestCallback;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SFileUtils;
import com.summer.helper.utils.SUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 发送话题
 * Created by xiaqiliang on 2017/4/26.
 */

public class SendTopicVideoService extends Service {

    private String videoPath;
    private Context context;
    protected String content;
    protected String groupId;
    MyHandler myHandler;
    long tempKey;

    protected String videoId;//上传图片时拼成的
    String subjectIds;

    protected boolean isReviseMode;

    protected String topicId;//修改的话题ID

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    protected void init() {
        context = this;
        myHandler = new MyHandler(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (intent == null)
            return;
        content = intent.getStringExtra("key_content");
        subjectIds = intent.getStringExtra("subject_ids");
        videoPath = intent.getStringExtra("videoPath");
        groupId = intent.getStringExtra("key_groupid");
        topicId = intent.getStringExtra("topic_id");
        tempKey = intent.getLongExtra("key_time", 0);
        isReviseMode = intent.getBooleanExtra("isReviseMode", false);
        if (TextUtils.isEmpty(videoPath)) {
            uploadFinalData();
        } else {
            uploadVideo();
        }
    }

    /**
     * 第一步，压缩图片
     */
    private void uploadVideo() {
        final Compressor mCompressor = new Compressor();
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                long size = new File(videoPath).length() / 1024;
                Logs.i("视频原大小" + size);
           /*     if (size < 3000) {
                    sendFileToServer(videoPath);
                    return;
                }*/
                String saveCompressPath = SFileUtils.getVideoDirectory() + System.currentTimeMillis() + "_compress" + SFileUtils.FileType.FILE_MP4;
                execCommand(mCompressor, videoPath, saveCompressPath);
            }

            @Override
            public void onLoadFail(String reason) {

            }
        });

    }

    /**
     * 开始压缩
     *
     * @param currentInputVideoPath
     * @param currentOutputVideoPath
     */
    private void execCommand(Compressor mCompressor, final String currentInputVideoPath, final String currentOutputVideoPath) {
        int mVideoWidth = 0;
        int mVideoHeight = 0;
        int rotation = 0;
        {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            try {
                mmr.setDataSource(currentInputVideoPath);
                mVideoWidth = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));//宽
                mVideoHeight = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));//高
                rotation = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
                Logs.i("width:" + mVideoWidth + ",,,height:" + mVideoHeight);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                mmr.release();
            }
        }
        String mesureSize = mVideoWidth + "x" + mVideoHeight;
        Logs.i("width:::" + mVideoWidth + ",,,," + mVideoHeight + ",,,rotation:" + rotation);
        if (mVideoWidth > 1000) {
            mVideoWidth = mVideoWidth / 2;
            mVideoHeight = mVideoHeight / 2;
            long size = new File(videoPath).length() / 1024;
            Logs.i("视频原大小" + size);//width:1920,,,height:1080
            if (size > 100 * 1000 && mVideoHeight >= 540 && mVideoWidth >= 540) {
                mVideoWidth = mVideoWidth / 2;
                mVideoHeight = mVideoHeight / 2;
            }
        }
        String rotationCmd = "";

        Logs.i("width:::" + mVideoWidth + ",,,," + mVideoHeight);
        if (rotation == 90 || rotation == 270) {
            mesureSize = mVideoHeight + "x" + mVideoWidth;
        }

        String cmd = "-threads 2 -y -i " + currentInputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
                "-crf 26 -acodec aac -ar 44100 -ac 1 -b:a 72k -s " + mesureSize + " " + rotationCmd + currentOutputVideoPath;
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                //SFileUtils.deleteFile(currentInputVideoPath);
                Logs.i("压缩后大小" + new File(currentOutputVideoPath).length() / 1024);
                sendFileToServer(currentOutputVideoPath);
            }

            @Override
            public void onExecFail(String reason) {
                Logs.i("结束视频压缩。。。。。。。。。" + reason);
            }

            @Override
            public void onExecProgress(String message) {
                Logs.i("视频压缩中。。。。。。。。。" + message);
            }
        });
    }


    private void sendFileToServer(String currentOutputVideoPath) {
        if (!TextUtils.isEmpty(currentOutputVideoPath)) {
            if (currentOutputVideoPath.startsWith("http")) {
                uploadFinalData();
            } else {
               //上传文件逻辑，这里做的逻辑是上传到阿里云后返回图片链接，再上传到自己的服务器
            }

        } else {
            handleRequest(false);
        }
    }



    private void uploadFinalData() {
        SummerParameter param = getSummerParameter();
        String token = "一般都有Token";
        String url = null;
        if (!isReviseMode) {

            url = ApiConstants.getHost(ApiConstants.getHostVersion2()) + "groups/" + groupId + "/topics";
            EasyHttp.post(AppContext.getInstance(), token, url, BaseResp.class, param, new RequestCallback<BaseResp>() {
                @Override
                public void done(BaseResp baseResp) {
                    if (baseResp != null) {
                        if (baseResp.isResult()) {
                            Logs.i("发布成功");
                            myHandler.sendEmptyMessage(1);
                        } else {
                            myHandler.obtainMessage(2, baseResp.getMsg()).sendToTarget();
                            Logs.i("。。。" + baseResp.getMsg());

                            handleRequest(false);
                        }
                    }

                }

                @Override
                public void onError(int errorCode, String errorStr) {
                    handleRequest(false);
                }
            });
        } else {
            param = getReviseParameter();
            url = ApiConstants.getHost(ApiConstants.getHostVersion2()) + "groups/" + groupId + "/topics/" + topicId;
            EasyHttp.put(AppContext.getInstance(), token, url, BaseResp.class, param, new RequestCallback<BaseResp>() {
                @Override
                public void done(BaseResp baseResp) {
                    if (baseResp != null) {
                        if (baseResp.isResult()) {
                            myHandler.sendEmptyMessage(1);
                        } else {
                            myHandler.obtainMessage(2, baseResp.getMsg()).sendToTarget();

                            handleRequest(false);
                        }
                    }
                }

                @Override
                public void onError(int errorCode, String errorStr) {
                    handleRequest(false);
                }
            });
        }
    }

    public static class MyHandler extends Handler {
        private final WeakReference<SendTopicVideoService> mActivity;

        public MyHandler(SendTopicVideoService activity) {
            mActivity = new WeakReference<SendTopicVideoService>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SendTopicVideoService activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 1:
                        activity.handleRequest(true);
                        break;
                    case 2:
                        String data = (String) msg.obj;
                        SUtils.makeToast(AppContext.getInstance(), data);
                        break;
                }
            }
        }
    }

    protected SummerParameter getSummerParameter() {
        SummerParameter param = PostData.getPostParameters(context);
        if (videoId != null) {
            param.put("topic_videos", videoId);
        }
        if (!TextUtils.isEmpty(subjectIds)) {
            param.put("hashtag_ids", subjectIds);
        }
        param.put("topic_text", content);
        return param;
    }

    protected SummerParameter getReviseParameter() {
        SummerParameter param = PostData.getPostParameters(context);
        param.put("topic_text", content);
        if (!TextUtils.isEmpty(subjectIds)) {
            param.put("hashtag_ids", subjectIds);
        }
        param.put("with_global_topic",1);
        return param;
    }

    /**
     * 根据发布状态设置结果
     *
     * @param sendResult
     */
    private void handleRequest(boolean sendResult) {
        Logs.i("发布状态:" + sendResult);
        CommonService mService = new CommonService(AppContext.getInstance());
        List<TopicDetailInfo> topics = (List<TopicDetailInfo>) mService.getListData(DBType.SEND_TOPIC, groupId);
        if (topics != null) {
            int size = topics.size();
            for (int i = 0; i < size; i++) {
                TopicDetailInfo info = topics.get(i);
                long tem = info.getTopic_add_time();
                Logs.i("time:" + tem + ",,," + tempKey);
                if (tem == tempKey) {
                    if (sendResult) {//发布成功就删除
                        topics.remove(info);
                    } else {
                        info.setSendStatus(SendStatus.FAILURE);
                    }
                    mService.insert(DBType.SEND_TOPIC, groupId, topics);
                    context.sendBroadcast(new Intent(BroadConst.NOTIFY_FAKE_TOPIC));
                    context.sendBroadcast(new Intent(BroadConst.NOITFY_REFRESH_GROUP));
                    stopSelf();
                    return;
                }
            }
        }
        stopSelf();
        context.sendBroadcast(new Intent(BroadConst.NOITFY_REFRESH_GROUP));
    }

}

