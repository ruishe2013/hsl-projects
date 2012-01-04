package com.htc.bean;

import java.io.Serializable;
import java.util.List;

public class BeanForJxls implements Serializable {

	private static final long serialVersionUID = -6844119770792829570L;
	
	private int eqid;			// ��������
	private String title; 		// ����
	private String rtime; 		// �����¼ʱ��
	private String dsrsn;		// ������ǩ(��:TH000011112222
	private List<BeanForRecord> beanList;//��Ҫ���ݵ����ݼ���
	
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
