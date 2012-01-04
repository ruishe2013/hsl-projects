package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForEqTypeCount.java
 * 作用 : 根据仪器类型汇总
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-23     YANGZHONLI       create
 */
public class BeanForEqTypeCount implements Serializable {

	private static final long serialVersionUID = 6979023717056871992L;
	
	private int eqType ; 	// 仪器类型: 1,'温湿度';2,'单温度';3,'单湿度'
	private int eqCount ;	// 系统中仪器类型对应的数量
	
	/**
	 * @describe:仪器类型: 1,'温湿度';2,'单温度';3,'单湿度'
	 * @date:2010-1-23
	 */
	public int getEqType() {
		return eqType;
	}
	public void setEqType(int eqType) {
		this.eqType = eqType;
	}
	public int getEqCount() {
		return eqCount;
	}
	public void setEqCount(int eqCount) {
		this.eqCount = eqCount;
	}
	
}
