package com.example.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bean.Task;
import com.example.utils.Convert2Json;
import com.example.utils.HttpUtil;

public class ViewContainer {

	private static Context mContext;
	private static ViewContainer viewContainer = new ViewContainer();

	private Map<String, Map<String, View>> map = new HashMap<String, Map<String, View>>();

	public void addView(String index, Pair<String, View>... pairs) {
		Map<String, View> m = new HashMap<String, View>();
		for (Pair<String, View> pair : pairs) {
			m.put(pair.first, pair.second);
		}
		map.put(index, m);
	}

	public static ViewContainer getInstance(Context context) {
		mContext = context;
		return viewContainer;
	}

	private void showList() {
		if (map != null && !map.isEmpty()) {
			for (String index : map.keySet()) {
				Map<String, View> m = map.get(index);
				for (String key : m.keySet()) {
					View view = m.get(key);
					String item = null;
					if (view instanceof EditText) {
						item = ((EditText) view).getText().toString();
					}
					if (view instanceof Spinner) {
						item = ((Spinner) view).getSelectedItemPosition() + "";
					}
					Log.e("ViewContainer:", index + " : " + key + " ," + item);
				}
			}
		}
		// saveData();
	}

	private List<Task> map2List() {
		List<Task> tasks = new ArrayList<Task>();
		if (map != null && !map.isEmpty()) {
			for (String index : map.keySet()) {
				Map<String, View> m = map.get(index);
				for (String key : m.keySet()) {
					View view = m.get(key);
					String value = view.toString();
					if (view instanceof EditText) {
						value = ((EditText) view).getText().toString();
					}
					if (view instanceof Spinner) {
						value = ((Spinner) view).getSelectedItemPosition() + "";
					}
					Task task = new Task(index, key, value);
					tasks.add(task);
				}
			}
		}
		return tasks;
	}

	/**
	 * 将所有数据保存到contentProvider;
	 */
	public void saveData(Context context) {
		List<Task> map2List = map2List();
		updateMyContentProvider(context, map2List);
		/*
		 * String data = Convert2Json.listTask2Json(map2List);
		 * FileHelper.downloadData("data.txt", data); new Thread(){ public void
		 * run() { HttpUtil.uploadData(mContext,"data.txt"); }; }.start();
		 */
	}

	private void updateMyContentProvider(Context context, List<Task> map2List) {
		String packageName = context.getPackageName();
		ContentResolver contentResolver = mContext.getContentResolver();
		try {
			contentResolver.delete(MyProvider.URI, null, null);
			ContentValues values = new ContentValues();
			for (Task task : map2List) {
				values.put(MyProvider.COLUMN_PACKAGE, packageName);
				values.put(MyProvider.COLUMN_ID, task.getIndex());
				values.put(MyProvider.COLUMN_KEY, task.getKey());
				values.put(MyProvider.COLUMN_VALUE, task.getValue());
				contentResolver.insert(MyProvider.URI, values);
			}
		} catch (Exception e) {
			Toast.makeText(context, "未安装MyDataDriver,不支持状态迁移操作", Toast.LENGTH_LONG).show();
		}
	}
}
