package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForTemp.java
 * 作用 : 一个数据结构,用了存储:
 * 	areaName:区域名 (如:测试区)
 * 	mark:标识(如:1号)
 * 	dsrsn:仪器编号(如:TH0000111112222)
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-17     YANGZHONLI       create
 */
public class BeanForLabel implements Serializable {

	private static final long serialVersionUID = -7581394543247507361L;
	
	/**
	 * 区域名 (如:测试区)
	 */
	private String areaName;
	/**
	 * 标识(如:1号)
	 */
	private String mark;
	/**
	 *dsrsn:仪器编号(如:TH0000111112222) 
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
