package com.summer.helper.recycle;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.summer.helper.utils.SAnimUtils;

public abstract class SRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	public int selectedPosition = 0;
	boolean triggerAnim = false;
	/* 有效个数 */
	int itemCount;
	public Context context;
	int prePosition;
	int lastPosition;
	int curPosition ;

	public SRecycleAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	/**
	 * 获取有效个数
	 * 
	 * @return
	 */
	public int getECount() {
		if (itemCount != 0) {
			return itemCount;
		}
		return getItemCount();
	}

	/**
	 * 设置有效个数
	 * 
	 * @param itemCount
	 */
	public void setECount(int itemCount) {
		this.itemCount = itemCount;
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		return null;
	}

	public void setSelected(int position) {
		//notifyItemChanged(selectedPosition);
		//mHandler.removeMessages(0);
		selectedPosition = position;
		/*triggerAnim = true;
		notifyItemChanged(selectedPosition);
		notifyItemChanged(lastPosition);
		lastPosition = selectedPosition;
		mHandler.sendEmptyMessageDelayed(0, 1000);*/
	}

	public void onViewSelected(View view, int position) {
		if (triggerAnim) {
			triggerAnim = false;
			SAnimUtils.scale(view, false);
			prePosition = position;
		} else {
			SAnimUtils.removeScale(view);
		}
	}

	/**
	 * Adapter的第二行动画pivot取View的宽高无效，手动传上去
	 * 
	 * @param view
	 * @param width
	 * @param height
	 */
	public void onViewSelected(View view, int width, int height,int position) {
		if (triggerAnim) {
			triggerAnim = false;
			prePosition = position;
			SAnimUtils.scale(view, false, width, height);
		} else {
			view.clearAnimation();
		}
	}

	public int getSelected() {
		return selectedPosition;
	}

	public boolean isTriggerAnim() {
		return triggerAnim;
	}

	public void setTriggerAnim(boolean triggerAnim) {
		this.triggerAnim = triggerAnim;
	}

	public abstract void onClick(int curPos);

}
