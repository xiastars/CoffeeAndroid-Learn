package com.summer.demo.ui.module.comment;

import android.app.Activity;
import android.content.Context;
import android.graphics.PointF;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.module.base.BaseActivity;
import com.summer.demo.module.base.BaseFragment;
import com.summer.demo.module.base.BaseFragmentActivity;
import com.summer.demo.module.base.CommonHelper;
import com.summer.demo.module.emoji.EmojiHelper;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SUtils;
import com.summer.helper.view.NRecycleView;
import com.summer.helper.view.SRecycleView;
import com.summer.helper.view.ScrollableLayout;

import java.util.List;

/**
 * 处理评论与回复
 * Created by xiaqiliang on 2017/4/15.
 */

public class CommentHelper extends CommonHelper implements View.OnClickListener {
    EditText edtContent;
    Button btnSend;
    private RelativeLayout rlCommentCount, rlEditParent;
    private TextView tvCommentCount;
    private ImageView ivEmoji;
    Context context;
    List<StarCommentInfo> comments;

    private OnCommentCountViewClickedListener mOnCommentCountViewClickedListener;

    boolean sendEnabled = false;
    long replyId;
    String replyName;
    int preTop;

    //是否是最热类型
    boolean isHotType;

    long fromId;

    SRecycleView sRecycleView;
    EmojiHelper emojiHelper;
    NRecycleView nvContainser;
    Activity activity;
    BaseFragment baseFragment;

    boolean isReresh;

    final int REQUEST_SEND = 0;
    final int REQUEST_NEW_COMMENT = 1;

