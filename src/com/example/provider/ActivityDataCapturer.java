package com.example.provider;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.xml.Data;
import com.example.xml.Process;
import com.example.xml.XmlParser;

public class ActivityDataCapturer {

	private static class ActiviytDataCapturerHolder{
		static final ActivityDataCapturer instance = new ActivityDataCapturer();
	}
	
	private ActivityDataCapturer(){
	}
	
	public static ActivityDataCapturer getInstance(){
		return ActiviytDataCapturerHolder.instance;
	}
	
	
	/**
	 * 读取xml文件，并装饰获取到的数据。保存到contentProvider中。
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
	
	
	/**
	 * 通过xml中的id获取activity下的文本信息。
	 * @param activity
	 * @return
	 * @throws Exception
	 */
	private Map<String, String> captureDatasByXML(Activity activity) throws Exception {
		Process process = XmlParser.parseXmlData();

		Class<? extends Activity> activityClass = activity.getClass();
		List<Data> dataList = process.getTaskMap().get(activity.getComponentName().getClassName()).getDataList();
		if (dataList == null || dataList.isEmpty()) {
			return null;
		}
		Map<String, String> map = new HashMap<>();
		for(Data data : dataList){
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
						.getSelectedItemId();
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


}
