package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myctrip.R;
import com.example.utils.Constants;
import com.example.utils.HttpUtil;
import com.example.utils.SpUtil;

public class WelcomeActivity extends Activity {

	public enum VISITOR_TYPE {
		PC, ANDROID;
	}

	private static VISITOR_TYPE type = VISITOR_TYPE.ANDROID;
	private Button btn_pc;
	private Button btn_android;
	private LinearLayout ll_root;
	private String pcId;
	private String androidId;
	private long startTime;
	private static final int MESSAGE_SESSIONID = 0;
	private static final int MESSAGE_SESSION_PIECE = 1;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_SESSIONID:
				handleSessionId(msg);
				break;
			case MESSAGE_SESSION_PIECE:
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		setContentView(R.layout.activity_welcome);
		
		startTime = System.currentTimeMillis();
		initView();
		initAnimation();
		initEvent();
	}


	private void handleSessionId(Message msg) {
		String result = (String) msg.obj;
		if (result != null && !result.equals("")) {
			String[] split = result.split(";");
			pcId = split[0];
			androidId = split[1];
			if (type == VISITOR_TYPE.ANDROID) {
				if (androidId != null && !androidId.equals("")) {
					String value = androidId.split("=")[1];
					if (value == null || value.equals("")
							|| value.equals("null")) {
						SpUtil.put(WelcomeActivity.this,
								Constants.ANDROID_SESSIONID, "");
						startActivity(new Intent(WelcomeActivity.this,
								LoginActivity.class));
					} else {
						String JSESSIONID = "JSESSIONID=" + value;
						SpUtil.put(WelcomeActivity.this,
								Constants.ANDROID_SESSIONID, JSESSIONID);
						Toast.makeText(WelcomeActivity.this, androidId,
								Toast.LENGTH_LONG).show();
						startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
						//downloadPiece();
					}
				}
			} else if (type == VISITOR_TYPE.PC) {
				if (pcId != null && !pcId.equals("")) {
					String[] pcIdKeyAndValue = pcId.split("=");
					String value = pcIdKeyAndValue[1];
					if (value == null || value.equals("")
							|| value.equals("null")) {
						SpUtil.remove(WelcomeActivity.this,
								Constants.PC_SESSIONID);
						Toast.makeText(WelcomeActivity.this, "PC端未登录过",
								Toast.LENGTH_LONG).show();
						return;
					}
					String JSESSIONID = "JSESSIONID=" + value;
					SpUtil.put(WelcomeActivity.this,
							Constants.PC_SESSIONID, JSESSIONID);
					Toast.makeText(WelcomeActivity.this, pcId,
							Toast.LENGTH_LONG).show();
				}
			}
		} else {
			Toast.makeText(WelcomeActivity.this, "服务器睡着了",
					Toast.LENGTH_LONG).show();
			startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
		}		
	}


	public static VISITOR_TYPE getType() {
		return type;
	}

	private void initEvent() {
		btn_android.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				type = VISITOR_TYPE.ANDROID;			
			}
		});
		
		downloadSessionId();
	}

	protected void downloadPiece() {
		new Thread() {
			public void run() {
				String piece = HttpUtil
						.downloadSessionPiece(WelcomeActivity.this);
				Message msg = Message.obtain();
				msg.obj = piece;
				msg.what = MESSAGE_SESSION_PIECE;
				waitAndSleep();
				mHandler.sendMessage(msg);
			}


		}.start();
	}

	protected void downloadSessionId() {
		new Thread() {
			public void run() {
				String result = HttpUtil
						.downloadPcSessionId(WelcomeActivity.this);
				Message msg = Message.obtain();
				msg.obj = result;
				msg.what = MESSAGE_SESSIONID;
				waitAndSleep();
				mHandler.sendMessage(msg);
			};
		}.start();
	}
	
	private void waitAndSleep() {
		long endTime = System.currentTimeMillis();
		if (endTime - startTime < 2000) {
			try {
				Thread.sleep(2000 - (endTime - startTime));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	private void initView() {
		btn_pc = (Button) findViewById(R.id.btn_pc);
		btn_android = (Button) findViewById(R.id.btn_android);
		ll_root = (LinearLayout) findViewById(R.id.ll_root);
	}
	
	private void initAnimation() {
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		ll_root.startAnimation(alphaAnimation);
	}
	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
}
