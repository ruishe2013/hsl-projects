package com.htc.action;

import java.io.IOException;
import java.util.*;

import com.htc.domain.*;
import com.htc.model.SetSysService;

/**
 * @ GprsSetAction.java
 * ���� : Gprs - ���ù���action. ��ҳ - ���� -���� - ɾ�� - ����ɾ�� - ���ֱ༭ - ��ʽ��֤
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class GprsSetAction extends AbstractActionForHigh {

	// ������
	private SetSysService setSysService;

	// ��ҳ
	private Pager pager; 						// ��ҳ��
	private String currentPage="1";				// ��ǰҳ
	private String pagerMethod; 				// ҳ�淽�����磺ǰһҳ����һҳ����һҳ�����һҳ��.
	private int[] ids; 							// ѡ��ɾ����ѡ��(��ѡ)
	private int showTipMsg=0;					// �Ƿ���ʾ������(0:����ʾ 1:��ʾ)	

	// ҳ����ʾ
	private List<GprsSet> gprsSetList;			// �����Ϣ�б�
	private GprsSet gprsSet;					// ҳ����ʾBean
	private List<EffectSign> usableList;		// ��Ч�Ĵ������ �б�

	// ���췽��
	public GprsSetAction() {
	}
	//ע��service -- spring ioc
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	

	@Override
	public String execute() {
		return SUCCESS;
	}

	/**
	 * @describe :��ʾ�б�, Ҳ����ͬʱ���ҳ������
	 * @param type {0�����ҳ������ ��0�������ҳ������}
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(int type) {
		
		// ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		if (type == 0) {
			gprsSet = null;
		}

		pager = setSysService.getPager("GprsSet", getCurrentPage(),getPagerMethod(), USERPAGESIZE,null);
		gprsSetList = (ArrayList) pager.getElements();
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
	}

	// ����½���ʱ������ҳ�� -- �����κ�����
	public String percreate(){
		// jspҳ������Ҫ��  currentPage ��ҳ�� -- �㷵����
		return PERCREATE;
	}	
	
	// ���� (Ψһ���У�������,��ע, ������ַ.�������������)
	public String create() {
		showTipMsg = 0;		
		// ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		if (!setSysService.insertGprsSet(gprsSet)) {
			String equiName = getText("gprs.letter.code") + getText("gprs.or.lable") + getText("gprs.number.code");
			equiName = getText(("error.exist"), new String[] { equiName });
			addFieldError("addFailure", equiName);
			showTipMsg = 1; // ��ʾҳ�����div
			return PERCREATE;
		} else {
			pagerMethod = "last"; // ע��ɹ����������һ��
			list(0);
			return SUCCESS;
		}
	}
	
	// ����
	public String back(){
		list(0);
		return SUCCESS;
	}
	
	// ���ֱ༭
	public String perupdate() {
		showTipMsg = 0;
		// ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		gprsSet = setSysService.getGprsSetById(gprsSet.getGprsSetId());
		if(gprsSet == null){
			addFieldError("showFailure", getText(("error.comm.deleted")));
			showTipMsg = 1;
			return SUCCESS;
		}else{
			return PERUPDATE;
		}
	}
	
	// ����
	public String update(){
		showTipMsg = 0;
		// ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		boolean existFlag = setSysService.updateGprsSet(gprsSet);
		
		if (!existFlag){
			String equiName = getText("gprs.letter.code") + getText("gprs.or.lable") + getText("gprs.number.code");
			equiName = getText(("error.exist"), new String[] { equiName });
			addFieldError("updateFailure", equiName);
			showTipMsg = 1;
			return PERUPDATE;
		}else{
			list(0);
			return SUCCESS;
		}
	}

	// ɾ��
	public String delete() {
		
		// ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		setSysService.deleteGprsSet(gprsSet.getGprsSetId());
		list(0);
		return SUCCESS;
	}

	// ����ɾ��
	public String deleteSelect() {
		
		// ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		if (ids != null) {
			setSysService.deleteGprsSetBatch(ids);
		}
		list(0);
		return SUCCESS;
	}

	// ��ҳ
	public String getList() {
		list(1);
		return SUCCESS;
	}
	
	//��ʽ��֤
	public String valiFormat(){
		return SUCCESS;
	}
	
	// ----------------����Ϊget��set����----------------	
	// ��ȡ ��Ч�Ĵ������ �б�
	public List<EffectSign> getUsableList() throws IOException {
		usableList = new ArrayList<EffectSign>();
		EffectSign signTemp;
		
		int intTemp =1;
		String key = "gprsEffectSign" + intTemp;
		boolean boolTemp = isExist(key);
		
		String[] elements;		
		//(1, "{date}", "����", "ʱ��", "1H��2D��5M");
		while (boolTemp) {
			elements = getSelString(key).split(":");
			signTemp = EffectSign.createEle(Integer.parseInt(elements[0]), elements[1], elements[2], elements[3], elements[4]);
			usableList.add(intTemp-1,signTemp);
			intTemp++;
			key = "gprsEffectSign" + intTemp;
			boolTemp = isExist(key);
		}
		
		return usableList;
	}
	
	// ----------------��ͨ get��set����----------------
	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public List<GprsSet> getGprsSetList() {
		return gprsSetList;
	}

	public void setGprsSetList(List<GprsSet> gprsSetList) {
		this.gprsSetList = gprsSetList;
	}

	public GprsSet getGprsSet() {
		return gprsSet;
	}

	public void setGprsSet(GprsSet gprsSet) {
		this.gprsSet = gprsSet;
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
}
