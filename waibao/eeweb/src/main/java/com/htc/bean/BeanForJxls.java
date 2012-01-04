package com.htc.bean;

import java.io.Serializable;
import java.util.List;

public class BeanForJxls implements Serializable {

	private static final long serialVersionUID = -6844119770792829570L;
	
	private int eqid;			// 仪器主键
	private String title; 		// 标题
	private String rtime; 		// 报表记录时间
	private String dsrsn;		// 仪器标签(如:TH000011112222
	private List<BeanForRecord> beanList;//需要操纵的数据集合
	
	public int getEqid() {
		return eqid;
	}
	public void setEqid(int eqid) {
		this.eqid = eqid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<BeanForRecord> getBeanList() {
		return beanList;
	}
	public void setBeanList(List<BeanForRecord> beanList) {
		this.beanList = beanList;
	}
	public String getRtime() {
		return rtime;
	}
	public void setRtime(String rtime) {
		this.rtime = rtime;
	}
	public String getDsrsn() {
		return dsrsn;
	}
	public void setDsrsn(String dsrsn) {
		this.dsrsn = dsrsn;
	}
	
}
