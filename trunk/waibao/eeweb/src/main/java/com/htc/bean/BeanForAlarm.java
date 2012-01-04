package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForSms.java
 * 作用 : 为查询最新的报警数据,做查询条件的Bean用。(...where	alarmStart = #alarmStart# and equipmentId = #equipmentId# and state = 2)
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-3     YANGZHONLI       create
 */
public class BeanForAlarm implements Serializable{

	private static final long serialVersionUID = 6285102188907394309L;
	/**
	 * 仪器主键列表,用逗号隔开
	 */
	private int equipmentId;
	/**
	 * 记录时间,毫秒数
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
