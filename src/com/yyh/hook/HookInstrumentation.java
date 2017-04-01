package com.yyh.hook;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Instrumentation;

import com.yyh.utils.ActivityDataCapturer;
import com.yyh.utils.ActivityDataInjector;
import com.yyh.xml.XParser;

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
				if (XParser.isXmlIdExist("oschina_app", "method")) {
					ActivityDataInjector.getInstance().injectDatasXMLComplete(activity);
				}else if (XParser.isXmlIdExist("oschina_app"))
					ActivityDataInjector.getInstance().injectDatasByXML(activity);
				else
					ActivityDataInjector.getInstance().injectDatasAuto(activity);
				methodResume.invoke(mOldInstrumentation, activity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		super.callActivityOnResume(activity);
	}

	@Override
	public void callActivityOnPause(Activity activity) {
		if (methodPause != null) {
			try {
				if (XParser.isXmlIdExist("oschina_app", "method")) {
					ActivityDataCapturer.getInstance().saveDatasXMLComplete(activity);
				}else if (XParser.isXmlIdExist("oschina_app"))
					ActivityDataCapturer.getInstance().saveDatasByXML(activity);
				else
					ActivityDataCapturer.getInstance().saveDatasAuto(activity);
				methodResume.invoke(mOldInstrumentation, activity);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.callActivityOnPause(activity);
	}

}
