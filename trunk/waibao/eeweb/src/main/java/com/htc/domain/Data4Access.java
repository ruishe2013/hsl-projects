package com.htc.domain;

import java.io.Serializable;

/**
 * @ Data4Access.java
 * 作用 : 药检局软件对应access的字段
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-17     YANGZHONLI       create
 */
public class Data4Access implements Serializable {
	
	private static final long serialVersionUID = 3764984527824523564L;
	
	//数据库表列表成员
	private String strDSRSN;			// 记录仪编号
	private String strDSRName;			// 记录仪名称
	private String strDateTime;			// 记录时间
	private String strTemp;				// 温度
	private String strHUM;				// 湿度
	private String strAirPress;			// 压强 - 暂无
	private String strEquipmentType;	// 仪器类型.如温湿度:TH
	
	
	public String getStrDSRSN() {
		return strDSRSN;
	}
	public void setStrDSRSN(String strDSRSN) {
		this.strDSRSN = strDSRSN;
	}
	public String getStrDSRName() {
		return strDSRName;
	}
	public void setStrDSRName(String strDSRName) {
		this.strDSRName = strDSRName;
	}
	public String getStrDateTime() {
		return strDateTime;
	}
	public void setStrDateTime(String strDateTime) {
		this.strDateTime = strDateTime;
	}
	public String getStrTemp() {
		return strTemp;
	}
	public void setStrTemp(String strTemp) {
		this.strTemp = strTemp;
	}
	public String getStrHUM() {
		return strHUM;
	}
	public void setStrHUM(String strHUM) {
		this.strHUM = strHUM;
	}
	public String getStrAirPress() {
		return strAirPress;
	}
	public void setStrAirPress(String strAirPress) {
		this.strAirPress = strAirPress;
	}
	public String getStrEquipmentType() {
		return strEquipmentType;
	}
	public void setStrEquipmentType(String strEquipmentType) {
		this.strEquipmentType = strEquipmentType;
	}
	
}
