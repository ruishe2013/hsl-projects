package com.htc.action;

import java.util.*;

import com.htc.common.CommonDataUnit;
import com.htc.common.FunctionUnit;
import com.htc.domain.*;
import com.htc.model.ManaService;
import com.htc.model.SetSysService;

/**
 * @ AreaAction.java
 * 作用 : 区域功能信息action. 分页 - 增加 -更改 - 删除 - 批量删除 - 部分编辑
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class AreaAction extends AbstractActionForHigh {

	// 服务类
	private ManaService manaService;
	private SetSysService setSysService;

	// 分页
	private Pager pager; 					// 分页类
	private String currentPage="1"; 		// 当前页
	private String pagerMethod; 			// 页面方法，如：前一页，后一页，第一页，最后一页等.
	private String[] ids; 					// 选中删除的选项(复选)
	private int showTipMsg=0;				// 是否显示错误标记(0:不显示 1:显示 2:该区域下,还有子标注.请删除所用子标注,再进行删除.)	
	
	// 页面显示
	private List<Workplace> placeList;		// 表格信息列表
	private Workplace place;				// 页面显示Bean
	
	// 模糊查询
	private Map<String, Object> searchKey;	// 查询关键字
	private String placeName="";			// page对应placeName
	
	// 修改短信管理
	private Map<String, String> phonelist;

	// 构造方法
	public AreaAction() {
	}
	//注册service -- spring ioc
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	
	/**
	 * @describe : 显示列表, 也可以同时清除页面内容
	 * @param type {0：清除页面内容 非0：不清除页面内容}
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(int type) {
		
		// 实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		
		if (type == 0) {
			place = null;
		}
		if (searchKey == null){
			searchKey = new HashMap<String, Object>();
		}
		if (!placeName.trim().equals("") ) {
			searchKey.put("name", placeName);
		}		
		
		pager = manaService.getPager("Workplace", getCurrentPage(),	getPagerMethod(), USERPAGESIZE,searchKey);
		placeList = (ArrayList) pager.getElements();
		this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
	}

	@Override
	public String execute() {
		return SUCCESS;
	}
	
	// 点击新建的时候跳入页面 -- 不做任何事情
	public String percreate(){
		// jsp页面中需要传  currentPage 给页面 -- 点返回用
		return PERCREATE;
	}	

	// 增加
	public String create(){
		showTipMsg = 0;
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		
		if (!manaService.insertWorkplace(place)) {
			String areaName = getText("mana.area.name.label");
			areaName = getText(("error.exist"), new String[] { areaName });
			addFieldError("addFailure", areaName);
			showTipMsg = 1; // 显示页面错误div
			return PERCREATE;
		} else {
			pagerMethod = "last"; // 注册成功后跳到最后一行
			
			// 重置系统相关信息
			//CommonDataUnit.resetSystem(true,false,false,false,false);			
			commonDataUnit.resetSystem(true,false,false,false,false);			
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
	public String perupdate(){
		showTipMsg = 0;
		// 实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		place = manaService.getWorkplaceById(place.getPlaceId());
		if (place == null){
			addFieldError("showFailure", getText(("error.comm.deleted")));
			showTipMsg = 1;
			list(0);
			return SUCCESS;
		}else{
			return PERUPDATE;
		}
	}	
	
	// 更改
	public String update(){
		showTipMsg = 0;
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		
//	    update tWorkplace set
//    	<isNotEmpty prepend=" " property="placeName">
//      	placeName = #placeName#
//    	</isNotEmpty>	
//    	<isNotNull prepend=" " property="remark">
//    		remark = #remark#
//    	</isNotNull>			
		place.setRemark(null); // 上面的ibatis语句中没有逗号(也不能加),要舍弃一个条件,所以这里设置null值过滤条件

		boolean existChildFlag = manaService.updateWorkplace(place);
		if (!existChildFlag) {
			String areaName = getText("mana.area.name.label");
			areaName =getText(("error.exist"),new String[] { areaName });
			addFieldError("upadteFailure", areaName);
			showTipMsg = 1;
			return PERUPDATE;
		} else {
			// 重置系统相关信息
			//CommonDataUnit.resetSystem(true,true,true,false,false);		
			commonDataUnit.resetSystem(true,true,true,false,false);		
			list(0);
			
			return SUCCESS;
		}
	}
	
	// 部分编辑gsm
	public String pergsm(){
		showTipMsg = 0;
		// 实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		place = manaService.getWorkplaceById(place.getPlaceId());
		if (place == null){
			addFieldError("showFailure", getText(("error.comm.deleted")));
			showTipMsg = 1;
			list(0);
			return SUCCESS;
		}else{
			// 获取短信号码列表 
			List<PhoneList> phonelists = setSysService.getAllPhoneLists();
			phonelist = fillGmsArea(place.getPlaceName(),place.getRemark(), phonelists);
			return "pergsm";
		}
	}
	
	private Map<String, String> fillGmsArea(String placeName, String remark, List<PhoneList> phonelists) {
		Map<String, String> phoneTemp = new HashMap<String, String>();
		StringBuffer strbuf = new StringBuffer();
		boolean isCheck = false;
		int listid = 0;
		String label = "";
		
		if (phonelists.size()>0){
			List<Integer> gsmlist = FunctionUnit.getIntListByStr(remark);
			// 通过比对短信列表和区域中保存的地址列表,来确定是否要选中复选框.
			for (int i = 0; i < phonelists.size(); i++) {
				listid = phonelists.get(i).getListId();
				label = phonelists.get(i).getName() + "[" + phonelists.get(i).getPhone()+"]";
				isCheck = false;
				
				for (int j = 0; j < gsmlist.size(); j++) {
					if(listid == gsmlist.get(j)){
						isCheck = true;
						break;
					}
				}
				// 添加到缓冲区
				strbuf.append(CommonDataUnit.create_Html_P(listid, label, isCheck));
			}
			phoneTemp.put(placeName, strbuf.toString());
		}
		return phoneTemp;
	}

	// 部分编辑gsm
	public String upgsm(){
		showTipMsg = 0;
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		manaService.updateWorkplaceGms(place);
		list(0);
		return SUCCESS;
	}
	
	// 删除
	public String delete() throws Exception {
		showTipMsg = 0;
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		
		//String	str  =  new  String(place.getPlaceName().getBytes("ISO-8859-1"),"gbk");
		String	str  =  place.getPlaceName();
		boolean existChildFlag = manaService.deleteWorkplace(str);
		if (!existChildFlag) {
			// 该区域下,还有子标注.请删除所用子标注,再进行删除.
			// addFieldError("deleteFailure", getText(("error.mana.hasChild")));
			showTipMsg = 2;
		}else{
			// 重置系统相关信息
			//CommonDataUnit.resetSystem(true,false,false,false,false);		
			commonDataUnit.resetSystem(true,false,false,false,false);		
		}
		list(0);
		return SUCCESS;
	}	

	// 批量删除
	public String deleteSelect() {
		showTipMsg = 0;
		
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		if (ids != null) {
			boolean existChildFlag = manaService.deleteWorkplaceBatch(ids);
			if (!existChildFlag) {
				addFieldError("deleteFailure", getText(("error.mana.hasChild")));
				showTipMsg = 1;
				list(1);
			} else {
				// 重置系统相关信息
				//CommonDataUnit.resetSystem(true,false,false,false,false);	
				commonDataUnit.resetSystem(true,false,false,false,false);	
				list(0);
			}
		}
		return SUCCESS;
	}
	
	// 模糊查询
	public String fuzzySearch()  {
		currentPage = "1";
		list(1);
		return SUCCESS;
	}	

	// 分页
	public String getList(){
		list(1);
		return SUCCESS;
	}

	// ----------------以下为get，set方法----------------
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public List<Workplace> getPlaceList() {
		return placeList;
	}

	public void setPlaceList(List<Workplace> placeList) {
		this.placeList = placeList;
	}

	public Workplace getPlace() {
		return place;
	}

	public void setPlace(Workplace place) {
		this.place = place;
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

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getPlaceName() {
		return placeName;
	}

	public Map<String, String> getPhonelist() {
		return phonelist;
	}
	
}
