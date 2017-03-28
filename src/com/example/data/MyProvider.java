package com.example.data;

import android.net.Uri;

public class MyProvider {
	public static final String AUTHORITY = "com.example.mydatadriver.MyProvider";
	public static final String DOWN_AUTHORITY = "com.example.mydatadriver.DownProvider";
	public static final Uri URI = Uri.parse("content://" + AUTHORITY);
	public static final Uri DOWN_URI = Uri.parse("content://" + DOWN_AUTHORITY);
	public static final String COLUMN_ID = "task_id";
	public static final String COLUMN_KEY = "task_key";
	public static final String COLUMN_VALUE = "task_value";
	public static final String COLUMN_PACKAGE = "package";

}
