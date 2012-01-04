package com.htc.action;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.googlecode.jsonplugin.annotations.JSON;
import com.htc.bean.BeanForOnlineWarn;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.FunctionUnit;
import com.htc.domain.*;
import com.htc.model.*;

/**
 * @ OnLineWarnDataAction.java
 * 作用 : 在线报警数据单元action.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-5     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class OnLineWarnDataAction extends AbstractAction {

	// 服务类
	private MainService mainService;
	
	// 页面显示
	//private Map<Integer, Record> warnMap = new ConcurrentHashMap<Integer, Record>();// 当前报警列表-未处理
	//private List<BeanForOnlineWarn> warnList = new ArrayList<BeanForOnlineWarn>(); // 当前报警列表-处理过
	private List<AlarmRec> warnList = null; // 当前报警列表-处理过
	
	//private int tempshowType;						// 温度显示格式(1:摄氏 2:华氏)	
	private int palyFalg;							// 设置声音开启状态设置声音开启状态 (1:不播放  2:播放 3:系统关闭播放)
	private String alarmPlayFile = ""; 				// 报警播放文件
	private int warnItemCount ; 					// 报警列表元素个数
	private int falshTime;							// 页面刷新时间-同串口获取时间	
	
	// json 返回
	private int alarmId;							// json 输入参数: 报警数据主键
	private int doWarnFalg; 						// json 输出参数: 报警处理结果(0:用户未登陆   1:复位操作成功  2:已经有人复位过了)
	
	// 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	@SuppressWarnings("unused")
	private int systemEqType;		
	
	// 构造方法
	public OnLineWarnDataAction() {
	}
	//注册service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	
	@Override
	public String execute() {
		warnList = mainService.selectUndoRec();
		if (warnList == null){warnList = new ArrayList<AlarmRec>();}
		// 报警列表元素个数
		warnItemCount = warnList.size();
		//设置声音开启状态 (1:不播放  2:播放 3:系统关闭播放)
		palyFalg = showpalyFalg();
		// 报警播放文件
		alarmPlayFile = commonDataUnit.getWarnPlayFile();
		// 页面刷新时间-同串口获取时间
		falshTime = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
		
		return SUCCESS;
	}
	
	/**
	 * @describe: 获取温度或者湿度的显示字符	
	 * @param value ：温度或者湿度值
	 * @param alarmtype ：报警类型
	 * @param type :[0:湿度 1:摄氏 2:华氏] 
	 * 		主要是在温度显示为华氏的时候用(1:摄氏 2:华氏),湿度时候为0
	 * @return:
	 * @date:2010-1-7
	 */
	public String catchTempHuimStr(String value,int alarmtype, int type){
		//eg:温度[温度数据+℃/F+报警类型]:如：15.58℃低温报警 
		//eg:湿度[湿度数据+%+报警类型]如：100.58%高湿报警
		StringBuffer strbuf = new StringBuffer();
		// 温湿度值
		if (type == 2){
			strbuf.append(getRealTemperature(value,type)); 
		}else{
			strbuf.append(value);
		}
		// 单位
		strbuf.append(type==0?"%RH":type==1?"℃":"F");
		type = type == 0? 2: 1;
		// 报警类型
		strbuf.append(getAlarmType(alarmtype,type));
		return strbuf.toString();
	}
	
	/**
	 * @describe: 获取声音开启状态 	
	 * @return: (1:不播放  2:播放)
	 * @date:2010-1-6
	 */
	public int showpalyFalg(){
		int rsInt = 1;
		// 先判断系统有没有开启声卡播放
		rsInt = commonDataUnit.getWarnOpenFlag();
		// 这些代码不用,因为页面上没有个人播放标志
//		if (rsInt == 2){
//			// 再判断个人有没有开启声卡播放
//			ActionContext ctx = ActionContext.getContext();
//			// 没有登录不播放
//			if (ctx.getSession().get(paly_SESSION_TAG)==null){
//				rsInt = 1;
//			}else{
//				rsInt = (Integer)ctx.getSession().get(paly_SESSION_TAG);
//			}			
//		}else{
//			rsInt = 3;
//		}		
		rsInt = rsInt == 2? 2:1;
		return rsInt;
	}
	
	/**
	 * @describe: 开启个人播放标志 -- 过期代码
	 * @date:2009-11-5
	 */
	public String openBackSound(){
		palyFalg = 2;		// (1:不播放 2:播放)
//		ActionContext ctx = ActionContext.getContext();
//		ctx.getSession().put(paly_SESSION_TAG, palyFalg);
		// 获取及时报警数据
		execute();
		return SUCCESS;
	}
	
	/**
	 * @describe: 关闭个人播放标志 (1:不播放 2:播放) -- 过期代码
	 * @date:2009-11-5
	 */
	public String closeBackSound(){ 
		palyFalg = 1;		// (1:不播放 2:播放)		
//		ActionContext ctx = ActionContext.getContext();
//		ctx.getSession().put(paly_SESSION_TAG, palyFalg);
		// 获取及时报警数据
		execute();
		return SUCCESS;
	}
	
	/**
	 * @describe: 处理报警,复位后修改数据库相应记录 
	 * 对应: Action : doWarnJson 
	 * 		 jsp : doWarn
	 * @date:2009-11-5
	 */
	public String doWarnJson() {
		String userName = getSysUserName();
		if (userName != null){
			// effectRow表示数据库影响的函数(1:表示修改成功 0:表示已经修改过了)
			int effectRow = mainService.updateAlarmRec(getUpdateAlarmBean(alarmId,userName));
			if (effectRow == 0){
				doWarnFalg = 2;//(0:用户未登陆   1:复位操作成功  2:已经有人复位过了)
			}else if (effectRow == 1){
				doWarnFalg = 1;//(0:用户未登陆   1:复位操作成功  2:已经有人复位过了)
			}
		}else{
			doWarnFalg = 0;//(0:用户未登陆   1:复位操作成功  2:已经有人复位过了)
		}
		return SUCCESS;
	}
	
	/**
	 * @describe: 获取需要处理的AlarmBean		
	 * @param alarmId 报警编号
	 * @param username	处理用户
	 * @date:2009-12-12
	 */
	public AlarmRec getUpdateAlarmBean(int alarmId,  String username){
		AlarmRec alarm = new AlarmRec();
		alarm.setAlarmEnd(new Date().getTime());// 结束时间201645
		alarm.setAlarmmode(AlarmRec.PC_MODE);	// 处理模式
		alarm.setState(1);						// 处理状态:1:已复位	2：未复位		
		alarm.setUsername(username);			// 处理用户
		
		alarm.setAlarmId(alarmId);				// 报警编号(数据库主键)
		return alarm;
	}
	
	/**
	 * @describe: 根据温度显示类型,显示温度值
	 * @param temp: 摄氏的温度值
	 * @param tempType: 温度显示格式(0:湿度 1:摄氏 2:华氏)
	 * @return
	 * @date:2009-12-17
	 */
	public String getRealTemperature(String temp, int tempType){
		return WarnDataAction.getRealTemperature(temp, tempType);
	}	
	
	/**
	 * @describe:	获取 报警 字符
	 * s:property	value="@com.htc.action.OnLineWarnDataAction@getAlarmType(value.alarmtype,1)"
	 * 调用本action的页面也可以:property	value="getAlarmType(value.alarmtype,1)"
	 * @param alarmtype  报警类型
	 * @param type (1:温度  2:湿度)
	 * @date:2009-11-5
	 */
	public String getAlarmType(int  alarmtype,int type){
		return WarnDataAction.getAlarmType(alarmtype, type);
	}
	
	/**
	 * @describe: 获取 仪器正常的温度,湿度返回
	 * s:property	value="@com.htc.action.OnLineWarnDataAction@getAreaString(value.equipmentId)"
	 * @param equipmentId 仪器ID
	 * @date:2009-11-5
	 */
	public String getAreaString(int  equipmentId){
		//return WarnDataAction.getAreaString(equipmentId);
		EquipData eq = commonDataUnit.getEquipByID(equipmentId);
		StringBuffer rsStrBuf = new StringBuffer();
		
		if (eq == null){
			rsStrBuf.append("-");
		}else{
			// 温度显示格式(1:摄氏 2:华氏)
			String str = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE))==1?"℃":"F";
			if (eq.getEquitype() != EquipData.TYPE_HUMI_ONLY){
				rsStrBuf.append(eq.getTempDown());
				rsStrBuf.append("~");
				rsStrBuf.append(eq.getTempUp());
				rsStrBuf.append(str);
			}
			if (eq.getEquitype() == EquipData.TYPE_TEMP_HUMI){
				rsStrBuf.append("/");
			}
			if (eq.getEquitype() != EquipData.TYPE_TEMP_ONLY){
				rsStrBuf.append(eq.getHumiDown());
				rsStrBuf.append("~");
				rsStrBuf.append(eq.getHumiUp());
				rsStrBuf.append("%RH");
			}
		}
		eq = null;
		return rsStrBuf.toString();		
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
		return commonDataUnit.getSystemEqType();
	}
	// ----------------以下为get，set方法----------------
	public int getPalyFalg() {
		return palyFalg;
	}
	
	public void setPalyFalg(int palyFalg) {
		this.palyFalg = palyFalg;
	}
	
	public List<AlarmRec> getWarnList() {
		return warnList;
	}

	@JSON(deserialize=false,serialize=false)
	public int getAlarmId() {
		return alarmId;
	}
	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}

	public int getDoWarnFalg() {
		return doWarnFalg;
	}

	public void setDoWarnFalg(int doWarnFalg) {
		this.doWarnFalg = doWarnFalg;
	}

	@JSON(deserialize=false,serialize=false)
	public String getAlarmPlayFile() {
		return alarmPlayFile;
	}	
	
	public void setAlarmPlayFile(String alarmPlayFile) {
		this.alarmPlayFile = alarmPlayFile;
	}

	public int getWarnItemCount() {
		return warnItemCount;
	}

	public void setWarnItemCount(int warnItemCount) {
		this.warnItemCount = warnItemCount;
	}
	public int getFalshTime() {
		return falshTime;
	}
	
}
