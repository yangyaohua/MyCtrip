package com.example.adapter;

import java.util.List;

import com.example.activity.AddPassengerInfoActivity;
import com.example.bean.Ticket;
import com.example.myctrip.R;
import com.example.utils.Constants;
import com.example.utils.HttpUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SearchTicketsAdapter extends BaseAdapter {

	private Context mContext;
	private List<Ticket> mList;
	public SearchTicketsAdapter(Context context, List<Ticket> ticketList) {
		super();
		this.mContext = context;
		this.mList = ticketList;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Ticket getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Ticket ticket = getItem(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.ticket_show_item, null);
			holder.serialNumber  = (TextView) convertView.findViewById(R.id.tv_serial_number);
			holder.originalAddress = (TextView) convertView.findViewById(R.id.tv_original_address);
			holder.destinationAddress = (TextView) convertView.findViewById(R.id.tv_destination_address);
			holder.consume = (TextView) convertView.findViewById(R.id.tv_consume);
			holder.startTime = (TextView) convertView.findViewById(R.id.tv_start_time);
			holder.endTime = (TextView) convertView.findViewById(R.id.tv_end_time);
			holder.price = (TextView) convertView.findViewById(R.id.tv_price);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.serialNumber.setText(ticket.getSerialNumber());
		holder.originalAddress.setText(ticket.getOriginAddress());
		holder.destinationAddress.setText(ticket.getDesAddress());
		holder.consume.setText(ticket.getConsume()+"");
		holder.startTime.setText(ticket.getStartTime().toLocaleString());
		holder.endTime.setText(ticket.getEndTime().toLocaleString());
		holder.price.setText(ticket.getPrice()+"");
		return convertView;
	}

	static class ViewHolder{
		TextView serialNumber, originalAddress, destinationAddress, startTime, endTime, consume, price;
	}
}
