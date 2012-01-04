package com.htc.domain;

import java.io.Serializable;

public class Workplace implements Serializable {
	
	private static final long serialVersionUID = 7749512176325793028L;
	
	private int placeId;
	private String placeName;
	private int useless = 1;
	private String remark=""; //gsm短信关联的手机对象串联的id，用逗号隔开
	//useless定义: 
	// 2:是"未定义"使用的,是系统默认的,不显示在CRUD界面,但是会出现在仪器添加选项中.
	//1:表示这个用户正在使用 0:表示这个账户已经被删掉了
	
	
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
