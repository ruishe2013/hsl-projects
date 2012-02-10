package com.htc.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.FunctionUnit;
import com.htc.domain.BackUpList;
import com.htc.domain.Pager;
import com.htc.model.ManaService;
import com.htc.model.ServiceAccess;
import com.htc.model.ServiceSqlServer;
import com.htc.model.SetSysService;
import com.htc.model.seriaPort.Level_First_Serial;
import com.htc.model.seriaPort.SimCard_Unit;

/**
 * @ SetSysAction.java
 * 作用 : 备份设置 和 系统设置 action .
 * 注意事项 : 取消数据备份和还原,以及系统颜色设置 功能
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class SetSysAction extends AbstractActionForHigh {
	
	// 序列化号
	private static final long serialVersionUID = -1969123516449503773L;
	
	private static final Log log = LogFactory.getLog(SetSysAction.class);

	// 服务类
	private SetSysService setSysService;
	private ManaService manaService;
	private ServiceAccess serviceAccess;
	private ServiceSqlServer serviceSqlServer;	
	private Level_First_Serial first_Level;
	private SimCard_Unit simCard_Unit;	
	//--------------------历史数据(报表数据)---Start--------------------//
	// 分页
	private Pager pager; 							// 分页类
	private String currentPage="1";					// 当前页
	private String pagerMethod; 					// 页面方法，如：前一页，后一页，第一页，最后一页等.
	
	// 页面显示
	private List<BackUpList> backUpLists;			// 表格信息列表
	// --------------------历史数据(报表数据)---Start--------------------//	
	
	// 页面显示
	private Map<String, String> sysArgs;			// 系统信息
	private Map<Integer, String>  baudRatelList;	// 波特率		-- 下拉框
	private List<String> backuppath;				// 历史数据自动备份路径
	private Map<Integer,String> tempshowSel;		// 温度显示类型 (1:摄氏,2:华氏) 
	private Map<Integer, String>  flashTimeList;	// 访问串口间隔	-- 下拉框	
	private Map<Integer,String> alarmFileSel;  		// 报警播放文件	
	private Map<Integer,String> cnmitype;  			// 短信模块,数据直接保存到串口命令:AT+CNMI=2,2(值为1)或者AT+CNMI=2,1(值为2)	
	private String downfilename;					// 下载的文件名	
	private int oldRecTime;							// 修改系统配置时, 实时曲线的数量
	
	// 页面 <s:checkbox/>  -- 把页面复选框是否选中,映射成Map类型(Map<"复选框键值",boolean>)
	private Map<String, Boolean> showGroup ;		// 这里设置两个键值:msssage和pcsound对应,短信报警和声卡报警是否启动
	private String KEY_MSSSAGE = "msssage" ;
	private String KEY_PCSOUND = "pcsound" ;
	private String KEY_ACCESS = "doaccess" ;
	
	// 页面是否显示提示信息
	private int showTipMsg = 0;						// 0:不显示  1:显示
	
	// 搜索用
	private Map<Integer, String> searchAreaMap;		// 区域信息Map
	private String searchPlace = "全部";				// 区域名
	private Map<String, Object> searchKey;			// 查询关键字
	private String timeFromStr;						// 搜索开始时间-字符
	private String timeToStr;						// 搜索结束时间-字符
	private String startTime;						// 搜索开始时间
	private String endTime;							// 搜索结束时间
	
	// 搜索标题-细节
	private String headTitle;						// 标题
	private String headDetail;						// 信息细节	
	
	// 构造方法
	public SetSysAction() {
	}
	//注册service -- spring ioc
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setServiceAccess(ServiceAccess serviceAccess) {
		this.serviceAccess = serviceAccess;
	}
	public void setServiceSqlServer(ServiceSqlServer serviceSqlServer) {
		this.serviceSqlServer = serviceSqlServer;
	}
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}	
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}	
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	public String initSys(){
		try {
			showTipMsg = 0;
			//实例化服务类
			//setSysService = setSysService == null ? new SetSysService() : setSysService ;
			sysArgs = setSysService.getSysParamMap();
			
			boolean tempBool = true;
			showGroup = showGroup == null ? new HashMap<String, Boolean>() : showGroup;
			
			tempBool = (sysArgs.get(BeanForSysArgs.OPEN_SHORT_MESSAGE)).equals("2");
			showGroup.put(KEY_MSSSAGE, tempBool);
			tempBool = (sysArgs.get(BeanForSysArgs.OPEN_PCSOUND)).equals("2");
			showGroup.put(KEY_PCSOUND, tempBool);
			tempBool = (sysArgs.get(BeanForSysArgs.OPEN_ACCESS_STORE)).equals("2");
			showGroup.put(KEY_ACCESS, tempBool);
			// 修改系统配置时, 实时曲线的数量
			oldRecTime = Integer.parseInt(sysArgs.get(BeanForSysArgs.MAX_RS_COUNT_LINE));
		} catch (Exception e) {
			log.error("initSys error", e);
		}
		return SETTING;
	}
	
	public String update(){
		try {
			String tempStr;
			
			// 短信报警是否打开(1:关闭 2:打开)
			tempStr = showGroup.get(KEY_MSSSAGE)? "2" : "1";
			sysArgs.put(BeanForSysArgs.OPEN_SHORT_MESSAGE, tempStr);
			// 声卡报警是否打开(1:关闭 2:打开)
			tempStr = showGroup.get(KEY_PCSOUND)? "2" : "1";
			sysArgs.put(BeanForSysArgs.OPEN_PCSOUND, tempStr);
			// Access数据保存是否打开(1:关闭 2:打开)
			Boolean access = showGroup.get(KEY_ACCESS);
			if (access == null) {
				access = Boolean.FALSE;
			}
			tempStr = access? "2" : "1";
			sysArgs.put(BeanForSysArgs.OPEN_ACCESS_STORE, tempStr);
			
			//实例化服务类
			//setSysService = setSysService == null ? new SetSysService() : setSysService ;
			// 提交修改
			if (setSysService.updateSysParam(sysArgs)){
				//重置系统变量,达到更新的目的
				if (oldRecTime == Integer.parseInt(sysArgs.get(BeanForSysArgs.MAX_RS_COUNT_LINE))){
					// 实时曲线的数量 无变化, 不重置 总览&实时曲线关联的数据库
					commonDataUnit.resetSystem(false,false,false,true,false);
				}else{
					// 实时曲线的数量 有变化, 重置 总览&实时曲线关联的数据库
					commonDataUnit.resetSystem(false,false,false,true,true);
					oldRecTime = Integer.parseInt(sysArgs.get(BeanForSysArgs.MAX_RS_COUNT_LINE));
				} 
				tempStr = "系统属性修改成功.";
			}else{
				tempStr = "系统属性修改失败.";
			}
			showTipMsg = 1;
			addActionMessage(tempStr);
		} catch (Exception e) {
			log.error("initSys error", e);
		}
		return SETTING;
	}
	
	/**
	 * @describe: 备份信息列出
	 * @param type: 1:有数量限制		2:无数量限制
	 * @date:2010-1-20
	 */
	@SuppressWarnings("unchecked")
	public void list(int type){
		if (searchKey == null){
			searchKey = new HashMap<String, Object>();
		}
		try {
			if ((startTime!=null)&&(endTime!=null)){
				SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
				searchKey.put("timeStart", dateFormat.parse(startTime + " 0:00:00"));
				searchKey.put("timeEnd", dateFormat.parse(endTime +  " 23:59:59"));
				if (!searchPlace.equals("全部")){
					searchKey.put("mark", searchPlace);
				}
			}		
			
			//实例化服务类
			//setSysService = setSysService == null ? new SetSysService() : setSysService ;
			if (type == 1){	// 1:有数量限制	
				pager = setSysService.getPager("BackUpList", getCurrentPage(),getPagerMethod(), USERPAGESIZE, searchKey, USERPAGESIZE);
			}else{			// 2:无数量限制
				pager = setSysService.getPager("BackUpList", getCurrentPage(),getPagerMethod(), USERPAGESIZE, searchKey);
			}			
			backUpLists = (ArrayList) pager.getElements();
			this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
	}	
	
	// 列出备份列表  -- 初始化时候用
	public String getLists() {
		list(1);
		return BACKUP;
	}
	
	// 列出备份列表 -- 分页和搜索的时候用
	public String getList() {
		list(2);
		return BACKUP;
	}
	
	// 下载历史备份
	public String downfile(){
		System.out.println(downfilename);
		return SUCCESS;
	}
	
	/**
	 * @describe: 截取文件路径,获取文件名部分
	 * @param temp: 文件绝对路径
	 * @date:2009-12-17
	 */
	public String subFileName(String filepath){
        String subStr = filepath.trim();
        subStr = subStr.substring(subStr.lastIndexOf("/")+1);		
		return subStr;
	}		
	
	/**
	 * @describe:	检测access数据库的连通性
	 * @date:2010-3-19
	 */
	public String chkAccess (){
		if (!serviceAccess.testLink()){
			accessnolinkInfo();
			return NODATARETURN;
		}
		if (!serviceSqlServer.testLink()){
			sqlservernolinkInfo();
			return NODATARETURN;
		}
		linkInfo();
		return NODATARETURN;
	}
	
	/**
	 * 检测数据采集和短信模块是否运行正常
	 */
	public String chkSerial(){
		boolean haveSms = showGroup.get(KEY_MSSSAGE); 		// 短信报警是否打开(1:false-关闭 2:true-打开)
		boolean dataState = first_Level.isRunningFlag();	// 数据采集串口是否在运行
		boolean smsState = simCard_Unit.isRunFlag();		// 短信模块是否在运行
		
		if(dataState == false){								// 数据采集如果没有运行,尝试连接一次
			dataState = first_Level.startRunSerial() == 0;	//  0:运行正常
		}
		if (haveSms&&smsState==false){						// 系统开启短信模块的前提下,尝试对短信模块连接一次
			String passport = "";
			if (first_Level.isRunningFlag() == true){
				passport = first_Level.getPortStr();
			}			
			smsState = simCard_Unit.openPort(passport, 9600);
		}
		
		headTitle = "检测状态...";
		headDetail = serialTestInfo(dataState, smsState, haveSms);
		return NODATARETURN;
	}
	
	private void sqlservernolinkInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "sqlserver数据库连接失败...";
		
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
		headDetail = strbuf.toString();
	}	
	
	/**
	 * @describe: access数据库连接失败
	 * @date:2009-12-10
	 */
	private void accessnolinkInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "access数据库连接失败...";
		strbuf.append("建议:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    ①在D:\\eeField\\accessdb目录下,查看DSR_DB.mdb文件是否存在");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ②找到文件,可能存在着系统问题,尝试重启电脑");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ③找不到文件,安装文件夹中找到DSR_DB.mdb(也可以联系我们获取这个文件)," +
				"拷贝到D:\\htc\\accessdb目录下(如果没有这个目录,请先创建),然后重启电脑");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ●以上操作后,仍然连接失败. 有可能软件上存在问题,请尽快联系我们...");
		strbuf.append("</label>");
		strbuf.append("<br/>");
		headDetail = strbuf.toString();
	}
	
	/**
	 * @describe: access数据库连接成功
	 * @date:2009-12-10
	 */
	private void linkInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "测试成功,请放心使用...";
		strbuf.append("<label>");
		strbuf.append("access数据库连接正确...");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("sqlserver数据库连接正确...");
		strbuf.append("</label><br/><br/>");
		headDetail = strbuf.toString();
	}
	
	/**
	 * 串口检测信息
	 * @param dataState 数据采集器是否正常
	 * @param smsState  短信模块是否正常
	 * @param haveSms   系统中是否有短信模块
	 */
	private String serialTestInfo(boolean dataState, boolean smsState, boolean haveSms) {
		StringBuffer strbuf = new StringBuffer();
		
		strbuf.append("<label>");
		strbuf.append(dataState?"数据采集器运行正常...":"数据采集器没有运行...");
		strbuf.append("</label><br/><br/>");
		
		if (haveSms){
			strbuf.append("<label>");
			strbuf.append(smsState?"短信模块运行正常...":"短信模块没有运行");
			strbuf.append("</label><br/><br/>");
		}
		
		if(!dataState || (!smsState && haveSms)){
			strbuf.append("<br/><br/>");
			strbuf.append("<label>");
			strbuf.append("    ●如果没有运行,请按顺序检测. ①检测仪器连接是否正确->②重启电脑->③联系我们...");
			strbuf.append("</label>");
		}
		return strbuf.toString();
	}
	
	// ----------------以下为get，set方法----------------
	// 波特率
	public Map<Integer, String> getBaudRatelList() {
		baudRatelList = getMapSel("gprsbaudRate");
		return baudRatelList;
	}	
	
	// 温度显示类型  1:摄氏,2:华氏
	public Map<Integer,String> getTempshowSel() {
		tempshowSel = getMapSel("systmpshow");
		return tempshowSel;
	}	
	
	// 访问串口间隔
	public Map<Integer, String> getFlashTimeList() {
		flashTimeList = getMapSel("syspicflash");
		return flashTimeList;
	}	
	
	// 报警播放文件	
	public Map<Integer, String> getAlarmFileSel() {
		alarmFileSel = getMapSel("sysalarmfile");
		return alarmFileSel;
	}
	// 历史数据自动备份路径
	public List<String> getBackuppath() {
		backuppath = getList("backuppath"); 
		return backuppath;
	}
	
	// 区域信息
	public Map<Integer, String> getSearchAreaMap() {
		searchAreaMap = manaService.getAllPlace();
		searchAreaMap.put(0, "全部");
		return searchAreaMap;
	}	
	// 短信模块,数据直接保存到串口命令:AT+CNMI=2,2(值为1)或者AT+CNMI=2,1(值为2)	
	public Map<Integer, String> getCnmitype() {
		cnmitype = new HashMap<Integer, String>();
		cnmitype.put(1, "AT+CNMI=2,2");
		cnmitype.put(2, "AT+CNMI=2,1");
		return cnmitype;
	}
	// ----------------普通 get，set方法----------------
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
	
	public List<BackUpList> getBackUpLists() {
		return backUpLists;
	}

	public void setBackUpLists(List<BackUpList> backUpLists) {
		this.backUpLists = backUpLists;
	}

	public Map<String, Boolean> getShowGroup() {
		return showGroup;
	}

	public void setShowGroup(Map<String, Boolean> showGroup) {
		this.showGroup = showGroup;
	}

	public Map<String, String> getSysArgs() {
		return sysArgs;
	}

	public void setSysArgs(Map<String, String> sysArgs) {
		this.sysArgs = sysArgs;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public void setShowTipMsg(int showTipMsg) {
		this.showTipMsg = showTipMsg;
	}

	public void setDownfilename(String downfilename) {
		this.downfilename = downfilename;
	}

	public String getSearchPlace() {
		return searchPlace;
	}

	public void setSearchPlace(String searchPlace) {
		this.searchPlace = searchPlace;
	}

	public String getTimeFromStr() {
		return timeFromStr;
	}

	public void setTimeFromStr(String timeFromStr) {
		this.timeFromStr = timeFromStr;
	}

	public String getTimeToStr() {
		return timeToStr;
	}

	public void setTimeToStr(String timeToStr) {
		this.timeToStr = timeToStr;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getHeadTitle() {
		return headTitle;
	}

	public String getHeadDetail() {
		return headDetail;
	}

	public int getOldRecTime() {
		return oldRecTime;
	}

	public void setOldRecTime(int oldRecTime) {
		this.oldRecTime = oldRecTime;
	}
	
}
