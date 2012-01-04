package com.htc.domain;

import java.io.Serializable;

/**
 * @ ZjMonitors.java
 * ���� : ���㽭ҩ��ֱ���� ����(̽ͷ)��Ϣ�б�
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-4-13     YANGZHONLI       create
 */
public class ZjMonitors implements Serializable{

	private static final long serialVersionUID = 8096934233389199522L;
	
	int MonitorID;			// ���Զ�Ӧ����������ID
    String MonitorName;		// ������
    int StoreID;			// ��֪��ʲô����,��ʱ ��д LicenseNoһ��������
    double T_High;			// ��ߵ������¶�
    double T_Low;			// ��͵������¶�
    double H_High;			// ��ߵ�����ʪ��
    double H_Low;			// ��͵�����ʪ��
    int NoID;				// ������Ҫ��Access�е� strDSRSN�ֶζ�Ӧ -- ϵͳ��strDSRSN�� ҩ����·��ı�� ��Ӧ--->>>����Ҳ��д LicenseNoһ��������
    String Sendcmd;			// ��ʱ����
    int LicenseNo;			// ��Ӧҩ����·��ı��
    String ZgCode;			// ��ʱ����
    
	public int getMonitorID() {
		return MonitorID;
	}
	public void setMonitorID(int monitorID) {
		MonitorID = monitorID;
	}
	public String getMonitorName() {
		return MonitorName;
	}
	public void setMonitorName(String monitorName) {
		MonitorName = monitorName;
	}
	public int getStoreID() {
		return StoreID;
	}
	public void setStoreID(int storeID) {
		StoreID = storeID;
	}
	public double getT_High() {
		return T_High;
	}
	public void setT_High(double high) {
		T_High = high;
	}
	public double getT_Low() {
		return T_Low;
	}
	public void setT_Low(double low) {
		T_Low = low;
	}
	public double getH_High() {
		return H_High;
	}
	public void setH_High(double high) {
		H_High = high;
	}
	public double getH_Low() {
		return H_Low;
	}
	public void setH_Low(double low) {
		H_Low = low;
	}
	public int getNoID() {
		return NoID;
	}
	public void setNoID(int noID) {
		NoID = noID;
	}
	public String getSendcmd() {
		return Sendcmd;
	}
	public void setSendcmd(String sendcmd) {
		Sendcmd = sendcmd;
	}
	public int getLicenseNo() {
		return LicenseNo;
	}
	public void setLicenseNo(int licenseNo) {
		LicenseNo = licenseNo;
	}
	public String getZgCode() {
		return ZgCode;
	}
	public void setZgCode(String zgCode) {
		ZgCode = zgCode;
	}
    
}
