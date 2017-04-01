package com.yyh.xml;

public class Method {
	private String type;
	private String format;
	private String inject;
	private String capture;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getInject() {
		return inject;
	}
	public void setInject(String inject) {
		this.inject = inject;
	}
	public String getCapture() {
		return capture;
	}
	public void setCapture(String capture) {
		this.capture = capture;
	}
	@Override
	public String toString() {
		return "Method [type=" + type + ", format=" + format + ", inject="
				+ inject + ", capture=" + capture + "]";
	}
	
	
}
