package com.example.ams;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

public class HookInstrumentation extends Instrumentation {

	private static final String TAG = "HookInstrumentation";
	private Instrumentation mOldInstrumentation = null;
	private Method methodResume = null;
	private Method methodStart = null;
	private Method methodPause = null;
	
	private void obtainMethods() throws NoSuchMethodException{
		if (mOldInstrumentation == null) {
			return;
		}
		Class<? extends Instrumentation> inClass = mOldInstrumentation.getClass();
		methodResume = inClass.getMethod("callActivityOnResume", Activity.class);
		methodStart = inClass.getMethod("callActivityOnStart", Activity.class);
		methodPause = inClass.getMethod("callActivityOnPause", Activity.class);
	}
	
	public boolean initOldInstr(Instrumentation oldInstrumentation) {
		mOldInstrumentation = oldInstrumentation;
		try {
			obtainMethods();
			return true;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 在调用onResume()方法时，拦截并恢复数据。
	 */
	@Override
	public void callActivityOnResume(Activity activity) {
		if (methodResume != null) {
			try {
				restoreData(activity);
				methodResume.invoke(mOldInstrumentation, activity);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			Log.e(TAG, "OnResume Intercept here");
		}

			super.callActivityOnResume(activity);
	}
	
	@Override
	public void callActivityOnPause(Activity activity) {
		if (methodPause != null) {
			try {
				saveDatas(activity);
				methodResume.invoke(mOldInstrumentation, activity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e(TAG, "onPause Intercept here");
		}
		super.callActivityOnPause(activity);
	}
	
	/**
	 * 将所有需要保存的字段，存储到ContentProvider中。
	 * @param activity
	 * @throws Exception
	 */
	private void saveDatas(Activity activity) throws Exception {
		ContentResolver contentResolver = activity.getContentResolver();
		ContentValues values = new ContentValues();
		String packageName = activity.getPackageName();
		String activityName = activity.getComponentName().getClassName();
		Map<String, String> map = captureDatas(activity);
		for(String key : map.keySet()){
			String value = map.get(key);
			values.put(Constants.COLUMN_PACKAGE, packageName);
			values.put(Constants.COLUMN_ID, activityName);
			values.put(Constants.COLUMN_KEY, key);
			values.put(Constants.COLUMN_VALUE, value);
			contentResolver.insert(Constants.URI_SAVE, values);
		}
	}

	/**
	 * 从activity中，获取需要保存的字段，用key-value方式保存到内存中。
	 * @param activity 
	 * @return
	 * @throws Exception 
	 */
	private Map<String, String> captureDatas(Activity activity) throws Exception {
		Map<String, String> map = new HashMap<>();
		Class<? extends Activity> activityClass = activity.getClass();
		Field[] parametersFields = activityClass.getDeclaredFields();
		for(Field field : parametersFields){
			field.setAccessible(true);
			String key = field.getName();
			String value = "";
			Object fieldObject = field.get(activity);
			if (fieldObject instanceof EditText) {
				value = ((EditText) fieldObject).getText().toString();
				map.put(key, value);
			}
			if (fieldObject instanceof Spinner) {
				value = String.valueOf(((Spinner) fieldObject).getSelectedItemPosition());
			}
		}
		return map;
	}

	/**
	 * 对于指定的activity，在onResume()方法中，恢复所有ContentProvider中保存的数据。
	 * @param activity
	 * @throws Exception
	 */
	private void restoreData(Activity activity) throws Exception {
		Class activityClass = activity.getClass();
		Map<String, String> dataMap = queryDatas(activity);
		if (dataMap != null && !dataMap.isEmpty()) {
			for(String key : dataMap.keySet()){
				String value = dataMap.get(key);
				Field[] parametersFields = activityClass.getDeclaredFields();
				if (hasField(parametersFields, key)) {
					Field keyField = activityClass.getDeclaredField(key);
					keyField.setAccessible(true);
					Object keyObject = keyField.get(activity);
					if (keyObject instanceof EditText) {
						((EditText) keyObject).setText(value);
					}else if (keyObject instanceof Spinner) {
						if (value != null && value.length() == 1) {
							((Spinner) keyObject).setSelection(Integer.valueOf(value));
						}
					}
				}
			}
		}
	}

	/**
	 * 判断activity中，是否存在变量名为key的字段。
	 * @param parametersFields
	 * @param key
	 * @return
	 */
	private boolean hasField(Field[] parametersFields, String key) {
		for(Field field : parametersFields){
			String name = field.getName();
			if (name.equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从内容提供器中，取出该activity中，所需要恢复的数据。
	 * @param activity
	 * @return
	 */
	private Map<String, String> queryDatas(Activity activity) {
		Map<String, String> map = new HashMap<>();
		ContentResolver contentResolver = activity.getContentResolver();
		String packageName = activity.getPackageName();
		String activityName = activity.getComponentName().getClassName();
		
		Cursor query = contentResolver.query(Constants.URI_RESTORE, null, Constants.COLUMN_PACKAGE + " = ? and " + Constants.COLUMN_ID + " = ?", new String[]{packageName, activityName}, null);
		while(query.moveToNext()){
			String key = query.getString(query.getColumnIndex(Constants.COLUMN_KEY));
			String value = query.getString(query.getColumnIndex(Constants.COLUMN_VALUE));
			map.put(key, value);
		}
		return map;
	}

}
