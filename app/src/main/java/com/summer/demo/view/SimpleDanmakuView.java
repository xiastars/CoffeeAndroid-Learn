package com.summer.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.bean.DanmakuInfo;
import com.summer.demo.module.emoji.EmojiUtil;
import com.summer.helper.listener.OnAnimEndListener;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SAnimUtils;
import com.summer.helper.utils.SUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by xiastars on 2017/7/21.
 */

public class SimpleDanmakuView extends RelativeLayout {
    Map<Integer, List<DanmakuInfo>> comments;
    OnSimpleClickListener listener;
    Context context;
    MyHandler myHandler;

    int pageIndex;
    String fromId;
    String eventId;
    boolean isEnd;//数量没有更多了
    Runnable danmakuRunnable;

    boolean enableDannmaku = true;
    String CONTEXT_TAG;

    public SimpleDanmakuView(Context context) {
        super(context);
        init(context);
    }

    public SimpleDanmakuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleDanmakuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        myHandler = new MyHandler(this);
        CONTEXT_TAG = context.getClass().getSimpleName();
        Logs.i("CONTEXT_TAG:" + CONTEXT_TAG);
    }

    public void setEventId(String eventID) {
        this.eventId = eventID;
    }

    public void addDatas(List<DanmakuInfo> infos, int pos) {
        if (!enableDannmaku) {
            return;
        }
        if (comments == null) {
            comments = new HashMap<>();
        }
        if (infos != null && infos.size() > 0) {
            comments.put(pos, infos);
        }
        if (infos.size() < 20) {
            isEnd = true;
        }
        startDanmaku(pos);
    }

    private void startDanmaku(int pos) {
        if (!enableDannmaku || comments == null) {
            return;
        }
        pageIndex++;
        if (pos >= comments.size()) {
            pos = 0;
        }

        List<DanmakuInfo> items = comments.get(pos);
        if (items == null) {
            return;
        }
        int count = items.size();
        for (int i = 0; i < count; i++) {
            DanmakuInfo info = items.get(i);
            getDanmakuView(info, i);
        }
        if (isEnd) {
            myHandler.sendEmptyMessageDelayed(1, 15 * 1000);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private View getDanmakuView(DanmakuInfo info, final int position) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_dannmaku, null);
        view.setVisibility(View.GONE);
        final ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        SUtils.setPicWithHolder(ivIcon, info.getImg(), R.drawable.default_icon_triangle);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        EmojiUtil.setEmojiText(tvTitle, info.getName());
        this.addView(view);
        danmakuRunnable = new Runnable() {
            @Override
            public void run() {
                SAnimUtils.showPropertyAnim(false, view, View.VISIBLE, "translationX", SUtils.screenWidth, -SUtils.getDip(view.getContext(), 330), -SUtils.getDip(view.getContext(), 330), 15000, new OnAnimEndListener() {
                    @Override
                    public void onEnd() {
                        SimpleDanmakuView.this.removeView(view);
                        if (isEnd && SimpleDanmakuView.this.getChildCount() < 1) {
                            startDanmaku(pageIndex);
                        }
                    }
                });
                if (position == 15 && !isEnd) {
                    requestCommentData(eventId);
                }
            }
        };
        myHandler.postDelayed(danmakuRunnable, position * 1000);
        int topPadding = new Random().nextInt(6) * SUtils.getDip(getContext(), 20);
        ((LayoutParams) view.getLayoutParams()).topMargin = topPadding;
        return view;
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        enableDannmaku = false;
        this.removeAllViews();
        myHandler.removeMessages(0);
        myHandler.removeMessages(1);
        myHandler.removeCallbacks(danmakuRunnable);
    }

    /**
     * 开始播放
     */
    public void startPlay() {
        enableDannmaku = true;
        Logs.i("继续播放" + isEnd);
        if (isEnd) {
            startDanmaku(0);
        } else {
            startDanmaku(pageIndex);
            requestCommentData(eventId);
        }
    }

    public void addSimpleListener(OnSimpleClickListener listener) {
        this.listener = listener;
    }

    public void requestOrResume(String eventId) {
        enableDannmaku = true;
        if (isEnd) {
            startPlay();
        } else {
            requestCommentData(eventId);
        }
    }

    /**
     * 网络请求数据
     *
     * @param eventID
     */
    public void requestCommentData(final String eventID) {

    }

    /**
     * 添加一条新的弹幕
     *
     * @param info
     */
    public void addNewComment(DanmakuInfo info) {
        boolean isEmpty = false;
        if (comments == null) {
            comments = new HashMap<>();
            isEmpty = true;
        }
        List<DanmakuInfo> items = comments.get(pageIndex);
        if (items == null) {
            items = new ArrayList<>();
            comments.put(pageIndex, items);
            if (pageIndex == 0) {
                isEmpty = true;
            }
        }
        items.add(info);
        if (isEmpty) {
            startDanmaku(0);
        }
    }

    public static class MyHandler extends Handler {
        private final WeakReference<SimpleDanmakuView> mActivity;

        public MyHandler(SimpleDanmakuView activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SimpleDanmakuView activity = mActivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case 0:

                    case 1:
                        activity.startDanmaku(activity.pageIndex);
                        break;
                }
            }
        }
    }

}


