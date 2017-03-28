package com.example.data;

import android.view.View;

@SuppressWarnings("hiding")
public class Pair<String, View> {

	public String first;
	public View second;
	
	public Pair(String first, View second){
		this.first = first;
		this.second = second;
	}
}
