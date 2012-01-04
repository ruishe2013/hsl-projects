package com.htc.domain;

import java.io.Serializable;

/**
 * @ Data4Access.java
 * ���� : ҩ��������Ӧaccess���ֶ�
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-17     YANGZHONLI       create
 */
public class Data4Access implements Serializable {
	
	private static final long serialVersionUID = 3764984527824523564L;
	
	//���ݿ���б��Ա
	private String strDSRSN;			// ��¼�Ǳ��
	private String strDSRName;			// ��¼������
	private String strDateTime;			// ��¼ʱ��
	private String strTemp;				// �¶�
	private String strHUM;				// ʪ��
	private String strAirPress;			// ѹǿ - ����
	private String strEquipmentType;	// ��������.����ʪ��:TH
	
	
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
