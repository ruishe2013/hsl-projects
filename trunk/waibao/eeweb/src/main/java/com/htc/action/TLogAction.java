package com.htc.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.htc.common.FunctionUnit;
import com.htc.domain.*;
import com.htc.model.MainService;

/**
 * @ TLogAction.java
 * 作用 : 系统日志action. 分页 - 删除 - 批量删除
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class TLogAction extends AbstractActionForOEM {

	// 服务类
	private MainService mainService;

	// 分页
	private Pager pager; 					// 分页类
	private String currentPage="1";			// 当前页
	private String pagerMethod; 			// 页面方法，如：前一页，后一页，第一页，最后一页等.
	private int[] ids; 						// 选中删除的选项(复选)
	
	// 页面显示
	private List<TLog> logList = new ArrayList<TLog>();	// 表格信息列表
	private Map<Integer, String>  logTypeMap;// 日志类型
	private int logId;						// 日志ID
	private boolean initFlag = false;		// 是否是第一次进入页面
	private int maxRecords = 100;      		// 最大记录数
	private int log_type=0;     			// 日志类型
	

	// 查询相关
	private Map<String, Object> searchKey;	// 查询关键字
	private String timeFromStr;				// 搜索开始时间-字符
	private String timeToStr;				// 搜索结束时间-字符
	private String startTime;				// 搜索开始时间
	private String endTime;					// 搜索结束时间	
	
	// 构造方法
	public TLogAction() {
	}
	//注册service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}

	@Override
	public String execute() {
		list(1);
		return SUCCESS;
	}

	// 分页
	public String getList(){
		list(2);
		return SUCCESS;
	}
	
	/**
	 * @describe: 备份信息列出
	 * @param type: 1:固定数量限制		2:页面控制数量
	 * @date:2010-1-20
	 */
	@SuppressWarnings("unchecked")
	public void list(int type){
		initFlag = true;
		if (searchKey == null){
			searchKey = new HashMap<String, Object>();
		}
		try {
			if ((startTime!=null)&&(endTime!=null)){
				SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
				searchKey.put("timeStart", dateFormat.parse(startTime + " 0:00:00"));
				searchKey.put("timeEnd", dateFormat.parse(endTime +  " 23:59:59"));
			}
			searchKey.put("type", log_type);
			// 实例化服务类
			//mainService = mainService == null ? new MainService() : mainService;
			if (type == 1){
				pager = mainService.getPager("TLog", getCurrentPage(),	getPagerMethod(), USERPAGESIZE, searchKey, USERPAGESIZE);
			}else{
				pager = mainService.getPager("TLog", getCurrentPage(),	getPagerMethod(), USERPAGESIZE, searchKey, maxRecords);
			}
			logList = (ArrayList) pager.getElements();
			this.setCurrentPage(String.valueOf(pager.getCurrentPage()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	// 删除
	public String delete() throws Exception {
		
		//实例化服务类
		//mainService = mainService == null ? new MainService() : mainService;
		
		mainService.deleteLogById(logId);
		list(2);
		return SUCCESS;
	}	

	// 批量删除
	public String deleteSelect() {
		
		//实例化服务类
		//mainService = mainService == null ? new MainService() : mainService;		
		
		if (ids != null) {
			mainService.deleteLogBatch(ids);
		}
		list(2);
		return SUCCESS;
	}
	// ----------------以下为get，set方法----------------
	// 日志类型[1:登录信息 2:查询信息 3:串口信息 4:错误信息]
	public Map<Integer, String> getLogTypeMap() {
		logTypeMap = new HashMap<Integer, String>();
		logTypeMap.put(0, "全部");
		logTypeMap.put(1, "登录信息");
		logTypeMap.put(2, "查询信息");
		logTypeMap.put(3, "串口信息");
		logTypeMap.put(4, "错误信息");
		logTypeMap.put(5, "短信报警");
		return logTypeMap;
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

	public int[] getIds() {
		return ids;
	}

	public void setIds(int[] ids) {
		this.ids = ids;
	}

	public List<TLog> getLogList() {
		return logList;
	}

	public void setLogList(List<TLog> logList) {
		this.logList = logList;
	}

	public int getMaxRecords() {
		return maxRecords;
	}

	public void setMaxRecords(int maxRecords) {
		this.maxRecords = maxRecords;
	}

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
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

	public boolean isInitFlag() {
		return initFlag;
	}

	public int getLog_type() {
		return log_type;
	}

	public void setLog_type(int log_type) {
		this.log_type = log_type;
	}

}
