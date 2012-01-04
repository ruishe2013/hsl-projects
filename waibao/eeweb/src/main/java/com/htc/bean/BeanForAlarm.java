package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForSms.java
 * ���� : Ϊ��ѯ���µı�������,����ѯ������Bean�á�(...where	alarmStart = #alarmStart# and equipmentId = #equipmentId# and state = 2)
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-3     YANGZHONLI       create
 */
public class BeanForAlarm implements Serializable{

	private static final long serialVersionUID = 6285102188907394309L;
	/**
	 * ���������б�,�ö��Ÿ���
	 */
	private int equipmentId;
	/**
	 * ��¼ʱ��,������
	 */
	private long alarmStart;
	
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public long getAlarmStart() {
		return alarmStart;
	}
	public void setAlarmStart(long alarmStart) {
		this.alarmStart = alarmStart;
	}
	
}
