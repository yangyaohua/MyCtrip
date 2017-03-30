package com.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myctrip.R;
import com.example.utils.Constants;
import com.example.utils.HttpUtil;
import com.example.utils.SpUtil;

public class LoginActivity extends BaseActivity{

	private Button btn_login;
	private EditText et_user_id, et_password;
	private TextView tv_regist;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Boolean success = (Boolean) msg.obj;
			if (success) {
				Toast.makeText(LoginActivity.this, "登录成功", 1).show();
				SpUtil.put(LoginActivity.this, Constants.LOGIN_STATE, true);
				startActivity(new Intent(LoginActivity.this,SearchTicketActivity.class));
			}else {
				Toast.makeText(LoginActivity.this, "网络连接失败,登录失败", 1).show();
				//startActivity(new Intent(LoginActivity.this,SearchTicketActivity.class));
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle("登录");
		initView();
		initEvent();
	}

	private void initEvent() {
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String userId = et_user_id.getText().toString().trim();
				String password = et_password.getText().toString().trim();
				if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(password)) {
					checkLogin(userId, password);
				}else {
					Toast.makeText(LoginActivity.this, "请填入完整信息", 1).show();
				}
			}
		});
	}

	protected void checkLogin(final String userId, final String password) {
		new Thread(){
			public void run() {
				String result = HttpUtil.login(LoginActivity.this, userId, password);
				boolean success = Boolean.valueOf(result);
				Message msg = Message.obtain();
				msg.obj = success;
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	private void initView() {
		btn_login = (Button) findViewById(R.id.btn_login);
		et_password = (EditText) findViewById(R.id.et_password);
		et_user_id = (EditText) findViewById(R.id.et_username);
		tv_regist = (TextView) findViewById(R.id.tv_regist);
	}

}
