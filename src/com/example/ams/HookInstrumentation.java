package com.example.ams;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.provider.ActivityDataCapturer;
import com.example.provider.ActivityDataInjector;
import com.example.provider.ProviderHelper;
import com.example.xml.Data;
import com.example.xml.Process;
import com.example.xml.Task;
import com.example.xml.XmlParser;

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
	private Method methodPause = null;

	private void obtainMethods() throws NoSuchMethodException {
		if (mOldInstrumentation == null) {
			return;
		}
		Class<? extends Instrumentation> inClass = mOldInstrumentation
				.getClass();
		methodResume = inClass
				.getMethod("callActivityOnResume", Activity.class);
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
				if (isXMLExist())
					ActivityDataInjector.getInstance().restoreDatasByXML(activity);
				else
					ActivityDataInjector.getInstance().restoreDatasAuto(activity);
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
				if (isXMLExist())
					ActivityDataCapturer.getInstance().saveDatasByXML(activity);
				else
					ActivityDataCapturer.getInstance().saveDatasAuto(activity);
				methodResume.invoke(mOldInstrumentation, activity);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.e(TAG, "onPause Intercept here");
		}
		super.callActivityOnPause(activity);
	}

	/**
	 * 判断xml文件是否存在
	 * 
	 * @return
	 */
	private boolean isXMLExist() {
		File file = new File(XmlParser.XML_DATA_PATH);
		return file.exists();
	}


}
