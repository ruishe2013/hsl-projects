package com.htc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.htc.bean.BeanForEqOrder;
import com.htc.bean.BeanForSysArgs;
import com.htc.domain.EquipData;
import com.htc.domain.Pager;
import com.htc.model.ManaService;
import com.htc.model.ServiceSqlServer;

/**
 * @ EquiAction.java
 * 作用 : 仪器功能信息action. 分页 - 增加 -更改 - 删除 - 批量删除 - 部分编辑	- 模糊查询 - 调整顺序
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class EquiAction extends AbstractActionForHigh {

	// 服务类
	private ManaService manaService;
	private ServiceSqlServer serviceSqlServer;

	// 分页
	private Pager pager; 							// 分页类
	private String currentPage="1"; 				// 当前页
	private String pagerMethod; 					// 页面方法，如：前一页，后一页，第一页，最后一页等.
	private int[] ids; 								// 选中删除的选项(复选)

	// 模糊查询
	private int searchType;							// page对应type
	private int searchPlaceId;						// page对应placeId
	private String searchMark;						// page对应mark
	private Map<String, Object> searchKey;			// 查询关键字

	// 页面显示
	private List<EquipData> equiList;				// 表格信息列表
	private EquipData equipData ;					// 页面显示Bean
	private	String tempUp = "40"; 
	private	String tempDown = "20";
	private	String tempDev = "0";
	private	String humiUp = "100";
	private	String humiDown = "0";
	private	String humiDev = "0";
	private int equitype = 1;						// 1:温湿度 2:单温度 3:单湿度
	private int showTipMsg=0;						// 是否显示错误标记(0:不显示 1:显示)	
	private int tempshowType;						// 温度显示格式(1:摄氏 2:华氏)
	
	// 顺序调整页面
	private String eqorderStr = "";					// 仪器排顺后,产生的顺序字符串,以逗号隔开.如:2,1,3
	
	// 仪器属性
	@SuppressWarnings("unused")
	private Map<Integer, String> equiTypes;			// 1:温湿度 2:单温度 3:单湿度
	private Map<Integer, String> conndatas;			// 串口发送位数:串口通信的时候,获取的字节数,默认是3个(温度,湿度,露点),4个的(温度,湿度,露点,电压)
	private Map<Integer, String> searchEquiType;	// 0:全部 1:温湿度 2:单温度 3:单湿度
	@SuppressWarnings("unused")
	private Map<Integer, String> areaMap;			// 地址标签列表  key:placeId value:placeName
	private Map<Integer, String> searchAreaMap;		// 0:全部 key:placeId
	private Map<Integer, String> powerShows;		// 是否显示电量信息 (0: 不显示    1: 显示) 
	private Map<Integer, String> powerValues;		// 显示电量值 (1:1.5v, 2:3.6v)
	
	// 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	@SuppressWarnings("unused")
	private int systemEqType;
	
	// 搜索标题-细节
	private String headTitle;						// 标题
	private String headDetail;						// 信息细节		

	// 构造方法
	public EquiAction() {
	}
	//注册service -- spring ioc
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setServiceSqlServer(ServiceSqlServer serviceSqlServer) {
		this.serviceSqlServer = serviceSqlServer;
	}
	
	/**
	 * @describe : 显示列表, 也可以同时清除页面内容	
	 * @param type {0：清除页面内容 非0：不清除页面内容}
	 * @date:2009-11-4
	 */
	@SuppressWarnings("unchecked")
	public void list(int type) {
		
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		
		if (type == 0) {
			equipData = null;
		}
		if (searchKey == null) {
			searchKey = new HashMap<String, Object>();
		}
		if (searchType != 0) {
			searchKey.put("type", searchType);
		}
		if (searchPlaceId != 0) {
			searchKey.put("placeId", searchPlaceId);
		}
		if (searchMark != "") {
			searchKey.put("mark", searchMark);
		}
		
		pager = manaService.getPager("EquipData", getCurrentPage(),
				getPagerMethod(), USERPAGESIZE, searchKey);
		equiList = (ArrayList) pager.getElements();
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
	
	// 增加 (唯一性有：区域名,标注, 仪器地址.这三个属性组成)
	public String create(){
		String returnPage = SUCCESS;
		showTipMsg = 0;
		
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;		
		if (equipData.getPlaceId()== 0) {
			String noArea = getText("error.mana.not.exist");
			addFieldError("ararNotExist", noArea);
			showTipMsg = 1;
			returnPage =  PERCREATE;
		} else {
			if (checkRange()){
				if (!manaService.insertEquipData(equipData)) {
					String equiName = getText("equi.set.label");
					equiName = getText(("error.exist"), new String[] { equiName });
					addFieldError("addFailure", equiName);
					showTipMsg = 1;
					returnPage =  PERCREATE;
				} else {
					pagerMethod = "last"; // 注册成功后跳到最后一行
					list(0);
					
					// 重置系统相关信息
					//CommonDataUnit.resetSystem(false,true,true,false,true);
					commonDataUnit.resetSystem(false,true,true,false,true);
					
				} // end else
			}else{
				showTipMsg = 1;
				returnPage = PERCREATE;						
			}
		}
		return returnPage;
	}
	
	// 返回
	public String back(){
		list(0);
		return SUCCESS;
	}
	
	// 部分编辑
	public String perupdate() {
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		equipData = manaService.getEquipDataById(equipData.getEquipmentId());
		
		// 把equipData分割成 一个一个单元,用来显示在页面上
		equipDataToPiece();
		if (equipData == null){
			addFieldError("showFailure", getText(("error.comm.deleted")));
			showTipMsg = 1; // 显示页面错误div
			return SUCCESS;
		}else{
			return PERUPDATE;
		}
	}	

	// 更改 -- 信息
	public String update() {
		String returnPage = SUCCESS;
		showTipMsg = 0;
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		boolean existFlag;
		
		if (checkRange()){
			existFlag = manaService.updateEquipData(equipData);
			if (!existFlag) {
				String equiName = getText("equi.set.label");
				equiName = getText(("error.exist"), new String[] { equiName });
				addFieldError("updateFailure", equiName);
				showTipMsg = 1;
				returnPage = PERUPDATE;
			} else {
				// 重置系统相关信息
				//CommonDataUnit.resetSystem(false,true,true,false,false);
				commonDataUnit.resetSystem(false,true,true,false,false);
				list(0);
			}
		}else{
			showTipMsg = 1;
			returnPage = PERUPDATE;			
		}
		return returnPage;
	}
	
	/**
	 * @describe:	整合成 EquipData,并判断温湿度是否越界
	 * @return: false: 异常  true : 正常
	 * @date:2009-11-18
	 */
	public boolean checkRange(){
		boolean flag = true;
		int checkInt = connectToEquipData();
		if (checkInt >= 10) {
			checkInt = checkInt - 10;
			if (equipData.getEquitype()!=2){
				flag = false;
				addFieldError("updateFailure1", "湿度范围不正确...");
			}
		}
		if ((checkInt >= 1) && (equipData.getEquitype()!=3) ){
			flag = false;
			addFieldError("updateFailure2", "温度范围不正确...");
		}
		return flag;
	}

	// 删除
	public String delete() {
		
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;		
		manaService.deleteEquipData(equipData.getEquipmentId());
		// 重置系统相关信息
		//CommonDataUnit.resetSystem(false,true,true,false,true);
		commonDataUnit.resetSystem(false,true,true,false,true);
		list(1);
		
		return SUCCESS;
	}

	// 批量删除
	public String deleteSelect() {
		
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;		
		
		if (ids != null) {
			manaService.deleteEquipBatch(ids);
			// 重置系统相关信息
			//CommonDataUnit.resetSystem(false,true,true,false,true);
			commonDataUnit.resetSystem(false,true,true,false,true);
		}
		list(0);
		return SUCCESS;
	}

	public void equipDataToPiece(){
		tempUp =  String.valueOf(equipData.getTempUp());
		tempDown = String.valueOf(equipData.getTempDown());
		tempDev = String.valueOf(equipData.getTempDev());
		humiUp = String.valueOf(equipData.getHumiUp());
		humiDown = String.valueOf(equipData.getHumiDown());
		humiDev = String.valueOf(equipData.getHumiDev());
		equitype = equipData.getEquitype();
	}
	
	/**
	 * @describe: 整合成	EquipData,并判断温湿是否越界
	 * @return: 
	 * 	1:代表温度范围不对(低温值大于等于高温值)
	 * 	10:代表湿度范围不对(低湿值大于等于高湿值)
	 * @date:2009-11-18
	 */
	public int connectToEquipData(){
		int rsint = 0;
		float temfloat;
		
		temfloat = Float.parseFloat(tempUp);
		equipData.setTempUp(temfloat);
		// 判断温度的范围
		rsint = temfloat <= Float.parseFloat(tempDown) ? 1 : 0;
		temfloat = Float.parseFloat(tempDown);
		equipData.setTempDown(temfloat);
		
		temfloat = Float.parseFloat(tempDev);
		equipData.setTempDev(temfloat);
		
		temfloat = Float.parseFloat(humiUp);
		equipData.setHumiUp(temfloat);
		// 判断湿度的范围
		rsint += temfloat <= Float.parseFloat(humiDown) ? 10 : 0;
		temfloat = Float.parseFloat(humiDown);
		equipData.setHumiDown(temfloat);
		
		temfloat = Float.parseFloat(humiDev);
		equipData.setHumiDev(temfloat);
		
		return rsint;
	}
	
	// 分页
	public String getList(){
		list(1);
		return SUCCESS;
	}

	// 模糊查询
	public String fuzzySearch() {
		currentPage = "1";
		list(1);
		return SUCCESS;
	}

	// 修改列表顺序
	public String updateOrderList() {
		//System.out.println("产生的顺序字符:" + eqorderStr);
		
		List<EquipData> orderList = new ArrayList<EquipData>();
		
		// 实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		String[] equipmentIdList = eqorderStr.split(",");
		int orderInt = 0;
		for (String equipmentId : equipmentIdList) {
			orderInt++;
			orderList.add(getEdata(equipmentId, orderInt));
		}
		
		// 修改数据库
		if (orderList.size() > 0) {
			manaService.updateEquiOrder(orderList);
		}
		
		// 重置系统相关信息
		//CommonDataUnit.resetSystem(false,true,false,false,false);
		commonDataUnit.resetSystem(false,true,false,false,false);
		
		//return SHOWORDER;
		list(0);
		return SUCCESS;
	}
	// 显示调整顺序用的列表
	public String showOrderList() {
		
		// 实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;
		
		BeanForEqOrder eqorderBean = new BeanForEqOrder();
		eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_ORDERID);
		equiList = manaService.getAllEquiObject(eqorderBean);
		eqorderBean = null;
		return SHOWORDER;
	}
	
	/**
	 * @describe: 填充 仪器信息. 主要是否显示仪器	
	 * @param key 仪器主键ID
	 * @param order 顺序号.按数字升序排列.如: 1表示排在最前面,2其次,然后345...
	 * @date:2009-11-4
	 */
	public EquipData getEdata(String equipmentId, int order) {
		EquipData edata = new EquipData();
		edata.setEquipmentId(Integer.parseInt(equipmentId.trim()));
		edata.setEquiorder(order);
		return edata;
	}
	
	// 把配置修改到Sqlserver,浙江药监局要用到的
	public String updata4ZJ(){
		// 在Access存储功能开启的情况下处理
		if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_ACCESS_STORE)) == 2){
			BeanForEqOrder eqorderBean = new BeanForEqOrder();
			eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_NOTHING);
			eqorderBean.setUseless(0);
			List<EquipData> equiplist = manaService.selectEquiOrderStr(eqorderBean);
			if (serviceSqlServer.flashAllMonitors(equiplist)){
				showInfo(1); // 成功的提示
			}else{
				showInfo(2); // 失败的提示
			}
		}
		return "noDataReturn";
	}
	
	/**
	 * @describe: 把配置写入药监局的数据库
	 * @param type <br>
	 * 1:成功的提示<br>
	 * 2:失败的提示<br>
	 * @date:2010-4-14
	 */
	private void showInfo(int type) {
		StringBuffer strbuf = new StringBuffer();
		headTitle = type == 1? "成功关联到药监局使用的数据库..." :"关联到药监局使用的数据库时出错:";
		
		if (type == 1){
			strbuf.append("<label>");
			strbuf.append("关联成功,请放心使用!");
			strbuf.append("</label><br/><br/>");		
		}else if (type == 2){
			strbuf.append("建议:");
			strbuf.append("<br/>");
			strbuf.append("<label>");
			strbuf.append("    ①检测系统中是否安装有sqlserver数据库");
			strbuf.append("</label><br/><br/>");
			strbuf.append("<br/>");
			strbuf.append("<label>");
			strbuf.append("    ②检测sqlserver数据库:用户名:sa 密码:sa");
			strbuf.append("</label><br/><br/>");
			strbuf.append("<br/>");
			strbuf.append("<br/>");
			strbuf.append("    ● 确认以上信息后,仍然出错. 有可能是软件上存在问题,请尽快联系我们...");	
		}
		headDetail = strbuf.toString();
	}
	
	// ----------------以下为get，set方法----------------
	public Map<Integer, String> getEquiTypes() {
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;	
		return manaService.getAllEquiType();
	}

	public Map<Integer, String> getSearchEquiType() {
		searchEquiType = getEquiTypes();
		searchEquiType.put(0, getText("user.all"));
		return searchEquiType;
	}
	
	public Map<Integer, String> getConndatas() {
		conndatas = new HashMap<Integer, String>();
		conndatas.put(3, "3位");
		conndatas.put(4, "4位");				
		return conndatas;
	}

	public Map<Integer, String> getAreaMap() {
		//实例化服务类
		//manaService = manaService == null ? new ManaService() : manaService;			
		return manaService.getAllPlace();
	}

	public Map<Integer, String> getSearchAreaMap() {
		searchAreaMap = getAreaMap();
		searchAreaMap.put(0, getText("user.all"));
		return searchAreaMap;
	}
	// 是否显示电量信息 (0: 不显示    1: 显示)
	public Map<Integer, String> getPowerShows() {
		powerShows = new HashMap<Integer, String>();
		powerShows.put(0, "不显示");
		powerShows.put(1, "显示");		
		return powerShows;
	}

	// 显示电量值 (1:1.5v, 2:3.6v)
	public Map<Integer, String> getPowerValues() {
		powerValues = new HashMap<Integer, String>();
		powerValues.put(1, "1.5V");
		powerValues.put(2, "3.6V");
		return powerValues;
	}

	//温度显示格式(1:摄氏 2:华氏)
	public int getTempshowType(){
		//tempshowType = Integer.parseInt(CommonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));		
		tempshowType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));		
		return tempshowType;
	}	
	
	/**
	 * @describe:
	 * 统计系统中所用仪器类型情况: 
	 * 		1:系统中既有温度又有湿度仪器		
	 * 		2:系统中全部是温度仪器	
	 * 		3:系统中全部是湿度仪器
	 * @return
	 * @date:2010-1-23
	 */
	public int getSystemEqType() {
		// 返回 systemEqType
		//return CommonDataUnit.getSystemEqType();
		return commonDataUnit.getSystemEqType();
	}	
	// ----------------普通 get，set方法----------------
	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public List<EquipData> getEquiList() {
		return equiList;
	}

	public void setEquiList(List<EquipData> equiList) {
		this.equiList = equiList;
	}

	public EquipData getEquipData() {
		return equipData;
	}

	public void setEquipData(EquipData equipData) {
		this.equipData = equipData;
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

	public int getSearchPlaceId() {
		return searchPlaceId;
	}

	public void setSearchPlaceId(int searchPlaceId) {
		this.searchPlaceId = searchPlaceId;
	}

	public String getSearchMark() {
		return searchMark;
	}

	public void setSearchMark(String searchMark) {
		this.searchMark = searchMark;
	}

	public Map<String, Object> getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(Map<String, Object> searchKey) {
		this.searchKey = searchKey;
	}

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}

	public String getTempUp() {
		return tempUp;
	}

	public void setTempUp(String tempUp) {
		this.tempUp = tempUp;
	}

	public String getTempDown() {
		return tempDown;
	}

	public void setTempDown(String tempDown) {
		this.tempDown = tempDown;
	}

	public String getTempDev() {
		return tempDev;
	}

	public void setTempDev(String tempDev) {
		this.tempDev = tempDev;
	}

	public String getHumiUp() {
		return humiUp;
	}

	public void setHumiUp(String humiUp) {
		this.humiUp = humiUp;
	}

	public String getHumiDown() {
		return humiDown;
	}

	public void setHumiDown(String humiDown) {
		this.humiDown = humiDown;
	}

	public String getHumiDev() {
		return humiDev;
	}

	public void setHumiDev(String humiDev) {
		this.humiDev = humiDev;
	}

	public int getEquitype() {
		return equitype;
	}

	public void setEquitype(int equitype) {
		this.equitype = equitype;
	}

	public void setEqorderStr(String eqorderStr) {
		this.eqorderStr = eqorderStr;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}
	
	public String getHeadTitle() {
		return headTitle;
	}
	
	public String getHeadDetail() {
		return headDetail;
	}
	
}
