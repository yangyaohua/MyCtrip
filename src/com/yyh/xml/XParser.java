package com.yyh.xml;

import java.lang.reflect.Field;

import org.xmlpull.v1.XmlPullParser;

import android.R.integer;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.nfc.Tag;
import android.util.Log;

import com.example.myctrip.R;

public class XParser {

	private static String TAG = "XParser";
	
	public static Methods parseMethodXML(Context context){
		Methods methods = new Methods();
		try {
			Class<?> xmlClass = R.xml.class;
			Field declaredField = xmlClass.getDeclaredField("method");
			int id = (int) declaredField.get(null);
			XmlResourceParser xml = context.getResources().getXml(id);

			int event = xml.getEventType();
			while(event != XmlResourceParser.END_DOCUMENT){
				switch (event) {
				case XmlResourceParser.START_DOCUMENT:
					break;
				case XmlResourceParser.START_TAG:
					if (xml.getName().equals("method")) {
						Method method = new Method();
						method.setCapture(xml.getAttributeValue(null, "capture"));
						method.setFormat(xml.getAttributeValue(null, "format"));
						method.setInject(xml.getAttributeValue(null, "inject"));
						method.setType(xml.getAttributeValue(null, "type"));
						methods.getMap().put(method.getType(), method);
					}
					break;
				case XmlResourceParser.TEXT:
					break;
				case XmlResourceParser.END_TAG:
					break;
				default:
					break;
				}
				event = xml.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return methods;
	}
	
	/**
	 * 解析oschina_app.xml，并保存到process对象。
	 * @param context
	 * @return
	 */
	public static Process parseAPPxml(Context context){
		Process process = new Process();
		try{
			Class<?> xmlClass = R.xml.class;
			Field declaredField = xmlClass.getDeclaredField("oschina_app");
			int object = (int) declaredField.get(null);
			XmlResourceParser xml = context.getResources().getXml(object);
			int event = xml.getEventType();
			String taskKey = null;
			while(event != XmlResourceParser.END_DOCUMENT){
				switch (event) {
				case XmlResourceParser.START_DOCUMENT:
					break;
				case XmlResourceParser.START_TAG:
					if (xml.getName().equals("process")) {
						process.setPackageName(xml.getAttributeValue(null, "package"));
						process.setProcessName(xml.getAttributeValue(null,"name"));
						process.setTerminalName(xml.getAttributeValue(null,"terminal"));
					}else if (xml.getName().equals("task")) {
						Task task = new Task();
						task.setTaskClass(xml.getAttributeValue(null,"class"));
						task.setTaskName(xml.getAttributeValue(null,"name"));
						taskKey = task.getTaskClass();
						process.getTaskMap().put(taskKey, task);
					}else if (xml.getName().equals("data")) {
						Data data = new Data();
						data.setDataName(xml.getAttributeValue(null, "name"));
						data.setDataId(xml.getAttributeValue(null, "id"));
						data.setDataType(xml.getAttributeValue(null, "type"));
						process.getTaskMap().get(taskKey).getDataList().add(data);
					}
				case XmlResourceParser.TEXT:
					break;
				case XmlResourceParser.END_TAG:
					break;
				default:
					break;
				}
				event = xml.next();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return process;
	}
	
	public static boolean isXmlIdExist(String ... xmls){
		try {
			for(String id : xmls){
				Class<?> xmlClass = R.xml.class;
				Field declaredField = xmlClass.getDeclaredField(id);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
