package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForSms.java
 * 作用 : 为查询最新的实时数据,做查询条件的Bean用。(... where equipmentId in ($placeList$) and	recLong = #recLong#)
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-3     YANGZHONLI       create
 */
public class BeanForMinRec implements Serializable{

	private static final long serialVersionUID = -7367962515890272039L;
	
	/**
	 * 仪器主键列表,用逗号隔开
	 */
	private String placeList;
	/**
	 * 记录时间,毫秒数
	 */
	private long recLong;
	
	
	public String getPlaceList() {
		return placeList;
	}
	public void setPlaceList(String placeList) {
		this.placeList = placeList;
	}
	public long getRecLong() {
		return recLong;
	}
	public void setRecLong(long recLong) {
		this.recLong = recLong;
	}
	
}
