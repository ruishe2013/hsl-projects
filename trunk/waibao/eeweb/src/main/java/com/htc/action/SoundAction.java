package com.htc.action;

import com.htc.model.MainService;

/**
 * @ SoundAction.java
 * ���� : �����������Ի�ȡaction. 
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class SoundAction extends AbstractAction {

	// ������
	private MainService mainService;
	
	// ������������״̬������������״̬ (1:������  2:���� 3:ϵͳ�رղ���)
	// ��Ҫ��ϲ����ļ����Ͳ��ż��
	private int palyFalg;							
	
	// ���췽��
	public SoundAction() {
	}
	//ע��service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	/**
	 * @describe: ��ȡ��������״̬ 	
	 * @return: (1:������  2:����)
	 * @date:2010-1-6
	 */	
	public int getPalyFalg() {
		// ��֤�б�������
		if (mainService.selectUndoRec().size()> 0){
			palyFalg = commonDataUnit.getWarnOpenFlag();
			palyFalg = palyFalg == 2? 2:1;
		}else{
			palyFalg = 1;
		}
		return palyFalg;
	}	
	
}
