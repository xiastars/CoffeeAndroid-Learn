package com.summer.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.summer.demo.R;

import java.util.List;
/**
 * 为首页的标题设置adapter
 * @编者 夏起亮
 *
 */
public class CommonAdapter extends BaseAdapter {
	
	private List<String> title;
	private ViewHolder holder;
	private Context context;
	private LayoutInflater inflater ;
	
    public CommonAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}
    
    public void notifyDataChanged(List<String> title){
    	this.title = title; 
    	notifyDataSetChanged();
    }   
    
	@Override
	public int getCount() {
		return title != null ? title.size() : 0;
	}
	@Override
	public Object getItem(int position) {
		return title.size();
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView =  inflater.inflate(R.layout.item_main,null);
			holder = getViewHolder(convertView);				
			convertView.setTag(holder);
		}else{
			holder =(ViewHolder)convertView.getTag();
		}
	    holder.content.setText(position+1+":"+title.get(position));
	    /*
	     * 根据奇偶数设置不同的颜色
	     */
	    if(position%2 == 0){
	    	holder.content.setBackgroundColor(context.getResources().getColor(R.color.pink));
	    }else{
	    	holder.content.setBackgroundColor(context.getResources().getColor(R.color.green));
	    }
	    
		return convertView;
	}
	private ViewHolder getViewHolder(View convertView){
		holder = new ViewHolder();
		holder.content = (TextView) convertView.findViewById(R.id.name);
	
		return holder;
		
	}

    class ViewHolder{
    	private TextView content;

    }

}