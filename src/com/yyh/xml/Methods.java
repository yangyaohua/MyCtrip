package com.yyh.xml;

import java.util.HashMap;
import java.util.Map;

public class Methods {

	private Map<String, Method> map = new HashMap<>();

	public Map<String, Method> getMap() {
		return map;
	}

	@Override
	public String toString() {
		return "Methods [map=" + map + "]";
	}
	
}