    public CommentHelper(View activity) {
        super(activity.getContext());
        this.activity = (Activity) activity.getContext();
        emojiHelper = new EmojiHelper();
        emojiHelper.initEmojiView( activity);
        ivEmoji = (ImageView) activity.findViewById(R.id.iv_emoji);
        this.edtContent = (EditText) activity.findViewById(R.id.edt_comment);
        this.btnSend = (Button) activity.findViewById(R.id.btn_send);
        rlCommentCount = (RelativeLayout) activity.findViewById(R.id.rl_comment_count);
        rlEditParent = (RelativeLayout) activity.findViewById(R.id.rl_edit_parent);
        rlCommentCount.setOnClickListener(this);
        tvCommentCount = (TextView) activity.findViewById(R.id.tv_comment_count);
        if (btnSend == null) {
            return;
        }
        context = btnSend.getContext();
        init();
        View parent = (View) activity.findViewById(R.id.ll_parent).getParent();
        if (parent != null) {
            parent.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    if (preTop == 0) {
                        preTop = top;
                    } else if (preTop < top && !emojiHelper.isEmojiView() && TextUtils.isEmpty(edtContent.getText().toString())) {
                        replyId = 0;
                        replyName = "";
                        edtContent.setHint("请输入评论:");
                        emojiHelper.setEmojiLayoutInvisible(false);
                    } else {
                        preTop = top;
                    }
                }
            });
        }
    }

    private void init() {
        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changeSendStyle(s.toString());
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }


    /**
     * 发布评论
     */
    public void sendComment() {
        if (!sendEnabled) {
            SUtils.makeToast(context, context.getString(R.string.hint_empty_comment));
            return;
        }
    }


    /**
     * 改变发送的颜色
     *
     * @param s
     */
    private void changeSendStyle(String s) {
        if (TextUtils.isEmpty(s)) {
            sendEnabled = false;
            btnSend.setBackgroundResource(R.drawable.so_greyd8_5);
        } else {
            sendEnabled = true;
            btnSend.setBackgroundResource(R.drawable.so_redd4_5);
        }
    }

    @Override
    protected void handleMsg(int position, Object object) {

    }

    @Override
    protected void dealDatas(int requestCode, Object obj) {
        switch (requestCode) {
            case REQUEST_SEND:
                fromId = 0;

                edtContent.setText("");
                SUtils.hideSoftInpuFromWindow(edtContent);
                edtContent.setHint("请输入评论:");
                //handleCommentScroll();
                break;
            case REQUEST_NEW_COMMENT:
                List<StarCommentInfo> items = (List<StarCommentInfo>) obj;
                if (isHotType) {
                    comments = items;
                }

                int size = items.size();

                if (size == 10) {
                    fromId = items.get(size - 1).getId();

                }
                Logs.i("是否是重新刷新模块:" + isReresh);
                if (activity != null) {
                    if (activity instanceof BaseFragmentActivity) {

                        BaseFragmentActivity ba = (BaseFragmentActivity) activity;
                        ba.baseHelper.setLoadCount(10);
                        if (isReresh) {
                            ba.pageIndex = 0;
                        }
                        if (nvContainser != null) {
                            ba.handleViewData(obj, nvContainser);
                        } else if (sRecycleView != null) {
                            ba.handleViewData(obj);
                        }
                    } else if (activity instanceof BaseActivity) {
                        BaseActivity b = (BaseActivity) activity;
                        b.baseHelper.setLoadCount(10);
                        if (isReresh) {
                            b.pageIndex = 0;
                        }
                        if (nvContainser != null) {
                            b.handleViewData(obj, nvContainser);
                        } else if (sRecycleView != null) {
                            b.handleViewData(obj);
                        }
                    }
                } else if (baseFragment != null) {
                    baseFragment.baseHelper.setLoadCount(10);
                    if (isReresh) {
                        baseFragment.pageIndex = 0;
                    }
                    if (nvContainser != null) {
                        baseFragment.handleViewData(obj, nvContainser);
                    } else if (sRecycleView != null) {
                        baseFragment.handleViewData(obj);
                    }

                }
                notifyCommentCountView();
                break;
        }
    }

    private void notifyCommentCountView() {
        if (tvCommentCount != null) {
            int totalCount = 100;
            Logs.i("totalCount:" + totalCount);
            if (totalCount == 0) {
                tvCommentCount.setVisibility(View.GONE);
            } else {
                tvCommentCount.setText(totalCount > 999 ? "999+" : totalCount + "");
                tvCommentCount.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setLoadMore(boolean loadMore) {
        if (activity != null) {
            if (activity instanceof BaseFragmentActivity) {

                BaseFragmentActivity ba = (BaseFragmentActivity) activity;
                ba.setLoadMore(loadMore);
            } else if (activity instanceof BaseActivity) {
                BaseActivity b = (BaseActivity) activity;
                b.setLoadMore(loadMore);
                Logs.i("xxxxxxxxxxxxxxxxxxxxxx");
            }
        } else if (baseFragment != null) {
            baseFragment.setLoadMore(loadMore);
        }
    }

    @Override
    protected void dealErrors(int requstCode, String requestType, String errString, boolean requestCode) {

    }

    public boolean isHotType() {
        return isHotType;
    }

    public void setHotType(boolean hotType) {
        isHotType = hotType;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_comment_count:
                handleCommentScroll();//让评论列表nvContainser滚动到顶部
                break;
        }
    }

    public void handleCommentScroll() {
        ViewGroup parent = getScrollParentView(nvContainser);
        boolean handle = false;
        if (parent != null) {
            int y = (int) getViewLocationInParentView(nvContainser, parent).y;
            if (parent.getScrollY() < y) {
                handle = true;
                if (parent instanceof ScrollView) {
                    ((ScrollView) parent).smoothScrollTo(parent.getScrollX(), y);
                } else {
                    parent.scrollTo(parent.getScrollX(), y);//有时间再补个动画
                }
            }
        }
        if (mOnCommentCountViewClickedListener != null)
            mOnCommentCountViewClickedListener.OnCommentCountViewClicked(handle);
    }

    private ViewGroup getScrollParentView(View view) {
        ViewParent parent = view.getParent();
        if (!(parent instanceof ViewGroup))
            return null;
        ViewGroup viewGroup = (ViewGroup) parent;
        if (viewGroup == null)
            return null;
        //如果是自定义的滚动父view，不是继承于安卓自带的可滚动View，加上（|| instanceof）判断
        if (viewGroup.isScrollContainer() || viewGroup instanceof ScrollableLayout) {
            return viewGroup;
        } else {
            return getScrollParentView(viewGroup);
        }
    }

    private PointF getViewLocationInParentView(View childView, View parentView) {
        float childX = childView.getX();
        float childY = childView.getY();
        PointF point = new PointF(childX, childY);
        View parent = (View) childView.getParent();
        while (parent != null && parent != parentView) {
            point.x += parent.getX();
            point.y += parent.getY();
            parent = (View) parent.getParent();
        }
        if (parent == null) {
            //根本不是父子view
            point.x = childX;
            point.y = childY;
        }
        return point;
    }

    public void setOnCommentCountViewClickedListener(OnCommentCountViewClickedListener listener) {
        mOnCommentCountViewClickedListener = listener;
    }

    public int getPagerIndex() {
        if (activity != null) {
            if (activity instanceof BaseFragmentActivity) {

                BaseFragmentActivity ba = (BaseFragmentActivity) activity;
                return ba.pageIndex;
            } else if (activity instanceof BaseActivity) {
                BaseActivity b = (BaseActivity) activity;
                return b.pageIndex;
            }
        } else if (baseFragment != null) {
            return baseFragment.pageIndex;
        }
        return 0;
    }

    public interface OnCommentCountViewClickedListener {
        /**
         * @param handleScrollSuccess 是否成功处理nvContainser的滚动事件
         */
        void OnCommentCountViewClicked(boolean handleScrollSuccess);
    }
}
