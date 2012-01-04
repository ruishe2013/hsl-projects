package com.htc.domain;

import java.io.Serializable;

public class Workplace implements Serializable {
	
	private static final long serialVersionUID = 7749512176325793028L;
	
	private int placeId;
	private String placeName;
	private int useless = 1;
	private String remark=""; //gsm���Ź������ֻ���������id���ö��Ÿ���
	//useless����: 
	// 2:��"δ����"ʹ�õ�,��ϵͳĬ�ϵ�,����ʾ��CRUD����,���ǻ�������������ѡ����.
	//1:��ʾ����û�����ʹ�� 0:��ʾ����˻��Ѿ���ɾ����
	
	
	public Workplace() {
	}
	public int getPlaceId() {
		return placeId;
	}
	public void setPlaceId(int placeId) {
		this.placeId = placeId;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public int getUseless() {
		return useless;
	}
	public void setUseless(int useless) {
		this.useless = useless;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
