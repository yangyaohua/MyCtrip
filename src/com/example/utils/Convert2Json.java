package com.example.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.example.bean.Task;

public class Convert2Json 
{
    public static String obj2Json(Object obj)
    {
    	String json = "{";
        Class c = obj.getClass();

        Field[] fields = c.getDeclaredFields();
        for(int i = 0; i < fields.length; i++)
        {
            Class fieldType = fields[i].getType();
            String typeName = fieldType.getName();
            String fieldName = fields[i].getName();
            String capital = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            Method m = null;
			try 
			{
				m = c.getMethod("get" + capital);
			} 
			catch (NoSuchMethodException e) 
			{
				e.printStackTrace();
			} 
			catch (SecurityException e) 
			{
				e.printStackTrace();
			}
			Object val = null;
			try 
			{
				val = m.invoke(obj);
			} 
			catch (IllegalAccessException e) 
			{
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} 
			catch (InvocationTargetException e) 
			{
				e.printStackTrace();
			}
			if(val instanceof String)
			{
				val = "\"" + val + "\"";
			}
			if(val instanceof Timestamp)
			{
				long time = ((Timestamp) val).getTime();
				val = String.valueOf(time);
				//String s = String.valueOf(val);
				//val = "\"" + s.substring(0, s.lastIndexOf(".")) + "\"";
			}
			/*if (val instanceof Date) {
				long time = ((Date) val).getTime();
				val = String.valueOf(time);
			}
			*/
			json += "\"" + fieldName + "\"" + ":" + val;
			if(i != fields.length - 1)
			{
				json += ", ";
			}
        }
        json += "}";
        return json;
    }
   
    
    
    public static String listTask2Json(List<Task> list){
    	String json = "{\n";
    	for(int i = 0; i < list.size(); i++){
    		json += "\t\"" + i + "\":" + obj2Json(list.get(i));
    		if (i != list.size() - 1) {
				json += ",\n";
			}
    	}
    	json += "\n}";
    	return json;
    }
}
