package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

public class BackUpList implements Serializable {
	
	private static final long serialVersionUID = -877961060952163943L;

	private int backId;
	private String fileName;
	private Date backtime;
	private String remark;
	
	public int getBackId() {
		return backId;
	}
	public void setBackId(int backId) {
		this.backId = backId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getBacktime() {
		return backtime;
	}
	public void setBacktime(Date backtime) {
		this.backtime = backtime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
