package com.htc.domain;

import java.io.Serializable;

public class EquipData implements Serializable {

	private static final long serialVersionUID = -7532072204874925730L;
	
	public static int TYPE_TEMP_HUMI = 1;
	public static int TYPE_TEMP_ONLY = 2;
	public static int TYPE_HUMI_ONLY = 3;
	
	private int equipmentId;
	private int address;
	private int equitype; 		// 1,'��ʪ��';2,'���¶�';3,'��ʪ��'
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
	private int showPower;		// �Ƿ���ʾ������Ϣ (0: ����ʾ    1: ��ʾ)
	private int powerType;		// ��������[1:1.5v,2:3.6v]
	private int useless = 1;	// 1:��ʾ����û�����ʹ�� 0:��ʾ����˻��Ѿ���ɾ����
	private String dsrsn;		// ��Ʒ���: ��TH001122334455
	
	private String typeStr;
	private String placeStr;
	
	private int showAccess;				// �Ƿ���ʾ��access((0: ����ʾ    1: ��ʾ))
	private int conndata;				// ����ͨ�ŵ�ʱ��,��ȡ���ֽ���,Ĭ����3��(�¶�,ʪ��,¶��),4����(�¶�,ʪ��,¶��,��ѹ)
	
	
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
	 * @describe:	1,'��ʪ��';2,'���¶�';3,'��ʪ��'
	 * @return:
	 * @date:2009-11-17
	 */
	public int getEquitype() {
		return equitype;
	}
	/**
	 * @describe:	1,'��ʪ��';2,'���¶�';3,'��ʪ��'
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
	 * @describe: �Ƿ���ʾ������Ϣ (0: ����ʾ    1: ��ʾ)
	 * @date:2009-11-17
	 */
	public int getShowPower() {
		return showPower;
	}
	/**
	 * @describe:	�Ƿ���ʾ������Ϣ (0: ����ʾ    1: ��ʾ)
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
	 * @describe: �Ƿ���ʾ��access((0: ����ʾ    1: ��ʾ))
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
