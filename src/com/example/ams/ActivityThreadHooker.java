package com.example.ams;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Instrumentation;
import android.util.Log;

public class ActivityThreadHooker {

	private static final String TAG = "ActivityThreadHooker";
	private static ActivityThreadHooker instance = null;
	private HookInstrumentation mHookInstrumentation = null;
	private Instrumentation mOldInstrumentation = null;
	private static boolean mHooked = false;
	
	public static boolean getHooked(){
		return mHooked;
	}
	
	public ActivityThreadHooker() throws Throwable{
		synchronized (ActivityThreadHooker.class) {
			if (instance != null) {
				throw new Exception("Only one ActivityThreadHooker instance can be created.");
			}
			instance = this;
			mHookInstrumentation = new HookInstrumentation();
			try {
				initHook();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		mHooked = true;
	}

	private void initHook() throws Exception{
		Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
		Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
		Object activityThreadObject = null;
		try {
			activityThreadObject = currentActivityThreadMethod.invoke(activityThreadClass, null);
		} catch (Exception e) {
			return;
		}
		
		if (activityThreadObject == null) {
			return;
		}
		
		Field instrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
		instrumentationField.setAccessible(true);
		mOldInstrumentation = (Instrumentation) instrumentationField.get(activityThreadObject);
		
		boolean res = mHookInstrumentation.initOldInstr(mOldInstrumentation);
		if (!res) {
			Log.e(TAG, "Error process old Instrumentation");
			return;
		}
		instrumentationField.set(activityThreadObject, mHookInstrumentation);
		Log.e(TAG,"Hook success");
	}
}
