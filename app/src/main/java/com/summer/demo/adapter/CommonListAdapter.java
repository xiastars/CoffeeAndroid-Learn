package com.summer.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.summer.demo.R;
import com.summer.demo.constant.TestData;
import com.summer.helper.adapter.SRecycleAdapter;
import com.summer.helper.listener.OnSimpleClickListener;
import com.summer.helper.utils.SUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonListAdapter extends SRecycleAdapter {

	OnSimpleClickListener listener;

	public CommonListAdapter(Context context) {
		super(context);
	}

	public CommonListAdapter(Context context,OnSimpleClickListener listener) {
		super(context);
		this.listener = listener;

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new CommonListAdapter.ViewHolder(createHolderView(R.layout.item_view_list, parent));
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		CommonListAdapter.ViewHolder vh = (CommonListAdapter.ViewHolder) holder;

		vh.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onClick(position);
				}
				SUtils.makeToast(context,"点击了第"+position+"行");
			}
		});
		//静态页面，数据写死
		int data = TestData.imgs[position];
		vh.ivImg.setBackgroundResource(data);
		vh.tvContent.setText(TestData.contents[position]);
	}

	//重写数量
	@Override
	public int getItemCount() {
		return TestData.imgs.length;
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		@BindView(R.id.iv_img)
		ImageView ivImg;
		@BindView(R.id.tv_content)
		TextView tvContent;

		ViewHolder(View view) {
			super(view);
			ButterKnife.bind(this, view);
		}
	}

}
