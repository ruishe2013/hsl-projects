package com.htc.domain;

import java.io.Serializable;

public class AlarmRec implements Serializable {
	
	private static final long serialVersionUID = -3420161907789123533L;
	/**
	 * 报警处理模式:未处理的报警
	 */
	public static final int NO_ACTION_MODE = 0;
	/**
	 * 报警处理模式:PC[通过电脑操作]
	 */
	public static final int PC_MODE = 1;	
	/**
	 * 报警处理模式:GPRS[通过手机短信操作]
	 */
	public static final int GPRS_MODE = 2;
	
	//TAlarmRec表列名
	private int alarmId;
	private String temperature;
	private String humidity;
	private long alarmStart;
	private long alarmEnd;
	private int alarmtype;		// 报警类型(温度超出范围 1:低于正常范围 2:高于正常范围) + (湿度超出范围 10:低于正常范围+20:高于正常范围)
	private int state;			// 处理状态:1:已复位	2：未复位
	private int alarmmode;		// 处理模式[未处理:0 PC:1 GPRS:2]
	private int equipmentId;
	private String username;	// 处理人名字
	private String placeName;	// 包含域名名和标签. 如:测试区-1号
	private String normalArea;	// 仪器正常范围. 如:1.0~10.0℃/0.0~100.0%RH
	private int equitype;		// TequipData表：equitype // 1,'温湿度';2,'单温度';3,'单湿度'
	private int gprsFlag;		// 是否已经发过短信(0:不用发 1:需要发送,但还没有发  2:已经发送)
	
	//where 条件字段
	private String placeList;  	  		// 当存在按地点查询时，地点列表(按逗号隔开)
	private String ascOrDesc;	  		// asc:代表按时间升序	desc按时间降序
	private int whichToSearch;	  		//(根据什么搜索)1报警时间 	2复位时间
	private int pageSize;				// 能返回的最大记录数 
	private String alarmtypeStr;   		// 报警类型(用逗号隔开)
	
	
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
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
	public long getAlarmStart() {
		return alarmStart;
	}
	public void setAlarmStart(long alarmStart) {
		this.alarmStart = alarmStart;
	}
	public long getAlarmEnd() {
		return alarmEnd;
	}
	public void setAlarmEnd(long alarmEnd) {
		this.alarmEnd = alarmEnd;
	}
	public int getAlarmtype() {
		return alarmtype;
	}
	public void setAlarmtype(int alarmtype) {
		this.alarmtype = alarmtype;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getAlarmmode() {
		return alarmmode;
	}
	public void setAlarmmode(int alarmmode) {
		this.alarmmode = alarmmode;
	}
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getNormalArea() {
		return normalArea;
	}
	public void setNormalArea(String normalArea) {
		this.normalArea = normalArea;
	}
	public int getEquitype() {
		return equitype;
	}
	public void setEquitype(int equitype) {
		this.equitype = equitype;
	}
	public String getPlaceList() {
		return placeList;
	}
	public void setPlaceList(String placeList) {
		this.placeList = placeList;
	}
	public String getAscOrDesc() {
		return ascOrDesc;
	}
	public void setAscOrDesc(String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}
	public int getWhichToSearch() {
		return whichToSearch;
	}
	public void setWhichToSearch(int whichToSearch) {
		this.whichToSearch = whichToSearch;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getAlarmtypeStr() {
		return alarmtypeStr;
	}
	public void setAlarmtypeStr(String alarmtypeStr) {
		this.alarmtypeStr = alarmtypeStr;
	}
	public int getGprsFlag() {
		return gprsFlag;
	}
	public void setGprsFlag(int gprsFlag) {
		this.gprsFlag = gprsFlag;
	}
}
