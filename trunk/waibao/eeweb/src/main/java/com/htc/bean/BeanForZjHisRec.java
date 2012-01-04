package com.htc.bean;

import java.io.Serializable;
import java.util.Date;

public class BeanForZjHisRec implements Serializable {
	
	private static final long serialVersionUID = 3831813936092566412L;
	
	// ���������ֶ� - ���Ǳ����
	private String placeList;	// �������������б������Ÿ�����
	private	Date timeFrom;		// ��ʼʱ��
	private	Date timeTo;		// ����ʱ��
	private double lowTemp;		// lowTemp��highTemp�γɲ�ѯ�¶ȷ�Χ
	private double highTemp;
	private double lowHumi;		// lowHumi��highHumi�γɲ�ѯʪ�ȷ�Χ
	private double highHumi;
	
	public String getPlaceList() {
		return placeList;
	}
	public void setPlaceList(String placeList) {
		this.placeList = placeList;
	}
	public Date getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(Date timeFrom) {
		this.timeFrom = timeFrom;
	}
	public Date getTimeTo() {
		return timeTo;
	}
	public void setTimeTo(Date timeTo) {
		this.timeTo = timeTo;
	}
	public double getLowTemp() {
		return lowTemp;
	}
	public void setLowTemp(double lowTemp) {
		this.lowTemp = lowTemp;
	}
	public double getHighTemp() {
		return highTemp;
	}
	public void setHighTemp(double highTemp) {
		this.highTemp = highTemp;
	}
	public double getLowHumi() {
		return lowHumi;
	}
	public void setLowHumi(double lowHumi) {
		this.lowHumi = lowHumi;
	}
	public double getHighHumi() {
		return highHumi;
	}
	public void setHighHumi(double highHumi) {
		this.highHumi = highHumi;
	}
	
}
