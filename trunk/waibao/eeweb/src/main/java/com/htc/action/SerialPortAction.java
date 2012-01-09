package com.htc.action;

import java.util.List;

import com.htc.domain.EquipData;
import com.htc.model.seriaPort.Level_First_Serial;

/**
 * @ SerialPortAction.java
 * ���� : �������� - ���ݶ�ȡaction.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class SerialPortAction extends AbstractAction {
	
	// ҳ��
	private String stateStr="";						// ҳ����ʾ״̬�ַ�
	private int runFlag;							// 1:��������	2:û������
	private int showTipMsg=0;						// �Ƿ���ʾ������(0:����ʾ 1:��ʾ)
	private String infile;							// ������ļ���

	private Level_First_Serial first_Level;
	//ע��service -- spring ioc
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}
	//���캯��
	public SerialPortAction() {
	}
	
	// ҳ����ʾ - ����ҳ���3��״̬
	public String execute() {
		if( getEquiListSize() == 0 ){
			setPageState(2,1);
		}else{
			if (getSerialRunState()){
				setPageState(1,2);
			}else{
				setPageState(2,3);
			}
		}
		return SUCCESS;
	}
	
	// ������������
	public String startRun(){
		int rs = first_Level.startRunSerial();
		if (rs == 0){				// �������� 
			setPageState(1,2);
		}else if(rs == 1){			// û�п��õ�����(˵��û���������)
			setPageState(2,1);
		}else if(rs == 2){			// �������ӳ���
			setPageState(2,3,"��������ʧ��"); 	
		}
		return SUCCESS;
	}
	
	// ֹͣ��������
	public String endRun(){
		if (first_Level.endRunSerial()){
			setPageState(2,3,"����ֹͣ...");
		}else{
			setPageState(1,3,"ֹͣʧ��...");
		}
		return SUCCESS;
	}	
	
	// ����
	public String restart(){
		showTipMsg = 1;
		endRun();
		startRun();
		addActionMessage(stateStr);
		return SUCCESS;
	}
	
	/**
	 * @describe: ��ȡ��������״̬	
	 * @return: true:�������� false:û������
	 * @date:2009-12-22
	 */
	public boolean getSerialRunState(){
		boolean rsbool = true;
		
		// 1: �жϴ����Ƿ�������
		rsbool = first_Level.isRunningFlag();
		
		return rsbool;
	}
	
	/**
	 * @describe: ��ȡ���������б����
	 * @date:2009-12-22
	 */
	public int getEquiListSize(){
		List<EquipData> equiList = commonDataUnit.getEquiList();
		return equiList.size();
	}	
	
	/**
	 * @describe: ����ҳ����ʾ��Ϣ	
	 * @param flag ����״̬[1:��������	2:û������]
	 * @param msgType:��Ϣ����
	 * [1:�����б�Ϊ��,��ʱ�������д���... 2:������������...	3:���ڻ�û�п�ʼ����...]
	 * @param more: ��������
	 * @date:2009-12-22
	 */
	public void setPageState(int flag, int msgType, String more){
		runFlag = flag;
		if (msgType == 1){
			stateStr = "�����б�Ϊ��,��ʱ�������д���...";
		}else if (msgType == 2){
			stateStr = "������������...";
		}else if (msgType == 3){
			stateStr = "���ڻ�û�п�ʼ����...";
		}
		if (!more.equals("")){stateStr = more;} 
	}
	/**
	 * @describe: ����ҳ����ʾ��Ϣ	
	 * @param flag ����״̬[1:��������	2:û������]
	 * @param msgType:��Ϣ����
	 * [1:�����б�Ϊ��,��ʱ�������д���... 2:������������...	3:���ڻ�û�п�ʼ����...]
	 * @date:2009-12-22
	 */	
	public void setPageState(int flag, int msgType){
		setPageState(flag, msgType,"");
	}		
	
	// ----------------����Ϊget��set����----------------
	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}

	public int getRunFlag() {
		return runFlag;
	}

	public void setRunFlag(int runFlag) {
		this.runFlag = runFlag;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public void setShowTipMsg(int showTipMsg) {
		this.showTipMsg = showTipMsg;
	}

	public String getInfile() {
		return infile;
	}

	public void setInfile(String infile) {
		this.infile = infile;
	}
	
}
