package com.example.provider;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import com.example.ams.Constants;

/**
 * 用单例，对本地数据库(contentProvider)进行操作。
 * @author Administrator
 *
 */
public class ProviderHelper {
	
	private static class ProviderHelperHolder{
		static final ProviderHelper instance = new ProviderHelper();
	}
	
	private ProviderHelper(){
	}
	
	public static ProviderHelper getInstance(){
		return ProviderHelperHolder.instance;
	}
	
	/**
	 * 根据activity更新MyContentProvider中保存的数据
	 * @param activity
	 * @param map
	 */
	public void updateDatas(Activity activity, Map<String, String> map) {
		String packageName = activity.getPackageName();
		String activityName = activity.getComponentName().getClassName();
		ContentResolver contentResolver = activity.getContentResolver();
		ContentValues values = new ContentValues();
		if (map != null && !map.isEmpty()) {
			contentResolver.delete(Constants.URI_SAVE, Constants.COLUMN_PACKAGE
					+ " = ? ", new String[] { packageName });
			for (String key : map.keySet()) {
				String value = map.get(key);
				values.put(Constants.COLUMN_PACKAGE, packageName);
				values.put(Constants.COLUMN_ID, activityName);
				values.put(Constants.COLUMN_KEY, key);
				values.put(Constants.COLUMN_VALUE, value);
				contentResolver.insert(Constants.URI_SAVE, values);
			}
		}
	}
	
	/**
	 * 从内容提供器中，取出该activity中，所需要恢复的数据。
	 * 
	 * @param activity
	 * @return
	 */
	public Map<String, String> queryDatas(Activity activity) {
		Map<String, String> map = new HashMap<>();
		ContentResolver contentResolver = activity.getContentResolver();
		String packageName = activity.getPackageName();
		String activityName = activity.getComponentName().getClassName();

		Cursor query = contentResolver.query(Constants.URI_RESTORE, null,
				Constants.COLUMN_PACKAGE + " = ? and " + Constants.COLUMN_ID
						+ " = ?", new String[] { packageName, activityName },
				null);
		while (query.moveToNext()) {
			String key = query.getString(query
					.getColumnIndex(Constants.COLUMN_KEY));
			String value = query.getString(query
					.getColumnIndex(Constants.COLUMN_VALUE));
			map.put(key, value);
		}
		return map;
	}
	
	public int deleteDatas(Activity activity){
		int delete = activity.getContentResolver().delete(Constants.URI_RESTORE,
				Constants.COLUMN_PACKAGE + " = ? ",
				new String[] { activity.getPackageName() });
		return delete;
	}
}
