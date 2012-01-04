package com.htc.bean;

import java.io.Serializable;

import javax.xml.crypto.Data;

/**
 * @ SerialSetDataBean.java
 * 作用 : 设置硬件属性的数据结构. 如: 设置 地址,机号,电量,校验时间时用到 
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class BeanForSetData implements Serializable  {
	
	private static final long serialVersionUID = 5561154310740134937L;
	private String address; 				//地址
	private String MCType = "0000"; 		//机号-类型号
	private String MCOrder = "0000"; 		//机号-批次
	private String MCNo = "0000"; 			//机号-流水号
	private String powerVal;				//电量
	private Data time; 						//校验时间
	private String version;					//版本号
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMCType() {
		return MCType;
	}
	public void setMCType(String type) {
		MCType = type;
	}
	public String getMCOrder() {
		return MCOrder;
	}
	public void setMCOrder(String order) {
		MCOrder = order;
	}
	public String getMCNo() {
		return MCNo;
	}
	public void setMCNo(String no) {
		MCNo = no;
	}
	public String getPowerVal() {
		return powerVal;
	}
	public void setPowerVal(String powerVal) {
		this.powerVal = powerVal;
	}
	public Data getTime() {
		return time;
	}
	public void setTime(Data time) {
		this.time = time;
	}
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String toString() {
		StringBuffer strbuf = new StringBuffer();
		strbuf.append("address:" + address);
		strbuf.append(" MCType:" + MCType);
		strbuf.append(" MCOrder:" + MCOrder);
		strbuf.append(" MCNo:" + MCNo);
		strbuf.append(" powerVal:" + powerVal);
		strbuf.append(" time:" + time);
		strbuf.append(" address:" + address);
		strbuf.append(" version:" + version);
		return strbuf.toString();
	}
}
