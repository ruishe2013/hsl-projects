package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @ ZjHistory.java
 * ���� : �㽭ҩ���Ҫ�õ���ʷ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-4-13     YANGZHONLI       create
 */
public class ZjHistory implements Serializable{
	
	private static final long serialVersionUID = -2311825028498293790L;
	
    int HID;					// �������ֶ�
    int MonitorID;				// ���Զ�Ӧ����������ID
    double Temperature;			// ��¼���¶�ֵ
    double Humidity;			// ��¼��ʪ��ֵ
    Date CheckTime;				// ���Ϊ��¼ʱ��
    int bSend;					// �������Ƿ��͵���˼
    String MonitorName;			// ������
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
