package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

public class TLog implements Serializable{
	
	private static final long serialVersionUID = -3660064206830334137L;
	/**
	 * ��¼��Ϣ = 1
	 */
	public static final int LOGIN_LOG = 1;
	/**
	 * ��ѯ��Ϣ = 2
	 */
	public static final int SEARCH_LOG = 2;
	/**
	 * ������Ϣ = 3
	 */
	public static final int SERIAL_LOG = 3;
	/**
	 * ������Ϣ = 4
	 */
	public static final int ERROR_LOG = 4;
	/**
	 * ���ű��� = 5
	 */
	public static final int ERROR_SMS = 5;	
	
	private int id;						// ����
	private int logtype;				// ��־����[1:��¼��Ϣ 2:��ѯ��Ϣ 3:������Ϣ 4:������Ϣ5:���ű���]
	private Date logtime;				// ��־��¼ʱ��
	private String logcontent;			// ��־����
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getLogtype() {
		return logtype;
	}
	public void setLogtype(int logtype) {
		this.logtype = logtype;
	}
	public Date getLogtime() {
		return logtime;
	}
	public void setLogtime(Date logtime) {
		this.logtime = logtime;
	}
	public String getLogcontent() {
		return logcontent;
	}
	public void setLogcontent(String logcontent) {
		this.logcontent = logcontent;
	}
	
}
