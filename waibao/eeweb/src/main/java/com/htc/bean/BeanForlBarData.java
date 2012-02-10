package com.htc.bean;

/**
 * @ SerialBarDataBean.java
 * ���� : ��ʱ���� �� �������� ��Ҫ�Ĵ���������
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class BeanForlBarData {
	
	/**
	 * ��������ID
	 */
	private int equipmentId;
	/**
	 * �¶�
	 */
	private String temp;
	/**
	 * �¶Ⱥ�׺ 
	 */
	private String appt;
	/**
	 * ʪ��
	 */
	private String humi;
	/**
	 * ʪ�Ⱥ�׺
	 */
	private String apph;
	/**
	 * ��ѹ
	 */
	private String power;
	/**
	 * ״̬ - ����
	 */
	private int stateInt;
	/**
	 * ״̬ - �ַ���
	 */
	private String stateStr;
	/**
	 * Ҫ��ʾ����ɫ
	 */
	private String colorTemp;
	/**
	 * Ҫ��ʾ����ɫ
	 */
	private String colorHumi;
	/**
	 * ������ǩ
	 */
	private String label; 
	
	private int address;
	
	@Override
	public String toString() {
		return "BeanForlBarData [address=" + address + ", equipmentId=" + equipmentId + "]";
	}
	public int getAddress() {
		return address;
	}
	public void setAddress(int address) {
		this.address = address;
	}
	public int getEquipmentId() {
		return equipmentId;
	}
	/**
	 * @param equipmentId ��������ID
	 * @date:2009-11-4
	 */
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getTemp() {
		return temp;
	}
	/**
	 * @param temp �¶�
	 * @date:2009-11-4
	 */
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getHumi() {
		return humi;
	}
	/**
	 * @param humi ʪ��
	 * @date:2009-11-4
	 */
	public void setHumi(String humi) {
		this.humi = humi;
	}
	public int getStateInt() {
		return stateInt;
	}
	/**
	 * @param stateInt ״̬ - ����
	 * @date:2009-11-4
	 */
	public void setStateInt(int stateInt) {
		this.stateInt = stateInt;
	}
	public String getStateStr() {
		return stateStr;
	}
	/**
	 * @param stateStr ״̬ - �ַ���
	 * @date:2009-11-4
	 */
	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
	public String getColorTemp() {
		return colorTemp;
	}
	/**
	 * @describe: Ҫ��ʾ���¶���ɫ	
	 * @param colorTemp:
	 * @date:2009-12-1
	 */
	public void setColorTemp(String colorTemp) {
		this.colorTemp = colorTemp;
	}
	public String getColorHumi() {
		return colorHumi;
	}
	/**
	 * @describe: Ҫ��ʾ��ʪ����ɫ
	 * @param colorHumi:
	 * @date:2009-12-1
	 */
	public void setColorHumi(String colorHumi) {
		this.colorHumi = colorHumi;
	}
	public String getLabel() {
		return label;
	}
	/**
	 * @param label ������ǩ
	 * @date:2009-11-4
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	public String getPower() {
		return power;
	}
	/**
	 * @describe: ���õ����ַ���	
	 * @param power: �����ַ���	
	 * @date:2009-11-14
	 */
	public void setPower(String power) {
		this.power = power;
	}
	public String getAppt() {
		return appt;
	}
	public void setAppt(String appt) {
		this.appt = appt;
	}
	public String getApph() {
		return apph;
	}
	public void setApph(String apph) {
		this.apph = apph;
	}
}
