package com.htc.action;

/**
 * @ AbstractActionForOEM.java
 * ���� : ��ֻ�г���Ȩ�޲��ܷ��ʵ�action��,��Ҫ�̳������.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class AbstractActionForOEM extends AbstractAction {

	//pagerPower = 86; //ҳ��Ȩ�޼���--1:����Ա  2:�߼�����Ա  86:����
	public int getPagerPower() {
		pagerPower = 86;
		return pagerPower;
	}
	
	public void setPagerPower(int pagerPower) {
		this.pagerPower = pagerPower;
	}	
	
}
