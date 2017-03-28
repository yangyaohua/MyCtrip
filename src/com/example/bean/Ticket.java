package com.example.bean;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Ticket implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6024089069158220116L;
	private int tid;
	private String serialNumber;
	private String originAddress;
	private String desAddress;
	private Timestamp startDay;
	private Timestamp startTime;//具体时间，几点几分
	private Timestamp endTime;
	private double price;
	private String consume;//耗时
	private int numbers;
	
	public Ticket() {
		super();
	}
	public Ticket(int tid, String originAddress, String desAddress, Timestamp startTime) {
		super();
		this.tid = tid;
		this.originAddress = originAddress;
		this.desAddress = desAddress;
		this.startDay = startTime;
	}
	
	
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public String getOriginAddress() {
		return originAddress;
	}
	public void setOriginAddress(String originAddress) {
		this.originAddress = originAddress;
	}
	public String getDesAddress() {
		return desAddress;
	}
	public void setDesAddress(String desAddress) {
		this.desAddress = desAddress;
	}
	public Timestamp getStartDay() {
		return startDay;
	}
	public void setStartDay(Timestamp startDay) {
		this.startDay = startDay;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getConsume() {
		return consume;
	}
	public void setConsume(String consume) {
		this.consume = consume;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getNumbers() {
		return numbers;
	}
	public void setNumbers(int numbers) {
		this.numbers = numbers;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
