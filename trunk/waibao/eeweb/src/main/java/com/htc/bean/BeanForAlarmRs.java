package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForSms.java
 * 作用 : 给"对当前的报警数据,应该执行哪种操作: 插入或者修改"这个方法提供的返回类型
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2010-3-3     YANGZHONLI       create
 */
public class BeanForAlarmRs implements Serializable{
	
	/**
	 * 查询结果类型0:查询操作有错误,不执行任何操作(默认值)<br>
	 */
	public static int NUDO_ALARM = 0;
	/**
	 * 查询结果类型1:执行插入<br>
	 */
	public static int ADD_ALARM = 1;
	/**
	 * 查询结果类型2:执行修改<br>
	 */
	public static int UPDATE_ALARM = 2;

	private static final long serialVersionUID = 8860185602365058408L;
	/**
	 * 报警记录主键
	 */
	private int alarmId;
	/**
	 * 查询结果类型
	 * 0:查询操作有错误,不执行任何操作(默认值)<br>
	 * 1:执行插入<br>
	 * 2:执行修改<br>
	 */
	private int rsType = 0;
	
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}
	/**
	 * 查询结果类型
	 * 0:查询操作有错误,不执行任何操作(默认值)<br>
	 * 1:执行插入<br>
	 * 2:执行修改<br>
	 */
	public int getRsType() {
		return rsType;
	}
	/**
	 * 查询结果类型
	 * 0:查询操作有错误,不执行任何操作(默认值)<br>
	 * 1:执行插入<br>
	 * 2:执行修改<br>
	 */	
	public void setRsType(int rsType) {
		this.rsType = rsType;
	}
	
}
