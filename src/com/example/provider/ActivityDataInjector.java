package com.example.provider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ams.Constants;
import com.example.xml.Data;
import com.example.xml.Process;
import com.example.xml.Task;
import com.example.xml.XmlParser;

/**
 * 使用单例模式，对Activity的页面进行信息注入
 * @author Administrator
 *
 */
public class ActivityDataInjector {
	
	private static class ActivityDataInjectorHolder{
		final static ActivityDataInjector instance = new ActivityDataInjector();
	}
	
	private ActivityDataInjector(){
	}
	
	public static ActivityDataInjector getInstance(){
		return ActivityDataInjectorHolder.instance;
	}
	
	/**
	 * 通过解析xml文件恢复数据。
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public void restoreDatasByXML(Activity activity) throws Exception {
		Class<? extends Activity> activityClass = activity.getClass();
		String activityName = activity.getComponentName().getClassName();
		Map<String, String> dataMap = ProviderHelper.getInstance().queryDatas(activity);
		Process process = XmlParser.parseXmlData();
		Map<String, Task> taskMap = process.getTaskMap();
		Task task = taskMap.get(activityName);
		List<Data> dataList = task.getDataList();
		if (dataList == null || dataList.isEmpty()) {
			return ;
		}
		for (Data data : dataList) {
			String dataId = data.getDataId();
			String value = dataMap.get(dataId);
			Field field = activityClass.getDeclaredField(dataId);
			field.setAccessible(true);
			Object keyObject = field.get(activity);
			if (keyObject instanceof EditText) {
				((EditText) keyObject).setText(value);
			} else if (keyObject instanceof Spinner) {
				if (value != null && value.length() == 1) {
					((Spinner) keyObject).setSelection(Integer
							.valueOf(value));
				}
			}
		}
		
		ProviderHelper.getInstance().deleteDatas(activity);
	}

	/**
	 * 对于指定的activity，在onResume()方法中，自动恢复所有ContentProvider中保存的数据。
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public void restoreDatasAuto(Activity activity) throws Exception {
		Class<?> activityClass = activity.getClass();
		Map<String, String> dataMap = ProviderHelper.getInstance().queryDatas(activity);
		if (dataMap != null && !dataMap.isEmpty()) {
			for (String key : dataMap.keySet()) {
				String value = dataMap.get(key);
				Field[] parametersFields = activityClass.getDeclaredFields();
				if (hasField(parametersFields, key)) {
					Field keyField = activityClass.getDeclaredField(key);
					keyField.setAccessible(true);
					Object keyObject = keyField.get(activity);
					if (keyObject instanceof EditText) {
						((EditText) keyObject).setText(value);
					} else if (keyObject instanceof Spinner) {
						if (value != null && value.length() == 1) {
							((Spinner) keyObject).setSelection(Integer
									.valueOf(value));
						}
					}
				}
			}
			
			ProviderHelper.getInstance().deleteDatas(activity);
		}
	}
	
	/**
	 * 判断activity中，是否存在变量名为key的字段。
	 * 
	 * @param parametersFields
	 * @param key
	 * @return
	 */
	private boolean hasField(Field[] parametersFields, String key) {
		for (Field field : parametersFields) {
			String name = field.getName();
			if (name.equals(key)) {
				return true;
			}
		}
		return false;
	}

}
