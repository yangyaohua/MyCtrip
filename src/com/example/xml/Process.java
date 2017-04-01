package com.example.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Process {
	private String processName;
	private String terminalName;
	private String packageName;
	private Map<String, Task> taskMap = new HashMap<>();
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getTerminalName() {
		return terminalName;
	}
	public void setTerminalName(String terminalName) {
		this.terminalName = terminalName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Map<String, Task> getTaskMap() {
		return taskMap;
	}
	@Override
	public String toString() {
		return "Process [processName=" + processName + ", terminalName="
				+ terminalName + ", packageName=" + packageName + ", taskMap="
				+ taskMap + "]";
	}
	
	
	
	
	
}
