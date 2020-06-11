package com.summer.demo.ui.mine.release;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.constant.BroadConst;
import com.summer.demo.dialog.BaseTipsDialog;
import com.summer.demo.listener.OnAudioPlayListener;
import com.summer.demo.module.album.util.ImageItem;
import com.summer.demo.module.album.util.SelectPhotoHelper;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.ui.mine.release.audio.AudioHelper;
import com.summer.demo.ui.mine.release.audio.OnAudioRecordListener;
import com.summer.demo.ui.mine.release.audio.PlayAudioHelper;
import com.summer.demo.ui.mine.release.bean.GroupDetInfo;
import com.summer.demo.ui.mine.release.bean.MarUser;
import com.summer.demo.ui.mine.release.bean.SubjectInfo;
import com.summer.demo.ui.mine.release.bean.SubjectTopInfo;
import com.summer.demo.ui.mine.release.view.CircleProgressImageView;
import com.summer.demo.ui.mine.release.view.TagGroup;
import com.summer.demo.ui.mine.release.view.ToggleButton;
import com.summer.demo.utils.BaseUtils;
import com.summer.demo.utils.CUtils;
import com.summer.demo.utils.TextWebUtils;
import com.summer.demo.view.CommonSureView5;
import com.summer.helper.listener.OnReturnObjectClickListener;
import com.summer.helper.permission.PermissionUtils;
import com.summer.helper.server.PostData;
import com.summer.helper.server.SummerParameter;
import com.summer.helper.utils.JumpTo;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 发布主题页面
 */

public class ReleaseTopicActivity extends BaseActivity implements View.OnClickListener {

    protected int textNum = 10000;

    @BindView(R.id.group_content)
    public EditText mEtGroupDesc;
    @BindView(R.id.tv_audio_time)
    TextView tvAudioTime;

