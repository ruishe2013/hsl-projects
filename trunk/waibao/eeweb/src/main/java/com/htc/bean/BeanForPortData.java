package com.htc.bean;

import java.util.Date;

/**
 * @ SerialPortDataBean.java
 * 作用 : 从串口接收数据,所使用的数据结构.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class BeanForPortData {
	
	private int recId;
	private int equipmentId;
	private int address; 		// 地址
	private float temp; 			// 温度
	private float humi;			// 湿度
	private float dewPoint;		// 露点
	private float powerV;			// 电压
	/**
	 * 仪器没有连接 :0
	 * 正确的侦 : Level_Final_Serial.Serial_right_Frame = 1
	 * 错误的侦 : Level_Final_Serial.Serial_right_Frame > 1
	 */
	private int	state;				// 状态
	private Date recTime;			// 记录时的时间
	private long recLong;			//把记录时间转换成毫秒数Long类型
	private int mark;				// 0无数据,1有数据
	
	private long oldrecLong;		// 最前面的记录时间
	
	
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
	 * @describe: 返回仪器运行状态
	 * 仪器没有连接 :0
	 * 正确的侦 : Level_Final_Serial.Serial_right_Frame = 1
	 * 错误的侦 : Level_Final_Serial.Serial_right_Frame > 1
	 * @date:2009-11-22
	 */
	public int getState() {
		return state;
	}
	/**
	 * @describe: 设置仪器运行状态
	 * 仪器没有连接 :0
	 * 正确的侦 : Level_Final_Serial.Serial_right_Frame = 1
	 * 错误的侦 : Level_Final_Serial.Serial_right_Frame > 1
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
	 * @describe:	// 0无数据,1有数据
	 * @date:2010-3-22
	 */
	public int getMark() {
		return mark;
	}
	/**
	 * @describe: // 0无数据,1有数据	
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
