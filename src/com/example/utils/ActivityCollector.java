package com.example.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.activity.LoginActivity;
import com.example.activity.SearchTicketActivity;

import android.app.Activity;
import android.util.Log;

public class ActivityCollector {
	public static List<Activity> activities = new ArrayList<Activity>();
	
	public static void addActivity(Activity activity){
		activities.add(activity);
	}
	
	public static void removeActivity(Activity activity){
		activities.remove(activity);
	}
	
	public static void finishAll(){
		for(Activity activity:activities){
			if(!activity.isFinishing()){
				if (activity instanceof LoginActivity) {
					continue;
				}
				if (activity instanceof SearchTicketActivity) {
					continue;
				}
				activity.finish();
			}
		}
	}
}
