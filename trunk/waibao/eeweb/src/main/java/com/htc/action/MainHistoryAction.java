package com.htc.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import com.googlecode.jsonplugin.annotations.JSON;
import com.htc.bean.BeanForFlashSetting;
import com.htc.bean.BeanForRecord;
import com.htc.bean.BeanForSearchRecord;
import com.htc.bean.BeanForSysArgs;
import com.htc.bean.BeanForZjHisRec;
import com.htc.common.FunctionUnit;
import com.htc.domain.EquipData;
import com.htc.domain.TLog;
import com.htc.domain.ZjHistory;
import com.htc.model.MainService;
import com.htc.model.ServiceSqlServer;

/**
 * @ MainHistoryAction.java
 * 作用 : 查询历史数据action. 
 * 		显示类型: 以表格和flash的形式的.
 * 		查询类型：即时数据, 日报表, 月报表
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class MainHistoryAction extends AbstractAction {

	// 服务类
	private MainService mainService;
	private ServiceSqlServer serviceSqlServer;

	// 对查询数据根据预定范围显示不同的CSS class样式;
	private String LOWDATACSS 	 = "lowdata";
	private String NORMALDATACSS = "normaldata";
	private String HIGHDATACSS 	 = "highdata";

	// json和table共用
	private int[] statBox;						// 统计类型 -- 复选框 日报,月报页面的统计类型
	
	// flash返回用
	private String tempDataStr;					// 温度数据字符				
	private String tempConfigStr;				// 温度配置字符				
	private String humiDataStr;					// 湿度数据字符			
	private String humiConfigStr;				// 温度配置字符
	private StringBuffer tempDataBuf =
		new StringBuffer();						// 缓存flash温度数据
	private StringBuffer humiDataBuf = 
		new StringBuffer();						// 缓存flash湿度数据
	
	// table返回用
	private String strJspTableHeader; 			// table内容首行
	private String strJspTable; 				// table内容 -- 不用
	private List<String> jsptableList;			// table内容 -- 用list
	private Date firstDate;						// 搜索的时候,保存的第一个日期
	private Date lastDate;						// 搜索的时候,保存的最后的日期

	// 页面数据
	private Map<Integer,String> searchInterval;	// 搜索间隔
	private Map<String, String> areaToEqList;	// 页面地址选择用的map(区域名,区域中的仪器列表)	
	private int showType;						// 页面显示类型(1:报表 2:曲线)
	private int maxSelector;					// 页面最多能显示的地址数量	
	private int max_rs_now;						// 页面最多能显示的时间区间-即时
	private int max_rs_day;						// 页面最多能显示的时间区间-日报	
	private int max_rs_month;					// 页面最多能显示的时间区间-月报	

	// 页面传递的参数
	private String placeListids="";				// 选中的列表
	private String timeFrom="";					// 开始时间
	private String timeTo="";					// 结束时间
	private int interval;						// 时间间隔 
	
	// 查询药监数据用
	private	String tempDown = "0";				// 温度下限
	private	String tempUp = "40"; 				// 温度上限
	private	String humiDown = "0";				// 湿度下限
	private	String humiUp = "100";				// 湿度上限
	
	// 搜索标题-细节
	private String headTitle;					// 格式:公司名 -[历史数据]查询结果 
	private String headDetail;					// 格式:查询类型: 日报表 查询时间范围: 2009年11月06日 12时~ 2009年11月06日 23时
	
	// 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	@SuppressWarnings("unused")
	private int systemEqType;		
	
	
	// 构造方法
	public MainHistoryAction() {
	}
	//注册service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}	
	public void setServiceSqlServer(ServiceSqlServer serviceSqlServer) {
		this.serviceSqlServer = serviceSqlServer;
	}
	
	@Override
	public String execute() {
		//页面地址选择用的map(区域名,区域中的仪器列表)
		//areaToEqList = CommonDataUnit.createEqAreaWithNOSel(null);	
		areaToEqList = commonDataUnit.createEqAreaWithNOSel(null);	
		// 页面最多能显示的地址数量
		if (showType == 1){	// 报表
			maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_TABLE));
			max_rs_now = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_TABLE_NOW));
			max_rs_day = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_TABLE_DAY));
			max_rs_month = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_TABLE_MONTH));
		}else if (showType == 2){	// 曲线
			maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_FLASH));
			max_rs_now = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_FLASH_NOW));
			max_rs_day = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_FLASH_DAY));
			max_rs_month = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_FLASH_MONTH));
		}
		return SUCCESS;
	}
	
	/**
	 * @describe: 增加到日志
	 * @param typeStr 1:即时数据 2:日报 3:月报 4:即时曲线 5:日曲线 6:月曲线
	 * @date:2009-12-22
	 */
	public void insertToLog(String typeStr, long runtime){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(getSysUserName()+":查询");
		strbuf.append(typeStr);
		strbuf.append("  ");
		strbuf.append("时间段:");
		strbuf.append(timeFrom);
		strbuf.append("~");
		strbuf.append(timeTo);
		strbuf.append("-->后台用时:" + runtime + "毫秒");
		//MainService mainService = new MainService();
		mainService.packTlog(TLog.SEARCH_LOG, strbuf.toString());
	}
	
	// 流水曲线 -json
	public String freeTimeJson() {
		long start = System.currentTimeMillis();
		int statInt = 0; // 从页面获取max(1000),avg(100),min(10)的值的总和	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		boolean haveData = false;
		try {
			haveData = searchForJson(statInt, MainService.TYPE_RECENT, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (haveData){
			start = System.currentTimeMillis() - start ;
			insertToLog("流水曲线",start);
			return PRINTOUT;
		}else{
			nodataInfo(1);
			return NODATARETURN;
		}	
	}

	// 日曲线-  -json
	public String dailyJson() {
		long start = System.currentTimeMillis();
		int statInt = 0; // 从页面获取max(1000),avg(100),min(10)的值的总和	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		boolean haveData = false;
		try {
			interval = 1; // 日报曲线间隔为1
			haveData = searchForJson(statInt, MainService.TYPE_DAILY, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (haveData){
			start = System.currentTimeMillis() - start ;
			insertToLog("日曲线",start);
			return PRINTOUT;
		}else{
			nodataInfo(1);
			return NODATARETURN;
		}	
	}

	// 月报曲线 -json
	public String monthJson() {
		long start = System.currentTimeMillis();
		int statInt = 0; // 从页面获取max(1000),avg(100),min(10)的值的总和	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		boolean haveData = false;
		try {
			interval = 1; // 月报曲线间隔为1
			haveData = searchForJson(statInt, MainService.TYPE_MONTH, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (haveData){
			start = System.currentTimeMillis() - start ;
			insertToLog("月曲线", start);
			return PRINTOUT;
		}else{
			nodataInfo(1);
			return NODATARETURN;
		}	
	}

	/**
	 * @describe: 没有数据时显示的信息
	 * @param type: 1:曲线 2:报表
	 * @date:2009-12-10
	 */
	private void nodataInfo(int type) {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "搜索不到历史" + (type==1?"曲线":"报表")+"...";
		strbuf.append("建议:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    ●调整开始时间和结束时间");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ●选择适当的仪器");
		strbuf.append("</label>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("*如果调整后,仍然没有查询结果,那么这段时间内确实没有数据记录存在...");		
		headDetail = strbuf.toString();
	}

	// 流水记录 - table
	public String freeTime() {
		long start = System.currentTimeMillis();
		int statInt = 0; // 从页面获取max(1000),avg(100),min(10)的值的总和	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		boolean haveData = false;
		try {
			haveData = searchForTable(statInt, MainService.TYPE_RECENT, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (haveData){
			setInfoForTable(MainService.TYPE_RECENT );
			start = System.currentTimeMillis() - start ;
			insertToLog("流水记录",start);
			return PRINTOUT;
		}else{
			nodataInfo(2);
			return NODATARETURN;
		}	
	}

	// 日报表 - table
	public String daily() {
		long start = System.currentTimeMillis();
		int statInt = 0; // 从页面获取max(1000),avg(100),min(10)的值的总和	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		interval = 1; // 日报表间隔为1
		boolean haveData = false;
		try {
			haveData = searchForTable(statInt, MainService.TYPE_DAILY, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (haveData){
			setInfoForTable(MainService.TYPE_DAILY );
			start = System.currentTimeMillis() - start ;
			insertToLog("日报表",start);
			return PRINTOUT;
		}else{
			nodataInfo(2);
			return NODATARETURN;
		}
	}

	// 月报表 - table
	public String month() {
		long start = System.currentTimeMillis();
		int statInt = 0; // 从页面获取max(1000),avg(100),min(10)的值的总和	
		for (int i = 0; i < statBox.length; i++) {
			statInt += statBox[i];
		}
		interval = 1; // 月报表间隔为1
		boolean haveData = false;
		try {
			haveData = searchForTable(statInt, MainService.TYPE_MONTH, interval);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (haveData){
			start = System.currentTimeMillis() - start ;
			insertToLog("月报表",start);
			setInfoForTable(MainService.TYPE_MONTH );
			return PRINTOUT;
		}else{
			nodataInfo(2);
			return NODATARETURN;
		}
	}
	
	/**
	 * @describe: 根据统计类型和搜索结果,返回页面显示信息	
	 * @param selectType 统计类型(MainService.TYPE_RECENT:即时数据 
	 * 							   MainService.TYPE_DAILY:代表日报类型
	 * 							   MainService.TYPE_MONTH:代表月报类型) 
	 * @return:
	 * @date:2009-12-10
	 */
	public void setInfoForTable(int statType){
		String tempStr = "";
		StringBuffer strbuf = new StringBuffer();
		
		// headTitle格式:公司名 -[历史数据]查询结果 
		// headDetail格式:查询类型: 日报表 	时间范围: 2009年11月06日 12时~ 2009年11月06日 23时		
		//strbuf.append(CommonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME));//公司名
		strbuf.append(commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME));//公司名
		strbuf.append("-历史数据查询结果 ");
		headTitle = strbuf.toString();
		
		strbuf.delete(0, strbuf.toString().length() - 1);
		tempStr = statType==MainService.TYPE_RECENT?"流水报表":
				statType==MainService.TYPE_DAILY?"日报表":"月报表";
		strbuf.append("<label>查询类型:" + tempStr +"</label>");
		// 统计类型
		strbuf.append("<label>统计类型: ");
		for (int i = 0; i < statBox.length; i++) {
			if (statBox[i] == MainService.TYPE_MIN_STAT){
				strbuf.append(" 最小值");		
			}else if (statBox[i] == MainService.TYPE_AVG_STAT){
				strbuf.append(" 平均值");	
			}else if (statBox[i] == MainService.TYPE_MAX_STAT){
				strbuf.append(" 最大值");	
			}
		}			
		strbuf.append("</label>");
		tempStr = statType==MainService.TYPE_RECENT?"<label>间隔:"+interval+"分钟</label>":"";
		strbuf.append(tempStr);
		strbuf.append("<br/>");
		tempStr = getDateStringByStat(firstDate, statType);
		strbuf.append("<label>时间范围:" + tempStr);
		tempStr = getDateStringByStat(lastDate, statType);
		strbuf.append("~" + tempStr);
		strbuf.append("</label>");
		
		headDetail = strbuf.toString();
	}

	/**
	 * @describe: 根据统计类型格式化时间	
	 * @param date 要格式化的时间
	 * @param statType 统计类型(MainService.TYPE_RECENT:即时数据 
	 * 							   MainService.TYPE_DAILY:代表日报类型
	 * 							   MainService.TYPE_MONTH:代表月报类型) 
	 * @return:
	 * @date:2009-12-10
	 */
	private String getDateStringByStat(Date date, int statType) {
		String dateStr = "";
		if(statType == MainService.TYPE_RECENT){//如: 2009年05月01日 05时12分56秒
			dateStr = FunctionUnit.getDateToStr(date,
					FunctionUnit.Calendar_END_SECOND, FunctionUnit.SHOW_CHINESE);
		}else if(statType == MainService.TYPE_DAILY){//如:2009年05月01日
			dateStr = FunctionUnit.getDateToStr(date,
					FunctionUnit.Calendar_END_HOUR, FunctionUnit.SHOW_CHINESE);
		}else if(statType == MainService.TYPE_MONTH){//如:2009年05月
			dateStr = FunctionUnit.getDateToStr(date,
					FunctionUnit.Calendar_END_DAY, FunctionUnit.SHOW_CHINESE);
		}
		return dateStr;
	}

	/**
	 * @describe :根据type(统计类型),产生flash数据原素
	 * @param type :统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,
	 *            还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @param selectType : 设置搜索数据类型(	MainService.TYPE_RECENT:即时数据 
	 * 										MainService.TYPE_DAILY:代表日报类型
	 * 										MainService.TYPE_MONTH:代表月报类型)
	 * @param interval : 数据查询的间隔.目前只有流水报表中使用这个变量 
	 * @throws Exception:数据库异常,以及所有异常
	 * @date:2009-11-2
     */	
	public boolean searchForJson(int type, int selectType,int interval) throws Exception {
		boolean  rsBool = false;
		
		// 实例化服务类
//		if (mainService == null) {
//			mainService = new MainService();
//		}
		// 去掉最后一个逗号,js中已经处理
		// placeListids = placeListids.equals("")?"0":placeListids.substring(0, placeListids.length()-1);		
		// 搜索记录 
		List<BeanForRecord> recordBeanListRs = mainService.searchRecords(fillRecordSearchBean(placeListids,
				timeFrom, timeTo, selectType, 1));

		// 对搜索结果，进行分析,并包装成页面显示数据
		// table元素
		if (recordBeanListRs.size() > 0) {
			rsBool = true;
			// 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
			int sysEqType = getSystemEqType();					
			
			// :分析,并包装flash数据
			package_Data(sysEqType, filldataWithDate(recordBeanListRs), placeListids, type, selectType, interval, 2);
			// 填充温湿度数据字符
			tempDataStr = tempDataBuf.toString();		// 温度数据字符
			humiDataStr = humiDataBuf.toString();		//  湿度数据字符
			// 填充温湿度配置字符
			flashConfig(placeListids, getFlashTitle(type, selectType));
		}
		
		// 清理缓存
		recordBeanListRs = null;
		return rsBool;
	}
	
	/**
	 * @describe:	获取flash配置 -- 没有上下限线条, 有放大功能(填充温湿度配置字符)
	 * @param userPlaceList 选择的地址列表
	 * @param headerStr 温湿度曲线标题
	 * @return:
	 * @date:2009-12-8
	 */
	public void flashConfig(String userPlaceList, String headerStr){
		List<String> equipLabels = new ArrayList<String>();		// 仪器标签列表				
		
		// 获取选中列表的仪器主键数组	-- 准备工作
		String[] strArray = userPlaceList.split(",");
		int[] equipmentIds = new int[strArray.length];		
		for (int i = 0; i < strArray.length; i++) {
			equipmentIds[i] = Integer.parseInt(strArray[i].trim());
		}
		// 获取显示类型 -- 准备工作
		//int tempType = Integer.parseInt(CommonDataUnit.
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // 温度显示类型(温度显示格式(1:摄氏 2:华氏))
		// 获取仪器标签列表 -- 准备工作
		for (Integer equipId: equipmentIds) {
			//equipLabels.add(CommonDataUnit.getEquiMapStrById(equipId));		
			equipLabels.add(commonDataUnit.getEquiMapStrById(equipId));		
		}
		
		// 温湿度 -- 加载配置区域
		BeanForFlashSetting configbean = new BeanForFlashSetting();
		configbean.redraw = true;									// 开启自适应大小(默认是关闭的)
		configbean.zoomable_use = true;								// 使用放大功能
		configbean.grid_x_show_count = 10 ;							// X轴显示10标签
		configbean.bullet_size = 4 ;								// 气球圆点大小改为4
		configbean.flashdata = equipLabels ;						// flsh包含图例的List列表
		configbean.label_name = headerStr;							// flash标签头
		
		// 温度曲线
		configbean.y_value_append = tempType == 1 ? "℃" : "F";	// Y轴附加标签
		configbean.legend_value_append = tempType == 1 ? "℃" : "F";	
		
		// 得到温度曲线配置字符
		tempConfigStr = configbean.toString();		
		
		// 湿度曲线
		configbean.y_value_append = "%";// Y轴附加标签
		configbean.legend_value_append = "%RH";
		// 得到湿度曲线配置字符
		humiConfigStr = configbean.toString();										
	}		
	
	/**
	 * @describe:	得到flash搜索时的头部<br>
	 * 格式:查询类型: 流水记录/日报表/月报表   		统计类型: 最小值/平均值/最大值		查询时间范围: fromtime ~ endtime<br>
	 * @param type :统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,
	 *            还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @param selectType : 设置搜索数据类型(	MainService.TYPE_RECENT:即时数据 
	 * 										MainService.TYPE_DAILY:代表日报类型
	 * 										MainService.TYPE_MONTH:代表月报类型)
	 * @date:2009-11-12
	 */
	private String getFlashTitle(int type, int selectType) {
		// 格式:查询类型: /日报表/月报表   		统计类型: 最小值/平均值/最大值		查询时间范围: fromtime ~ endtime
		StringBuffer strbuf = new StringBuffer();
		String startTimeStr = "", endTimeStr = "", another = "";
		
		strbuf.append("查询类型: ");
		if (selectType == MainService.TYPE_RECENT){
			strbuf.append("流水曲线		");
			another = "		间隔" + interval + "分钟";
		}else if(selectType == MainService.TYPE_DAILY){
			strbuf.append("日报曲线		");			
		}else if(selectType == MainService.TYPE_MONTH){
			strbuf.append("月报曲线		");			
		}
		startTimeStr = getDateStringByStat(firstDate,selectType);
		endTimeStr = getDateStringByStat(lastDate,selectType);
		
		// 统计类型
		if (type == MainService.TYPE_NO_STAT){	// 不加
		}else if (type == MainService.TYPE_MIN_STAT){
			strbuf.append("统计类型: ");
			strbuf.append("最小值	");			
		}else if (type == MainService.TYPE_AVG_STAT){
			strbuf.append("统计类型: ");
			strbuf.append("平均值	");			
		}else if (type == MainService.TYPE_MAX_STAT){
			strbuf.append("统计类型: ");
			strbuf.append("最大值	");			
		}
		
		strbuf.append("实际查询时间范围: ");
		strbuf.append(startTimeStr + " ~ " + endTimeStr + another);
		
		return strbuf.toString();
	}
	
	/**
	 * @describe :根据type(统计类型),产生table原素
	 * @param type :统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,
	 *            还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @param selectType : 设置搜索数据类型(	MainService.TYPE_RECENT:即时数据 
	 * 										MainService.TYPE_DAILY:代表日报类型
	 * 										MainService.TYPE_MONTH:代表月报类型)
	 * @param interval : 数据查询的间隔.目前只有流水报表中使用这个变量 
	 * @return true:有数据返回  false:无数据返回 
	 * @throws Exception:数据库异常,所有异常
	 * @date:2009-11-2
     */
	public boolean searchForTable(int type, int selectType, int interval) throws Exception {
		boolean  rsBool = false;
		// 实例化服务类
//		if (mainService == null) {
//			mainService = new MainService();
//		}
		
		// 去掉最后一个逗号,js中已经处理
		//placeListids = placeListids.equals("")?"0":placeListids.substring(0, placeListids.length()-1);		
		// 搜索记录 
		List<BeanForRecord> recordBeanListRs = mainService.searchRecords(fillRecordSearchBean(placeListids,
				timeFrom, timeTo, selectType, 1));
		
		// 对搜索结果，进行分析,并包装成页面显示数据
		// table元素
		if (recordBeanListRs.size() > 0) {
			// 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
			int sysEqType = getSystemEqType();	
			
			rsBool = true;	// 有数据返回
			// 1:分析,并包装数据
			jsptableList = package_Data(sysEqType, filldataWithDate(recordBeanListRs), placeListids, type, selectType, interval,1);
			// 2:页面显示的地址列表--显示在第一行
			strJspTableHeader = packageToTableHeader(sysEqType, placeListids, type);
		}
		// 清理缓存
		recordBeanListRs = null;
		return rsBool;
	}

	/**
	 * @describe: 搜索到数据,设置数据的标题
	 * @param sysEqType : 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	 * @param placeListids : 要显示的地址列表,逗号隔开的数字字符串.如：1,2,3,5
	 * @param type :统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,
	 *            还有TYPE_MIN_STAT和TYPE_AVG_STAT) TYPE_RECENT时<td colspan="2">
	 *            ,其余的<td colspan="2或4或6">
	 * @return: jsp页面可第一个<tr></tr>集合的字符串组合
	 * @date:2009-11-2
     */
	private String packageToTableHeader(int sysEqType, String placeListids, int type) {
		StringBuffer strBufA = new StringBuffer();
		StringBuffer strBufB = new StringBuffer();
		String[] strArray = placeListids.split(",");
		int intTemp;
		String strTemp = null;
		String str_Temp = "";
		String str_Humi = "";
		
		// 获取显示类型 -- 准备工作
		//int tempType = Integer.parseInt(CommonDataUnit.
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // 温度显示类型(温度显示格式(1:摄氏 2:华氏))
		String tempTypeShowStr = tempType==1?"℃":"F";

		// 根据类型type,设定td元素需要占的列数
		intTemp = 0;
		int colspan = sysEqType == 1 ? 2 : 1;
		if (type >= MainService.TYPE_MAX_STAT) { // 最大值,日报和月报用
			type = type - MainService.TYPE_MAX_STAT;
			intTemp += colspan;
			str_Temp += sysEqType != 3?"<td>最大温度(" + tempTypeShowStr + ")</td>":"";
			str_Humi += sysEqType != 2?"<td>最大湿度(%RH)</td>":"";
		}
		if (type >= MainService.TYPE_AVG_STAT) { // 平均值,日报和月报用
			type = type - MainService.TYPE_AVG_STAT;
			intTemp += colspan;
			str_Temp += sysEqType != 3?"<td>平均温度(" + tempTypeShowStr + ")</td>":"";
			str_Humi += sysEqType != 2?"<td>平均湿度(%RH)</td>":"";
		}
		if (type >= MainService.TYPE_MIN_STAT) { // 最小值,日报和月报用
			type = type - MainService.TYPE_MIN_STAT;
			intTemp += colspan;
			str_Temp += sysEqType != 3?"<td>最小温度(" + tempTypeShowStr + ")</td>":"";
			str_Humi += sysEqType != 2?"<td>最小湿度(%RH)</td>":"";
			type = 0;		//保证不进入下一个if
		}
		if (type >= MainService.TYPE_NO_STAT) { // 不统计只显示数据,即时数据用
			type = type - MainService.TYPE_NO_STAT;
			intTemp += colspan;
			str_Temp += sysEqType != 3?"<td>温度(" + tempTypeShowStr + ")</td>":"";
			str_Humi += sysEqType != 2?"<td>湿度(%RH)</td>":"";
		}

		// 列举 仪器标识串
		strBufA.append("<td rowspan=2>NO</td><td rowspan=2>记录时间</td>");
		Map<Integer, String> placeList = getPlaceList();
		for (int i = 0; i < strArray.length; i++) {
			// 仪器标识串
			strTemp = placeList.get(Integer.parseInt(strArray[i].trim()));
			strBufA.append("<td colspan=\"" + intTemp + "\">");
			strBufA.append(strTemp + "</td>");
			strBufB.append(str_Temp + str_Humi);
		}

		return strBufA.toString() + "</tr><tr class=\"top\">"+ strBufB.toString();
	}
	
	/**
	 * @describe: 返回按时间排列的结果集
	 * @param recordBeanListRs : 要分析的数据集
	 * @date:2009-12-7
	 */
	private Map<Date, Map<Integer, BeanForRecord>> filldataWithDate(List<BeanForRecord> recordBeanListRs){
		
		Map<Date, Map<Integer, BeanForRecord>> sortStore = 
			new LinkedHashMap<Date, Map<Integer,BeanForRecord>>();					// 按时间排序的结果集
		Map<Integer, BeanForRecord> tempStore = new HashMap<Integer, BeanForRecord>();	// 临时数据集(sortStore中的元素)
		
		// recordBeanListRs结果是按照时间来升序排列的,因此可以根据时间来分类
		int equipmentId = 0 ;
		Date currentDate = null; // 数据集遍历当前时间
		Date preDate = null; // 数据集遍历前一个时间
		for (BeanForRecord recordBean : recordBeanListRs) {
			equipmentId = recordBean.getEquipmentId();
			currentDate = recordBean.getRecTime();	// 遍历当前时间
			
			// 重复Id时,且时间不同时,进行 内存转化,即保存目标数据 
			// 如果由于修改系统时间,导致同一时刻有两条记录.这种情况下,取后面的数据,忽略前面的数据
			if (tempStore.containsKey(equipmentId)){
				if (currentDate.compareTo(preDate)!=0){
					Map<Integer, BeanForRecord> tempStore_ = new HashMap<Integer, BeanForRecord>();
					for (int equipId : tempStore.keySet()) {	// 转化空间
						tempStore_.put(equipId, tempStore.get(equipId));
					}// end for
					sortStore.put(preDate, tempStore_);
					tempStore.clear();
				}
			}	
			
			tempStore.put(equipmentId, recordBean);
			preDate = recordBean.getRecTime();	// 遍历前一个时间
		}
		
		// 加上最后一个数据集
		Map<Integer, BeanForRecord> tempStore_ = new HashMap<Integer, BeanForRecord>();
		for (int equipId : tempStore.keySet()) {	// 转化空间
			tempStore_.put(equipId, tempStore.get(equipId));
		}// end for
		sortStore.put(currentDate, tempStore_);		
		tempStore = null;
		
		return sortStore;
	}
	
	/**
	 * @describe: 对数据(即时数据)进行分析,并包装成jsp页面可以显示的<tr>...</tr>集合
	 * @param sysEqType : 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	 * @param sortStore : 按时间排列打包的数据集
	 * @param placeListids : 要显示的地址列表,逗号隔开的数字字符串.如：1,2,3,5
	 * @param type :统计类型.(TYPE_NO_STAT:t没有统计,TYPE_MAX_STAT:统计最大值,
	 *            还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @param selectType : 设置搜索数据类型( MainService.TYPE_RECENT:即时数据	
	 * 			MainService.TYPE_DAILY:代表日报类型	MainService.TYPE_MONTH:代表月报类型)             
	 * @param interval : 数据查询的间隔.目前只有流水报表中使用这个变量 
	 * @param tableOrflash : 1: table调用,2: flash调用
	 * @return jsp页面可以显示的<tr></tr>集合的字符串组合
	 * @throws Exception 
	 * @date:2009-10-31
	 */
	private List<String> package_Data(int sysEqType, Map<Date, Map<Integer, BeanForRecord>> sortStore, String placeListids, 
			int type,  int selectType, int interval, int tableOrflash) throws Exception {
		
		//StringBuffer strBuf = new StringBuffer();					// 返回结果的buffer -- 不用
		List<String> rsList = new CopyOnWriteArrayList<String>();	// 返回结果的List
		
		// 获取时间的格式			-- 准备工作
		int calendar_type = 0;
		if(selectType == MainService.TYPE_RECENT){		//	即时数据 
			calendar_type = FunctionUnit.Calendar_END_SECOND ;
			interval = interval *60;
		}else if(selectType == MainService.TYPE_DAILY){	//	日报类型
			calendar_type = FunctionUnit.Calendar_END_HOUR ;
		}else if(selectType == MainService.TYPE_MONTH){	//	月报类型
			calendar_type = FunctionUnit.Calendar_END_DAY ;
		}
		
		// 获取选中列表的仪器主键数组	-- 准备工作
		String[] strArray = placeListids.split(",");
		int[] equipmentIds = new int[strArray.length];		
		for (int i = 0; i < strArray.length; i++) {
			equipmentIds[i] = Integer.parseInt(strArray[i].trim());
		}
		
//		for (Date temp_Date : sortStore.keySet()) {	// 外层数据
//			for (Integer equipId : sortStore.get(temp_Date).keySet()) { // 内层数据
//				System.out.println(equipId+":"+temp_Date.toLocaleString()+":"+sortStore.get(temp_Date).get(equipId).getHumiAvg());
//			}
//		}
		
		// 获取显示类型 -- 准备工作
		//int tempType = Integer.parseInt(CommonDataUnit.
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // 温度显示类型(温度显示格式(1:摄氏 2:华氏))
		
		// 开始刷选数据
		String tempStr = null;	// 临时存放结果数
		Date nextDate = null;	// 临时时间 - 保存下个时间
		Date currentDate = null;		// 临时时间 - 保存当前时间
		int rowCount = 1;		// 用来显示行号的
		Map<Integer, BeanForRecord> tempBean = new HashMap<Integer, BeanForRecord>();
		boolean tempBool = selectType == MainService.TYPE_RECENT;// 数据是否是即使数据
		
		Set<Entry<Date, Map<Integer, BeanForRecord>>> entrys = sortStore.entrySet();// 键值对(外层)
		Date temp_Date; // 键-时间(外层)
		Map<Integer, BeanForRecord> value; // 值(外层)
		int equipId ;// 键-仪器ID(内层)
		BeanForRecord bean = null; // 值(内层)
        for(Entry<Date, Map<Integer, BeanForRecord>> entry: entrys){
        	temp_Date = null; temp_Date = entry.getKey();
        	value = null; 	value = entry.getValue();
        	lastDate = temp_Date;	// 获取最后一个日期
        	if (nextDate == null){		// 获取比较的时间段
        		firstDate = temp_Date;	// 搜索的时候,先取第一个日期,后面用来比较	
        		currentDate = temp_Date;
        		nextDate = FunctionUnit.nextTime(temp_Date, interval, calendar_type).getTime();
        	}else{
        		while (nextDate.after(temp_Date) == false) {
        			// 把日期相同放入缓存,直到下一个不相等的时间,然后把这些缓存处理变成<tr></tr>元素 或者flash元素
        			tempStr = FunctionUnit.getDateToStr(currentDate, 
        					(calendar_type == FunctionUnit.Calendar_END_SECOND?FunctionUnit.Calendar_END_MINUTE : calendar_type)
        					,FunctionUnit.UN_SHOW_CHINESE);	// 时间字符串
        			
        			if (tableOrflash == 1){	// table 时调用
        				tempStr = makeTrElement(sysEqType, rowCount, tempBean, equipmentIds, type, tempStr);
        				rsList.add(tempStr);
        				//strBuf.append(tempStr);
        			}else if (tableOrflash == 2) { //flash 时调用
        				makeFlashBuf(tempBean, equipmentIds, type, tempStr);
        			}
        			tempBean.clear(); tempBean = null;
        			tempBean = new HashMap<Integer, BeanForRecord>();
        			currentDate = nextDate;
        			nextDate = FunctionUnit.nextTime(nextDate, interval, calendar_type).getTime();
        			rowCount ++;
				}
        	}
	        for(Entry<Integer, BeanForRecord> entry_: value.entrySet()){
	            equipId= entry_.getKey();
	            bean = null;	bean = entry_.getValue();
				if (tempBean.containsKey(equipId)){		// 重新调整各数据最大,最小,平均赋值
					// 最大温度
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getTemperature():bean.getTempMax()),
							tempBean.get(equipId).getTempMax(),FunctionUnit.STAT_MAX, tempType);
					tempBean.get(equipId).setTempMax(tempStr);
					// 最小温度
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getTemperature():bean.getTempMin()),
							tempBean.get(equipId).getTempMin(),FunctionUnit.STAT_MIN, tempType);					
					tempBean.get(equipId).setTempMin(tempStr);
					// 平均温度
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getTemperature():bean.getTempAvg()),
							tempBean.get(equipId).getTempAvg(),FunctionUnit.STAT_AVG, tempType);					
					tempBean.get(equipId).setTempAvg(tempStr);
					
					// 最大湿度
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getHumidity():bean.getHumiMax()),
							tempBean.get(equipId).getHumiMax(),FunctionUnit.STAT_MAX, 0);					
					tempBean.get(equipId).setHumiMax(tempStr);					
					// 最小湿度
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getHumidity():bean.getHumiMin()),
							tempBean.get(equipId).getHumiMin(),FunctionUnit.STAT_MIN, 0);					
					tempBean.get(equipId).setHumiMin(tempStr);
					// 平均湿度
					tempStr = FunctionUnit.getStatFloat((tempBool?bean.getHumidity():bean.getHumiAvg()),
							tempBean.get(equipId).getHumiAvg(),FunctionUnit.STAT_AVG, 0);					
					tempBean.get(equipId).setHumiAvg(tempStr);					
				}else{
					if(tempBool){// 即时数据 
						tempStr = bean.getTemperature(); // 给即时数据最大,最小,平均赋值
						bean.setTempMax(tempStr);	bean.setTempMin(tempStr);	bean.setTempAvg(tempStr);
						tempStr = bean.getHumidity();
						bean.setHumiMax(tempStr);	bean.setHumiMin(tempStr);	bean.setHumiAvg(tempStr);
					}
					if (tempType == 2){// 温度显示格式(1:摄氏 2:华氏)
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( Float.parseFloat(bean.getTempAvg()) *9/5 + 32);
						bean.setTempAvg(tempStr);			
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( Float.parseFloat(bean.getTempMax()) *9/5 + 32);
						bean.setTempMax(tempStr);
						tempStr = FunctionUnit.FLOAT_DATA_FORMAT.format( Float.parseFloat(bean.getTempMin()) *9/5 + 32);
						bean.setTempMin(tempStr);
					}
					tempBean.put(equipId, bean);
					
				}// if (tempBean.containsKey(equipId))
	        }// for 内层数据
        }// for 外层数据
		
		// for循环结束后,还有最后一次没加上去,这里补上
		tempStr = FunctionUnit.getDateToStr(lastDate, 
				(calendar_type == FunctionUnit.Calendar_END_SECOND?FunctionUnit.Calendar_END_MINUTE : calendar_type), 
				FunctionUnit.UN_SHOW_CHINESE);	// 时间字符串
		
		if (tableOrflash == 1){	// table 时调用
			tempStr = makeTrElement(sysEqType, rowCount, tempBean, equipmentIds, type, tempStr);
			rsList.add(tempStr);
			//strBuf.append(tempStr);
		}else if (tableOrflash == 2) { //flash 时调用
			makeFlashBuf(tempBean, equipmentIds, type, tempStr);
		}
		sortStore.clear();	sortStore = null; tempBean.clear();	tempBean = null; 
		//return strBuf.toString();
		return rsList;
	}
	
	/**
	 * @describe: 填充flash温度需要的数据	
	 * @param store 要处理的数据集合 
	 * @param equipmentIds 要显示的地址列表
	 * @param type :统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,
	 *            还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @param dateStr:时间字符串
	 * @date:2009-12-8
	 */
	private void makeFlashBuf(Map<Integer, BeanForRecord> store, int[] equipmentIds, 
			int type, String dateStr){
		BeanForRecord recordBean;
		StringBuffer temp_buf = new StringBuffer();
		temp_buf.append(dateStr);
		temp_buf.append(";");
		
		StringBuffer humi_buf = new StringBuffer();
		humi_buf.append(dateStr);
		humi_buf.append(";");
		
		// 根据地址列表,选出相关的温湿度数据
		for (int i = 0; i < equipmentIds.length; i++) {
			if (store.containsKey(equipmentIds[i])) {
				recordBean = null;
				recordBean = store.get(equipmentIds[i]);
				//过滤不正常数据
				if (recordBean.getEquipmentId() <= 0) {
					temp_buf.append(";");
					humi_buf.append(";");
				}else { // 正常数据解析
					temp_buf.append(makeFlashPoint(recordBean, type, 1));
					temp_buf.append(";");
					humi_buf.append(makeFlashPoint(recordBean, type, 2));
					humi_buf.append(";");
				}
			} else {
				temp_buf.append(";");
				humi_buf.append(";");
			}
		}// end for
		
		// 一个时间结束,加换行符.去掉最后一个分号
		tempDataBuf.append(temp_buf.substring(0, temp_buf.length()-1));
		tempDataBuf.append("\n");
		humiDataBuf.append(humi_buf.substring(0, humi_buf.length()-1));
		humiDataBuf.append("\n");
	}
	

	/**
	 * @describe: 根据统计类型,获取温度或者湿度的统计值	
	 * @param recordBean 分析的数据集
	 * @param type :统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,
	 *            还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @param tempOrhumi: 1: 温度	2: 湿度
	 * @date:2009-12-8
	 */
	private String makeFlashPoint(BeanForRecord recordBean, int type, int tempOrhumi) {
		//EquipData equip = CommonDataUnit.getEquipByID(recordBean.getEquipmentId());
		EquipData equip = commonDataUnit.getEquipByID(recordBean.getEquipmentId());
		String tempStr="" ,humiStr="";
		if (equip == null) {
			return tempStr;
		}
		// eqType(1,'温湿度';2,'单温度';3,'单湿度')
		int eqType = equip.getEquitype();
		
		// 获取 温 湿度
		if (type == MainService.TYPE_MAX_STAT){			//最大值
			tempStr = recordBean.getTempMax();
			humiStr = recordBean.getHumiMax();
		}
		if (type == MainService.TYPE_AVG_STAT){			//平均值
			tempStr = recordBean.getTempAvg();
			humiStr = recordBean.getHumiAvg();
		}
		if (type == MainService.TYPE_MIN_STAT){			//最小值
			tempStr = recordBean.getTempMin();
			humiStr = recordBean.getHumiMin();
		}
		if (tempOrhumi == 1){ // 温度
			tempStr = eqType == EquipData.TYPE_HUMI_ONLY? "": tempStr;
		}else if(tempOrhumi == 2){ // 湿度
			tempStr = eqType == EquipData.TYPE_TEMP_ONLY? "": humiStr;
		}
		return tempStr;
	}

	/**
	 * @describe: 把这些缓存处理变成<tr>...</tr>元素	
	 * @param sysEqType : 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	 * @param rowCount 显示的行数
	 * @param store 要处理的数据集合
	 * @param equipmentIds 要显示的地址列表
	 * @param type :统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,
	 *            还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @param dateStr 时间字符串
	 * @return:
	 * @date:2009-12-8
	 */
	private String makeTrElement(int sysEqType, int rowCount, Map<Integer, BeanForRecord> store, int[] equipmentIds, 
			int type, String dateStr) {
		StringBuffer strBuf = new StringBuffer();
		BeanForRecord recordBean;
		String tempStr = "";
		
		strBuf.append("<tr>");
		strBuf.append("<td>" + rowCount + "</td>");
		strBuf.append("<td>" + dateStr + "</td>");
		// 根据地址列表,选出相关的温湿度数据
		for (int i = 0; i < equipmentIds.length; i++) {
			if (store.containsKey(equipmentIds[i])) {
				recordBean = null;
				recordBean = store.get(equipmentIds[i]);
				//过滤不正常数据
				if (recordBean.getEquipmentId() <= 0) {
					tempStr = getEmptyTd(sysEqType, type);
				}else {
					tempStr = makeTdElement(sysEqType, recordBean, type);
				}
			} else {
				tempStr = getEmptyTd(sysEqType, type);
			}
			strBuf.append(tempStr);
		}// end for
		strBuf.append("</tr>");
		return strBuf.toString();
	}
	
	/**
	 * @describe: 当选择的地址没有数据,这时显示的<td>元素就为"-",而这个<td>元素个数个需要根据type动态调整
	 * @param sysEqType 系统温湿度类型
	 * @param type 统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @return: 根据type,动态返回<td>个数
	 * @date:2009-11-3
	 */
	private String getEmptyTd(int sysEqType,int type) {
		StringBuffer strBuf = new StringBuffer();
		if (type >= MainService.TYPE_MAX_STAT){
			type = type - MainService.TYPE_MAX_STAT;
			strBuf.append("<td>-</td>");
		}
		if (type >= MainService.TYPE_AVG_STAT){
			type = type - MainService.TYPE_AVG_STAT;
			strBuf.append("<td>-</td>");
		}
		if (type >= MainService.TYPE_MIN_STAT){
			type = type - MainService.TYPE_MIN_STAT;
			strBuf.append("<td>-</td>");
			type = 0;	//保证不进入下一个if
		}
		if (type >= MainService.TYPE_NO_STAT){
			type = type - MainService.TYPE_NO_STAT;
			strBuf.append("<td>-</td>");
		}
		//根据系统温湿度类型,调整列
		String rsStr = sysEqType ==1 ? strBuf.toString() + strBuf.toString():strBuf.toString(); 
		return rsStr;
	}

	/**
	 * @describe :分析数据,结合仪器警报界限,越界的用不同的css来显示,得到温度和湿度<td></td>元素
	 *           注意点:css是固定的.低于界限LOWDATACSS,正常界限NORMALDATACSS,高于界限HIGHDATACSS
	 * @param sysEqType :系统温湿度类型
	 * @param recordBean :需要分析的数据,主要涉及:温湿度和地址主键
	 * @param type :统计类型.(TYPE_NO_STAT:没有统计,TYPE_MAX_STAT:统计最大值,还有TYPE_MIN_STAT和TYPE_AVG_STAT)
	 * @return:<td></td>元素内容字符串
	 * @date:2009-10-31
	 */
	private String makeTdElement(int sysEqType, BeanForRecord recordBean, int type) {
		//EquipData equip = CommonDataUnit.getEquipByID(recordBean.getEquipmentId());
		EquipData equip = commonDataUnit.getEquipByID(recordBean.getEquipmentId());
		StringBuffer strBuf = new StringBuffer();
		int typeTemp = type;
		//float tempFloat;
		String tempStr = "";

		if (equip == null) {
			tempStr = getEmptyTd(sysEqType, typeTemp);
			return tempStr;
		}
		
		// eqType(1,'温湿度';2,'单温度';3,'单湿度')
		int eqType = equip.getEquitype();
		if (sysEqType != 3){
			// 温度
			if (typeTemp >= MainService.TYPE_MAX_STAT){			//最大值
				typeTemp = typeTemp - MainService.TYPE_MAX_STAT;
				tempStr = eqType == EquipData.TYPE_HUMI_ONLY?"<td>-</td>": 
					fillTdContent(recordBean.getTempMax(), equip.getTempDown(), equip.getTempUp(), "℃");
				strBuf.append(tempStr);
			}
			if (typeTemp >= MainService.TYPE_AVG_STAT){			//平均值
				typeTemp = typeTemp - MainService.TYPE_AVG_STAT;
				tempStr = eqType == EquipData.TYPE_HUMI_ONLY?"<td>-</td>": 
					fillTdContent(recordBean.getTempAvg(), equip.getTempDown(), equip.getTempUp(), "℃");
				strBuf.append(tempStr);
			}
			if (typeTemp >= MainService.TYPE_MIN_STAT){			//最小值
				typeTemp = typeTemp - MainService.TYPE_MIN_STAT;
				tempStr = eqType == EquipData.TYPE_HUMI_ONLY?"<td>-</td>": 
					fillTdContent(recordBean.getTempMin(), equip.getTempDown(), equip.getTempUp(), "℃");
				strBuf.append(tempStr);
				typeTemp = 0;	//保证不进入下一个if
			}
			if (typeTemp >= MainService.TYPE_NO_STAT){			 //不统计只显示数据,即时数据用(目前没有用到)
				typeTemp = typeTemp - MainService.TYPE_NO_STAT;
				tempStr = eqType == EquipData.TYPE_HUMI_ONLY?"<td>-</td>": 
					fillTdContent(recordBean.getTemperature(), equip.getTempDown(), equip.getTempUp(), "℃");
				strBuf.append(tempStr);
			}		
		}
		
		// 湿度
		typeTemp = type;
		if (sysEqType != 2){
			if (typeTemp >= MainService.TYPE_MAX_STAT){			//最大值
				typeTemp = typeTemp - MainService.TYPE_MAX_STAT;
				tempStr = eqType == EquipData.TYPE_TEMP_ONLY?"<td>-</td>":
					fillTdContent(recordBean.getHumiMax(), equip.getHumiDown(), equip.getHumiUp(), "%");
				strBuf.append(tempStr);
			}
			if (typeTemp >= MainService.TYPE_AVG_STAT){			//平均值
				typeTemp = typeTemp - MainService.TYPE_AVG_STAT;
				tempStr = eqType == EquipData.TYPE_TEMP_ONLY?"<td>-</td>":
					fillTdContent(recordBean.getHumiAvg(), equip.getHumiDown(), equip.getHumiUp(), "%");
				strBuf.append(tempStr);
			}
			if (typeTemp >= MainService.TYPE_MIN_STAT){			//最小值
				typeTemp = typeTemp - MainService.TYPE_MIN_STAT;
				tempStr = eqType == EquipData.TYPE_TEMP_ONLY?"<td>-</td>":
					fillTdContent(recordBean.getHumiMin(), equip.getHumiDown(), equip.getHumiUp(), "%");
				strBuf.append(tempStr);
				typeTemp = 0;	//保证不进入下一个if
			}
			if (typeTemp >= MainService.TYPE_NO_STAT){			 //不统计只显示数据,即时数据用(目前没有用到)
				typeTemp = typeTemp - MainService.TYPE_NO_STAT;
				tempStr = eqType == EquipData.TYPE_TEMP_ONLY?"<td>-</td>":
					fillTdContent(recordBean.getHumidity(),	equip.getHumiDown(), equip.getHumiUp(), "%");
				strBuf.append(tempStr);
			}			
		}			

		return strBuf.toString();
	}

	/**
	 * @describe: data根据down(下限),up(上限),结合<td>元素,显示不同的css样式
	 * 			(LOWDATACSS,NORMALDATACSS,HIGHDATACSS)
	 * @param data :需要分析的数据
	 * @param down :下限
	 * @param up :上限
	 * @param endFlag :内容体的后缀. 温度:℃   湿度:% 
	 * @return :结合<td>元素,显示不同的css样式
	 * @date:2009-11-3
	 */
	private String fillTdContent(String data, float down, float up, String endFlag) {
		StringBuffer strBuf = new StringBuffer();
		float tempFloat;
		String tempStr = "";
		if (data.equals("")){
			strBuf.append("<td></td>");	
		}else{
			tempFloat = Float.parseFloat(data);
			tempStr = tempFloat < down ? LOWDATACSS	: tempFloat > up ? HIGHDATACSS: NORMALDATACSS;
			strBuf.append("<td class = \"" + tempStr + "\">");
			//strBuf.append(data + endFlag );
			strBuf.append(data);
			strBuf.append("</td>");		
		}
		return strBuf.toString();
	}

	/**
	 * @describe: 根据 搜索地址列表, 开始时间, 结束时间和选择的类型,生成查询对象	
	 * @param places : 搜索地址列表
	 * @param startTime : 开始时间
	 * @param endTime 结束时间
	 * @param selectType : 设置搜索数据类型(	MainService.TYPE_RECENT:即时数据 
	 * 										MainService.TYPE_DAILY:代表日报类型
	 * 										MainService.TYPE_MONTH:代表月报类型)
	 * @param orderByType : 排序类型: 1:order by recTime, equipmentId 2:order by equipmentId, recTime(现在都用1)
	 * @return 返回 经过填充 查询对象
	 * @throws Exception: 异常
	 * @date:2009-11-3
	 */	
	public BeanForSearchRecord fillRecordSearchBean(String places, String startTime,
			String endTime, int selectType, int orderByType) throws Exception {
		BeanForSearchRecord beanForSearchRecord = new BeanForSearchRecord();
		SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
		Date date = null;
		
		// 地址列表 
		beanForSearchRecord.setPlaceList(places);
		// 排序类型
		beanForSearchRecord.setOrderByType(orderByType);
		if (selectType == MainService.TYPE_RECENT) {// 即时数据
			//设置搜索数据类型:即时数据
			beanForSearchRecord.setStattype(0);			
			// 开始时间 eg:2009-09-01 18:10:02
			date = dateFormat.parse(startTime);
			beanForSearchRecord.setAlarmStartFrom(date);
			// 结束时间
			date = dateFormat.parse(endTime);
			beanForSearchRecord.setAlarmStartTo(date);
		}else if (selectType == MainService.TYPE_DAILY) {// 日报
			//设置搜索数据类型:日报
			beanForSearchRecord.setStattype(1);
			// 开始时间 eg:2009-08-31
			date = dateFormat.parse(startTime + " 0:00:00");
			beanForSearchRecord.setAlarmStartFrom(date);
			// 结束时间
			date = dateFormat.parse(endTime +  " 23:59:59");
			beanForSearchRecord.setAlarmStartTo(date);
		} else if (selectType == MainService.TYPE_MONTH) {// 月报
			//设置搜索数据类型:月报
			beanForSearchRecord.setStattype(2);			
			// 开始时间 eg:2009-05-dd
			date = dateFormat.parse(startTime + " 0:00:00");
			beanForSearchRecord.setAlarmStartFrom(date);
			// 结束时间--搜索的时候会月加一
			date = dateFormat.parse(endTime + " 0:00:00");
			date = FunctionUnit.nextTime(date, 1, FunctionUnit.Calendar_END_MONTH).getTime();
			date = FunctionUnit.nextTime(date, -1, FunctionUnit.Calendar_END_SECOND).getTime();
			beanForSearchRecord.setAlarmStartTo(date);
		}

		return beanForSearchRecord;
	}
	
	public String hisZj(){
		areaToEqList = commonDataUnit.createEqAreaWithNOSel(null);
		return SUCCESS;
	}
	
	public String hisZjRec(){
		BeanForZjHisRec zjBean;
		try {
			zjBean = fillBean4ZjHisRec();
			List<ZjHistory> zjHis = serviceSqlServer.selectZjHisRec(zjBean);
			if (zjHis != null && zjHis.size() > 0){
				doHisZjRecHtml(zjHis);// 把数据包装成页面元素 -- 显示成html表格集合	
				return PRINTOUT;
			}			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		nodataInfo();
		return NODATARETURN;
	}
	
	/**
	 * @describe: 把药监局数据显示成html表格集合	
	 * @date:2010-4-15
	 */
	public void doHisZjRecHtml(List<ZjHistory> zjHis) throws ParseException{
		StringBuffer strbuf = new StringBuffer();
		int tempInt = 1;
		// 搜索标题-细节
		headTitle = commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + " - 药监历史数据查询";// 格式:公司名 - 查询名 
		SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
		headDetail = "时间范围:" + getDateStringByStat(dateFormat.parse(timeFrom), MainService.TYPE_RECENT) +
					"~" + getDateStringByStat(dateFormat.parse(timeTo), MainService.TYPE_RECENT);	// 搜索细节
		
		//strJspTableHeader;// 表格标题
		
		strbuf.append("<td>NO</td>");
		strbuf.append("<td>记录时间</td>");
		strbuf.append("<td>仪器名称</td>");
		strbuf.append("<td>药监局编号</td>");
		strbuf.append("<td>温度值(℃)</td>");
		strbuf.append("<td>湿度值(%RH)</td>");
		//strbuf.append("<td>BSend</td>");//未知含义
		strJspTableHeader = strbuf.toString();
		
		jsptableList = new ArrayList<String>();
		for (ZjHistory zjBean : zjHis) {
			strbuf.delete(0, strbuf.length());
			strbuf.append("<tr>");
			strbuf.append("<td>" + tempInt + "</td>");
			strbuf.append("<td>" + FunctionUnit.getDateToStr(zjBean.getCheckTime(), FunctionUnit.Calendar_END_SECOND, FunctionUnit.SHOW_CHINESE) + "</td>");
			strbuf.append("<td>" + zjBean.getMonitorName() + "</td>");
			strbuf.append("<td>" + zjBean.getLicenseNo() + "</td>");
			strbuf.append("<td>" + zjBean.getTemperature() + "</td>");
			strbuf.append("<td>" + zjBean.getHumidity() + "</td>");
			//strbuf.append("<td>" + zjBean.getBSend() + "</td>");
			strbuf.append("</tr>");
			jsptableList.add(strbuf.toString()); // 表格数据
			tempInt++;
		}
	}
	
	private void nodataInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "搜索不到药监历史数据...";
		strbuf.append("建议:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    ●调整开始时间和结束时间");
		strbuf.append("</label><br/><br/>");
		strbuf.append("<label>");
		strbuf.append("    ●选择适当的仪器");
		strbuf.append("</label>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("*如果调整后,仍然没有查询结果,那么这段时间内确实没有数据记录存在...");		
		headDetail = strbuf.toString();
	}	
	
	private BeanForZjHisRec fillBean4ZjHisRec() throws ParseException{
		BeanForZjHisRec zjBean = new BeanForZjHisRec();
		SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
		
		zjBean.setPlaceList(placeListids);					// 设置仪器主键列表（按逗号隔开）
		zjBean.setTimeFrom(dateFormat.parse(timeFrom));		// 开始时间
		zjBean.setTimeTo(dateFormat.parse(timeTo));			// 结束时间
		zjBean.setLowTemp(Double.parseDouble(tempDown));	// 温度下限
		zjBean.setHighTemp(Double.parseDouble(tempUp));		// 温度下限
		zjBean.setLowHumi(Double.parseDouble(humiDown));	// 温度上限
		zjBean.setHighHumi(Double.parseDouble(humiUp));		// 湿度上限
		
		return zjBean;
	}

	// ----------------以下为get，set方法----------------
	/**
	 * @describe : 获取全部仪器标识串.Map(equipmentId : 地点 + 标号 + 仪器地址)
	 * @date:2009-11-3
	 */
	public Map<Integer, String> getPlaceList() {
		//return CommonDataUnit.getEquiMapStr();
		return commonDataUnit.getEquiMapStr();
	}
	
	//搜索间隔
	public Map<Integer, String> getSearchInterval() {
		searchInterval = getMapSel("searchInterval");
		return searchInterval;
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
	@JSON(deserialize=false,serialize=false)
	public int getSystemEqType() {
		// 返回 systemEqType
		//return CommonDataUnit.getSystemEqType();
		return commonDataUnit.getSystemEqType();
	}
	// ----------------普通 get，set方法----------------
	public Map<String, String> getAreaToEqList() {
		return areaToEqList;
	}
	
	public String getPlaceListids() {
		return placeListids;
	}

	public void setPlaceListids(String placeListids) {
		this.placeListids = placeListids;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public int getShowType() {
		return showType;
	}
	
	public void setShowType(int showType) {
		this.showType = showType;
	}

	public int getMaxSelector() {
		return maxSelector;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public int getMax_rs_now() {
		return max_rs_now;
	}

	public int getMax_rs_day() {
		return max_rs_day;
	}

	public int getMax_rs_month() {
		return max_rs_month;
	}

	public String getStrJspTable() {// 不用
		return strJspTable;
	}
	public List<String> getJsptableList() {// 使用
		return jsptableList;
	}

	public String getStrJspTableHeader() {
		return strJspTableHeader;
	}

	public int[] getStatBox() {
		return statBox;
	}

	public void setStatBox(int[] statBox) {
		this.statBox = statBox;
	}

	public String getTempDataStr() {
		return tempDataStr;
	}

	public String getTempConfigStr() {
		return tempConfigStr;
	}

	public String getHumiDataStr() {
		return humiDataStr;
	}

	public String getHumiConfigStr() {
		return humiConfigStr;
	}

	public String getHeadTitle() {
		return headTitle;
	}

	public String getHeadDetail() {
		return headDetail;
	}
	public String getTempDown() {
		return tempDown;
	}
	public void setTempDown(String tempDown) {
		this.tempDown = tempDown;
	}
	public String getTempUp() {
		return tempUp;
	}
	public void setTempUp(String tempUp) {
		this.tempUp = tempUp;
	}
	public String getHumiDown() {
		return humiDown;
	}
	public void setHumiDown(String humiDown) {
		this.humiDown = humiDown;
	}
	public String getHumiUp() {
		return humiUp;
	}
	public void setHumiUp(String humiUp) {
		this.humiUp = humiUp;
	}
	
}
