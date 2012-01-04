package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @ ZjHistory.java
 * 作用 : 浙江药监局要用的历史表
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-4-13     YANGZHONLI       create
 */
public class ZjHistory implements Serializable{
	
	private static final long serialVersionUID = -2311825028498293790L;
	
    int HID;					// 自增长字段
    int MonitorID;				// 可以对应仪器的主键ID
    double Temperature;			// 记录的温度值
    double Humidity;			// 记录的湿度值
    Date CheckTime;				// 理解为记录时间
    int bSend;					// 好像是是否发送的意思
    String MonitorName;			// 仪器名
    int LicenseNo;
    
	public int getHID() {
		return HID;
	}
	public void setHID(int hid) {
		HID = hid;
	}
	public int getMonitorID() {
		return MonitorID;
	}
	public void setMonitorID(int monitorID) {
		MonitorID = monitorID;
	}
	public double getTemperature() {
		return Temperature;
	}
	public void setTemperature(double temperature) {
		Temperature = temperature;
	}
	public double getHumidity() {
		return Humidity;
	}
	public void setHumidity(double humidity) {
		Humidity = humidity;
	}
	public Date getCheckTime() {
		return CheckTime;
	}
	public void setCheckTime(Date checkTime) {
		CheckTime = checkTime;
	}
	public int getBSend() {
		return bSend;
	}
	public void setBSend(int send) {
		bSend = send;
	}
	public String getMonitorName() {
		return MonitorName;
	}
	public void setMonitorName(String monitorName) {
		MonitorName = monitorName;
	}
	public int getLicenseNo() {
		return LicenseNo;
	}
	public void setLicenseNo(int licenseNo) {
		LicenseNo = licenseNo;
	}
	
}
