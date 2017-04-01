package com.yyh.xml;

public class Data{
	private String dataName;
	private String dataId;
	private String dataType;
	
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
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
		return "Data [dataName=" + dataName + ", dataId=" + dataId
				+ ", dataType=" + dataType + "]";
	}
	
	
}