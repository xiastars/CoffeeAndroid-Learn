package com.summer.helper.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.summer.helper.recycle.MaterialRefreshLayout;

/**
 * 自定义RecycleView
 * 
 * @author xiaqiliang
 *
 */
public class HRecycleView extends MaterialRefreshLayout {

	public HRecycleView(Context context) {
		super(context);
		init();
		setHorizontalList();
	}

	public HRecycleView(Context context, AttributeSet attri) {
		super(context, attri);
		init();
		setHorizontalList();
	}

	private void setHorizontalList() {
		setIsHor();
		setWaveShow(false);
		setIsOverLay(true);
		setShowArrow(false);
		LinearLayoutManager manager = new LinearLayoutManager(getContext());
		manager.setOrientation(LinearLayoutManager.HORIZONTAL);
		this.getRefreshView().setLayoutManager(manager);
	}

	/**
	 * 设置右拉加载
	 */
	public void setLoadMore(){
		this.setLoadMore(true);
	}

	public void setAdapter(RecyclerView.Adapter adapter){
		this.getRefreshView().setAdapter(adapter);
	}

	//是否支持刷新
	public void enableRefreshable(boolean enable) {
		this.refreshable = enable;
	}

	public void onRefreshComplete() {
	/*	this.setLoading(false);
		this.setRefreshing(false);*/
		// this.setLoadCompleted(true);
	}

	private void init() {
		this.getRefreshView().setHasFixedSize(true);
		// getScrollView().addItemDecoration(new
		// DividerItemDecoration(getContext(), orientation));
	}

}
