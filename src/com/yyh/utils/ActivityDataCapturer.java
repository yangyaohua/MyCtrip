package com.yyh.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;

import com.yyh.xml.Data;
import com.yyh.xml.Method;
import com.yyh.xml.Methods;
import com.yyh.xml.Process;
import com.yyh.xml.Task;
import com.yyh.xml.XParser;

public class ActivityDataCapturer {

	private static class ActiviytDataCapturerHolder {
		static final ActivityDataCapturer instance = new ActivityDataCapturer();
	}

	private ActivityDataCapturer() {
	}

	public static ActivityDataCapturer getInstance() {
		return ActiviytDataCapturerHolder.instance;
	}

	/**
	 * 读取xml文件，并装饰获取到的数据。保存到contentProvider中。
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public void saveDatasByXML(Activity activity) throws Exception {
		Map<String, String> map = captureDatasByXML(activity);
		ProviderHelper.getInstance().updateDatas(activity, map);
	}

	/**
	 * 将所有需要保存的字段，存储到ContentProvider中。
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public void saveDatasAuto(Activity activity) throws Exception {
		Map<String, String> map = captureDatasAuto(activity);
		ProviderHelper.getInstance().updateDatas(activity, map);
	}

	public void saveDatasXMLComplete(Activity activity) throws Exception {
		Map<String, String> map = captureDatasXMLComplete(activity);
		ProviderHelper.getInstance().updateDatas(activity, map);
	}

	/**
	 * 通过xml中的id获取activity下的文本信息。
	 * 
	 * @param activity
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> captureDatasByXML(Activity activity)
			throws Exception {
		Process process = XParser.parseAPPxml(activity);

		Class<? extends Activity> activityClass = activity.getClass();
		List<Data> dataList = process.getTaskMap()
				.get(activity.getComponentName().getClassName()).getDataList();
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		Map<String, String> map = new HashMap<>();
		for (Data data : dataList) {
			String dataId = data.getDataId();
			Field field = activityClass.getDeclaredField(dataId);
			field.setAccessible(true);
			Object fieldObject = field.get(activity);
			String value = "";
			if (fieldObject instanceof EditText) {
				value = ((EditText) fieldObject).getText().toString();
				map.put(dataId, value);
			}
			if (fieldObject instanceof Spinner) {
				long selectedItemId = ((Spinner) fieldObject)
						.getSelectedItemPosition();
				value = selectedItemId + "";
				map.put(dataId, value);
			}
		}
		return map;
	}

	/**
	 * 自动从activity中，获取需要保存的字段，用key-value方式保存到内存中。
	 * 
	 * @param activity
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> captureDatasAuto(Activity activity)
			throws Exception {
		Map<String, String> map = new HashMap<>();
		Class<? extends Activity> activityClass = activity.getClass();
		Field[] parametersFields = activityClass.getDeclaredFields();
		for (Field field : parametersFields) {
			field.setAccessible(true);
			String key = field.getName();
			String value = "";
			Object fieldObject = field.get(activity);
			if (fieldObject instanceof EditText) {
				value = ((EditText) fieldObject).getText().toString();
				map.put(key, value);
			}
			if (fieldObject instanceof Spinner) {
				long selectedItemId = ((Spinner) fieldObject)
						.getSelectedItemId();
				value = selectedItemId + "";
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * 完全通过xml进行捕获数据。
	 * 
	 * @param activity
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> captureDatasXMLComplete(Activity activity)
			throws Exception {
		Class<? extends Activity> activityClass = activity.getClass();
		Methods methods = XParser.parseMethodXML(activity);
		Process process = XParser.parseAPPxml(activity);
		Task task = process.getTaskMap().get(
				activity.getComponentName().getClassName());
		if (task == null) {
			return null;
		}
		List<Data> dataList = task.getDataList();
		Map<String, Method> map = methods.getMap();
		Map<String, String> resultMap = new HashMap<>();
		for (Data data : dataList) {
			String dataId = data.getDataId();
			String dataType = data.getDataType();
			Field field = activityClass.getDeclaredField(dataId);
			field.setAccessible(true);
			Object object = field.get(activity);// 获取到对象。

			String captureMethod = map.get(dataType).getCapture();
			Class<?> dataTypeClass = Class.forName(dataType);
			java.lang.reflect.Method declaredMethod = null;
			declaredMethod = dataTypeClass.getMethod(captureMethod);
			Object value = declaredMethod.invoke(object);
			String result = String.valueOf(value);
			resultMap.put(dataId, result);

		}
		return resultMap;
	}
}
