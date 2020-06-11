package com.summer.demo.ui.mine.release;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.ghnor.flora.callback.Callback;
import com.summer.demo.AppContext;
import com.summer.demo.R;
import com.summer.demo.bean.BaseResp;
import com.summer.demo.constant.ApiConstants;
import com.summer.demo.constant.BroadConst;
import com.summer.demo.helper.ImgHelper;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.ui.main.MainActivity;
import com.summer.demo.ui.mine.release.bean.SendStatus;
import com.summer.demo.ui.mine.release.bean.TopicDetailInfo;
import com.summer.helper.db.CommonService;
import com.summer.helper.db.DBType;
import com.summer.helper.server.EasyHttp;
import com.summer.helper.server.PostData;
import com.summer.helper.server.RequestCallback;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送话题
 * Created by xiaqiliang on 2017/4/26.
 */

public class SendTopicService extends Service {

    private ArrayList<ImageItem> arrayList;
    private int loadIndex; // 标识已上传多图的数量
    private Context context;
    protected String content;
    protected String groupId;
    MyHandler myHandler;
    long tempKey;

    protected String images;//上传图片时拼成的

    protected boolean isReviseMode;

    protected String SERVER_URL;

    protected String topicId;//修改的话题ID

    String imgIds;
    //发布类型
    String publishType;
    //被提问人
    String questionee;
    //付费类型
    String issueType;
    //付费额度
    String issueMoney;
    //匿名
    String anonymous;
    String userName;
    String audioPath;//音频路径
    String audioId;//音频ID
    String subjectIds;
    String address;
    boolean needWatermark;//需要水印否

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
        needWatermark = intent.getBooleanExtra("needWatermark",false);
        address = intent.getStringExtra("address");
        content = intent.getStringExtra("key_content");
        questionee = intent.getStringExtra("questionee_id");
        issueType = intent.getStringExtra("issue_type");
        groupId = intent.getStringExtra("key_groupid");
        publishType = intent.getStringExtra("key_type");
        subjectIds = intent.getStringExtra("subject_ids");
        topicId = intent.getStringExtra("topic_id");
        issueMoney = intent.getStringExtra("issue_money");
        anonymous = intent.getStringExtra("key_anonymous");
        audioPath = intent.getStringExtra("key_audio_path");
        userName = intent.getStringExtra("key_username");
        tempKey = intent.getLongExtra("key_time", 0);
        isReviseMode = intent.getBooleanExtra("isReviseMode", false);
        loadIndex = 0;
        arrayList = (ArrayList<ImageItem>) intent.getSerializableExtra("key_images");
        SERVER_URL = "groups/" + groupId + "/topics";
        if (!TextUtils.isEmpty(groupId)) {
            int isSupportWaterMark = WaterMarkHelper.getGroupSupportWaterMark(groupId);
            WaterMarkHelper.setSupportWatermark(isSupportWaterMark > 0);
        }
        images = "";
        imgIds = "";
        audioId = null;

