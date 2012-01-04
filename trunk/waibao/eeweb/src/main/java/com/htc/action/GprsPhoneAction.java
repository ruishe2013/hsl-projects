package com.htc.action;

import java.util.*;

import com.htc.domain.*;
import com.htc.model.SetSysService;

/**
 * @ GprsPhoneAction.java
 * 作用 : Gprs 通讯列表 功能信息action. 分页 - 增加 -更改 - 删除 - 批量删除 - 部分编辑
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class GprsPhoneAction extends AbstractActionForHigh {
	
	// 服务类
	private SetSysService setSysService;
	
	// 分页
	private Pager pager; 						// 分页类
	private String currentPage="1"; 			// 当前页
	private String pagerMethod; 				// 页面方法，如：前一页，后一页，第一页，最后一页等.
	private int[] ids; 							// 选中删除的选项(复选)
	private int showTipMsg=0;					// 是否显示错误标记(0:不显示 1:显示)	

	// 页面显示
	private List<PhoneList> list_phoneList;		// 表格信息列表
	private PhoneList phoneList;				// 页面显示Bean
	
	// 模糊查询
	private String searchName = ""; 			// page对应name
	private Map<String, Object> searchKey;		// 查询关键字	

	// 构造方法
	public GprsPhoneAction() {
	}
	//注册service -- spring ioc
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}

	@Override
	public String execute() {
		return SUCCESS;
	}

	/**
	 * @describe: 显示列表, 也可以同时清除页面内容	
	 * @param type {0：清除页面内容 非0：不清除页面内容}
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(int type){
		
		// 实例化服务类
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
	
	// 点击新建的时候跳入页面 -- 不做任何事情
	public String percreate(){
		// jsp页面中需要传  currentPage 给页面 -- 点返回用
		return PERCREATE;
	}	


	// 增加 (唯一性有：区域名,标注, 仪器地址.这三个属性组成)
	public String create() {
		showTipMsg = 0;		
		//实例化服务类
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		if (!setSysService.insertPhoneList(phoneList)) {
			String equiName = getText("gprs.name.label") + getText("gprs.or.lable") + getText("gprs.phone.label");
			equiName = getText(("error.exist"), new String[] { equiName });
			addFieldError("addFailure", equiName);
			showTipMsg = 1; // 显示页面错误div
			return PERCREATE;
		} else {
			pagerMethod = "last"; // 注册成功后跳到最后一行
			list(0);
			commonDataUnit.fillPhoneList();
			return SUCCESS;			
		}
	}
	
	// 返回
	public String back(){
		list(0);
		return SUCCESS;
	}
	
	// 部分编辑
	public String perupdate() {
		showTipMsg = 0;
		//实例化服务类
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
	
	// 更改
	public String update(){
		showTipMsg = 0;
		//实例化服务类
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

	// 删除
	public String delete() {
		
		//实例化服务类
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		setSysService.deletePhoneList(phoneList.getListId());
		list(0);
		commonDataUnit.fillPhoneList();
		return SUCCESS;
	}

	// 批量删除
	public String deleteSelect() {
		
		//实例化服务类
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		if (ids != null) {
			setSysService.deletePhoneListBatch(ids);
		}
		list(0);
		commonDataUnit.fillPhoneList();
		return SUCCESS;
	}

	// 分页
	public String getList() {
		list(1);
		return SUCCESS;
	}
	
	// 模糊查询
	public String fuzzySearch()  {
		currentPage = "1";
		list(1);
		return SUCCESS;
	}	

	// ----------------以下为get，set方法----------------	
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
