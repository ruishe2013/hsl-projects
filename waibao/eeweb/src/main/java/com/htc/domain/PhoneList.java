package com.htc.domain;

import java.io.Serializable;

public class PhoneList implements Serializable {
	
	private static final long serialVersionUID = -2986591450768391531L;
	
	private int listId;
	private String name;
	private String phone;
	private String remark;
	private int useless;
	
	public int getListId() {
		return listId;
	}
	public void setListId(int listId) {
		this.listId = listId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getUseless() {
		return useless;
	}
	public void setUseless(int useless) {
		this.useless = useless;
	}	

}
