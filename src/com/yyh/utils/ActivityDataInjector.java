package com.yyh.utils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView.BufferType;

import com.yyh.xml.Data;
import com.yyh.xml.Method;
import com.yyh.xml.Methods;
import com.yyh.xml.Process;
import com.yyh.xml.Task;
import com.yyh.xml.XParser;

/**
 * 使用单例模式，对Activity的页面进行信息注入
 * 
 * @author Administrator
 *
 */
public class ActivityDataInjector {

	private static class ActivityDataInjectorHolder {
		final static ActivityDataInjector instance = new ActivityDataInjector();
	}

	private ActivityDataInjector() {
	}

	public static ActivityDataInjector getInstance() {
		return ActivityDataInjectorHolder.instance;
	}

	/**
	 * 通过解析xml文件恢复数据。
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public void injectDatasByXML(Activity activity) throws Exception {
		Class<? extends Activity> activityClass = activity.getClass();
		String activityName = activity.getComponentName().getClassName();
		Map<String, String> dataMap = ProviderHelper.getInstance().queryDatas(
				activity);
		Process process = XParser.parseAPPxml(activity);
		Map<String, Task> taskMap = process.getTaskMap();
		Task task = taskMap.get(activityName);
		List<Data> dataList = task.getDataList();
		if (dataList == null || dataList.isEmpty()) {
			return;
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
					((Spinner) keyObject).setSelection(Integer.valueOf(value));
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
	public void injectDatasAuto(Activity activity) throws Exception {
		Class<?> activityClass = activity.getClass();
		Map<String, String> dataMap = ProviderHelper.getInstance().queryDatas(
				activity);
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

	/**
	 * 通用于所有的数据类型，文本类型。
	 * 
	 * @param activity
	 * @throws Exception
	 */
	public void injectDatasXMLComplete(Activity activity) throws Exception {
		Class<? extends Activity> activityClass = activity.getClass();

		Methods methods = XParser.parseMethodXML(activity);
		Process process = XParser.parseAPPxml(activity);
		String activityName = activity.getComponentName().getClassName();
		Task task = process.getTaskMap().get(activityName);
		if (task == null) {
			return;
		}
		List<Data> dataList = task.getDataList();
		Map<String, String> dataMap = ProviderHelper.getInstance().queryDatas(
				activity);

		for (Data data : dataList) {
			String dataType = data.getDataType();
			String dataId = data.getDataId();
			Field field = activityClass.getDeclaredField(dataId);
			field.setAccessible(true);
			Object object = field.get(activity);// 获取到该对象。

			Method method = methods.getMap().get(dataType);
			Class<?> dataTypeClass = Class.forName(dataType);
			String format = method.getFormat();
			String inject = method.getInject();
			String value = dataMap.get(dataId);

			if (value != null) {
				java.lang.reflect.Method declaredMethod = dataTypeClass.getMethod(inject, formatClass(format));
				/*if (format.equals("int") || format.equals("integer")
						|| format.equals("Integer")) {
					declaredMethod = dataTypeClass.getMethod(inject, int.class);
				} else if (format.equals("string") || format.equals("String")) {
					declaredMethod = dataTypeClass.getMethod(inject,
							CharSequence.class, BufferType.class);
				}*/
				Object valueObject = valueFormat(format, value);
				declaredMethod.invoke(object, valueObject);
				/*if (format.equals("int") || format.equals("integer")
						|| format.equals("Integer")) {
					declaredMethod.invoke(object, valueObject);
				} else if (format.equals("string") || format.equals("String")) {
					declaredMethod.invoke(object, valueObject, null);
				}*/
			}
		}
		ProviderHelper.getInstance().deleteDatas(activity);
	}
	
	private Class<?> formatClass(String format){
		switch (format) {
		case "integer":
		case "int":
		case "Integer":
			return int.class;
		case "string":
		case "String":
			return CharSequence.class;
		default:
			break;
		}
		return CharSequence.class;
	}

	private Object valueFormat(String format, String value) {
		switch (format) {
		case "int":
		case "integer":
		case "Integer":
			return Integer.valueOf(value);
		case "string":
		case "String":
			return String.valueOf(value);
		}
		return value;
	}

}
