package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

public class SmsRecord implements Serializable {
	
	private static final long serialVersionUID = -1827144901057022047L;
	
	private int id;
	private String smsphone;		// ���ź���
	private String smscontent="";	// ��������
	private Date smsrectime;		// ����ʱ��
	private int smstype;			// ��������(1:��ʾ�����Ķ���  2:���յĶ���)
	
	// ��ѯ��
	private	Date smsStart;			// smsStart��ʼʱ��
	private	Date smsTo;				// smsTo����ʱ��
	private String typeStr;			// ��������(1:��ʾ�����Ķ���  2:���յĶ���)
	
	public SmsRecord() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSmsphone() {
		return smsphone;
	}

	public void setSmsphone(String smsphone) {
		this.smsphone = smsphone;
	}

	public String getSmscontent() {
		return smscontent;
	}

	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}

	public Date getSmsrectime() {
		return smsrectime;
	}

	public void setSmsrectime(Date smsrectime) {
		this.smsrectime = smsrectime;
	}

	public int getSmstype() {
		return smstype;
	}
	
	/**
	 * ��������(1:��ʾ�����Ķ���  2:���յĶ���)
	 */
	public void setSmstype(int smstype) {
		this.smstype = smstype;
	}

	public Date getSmsStart() {
		return smsStart;
	}

	public void setSmsStart(Date smsStart) {
		this.smsStart = smsStart;
	}

	public Date getSmsTo() {
		return smsTo;
	}

	public void setSmsTo(Date smsTo) {
		this.smsTo = smsTo;
	}

	public String getTypeStr() {
		return typeStr;
	}

	/**
	 * ��������(1:��ʾ�����Ķ���  2:���յĶ���)
	 */
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

}
