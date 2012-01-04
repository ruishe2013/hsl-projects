package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForSms.java
 * ���� : ��"�Ե�ǰ�ı�������,Ӧ��ִ�����ֲ���: ��������޸�"��������ṩ�ķ�������
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-3     YANGZHONLI       create
 */
public class BeanForAlarmRs implements Serializable{
	
	/**
	 * ��ѯ�������0:��ѯ�����д���,��ִ���κβ���(Ĭ��ֵ)<br>
	 */
	public static int NUDO_ALARM = 0;
	/**
	 * ��ѯ�������1:ִ�в���<br>
	 */
	public static int ADD_ALARM = 1;
	/**
	 * ��ѯ�������2:ִ���޸�<br>
	 */
	public static int UPDATE_ALARM = 2;

	private static final long serialVersionUID = 8860185602365058408L;
	/**
	 * ������¼����
	 */
	private int alarmId;
	/**
	 * ��ѯ�������
	 * 0:��ѯ�����д���,��ִ���κβ���(Ĭ��ֵ)<br>
	 * 1:ִ�в���<br>
	 * 2:ִ���޸�<br>
	 */
	private int rsType = 0;
	
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	/**
	 * ��ѯ�������
	 * 0:��ѯ�����д���,��ִ���κβ���(Ĭ��ֵ)<br>
	 * 1:ִ�в���<br>
	 * 2:ִ���޸�<br>
	 */
	public int getRsType() {
		return rsType;
	}
	/**
	 * ��ѯ�������
	 * 0:��ѯ�����д���,��ִ���κβ���(Ĭ��ֵ)<br>
	 * 1:ִ�в���<br>
	 * 2:ִ���޸�<br>
	 */	
	public void setRsType(int rsType) {
		this.rsType = rsType;
	}
	
}
