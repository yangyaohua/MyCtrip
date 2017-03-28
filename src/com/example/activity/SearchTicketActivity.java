package com.example.activity;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.SearchTicketsAdapter;
import com.example.bean.Ticket;
import com.example.data.Pair;
import com.example.data.ViewContainer;
import com.example.myctrip.R;
import com.example.utils.ActivityCollector;
import com.example.utils.Constants;
import com.example.utils.HttpUtil;

public class SearchTicketActivity extends BaseActivity{

	private EditText et_destination_address, et_original_address, et_start_day;
	private Button btn_search;
	private DateFormat fmtDate;
	private Calendar dateAndTime;
	private DatePickerDialog.OnDateSetListener dateListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_ticket);
		setTitle("火车票查询");
		
		initDate();
		initView();
		initEvent();
	}





	private void initDate() {
		fmtDate = new SimpleDateFormat("yyyy-MM-dd");
		dateAndTime = Calendar.getInstance(Locale.CHINA);
		dateListener = new DatePickerDialog.OnDateSetListener()
	    {
	        @Override
	        public void onDateSet(DatePicker view, int year, int monthOfYear,
	                int dayOfMonth) {
	            dateAndTime.set(Calendar.YEAR, year);
	            dateAndTime.set(Calendar.MONTH, monthOfYear);
	            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);    
	            upDateDate();   
	        }        
	    };	
	    
	}

	protected void upDateDate() {
		et_start_day.setText(fmtDate.format(dateAndTime.getTime()));
	}

	private void initEvent() {
		et_start_day.setFocusable(false);
		et_start_day.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				   DatePickerDialog  dateDlg = new DatePickerDialog(SearchTicketActivity.this,dateListener,dateAndTime.get(Calendar.YEAR),dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
                   dateDlg.show();			
			}
		});
		
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				String originalAddress = et_original_address.getText().toString().trim();
				String destinationAddress = et_destination_address.getText().toString().trim();
				String startTime = et_start_day.getText().toString().trim();
				/*ticketList.clear();
				if (mAdapter != null) {
					mAdapter.notifyDataSetChanged();
				}*/
				if (originalAddress.equals("") || destinationAddress.equals("") || startTime.equals("")) {
					Toast.makeText(SearchTicketActivity.this, "请填写完整信息", Toast.LENGTH_LONG).show();
				}else {
					Intent intent = new Intent(SearchTicketActivity.this, SearchResultActivity.class);
					intent.putExtra(Constants.TEMP_ORIGINAL_ADDRESS, originalAddress);
					intent.putExtra(Constants.TEMP_DESTINATION_ADDRESS, destinationAddress);
					intent.putExtra(Constants.TEMP_START_TIME, startTime);
					startActivity(intent);
					/*Date startDate = Date.valueOf(startTime);
					searchTicket(originalAddress, destinationAddress, startDate);*/
				}
			}
		});
		

		
	}

	private void initView() {
		et_original_address = (EditText) findViewById(R.id.et_original_address);
		et_destination_address = (EditText) findViewById(R.id.et_destination_address);
		et_start_day = (EditText) findViewById(R.id.et_start_day);
		btn_search = (Button) findViewById(R.id.btn_search);
		
		Intent intent = getIntent();
		if (intent != null) {
			et_original_address.setText(intent.getStringExtra("et_original_address"));
			et_destination_address.setText(intent.getStringExtra("et_destination_address"));
			et_start_day.setText(intent.getStringExtra("et_start_day"));
		}
	}



	@Override
	protected void addUserDataView(ViewContainer container) {
		container.addView("1", new Pair<String, View>("et_original_address",et_original_address), new Pair<String, View>("et_destination_address", et_destination_address), new Pair<String, View>("et_start_day", et_start_day));
	}
	
}
