package com.htc.action;

import java.text.SimpleDateFormat;
import java.util.*;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.*;
import com.htc.domain.*;
import com.htc.model.*;

/**
 * @ WarnDataAction.java
 * 作用 : 报警数据查询action.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class WarnDataAction extends AbstractAction {

	// 服务类
	private MainService mainService;
	
	// 页面显示
	private List<AlarmRec> alarmRecList;			// 表格信息列表
	private Map<String, String> areaToEqList;		// 页面地址选择用的map(区域名,区域中的仪器列表)
	private int maxSelector;						// 页面最多能显示的地址数量	
	private int max_rs_alarm;						// 页面最多能显示的时间区间-即时
	
	// 页面传递的参数
	private AlarmRec alarmRec;						// 页面显示Bean
	private String placeListids="";					// 选中的列表
	private String timeFrom;						// 开始时间:格式：2009-5-5 12:11:12--sql查询条件用
	private String timeTo;							// 结束时间
	private String fromTime;						// 开始时间:格式：2009年5月5日 12时11分12秒--页面返回标题用
	private String toTime;							// 结束时间
	private int maxcount;							// 返回的最大记录数
	private int tempshowType;						// 温度显示格式(1:摄氏 2:华氏)	
	
	private String doSetPerson;						// 报警处理人员
	private int doperson=0;							// 是否把报警处理人员加入搜索条件- 0:不处理 1:处理 		
	private int resetBox;							// 复位状态-1:已复位 2:未复位 
	private int timeBox;							// 时间类型-1:报警时间 2:复位时间 
	private int orderBox;							// 排序类型-1:升序 2:降序
	private int[] warnBoxForTemp;					// 报警类型-2:高温 1:低温 
	private int[] warnBoxForHumi;					// 报警类型-20:高湿 10:低湿 
	
	// 搜索标题-细节
	private String headTitle;				 
	private String headDetail;
	
	// 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	@SuppressWarnings("unused")
	private int systemEqType;		
	
	
	// 构造方法
	public WarnDataAction() {
	}
	//注册service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}	

	@Override
	public String execute() {
		
		//页面地址选择用的map(区域名,区域中的仪器列表)
		areaToEqList = commonDataUnit.createEqAreaWithNOSel(null);	
		// 页面最多能显示的地址数量
		maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_ALARM));
		// 页面时间选择的最大差值
		max_rs_alarm = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_ALARM));
		
		return SUCCESS;
	}
	
	/**
	 * @describe: 增加到日志
	 * @date:2009-12-22
	 */
	public void insertToLog(){
		StringBuffer strbuf = new StringBuffer();
		strbuf.append(getSysUserName()+":查询");
		strbuf.append("警报数据");
		strbuf.append("  ");
		strbuf.append("时间段:");
		strbuf.append(timeFrom);
		strbuf.append("~");
		strbuf.append(timeTo);
		//MainService mainService = new MainService();
		mainService.packTlog(TLog.SEARCH_LOG, strbuf.toString());
	}
	
	// 列出列表
	public void list() {
		insertToLog();
		
		StringBuffer alarmtypeStrbuf = new StringBuffer();
		
		try {
			// 计算:报警类型
			if (warnBoxForTemp != null){
				for (int i = 0; i < warnBoxForTemp.length; i++) {
					alarmtypeStrbuf.append(warnBoxForTemp[i]);
					alarmtypeStrbuf.append(",");
				}
			}
			if (warnBoxForHumi != null){
				for (int j = 0; j < warnBoxForHumi.length; j++) {
					alarmtypeStrbuf.append(warnBoxForHumi[j]);
					alarmtypeStrbuf.append(",");
				}
			}
			if ((warnBoxForTemp != null) && (warnBoxForHumi != null)) {
				for (int i = 0; i < warnBoxForTemp.length; i++) {
					for (int j = 0; j < warnBoxForHumi.length; j++) {
						alarmtypeStrbuf.append(warnBoxForTemp[i] + warnBoxForHumi[j]);
						alarmtypeStrbuf.append(",");
					}
				}
			}
			
			//实例化服务类
			//mainService = mainService == null ? new MainService() : mainService;
			
			if (alarmRec == null){
				alarmRec = new AlarmRec();
			}
			// 1:要搜索的仪器主键 -- 去掉 placeListids 最后一个逗号,js中已经处理
			// placeListids = placeListids.substring(0,placeListids.length() - 1);
			alarmRec.setPlaceList(placeListids);
			// 2:开始时间
			// 3:结束时间
			SimpleDateFormat dateFormat = new SimpleDateFormat(FunctionUnit.DATA_FORMAT_LONG);
			alarmRec.setAlarmStart(dateFormat.parse(timeFrom).getTime());
			alarmRec.setAlarmEnd(dateFormat.parse(timeTo).getTime());
			// 4:能返回的最大记录数 
			alarmRec.setPageSize(maxcount);
			// 5:操作人员
			if ( (doperson==1)&& (doSetPerson!=null)&&(doSetPerson.trim().length() > 0) ){
				alarmRec.setUsername(doSetPerson);
			}
			// 6:复位状态[1:已复位	2：未复位]
			alarmRec.setState(resetBox);
			// 7:时间类型[1:(按报警时间搜索) 2:(按复位时间搜索)]
			alarmRec.setWhichToSearch(timeBox);
			// 8:排序类型[1:升序 2:降序]
			alarmRec.setAscOrDesc(orderBox==1?"ASC":"DESC");
			// 9:报警类型[2:高温 1:低温 20:高湿 10:低湿]--去掉最后一个逗号
			alarmRec.setAlarmtypeStr(alarmtypeStrbuf.substring(0, alarmtypeStrbuf.length()-1));
			// 10:处理模式[PC ,GPRS(1，2)] -- 先固定
			//alarmRec.setAlarmmode(1);
			
			// 根据填充条件,搜索记录
			alarmRecList = mainService.getAllAlarmRec(alarmRec);
			
			// 设置标题
			// headTitle格式:公司名 -报警数据查询结果 
			headTitle = commonDataUnit.getSysArgsByKey(BeanForSysArgs.COMPANY_NAME) + "-报警数据查询结果 ";
			// headDetail格式:时间范围: 2009年11月06日 12时~ 2009年11月06日 23时		
			StringBuffer strbuf = new StringBuffer();
			strbuf.append("时间范围:");
			strbuf.append(fromTime);
			strbuf.append("~");
			strbuf.append(toTime);
			headDetail = strbuf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 获取列表
	public String getWarnList() {
		list();
		if (alarmRecList.size() == 0){// 没有搜索到数据
			nodataInfo();
			return NODATARETURN;
		}else{
			// 温度显示格式(1:摄氏 2:华氏)	
			tempshowType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));	
			return PRINTOUT;
		}
	}

	/**
	 * @describe: 没有数据时显示的信息
	 * @date:2009-12-10
	 */
	private void nodataInfo() {
		StringBuffer strbuf = new StringBuffer();
		headTitle = "搜索不到报警记录...";
		strbuf.append("建议:");
		strbuf.append("<br/>");
		strbuf.append("<label>");
		strbuf.append("    ●调整开始时间和结束时间");
		strbuf.append("</label><br/>");
		strbuf.append("<label>");
		strbuf.append("    ●选择适当的仪器");
		strbuf.append("</label><br/>");
		strbuf.append("<label>");
		strbuf.append("    ●调整的报警类型");
		strbuf.append("</label><br/>");
		strbuf.append("<label>");
		strbuf.append("    ●换一个复位人员");
		strbuf.append("</label>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("<br/>");
		strbuf.append("*如果调整后,仍然没有查询结果,那么这段时间内确实没有数据记录存在...");
		headDetail = strbuf.toString();
	}
	
	// 删除报警记录
	public String delete() {
		//实例化服务类
		//mainService = mainService == null ? new MainService() : mainService;
		
		mainService.deleteAlarmRecById(alarmRec.getAlarmId());
		list();
		return PRINTOUT;
	}

	/**
	 * @describe: 根据温度显示类型,显示温度值
	 * @param temp: 摄氏的温度值
	 * @param tempType: 温度显示格式(0:湿度 1:摄氏 2:华氏)
	 * @return
	 * @date:2009-12-17
	 */
	public static String getRealTemperature(String temp, int tempType){
		float tempfloat = 0;
		if (tempType == 2){// 温度显示格式(1:摄氏 2:华氏)
			tempfloat = Float.parseFloat(temp) *9/5 + 32;
			temp = FunctionUnit.FLOAT_DATA_FORMAT.format(tempfloat);
		}
		return temp;
	}
	
	/**
	 * @describe:	获取 报警 字符
	 * @param alarmtype 报警类型
	 * @param type(1:温度  2:湿度)
	 * @date:2009-11-5
	 */
	public static String getAlarmType(int  alarmtype,int type){
		String rsStr = "";
		if (type == 1){//温度状态(低温报警,高温报警)
			if (alarmtype % 10 == 1){
				rsStr = "低温报警";
			}else if ( alarmtype % 10 == 2){
				rsStr = "高温报警";
			}else{
				rsStr = "正常";
			}
		}else if(type == 2){//湿度状态 (低湿报警,高湿报警)
			if ( (alarmtype < 20) && (alarmtype >=10) ){
				rsStr += "低湿报警";
			}else if ( alarmtype >= 20){
				rsStr += "高湿报警";
			}else{
				rsStr = "正常";
			}
		}
		return rsStr;
	}
	
	/**
	 * @describe: 获取 仪器正常范围. 如:1.0~10.0℃/0.0~100.0%RH
	 * @param equipmentId 仪器ID
	 * @date:2009-11-5
	 */
	public String getAreaString(int  equipmentId){
		return commonDataUnit.getAreaString(equipmentId);
	}

	
	/**
	 * @describe: 从毫秒数获取时间类型的字符串
	 * @date:2010-1-24
	 */
	public static String getDateStrFromMills(long mills){
		String dtemp = null;
		dtemp = FunctionUnit.mill2DateStr(mills, "yyyy-MM-dd HH:mm:ss");
		return dtemp;
	} 	
	
	/**
	 * @describe: 得到地址list
	 * @param placeListStr 逗号隔开的ID
	 * @date:2009-11-6
	 */
	public List<Integer> getAddressList(String placeListStr){
		String [] strArray = placeListStr.split(",");
		List<Integer> intList = new ArrayList<Integer>();
		for (int i = 0; i < strArray.length; i++) {
			intList.add(Integer.parseInt(strArray[i].trim()));
		}
		return intList;
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
		return commonDataUnit.getSystemEqType();
	}
	//----------------普通 get，set方法----------------
	//温度显示格式(1:摄氏 2:华氏)
	public int getTempshowType(){
		return tempshowType;
	}	
	
	public List<AlarmRec> getAlarmRecList() {
		return alarmRecList;
	}

	public Map<String, String> getAreaToEqList() {
		return areaToEqList;
	}

	public int getMaxSelector() {
		return maxSelector;
	}

	public int getMax_rs_alarm() {
		return max_rs_alarm;
	}

	public void setAlarmRec(AlarmRec alarmRec) {
		this.alarmRec = alarmRec;
	}

	public void setPlaceListids(String placeListids) {
		this.placeListids = placeListids;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public void setMaxcount(int maxcount) {
		this.maxcount = maxcount;
	}

	public String getHeadTitle() {
		return headTitle;
	}

	public String getHeadDetail() {
		return headDetail;
	}

	public void setDoSetPerson(String doSetPerson) {
		this.doSetPerson = doSetPerson;
	}

	public void setResetBox(int resetBox) {
		this.resetBox = resetBox;
	}

	public void setTimeBox(int timeBox) {
		this.timeBox = timeBox;
	}

	public void setOrderBox(int orderBox) {
		this.orderBox = orderBox;
	}

	public void setWarnBoxForTemp(int[] warnBoxForTemp) {
		this.warnBoxForTemp = warnBoxForTemp;
	}

	public void setWarnBoxForHumi(int[] warnBoxForHumi) {
		this.warnBoxForHumi = warnBoxForHumi;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public void setDoperson(int doperson) {
		this.doperson = doperson;
	}

}
