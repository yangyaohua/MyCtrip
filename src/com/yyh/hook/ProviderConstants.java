package com.yyh.hook;

import android.net.Uri;

public class ProviderConstants {
	public static final String AUTHORITY_SAVE = "com.example.mydatadriver.MyProvider";
	public static final Uri URI_SAVE = Uri.parse("content://" + AUTHORITY_SAVE);
	
	public static final String AUTHORITY_RESTORE = "com.example.mydatadriver.RestoreProvider";
	public static final Uri URI_RESTORE = Uri.parse("content://" + AUTHORITY_RESTORE);
	public static final String COLUMN_PACKAGE = "package";
	public static final String COLUMN_ID = "task_id";
	public static final String COLUMN_KEY = "task_key";
	public static final String COLUMN_VALUE = "task_value";
}
