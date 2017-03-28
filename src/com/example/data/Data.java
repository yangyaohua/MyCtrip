package com.example.data;

public class Data{
	private String dataName;
	private String dataId;
	public String getDataName() {
		return dataName;
	}
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	@Override
	public String toString() {
		return "Data [dataName=" + dataName + ", dataId=" + dataId + "]";
	}
	
}