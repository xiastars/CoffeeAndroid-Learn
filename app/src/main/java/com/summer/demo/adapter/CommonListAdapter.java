package com.summer.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.summer.demo.R;
import com.summer.helper.utils.SUtils;

/**
 * 所有的Adapter都必须继承自BaseAdapter类
 */
public class CommonListAdapter extends BaseAdapter {
	private Context context;
	boolean isShowDelete = false;

	int[] datas = {R.drawable.xiehou01,R.drawable.xiehou02,R.drawable.xiehou03,R.drawable.xiehou04};

	/**
	 * 通常情况下，创建一个会传Context的构造器
	 * @param context
	 */
	public CommonListAdapter(Context context) {
		this.context = context;
	}

	/**
	 * 必须重写的方法，如果return 为0,则ListView为空，以下为规范写法
	 * @return
	 */
	@Override
	public int getCount() {
		return datas != null ? datas.length : 0;
	}


	/**
	 * 获取当前位置的对象
	 * @param position
	 * @return
	 */
	@Override
	public Object getItem(int position) {
		return datas[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//ViewHolder用来重用布局，不这样，数据多时，滚动界面会卡
		ViewHolder viewHodler;
		if (convertView == null) {
			//以下方法把R.layout.item_icon这个布局加载为View
			convertView = LayoutInflater.from(context).inflate(R.layout.item_icon,null);
			viewHodler = new ViewHolder(convertView);
			convertView.setTag(viewHodler);

		} else {
			viewHodler = (ViewHolder) convertView.getTag();
		}
		viewHodler.icon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = position +1;
				SUtils.makeToast(context,"点击了第"+index+"个");
				// }
			}
		});
		int data = (int) getItem(position);
			viewHodler.icon.setBackgroundResource(data);
		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		public ViewHolder(View view) {
			icon = (ImageView) view.findViewById(R.id.item_album);
		}
	}

	public interface PurposeSelectedListener {
		void afterClick(int position);
	}
}
