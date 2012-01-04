package com.htc.action;

import com.htc.common.CommonDataUnit;

/**
 * @ ComNameAction.java
 * ���� : ר��������ȡ��˾����action
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
public class ComNameAction extends AbstractAction {

	// json ����
	private String comName;						// json �������: �����������ID
	
	// ���췽��
	public ComNameAction() {
	}
	//ע��service -- spring ioc
	
	@Override
	public String execute() {
		comName = commonDataUnit.comName();
		return SUCCESS;
	}
	
	public String getComName() {
		return comName;
	}

}
