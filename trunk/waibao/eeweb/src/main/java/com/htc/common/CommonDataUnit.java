package com.htc.common;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.htc.bean.*;
import com.htc.domain.*;
import com.htc.model.*;
import com.htc.model.seriaPort.SimCard_Unit;

/**
 * @ WarnDataUnit.java
 * 作用 : 共享数据单元<br>
 * 	1: 仪器信息(一次加载,提高重用性) Map(equipmentId, EquipData)<br>
 * 	2: 报警数据信息列表 Map(equipmentId, Record)<br>
 * 	3: 地址字符串列表(格式化后的) Map(equipmentId, String)<br>
 * 	4: 仪器主键的列表 List(equipmentId)<br>
 * 	5: 报警播放的文件<br>
 *  6: 各地址对应的电压值 Map(equipmentId, power)<br>
 *  7: 检查收到数据是否是警报数据<br>
 *  8: 获取第一个仪器的ID<br>
 * 注意事项 : 无<br>
 * VERSION       DATE            BY       CHANGE/COMMENT<br>
 * 1.0          2009-11-4     YANGZHONLI  create<br>
 */
public class CommonDataUnit {
	//对这些集合进行并发修改是安全的(ConcurrentHashMap、CopyOnWriteArrayList)
	/**
	 * 仪器信息(一次加载,提高重用性)Map(Integer, EquipData)
	 */
	public Map<Integer, EquipData> equiMap = new ConcurrentHashMap<Integer, EquipData>();
	/**
	 * 仪器信息(一次加载,提高重用性)List(EquipData)
	 */
	public List<EquipData> equiList = new CopyOnWriteArrayList<EquipData>();
	/**
	 * 地址字符串列表(格式化后的)
	 */
	public Map<Integer, String> equiMapStr = new ConcurrentHashMap<Integer, String>();
	/**
	 *现有电话号码列表 
	 */
	public List<PhoneList> phonelists = new ArrayList<PhoneList>();
	/**
	 * 报警播放的文件
	 */
	public String warnPlayFile = null;
	/**
	 * 是 否播放报警文件(0:未初始化 1:不开 2：开)(全局)
	 */
	public int warnOpenFlag = 0;
	/**
	 * 系统信息map(String,String)
	 */
	public Map<String, String> sysArgs = new ConcurrentHashMap<String, String>();

	/**
	 * 读取全部的区域信息
	 */
	public List<Workplace> allWorkPlaceList = new CopyOnWriteArrayList<Workplace>();
	/**
	 * 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	 */
	public int systemEqType = 0;
	/**
	 * access中数据是否做了清空处理, 
	 * accessDataEmpty=true 清空,无数据
	 * accessDataEmpty=false 有数据
	 */
	public boolean accessDataEmpty = false;
	/**
	 * access中数据连接状态
	 * accessLinkState=true 连接正常
	 * accessLinkState=false 连接失败
	 * 默认是TRUE
	 */
	public boolean accessLinkState = true;
	/**
	 * 每10分钟,向access数据库中插入或者修改数据标记:
	 * 间隔1000 * 60 * 10
	 */
	public long accessTimeFalg = -1;
	/**
	 * 仪器是否在运行，串口正在采集数据时eqDoingData为true,采集完后eqDoingData为false
	 */
	public boolean eqDoingData = false;
	/**
	 * 需要连接到access的点数
	 */
	public int accessLinkCount;
	/**
	 * 短信模块是否在处理任务. 处理任务时smsDoingData为true,处理完后smsDoingData为false 
	 */
	public boolean smsDoingData = false;
	
	/**
	 *  短信模块输入流缓冲池
	 */
	private Queue<SmsRecord> queue4SimCard = new LinkedList<SmsRecord>();
	
	private ManaService manaService;
	private SetSysService setSysService;
	private MainService mainService;
	private ServiceAccess serviceAccess;
	private SimCard_Unit simCard_Unit;
	
