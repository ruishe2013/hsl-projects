package com.htc.action;

import java.util.*;

import com.htc.domain.*;
import com.htc.model.SetSysService;

/**
 * @ GprsPhoneAction.java
 * ���� : Gprs ͨѶ�б� ������Ϣaction. ��ҳ - ���� -���� - ɾ�� - ����ɾ�� - ���ֱ༭
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class GprsPhoneAction extends AbstractActionForHigh {
	
	// ������
	private SetSysService setSysService;
	
	// ��ҳ
	private Pager pager; 						// ��ҳ��
	private String currentPage="1"; 			// ��ǰҳ
	private String pagerMethod; 				// ҳ�淽�����磺ǰһҳ����һҳ����һҳ�����һҳ��.
	private int[] ids; 							// ѡ��ɾ����ѡ��(��ѡ)
	private int showTipMsg=0;					// �Ƿ���ʾ������(0:����ʾ 1:��ʾ)	

	// ҳ����ʾ
	private List<PhoneList> list_phoneList;		// �����Ϣ�б�
	private PhoneList phoneList;				// ҳ����ʾBean
	
	// ģ����ѯ
	private String searchName = ""; 			// page��Ӧname
	private Map<String, Object> searchKey;		// ��ѯ�ؼ���	

	// ���췽��
	public GprsPhoneAction() {
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
	 * @describe: ��ʾ�б�, Ҳ����ͬʱ���ҳ������	
	 * @param type {0�����ҳ������ ��0�������ҳ������}
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(int type){
		
		// ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		if (type == 0) {
			phoneList = null;
		}
		if (searchKey == null) {
			searchKey = new HashMap<String, Object>();
		}
		if (!searchName.trim().equals("") ) {
			searchKey.put("name", searchName);
		}		
		pager = setSysService.getPager("PhoneList", getCurrentPage(), getPagerMethod(), USERPAGESIZE, searchKey);
		list_phoneList = (ArrayList) pager.getElements();
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
		//ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		if (!setSysService.insertPhoneList(phoneList)) {
			String equiName = getText("gprs.name.label") + getText("gprs.or.lable") + getText("gprs.phone.label");
			equiName = getText(("error.exist"), new String[] { equiName });
			addFieldError("addFailure", equiName);
			showTipMsg = 1; // ��ʾҳ�����div
			return PERCREATE;
		} else {
			pagerMethod = "last"; // ע��ɹ����������һ��
			list(0);
			commonDataUnit.fillPhoneList();
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
		//ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		phoneList = setSysService.getPhoneListById(phoneList.getListId());
		if (phoneList == null){
			addFieldError("showFailure", getText(("error.comm.deleted")));
			showTipMsg = 1;
			list(1);
			return SUCCESS;
		}else{
			return PERUPDATE;
		}
	}	
	
	// ����
	public String update(){
		showTipMsg = 0;
		//ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		boolean existFlag =	setSysService.updatePhoneList(phoneList);
		if ( !existFlag){
			String equiName = getText("gprs.name.label") + getText("gprs.or.lable") + getText("gprs.phone.label");
			equiName = getText(("error.exist"), new String[] { equiName });
			addFieldError("updateFailure", equiName);
			showTipMsg = 1;
			return PERUPDATE;
		}else{
			list(0);
			commonDataUnit.fillPhoneList();
			return SUCCESS;
		}
	}

	// ɾ��
	public String delete() {
		
		//ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		setSysService.deletePhoneList(phoneList.getListId());
		list(0);
		commonDataUnit.fillPhoneList();
		return SUCCESS;
	}

	// ����ɾ��
	public String deleteSelect() {
		
		//ʵ����������
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		if (ids != null) {
			setSysService.deletePhoneListBatch(ids);
		}
		list(0);
		commonDataUnit.fillPhoneList();
		return SUCCESS;
	}

	// ��ҳ
	public String getList() {
		list(1);
		return SUCCESS;
	}
	
	// ģ����ѯ
	public String fuzzySearch()  {
		currentPage = "1";
		list(1);
		return SUCCESS;
	}	

	// ----------------����Ϊget��set����----------------	
	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
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

	public List<PhoneList> getList_phoneList() {
		return list_phoneList;
	}

	public void setList_phoneList(List<PhoneList> list_phoneList) {
		this.list_phoneList = list_phoneList;
	}

	public PhoneList getPhoneList() {
		return phoneList;
	}

	public void setPhoneList(PhoneList phoneList) {
		this.phoneList = phoneList;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

}
