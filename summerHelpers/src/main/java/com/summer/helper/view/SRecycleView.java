package com.summer.helper.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.summer.helper.recycle.MaterialRefreshLayout;
import com.summer.helper.recycle.SRecycleAdapter;
import com.summer.helper.utils.Logs;
import com.summer.helper.utils.SAnimUtils;
import com.summer.helper.utils.SUtils;

/**
 * 自定义RecycleView
 *
 * @author xiaqiliang
 */
public class SRecycleView extends MaterialRefreshLayout {

	/** GridView列数 */
	int numGrid;
	/** 按键操作时的主动滚动 */
	boolean mScroll;
	int mSelected = -1;
	ISRecycleListener listener;
	boolean isShowNotify = false;

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
		this.getRefreshView().setLayoutManager(new LinearLayoutManager(getContext()));
		((DefaultItemAnimator) this.getRefreshView().getItemAnimator()).setSupportsChangeAnimations(false);
	}

	public void setAdapter(@SuppressWarnings("rawtypes") RecyclerView.Adapter adapter) {
		this.getRefreshView().setAdapter(adapter);
	}

	/**
	 * 设置为GridView
	 *
	 * @param num
	 *            列数
	 */
	public void setGridView(int num) {
		numGrid = num;
		this.getRefreshView().setLayoutManager(new GridLayoutManager(getContext(), num));
		((DefaultItemAnimator) this.getRefreshView().getItemAnimator()).setSupportsChangeAnimations(false);
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
		RecyclerView view = this.getRefreshView();
		GridLayoutManager manager = (GridLayoutManager) ((RecyclerView) view).getLayoutManager();
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
		this.setLoadMore(true);
	}

	/**
	 * 是否支持刷新
	 * 
	 * @param enable
	 */
	public void enableRefreshable(boolean enable) {
		this.refreshable = enable;
	}

	public void setWaveShow(int color) {
		this.setWaveShow(true);
		this.setWaveColor(color);
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
			this.getRefreshView().scrollToPosition(0);
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				RecyclerView view = SRecycleView.this.getRefreshView();
				GridLayoutManager manager = (GridLayoutManager) ((RecyclerView) view).getLayoutManager();
				int count = manager.getChildCount();
				Logs.i("xia", count + ",,Count:");
				for (int i = 0; i < count; i++) {
					View v = manager.getChildAt(i);
					if (v != null) {
						v.setScaleX(1.0f);
						v.setScaleY(1.0f);
					}
				}
				View child = manager.findViewByPosition(mSelected);
				if (child != null) {
					SAnimUtils.scale(child, false);
				}
				if (listener != null) {
					listener.selectPostion(mSelected);
				}
				break;
			}
		};
	};

	@Override
	public void finishRefresh() {
		super.finishRefresh();
		setmSelected(mSelected);
	};

	@Override
	public void finishRefreshLoadMore() {
		super.finishRefreshLoadMore();
		setmSelected(mSelected);
	};

	/**
	 * 设置圈圈在View上面
	 */
	public void setOverLay() {
		setIsOverLay(true);
	}

	public void setDivider(Drawable drawable) {
		this.getRefreshView()
				.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, drawable));
	}

	public interface ISRecycleListener {
		public void selectPostion(int postion);
	}
}
