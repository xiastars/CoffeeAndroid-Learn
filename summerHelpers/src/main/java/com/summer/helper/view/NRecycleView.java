package com.summer.helper.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.summer.helper.utils.SAnimUtils;
import com.summer.helper.utils.SUtils;

import java.lang.ref.WeakReference;

/**
 * 自定义RecycleView,继承原生，不支持下拉刷新 
 * @author xiaqiliang
 *
 */
public class NRecycleView extends RecyclerView {
	LayoutManager mManager;

	/** 方向 */
	int orientation = DividerItemDecoration.VERTICAL_LIST;
	/** GridView列数 */
	int numGrid;
	/** 按键操作时的主动滚动 */
	boolean mScroll;
	/** 按键操作时判断是横向 */
	boolean isHor;

	int mSelected;
	/** 切换选中时是否需要更新Adapter */
	boolean shouldNotify;

	MyHandler mHandler;
	View fakeView;

	public NRecycleView(Context context) {
		super(context);
		mHandler = new MyHandler(this);
	}

	public NRecycleView(Context context, AttributeSet attri) {
		super(context, attri);
		mHandler = new MyHandler(this);
	}

	public void setList() {
		SUtils.initScreenDisplayMetrics((Activity) getContext());
		this.setLayoutManager(new LinearLayoutManager(getContext()));
	}

	public void setHorizontalList() {
		SUtils.initScreenDisplayMetrics((Activity) getContext());
		isHor = true;
		numGrid = 10000;
		LinearLayoutManager manager = new LinearLayoutManager(getContext());
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		this.setLayoutManager(manager);
	}

	public int getGridNum() {
		return numGrid;
	}

	public void setGridView(int num) {
		numGrid = num;
		this.setLayoutManager(new GridLayoutManager(getContext(), num));
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
		if (cur >= all) {
			return;
		}
		if (mScroll) {
			int height = this.getChildAt(0).getHeight();
			if (reverse) {
				height *= -1;
			}
			this.smoothScrollBy(0, height);
		} else {
			LayoutManager manager = (LayoutManager) getLayoutManager();
			View child = manager.findViewByPosition(cur);
			int height = manager.getChildAt(0).getHeight();
			if (child != null) {
				if (isHor) {
					int width = manager.getChildAt(0).getWidth();
					if (child.getRight() + width > SUtils.screenWidth) {
						smoothScrollBy(width, 0);
					} else if (child.getLeft() - width < 0) {
						smoothScrollBy(-width, 0);
					}
				} else {
					if (child.getBottom() > SUtils.screenHeight) {
						smoothScrollBy(0, height);
					} else if (child.getTop() - height < 0) {
						smoothScrollBy(0, -height);
					}
				}

			} else {
				smoothScrollBy(0, height);
			}
		}
	}

	public int getmSelected() {
		return mSelected;
	}

	public void setmSelected(int mSelected) {
		this.mSelected = mSelected;
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageDelayed(0, 30);
		if (shouldNotify) {
			this.getAdapter().notifyDataSetChanged();
		}
		/* 当焦点退出时，如果有滚动，则回退 */
		if (mSelected == -1) {
			scrollToPosition(0);
		}
	}

	public static class MyHandler extends Handler {
		private final WeakReference<NRecycleView> mActivity;

		public MyHandler(NRecycleView activity) {
			mActivity = new WeakReference<NRecycleView>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			NRecycleView activity = mActivity.get();
			if (null != activity) {
				switch (msg.what) {
				case 0:
					LayoutManager manager = activity.getLayoutManager();
					int count = manager.getChildCount();
					for (int i = 0; i < count; i++) {
						View v = manager.getChildAt(i);
						if (v != null) {
							v.setScaleX(1.0f);
							v.setScaleY(1.0f);
						}
					}
					View child = manager.findViewByPosition(activity.mSelected);
					if (child != null) {
						SAnimUtils.scale(child, false);
						activity.addFakeView(child);
					}
					break;
				}
			}
		}
	}

	private void addFakeView(View view) {
		if (this.getParent() == null) {
			return;
		}
/*
		ViewGroup viewGroup = (ViewGroup) this.getParent();
		if (fakeView != null) {
			viewGroup.removeView(fakeView);
		}
		fakeView = new View(getContext());
		fakeView.draw(view.getC);
		fakeView = view;
		viewGroup.addView(fakeView);*/
		/*F
		mFake = new JMenu(context);
		mFake.setDefaultIcon(defaultIcon);
		mFake.setLayoutPosition(mParams.leftMargin, mParams.topMargin);
		mFake.measureLayout(childParams.width, childParams.height);*/
	}

	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}

	public int getViewScrollX() {
		LayoutManager manager = this.getLayoutManager();
		if (manager instanceof LinearLayoutManager) {
			LinearLayoutManager layoutManager = (LinearLayoutManager) this.getLayoutManager();
			int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
			View firstVisibleItem = this.getChildAt(0);
			int itemWidth = firstVisibleItem.getWidth();
			int firstItemRight = layoutManager.getDecoratedRight(firstVisibleItem);
			return (this.getAdapter().getItemCount() * itemWidth - (firstItemPosition + 2) * itemWidth
					+ firstItemRight);
		}
		return 0;
	}

	public boolean isHor() {
		return isHor;
	}

	public void setHor(boolean isHor) {
		this.isHor = isHor;
	}

	public boolean isShouldNotify() {
		return shouldNotify;
	}

	public void setShouldNotify() {
		this.shouldNotify = true;
	}

}
