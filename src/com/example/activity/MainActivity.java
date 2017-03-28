package com.example.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.fragment.MainFragment;
import com.example.fragment.MineFragment;
import com.example.myctrip.R;

public class MainActivity extends FragmentActivity {

	private TextView tv_login, tv_about, tv_main, tv_mine;
	private View v_main, v_mine;
	private FragmentManager mManager;
	private FragmentTransaction mTransition;
	private int mFragmentIndex;
	private MainFragment mainFragment;
	private MineFragment mineFragment;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		initDatas();
		initEvent();
	}

	private void initDatas() {
		mManager = getSupportFragmentManager();
		mTransition = mManager.beginTransaction();
		tv_main.setTextColor(ColorStateList.valueOf(Color.RED));
		
		mainFragment = new MainFragment();
		mFragmentIndex = 0;
		mTransition.replace(R.id.fl_content, mainFragment);
		mTransition.commit();
	}

	private void initView() {
		tv_login = (TextView) findViewById(R.id.tv_login);
		tv_about = (TextView) findViewById(R.id.tv_about);
		
		v_main = findViewById(R.id.v_main);
		v_mine = findViewById(R.id.v_mine);
		
		tv_main = (TextView) findViewById(R.id.tv_main);
		tv_mine = (TextView) findViewById(R.id.tv_mine);
	}

	private void initEvent() {
		
		tv_main.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFragmentIndex = 0;
				updateFragment();
			}
		});
		
		tv_mine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFragmentIndex = 1;
				updateFragment();
			}
		});

		tv_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,LoginActivity.class));
			}
		});
		
		tv_about.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this,AboutActivity.class));
			}
		});
	}

	protected void updateFragment() {
		mManager = getSupportFragmentManager();
		mTransition = mManager.beginTransaction();
		hideFragment();
		switch (mFragmentIndex) {
		case 0:
			tv_main.setTextColor(ColorStateList.valueOf(Color.RED));
			tv_mine.setTextColor(ColorStateList.valueOf(Color.WHITE));
			v_main.setVisibility(View.VISIBLE);
			v_mine.setVisibility(View.INVISIBLE);
			mainFragment = new MainFragment();
			mTransition.replace(R.id.fl_content, mainFragment);
			break;
		case 1:
			tv_main.setTextColor(ColorStateList.valueOf(Color.WHITE));
			tv_mine.setTextColor(ColorStateList.valueOf(Color.RED));
			v_main.setVisibility(View.INVISIBLE);
			v_mine.setVisibility(View.VISIBLE);
			mineFragment = new MineFragment();
			mTransition.replace(R.id.fl_content, mineFragment);
			break;
		default:
			break;
		}
		mTransition.commit();
	}

	private void hideFragment() {
		if (mineFragment != null) {
			mTransition.remove(mineFragment);
		}
		if (mainFragment != null) {
			mTransition.remove(mainFragment);
		}
	}

	
}
