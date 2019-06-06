package com.summer.helper.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.malata.summer.helper.R;
import com.summer.helper.adapter.SRecycleAdapter;
import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SAnimUtils;
import com.summer.helper.utils.SUtils;

/**
 * 自定义RecycleView
 *
 * @author xiaqiliang
 */
public class SRecycleView extends MaterialRefreshLayout implements ScrollableHelper.ScrollableContainer {

    /**
     * GridView列数
     */
    int numGrid;
    /**
     * 按键操作时的主动滚动
     */
    boolean mScroll;
    int mSelected = -1;
    ISRecycleListener listener;
    boolean isShowNotify = false;

    int measureHeight;

    public boolean isShowNotify() {
        return isShowNotify;
    }

    public void setShowNotify(boolean isShowNotify) {
        this.isShowNotify = isShowNotify;
    }

    public void setRecycleListener(ISRecycleListener listener) {
        this.listener = listener;
    }

    public SRecycleView(Context context) {
        super(context);

    }

    public SRecycleView(Context context, AttributeSet attri) {
        super(context, attri);
    }

    /**
     * 设置为ListView
     */
    public void setList() {
        this.getRefreshViewForTypeRecycleView().setLayoutManager(new LinearLayoutManager(getContext()));
        ((DefaultItemAnimator) this.getRefreshViewForTypeRecycleView().getItemAnimator()).setSupportsChangeAnimations(false);

    }

    public void setAdapter(@SuppressWarnings("rawtypes") RecyclerView.Adapter adapter) {
        this.getRefreshViewForTypeRecycleView().setAdapter(adapter);
    }

    public RecyclerView.Adapter getAdapter(){
        return this.getRefreshViewForTypeRecycleView().getAdapter();
    }

    /**
     * 设置为GridView
     *
     * @param num 列数
     */
    public void setGridView(int num) {
        numGrid = num;
        this.getRefreshViewForTypeRecycleView().setLayoutManager(new GridLayoutManager(getContext(), num));
        ((DefaultItemAnimator) this.getRefreshViewForTypeRecycleView().getItemAnimator()).setSupportsChangeAnimations(false);
    }

    public int getGridNum() {
        return numGrid;
    }

    /**
     * 如果为true，则每过一行滚动一下，反之到最底下才滚动
     *
     * @param scroll
     */
    public void setScroll(boolean scroll) {
        this.mScroll = scroll;
    }

    public void notifyScoll(int cur, int all, boolean reverse) {
        RecyclerView view = this.getRefreshViewForTypeRecycleView();
        GridLayoutManager manager = (GridLayoutManager) view.getLayoutManager();
        if (cur >= all) {
            autoRefreshLoadMore();
            return;
        }
        SRecycleAdapter adapter = (SRecycleAdapter) view.getAdapter();
        View childF = manager.getChildAt(0);
        int height = childF.getHeight() + childF.getPaddingBottom();
        View child = manager.findViewByPosition(cur);
        if (child != null) {
            if (child.getBottom() > SUtils.screenHeight) {
                if (cur + numGrid > all) {
                    view.smoothScrollBy(0, height);
                } else {
                    view.smoothScrollBy(0, height);
                }
            } else if (child.getTop() - height < 0) {
                view.smoothScrollBy(0, -height);
            }
        } else {
            view.smoothScrollBy(0, height);
        }

        if (isShowNotify)
            adapter.notifyDataSetChanged();
    }

    /**
     * 设置上拉加载
     */
    public void setLoadMore() {
        this.setPullUpRefreshable(true);
        this.getRefreshViewForTypeRecycleView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!canChildScrollDown() && !childCanScrollInParentWithUp(false)) {
                    setAutoLoadmore();
                }
            }
        });
    }

    public int getmSelected() {
        return mSelected;
    }

    public void setmSelected(int mSelected) {
        this.mSelected = mSelected;
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 5);
        /* 当焦点退出时，如果有滚动，则回退 */
        if (mSelected == -1) {
            this.getRefreshViewForTypeRecycleView().scrollToPosition(0);
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    RecyclerView view = SRecycleView.this.getRefreshViewForTypeRecycleView();
                    GridLayoutManager manager = (GridLayoutManager) view.getLayoutManager();
                    int count = manager.getChildCount();
                    Logs.i(count + ",,Count:");
                    for (int i = 0; i < count; i++) {
                        View v = manager.getChildAt(i);
                        if (v != null) {
                            v.setScaleX(1.0f);
                            v.setScaleY(1.0f);
                        }
                    }
                    View child = manager.findViewByPosition(mSelected);
                    if (child != null) {
                        SAnimUtils.scale(child, 1.5f, false);
                    }
                    if (listener != null) {
                        listener.selectPostion(mSelected);
                    }
                    break;
            }
        }
    };

    @Override
    public void finishPullDownRefresh() {
        super.finishPullDownRefresh();
        //setmSelected(mSelected);
    }

    @Override
    public void finishPullUpRefresh() {
        super.finishPullUpRefresh();
        //setmSelected(mSelected);
    }

    /**
     * 设置圈圈在View上面
     */
    public void setOverLay() {
        setIsOverLay(true);
    }

    public void setGridDivider(int color) {
        getRefreshViewForTypeRecycleView().addItemDecoration(new GridItemDecoration(getContext()), color);
    }

    public void setDivider(Drawable drawable) {
        this.getRefreshViewForTypeRecycleView().addItemDecoration(new ListItemDecoration(getContext(), LinearLayoutManager.VERTICAL, drawable));
    }

    public void setCommonDividerGrey(int left, int right) {
        int color = getContext().getResources().getColor(R.color.grey_e1);
        Rect rect = new Rect();
        rect.left = left;
        rect.right = right;
        setDividerColor(color, rect);
    }

    /**
     * 两边相等的分割线
     *
     * @param left
     */
    public void setCommonDividerGreyLR(int left) {
        setCommonDividerGrey(left, left);
    }

    public void setDividerColor(int color, Rect rect) {
        this.getRefreshViewForTypeRecycleView().addItemDecoration(new ListItemDecoration(getContext(), LinearLayoutManager.VERTICAL, color, rect));
    }

    @Override
    public View getScrollableView() {
        return this;
    }

    public interface ISRecycleListener {
        void selectPostion(int postion);
    }

    public void setViewHeight(int height) {
        this.getLayoutParams().height = height;
        this.measureHeight = height;
        //measure(getMeasuredWidth(),height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (this.getParent() != null && this.getParent() instanceof ViewPager && measureHeight != 0) {
            super.onMeasure(widthMeasureSpec, measureHeight);
            return;
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
