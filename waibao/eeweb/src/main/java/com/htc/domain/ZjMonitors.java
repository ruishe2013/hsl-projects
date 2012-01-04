package com.htc.domain;

import java.io.Serializable;

/**
 * @ ZjMonitors.java
 * 作用 : 和浙江药监局比配的 仪器(探头)信息列表
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-4-13     YANGZHONLI       create
 */
public class ZjMonitors implements Serializable{

	private static final long serialVersionUID = 8096934233389199522L;
	
	int MonitorID;			// 可以对应仪器的主键ID
    String MonitorName;		// 仪器名
    int StoreID;			// 不知道什么含义,暂时 填写 LicenseNo一样的数据
    double T_High;			// 最高的限制温度
    double T_Low;			// 最低的限制温度
    double H_High;			// 最高的限制湿度
    double H_Low;			// 最低的限制湿度
    int NoID;				// 好像是要和Access中的 strDSRSN字段对应 -- 系统中strDSRSN和 药监局下发的编号 对应--->>>这里也填写 LicenseNo一样的数据
    String Sendcmd;			// 暂时不填
    int LicenseNo;			// 对应药监局下发的编号
    String ZgCode;			// 暂时不填
    
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
