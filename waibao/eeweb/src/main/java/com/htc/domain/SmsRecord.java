package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

public class SmsRecord implements Serializable {
	
	private static final long serialVersionUID = -1827144901057022047L;
	
	private int id;
	private String smsphone;		// 短信号码
	private String smscontent="";	// 短信内容
	private Date smsrectime;		// 短信时间
	private int smstype;			// 短信类型(1:表示发出的短信  2:接收的短信)
	
	// 查询用
	private	Date smsStart;			// smsStart开始时间
	private	Date smsTo;				// smsTo结束时间
	private String typeStr;			// 短信类型(1:表示发出的短信  2:接收的短信)
	
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
	 * 短信类型(1:表示发出的短信  2:接收的短信)
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
	 * 短信类型(1:表示发出的短信  2:接收的短信)
	 */
	public void setTypeStr(String typeStr) {
		this.typeStr = typeStr;
	}

}
