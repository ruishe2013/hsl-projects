package com.htc.bean;

/**
 * @ BeanForEnDeJson.java
 * 作用 : 短信接收和发生需要的
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-1-28     YANGZHONLI       create
 */
public class BeanForShortMessage {
	
	private String phoneNumber;	// 发短信方的 手机号码
	private String receiveTime;	// 短信接收时间
	private String content;		// 短信内容
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}	
}
