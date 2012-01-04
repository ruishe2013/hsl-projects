package com.htc.action;

import java.io.IOException;
import java.util.*;

import com.htc.domain.*;
import com.htc.model.SetSysService;

/**
 * @ GprsSetAction.java
 * 作用 : Gprs - 设置功能action. 分页 - 增加 -更改 - 删除 - 批量删除 - 部分编辑 - 格式验证
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class GprsSetAction extends AbstractActionForHigh {

	// 服务类
	private SetSysService setSysService;

	// 分页
	private Pager pager; 						// 分页类
	private String currentPage="1";				// 当前页
	private String pagerMethod; 				// 页面方法，如：前一页，后一页，第一页，最后一页等.
	private int[] ids; 							// 选中删除的选项(复选)
	private int showTipMsg=0;					// 是否显示错误标记(0:不显示 1:显示)	

	// 页面显示
	private List<GprsSet> gprsSetList;			// 表格信息列表
	private GprsSet gprsSet;					// 页面显示Bean
	private List<EffectSign> usableList;		// 有效的代替符号 列表

	// 构造方法
	public GprsSetAction() {
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
	 * @describe :显示列表, 也可以同时清除页面内容
	 * @param type {0：清除页面内容 非0：不清除页面内容}
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(int type) {
		
		// 实例化服务类
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		if (type == 0) {
			gprsSet = null;
		}

		pager = setSysService.getPager("GprsSet", getCurrentPage(),getPagerMethod(), USERPAGESIZE,null);
		gprsSetList = (ArrayList) pager.getElements();
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
		// 实例化服务类
		//setSysService = setSysService == null ? new SetSysService() : setSysService;		
		
		if (!setSysService.insertGprsSet(gprsSet)) {
			String equiName = getText("gprs.letter.code") + getText("gprs.or.lable") + getText("gprs.number.code");
			equiName = getText(("error.exist"), new String[] { equiName });
			addFieldError("addFailure", equiName);
			showTipMsg = 1; // 显示页面错误div
			return PERCREATE;
		} else {
			pagerMethod = "last"; // 注册成功后跳到最后一行
			list(0);
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
		// 实例化服务类
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
	
	// 更改
	public String update(){
		showTipMsg = 0;
		// 实例化服务类
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

	// 删除
	public String delete() {
		
		// 实例化服务类
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		setSysService.deleteGprsSet(gprsSet.getGprsSetId());
		list(0);
		return SUCCESS;
	}

	// 批量删除
	public String deleteSelect() {
		
		// 实例化服务类
		//setSysService = setSysService == null ? new SetSysService() : setSysService;
		
		if (ids != null) {
			setSysService.deleteGprsSetBatch(ids);
		}
		list(0);
		return SUCCESS;
	}

	// 分页
	public String getList() {
		list(1);
		return SUCCESS;
	}
	
	//格式验证
	public String valiFormat(){
		return SUCCESS;
	}
	
	// ----------------以下为get，set方法----------------	
	// 获取 有效的代替符号 列表
	public List<EffectSign> getUsableList() throws IOException {
		usableList = new ArrayList<EffectSign>();
		EffectSign signTemp;
		
		int intTemp =1;
		String key = "gprsEffectSign" + intTemp;
		boolean boolTemp = isExist(key);
		
		String[] elements;		
		//(1, "{date}", "日期", "时间", "1H或2D或5M");
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
	
	// ----------------普通 get，set方法----------------
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
