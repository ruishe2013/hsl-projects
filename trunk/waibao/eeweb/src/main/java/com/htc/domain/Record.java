package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {
	
	private static final long serialVersionUID = 6193402910672107666L;
	
	//TRecord表列名
	private String temperature;
	private String humidity;
	private Date recTime;	//记录时间或者首次报警时间
	private long recLong;	//把记录时间转换成毫秒数Long类型
	private Date recTime_new;//最新的报警时间
	private int equipmentId;
	
	//where 条件字段
	private String placeList;  	  		//当存在按地点查询时，地点列表（按逗号隔开）
	private	Date alarmStartFrom;		//alarmStart开始
	private	Date alarmStartTo;			//alarmStart结束
	
//	//alarmtype:报警类型(温度超出范围  1:低于正常范围	 2:高于正常范围) + (湿度超出范围  10:低于正常范围	 20:高于正常范围)
//	private int alarmtype;				//
//	private String areaAddLabel;		//equipmentId对应的：区域-标注-地址
	private int equitype; 				//1,'温湿度';2,'单温度';3,'单湿度'
	
	// 向access 传送数据时用到的变量
	private String dsrsn;				// 仪器编号,如:TH000011112222
	private String label;				// 仪器标签,如:测试区-1号
	private int showAccess;				// 是否显示到access((0: 不显示    1: 显示))	
	
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public Date getRecTime() {
		return recTime;
	}
	public void setRecTime(Date recTime) {
		this.recTime = recTime;
	}
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getPlaceList() {
		return placeList;
	}
	public void setPlaceList(String placeList) {
		this.placeList = placeList;
	}
	public Date getAlarmStartFrom() {
		return alarmStartFrom;
	}
	public void setAlarmStartFrom(Date alarmStartFrom) {
		this.alarmStartFrom = alarmStartFrom;
	}
	public Date getAlarmStartTo() {
		return alarmStartTo;
	}
	public void setAlarmStartTo(Date alarmStartTo) {
		this.alarmStartTo = alarmStartTo;
	}
//	public String getAreaAddLabel() {
//		return areaAddLabel;
//	}
//	public void setAreaAddLabel(String areaAddLabel) {
//		this.areaAddLabel = areaAddLabel;
//	}
	public int getEquitype() {
		return equitype;
	}
	public void setEquitype(int equitype) {
		this.equitype = equitype;
	}
//	public int getAlarmtype() {
//		return alarmtype;
//	}
//	public void setAlarmtype(int alarmtype) {
//		this.alarmtype = alarmtype;
//	}
	public Date getRecTime_new() {
		return recTime_new;
	}
	public void setRecTime_new(Date recTime_new) {
		this.recTime_new = recTime_new;
	}
	public long getRecLong() {
		return recLong;
	}
	public void setRecLong(long recLong) {
		this.recLong = recLong;
	}
	public String getDsrsn() {
		return dsrsn;
	}
	public void setDsrsn(String dsrsn) {
		this.dsrsn = dsrsn;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @describe:	是否显示到access((0: 不显示    1: 显示))
	 * @date:2010-3-25
	 */
	public int getShowAccess() {
		return showAccess;
	}
	/**
	 * @describe:	是否显示到access((0: 不显示    1: 显示))
	 * @date:2010-3-25
	 */
	public void setShowAccess(int showAccess) {
		this.showAccess = showAccess;
	}
	
}
