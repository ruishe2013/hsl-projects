package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

public class HisRecord implements Serializable {
	
	private static final long serialVersionUID = 2025124905499520160L;
	
	//THisRecord������
	private int recId;
	private int equipmentId;
	private Date recTime;
	private String tempavg;
	private String tempmax;
	private String tempmin;
	private String humiavg;
	private String humimax;
	private String humimin;
	private int stattype;	//1:�����ձ�����	 2:�����±�����
	private String recTimeStr;
	
	//where �����ֶ�
	private String placeList;  	  		//�����ڰ��ص��ѯʱ���ص��б������Ÿ�����
	private	Date alarmStartFrom;		//alarmStart��ʼ
	private	Date alarmStartTo;			//alarmStart����
	
	public int getRecId() {
		return recId;
	}
	public void setRecId(int recId) {
		this.recId = recId;
	}
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public Date getRecTime() {
		return recTime;
	}
	public void setRecTime(Date recTime) {
		this.recTime = recTime;
	}
	public String getTempavg() {
		return tempavg;
	}
	public void setTempavg(String tempavg) {
		this.tempavg = tempavg;
	}
	public String getTempmax() {
		return tempmax;
	}
	public void setTempmax(String tempmax) {
		this.tempmax = tempmax;
	}
	public String getTempmin() {
		return tempmin;
	}
	public void setTempmin(String tempmin) {
		this.tempmin = tempmin;
	}
	public String getHumiavg() {
		return humiavg;
	}
	public void setHumiavg(String humiavg) {
		this.humiavg = humiavg;
	}
	public String getHumimax() {
		return humimax;
	}
	public void setHumimax(String humimax) {
		this.humimax = humimax;
	}
	public String getHumimin() {
		return humimin;
	}
	public void setHumimin(String humimin) {
		this.humimin = humimin;
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
	public int getStattype() {
		return stattype;
	}
	public void setStattype(int stattype) {
		this.stattype = stattype;
	}
	public String getRecTimeStr() {
		return recTimeStr;
	}
	public void setRecTimeStr(String recTimeStr) {
		this.recTimeStr = recTimeStr;
	}
	
}
