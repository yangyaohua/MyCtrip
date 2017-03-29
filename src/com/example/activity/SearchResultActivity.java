package com.example.activity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.example.adapter.SearchTicketsAdapter;
import com.example.bean.Ticket;
import com.example.myctrip.R;
import com.example.utils.ActivityCollector;
import com.example.utils.Constants;
import com.example.utils.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchResultActivity extends Activity {

	private ListView lv_tickets;
	private List<Ticket> ticketList;
	private SearchTicketsAdapter mAdapter;
	private static int MESSAGE_SEARCH_TICKETS = 0;
	private static int MESSAGE_SAVE_TID = 1;

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:				
				if (ticketList != null && !ticketList.isEmpty()) {
					mAdapter = new SearchTicketsAdapter(SearchResultActivity.this, ticketList);
					lv_tickets.setAdapter(mAdapter);
				}
				break;
			case 1:
				boolean result = (Boolean) msg.obj;
				if (result) {
					startActivity(new Intent(SearchResultActivity.this, AddPassengerInfoActivity.class));
				}else {
					Toast.makeText(SearchResultActivity.this, "出错了。", Toast.LENGTH_LONG).show();
				}
			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);
		ActivityCollector.addActivity(this);
		initView();
		initData();
		initEvent();
	}
	
	private void initData() {
		Intent intent = getIntent();
		String daddress = intent.getStringExtra(Constants.TEMP_DESTINATION_ADDRESS);
		String oaddress = intent.getStringExtra(Constants.TEMP_ORIGINAL_ADDRESS);
		String startTime = intent.getStringExtra(Constants.TEMP_START_TIME);
		Date startDate = Date.valueOf(startTime);
		searchTicket(oaddress, daddress, startDate);
	}

	protected void searchTicket(final String originalAddress, final String destinationAddress, final Date startDate) {
		new Thread(){
			public void run() {
				ticketList= HttpUtil.searchTicket(SearchResultActivity.this, originalAddress, destinationAddress, startDate);
				Message msg = Message.obtain();
				mHandler.sendEmptyMessage(MESSAGE_SEARCH_TICKETS);
			};
		}.start();
	}

	private void initEvent() {
		lv_tickets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Ticket ticket = ticketList.get(position);
				saveTid(ticket.getTid());
			}
		});
	}

	protected void saveTid(final int tid) {
		new Thread(){
			public void run() {
				boolean result = HttpUtil.onTicketItemClicked(SearchResultActivity.this,tid);
				Message msg = Message.obtain();
				msg.what = MESSAGE_SAVE_TID;
				msg.obj = result;
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	private void initView() {
		lv_tickets = (ListView) findViewById(R.id.lv_tickets);
		ticketList = new ArrayList<Ticket>();
	}

}