        if (SUtils.isEmptyArrays(arrayList)) {
            sendTopic();
        } else {
            // 上传多图的帖子,最多重试三次
            imgIds = "";
            final ImageItem postContent = arrayList.get(loadIndex);
            if (!TextUtils.isEmpty(postContent.getImagePath())) {
                if (loadIndex == 0) {
                    createNotifcation(getString(R.string.img_uploading) + "  " + loadIndex + "/" + arrayList.size(), arrayList.size(), loadIndex);
                }
                loadImages();
            }
        }
    }

    private void loadImages() {
        if (loadIndex >= arrayList.size()) {
            handleRequest(false);
            return;
        }
        final ImageItem postContent = arrayList.get(loadIndex);
        String imagePath = postContent.getImagePath();

        if (!TextUtils.isEmpty(imagePath)) {
            if (imagePath.startsWith("http")) {
                loadImgNext("topicID");
            } else {
                ImgHelper.compressImg(context, needWatermark, imagePath, new Callback<String>() {
                    @Override
                    public void callback(String s) {
                        /*
                        BaseUtils.sendGroupImg(context, s, groupId, userName, new OnResponseImgListener() {
                            @Override
                            public void succeed(String imgId, String imgUrl) {
                                loadImgNext(imgId);
                            }

                            @Override
                            public void failure() {
                                handleRequest(false);
                                notificationManager.cancelAll();
                                Logs.i("code:上传失败");
                            }
                        });

                         */
                    }
                });
            }

        } else {
            notificationManager.cancelAll();
            handleRequest(false);
        }

    }

    private void loadImgNext(String url) {
        loadIndex++;
        images = (TextUtils.isEmpty(images)) ? url : (images + "," + url);
        Logs.i("images:" + images);
        //如果上传的总数达到了总数据量
        createNotifcation(getString(R.string.img_uploading) + "  "
                        + loadIndex + "/" + arrayList.size(),
                arrayList.size(), loadIndex);
        if (loadIndex == arrayList.size()) {
            Logs.i("整体发布");
            notificationManager.cancelAll();
            sendTopic();
            imgIds += url + ",";
        } else {
            imgIds += url + ",";
            Logs.i("上传成功，正在上传第" + loadIndex + "张");
            myHandler.sendEmptyMessage(0);
        }
    }

    public static class MyHandler extends Handler {
        private final WeakReference<SendTopicService> mActivity;

        public MyHandler(SendTopicService activity) {
            mActivity = new WeakReference<SendTopicService>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SendTopicService activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 0:
                        activity.loadImages();
                        break;
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


    /**
     * 发布话题
     */
    private void sendTopic() {
        if (!TextUtils.isEmpty(audioPath)) {//如果有音频
            /*
            BaseUtils.sendAudio(context, audioPath, new OnResponseImgListener() {
                @Override
                public void succeed(String imgId, String imgUrl) {
                    audioId = imgId;
                    sendCompleteTopic();
                }

                @Override
                public void failure() {
                    handleRequest(false);
                    if (notificationManager != null) {
                        notificationManager.cancelAll();
                    }
                    Logs.i("code:上传失败");
                }
            });
        } else {
            sendCompleteTopic();
        }

             */
        }
    }

    private void sendCompleteTopic() {
        SummerParameter param = getSummerParameter();
        String token = "FSXQSharedPreference.getInstance().getAppToken()";
        String url = null;
        if (!isReviseMode) {

            url = ApiConstants.getHost(ApiConstants.getHostVersion2()) + "groups/" + groupId + "/topics?with_global_topic=1";
            EasyHttp.post(AppContext.getInstance(), token, url, BaseResp.class, param, new RequestCallback<BaseResp>() {
                @Override
                public void done(BaseResp baseResp) {
                    if (baseResp != null) {
                        if (baseResp.isResult()) {
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
                    Logs.i("err:" + errorStr + ",,," + errorCode);
                    handleRequest(false);
                }
            });
        } else {
            param = getReviseParameter();
            url = ApiConstants.getHost(ApiConstants.getHostVersion2()) + "groups/" + groupId + "/topics/" + topicId + "?with_global_topic=1";
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

    protected SummerParameter getSummerParameter() {
        SummerParameter param = PostData.getPostParameters(context);
        if (images != null) {
            param.put("topic_images", images);
        }
        param.put("topic_text", content);
        if (!TextUtils.isEmpty(questionee)) {
            param.put("questionee_id", questionee);
        }
        if (!TextUtils.isEmpty(audioId)) {
            param.put("topic_voices", audioId);
        }
        if (!TextUtils.isEmpty(issueType)) {
            param.put("issue_type", issueType);
        }
        if (!TextUtils.isEmpty(issueMoney)) {
            param.put("issue_money", issueMoney);
        }
        if (!TextUtils.isEmpty(anonymous)) {
            param.put("anonymous", anonymous);
        }
        if (!TextUtils.isEmpty(subjectIds)) {
            param.put("hashtag_ids", subjectIds);
        }
        param.put("address", address);
        return param;
    }

    protected SummerParameter getReviseParameter() {
        SummerParameter param = PostData.getPostParameters(context);
        param.put("topic_images", images);
        if (!TextUtils.isEmpty(subjectIds)) {
            param.put("hashtag_ids", subjectIds);
        }
        param.put("address", address);
        param.put("topic_text", content);
        return param;
    }

    /**
     * 根据发布状态设置结果
     *
     * @param sendResult
     */
    private void handleRequest(boolean sendResult) {
        CommonService mService = new CommonService(AppContext.getInstance());
        List<TopicDetailInfo> topics = (List<TopicDetailInfo>) mService.getListData(DBType.SEND_TOPIC, groupId);
        if (topics != null) {
            int size = topics.size();
            for (int i = 0; i < size; i++) {
                TopicDetailInfo info = topics.get(i);
                long tem = info.getTopic_add_time();
                if (tem == tempKey) {
                    if (sendResult) {//发布成功就删除
                        topics.remove(info);
                    } else {
                        info.setSendStatus(SendStatus.FAILURE);
                    }
                    mService.insert(DBType.SEND_TOPIC, groupId, topics);
                    context.sendBroadcast(new Intent(BroadConst.NOTIFY_FAKE_TOPIC));
                    context.sendBroadcast(new Intent(BroadConst.NOITFY_REFRESH_GROUP));
                    sendNext();
                    stopSelf();
                    return;
                }
            }
        }
        stopSelf();
        context.sendBroadcast(new Intent(BroadConst.NOITFY_REFRESH_GROUP));
    }

    private void sendNext() {
        CommonService mService = new CommonService(AppContext.getInstance());
        List<TopicDetailInfo> topics = (List<TopicDetailInfo>) mService.getListData(DBType.SEND_TOPIC, groupId);
        if (topics != null) {
            int size = topics.size();
            for (int i = 0; i < size; i++) {
                TopicDetailInfo info = topics.get(i);
                int sendStatus = info.getSendStatus();
                if (sendStatus == SendStatus.WAITING) {
                    //重新发送
                    //ReleaseTopicActivity.retrySend(context, info);
                    return;
                }
            }
        }
    }

    // 在标题栏上显示通知
    private NotificationManager notificationManager;
    private Notification notification;

    private void createNotifcation(String msg, int size, int oneof) {
        try {
            if (notificationManager == null) {
                notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            }
            notification = new Notification(R.drawable.ic_launcher, msg, System.currentTimeMillis());
            notification.defaults = Notification.DEFAULT_LIGHTS;
            notification.flags |= Notification.FLAG_NO_CLEAR;
            notification.when = System.currentTimeMillis();
            notification.iconLevel = 5;
            notification.icon = R.drawable.ic_launcher;
            // 使用notification.xml文件作VIEW
            notification.contentView = new RemoteViews(getPackageName(), R.layout.notfication_send);
            notification.contentView.setViewVisibility(R.id.uploadpb, View.VISIBLE);
            notification.contentView.setTextViewText(R.id.uploadText, msg);
            notification.contentView.setProgressBar(R.id.uploadpb, size, oneof, false);
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            notification.contentIntent = contentIntent;
            notificationManager.notify(1060, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

