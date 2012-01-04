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
 * 作用 : 用户模块action.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class UserAction extends AbstractActionForHigh {

	// 服务类
	private UserService userService;
	private MainService mainService;

	// 分页
	private Pager pager; 						// 分页类
	private String currentPage="1";				// 当前页
	private String pagerMethod; 				// 页面方法，如：前一页，后一页，第一页，最后一页等.
	private int[] ids; 							// 选中删除的选项(复选)
	
	// 页面显示
	private User user;							// 表格信息列表
	private List<User> userList;				// 页面显示Bean
	
	/**
	 * 页面错误信息显示标记:
	 * 0:正确(初始化时候用)
	 * 1:用户名或者密码错误...(登录页面),有错误信息(其余页面)
	 * 2:初始化数据出错,请联系厂家...
	 * 3:数据还没有初始化,请用admin登陆.系统将会自动初始化数据库...
	 */
	private int showTipMsg=0;					

	// 模糊查询
	private String searchName = ""; 			// page对应name
	private int searchPower = 0;				// page对应power
	private Map<String, Object> searchKey;		// 查询关键字

	// 权限属性
	@SuppressWarnings("unused")
	private Map<Integer, String> powers;		// 1:高级管理员 2:管理员
	private Map<Integer, String> searchPowers;	// 0:全部 1:高级管理员 2:管理员

	// 构造方法
	public UserAction() {
	}
	//注册service -- spring ioc
	public void setUserService(UserService userService) {
		this.userService = userService;
	}	
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	/**
	 * @describe:列出列表
	 * @param type {0：清除对象 非0：不清除对象}
	 * @date:2009-11-6
	 */
	@SuppressWarnings("unchecked")
	public void list(int type)  {
		// 实例化服务类
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
	
	// 点击新建的时候跳入页面 -- 不做任何事情
	public String percreate(){
//测试----------------start
//		for (int i = 1; i <= 1; i++) {
//			//存入数据库  - [温度-湿度-eqID-记录时间]
//			Record record = new Record();
//			record.setTemperature("10.25");
//			record.setHumidity("105.86");
//			record.setEquipmentId(i);
//			record.setRecTime(new Date());
//			
//			// 对数据记录进行范围检查,以便发现不正常数据, 及时报警
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
//		System.out.println("全部导出xls用时:" + start + "毫秒");
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
//		dd.setStrDSRName("测试区-01号");
//		dd.setStrDateTime("2010-03-18 10:25:34");
//		dd.setStrTemp("23.64");
//		dd.setStrHUM("89.63");
//		dd.setStrAirPress("555");
//		dd.setStrEquipmentType("TH");
//		datas.add(dd);
//		AccessService.getInstance().insertBatch(datas);
		
//		dd.setStrDSRSN("TH000011112222");
//		dd.setStrDSRName("测试区-0123号");
//		dd.setStrDateTime("2019-03-18 10:25:34");
//		dd.setStrTemp("93.64");
//		dd.setStrHUM("109.63");
//		dd.setStrAirPress("556665");
//		dd.setStrEquipmentType("TH");
//		datas.add(dd);
//		AccessService.getInstance().updataBatch(datas);

//		AccessService.getInstance().deleteAll();
		//测试----------------end			
		
		// jsp页面中需要传  currentPage 给页面 -- 点返回用
		return PERCREATE;
	}

	// 用户注册
	public String create(){
		showTipMsg = 0;
		// 实例化服务类
		//userService = userService == null? new UserService() : userService;
		if (!userService.insertUser(user)) {
			String userName = getText("user.name.label");
			userName = getText(("error.exist"),	new String[] {userName});			
			addFieldError("registerFailure", userName);
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
	
	// 点击修改的时候跳入页面 -- 返回目标对象
	public String perupdate(){
		// jsp页面中需要传的值:user.getUserId, currentPage
		// 实例化服务类
		//userService = userService == null? new UserService() : userService;		
		user = userService.getUserById(user.getUserId());
		if (user == null){
			addFieldError("showFailure", "该用户已不存在.");
			showTipMsg = 1; // 显示页面错误div
			return SUCCESS;
		}else{
			return PERUPDATE;
		}
	}

	// 更改用户信息
	public String update() {
		showTipMsg = 0;
		// 实例化服务类
		//userService = userService == null? new UserService() : userService;
		if (userService.updateUser(user)){
			list(0);
			return SUCCESS;
		}else{
			addFieldError("showFailure", "该用户已经存在...");
			showTipMsg = 1; // 显示页面错误div
			return PERUPDATE;
		}
	}
	
	// 更改用户个人信息
	public String perpass() {
		// 实例化服务类
		
		//userService = userService == null? new UserService() : userService;
		String str="";
		try {
			str = new  String(user.getName().getBytes("ISO-8859-1"),"gbk");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		user = userService.getUserByName(str);	
		if (user == null){//该项已被删除
			return "not_login_yet";
		}else{
			return "perupdatepass";
		}
	}
	
	// 更改用户个人信息
	public String updatepass() {
		showTipMsg = 1;
		//页面权限级别下降,以便修改密码--1:管理员  2:高级管理员  86:厂家
		setPagerPower(1);
		// 实例化服务类
		//userService = userService == null? new UserService() : userService;
		user.setUserId(0);// 把id设置为0, 就可以区别修改密码和修改信息了
		userService.updateUser(user);
		
		String infoStr = getText("user.person.info");
		infoStr = getText(("message.update.success"),new String[] { infoStr });
		addActionMessage(infoStr);
		return "perupdatepass";
	}
	
	// 删除用户
	public String delete() {
		// 实例化服务类
		//userService = userService == null? new UserService() : userService;
		userService.deleteUser(user.getUserId());
		list(1);
		return SUCCESS;
	}

	// 批量删除
	public String deleteSelect() {
		if (ids != null) {
			// 实例化服务类
			//userService = userService == null? new UserService() : userService;
			
			userService.deleteBatch(ids);
		}
		list(1);
		return SUCCESS;
	}

	// 用户登陆
	public String login() {
		
		String userName = user.getName();
		String userPass = user.getPassword();
		
		// 判断数据是否存在
		if (!ResetDatabase.checkDBExist()){
			// admin账号用来初始化数据库 - 每次登陆检查数据库是否为空,为空则初始化数据库
			if (userName.equals("admin") && userPass.equals("admin") )  {
				// 不创建数据库用户 -> 创建数据 -> 写入系统需要的默认数据
				if (ResetDatabase.resetData()){
					mainService.packTlog(TLog.LOGIN_LOG, "系统初始化完成...");
				}else{
					//addFieldError("loadFailure","初始化数据出错,请尽快联系我们...");
					showTipMsg = 2;
					return LOGINOUT;						
				}
			}else{
				//addFieldError("loadFailure","数据还没有初始化,请用admin登陆.系统将会自动初始化数据库...");
				showTipMsg = 3;
				return LOGINOUT;				
			}
		}
		
		// 过滤厂家用户 htcadminhtcweb
		if (userName.equals(OEMNAME)){ // OEMNAME = htcadminhtcweb
			// 密码为当前时间的年月日,只有一位的时候,补零. 如: 20091002,表示2009年10月2号登陆的密码.
			String checkPass = FunctionUnit.getDateToStr(new Date(), FunctionUnit.Calendar_END_DAY
					, FunctionUnit.UN_SHOW_CHINESE).replace("-", "");
			if (!userPass.equals(checkPass)){
				//addFieldError("loadFailure", "用户名或者密码错误");
				showTipMsg = 1;
				return LOGINOUT;
			}else{
				// 设置session
				ActionContext ctx = ActionContext.getContext();
				ctx.getSession().put(LOGINSESSIONTAG, FACTORY_NAME); // 把登陆名显示为:厂家
				//ctx.getSession().put(paly_SESSION_TAG, 2);//默认开启播放(1:不播放 2:播放) -- 目前不需要2010-03-18			
				ctx.getSession().put(user_POWER_TAG, 86);//个人权限session 86：厂家权限
				mainService.packTlog(TLog.LOGIN_LOG, "[厂家]登陆...");
				return LOGININ;
			}
		}else if(userName.equals(OEM_NAME)){//保留账户(htc_htc_htc)不能登录
			//addFieldError("loadFailure", getText("message.load.error"));
			showTipMsg = 1;
			return LOGINOUT;
		}else{
			// 实例化服务类
			//userService = userService == null? new UserService() : userService;
			
			User userTemp;
			userTemp = userService.getUserByName(userName, userPass);
			// if 用户登陆错误 else 用户登陆成功
			if (userTemp == null) {
				// addFieldError("loadFailure", getText("message.load.error"));
				showTipMsg = 1;
				return LOGINOUT;
			} else {
				// 设置session
				ActionContext ctx = ActionContext.getContext();
				ctx.getSession().put(LOGINSESSIONTAG, userName);
				//ctx.getSession().put(paly_SESSION_TAG, 2);//默认开启播放(1:不播放 2:播放)
				ctx.getSession().put(user_POWER_TAG, userTemp.getPower());//个人权限session 1：管理员 2:高级管理员
				mainService.packTlog(TLog.LOGIN_LOG, "["+userName+"]登陆...");
				return LOGININ;
			}
		}
	}
	
	// 用户注销
	public String loginOut()  {
		// 取消session
		ActionContext ctx = ActionContext.getContext();
		String username = (String) ctx.getSession().get(LOGINSESSIONTAG); 
		if (username != null) {
			mainService.packTlog(TLog.LOGIN_LOG, "["+username+"]退出...");
			ctx.getSession().remove(LOGINSESSIONTAG);
		}
//		if (ctx.getSession().get(paly_SESSION_TAG) != null) {
//			ctx.getSession().remove(paly_SESSION_TAG);
//		}
		return INDEX;
	}

	// 模糊查询
	public String fuzzySearch()  {
		currentPage = "1";
		list(1);
		return SUCCESS;
	}

	// 清除模糊内容
	public void clearSearch() {
		searchName = "";
		searchPower = 0;
	}
	
	// 分页
	public String getList() {
		list(1);
		return SUCCESS;
	}	
	
	// ----------------以下为get，set方法----------------
	public Map<Integer, String> getPowers() {
		// 实例化服务类
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
	//----------------普通 get，set方法----------------
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