	//注册service -- spring ioc
	public void setManaService(ManaService manaService) {
		this.manaService = manaService;
	}
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setServiceAccess(ServiceAccess serviceAccess) {
		this.serviceAccess = serviceAccess;
	}
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}
	
	/**
	 * @describe:统计系统中所用仪器类型情况:<br> 
	 * @return	0:系统中没有任何仪器<br>
	 * 			1:系统中既有温度又有湿度仪器<br>		
	 * 			2:系统中全部是温度仪器<br>	
	 * 			3:系统中全部是湿度仪器<br>
	 * @date:2010-1-23
	 */
	public synchronized int getSystemEqType(){
		if (systemEqType == 0){
			int tempInt = 0;
			List<BeanForEqTypeCount> eqCount = manaService.getEqCountByType();
			for (BeanForEqTypeCount bean : eqCount) {
				if (bean.getEqType() == 1){// 1:温湿度
					tempInt += 100; 
				}else if (bean.getEqType() == 2){// 2:单温度
					tempInt += 10;
				}else if (bean.getEqType() == 3){//	3:单湿度
					tempInt += 1;
				}
			}
			systemEqType = tempInt==0?0:tempInt==1?3:tempInt==10?2:1;
		}
		return systemEqType;
	}
	
	public void reSetSystemEqType(){
		systemEqType = 0;
	}
	
	/**
	 * 填充电话号码列表
	 */
	public void fillPhoneList(){
		phonelists = setSysService.getAllPhoneLists();	//现有电话号码列表
	}
	
	/**
	 * 获取电话号码列表
	 */
	public List<PhoneList> getPhoneList(){
		if (phonelists.size() <= 0){
			fillPhoneList();
		}
		return phonelists;			
	}
	
	/**
	 * @describe :填充 选择仪器信息 (一次转载,提高重用性) equiMap:Map(equipmentId, EquipData) 和
	 *           equiAddrList:List(equipmentId)
	 * @date:2009-11-2
     */
	public void fillEquiMap() {
		BeanForEqOrder eqorderBean = new BeanForEqOrder();
		eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_ORDERID);

		equiList = manaService.getAllEquiObject(eqorderBean);
		int equipmentId;
		if (equiList == null){ 
			equiList =  new CopyOnWriteArrayList<EquipData>();
		}
		equiMap.clear();
		accessLinkCount = 0; // 初始化,需要显示到access的个数 
		for (int j = 0; j < equiList.size(); j++) {
			equipmentId = equiList.get(j).getEquipmentId();
			if (equiList.get(j).getShowAccess()==1){ // 需要显示到access的个数
				accessLinkCount++;
			}
			equiMap.put(equipmentId, equiList.get(j));
		}
	}
	
	/**
	 * @describe: 获取第一个仪器的ID		
	 * @return: 返回0,则没有仪器连接
	 * @date:2009-11-4
	 */
	public int getFirstEquipmentId(){
		int equipmentId = 0;
		if (equiMap.size() <= 0){
			fillEquiMap();
		};
		equipmentId = equiMap.size() > 0 ? equiMap.keySet().iterator().next() : 0;
		return equipmentId;
	}
	
	/**
	 * @describe :获取全部的仪器信息List(EquipData)
	 * @date:2009-11-2
     */
	public List<EquipData> getEquiList() {
		if (equiList.size() <= 0){
			fillEquiMap();
		}
		return equiList;
	}

	/**
	 * @describe: 删除对应的仪器对应信息List(EquipData)
	 * @param equipmentId : 仪器ID
	 * @date:2009-11-2
	 */
	public void removeEquiList(int equipmentId) {
		for (EquipData equipData : equiList) {
			if (equipData.getEquipmentId() == equipmentId){
				equiList.remove(equipData);
				break;
			}
		}
	}
	
	/**
	 * @describe: 获取全部仪器 主键的列表(List: equipmentId)
	 * @date:2009-11-2
     */
	public Set<Integer> getEquipmentList() {
		if (equiMap.size() <= 0 ){
			fillEquiMap();
		}
		return equiMap.keySet();
	}

	/**
	 * @describe :获取全部的仪器信息(Map:equipmentId, EquipData)
	 * @date:2009-11-2
     */
	public Map<Integer, EquipData> getEquiMap() {
		if (equiMap.size() <= 0){
			fillEquiMap();
		}
		return equiMap;
	}

	/**
	 * @describe :获取 选中对应的仪器信息数据对象 EquipData
	 * @param equipmentId : 仪器Id
	 * @date:2009-11-2
     */
	public EquipData getEquipByID(int equipmentId) {
		if ( (equiMap.size() == 0) || (!equiMap.containsKey(equipmentId)) ){
			fillEquiMap();
		}
		return equiMap.get(equipmentId);
	}

	/**
	 * @describe: 删除对应的仪器对应信息
	 * @param equipmentId : 仪器ID
	 * @date:2009-11-2
	 */
	public void removeEquiMap(int equipmentId) {
		if (equiMap.containsKey(equipmentId)) {
			equiMap.remove(equipmentId);
		}
	}
	
	/**
	 * @describe: 填充全部仪器标识串 Map(equipmentId : 地点 + 标号 + 仪器地址)
	 * @date:2009-11-2
     */
	public void fillEquiStr() {
		BeanForEqOrder eqorderBean = new BeanForEqOrder();
		eqorderBean.setEqorderType(BeanForEqOrder.SELECT_WITH_EQUIPID);
		equiMapStr = manaService.getAllEquiString(eqorderBean);
	}		

	/**
	 * @describe: 获取全部仪器标识串.Map(equipmentId : 地点 + 标号 + 仪器地址)
	 * @date:2009-11-2
	 */
	public Map<Integer, String> getEquiMapStr() {
		if (equiMapStr.size() <= 0){
			fillEquiStr();
		}
		return equiMapStr;
	}
	
	/**
	 * @describe: 获取单个仪器标识串.Map(equipmentId : 地点 + 标号 + 仪器地址)
	 * @date:2009-11-2
     */
	public String getEquiMapStrById(int  equipmentId) {
		if ( (equiMapStr.size() == 0) || (!equiMapStr.containsKey(equipmentId)) ){
			fillEquiStr();
		}
		return equiMapStr.get(equipmentId);
	}
	
	/**
	 * @describe: 删除对应的仪器标识串
	 * @param equipmentId : 仪器ID
	 * @date:2009-11-2
	 */
	public void removeEquiMapStr(int equipmentId) {
		if (equiMapStr.containsKey(equipmentId)) {
			equiMapStr.remove(equipmentId);
		}
	}

	/**
	 * @describe: 获取报警播放的文件
	 * @date:2009-11-2
     */
	public  String getWarnPlayFile() {
		if ((warnPlayFile == null) || (warnPlayFile.length() <= 0)) {
			warnPlayFile = getSysArgsByKey(BeanForSysArgs.ALARM_PLAYFILE);
		}
		return warnPlayFile;
	}

	/**
	 * @describe: 设置报警播放的文件
	 * @date:2009-11-2
     */
	public void setWarnPlayFile(String warnPlayFile) {
		this.warnPlayFile = warnPlayFile;
	}

	/**
	 * @describe: 获取系统播放报警标志 (0:未初始化 1:关闭 2：打开)
	 * @date:2009-11-2
     */
	public  int getWarnOpenFlag() {
		String open_pcSound = "";
		if (warnOpenFlag == 0) {
			open_pcSound = getSysArgsByKey(BeanForSysArgs.OPEN_PCSOUND);
			warnOpenFlag = Integer.parseInt(open_pcSound);
		}
		return warnOpenFlag;
	}

	/**
	 * @describe: 通过键值获取系统变量	
	 * @param argsKey 系统键值
	 * @return: 相应的系统变量
	 * @date:2009-11-29
	 */
	public String getSysArgsByKey(String argsKey){
		String rsStr= null;
		
		try {
			if ( sysArgs.containsKey(argsKey) ) {
				rsStr = sysArgs.get(argsKey);
			}else{// 第一次检查没有重新加载数据库 
				sysArgs = getAllSysArgs();
				if (sysArgs.containsKey(argsKey)){
					rsStr = sysArgs.get(argsKey);
				}
			}
		} catch (Exception e) {
			 // 加载后再没有就使用默认值
			rsStr = getDefSysArgsByKey(argsKey);
		}
		return rsStr;
	}
	
	/**
	 * @describe:	获取系统默认的参数
	 * @param argsKey
	 * @date:2009-11-29
	 */
	public String getDefSysArgsByKey(String argsKey){
		return BeanForSysArgs.getDefaultSysArgsMap().get(argsKey);
	}
	
	/**
	 * @describe: 获取系统的全部变量
	 * @return:
	 * @date:2009-11-29
	 */
	public Map<String, String> getAllSysArgs(){
		return setSysService.getSysParamMap();
	} 
	
	/**
	 * @describe:	重置系统变量
	 * @date:2009-11-29
	 */
	public void reSetSysArgs(){
		warnOpenFlag = 0;
		warnPlayFile = null;
		sysArgs.clear();
	}
	
	
	/**
	 * @describe: 设置是否播放报警文件(0:未初始化 1:不开 2：开)
	 * @date:2009-11-2
     */
	public void setWarnOpenFlag(int warnOpenFlag) {
		this.warnOpenFlag = warnOpenFlag;
	}

	/**
	 * @describe : 检查的刚得到数据是否是警报数据,如果是,加入警报列表warnList(Map:Integer, Record)
	 * @param record : 检查的Record类型数据
	 * @date:2009-11-2
     */
	public void checkDataWarn(Record record, EquipData equipData) {
		if (equipData == null) {return;}
		
		//进入的数据-[温度-湿度-eqID-记录时间]---报警需要增加有-[报警类型,仪器类型,仪器标签]
		int hasRang = 0; // 数据是否为报警记录(温度,湿度两项之和)
		String tempInfoMsg ="-"; // 温度相关的内容
		String humiInfoMsg ="-"; // 湿度相关的内容

		int alarmtype = 0;// 报警类型(温度超出范围 1:低于正常范围 2:高于正常范围) + (湿度超出范围 10:低于正常范围+20:高于正常范围)
		// 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
		int sysEqType = getSystemEqType(); 
		
		float tempFloat = 0;
		// 比较温度是否超出界限
		if (sysEqType != 3){
			if (equipData.getEquitype() != EquipData.TYPE_HUMI_ONLY){// 单温度和温湿度能检查温度
				tempFloat = Float.parseFloat(record.getTemperature());	// 温度
				int tempType = Integer.parseInt(
						getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // 温度显示类型(温度显示格式(1:摄氏℃ 2:华氏F))
				if (tempType == 2){
					tempFloat = tempFloat*9/5 + 32;// 显示为华氏的时候,转换格式
				}
				
				tempInfoMsg = tempFloat + (tempType==1?"℃":"F") + ",";
				if (tempFloat < (equipData.getTempDown())) {// 下限
					hasRang += 1;		alarmtype = 1;
					tempInfoMsg = tempInfoMsg + "低温报警";
				} else if (tempFloat > (equipData.getTempUp())) {// 上限
					hasRang += 1;		alarmtype = 2;
					tempFloat = tempFloat - equipData.getTempUp();
					tempInfoMsg = tempInfoMsg + "高温报警";
				} else {// 正常
					tempInfoMsg = tempInfoMsg + "正常";
				}
			}
		}// end if (sysEqType != 3)
		if (sysEqType != 2){
			if (equipData.getEquitype() != EquipData.TYPE_TEMP_ONLY){// 单湿度和温湿度能检查温度
				tempFloat = Float.parseFloat(record.getHumidity());			// 湿度
				// 比较湿度是否超出界限
				humiInfoMsg = tempFloat + "%RH,";
				if (tempFloat < (equipData.getHumiDown())) {// 下限
					hasRang += 1;		alarmtype += 10;
					humiInfoMsg = humiInfoMsg + "低湿报警";
				} else if (tempFloat > (equipData.getHumiUp())) {// 上限
					hasRang += 1;		alarmtype += 20;
					humiInfoMsg = humiInfoMsg + "高湿报警";
				} else {// 正常
					humiInfoMsg = humiInfoMsg + "正常";
				}
			}			
		}
		// 保存 超出界限的数据记录,并存入文件
		if (hasRang > 0) {
			// 对当前的报警数据,应该执行哪种操作:插入或者修改
			int equipmentId = record.getEquipmentId();
			BeanForAlarmRs rsBean = mainService.getDoWhich(equipmentId);			
			AlarmRec alarmRec = new AlarmRec();
			alarmRec.setAlarmEnd(record.getRecTime().getTime());
			
			if (rsBean.getRsType() == BeanForAlarmRs.ADD_ALARM){
				alarmRec.setEquipmentId(equipmentId);
				alarmRec.setTemperature(tempInfoMsg);
				alarmRec.setHumidity(humiInfoMsg);
				alarmRec.setAlarmStart(record.getRecTime().getTime());
				alarmRec.setAlarmtype(alarmtype);// 报警类型(温度超出范围 1:低于正常范围 2:高于正常范围) + (湿度超出范围 10:低于正常范围+20:高于正常范围)
				// 设置处理状态为[为复位]
				alarmRec.setState(2);//处理状态:1:已复位	2：未复位
				alarmRec.setUsername("暂无");// 设置用户名为:
				alarmRec.setAlarmmode(AlarmRec.NO_ACTION_MODE);// 设置处理模式[未处理:0 PC:1 GPRS:2]
				alarmRec.setPlaceName(equipData.getPlaceStr() + "-" + equipData.getMark());// 域名名
				alarmRec.setNormalArea(getAreaString(equipData));// 仪器正常范围
				alarmRec.setEquitype(equipData.getEquitype());// 温湿度类型：1,'温湿度';2,'单温度';3,'单湿度'
				
				mainService.insertAlarmRec(alarmRec);
				// 处理短信报警
				// 判断 -- 短信报警是否打开(1:关闭 2:打开) 和 短信通信串口是否打开  
				if ((Integer.parseInt(getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE))==2)&&(simCard_Unit.isRunFlag())){
					// 获取发送短信的手机列表
					BeanForSms smsBean = manaService.getPlacePhones(equipmentId);  // 获取仪器关联地址的主键
					List<String> phoneList = setSysService.getPhones(smsBean.getPhonelist());// 获取仪器关联地址的手机列表
					String msg = "仪器名:"+ alarmRec.getPlaceName()+ "\n" +
								"温度:" + tempInfoMsg + "\n" +
								"湿度:"+humiInfoMsg + "\n" + 
								//"正常范围:" + alarmRec.getNormalArea()+ "\n" +
								"时间:" + FunctionUnit.getDateToStr(record.getRecTime(),	FunctionUnit.Calendar_END_MINUTE, FunctionUnit.UN_SHOW_CHINESE);
					SmsRecord smsRec = null;
					for (String phoneNo : phoneList) {
						// simCard_Unit.sendMessage(getSysArgsByKey(BeanForSysArgs.SMS_CENTER_NUMBER), phoneNo, msg); // 放在模块计时器中处理
						// 把短信发送信息保存到数据库
						smsRec = new SmsRecord();
						smsRec.setSmsphone(phoneNo);				// 短信接收号码
						smsRec.setSmscontent(msg);					// 短信内容
						smsRec.setSmstype(1); 						// 发出短信的类型
						smsRec.setSmsrectime(record.getRecTime());	// 短信发送时间
						// mainService.insertSmsRec(smsRec);	 	// 放在模块计时器中处理
						queue4SimCard.offer(smsRec);				// 把需要发送的短信加入列队
					}
				}
			}else if (rsBean.getRsType() == BeanForAlarmRs.UPDATE_ALARM){
				alarmRec.setAlarmId(rsBean.getAlarmId());// 设置报警编号,用来执行语句 (update talarmrec set alarmEnd = #alarmEnd# where alarmId = #alarmId#)
				mainService.updateAlarmRecing(alarmRec);
			}
		}// end (hasRang > 0)
			
	}// end public
	
	/**
	 * 得到队列前面的一个SmsRecord元素
	 */
	public SmsRecord pollSimCard(){
		return queue4SimCard.poll();
	}
	
	/**
	 * @describe: 获取 仪器正常范围. 如:1.0~10.0℃/0.0~100.0%RH
	 * @param equipmentId 仪器ID
	 * @date:2009-11-5
	 */	
	public String getAreaString(int  equipmentId){
		EquipData eq = getEquipByID(equipmentId);
		return getAreaString(eq);
	}	
	
	/**
	 * @describe: 获取 仪器正常范围. 如:1.0~10.0℃/0.0~100.0%RH
	 * @param eq 仪器信息
	 * @date:2009-11-5
	 */	
	public String getAreaString(EquipData eq){
		StringBuffer rsStrBuf = new StringBuffer();
		if (eq == null){
			rsStrBuf.append("-");
		}else{
			// 温度显示格式(1:摄氏 2:华氏)
			String str = Integer.parseInt(getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE))==1?"℃":"F";
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
	 * @describe: 读取全部的区域信息	 
	 * @date:2009-11-30
	 */
	public  List<Workplace> getAllWorkPlaceList() {
		if (allWorkPlaceList.size() <= 0){
			reSetAllWorkPlaceList();
		}
		return allWorkPlaceList;
	}

	/**
	 * @describe: 重新 获取全部区域
	 * @param allWorkPlaceList:
	 * @date:2009-11-30
	 */
	public  void reSetAllWorkPlaceList() {
		this.allWorkPlaceList = manaService.getAllWorkplace(1); 
	}

	/**
	 * @describe: 没有默认选择仪器时,创建仪器列表在页面上的显示区域
	 * 格式:map(区域名,区域中的仪器列表)
	 * @param selEquipId : 需要默认选中的仪器Id数组
	 * @return:
	 * @date:2009-11-30
	 */
	public  Map<String, String> createEqAreaWithNOSel(List<Integer> selEquipIds){
		Map<String, String> showArea = new HashMap<String, String>();  // 返回的map对象
		StringBuffer strBuf = new StringBuffer(); // 临时变量
		boolean isCheck = false;				  // 临时变量--是否要选中	
		String areaName ;						  // 临时变量 --区域名
		Map<Integer, EquipData> allEquip = getEquiMap(); 	// 获取全部仪器
		if (allEquip.size() <= 0){return showArea;}
		
		List<Workplace> workPlace = getAllWorkPlaceList();  // 获取全部区域
		EquipData tempEquip = null;					  		// 临时变量--仪器属性
		int equipId ;
		for (int i = 0; i < workPlace.size(); i++) {
			areaName = workPlace.get(i).getPlaceName();		// 区域名
			strBuf.delete(0, strBuf.length());
			
			for (Entry<Integer, EquipData> entry : allEquip.entrySet()) {
				equipId = entry.getKey();
				tempEquip = null;	tempEquip = entry.getValue();
				if ( areaName.equals(tempEquip.getPlaceStr()) ){
					isCheck = selEquipIds == null ? false : selEquipIds.contains(equipId); // 查看是否需要选中
					strBuf.append(create_Html_P(tempEquip.getEquipmentId(),tempEquip.getMark(),isCheck));
				}				
			}// end for
			
			// 包装对应html元素
			if (strBuf.length() > 0) {
				showArea.put(areaName, strBuf.toString());
			}
			
		}// end fof
		return showArea;
	}
	
	/**
	 * @describe: 创建Html中td元素信息	
	 * @param equipmentId : 仪器主键
	 * @param label 显示的标签
	 * @param isCheck 是否选中(true:选择 false:不选择)
	 * @return:  填充后的p元素信息
	 * @date:2009-11-30
	 */
	public static String create_Html_P(int equipmentId, String label, boolean isCheck){
		// 格式如下://检测最多显示12汉字(12个字节)
//		 <li>		
//			<label for="undo_reset" title="ttttt">						
//				<input type="checkbox"	name="resetBox" id="undo_reset" value="2" />未复位
//			</label>
//		</li>
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<li>");
		strBuf.append("<label for=\"eq"+ equipmentId + "\" title=\"" +label+ "\">");//提示信息
		strBuf.append("<input type=\"checkbox\" id=\"eq" + equipmentId + "\" value=\"" +equipmentId+ "\"");
		strBuf.append(isCheck?" checked=\"checked\"":"");
		strBuf.append(" />");//checkbox 
		//strBuf.append(FunctionUnit.substringByByte(label, 24, "..."));//最多获取12个汉字  
		strBuf.append(label);//最多获取12个汉字  
		strBuf.append("</label>");
		strBuf.append("</li>");
		
		return strBuf.toString();
	}
	
	/**
	 * @describe: 创建Html中td元素信息	
	 * @param equipmentId : 仪器主键
	 * @param label 显示的标签
	 * @param isCheck 是否选中(true:选择 false:不选择)
	 * @return:  填充后的p元素信息
	 * @date:2009-11-30
	 */
	public static String create_Html_P(String equipmentId, String label, boolean isCheck){
		// 格式如下://检测最多显示12汉字(12个字节)
//		 <li>		
//			<label for="undo_reset" title="ttttt">						
//				<input type="checkbox"	name="resetBox" id="undo_reset" value="2" />未复位
//			</label>
//		</li>
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("<li>");
		strBuf.append("<label for=\"eq"+ equipmentId + "\" title=\"" +label+ "\">");//提示信息
		strBuf.append("<input type=\"checkbox\" id=\"eq" + equipmentId + "\" value=\"" +equipmentId+ "\"");
		strBuf.append(isCheck?" checked=\"checked\"":"");
		strBuf.append(" />");//checkbox 
		//strBuf.append(FunctionUnit.substringByByte(label, 24, "..."));//最多获取12个汉字  
		strBuf.append(label);//最多获取12个汉字  
		strBuf.append("</label>");
		strBuf.append("</li>");
		
		return strBuf.toString();
	}
	
	/**
	 * @describe: 获取区域和仪器的对应列表	
	 * @date:2010-1-19
	 */
	public  Map<String,Map<Integer,BeanForLabel>> area2Equipment(){
		Map<String,Map<Integer,BeanForLabel>> rsMap = new ConcurrentHashMap<String,Map<Integer,BeanForLabel>>();
		List<Workplace> workPlace = getAllWorkPlaceList();	// 获取全部区域
		Map<Integer, EquipData> allEquip = getEquiMap();	// 获取全部仪器
		
		String areaName ;									// 临时变量 --区域名
		EquipData tempEquip = null;					  		// 临时变量--仪器属性
		int equipId ;										// 临时变量--仪器ID
		Map<Integer,BeanForLabel> tempArea = null;				// 临时变量--区域对象
		BeanForLabel bean;
		
		for (int i = 0; i < workPlace.size(); i++) {
			areaName = workPlace.get(i).getPlaceName();		// 区域名
			tempArea = null;	tempArea = new ConcurrentHashMap<Integer, BeanForLabel>();
			for (Entry<Integer, EquipData> entry : allEquip.entrySet()) {
				equipId = entry.getKey();
				tempEquip = null;	tempEquip = entry.getValue();	
				if ( areaName.equals(tempEquip.getPlaceStr()) ){
					bean = new BeanForLabel();
					bean.setAreaName(areaName);
					bean.setDsrsn(tempEquip.getDsrsn());
					bean.setMark(tempEquip.getMark());
					tempArea.put(equipId, bean);
				}
			}
			rsMap.put(areaName, tempArea);
		}
		return rsMap;
	}
	
	public String comName(){
		String str = getSysArgsByKey(BeanForSysArgs.COMPANY_NAME);
		return str;
	}

	/**
	 * 判断 access中数据是否做了清空处理<br>
	 * accessDataEmpty=true 清空,无数据<br>
	 * accessDataEmpty=false 有数据<br>
	 */
	public  boolean isAccessDataEmpty() {
		return accessDataEmpty;
	}

	/**
	 * 设置 access中数据是否做了清空处理<br> 
	 * accessDataEmpty=true 清空,无数据<br>
	 * accessDataEmpty=false 有数据<br>
	 */
	public  void setAccessDataEmpty(boolean accessDataEmpty) {
		this.accessDataEmpty = accessDataEmpty;
	}

	/**
	 * 每10分钟,向access数据库中插入或者修改数据标记:
	 * 间隔1000 * 60 * 10
	 */
	public  long getAccessTimeFalg() {
		return accessTimeFalg;
	}

	/**
	 * 每10分钟,向access数据库中插入或者修改数据标记:
	 * 间隔1000 * 60 * 10
	 */
	public  void setAccessTimeFalg(long accessTimeFalg) {
		this.accessTimeFalg = accessTimeFalg;
	}
	
	/**
	 * access中数据连接状态<br/>
	 * accessLinkState=true 连接正常<br/>
	 * accessLinkState=false 连接失败<br/>
	 * 默认是TRUE
	 */
	public  boolean isAccessLinkState() {
		return accessLinkState;
	}

	/**
	 * access中数据连接状态<br/>
	 * accessLinkState=true 连接正常<br/>
	 * accessLinkState=false 连接失败<br/>
	 * 默认是TRUE
	 */
	public  void setAccessLinkState(boolean accessLinkState) {
		this.accessLinkState = accessLinkState;
	}
	
	/**
	 * @describe:	系统环境变化时,重置系统相关信息<br>
	 * @param reArea: 是否重置 区域信息<br>
	 * @param reEq:   是否重置 仪器信息<br>
	 * @param reAccess: 是否重置 Access信息<br>
	 * @param rsSys: 是否重置 系统信息<br>
	 * @param rsMinRec: 是否重置 总览和实时曲线相关的数据库<br>
	 * @date:2010-3-22
	 */
	public synchronized  void resetSystem(boolean reArea, 
			boolean reEq, boolean reAccess, boolean rsSys, boolean rsMinRec){
		if (reArea){ // 是否重置 区域信息
			reSetAllWorkPlaceList();
		}
		if (reEq){ // 是否重置 仪器信息
			fillEquiMap();
			fillEquiStr();
			reSetSystemEqType();
		}
		if (reAccess){ // 是否重置 Access信息
			if (serviceAccess.deleteAll()){
				setAccessDataEmpty(true);
			};			
		}
		if (rsSys){ // 是否重置 系统信息
			reSetSysArgs();
		}
		if (rsMinRec){//是否重置 总览和实时曲线相关的数据库
			// 先删除全部数据
			mainService.truncateMinRecord();
			//MainService.getInstance().deleteMinAll();
			// 结合"即时曲线 点数的限制数", 再添加临时的空数据
			mainService.insertMinRecord(getEquiList(), 
					Integer.parseInt(getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_LINE)));
		}
	}
	
	/**
	 * 仪器是否在运行，串口正在采集数据时eqDoingData为true,采集完后eqDoingData为false
	 */
	public synchronized  boolean isEqDoingData() {
		return eqDoingData;
	}
	/**
	 * 仪器是否在运行，串口正在采集数据时eqDoingData为true,采集完后eqDoingData为false
	 */
	public synchronized  void setEqDoingData(boolean eqDoingData) {
		this.eqDoingData = eqDoingData;
	}

	/**
	 * @describe:	需要连接到access的点数
	 * @date:2010-3-26
	 */
	public  int getAccessLinkCount() {
		return accessLinkCount;
	}
	
	/**
	 * 短信模块是否在处理任务. 处理任务时smsDoingData为true,处理完后smsDoingData为false 
	 */
	public synchronized boolean isSmsDoingData() {
		return smsDoingData;
	}
	/**
	 * 短信模块是否在处理任务. 处理任务时smsDoingData为true,处理完后smsDoingData为false 
	 */	
	public synchronized void setSmsDoingData(boolean smsDoingData) {
		this.smsDoingData = smsDoingData;
	}
	
}