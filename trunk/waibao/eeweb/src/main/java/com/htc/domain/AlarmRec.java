package com.htc.domain;

import java.io.Serializable;

public class AlarmRec implements Serializable {
	
	private static final long serialVersionUID = -3420161907789123533L;
	/**
	 * ��������ģʽ:δ����ı���
	 */
	public static final int NO_ACTION_MODE = 0;
	/**
	 * ��������ģʽ:PC[ͨ�����Բ���]
	 */
	public static final int PC_MODE = 1;	
	/**
	 * ��������ģʽ:GPRS[ͨ���ֻ����Ų���]
	 */
	public static final int GPRS_MODE = 2;
	
	//TAlarmRec������
	private int alarmId;
	private String temperature;
	private String humidity;
	private long alarmStart;
	private long alarmEnd;
	private int alarmtype;		// ��������(�¶ȳ�����Χ 1:����������Χ 2:����������Χ) + (ʪ�ȳ�����Χ 10:����������Χ+20:����������Χ)
	private int state;			// ����״̬:1:�Ѹ�λ	2��δ��λ
	private int alarmmode;		// ����ģʽ[δ����:0 PC:1 GPRS:2]
	private int equipmentId;
	private String username;	// ����������
	private String placeName;	// �����������ͱ�ǩ. ��:������-1��
	private String normalArea;	// ����������Χ. ��:1.0~10.0��/0.0~100.0%RH
	private int equitype;		// TequipData��equitype // 1,'��ʪ��';2,'���¶�';3,'��ʪ��'
	private int gprsFlag;		// �Ƿ��Ѿ���������(0:���÷� 1:��Ҫ����,����û�з�  2:�Ѿ�����)
	
	//where �����ֶ�
	private String placeList;  	  		// �����ڰ��ص��ѯʱ���ص��б�(�����Ÿ���)
	private String ascOrDesc;	  		// asc:����ʱ������	desc��ʱ�併��
	private int whichToSearch;	  		//(����ʲô����)1����ʱ�� 	2��λʱ��
	private int pageSize;				// �ܷ��ص�����¼�� 
	private String alarmtypeStr;   		// ��������(�ö��Ÿ���)
	
	
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
