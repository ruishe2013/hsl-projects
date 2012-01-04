package com.htc.domain;

import java.io.Serializable;
import java.util.Date;

public class TLog implements Serializable{
	
	private static final long serialVersionUID = -3660064206830334137L;
	/**
	 * 登录信息 = 1
	 */
	public static final int LOGIN_LOG = 1;
	/**
	 * 查询信息 = 2
	 */
	public static final int SEARCH_LOG = 2;
	/**
	 * 串口信息 = 3
	 */
	public static final int SERIAL_LOG = 3;
	/**
	 * 错误信息 = 4
	 */
	public static final int ERROR_LOG = 4;
	/**
	 * 短信报警 = 5
	 */
	public static final int ERROR_SMS = 5;	
	
	private int id;						// 主键
	private int logtype;				// 日志类型[1:登录信息 2:查询信息 3:串口信息 4:错误信息5:短信报警]
	private Date logtime;				// 日志记录时间
	private String logcontent;			// 日志内容
	
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
