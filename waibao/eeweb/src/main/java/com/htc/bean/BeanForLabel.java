package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForTemp.java
 * ���� : һ�����ݽṹ,���˴洢:
 * 	areaName:������ (��:������)
 * 	mark:��ʶ(��:1��)
 * 	dsrsn:�������(��:TH0000111112222)
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-17     YANGZHONLI       create
 */
public class BeanForLabel implements Serializable {

	private static final long serialVersionUID = -7581394543247507361L;
	
	/**
	 * ������ (��:������)
	 */
	private String areaName;
	/**
	 * ��ʶ(��:1��)
	 */
	private String mark;
	/**
	 *dsrsn:�������(��:TH0000111112222) 
	 */
	private String dsrsn;
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	public String getDsrsn() {
		return dsrsn;
	}
	public void setDsrsn(String dsrsn) {
		this.dsrsn = dsrsn;
	}
	
}
