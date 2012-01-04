package com.htc.bean;

import java.io.Serializable;
import java.util.Date;

public class BeanForZjHisRec implements Serializable {
	
	private static final long serialVersionUID = 3831813936092566412L;
	
	// 搜索条件字段 - 都是必须的
	private String placeList;	// 设置仪器主键列表（按逗号隔开）
	private	Date timeFrom;		// 开始时间
	private	Date timeTo;		// 结束时间
	private double lowTemp;		// lowTemp和highTemp形成查询温度范围
	private double highTemp;
	private double lowHumi;		// lowHumi和highHumi形成查询湿度范围
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
