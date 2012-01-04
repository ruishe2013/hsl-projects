package com.htc.action;

/**
 * @ AbstractActionForOEM.java
 * 作用 : 在只有厂家权限才能访问的action中,需要继承这个类.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class AbstractActionForOEM extends AbstractAction {

	//pagerPower = 86; //页面权限级别--1:管理员  2:高级管理员  86:厂家
	public int getPagerPower() {
		pagerPower = 86;
		return pagerPower;
	}
	
	public void setPagerPower(int pagerPower) {
		this.pagerPower = pagerPower;
	}	
	
}
