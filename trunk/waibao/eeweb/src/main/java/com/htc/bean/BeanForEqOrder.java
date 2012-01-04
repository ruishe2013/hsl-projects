package com.htc.bean;

import java.io.Serializable;

/**
 * @ eqOrderBean.java
 * ���� : ��������Ԫ
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-23     YANGZHONLI       create
 */
public class BeanForEqOrder implements Serializable {

	private static final long serialVersionUID = -1742031264655201509L;
	
	/**
	 * ��������˳��������ȡ���� 0
	 */
	public static int SELECT_WITH_ORDERID = 0;
	/**
	 * ���������������ȡ���� 1
	 */
	public static int SELECT_WITH_EQUIPID = 1;
	/**
	 * �������κ��ֶ�����
	 */
	public static int SELECT_WITH_NOTHING = 2;
	
	private int eqorderType;
	private int useless = 1;		// useless=1:�������õ�����		useless=0:�������õ�����(������ɾ����)
	
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
