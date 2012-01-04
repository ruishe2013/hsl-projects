package com.htc.domain;

import java.io.Serializable;

public class GprsSet implements Serializable {
	
	private static final long serialVersionUID = -3791622057406951918L;
	
	private int gprsSetId;
	private int numId;
	private String alias;
	private String mesFormat;
	private String remark;
	
	public GprsSet(){}
	
	public int getGprsSetId() {
		return gprsSetId;
	}
	public void setGprsSetId(int gprsSetId) {
		this.gprsSetId = gprsSetId;
	}
	public int getNumId() {
		return numId;
	}
	public void setNumId(int numId) {
		this.numId = numId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getMesFormat() {
		return mesFormat;
	}
	public void setMesFormat(String mesFormat) {
		this.mesFormat = mesFormat;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
