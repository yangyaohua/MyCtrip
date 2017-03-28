package com.example.adapter;

import com.example.myctrip.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	
	private static final int[] icons = {
		R.drawable.a0,R.drawable.a1,R.drawable.a1,R.drawable.a2,R.drawable.a3
	};
	
	private static final String[] names = {
		"车票预订","历史订单","写点啥呢","退出登录"
	};
	
	public MainAdapter(Context context) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.main_item, null);
		TextView tv_name = (TextView)view.findViewById(R.id.tv_main_item_name);
		ImageView iv_icon = (ImageView)view.findViewById(R.id.iv_main_item_icon);
		tv_name.setText(names[position]);
		iv_icon.setImageResource(icons[position]);
		return view;
	}

}
