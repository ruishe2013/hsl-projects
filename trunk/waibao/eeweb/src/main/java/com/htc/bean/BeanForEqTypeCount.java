package com.htc.bean;

import java.io.Serializable;

/**
 * @ BeanForEqTypeCount.java
 * ���� : �����������ͻ���
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-23     YANGZHONLI       create
 */
public class BeanForEqTypeCount implements Serializable {

	private static final long serialVersionUID = 6979023717056871992L;
	
	private int eqType ; 	// ��������: 1,'��ʪ��';2,'���¶�';3,'��ʪ��'
	private int eqCount ;	// ϵͳ���������Ͷ�Ӧ������
	
	/**
	 * @describe:��������: 1,'��ʪ��';2,'���¶�';3,'��ʪ��'
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
