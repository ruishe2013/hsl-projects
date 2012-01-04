package com.htc.action;

/**
 * @ AbstractActionForHigh.java
 * ���� : ��ֻ�и߼�����Ա,Ȩ�޲��ܷ��ʵ�action��,��Ҫ�̳������.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class AbstractActionForHigh extends AbstractAction {

	/**
	 * ��ʱ�ı�ҳ��Ȩ�޼���
	 * �����õ��ĳ��� : �޸ĸ�����Ϣ������ʱ,��Ҫ����Ȩ��.
	 * �÷�1:<s:hidden name="changeKey" value="1"></s:hidden>
	 * �÷�2:<a href="${pageContext.request.contextPath}/userdo/infouserAction.action?user.name=${sessionScope.managerName}&changeKey=1">
	 */
	private int changeKey = 0;
	
	/**
	 * @describe: ��ȡ ��ʱ�ı�ҳ��Ȩ�޼���	
	 * @date:2009-11-4
	 */
	public int getChangeKey() {
		return changeKey;
	}

	/**
	 * @describe: ���� ��ʱ�ı�ҳ��Ȩ�޼���	
	 * @date:2009-11-4
	 */
	public void setChangeKey(int changeKey) {
		this.changeKey = changeKey;
	}

	//pagerPower = 2; //ҳ��Ȩ�޼���--1:����Ա  2:�߼�����Ա  86:����
	public int getPagerPower() {
		if (changeKey != 0){
			pagerPower = 1;
		}else{
			pagerPower = 2;
		}
		return pagerPower;
	}
	
	public void setPagerPower(int pagerPower) {
		this.pagerPower = pagerPower;
	}		
	
}
