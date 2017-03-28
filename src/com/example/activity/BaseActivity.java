package com.example.activity;

import com.example.data.ViewContainer;
import com.example.myctrip.R;
import com.example.utils.ActivityCollector;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public abstract class BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActivityCollector.addActivity(this);
	}

	@Override
	protected void onResume() {
		addUserDataView(ViewContainer.getInstance(this));
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		ViewContainer.getInstance(this).saveData(BaseActivity.this);
		super.onPause();
	}
	
	/**
	 * 添加用户需要转移的数据
	 * @param instance
	 */
	protected abstract void addUserDataView(ViewContainer instance);

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_data, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_save:
			ViewContainer.getInstance(this).saveData(BaseActivity.this);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
