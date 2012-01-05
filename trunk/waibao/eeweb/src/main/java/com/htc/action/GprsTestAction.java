package com.htc.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.htc.bean.BeanForSysArgs;
import com.htc.domain.GprsSet;
import com.htc.domain.Pager;
import com.htc.model.SetSysService;
import com.htc.model.seriaPort.Level_First_Serial;
import com.htc.model.seriaPort.SimCard_Unit;

/**
 * @ GprsTestAction.java
 * ���� : Gprs - ���Թ���action. ��ҳ - ���� -�Ͽ����� - ����
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class GprsTestAction extends AbstractActionForHigh {

	//������
	private SetSysService setSysService;
	private Level_First_Serial first_Level;
	private SimCard_Unit simCard_Unit;
	
	// ��ҳ
	private Pager pager; 							// ��ҳ��
	private String currentPage="1";					// ��ǰҳ
	private String pagerMethod; 					// ҳ�淽�����磺ǰһҳ����һҳ����һҳ�����һҳ��.
	private int showTipMsg=0;						// �Ƿ���ʾ������(0:����ʾ 1:��ʾ)	
	
	// ҳ����ʾ
	private int portNo;								// �˿ں�(����)
	private int baudRateNo = 9600;					// ������(����)
	private String centerNo = "13800571500" ;		// ��������(����)
	private String phoneNo ;						// ���Ե��ֻ�����
	private String sendcontent ;					// �����͵�����
	private String msgShow ;						// ajax���ص�״̬���н��
	private int linkFlag = 0;						// �˿�����״�� (0:û������ 1:����) -- û���õ�
	
	private List<GprsSet> gprsSetList;				// �����Ϣ�б�(����)
	private Map<Integer, String> portList;			// �˿ں� �б� (����)
	private Map<Integer, String> baudRatelList;		// �������б�  (����)
	
	private int runFlag;							// 1:��������	2:û������
	private String stateStr="";						// ҳ����ʾ״̬�ַ�
	
	// ���췽��
	public GprsTestAction() {
	}
	//ע��service -- spring ioc
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}
	
	@Override
	public String execute() {
		//list();
		if (simCard_Unit.isRunFlag()){
		//if (Smslib_SendJob.getInstance().isSms_run_flag()){
			runFlag = 1;
			stateStr = "��������...";
		}else{
			runFlag = 2;
			stateStr = "��û������...";
		}
		centerNo = this.commonDataUnit.getSysArgsByKey(BeanForSysArgs.SMS_CENTER_NUMBER);
		
		return SUCCESS;
	}

	/**
	 * @describe: ��ʾ�б�	
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(){
		
		// ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		pager = setSysService.getPager("GprsSet", getCurrentPage(),getPagerMethod(), USERPAGESIZE,null);
		gprsSetList = (ArrayList) pager.getElements();
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
	}
	
	// ����
	public String link() {
		String passport ="";
		showTipMsg = 1;
		if (simCard_Unit.isRunFlag()){
		//if (Smslib_SendJob.getInstance().isSms_run_flag()){
			stateStr = "��������...";
			runFlag = 1;			
		}else{
			if (first_Level.isRunningFlag() == true){
				passport = first_Level.getPortStr();
			}			
			//System.out.println("������...");
			if (simCard_Unit.openPort(passport, 9600)){
			//if (Smslib_SendJob.getInstance().startService(9600,passport)){
				stateStr = "��������...";
				runFlag = 1;
			}else{
				stateStr = "����ʧ��...";
				runFlag = 2;
			}
		}
		return SUCCESS;
	}
	
	// �Ͽ�����
	public String dislink() {
		//System.out.println("�Ͽ�������...");
		showTipMsg = 1;
		runFlag = 2;
		
		if (simCard_Unit.isRunFlag()){
		//if (Smslib_SendJob.getInstance().isSms_run_flag()){
			if (simCard_Unit.closePort()){
			//if (Smslib_SendJob.getInstance().stopService()){
				stateStr = "�Ͽ��ɹ�...";
			}else{
				stateStr = "����ʧ��...";
				runFlag = 1;
			}
		}else{
			stateStr = "�Ͽ��ɹ�...";
		}
		return SUCCESS;
	}
	
	// ���Է��Ͷ��� - ����ʽ���зֿ�
	public String send(){
		showTipMsg = 1;
		String phone,msg;
		try {
			if (simCard_Unit.isRunFlag()){
				msgShow = "����ģ����������,Ҫ���Զ��ŷ���,����ֹͣģ������...";
			}else{
				String passport ="";
				centerNo = URLDecoder.decode(centerNo, "UTF-8");
				phone = URLDecoder.decode(phoneNo, "UTF-8");
				msg = URLDecoder.decode(sendcontent, "UTF-8");
				
				if (first_Level.isRunningFlag() == true){
					passport = first_Level.getPortStr();
				}		
				if (simCard_Unit.singleForTest(passport, 9600, centerNo, phone, msg)){
					msgShow = "���ͳɹ�...";
				}else{
					msgShow = "����ʧ��...";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	// ��ҳ
	public String getList() {
		list();
		return SUCCESS;
	}
	
	// ----------------����Ϊget��set����----------------
	//�˿ں�
	public Map<Integer, String> getPortList() {
		portList = getMapSel("gprscom");
		return portList;
	}
	//������
	public Map<Integer, String> getBaudRatelList() {
		baudRatelList = getMapSel("gprsbaudRate");
		return baudRatelList;
	}

	// ----------------��ͨ get��set����----------------
	public int getPortNo() {
		return portNo;
	}

	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}

	public int getBaudRateNo() {
		return baudRateNo;
	}

	public void setBaudRateNo(int baudRateNo) {
		this.baudRateNo = baudRateNo;
	}

	public String getCenterNo() {
		return centerNo;
	}

	public void setCenterNo(String centerNo) {
		this.centerNo = centerNo;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public List<GprsSet> getGprsSetList() {
		return gprsSetList;
	}
	
	public void setGprsSetList(List<GprsSet> gprsSetList) {
		this.gprsSetList = gprsSetList;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPagerMethod() {
		return pagerMethod;
	}

	public void setPagerMethod(String pagerMethod) {
		this.pagerMethod = pagerMethod;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public int getLinkFlag() {
		return linkFlag;
	}

	public void setLinkFlag(int linkFlag) {
		this.linkFlag = linkFlag;
	}

	public String getSendcontent() {
		return sendcontent;
	}

	public void setSendcontent(String sendcontent) {
		this.sendcontent = sendcontent;
	}

	public String getMsgShow() {
		return msgShow;
	}

	public int getRunFlag() {
		return runFlag;
	}

	public String getStateStr() {
		return stateStr;
	}

}
