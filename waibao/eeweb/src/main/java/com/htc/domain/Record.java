package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {
	
	private static final long serialVersionUID = 6193402910672107666L;
	
	//TRecord������
	private String temperature;
	private String humidity;
	private Date recTime;	//��¼ʱ������״α���ʱ��
	private long recLong;	//�Ѽ�¼ʱ��ת���ɺ�����Long����
	private Date recTime_new;//���µı���ʱ��
	private int equipmentId;
	
	//where �����ֶ�
	private String placeList;  	  		//�����ڰ��ص��ѯʱ���ص��б������Ÿ�����
	private	Date alarmStartFrom;		//alarmStart��ʼ
	private	Date alarmStartTo;			//alarmStart����
	
//	//alarmtype:��������(�¶ȳ�����Χ  1:����������Χ	 2:����������Χ) + (ʪ�ȳ�����Χ  10:����������Χ	 20:����������Χ)
//	private int alarmtype;				//
//	private String areaAddLabel;		//equipmentId��Ӧ�ģ�����-��ע-��ַ
	private int equitype; 				//1,'��ʪ��';2,'���¶�';3,'��ʪ��'
	
	// ��access ��������ʱ�õ��ı���
	private String dsrsn;				// �������,��:TH000011112222
	private String label;				// ������ǩ,��:������-1��
	private int showAccess;				// �Ƿ���ʾ��access((0: ����ʾ    1: ��ʾ))	
	
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
	 * @describe:	�Ƿ���ʾ��access((0: ����ʾ    1: ��ʾ))
	 * @date:2010-3-25
	 */
	public int getShowAccess() {
		return showAccess;
	}
	/**
	 * @describe:	�Ƿ���ʾ��access((0: ����ʾ    1: ��ʾ))
	 * @date:2010-3-25
	 */
	public void setShowAccess(int showAccess) {
		this.showAccess = showAccess;
	}
	
}
