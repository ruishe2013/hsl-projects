package com.htc.bean;

import java.io.Serializable;

/**
 * @ eqOrderBean.java
 * 作用 : 仪器排序单元
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-23     YANGZHONLI       create
 */
public class BeanForEqOrder implements Serializable {

	private static final long serialVersionUID = -1742031264655201509L;
	
	/**
	 * 按照仪器顺序号升序获取数据 0
	 */
	public static int SELECT_WITH_ORDERID = 0;
	/**
	 * 按照仪器号升序获取数据 1
	 */
	public static int SELECT_WITH_EQUIPID = 1;
	/**
	 * 不按照任何字段排序
	 */
	public static int SELECT_WITH_NOTHING = 2;
	
	private int eqorderType;
	private int useless = 1;		// useless=1:所有有用的区域		useless=0:所有有用的区域(包括被删掉的)
	
	public int getEqorderType() {
		return eqorderType;
	}
	public void setEqorderType(int eqorderType) {
		this.eqorderType = eqorderType;
	}
	public int getUseless() {
		return useless;
	}
	public void setUseless(int useless) {
		this.useless = useless;
	}
	
	
}
