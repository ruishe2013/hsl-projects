package com.htc.domain;

import java.io.Serializable;

public class EquipData implements Serializable {

	private static final long serialVersionUID = -7532072204874925730L;
	
	public static int TYPE_TEMP_HUMI = 1;
	public static int TYPE_TEMP_ONLY = 2;
	public static int TYPE_HUMI_ONLY = 3;
	
	private int equipmentId;
	private int address;
	private int equitype; 		// 1,'温湿度';2,'单温度';3,'单湿度'
	private String mark;
	private String remark;
	private	float tempUp ; 
	private	float tempDown ;
	private	float tempDev;
	private	float humiUp ;
	private	float humiDown ;
	private	float humiDev;
	private	int equiorder;
	private	int placeId;
	private int showPower;		// 是否显示电量信息 (0: 不显示    1: 显示)
	private int powerType;		// 电量类型[1:1.5v,2:3.6v]
	private int useless = 1;	// 1:表示这个用户正在使用 0:表示这个账户已经被删掉了
	private String dsrsn;		// 产品编号: 如TH001122334455
	
	private String typeStr;
	private String placeStr;
	
	private int showAccess;				// 是否显示到access((0: 不显示    1: 显示))
	private int conndata;				// 串口通信的时候,获取的字节数,默认是3个(温度,湿度,露点),4个的(温度,湿度,露点,电压)
	
	
	public EquipData(){}
	
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
	 * @describe:	1,'温湿度';2,'单温度';3,'单湿度'
	 * @return:
	 * @date:2009-11-17
	 */
	public int getEquitype() {
		return equitype;
	}
	/**
	 * @describe:	1,'温湿度';2,'单温度';3,'单湿度'
	 * @param equitype:
	 * @date:2009-11-17
	 */
	public void setEquitype(int equitype) {
		this.equitype = equitype;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public float getTempUp() {
		return tempUp;
	}
	public void setTempUp(float tempUp) {
		this.tempUp = tempUp;
	}
	public float getTempDown() {
		return tempDown;
	}
	public void setTempDown(float tempDown) {
		this.tempDown = tempDown;
	}
	public float getTempDev() {
		return tempDev;
	}
	public void setTempDev(float tempDev) {
		this.tempDev = tempDev;
	}
	public float getHumiUp() {
		return humiUp;
	}
	public void setHumiUp(float humiUp) {
		this.humiUp = humiUp;
	}
	public float getHumiDown() {
		return humiDown;
	}
	public void setHumiDown(float humiDown) {
		this.humiDown = humiDown;
	}
	public float getHumiDev() {
		return humiDev;
	}
	public void setHumiDev(float humiDev) {
		this.humiDev = humiDev;
	}
	public int getEquiorder() {
		return equiorder;
	}
	public void setEquiorder(int equiorder) {
		this.equiorder = equiorder;
	}
	public int getPlaceId() {
		return placeId;
	}
	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
	/**
	 * @describe: 是否显示电量信息 (0: 不显示    1: 显示)
	 * @date:2009-11-17
	 */
	public int getShowPower() {
		return showPower;
	}
	/**
	 * @describe:	是否显示电量信息 (0: 不显示    1: 显示)
	 * @param showPower:
	 * @date:2009-11-17
	 */
	public void setShowPower(int showPower) {
		this.showPower = showPower;
	}
	public String getTypeStr() {
		return typeStr;
	}
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}
	public String getPlaceStr() {
		return placeStr;
	}
	public void setPlaceStr(String placeStr) {
		this.placeStr = placeStr;
	}
	public int getPowerType() {
		return powerType;
	}
	public void setPowerType(int powerType) {
		this.powerType = powerType;
	}

	public int getUseless() {
		return useless;
	}

	public void setUseless(int useless) {
		this.useless = useless;
	}

	public String getDsrsn() {
		return dsrsn;
	}

	public void setDsrsn(String dsrsn) {
		this.dsrsn = dsrsn;
	}

	/**
	 * @describe: 是否显示到access((0: 不显示    1: 显示))
	 * @date:2010-3-25
	 */
	public int getShowAccess() {
		return showAccess;
	}

	public void setShowAccess(int showAccess) {
		this.showAccess = showAccess;
	}

	public int getConndata() {
		return conndata;
	}

	public void setConndata(int conndata) {
		this.conndata = conndata;
	}
	
}
