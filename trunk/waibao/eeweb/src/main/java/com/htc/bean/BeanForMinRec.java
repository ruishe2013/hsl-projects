package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForSms.java
 * ���� : Ϊ��ѯ���µ�ʵʱ����,����ѯ������Bean�á�(... where equipmentId in ($placeList$) and	recLong = #recLong#)
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-3     YANGZHONLI       create
 */
public class BeanForMinRec implements Serializable{

	private static final long serialVersionUID = -7367962515890272039L;
	
	/**
	 * ���������б�,�ö��Ÿ���
	 */
	private String placeList;
	/**
	 * ��¼ʱ��,������
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
