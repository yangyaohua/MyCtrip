package com.yyh.hook;

import android.app.Application;

public class MyApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		
		try {
			new ActivityThreadHooker();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
