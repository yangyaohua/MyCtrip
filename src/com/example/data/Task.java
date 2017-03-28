package com.example.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Task{
	private String taskName;
	private String taskClass;
	private List<Data> dataList = new ArrayList<>();
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskClass() {
		return taskClass;
	}
	public void setTaskClass(String taskClass) {
		this.taskClass = taskClass;
	}
	public List<Data> getDataList() {
		return dataList;
	}
	@Override
	public String toString() {
		return "Task [taskName=" + taskName + ", taskClass=" + taskClass
				+ ", dataList=" + dataList + "]";
	}
	
	
	
	
}