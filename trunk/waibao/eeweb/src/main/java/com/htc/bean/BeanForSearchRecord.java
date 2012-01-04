package com.htc.bean;

import java.io.Serializable;
import java.util.Date;

public class BeanForSearchRecord implements Serializable {
	
	private static final long serialVersionUID = 7952035463339056486L;
	
	// ���������ֶ�
	private String placeList;  	  		// �������������б������Ÿ�����
	private	Date alarmStartFrom;		// ��ʼʱ��
	private	Date alarmStartTo;			// ����ʱ��
	private int stattype;				// 0:��ʱ����  1:�����ձ�����	 2:�����±�����
	private int orderByType;			// ��������: 1: order by recTime, equipmentId  2: order by equipmentId, recTime				
	
	public String getPlaceList() {
		return placeList;
	}
	/**
	 * @describe: ������Ҫ������ ���������б�(�����Ÿ���)
	 * @param placeList: ���Ÿ������ַ���
	 * @date:2009-11-3
	 */
	public void setPlaceList(String placeList) {
		this.placeList = placeList;
	}
	public Date getAlarmStartFrom() {
		return alarmStartFrom;
	}
	/**
	 * @describe: ������Ҫ������ ��ʼʱ��
	 * @param alarmStartFrom: ��ʼʱ��
	 * @date:2009-11-3
	 */
	public void setAlarmStartFrom(Date alarmStartFrom) {
		this.alarmStartFrom = alarmStartFrom;
	}
	public Date getAlarmStartTo() {
		return alarmStartTo;
	}
	/**
	 * @describe : ������Ҫ������ ����ʱ��
	 * @param alarmStartTo:����ʱ��
	 * @date:2009-11-3
	 */
	public void setAlarmStartTo(Date alarmStartTo) {
		this.alarmStartTo = alarmStartTo;
	}
	/**
	 * @describe: ���� ������������: 0: ��ʱ���� 1:�����ձ�����	 2:�����±����� 		
	 * @date:2009-11-3
	 */
	public int getStattype() {
		return stattype;
	}
	/**
	 * @describe : ����������������: 0: ��ʱ���� 1:�����ձ�����	 2:�����±����� 	
	 * @param stattype:  0: ��ʱ���� 1:�����ձ�����	 2:�����±�����
	 * @date:2009-11-3
	 */
	public void setStattype(int stattype) {
		this.stattype = stattype;
	}
	public int getOrderByType() {
		return orderByType;
	}
	/**
	 * @describe: ��������: ��ʾΪ���ʱѡ�� 1; ��ʾΪ����ʱѡ�� 2
	 * @param orderType: 1: order by recTime, equipmentId  2: order by equipmentId, recTime
	 * @date:2009-11-3
	 */
	public void setOrderByType(int orderByType) {
		this.orderByType = orderByType;
	}
	
}
