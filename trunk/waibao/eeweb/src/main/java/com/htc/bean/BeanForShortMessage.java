package com.htc.bean;

/**
 * @ BeanForEnDeJson.java
 * ���� : ���Ž��պͷ�����Ҫ��
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-1-28     YANGZHONLI       create
 */
public class BeanForShortMessage {
	
	private String phoneNumber;	// �����ŷ��� �ֻ�����
	private String receiveTime;	// ���Ž���ʱ��
	private String content;		// ��������
	
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
