package com.htc.domain;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = -1853419149995204092L;
	
	//数据库表列表成员
	private int userId;
	private String name;
	private String password;
	private int power;			// 0:厂家用(只有htc_htc_htc这一个账号) 1: 管理员 2: 高级管理员
	private int useless;		// 1:表示这个用户正在使用 0:表示这个账户已经被删掉了
	private String placeAStr;	// 总览画面用户列表
	private String placeBStr;	// 实时曲线用户列表
	private String personStr;	// 用户个性数据区域(暂时没有用到)
	private int updateFlag =0;	// 0:修改一般信息(姓名,密码,权限) 1:修改placeAStr	2:修改placeBStr 
	
	//--辅助成员--权限名
	private String powerStr;   
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
	public int getUseless() {
		return useless;
	}
	public void setUseless(int useless) {
		this.useless = useless;
	}
	public String getPlaceAStr() {
		return placeAStr;
	}
	public void setPlaceAStr(String placeAStr) {
		this.placeAStr = placeAStr;
	}
	public String getPlaceBStr() {
		return placeBStr;
	}
	public void setPlaceBStr(String placeBStr) {
		this.placeBStr = placeBStr;
	}
	public String getPersonStr() {
		return personStr;
	}
	public void setPersonStr(String personStr) {
		this.personStr = personStr;
	}
	public String getPowerStr() {
		return powerStr;
	}
	public void setPowerStr(String powerStr) {
		this.powerStr = powerStr;
	}
	public int getUpdateFlag() {
		return updateFlag;
	}
	public void setUpdateFlag(int updateFlag) {
		this.updateFlag = updateFlag;
	}
}
