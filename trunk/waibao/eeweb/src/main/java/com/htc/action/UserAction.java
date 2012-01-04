package com.htc.action;

import java.io.UnsupportedEncodingException;
import java.util.*;
import com.htc.common.FunctionUnit;
import com.htc.domain.*;
import com.htc.model.*;
import com.htc.model.tool.ResetDatabase;
import com.opensymphony.xwork2.*;

/**
 * @ UserAction.java
 * ���� : �û�ģ��action.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class UserAction extends AbstractActionForHigh {

	// ������
	private UserService userService;
	private MainService mainService;

	// ��ҳ
	private Pager pager; 						// ��ҳ��
	private String currentPage="1";				// ��ǰҳ
	private String pagerMethod; 				// ҳ�淽�����磺ǰһҳ����һҳ����һҳ�����һҳ��.
	private int[] ids; 							// ѡ��ɾ����ѡ��(��ѡ)
	
	// ҳ����ʾ
	private User user;							// �����Ϣ�б�
	private List<User> userList;				// ҳ����ʾBean
	
	/**
	 * ҳ�������Ϣ��ʾ���:
	 * 0:��ȷ(��ʼ��ʱ����)
	 * 1:�û��������������...(��¼ҳ��),�д�����Ϣ(����ҳ��)
	 * 2:��ʼ�����ݳ���,����ϵ����...
	 * 3:���ݻ�û�г�ʼ��,����admin��½.ϵͳ�����Զ���ʼ�����ݿ�...
	 */
	private int showTipMsg=0;					

	// ģ����ѯ
	private String searchName = ""; 			// page��Ӧname
	private int searchPower = 0;				// page��Ӧpower
	private Map<String, Object> searchKey;		// ��ѯ�ؼ���

	// Ȩ������
	@SuppressWarnings("unused")
	private Map<Integer, String> powers;		// 1:�߼�����Ա 2:����Ա
	private Map<Integer, String> searchPowers;	// 0:ȫ�� 1:�߼�����Ա 2:����Ա

	// ���췽��
	public UserAction() {
	}
	//ע��service -- spring ioc
	public void setUserService(UserService userService) {
		this.userService = userService;
	}	
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	/**
	 * @describe:�г��б�
	 * @param type {0��������� ��0�����������}
	 * @date:2009-11-6
	 */
	@SuppressWarnings("unchecked")
	public void list(int type)  {
		// ʵ����������
		//userService = userService == null? new UserService() : userService;
		if (type == 0) {
			user = null;
			clearSearch();
		}

		if (searchKey == null) {
			searchKey = new HashMap<String, Object>();
		}
		if (!searchName.trim().equals("") ) {
			searchKey.put("name", searchName);
		}
		if (searchPower != 0) {
			searchKey.put("power", searchPower);
		}

		pager = userService.getPager(getCurrentPage(), getPagerMethod(), USERPAGESIZE, searchKey);
		userList = (ArrayList) pager.getElements();
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
	}

	@Override
	public String execute()  {
		return SUCCESS;
	}
	
	// ����½���ʱ������ҳ�� -- �����κ�����
	public String percreate(){
//����----------------start
//		for (int i = 1; i <= 1; i++) {
//			//�������ݿ�  - [�¶�-ʪ��-eqID-��¼ʱ��]
//			Record record = new Record();
//			record.setTemperature("10.25");
//			record.setHumidity("105.86");
//			record.setEquipmentId(i);
//			record.setRecTime(new Date());
//			
//			// �����ݼ�¼���з�Χ���,�Ա㷢�ֲ���������, ��ʱ����
//			EquipData equip = CommonDataUnit.getEquipByID(i);
//			CommonDataUnit.checkDataWarn(record, equip);			
//		}
		
//		long start = System.currentTimeMillis();
//		try {
//			ExcelBackUp.storeToXls(FunctionUnit.getStrToDate("2009-12-18 00:00:00"),
//						   FunctionUnit.getStrToDate("2009-12-18 20:50:00"));
//			//xls.storeToXls(FunctionUnit.getStrToDate("2009-12-17 12:20:10"),
//					   //FunctionUnit.getStrToDate("2009-12-20 12:20:10"));			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		start = System.currentTimeMillis() - start;
//		System.out.println("ȫ������xls��ʱ:" + start + "����");
//		
//		SerialPortQuartz serialPortQuartz = SerialPortQuartz.getInstance();
//		serialPortQuartz.runTask();
		
		
//		List<Data4Access> list = AccessService.getInstance().getList();
//		if (list != null){
//			for (Data4Access data : list) {
//				System.out.println(data.getStrDSRSN());
//			}
//		}

		
//		List<Data4Access>  datas = new ArrayList<Data4Access>();
//		Data4Access dd = new Data4Access();
		
//		dd.setStrDSRSN("TH000011112222");
//		dd.setStrDSRName("������-01��");
//		dd.setStrDateTime("2010-03-18 10:25:34");
//		dd.setStrTemp("23.64");
//		dd.setStrHUM("89.63");
//		dd.setStrAirPress("555");
//		dd.setStrEquipmentType("TH");
//		datas.add(dd);
//		AccessService.getInstance().insertBatch(datas);
		
//		dd.setStrDSRSN("TH000011112222");
//		dd.setStrDSRName("������-0123��");
//		dd.setStrDateTime("2019-03-18 10:25:34");
//		dd.setStrTemp("93.64");
//		dd.setStrHUM("109.63");
//		dd.setStrAirPress("556665");
//		dd.setStrEquipmentType("TH");
//		datas.add(dd);
//		AccessService.getInstance().updataBatch(datas);

//		AccessService.getInstance().deleteAll();
		//����----------------end			
		
		// jspҳ������Ҫ��  currentPage ��ҳ�� -- �㷵����
		return PERCREATE;
	}

	// �û�ע��
	public String create(){
		showTipMsg = 0;
		// ʵ����������
		//userService = userService == null? new UserService() : userService;
		if (!userService.insertUser(user)) {
			String userName = getText("user.name.label");
			userName = getText(("error.exist"),	new String[] {userName});			
			addFieldError("registerFailure", userName);
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
	
	// ����޸ĵ�ʱ������ҳ�� -- ����Ŀ�����
	public String perupdate(){
		// jspҳ������Ҫ����ֵ:user.getUserId, currentPage
		// ʵ����������
		//userService = userService == null? new UserService() : userService;		
		user = userService.getUserById(user.getUserId());
		if (user == null){
			addFieldError("showFailure", "���û��Ѳ�����.");
			showTipMsg = 1; // ��ʾҳ�����div
			return SUCCESS;
		}else{
			return PERUPDATE;
		}
	}

	// �����û���Ϣ
	public String update() {
		showTipMsg = 0;
		// ʵ����������
		//userService = userService == null? new UserService() : userService;
		if (userService.updateUser(user)){
			list(0);
			return SUCCESS;
		}else{
			addFieldError("showFailure", "���û��Ѿ�����...");
			showTipMsg = 1; // ��ʾҳ�����div
			return PERUPDATE;
		}
	}
	
	// �����û�������Ϣ
	public String perpass() {
		// ʵ����������
		
		//userService = userService == null? new UserService() : userService;
		String str="";
		try {
			str = new  String(user.getName().getBytes("ISO-8859-1"),"gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		user = userService.getUserByName(str);	
		if (user == null){//�����ѱ�ɾ��
			return "not_login_yet";
		}else{
			return "perupdatepass";
		}
	}
	
	// �����û�������Ϣ
	public String updatepass() {
		showTipMsg = 1;
		//ҳ��Ȩ�޼����½�,�Ա��޸�����--1:����Ա  2:�߼�����Ա  86:����
		setPagerPower(1);
		// ʵ����������
		//userService = userService == null? new UserService() : userService;
		user.setUserId(0);// ��id����Ϊ0, �Ϳ��������޸�������޸���Ϣ��
		userService.updateUser(user);
		
		String infoStr = getText("user.person.info");
		infoStr = getText(("message.update.success"),new String[] { infoStr });
		addActionMessage(infoStr);
		return "perupdatepass";
	}
	
	// ɾ���û�
	public String delete() {
		// ʵ����������
		//userService = userService == null? new UserService() : userService;
		userService.deleteUser(user.getUserId());
		list(1);
		return SUCCESS;
	}

	// ����ɾ��
	public String deleteSelect() {
		if (ids != null) {
			// ʵ����������
			//userService = userService == null? new UserService() : userService;
			
			userService.deleteBatch(ids);
		}
		list(1);
		return SUCCESS;
	}

	// �û���½
	public String login() {
		
		String userName = user.getName();
		String userPass = user.getPassword();
		
		// �ж������Ƿ����
		if (!ResetDatabase.checkDBExist()){
			// admin�˺�������ʼ�����ݿ� - ÿ�ε�½������ݿ��Ƿ�Ϊ��,Ϊ�����ʼ�����ݿ�
			if (userName.equals("admin") && userPass.equals("admin") )  {
				// ���������ݿ��û� -> �������� -> д��ϵͳ��Ҫ��Ĭ������
				if (ResetDatabase.resetData()){
					mainService.packTlog(TLog.LOGIN_LOG, "ϵͳ��ʼ�����...");
				}else{
					//addFieldError("loadFailure","��ʼ�����ݳ���,�뾡����ϵ����...");
					showTipMsg = 2;
					return LOGINOUT;						
				}
			}else{
				//addFieldError("loadFailure","���ݻ�û�г�ʼ��,����admin��½.ϵͳ�����Զ���ʼ�����ݿ�...");
				showTipMsg = 3;
				return LOGINOUT;				
			}
		}
		
		// ���˳����û� htcadminhtcweb
		if (userName.equals(OEMNAME)){ // OEMNAME = htcadminhtcweb
			// ����Ϊ��ǰʱ���������,ֻ��һλ��ʱ��,����. ��: 20091002,��ʾ2009��10��2�ŵ�½������.
			String checkPass = FunctionUnit.getDateToStr(new Date(), FunctionUnit.Calendar_END_DAY
					, FunctionUnit.UN_SHOW_CHINESE).replace("-", "");
			if (!userPass.equals(checkPass)){
				//addFieldError("loadFailure", "�û��������������");
				showTipMsg = 1;
				return LOGINOUT;
			}else{
				// ����session
				ActionContext ctx = ActionContext.getContext();
				ctx.getSession().put(LOGINSESSIONTAG, FACTORY_NAME); // �ѵ�½����ʾΪ:����
				//ctx.getSession().put(paly_SESSION_TAG, 2);//Ĭ�Ͽ�������(1:������ 2:����) -- Ŀǰ����Ҫ2010-03-18			
				ctx.getSession().put(user_POWER_TAG, 86);//����Ȩ��session 86������Ȩ��
				mainService.packTlog(TLog.LOGIN_LOG, "[����]��½...");
				return LOGININ;
			}
		}else if(userName.equals(OEM_NAME)){//�����˻�(htc_htc_htc)���ܵ�¼
			//addFieldError("loadFailure", getText("message.load.error"));
			showTipMsg = 1;
			return LOGINOUT;
		}else{
			// ʵ����������
			//userService = userService == null? new UserService() : userService;
			
			User userTemp;
			userTemp = userService.getUserByName(userName, userPass);
			// if �û���½���� else �û���½�ɹ�
			if (userTemp == null) {
				// addFieldError("loadFailure", getText("message.load.error"));
				showTipMsg = 1;
				return LOGINOUT;
			} else {
				// ����session
				ActionContext ctx = ActionContext.getContext();
				ctx.getSession().put(LOGINSESSIONTAG, userName);
				//ctx.getSession().put(paly_SESSION_TAG, 2);//Ĭ�Ͽ�������(1:������ 2:����)
				ctx.getSession().put(user_POWER_TAG, userTemp.getPower());//����Ȩ��session 1������Ա 2:�߼�����Ա
				mainService.packTlog(TLog.LOGIN_LOG, "["+userName+"]��½...");
				return LOGININ;
			}
		}
	}
	
	// �û�ע��
	public String loginOut()  {
		// ȡ��session
		ActionContext ctx = ActionContext.getContext();
		String username = (String) ctx.getSession().get(LOGINSESSIONTAG); 
		if (username != null) {
			mainService.packTlog(TLog.LOGIN_LOG, "["+username+"]�˳�...");
			ctx.getSession().remove(LOGINSESSIONTAG);
		}
//		if (ctx.getSession().get(paly_SESSION_TAG) != null) {
//			ctx.getSession().remove(paly_SESSION_TAG);
//		}
		return INDEX;
	}

	// ģ����ѯ
	public String fuzzySearch()  {
		currentPage = "1";
		list(1);
		return SUCCESS;
	}

	// ���ģ������
	public void clearSearch() {
		searchName = "";
		searchPower = 0;
	}
	
	// ��ҳ
	public String getList() {
		list(1);
		return SUCCESS;
	}	
	
	// ----------------����Ϊget��set����----------------
	public Map<Integer, String> getPowers() {
		// ʵ����������
		//userService = userService == null? new UserService() : userService;
		//searchPowers = getMapSel("power");
		//return searchPowers;
		return userService.getAllPower();
	}

	public Map<Integer, String> getSearchPowers() {
		searchPowers = getPowers();
		searchPowers.put(0, getText("user.all"));
		return searchPowers;
	}
	//----------------��ͨ get��set����----------------
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

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

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public int getSearchPower() {
		return searchPower;
	}

	public void setSearchPower(int searchPower) {
		this.searchPower = searchPower;
	}

	public Map<String, Object> getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(Map<String, Object> searchKey) {
		this.searchKey = searchKey;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

}
