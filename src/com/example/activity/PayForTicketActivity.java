package com.example.activity;

import com.example.bean.Ticket;
import com.example.data.Pair;
import com.example.data.ViewContainer;
import com.example.myctrip.R;
import com.example.utils.ActivityCollector;
import com.example.utils.Constants;
import com.example.utils.HttpUtil;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PayForTicketActivity extends BaseActivity {
	
	private EditText et_pay_name;
	private EditText et_pay_password;
	private Button btn_pay;
	private static int MESSAGE_PAY = 0;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				boolean result = (Boolean) msg.obj;
				if (result) {
					Toast.makeText(PayForTicketActivity.this, "支付成功", Toast.LENGTH_LONG).show();;
					ActivityCollector.finishAll();
				}else {
					Toast.makeText(PayForTicketActivity.this, "支付失败", Toast.LENGTH_LONG).show();
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
		setContentView(R.layout.activity_payfor_ticket);
		setTitle("支付");
		ActivityCollector.addActivity(this);
		
		initView();
		initEvent();
	}


	private void initEvent() {
		btn_pay.setOnClickListener( new OnClickListener() {
			@Override
			public void onClick(View v) {
				payforTicket();
			}
		});
	}

	protected void payforTicket() {
		final String phoneNumber = et_pay_name.getText().toString();
		final String payPassword = et_pay_password.getText().toString();
		if (payPassword.equals("") || phoneNumber.equals("")) {
			Toast.makeText(this, "请填入完整信息", Toast.LENGTH_LONG).show();
		}else{
			connect(phoneNumber,payPassword);
		}

	}

	private void connect(final String phoneNumber, final String payPassword) {
		new Thread(){
			public void run() {
				boolean result = HttpUtil.payforTicket(PayForTicketActivity.this, phoneNumber,payPassword);
				Message msg = Message.obtain();
				msg.what = MESSAGE_PAY;
				msg.obj = result;
				mHandler.sendMessage(msg);
			};
		}.start();		
	}

	private void initView() {
		et_pay_name = (EditText) findViewById(R.id.et_pay_name);
		et_pay_password = (EditText) findViewById(R.id.et_pay_password);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		
		Intent intent = getIntent();
		if (intent != null) {
			et_pay_name.setText(intent.getStringExtra("et_pay_name"));
			et_pay_password.setText(intent.getStringExtra("et_pay_password"));
		}
	}


	@Override
	protected void addUserDataView(ViewContainer container) {
		container.addView("3", new Pair<String, View>("et_pay_name",et_pay_name),new Pair<String, View>("et_pay_password", et_pay_password));
	}

}
