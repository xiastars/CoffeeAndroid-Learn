package com.summer.demo.ui.view.commonfragment.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 中间大，两头小的，模仿Gallery的RecyvlerView
 */
public class GalleryRecyclerView extends RecyclerView {

    private static final int OFFEST_SPAN_COUNT = 1;

    private Context mContext;
    private OnScrollPositionChangeListener mScrollPositionChangeListener;
    private OnScrollStopListener mScrollStopListener;
    private int mCurrentOffset;
    private int mOnePageWidth; // 滑动一页的距离
    private int mCurrentItemPos;
    private int mLastHeighlightPosition = -1;
    private float mHighlightScale = 1.28f;
    private int mReferenceX;

    public GalleryRecyclerView(Context context) {
        this(context, null);
    }

    public GalleryRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init(){
        setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        new LinearSnapHelper().attachToRecyclerView(this);
    }
    
    private void handleScroll() {
        if (getChildCount() < 3)
            return;
        if (mOnePageWidth <= 0) {
            View view = getChildAt(1);
            int p = getChildAdapterPosition(view);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            mOnePageWidth = view.getWidth() + params.leftMargin + params.rightMargin;
            mReferenceX = (getWidth() - mOnePageWidth) / 2;
        }
        if (mOnePageWidth <= 0)
            return;
        boolean pageChanged = false;
        // 滑动超过一页说明已翻页
        if (Math.abs(mCurrentOffset - getHighlightPosition() * mOnePageWidth) >= mOnePageWidth) {
            pageChanged = true;
        }
        mCurrentItemPos = Math.max(mCurrentItemPos, OFFEST_SPAN_COUNT);
        if (pageChanged) {
            mCurrentItemPos = mCurrentOffset / mOnePageWidth + OFFEST_SPAN_COUNT;
            if (mScrollPositionChangeListener != null)
                mScrollPositionChangeListener.onScrollChange(getHighlightPosition());
        }

        /*
        int childCount = getChildCount();
        for (int i=0;i<childCount;i++){
            View view = getChildAt(i);
            float scale = Math.max(1, mHighlightScale - Math.abs(view.getX() - mReferenceX) / mOnePageWidth * (mHighlightScale - 1));
            view.setScaleX(scale);
            view.setScaleY(scale);
        }

         */
    }

    /**
     * 获取当前突出变大的选项位置
     */
    public int getHighlightPosition(){
        return Math.max(0, mCurrentItemPos- OFFEST_SPAN_COUNT);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        handleScroll();
    }

    @Override
    public void scrollToPosition(int position) {
        scrollBy((position - getHighlightPosition()) * mOnePageWidth, 0);
    }

    @Override
    public void smoothScrollToPosition(int position) {
//        super.smoothScrollToPosition(position + OFFEST_SPAN_COUNT);
        smoothScrollBy((position - getHighlightPosition()) * mOnePageWidth, 0);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        mCurrentOffset += dx;
        handleScroll();
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == SCROLL_STATE_IDLE){
            if (mScrollStopListener != null) {
                int cur = getHighlightPosition();
                if (cur != mLastHeighlightPosition){
                    mLastHeighlightPosition = cur;
                    mScrollStopListener.onScrollStop(mLastHeighlightPosition);
                }
            }
        }
    }

    public void setmHighlightScale(float scale){
        mHighlightScale = scale;
        requestLayout();
    }

    public void setOnScrollStopListener(OnScrollStopListener listener){
        mScrollStopListener = listener;
    }

    public void setOnScrollPositionChangeListener(OnScrollPositionChangeListener listener){
        mScrollPositionChangeListener = listener;
    }

    public interface OnScrollStopListener {
        void onScrollStop(int position);
    }

    public interface OnScrollPositionChangeListener {
        void onScrollChange(int position);
    }
}
