package com.htc.bean;

/**
 * @ SerialBarDataBean.java
 * 作用 : 即时数据 和 串口数据 需要的串口数据类
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class BeanForlBarData {
	
	/**
	 * 仪器主键ID
	 */
	private int equipmentId;
	/**
	 * 温度
	 */
	private String temp;
	/**
	 * 温度后缀 
	 */
	private String appt;
	/**
	 * 湿度
	 */
	private String humi;
	/**
	 * 湿度后缀
	 */
	private String apph;
	/**
	 * 电压
	 */
	private String power;
	/**
	 * 状态 - 数字
	 */
	private int stateInt;
	/**
	 * 状态 - 字符串
	 */
	private String stateStr;
	/**
	 * 要显示的颜色
	 */
	private String colorTemp;
	/**
	 * 要显示的颜色
	 */
	private String colorHumi;
	/**
	 * 仪器标签
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
	 * @param equipmentId 仪器主键ID
	 * @date:2009-11-4
	 */
	public void setEquipmentId(int equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getTemp() {
		return temp;
	}
	/**
	 * @param temp 温度
	 * @date:2009-11-4
	 */
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getHumi() {
		return humi;
	}
	/**
	 * @param humi 湿度
	 * @date:2009-11-4
	 */
	public void setHumi(String humi) {
		this.humi = humi;
	}
	public int getStateInt() {
		return stateInt;
	}
	/**
	 * @param stateInt 状态 - 数字
	 * @date:2009-11-4
	 */
	public void setStateInt(int stateInt) {
		this.stateInt = stateInt;
	}
	public String getStateStr() {
		return stateStr;
	}
	/**
	 * @param stateStr 状态 - 字符串
	 * @date:2009-11-4
	 */
	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
	public String getColorTemp() {
		return colorTemp;
	}
	/**
	 * @describe: 要显示的温度颜色	
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
	 * @describe: 要显示的湿度颜色
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
	 * @param label 仪器标签
	 * @date:2009-11-4
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	public String getPower() {
		return power;
	}
	/**
	 * @describe: 设置电量字符串	
	 * @param power: 电量字符串	
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
