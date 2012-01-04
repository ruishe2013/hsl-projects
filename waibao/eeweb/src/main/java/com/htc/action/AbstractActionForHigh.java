package com.htc.action;

/**
 * @ AbstractActionForHigh.java
 * 作用 : 在只有高级管理员,权限才能访问的action中,需要继承这个类.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class AbstractActionForHigh extends AbstractAction {

	/**
	 * 临时改变页面权限级别
	 * 现在用到的场所 : 修改个人信息的密码时,需要降低权限.
	 * 用法1:<s:hidden name="changeKey" value="1"></s:hidden>
	 * 用法2:<a href="${pageContext.request.contextPath}/userdo/infouserAction.action?user.name=${sessionScope.managerName}&changeKey=1">
	 */
	private int changeKey = 0;
	
	/**
	 * @describe: 获取 临时改变页面权限级别	
	 * @date:2009-11-4
	 */
	public int getChangeKey() {
		return changeKey;
	}

	/**
	 * @describe: 设置 临时改变页面权限级别	
	 * @date:2009-11-4
	 */
	public void setChangeKey(int changeKey) {
		this.changeKey = changeKey;
	}

	//pagerPower = 2; //页面权限级别--1:管理员  2:高级管理员  86:厂家
	public int getPagerPower() {
		if (changeKey != 0){
			pagerPower = 1;
		}else{
			pagerPower = 2;
		}
		return pagerPower;
	}
	
	public void setPagerPower(int pagerPower) {
		this.pagerPower = pagerPower;
	}		
	
}
