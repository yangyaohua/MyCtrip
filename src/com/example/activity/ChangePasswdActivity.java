package com.example.activity;


import com.example.myctrip.R;
import com.example.utils.HttpUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswdActivity extends Activity {

	private EditText et_old;
	private EditText et_renew;
	private EditText et_new;
	private Button submit;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				boolean success = (Boolean) msg.obj;
				if (success) {
					Toast.makeText(getApplicationContext(), "修改密码成功", 1).show();
					finish();
				}else {
					Toast.makeText(getApplicationContext(), "修改密码失败", 1).show();
				}
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "信息不完整", 1).show();

				break;
			case 2:
				Toast.makeText(getApplicationContext(), "新密码不一致", 1).show();

				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_change_passwd);
		initView();
		
		initEvent();
	}

	private void initEvent() {
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(){
					public void run() {
						String oldP = et_old.getText().toString().trim();
						String newP = et_new.getText().toString().trim();
						String renewP = et_renew.getText().toString().trim();
						if (oldP.equals("") || newP.equals("") || renewP.equals("")) {
							mHandler.sendEmptyMessage(1);
						}else if (!newP.equals(renewP)) {
							mHandler.sendEmptyMessage(2);
						}else{
							boolean success = HttpUtil.changePassword(ChangePasswdActivity.this,oldP,newP);
							Message message = Message.obtain();
							message.obj = success;
							message.what = 0;
							mHandler.sendMessage(message);
						}
					};
				}.start();
			}
		});
	}

	private void initView() {
		et_old = (EditText) findViewById(R.id.et_old);
		et_new = (EditText) findViewById(R.id.et_new);
		et_renew = (EditText) findViewById(R.id.et_renew);
		submit = (Button) findViewById(R.id.submit);
	}
}