    CommonSureView5 btnSend;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.iv_watermark)
    ImageView ivWatermark;
    @BindView(R.id.ll_watermark)
    LinearLayout llWatermark;
    @BindView(R.id.rl_audio_container)
    RelativeLayout rlContainer;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.ll_audio_play)
    RelativeLayout rlAudioPlay;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_available)
    TextView tvAvailable;
    @BindView(R.id.tg_subject)
    TagGroup tgSubject;
    @BindView(R.id.tv_renew_detail)
    TextView tvRenewDetail;
    @BindView(R.id.tv_renew)
    TextView tvRenew;
    @BindView(R.id.rl_renew)
    RelativeLayout rlRenew;
    @BindView(R.id.ll_subject)
    public LinearLayout llSubject;
    @BindView(R.id.tv_record)
    TextView tvRecord;
    @BindView(R.id.iv_record)
    ImageView ivRecord;
    @BindView(R.id.rl_more_pic)
    RelativeLayout rlMorePic;
    @BindView(R.id.ll_record_unset)
    LinearLayout llRecordUnset;
    @BindView(R.id.tv_record_time)
    TextView tvRecordTime;
    @BindView(R.id.iv_recording)
    ImageView ivRecording;
    @BindView(R.id.iv_left)
    ProgressBar ivLeft;
    @BindView(R.id.iv_right)
    ProgressBar ivRight;
    @BindView(R.id.rl_recording)
    RelativeLayout rlRecording;
    @BindView(R.id.tv_autio_time)
    TextView tvAutioTime;
    @BindView(R.id.iv_audio_play)
    CircleProgressImageView ivAudioPlay;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_send)
    TextView tvSend;
    @BindView(R.id.ll_audio_bottom)
    LinearLayout llAudioBottom;
    @BindView(R.id.rl_recoded)
    RelativeLayout rlRecoded;
    @BindView(R.id.rl_audio)
    RelativeLayout rlAudio;
    @BindView(R.id.rl_video_mark)
    RelativeLayout rlVideoMark;
    @BindView(R.id.iv_pics)
    ImageView ivPics;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_relase_audio)
    TextView tvReleaseAudio;
    @BindView(R.id.tv_relase_video)
    TextView tvReleaseVideo;
    @BindView(R.id.tb_anony)
    public ToggleButton tbAnony;

    AudioHelper audioHelper;

    String localAddress;

    protected boolean isAnony;
    protected boolean needWatermark = true;
    protected boolean isAskMode = false;//当前是提问模式
    protected float divideSize = 1.5f;

    public final static int REQUEST_HANDLE_MEDIA = 1001;//处理图片视频

    public ArrayList<ImageItem> images = new ArrayList<ImageItem>();//这是选择的图片数组
    final int REQUEST_PHOTO = 7;

    final int REQUEST_GROUP_DETAIL = 1;//请求星球信息

    private String selectVideo;
    private ImageItem videoItem;

    protected GroupDetInfo detailInfo;

    protected String sendContent;
    protected String groupId;
    boolean isReviseMode = false;
    String topicId;//修改的话题ID

    String audioPath;//音频地址
    int audioLength;

    int sendType;//0是普通，1是视频,2是音频

    boolean needNotify;
    String userAlias;
    protected String BACK_TAG = "discuss_back_confirm";
    protected String BACK_CANCEL = "discuss_back_cancel";

    PlayAudioHelper playAudioHelper;
    boolean isMaster;
    int fromWhere;
    List<SubjectInfo> subjects;
    SubjectInfo passedSubject;//从参与讨论过来的
    protected boolean isSending;

    @Override
    protected int setTitleId() {
        return 0;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_topic_ask_publish;
    }

    public static void showVideoOrAudio(Context context, GroupDetInfo detailInfo, String videoPath, String audioPath, String contentText, String topicId, String groupId,String address) {
        Intent intent = new Intent(context, ReleaseTopicActivity.class);
        intent.putExtra(JumpTo.TYPE_OBJECT, (Serializable) detailInfo);
        if (detailInfo == null) {
            intent.putExtra("group_id", groupId);
        }
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("audioPath", audioPath);
        intent.putExtra("content", contentText);
        intent.putExtra("reviseMode", true);
        intent.putExtra("topicId", topicId);
        intent.putExtra("address",address);
        context.startActivity(intent);
    }

    public static void show(Context context, GroupDetInfo detailInfo, ArrayList<ImageItem> img, String contentText, String topicId, String groupId,String address) {
        Intent intent = new Intent(context, ReleaseTopicActivity.class);
        intent.putExtra(JumpTo.TYPE_OBJECT, (Serializable) detailInfo);
        intent.putExtra("imgs", (Serializable) img);
        if (detailInfo == null) {
            intent.putExtra("group_id", groupId);
        }
        intent.putExtra("address",address);
        intent.putExtra("content", contentText);
        intent.putExtra("reviseMode", true);
        intent.putExtra("topicId", topicId);
        context.startActivity(intent);
    }

    /**
     * 从发现模块跳转
     *
     * @param context
     * @param groupId
     */
    public static void show(Context context, String groupId) {
        Intent intent = new Intent(context, ReleaseTopicActivity.class);
        intent.putExtra("group_id", groupId);
        intent.putExtra("from_where", 1);
        context.startActivity(intent);
    }

    /**
     * 从发现模块跳转
     *
     * @param context
     * @param groupId
     */
    public static void show(Context context, String groupId, SubjectInfo subjectInfo) {
        Intent intent = new Intent(context, ReleaseTopicActivity.class);
        intent.putExtra("group_id", groupId);
        intent.putExtra("subjectInfo", subjectInfo);
        context.startActivity(intent);
    }

    public static void show(Context context, GroupDetInfo detailInfo, String contentText) {
        Intent intent = new Intent(context, ReleaseTopicActivity.class);
        intent.putExtra(JumpTo.TYPE_OBJECT, (Serializable) detailInfo);
        intent.putExtra("content", contentText);
        intent.putExtra("tagType", true);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        setBackTag("ac_release");
        setTitle("发表主题");
        changeViewBackRes(R.drawable.publish_close_icon);

        detailInfo = (GroupDetInfo) JumpTo.getObject(this);
        fromWhere = getIntent().getIntExtra("from_where", 0);
        Intent intent = getIntent();
        ArrayList<ImageItem> imgs = (ArrayList<ImageItem>) intent.getSerializableExtra("imgs");
        isReviseMode = intent.getBooleanExtra("reviseMode", false);
        if (imgs != null) {
            images.addAll(imgs);
            SUtils.setPic(ivPics, images.get(0).getImagePath());
        }
        topicId = intent.getStringExtra("topicId");
        String content = intent.getStringExtra("content");
        if (!TextUtils.isEmpty(content)) {
            mEtGroupDesc.setText(content);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

            spannableStringBuilder = TextWebUtils.generateOneSpan(context, spannableStringBuilder, content, mEtGroupDesc.getTextSize(), getResColor(R.color.grey_4a));
            spannableStringBuilder.append(" ");
            mEtGroupDesc.setText(spannableStringBuilder);
            SUtils.setSelection(mEtGroupDesc);

        }
        passedSubject = (SubjectInfo) intent.getSerializableExtra("subjectInfo");
        if (passedSubject != null) {
            appendNewTag(passedSubject);
        }
        showSendButton();
        String videoPath = intent.getStringExtra("videoPath");
        if (!TextUtils.isEmpty(videoPath)) {
            showModifyVideoMode(videoPath);
        }
        String address = intent.getStringExtra("address");
        if(!TextUtils.isEmpty(address)){
            localAddress = address;
            tvAddress.setText(address);
        }
        String audioPath = intent.getStringExtra("audioPath");
        if (!TextUtils.isEmpty(audioPath)) {
            showModifyAudioMode(audioPath);
        }
        tgSubject.setNormalBackground(R.drawable.so_white_greyd2_45);
        tgSubject.setTextNormalColor(getResColor(R.color.grey_93));
        tgSubject.setShouldChangeStatus(true);
        tgSubject.setSelectedBackground(R.drawable.gradient_blue8f_blue56_45);
        tgSubject.setTextSelectedColor(getResColor(R.color.white));
        tgSubject.setAppendTagMark(false);
        if (isReviseMode) {
            btnSend.changeStyle(true);
            tvReleaseAudio.setVisibility(View.GONE);
            tvReleaseVideo.setVisibility(View.GONE);
        }
        initTagView();
        loadData();
        if (detailInfo == null) {
            String groupId = intent.getStringExtra("group_id");
            if (groupId == null) {
                groupId = MarUser.DEFAULT_GROUP_ID;
            }
            reqeustGroupInfo(groupId);
            return;
        }
        initTopicView();
    }

    public void setShowAsk() {
        llAddress.setVisibility(View.INVISIBLE);
        llSubject.setVisibility(View.GONE);
        tvReleaseAudio.setVisibility(View.GONE);
        tvReleaseVideo.setVisibility(View.GONE);
        tbAnony.setShowAnony();
        tbAnony.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                isAnony = on;
                if (isAnony) {
                    needWatermark = true;
                    changeWatermarkState();
                }
            }
        });
    /*    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llWatermark.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);*/
    }

    private void initTopicView() {
        WaterMarkHelper.setSupportWaterMark(GroupHelper.isSupportWaterMark(detailInfo), groupId);

        if (!isAskMode) {
            GroupHelper.showRenewView(detailInfo, rlRenew, tvRenewDetail, tvRenew);
        }
        isMaster = GroupHelper.isSelfMaster(detailInfo);
        groupId = detailInfo.getId();
        userAlias = detailInfo.getCurrent_user().getName();
        llWatermark.setVisibility(MarUser.WATER_POS > 0 ? View.VISIBLE : View.GONE);
        SUtils.setPicResource(ivWatermark, R.drawable.selected_watermark_icon);
        mEtGroupDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Logs.i("charSe:" + charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

                int length = editable.toString().trim().length();
                if (SUtils.isEmptyArrays(images)) {
                    btnSend.changeStyle(length > 0);
                } else {
                    btnSend.changeStyle(true);
                }
                if (!needNotify) {
                    CUtils.onClick(context, isAskMode ? "isAskMode" : "discuss_text");
                    needNotify = true;

                }
                checkStatus(tgSubject);

            }
        });

        init();
        //提问跟主题的最大长度不一致，动态设置
        mEtGroupDesc.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textNum)});
        initBroadcast(BroadConst.NOTIFY_DELETE_SUBJECT, BroadConst.NOTIFY_MEDIA_CHANGED);

        showKeyboard(mEtGroupDesc);
    }


    /**
     * 有些地方跳转过来没有足够的星球信息
     *
     * @param groupId
     */
    private void reqeustGroupInfo(String groupId) {
        if (groupId == null) {
            return;
        }
        final SummerParameter params = PostData.getPostParameters(context);
        params.put("scope", "all");
        params.putLog("获取星球详情");
        getDataTwo(REQUEST_GROUP_DETAIL, GroupDetInfo.class, params, "groups/" + groupId);
    }

    @Override
    protected void onMsgReceiver(String action, Intent intent) {
        super.onMsgReceiver(action, intent);
        switch (action) {
            case BroadConst.NOTIFY_DELETE_SUBJECT:
                String id = intent.getStringExtra("id");
                deleteTag(id);
                break;
            case BroadConst.NOTIFY_MEDIA_CHANGED:
                boolean isVideo = intent.getBooleanExtra(JumpTo.TYPE_BOOLEAN, false);
                ArrayList<ImageItem> arrayList = (ArrayList<ImageItem>) intent.getSerializableExtra(JumpTo.TYPE_OBJECT);
                if (!SUtils.isEmptyArrays(arrayList)) {
                    for (ImageItem item : arrayList) {
                        if (item.isFake()) {
                            arrayList.remove(item);
                            break;
                        }
                    }
                }
                if (isVideo) {//相册选择照片返回
                    if (arrayList != null && arrayList.size() > 0) {
                        images = null;
                        sendType = 1;
                        videoItem = arrayList.get(0);
                        selectVideo = arrayList.get(0).getVideoPath();
                        showVideoView(videoItem.getVideoPath(), selectVideo);
                        btnSend.changeStyle(true);
                    } else {
                        images = null;
                        videoItem = null;
                        selectVideo = null;
                        rlVideoMark.setVisibility(View.GONE);
                        SUtils.setPicResource(ivPics, R.drawable.picture_add_icon);
                    }

                } else {

                    images = arrayList;
                    Logs.i("images" + images);
                    if (!SUtils.isEmptyArrays(images)) {
                        if (images.size() > 1) {
                            rlMorePic.setVisibility(View.VISIBLE);
                        } else {
                            rlMorePic.setVisibility(View.GONE);
                        }
                        btnSend.changeStyle(true);
                        SUtils.setPic(ivPics, images.get(0).getImagePath());
                    } else {
                        Logs.i("images" + images);
                        resetImgCover();
                    }
                }
                break;
        }
    }

    private void resetImgCover() {
        //预览时，删除图片，删到最后一张时，有可能正在执行上一张的显示
        myHandlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                SUtils.setPicResource(ivPics, R.drawable.picture_add_icon);
            }
        }, 200);
    }

    private void initTagView() {
        //键盘弹出，隐藏话题模块
        mEtGroupDesc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        tgSubject.setAddMoreRow();
        tgSubject.addOnClickListener(new OnReturnObjectClickListener() {
            @Override
            public void onClick(Object object) {
                SubjectInfo info = (SubjectInfo) object;
        /*        if(passedSubject == null){
                    passedSubject = info;
                }*/
                if (info.isMoreView()) {
                    addNewSubject();
                } else {
                    appendNewTag(object);
                }

            }
        });
    }

    /**
     * 添加一个话题到编辑
     *
     * @param object
     */
    private void appendNewTag(Object object) {
        SubjectInfo info = (SubjectInfo) object;
        if (info != null) {
            checkContain(info);
            Spannable spannableString = new SpannableString(mEtGroupDesc.getText());
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(spannableString);
            spannableStringBuilder.append(" ");
            spannableStringBuilder = TextWebUtils.generateOneSpan(context, spannableStringBuilder, TextWebUtils.returnTag(info.getTitle(),info.getId()), mEtGroupDesc.getTextSize(), getResColor(R.color.grey_4a));
            spannableStringBuilder.append(" ");
            mEtGroupDesc.setText(spannableStringBuilder);
            SUtils.setSelection(mEtGroupDesc);
            showSubjectView(false);
        }
    }

    @Override
    public void loadData() {
        SummerParameter parameter = PostData.getPostParameters(context);
        parameter.putLog("话题列表");
        parameter.setShowVirtualData();
        requestData(0, SubjectTopInfo.class, parameter, "groups/hash_tags",true);
    }

    private void showModifyAudioMode(String audioPath) {
        rlAudioPlay.setVisibility(View.GONE);
        this.audioPath = audioPath;
        hideAudio();
        showAudioView(true);
    }

    private void showModifyVideoMode(String videoPath) {
        rlAudioPlay.setVisibility(View.GONE);
        showVideoView(videoPath, videoPath);
    }

    protected void init() {

    }

    private void showSendButton() {
        btnSend = (CommonSureView5) findViewById(R.id.btn_right);
        btnSend.setText("发表");
        btnSend.setNeedBackgroud(false);
        btnSend.setTextSize(18);
        btnSend.getPaint().setFakeBoldText(true);
        btnSend.setEnableColor(R.color.blue_56);
        btnSend.setVisibility(View.VISIBLE);
        btnSend.changeStyle(false);
        btnSend.setOnClickListener(this);
    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {
        switch (requestCode) {
            case 0:
                SubjectTopInfo topInfo = (SubjectTopInfo) obj;
                List<SubjectInfo> historyData = topInfo.getRecent();
                if (historyData == null) {
                    historyData = new ArrayList<>();
                }
                List<SubjectInfo> subjectInfos = topInfo.getDefault_tag();
                if (subjectInfos == null) {
                    subjectInfos = new ArrayList<>();
                }
                checkContainAndDelete(passedSubject, historyData);
                checkContainAndDelete(passedSubject, subjectInfos);
                if (passedSubject != null) {
                    historyData.add(0, passedSubject);
                }

                if (subjectInfos.size() > 7) {
                    subjectInfos = subjectInfos.subList(0, 7);
                }
                historyData.addAll(subjectInfos);
                SubjectInfo subjectInfo = new SubjectInfo();
                subjectInfo.setMoreView(true);
                subjectInfo.setAdd(true);
                subjectInfo.setTextColor(getResColor(R.color.blue_56));
                historyData.add(subjectInfo);
                subjects = historyData;
                tgSubject.setTags(historyData);
                showSubjectView(false);
                checkStatus(tgSubject);
                break;
            case REQUEST_GROUP_DETAIL:
                detailInfo = (GroupDetInfo) obj;
                initTopicView();
                break;
        }
    }

    private void checkContain(SubjectInfo subjectInfo) {
/*        if (subjects == null) {
            return;
        }
        String id = subjectInfo.getId();
        boolean isContain = false;
        for (SubjectInfo info : subjects) {
            if (info != null && info.getTitle().equals(subjectInfo.getTitle())) {
                isContain = true;
                break;
            }
        }
        if (!isContain) {
            subjects.add(0,subjectInfo);
        }*/
    }

    /**
     * 检查是不是已存在目标话题，如果存在就删除
     *
     * @param subjectInfo
     * @param infos
     */
    private void checkContainAndDelete(SubjectInfo subjectInfo, List<SubjectInfo> infos) {
        if (subjectInfo == null) {
            return;
        }
        if (infos == null) {
            return;
        }
        String id = subjectInfo.getId();
        for (SubjectInfo info : infos) {
            if (info.getId().equals(id)) {
                infos.remove(info);
                break;
            }
        }
    }

    protected void reqeustPhotos(int count) {
        SelectPhotoHelper selectPhotoHelper = new SelectPhotoHelper(context, new OnReturnObjectClickListener() {
            @Override
            public void onClick(Object object) {
                images = (ArrayList<ImageItem>) object;
                if (!SUtils.isEmptyArrays(images)) {
                    btnSend.changeStyle(true);
                    SUtils.setPic(ivPics, images.get(0).getImagePath());
                    if (images.size() > 1) {
                        rlMorePic.setVisibility(View.VISIBLE);
                    } else {
                        rlMorePic.setVisibility(View.GONE);
                    }
                }
            }
        });
        selectPhotoHelper.enterToAlbumForMore(count);
    }


    @OnClick({R.id.iv_pics, R.id.tv_relase_video, R.id.tv_relase_audio, R.id.ll_audio_play, R.id.iv_watermark, R.id.tv_add, R.id.ll_address})
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_pics:
                if (hasMedieas()) {
                    return;
                }
                String dialogContent = null;
                CUtils.onClick(context, isAskMode ? "discuss_quest_pic" : "discuss_pic");
                if (!TextUtils.isEmpty(audioPath) || (audioHelper != null && audioHelper.isOnAudioHelper())) {
                    dialogContent = "上传图片，音频将被清除，你确定要继续上传图片吗？";
                } else if (!TextUtils.isEmpty(selectVideo)) {
                    dialogContent = "上传图片，视频将被清除，你确定要继续上传图片吗？";
                }
                if (dialogContent != null) {
                    BaseUtils.showEasyDialog(context, dialogContent, new BaseTipsDialog.DialogAfterClickListener() {
                        @Override
                        public void onSure() {
                            audioPath = null;
                            selectVideo = null;
                            rlVideoMark.setVisibility(View.GONE);
                            onImgButton();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    return;
                }

                onImgButton();

                break;
            case R.id.btn_right:
                if (isSending) {
                    return;
                }
                isSending = true;
                SUtils.hideSoftInpuFromWindow(mEtGroupDesc);
                hideAudio();
                sendContent = mEtGroupDesc.getText().toString();
                onSendButtonClick(sendContent);
                break;
            case R.id.tv_relase_video:
                onVideoBtnCLick();

                break;
            case R.id.tv_relase_audio:
                CUtils.onClick(context, "discuss_audio");
                String audioContent = null;
                showSubjectView(false);
                if (!TextUtils.isEmpty(selectVideo)) {
                    audioContent = "上传音频，视频将被清除，你确定要继续录制音频吗？";
                } else if (!SUtils.isEmptyArrays(images)) {
                    audioContent = "上传音频，图片将被清除，你确定要继续录制音频吗？";
                }
                if (audioContent != null) {
                    BaseUtils.showEasyDialog(context, audioContent, new BaseTipsDialog.DialogAfterClickListener() {
                        @Override
                        public void onSure() {
                            rlVideoMark.setVisibility(View.GONE);
                            images = null;
                            videoItem = null;
                            selectVideo = null;
                            resetImgCover();
                            onAudioButton();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    return;
                }
                onAudioButton();
                break;
            case R.id.ll_audio_play:
                if (playAudioHelper == null) {

                    playAudioHelper = new PlayAudioHelper(new OnAudioPlayListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivPlay.setVisibility(View.VISIBLE);
                                    tvAudioTime.setVisibility(View.GONE);
                                }
                            });
                        }
                    });
                }
                playAudioHelper.setSeekBarAndiTimeView(null, tvAudioTime);
                if (playAudioHelper.checkEnable()) {
                    playAudioHelper.playMediaFile(audioPath);
                    ivPlay.setVisibility(View.GONE);
                    tvAudioTime.setVisibility(View.VISIBLE);
                } else {
                    ivPlay.setVisibility(View.VISIBLE);
                    tvAudioTime.setVisibility(View.GONE);
                    playAudioHelper.stopPlayingAudio();
                }

                break;
            case R.id.iv_watermark:
                CUtils.onClick(context, "discuss_watermark");
                if(isAnony){
                    return;
                }
                changeWatermarkState();
                break;
            case R.id.ll_address:
                if (!PermissionUtils.checkLocationPermission(context)) {
                    return;
                }
                //JumpTo.getInstance().commonResultJump(context, LocationActivity.class, 1001);
                break;
        }
    }

    public void changeWatermarkState() {
        if (needWatermark) {
            needWatermark = false;
            SUtils.setPicResource(ivWatermark, R.drawable.unselected_watermark_icon);
        } else {
            SUtils.setPicResource(ivWatermark, R.drawable.selected_watermark_icon);
            needWatermark = true;
        }
        WaterMarkHelper.setTempWatermark(needWatermark);
    }

    /**
     * 是否已存在多媒体
     *
     * @return
     */
    private boolean hasMedieas() {
        boolean hasMedieas = false;
        if (!SUtils.isEmptyArrays(images)) {
            ShowMediaActivity.show(context, images, false);
            return true;
        }
        return hasMedieas;
    }

    private void addNewSubject() {
        DialogAddSubject dialogAddSubject = new DialogAddSubject(context, groupId);
        dialogAddSubject.setOnReturnObjectClickListener(new OnReturnObjectClickListener() {
            @Override
            public void onClick(Object object) {
                if (object == null) {
                    return;
                }
                appendNewTag(object);
                SubjectInfo subjectInfo = (SubjectInfo) object;
                subjectInfo.setSelect(true);
                List<SubjectInfo> infos = tgSubject.getTags();
                if (infos == null) {
                    infos = new ArrayList<>();
                }
                infos.add(0, subjectInfo);
                if (infos.size() > 10) {
                    infos.remove(infos.size() - 2);
                }
                tgSubject.setTags(infos);
            }
        });
        dialogAddSubject.show();
    }

    private void deleteTag(String id) {
        List<SubjectInfo> infos = tgSubject.getTags();
        if (infos != null) {
            for (SubjectInfo info : infos) {
                if (info.getId() != null && id != null && id.equals(info.getId())) {
                    infos.remove(info);
                    break;
                }
            }
        }
        tgSubject.setTags(infos);
    }

    /**
     * 显示主题模块
     */
    private void showSubjectView(boolean showSubject) {
        if (isMaster) {
            tvAdd.setVisibility(View.VISIBLE);
        } else {
            tvAdd.setVisibility(View.GONE);
        }
        if (showSubject) {
            if (rlContainer.getVisibility() == View.VISIBLE) {
                rlContainer.setVisibility(View.GONE);
            }
            SUtils.hideSoftInpuFromWindow(mEtGroupDesc);
            myHandlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSubjectStatus();
                }
            }, 300);

        } else {
            if (rlContainer.getVisibility() == View.GONE && audioHelper != null && audioHelper.isOnAudioHelper()) {
                rlContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 更新话题状态
     */
    private void updateSubjectStatus() {
        if (SUtils.isEmptyArrays(tgSubject.getTags()) && SUtils.isEmptyArrays(subjects) && !isMaster) {
            tvAvailable.setVisibility(View.GONE);
            tgSubject.setVisibility(View.GONE);
        }
        checkStatus(tgSubject);
    }

    private void checkStatus(TagGroup tagGroup) {
        List<SubjectInfo> subjectInfos = tagGroup.getTags();
        if (SUtils.isEmptyArrays(subjectInfos)) {
            return;
        }
        SpannableString spannableString = new SpannableString(mEtGroupDesc.getText());
        String content = spannableString.toString();
        Logs.i("Content:" + content);
        for (int i = 0; i < subjectInfos.size(); i++) {
            SubjectInfo info = subjectInfos.get(i);
            String id = info.getId();
            if (content.contains("\" hid=\"" + id + "\" />")) {
                info.setSelect(true);
            } else {
                info.setSelect(false);
            }
        }
        tagGroup.setTags(subjectInfos);
    }

    /**
     * 获取当前输入的所有话题ID
     *
     * @return
     */
    private String getAllSubjectID() {
        String subjectId = "";
        if (SUtils.isEmptyArrays(subjects)) {
            return "";
        }
        passedSubject = null;
        SpannableString spannableString = new SpannableString(mEtGroupDesc.getText());
        String content = spannableString.toString();
        for (int i = 0; i < subjects.size(); i++) {
            SubjectInfo info = subjects.get(i);

            String id = info.getId();
            if (content.contains("\" hid=\"" + id + "\" />")) {
                if(passedSubject == null){
                    passedSubject = info;
                }
                if (TextUtils.isEmpty(subjectId)) {
                    subjectId = id;
                } else {
                    subjectId = subjectId + "," + id;
                }
            }
        }
        Logs.i("subjectId:" + subjectId);
        return subjectId;
    }

    protected void onVideoBtnCLick() {
        String videoDialogContent = null;

        if (!TextUtils.isEmpty(audioPath) || (audioHelper != null && audioHelper.isOnAudioHelper())) {
            videoDialogContent = "上传视频，音频将被清除，你确定要继续选择视频吗？";
        } else if (!SUtils.isEmptyArrays(images)) {
            videoDialogContent = "上传视频，图片将被清除，你确定要继续选择视频吗？";
        }
        if (videoDialogContent != null) {
            BaseUtils.showEasyDialog(context, videoDialogContent, new BaseTipsDialog.DialogAfterClickListener() {
                @Override
                public void onSure() {
                    audioPath = null;
                    images = null;
                    hideAudio();
                    showAudioView(false);
                    selectVideo();
                    stopRecoding();
                }

                @Override
                public void onCancel() {

                }
            });
            return;
        }
        hideAudio();
        showAudioView(false);
        selectVideo();
    }

    private void onImgButton() {
        showSubjectView(false);
        reqeustPhotos(9);
        hideAudio();
        showAudioView(false);
        stopRecoding();
    }

    /**
     * 点击音频
     */
    private void onAudioButton() {
        if (rlContainer.getVisibility() == View.VISIBLE) {

            if (audioHelper.isRecording) {
                return;
            }
            rlContainer.setVisibility(View.GONE);
        } else {
            SUtils.hideSoftInpuFromWindow(mEtGroupDesc);
            myHandlder.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rlContainer.setVisibility(View.VISIBLE);
                    audioHelper = new AudioHelper();
                    audioHelper.init(rlContainer);
                    audioHelper.setOnAudioRecordListener(new OnAudioRecordListener() {
                        @Override
                        public void onRecorded(String path, int legnth) {
                            sendType = 2;
                            audioPath = path;
                            audioLength = legnth;
                            hideAudio();
                            showAudioView(true);
                            btnSend.changeStyle(true);
                        }

                        @Override
                        public void onCancel() {
                            sendType = 0;
                            audioPath = null;
                            rlContainer.setVisibility(View.GONE);
                        }
                    });
                }
            }, 300);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecoding();
        stopPlaying();
    }

    private void stopRecoding() {
        if (audioHelper != null) {
            audioHelper.resureRecording();
            audioHelper.setOnAudioHelper(false);
        }
    }

    private void stopPlaying() {
        if (playAudioHelper != null) {
            playAudioHelper.stopPlayingAudio();
        }
    }

    /**
     * 显示音频还是图片
     *
     * @param showAudioView
     */
    private void showAudioView(boolean showAudioView) {
        if (showAudioView) {
            GroupHelper.setAudioTime(audioLength, tvAudioTime);
            rlAudioPlay.setVisibility(View.VISIBLE);
        } else {
            rlAudioPlay.setVisibility(View.GONE);
        }
    }

    private void hideAudio() {
        rlContainer.setVisibility(View.GONE);
    }

    public void selectVideo() {
        CUtils.onClick(context, "discuss_video");
        if (!PermissionUtils.checkReadPermission(context)) {
            return ;
        }
        SelectPhotoHelper selectPhotoHelper = new SelectPhotoHelper(context, new OnReturnObjectClickListener() {
            @Override
            public void onClick(Object object) {
                ArrayList<ImageItem> arrayList = (ArrayList<ImageItem>)object;
                if (arrayList != null && arrayList.size() > 0) {
                    images = null;
                    sendType = 1;
                    videoItem = arrayList.get(0);
                    selectVideo = arrayList.get(0).getVideoPath();
                    showVideoView(videoItem.getVideoPath(), selectVideo);
                    btnSend.changeStyle(true);
                }
            }
        });
        selectPhotoHelper.selectVideo();
    }

    public void onSendButtonClick(String content) {
        CUtils.onClick(context, "discuss_submit");
        if (sendType == 1) {
            sendVideo();
        } else {
            sendTopic();
        }
        if (fromWhere == 1) {
           //跳转到哪里去
        }
    }

    /**
     * 创建视频类型话题
     */
    private void sendVideo() {
        String subjectID = getAllSubjectID();
        Intent intent = new Intent(context, SendTopicVideoService.class);
        intent.putExtra("key_groupid", groupId);
        intent.putExtra("videoPath", videoItem.getVideoPath());
        intent.putExtra("key_content", sendContent);//
        intent.putExtra("subject_ids", subjectID);
        intent.putExtra("key_images", images);
        intent.putExtra("isReviseMode", isReviseMode);
        intent.putExtra("topic_id", topicId);
        long tempKey = System.currentTimeMillis();
        intent.putExtra("key_time", tempKey);
        //boolean isWaiting = createFakeTopic(tempKey, subjectID);
        startService(intent);

        finishActivity();
    }

    private void finishActivity() {
        if (passedSubject != null) {
            //将当前正在发送中的主动先显示到页面上
            //SubjectGroupActivity.show(context, passedSubject);
            finish();
            return;
        }
        finish();
    }

    protected void createIntentSendToService(String type, String questionee_id, String issueType, String anonymous, String money) {
        Logs.i("anony:" + anonymous);
        String subjectID = getAllSubjectID();
        sendContent = TextWebUtils.returnNewUrl(sendContent);
        Intent intent = new Intent(context, SendTopicService.class);
        intent.putExtra("key_type", type);
        intent.putExtra("subject_ids", subjectID);
        intent.putExtra("key_groupid", groupId);
        intent.putExtra("needWatermark", needWatermark);
        intent.putExtra("questionee_id", questionee_id);
        intent.putExtra("issue_type", issueType);//0 否 1 是
        intent.putExtra("issue_money", money);//单位为分
        intent.putExtra("address", localAddress);
        intent.putExtra("key_anonymous", anonymous);
        intent.putExtra("key_content", sendContent);//
        intent.putExtra("key_username", userAlias);
        intent.putExtra("key_images", images);
        if (audioPath != null && !audioPath.startsWith("http")) {
            intent.putExtra("key_audio_path", audioPath);
        }
        intent.putExtra("isReviseMode", isReviseMode);
        intent.putExtra("topic_id", topicId);
        long tempKey = System.currentTimeMillis();
        intent.putExtra("key_time", tempKey);
        /**
        boolean isWaiting = createFakeTopic(tempKey, subjectID);
        if (!isWaiting) {
            startService(intent);
        }
         **/
        finishActivity();
    }

    /**
    private boolean createFakeTopic(long time, String subjectIds) {
        CommonService mService = new CommonService(context);
        List<TopicDetailInfo> items = (List<TopicDetailInfo>) mService.getListData(DbType.SEND_TOPIC, groupId);
        if (items == null) {
            items = new ArrayList<>();
        }
        TopicDetailInfo topicDetailInfo = new TopicDetailInfo();
        topicDetailInfo.setLocalSubjectId(subjectIds);
        TopicContentInfo topicContentInfo = new TopicContentInfo();
        topicContentInfo.setText(sendContent);
        topicDetailInfo.setTopic(topicContentInfo);
        topicDetailInfo.setSendStatus(SendStatus.SENDING);
        topicDetailInfo.setGroupId(groupId);
        GroupUserInfo topicUserInfo = new GroupUserInfo();
        topicDetailInfo.setUser(topicUserInfo);
        createVoiceInfo(topicDetailInfo);
        createVideoInfo(topicDetailInfo);
        createImgInfo(topicDetailInfo);
        if (detailInfo != null) {
            GroupUserInfo specificInfo = detailInfo.getCurrent_user();
            topicUserInfo.setIdentity(specificInfo.getIdentity());
            topicUserInfo.setName(specificInfo.getName());
        }
        topicUserInfo.setAvatar(MarUser.USER_AVATAR);
        topicDetailInfo.setTopic_add_time(time);
        items.add(0, topicDetailInfo);


        boolean isWaiting = false;
        if (BaseUtils.isServiceWork(context, SendTopicService.class.getName())) {
            topicDetailInfo.setSendStatus(SendStatus.WAITING);
            isWaiting = true;
        }
        mService.insert(DbType.SEND_TOPIC, groupId, items);
        sendBroadcast(BroadConst.NOTIFY_FAKE_TOPIC);
        return isWaiting;
    }

     */

    /**
     * 创建虚假的图片
     *
     * @param topicDetailInfo

    private void createImgInfo(TopicDetailInfo topicDetailInfo) {
        if (SUtils.isEmptyArrays(images)) {
            return;
        }
        TopicContentInfo topicInfo = topicDetailInfo.getTopic();
        topicInfo.setType(TopicType.IMG_TYPE);
        if (topicInfo == null) {
            topicInfo = new TopicContentInfo();
        }
        List<TopicImgInfo> topicImgInfos = new ArrayList<>();
        topicInfo.setImages(topicImgInfos);
        for (ImageItem item : images) {
            String imgPath = item.getImagePath();
            TopicImgInfo imgInfo = new TopicImgInfo();
            imgInfo.setMini(imgPath);
            imgInfo.setPreview(imgPath);
            topicImgInfos.add(imgInfo);
        }

    }
     */



    /**
     * 创建临时的音频
     *
     * @param topicDetailInfo

    private void createVoiceInfo(TopicDetailInfo topicDetailInfo) {
        if (TextUtils.isEmpty(audioPath)) {
            return;
        }
        TopicContentInfo topicInfo = topicDetailInfo.getTopic();
        if (topicInfo == null) {
            topicInfo = new TopicContentInfo();
            topicDetailInfo.setTopic(topicInfo);
        }
        TopicVoiceInfo topicVoiceInfo = new TopicVoiceInfo();
        topicVoiceInfo.setUrl(audioPath);
        topicVoiceInfo.setLength(audioLength);
        topicInfo.setType(TopicType.AUDIO_TYPE);
        topicInfo.setVoices(topicVoiceInfo);
    }
     */
    /**
     * 创建临时的视频
     *
     * @param topicDetailInfo

    private void createVideoInfo(TopicDetailInfo topicDetailInfo) {
        if (TextUtils.isEmpty(selectVideo)) {
            return;
        }
        TopicContentInfo topicInfo = topicDetailInfo.getTopic();
        if (topicInfo == null) {
            topicInfo = new TopicContentInfo();
        }
        TopicVideoInfo topicVideoInfo = new TopicVideoInfo();
        topicVideoInfo.setUrl(selectVideo);
        topicVideoInfo.setLocalImg(videoItem.getVideoPath());
        topicInfo.setType(TopicType.VIDEO_TYPE);
        topicInfo.setVideos(topicVideoInfo);
    }
     */
    /**
    public static void retrySend(Context context, TopicDetailInfo info) {
        TopicContentInfo topicContentInfo = info.getTopic();
        Logs.i("topicCOntent:" + topicContentInfo);
        if (topicContentInfo == null) {
            return;
        }
        String sendContent = topicContentInfo.getText();
        String videoPath = TopicHelper.get().getVideoPath(info);
        Intent intent = new Intent(context, TextUtils.isEmpty(videoPath) ? SendTopicService.class : SendTopicVideoService.class);
        intent.putExtra("key_groupid", info.getGroupId());
        intent.putExtra("subject_ids", info.getLocalSubjectId());
        intent.putExtra("questionee_id", "");
        intent.putExtra("issue_type", "");//0 否 1 是
        intent.putExtra("issue_money", "");//单位为分
        intent.putExtra("key_anonymous", "");
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("key_content", sendContent);//
        String audioPath = TopicHelper.get().getAudioPath(info);
        intent.putExtra("key_audio_path", audioPath);
        intent.putExtra("key_username", NameHelper.get().getNameByOwner(info.getUser()));
        List<TopicImgInfo> imgInfos = topicContentInfo.getImages();
        if (imgInfos != null) {
            ArrayList<ImageItem> items = new ArrayList<>();
            for (TopicImgInfo img : imgInfos) {
                ImageItem imageItem = new ImageItem();
                imageItem.setImagePath(img.getPreview());
                items.add(imageItem);
            }
            intent.putExtra("key_images", items);
        }
        intent.putExtra("key_time", info.getTopic_add_time());

        context.startService(intent);
    }

     */

    private void sendTopic() {
        createIntentSendToService("talk", "", "", "", "");

    }

    private void showVideoView(final String coverPath, final String videoPath) {
        rlVideoMark.setVisibility(View.VISIBLE);
        SUtils.setPic(ivPics, coverPath);
        rlVideoMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images = new ArrayList<>();
                ImageItem imageItem = new ImageItem();
                imageItem.setImagePath(coverPath);
                imageItem.setVideoPath(videoPath);
                images.add(imageItem);
               // ShowMediaVideoActivity.show(context, images, true, false,isReviseMode);
                JumpTo.getInstance().commonJump(context, ShowVideoActivity.class, videoPath);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logs.i("resultCode:" + requestCode + ",,," + resultCode + ",,data" + data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PHOTO) {//相册选择照片返回


            } else if (requestCode == 1001) {
                String name = data.getStringExtra("location_name");
                localAddress = name;
                if (TextUtils.isEmpty(name)) {
                    tvAddress.setText("添加位置");
                } else {
                    tvAddress.setText(name);
                }

            }
        }
    }

    @Override
    protected void onBackClick() {
        if (!TextUtils.isEmpty(mEtGroupDesc.getText().toString()) || !SUtils.isEmptyArrays(images) || !TextUtils.isEmpty(selectVideo) || !TextUtils.isEmpty(audioPath)) {
            BaseUtils.showEasyDialog(context, "是否退出此次编辑？", new BaseTipsDialog.DialogAfterClickListener() {
                @Override
                public void onSure() {
                    onFinish();
                }

                @Override
                public void onCancel() {
                    CUtils.onClick(context, BACK_CANCEL);
                }
            });
        } else {
            onFinish();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CUtils.onClick(context, BACK_TAG + "_back");
            onBackClick();
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void onFinish() {
        CUtils.onClick(context, BACK_TAG);
        this.finish();
    }
}
