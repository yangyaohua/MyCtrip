package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myctrip.R;
import com.example.utils.ActivityCollector;
import com.example.utils.Constants;
import com.example.utils.HttpUtil;
import com.example.utils.SpUtil;

public class AddPassengerInfoActivity extends BaseActivity {

	private EditText et_book_name,et_certificate_number,et_token_name, et_phonenumber;
	private Spinner spinner_certificates_type;
	private Button btn_book;
	private static int MESSAGE_REVERSE = 0;
	private static int MESSAGE_TICKET_PRICE = 1;
	private String cnumber;
	private TextView tv_price;

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				boolean result = (Boolean) msg.obj;
				if (result) {
					Intent intent = new Intent(AddPassengerInfoActivity.this, PayForTicketActivity.class);
					startActivity(intent);
				}else {
					startActivity(new Intent(AddPassengerInfoActivity.this,LoginActivity.class));
					SpUtil.remove(AddPassengerInfoActivity.this, Constants.ANDROID_SESSIONID);
					Toast.makeText(AddPassengerInfoActivity.this, "尚未登录(session过期),或连接服务器出错", Toast.LENGTH_LONG).show();
				}
				break;
			case 1:
				String price = (String) msg.obj;
				if (price != null && !price.equals("")) {
					tv_price.setText("需要支付： " + price + "元");
				}
				break;
			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_passenger_info);
		setTitle("乘客信息");
		ActivityCollector.addActivity(this);
		
		initView();
		initEvent();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		new Thread(){
			public void run() {
				String result = HttpUtil.getTicketPrice(AddPassengerInfoActivity.this);
				Message msg = Message.obtain();
				msg.what = MESSAGE_TICKET_PRICE;
				msg.obj = result;
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	private void initEvent() {
		btn_book.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkInfo();
			}
		});
	}

	protected void checkInfo() {
		String bname = et_book_name.getText().toString().trim();
		String ctype = (String) spinner_certificates_type.getSelectedItem();
		cnumber = et_certificate_number.getText().toString().trim();
		String phoneNumber = et_phonenumber.getText().toString().trim();
		String tname = et_token_name.getText().toString().trim();
		if (bname.equals("") || cnumber.equals("") || phoneNumber.equals("") || tname.equals("")) {
			Toast.makeText(this, "请输入完整信息", Toast.LENGTH_LONG).show();
		}else {
			boolean login = (Boolean) SpUtil.get(AddPassengerInfoActivity.this, Constants.LOGIN_STATE, false);
			if (!login) {
				startActivity(new Intent(AddPassengerInfoActivity.this, LoginActivity.class));
				return;
			}
			reverseTicket();
		}
	}

	private void reverseTicket() {
		new Thread(){
			public void run() {
				boolean result = HttpUtil.reverseTicket(AddPassengerInfoActivity.this);
				Message msg = Message.obtain();
				msg.what = MESSAGE_REVERSE;
				msg.obj = result;
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	private void initView() {
		et_book_name = (EditText) findViewById(R.id.et_book_name);
		spinner_certificates_type = (Spinner) findViewById(R.id.spinner_certificates_type);
		et_certificate_number = (EditText) findViewById(R.id.et_certificate_number);
		
		et_token_name = (EditText) findViewById(R.id.et_token_name);
		et_phonenumber = (EditText) findViewById(R.id.et_phonenumber);
		btn_book = (Button) findViewById(R.id.btn_pay);
		tv_price = (TextView) findViewById(R.id.tv_price);
	}

}
