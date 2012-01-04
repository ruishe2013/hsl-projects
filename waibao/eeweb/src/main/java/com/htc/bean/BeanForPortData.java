package com.htc.bean;

import java.util.Date;

/**
 * @ SerialPortDataBean.java
 * ���� : �Ӵ��ڽ�������,��ʹ�õ����ݽṹ.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class BeanForPortData {
	
	private int recId;
	private int equipmentId;
	private int address; 		// ��ַ
	private float temp; 			// �¶�
	private float humi;			// ʪ��
	private float dewPoint;		// ¶��
	private float powerV;			// ��ѹ
	/**
	 * ����û������ :0
	 * ��ȷ���� : Level_Final_Serial.Serial_right_Frame = 1
	 * ������� : Level_Final_Serial.Serial_right_Frame > 1
	 */
	private int	state;				// ״̬
	private Date recTime;			// ��¼ʱ��ʱ��
	private long recLong;			//�Ѽ�¼ʱ��ת���ɺ�����Long����
	private int mark;				// 0������,1������
	
	private long oldrecLong;		// ��ǰ��ļ�¼ʱ��
	
	
	public int getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	/**
	 * @describe: ������������״̬
	 * ����û������ :0
	 * ��ȷ���� : Level_Final_Serial.Serial_right_Frame = 1
	 * ������� : Level_Final_Serial.Serial_right_Frame > 1
	 * @date:2009-11-22
	 */
	public int getState() {
		return state;
	}
	/**
	 * @describe: ������������״̬
	 * ����û������ :0
	 * ��ȷ���� : Level_Final_Serial.Serial_right_Frame = 1
	 * ������� : Level_Final_Serial.Serial_right_Frame > 1
	 * @date:2009-11-22
	 */
	public void setState(int state) {
		this.state = state;
	}
	public Date getRecTime() {
		return recTime;
	}
	public void setRecTime(Date recTime) {
		this.recTime = recTime;
	}
	public long getRecLong() {
		return recLong;
	}
	public void setRecLong(long recLong) {
		this.recLong = recLong;
	}
	/**
	 * @describe:	// 0������,1������
	 * @date:2010-3-22
	 */
	public int getMark() {
		return mark;
	}
	/**
	 * @describe: // 0������,1������	
	 * @date:2010-3-22
	 */
	public void setMark(int mark) {
		this.mark = mark;
	}
	public int getRecId() {
		return recId;
	}
	public void setRecId(int recId) {
		this.recId = recId;
	}
	public long getOldrecLong() {
		return oldrecLong;
	}
	public void setOldrecLong(long oldrecLong) {
		this.oldrecLong = oldrecLong;
	}
	public float getTemp() {
		return temp;
	}
	public void setTemp(float temp) {
		this.temp = temp;
	}
	public float getHumi() {
		return humi;
	}
	public void setHumi(float humi) {
		this.humi = humi;
	}
	public float getDewPoint() {
		return dewPoint;
	}
	public void setDewPoint(float dewPoint) {
		this.dewPoint = dewPoint;
	}
	public float getPowerV() {
		return powerV;
	}
	public void setPowerV(float powerV) {
		this.powerV = powerV;
	}
	
}
