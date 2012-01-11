package com.htc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.googlecode.jsonplugin.annotations.JSON;
import com.htc.bean.BeanForFlashData;
import com.htc.bean.BeanForFlashSetting;
import com.htc.bean.BeanForPortData;
import com.htc.bean.BeanForSysArgs;
import com.htc.bean.BeanForlBarData;
import com.htc.common.FunctionUnit;
import com.htc.domain.EquipData;
import com.htc.domain.User;
import com.htc.model.MainService;
import com.htc.model.UserService;
import com.htc.model.seriaPort.Level_Second_Serial;
import com.htc.util.CollectionUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * @ MainRecentDataAction.java
 * 作用 : 即时数据功能action. 在总览画面, 实时曲线, 串口调试中显示即时数据.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class MainRecentDataAction extends AbstractAction {

	// 服务类
	private UserService userService;
	private MainService mainService;
	
	// 即时曲线 和总览画面   共用
	private Map<String, String> areaToEqList;		// 页面地址选择用的map(区域名,区域中的仪器列表)
	private String userPlaceList;					// 用户所使用的地址列表
	private int palyFalg;							// 设置声音开启状态设置声音开启状态 (1:不播放  2:播放 3:系统关闭播放)
	private String alarmPlayFile = ""; 				// 报警播放文件
	
	/**
	 * 用来选择地址列表的标签.(1:placeAStr(总览)  2:placeBStr(实时))
	 */
	private int tagFlag;							// 用来选择地址列表的标签.(1:placeAStr(总览)  2placeBStr(实时))
	private int falshTime;							// 页面刷新时间-同串口获取时间
	private int maxSelector;						// 页面最多能显示的地址数量
	
	// 即时曲线
	private String dataStr;							// 数据字符				
	private String configStr;						// 配置字符				
	
	// 总览画面
	private Map<Integer, BeanForlBarData> barData;	// 数据集 		
	private String recTimeStr = "";					// 最新更新记录数据的时间
	
	private Map<Integer, WorkPlaceEntity> workPlaceBarDatas;	// 数据集 		
	
	// 统计系统中所用仪器类型情况: 1:系统中既有温度又有湿度仪器		2:系统中全部是温度仪器	3:系统中全部是湿度仪器
	@SuppressWarnings("unused")
	private int systemEqType;		
	
	
	// 构造方法
	public MainRecentDataAction() {
	}
	//注册service -- spring ioc
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}

	@Override
	public String execute() {
		initPer();	
		doSomeBefore();
		return SUCCESS;
	}
	
	/**
	 * @describe: 初始化信息
	 * @date:2009-12-2
	 */
	 public void initPer(){
		
		User user = getCurrrentUser();
		boolean rsFlag = user != null;
		userPlaceList = "0";
		// 用户所使用的地址列表 和 页面最多能显示的地址数量
		if (tagFlag == 1){
			if (rsFlag){userPlaceList = user.getPlaceAStr();}
			maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_BAR));
		}else if (tagFlag == 2){
			if (rsFlag){userPlaceList = user.getPlaceBStr();}
			maxSelector = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_SELECT_COUNT_LINE));
			
			// 设置session
			ActionContext ctx = ActionContext.getContext();
			ctx.getSession().put("user_flash_list", userPlaceList); // falsh需要显示的个人地址列表		
		}
		
		List<Integer> placeList = FunctionUnit.getIntListByStr(userPlaceList);
		//页面地址选择用的map(区域名,区域中的仪器列表)
		areaToEqList = commonDataUnit.createEqAreaWithNOSel(placeList);
		userPlaceList = userPlaceList.trim().length() == 0?"0":userPlaceList;
	}
	
	/**
	 * @describe: 从总览画面跳转到实时曲线画面
	 * @date:2009-12-27
	 */
	public String jumpToLine(){
		String tempStr = userPlaceList;
		initPer();
		// 设置session
		ActionContext ctx = ActionContext.getContext();
		ctx.getSession().put("user_flash_list", tempStr); // falsh需要显示的个人地址列表		
		return SUCCESS;
	}
	
	/**
	 * @describe: 获取正在访问的用户名	
	 * @return: null表示用户没有登录,或者数据获取失败
	 * @date:2009-11-30
	 */
	@JSON(deserialize=false,serialize=false)
	public User getCurrrentUser(){
		User user  = null;
		//1: 检测是否登录,未登录,则退出
		String userName = getSysUserName();
		//2: 判断是不是商家
		userName = userName.equals(FACTORY_NAME) ? OEM_NAME : userName;
		if (userName != null){
			// 实例化服务类
			//userService = userService == null ? new UserService() : userService;
			user = userService.getUserByName(userName);
		}		

		return user;
	}
	
	//柱状图
	public String barChartJson(){
		fillBarFlashData2();		//封装flash数据，并检测是否有数据存在
		return SUCCESS;
	}
	
	/**
	 * @describe: 前置处理:  获取用户使用的地址列表
	 * @return 有地址 : true  无地址 : false 
	 * @throws Exception:
	 * @date:2009-11-4
	 */
	public boolean doSomeBefore(){
		// 页面刷新时间
		falshTime = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
		boolean rsbool = userPlaceList == null ? false : !userPlaceList.equals("0");
		return rsbool;
	}
	
	/**
	 * @describe: 修改个性化配置(地址列表), 保存到数据库	
	 * @date:2009-11-4
	 */
	public String updateUserConfig() {
		User user = getCurrrentUser();
		if (user != null){
			if (userPlaceList == null){initPer(); return SUCCESS;} // 防止加密URL被手工切断
			// (1:placeAStr(总览)  2placeBStr(实时))
			// userPlaceList = userPlaceList.equals("")?"0":userPlaceList.substring(0, userPlaceList.length()-1);//在js中已经截断了最后一个逗号
			if (tagFlag == 1){
				user.setUpdateFlag(1);//1: 标识修改placeAStr
				user.setPlaceAStr(userPlaceList);
			}else if (tagFlag == 2){
				user.setUpdateFlag(2);//2: 标识修改placeBStr
				user.setPlaceBStr(userPlaceList);
			}			
			// 实例化服务类
			//userService = userService == null ? new UserService() : userService;
			userService.updateUser(user);
			// addActionMessage("更新列表成功...");
		}else{
			// addFieldError("updateconfigFailure", "更新列表失败...");
			// 用户不存在权限级别为0,显示无权访问
			ActionContext ctx = ActionContext.getContext();
			ctx.getSession().put(user_POWER_TAG, 0);//个人权限session 86：厂家权限				
		}
		initPer();
		return SUCCESS;
	}
	
	/**
	 * @describe: 检测是否有数据存在性,然后 封装bar flash数据.
	 * @date:2009-11-5
	 * @deprecated see fillBarFlashData2
	 */
	public void fillBarFlashData(){
		BeanForPortData serialPortDataBean;			// 从串口产生的数据格式
		BeanForlBarData serialBarDataBean;			// 封装到页面的数据格式
		EquipData equipData ; 						// 仪器信息
		int tempType;								// 显示温度类型(1:摄氏	2:华氏)
		int equipmentId;
		String temp, humi, colorTemp,colorHumi ,label, state, power;
		
		barData = null;		//清理缓存
		barData = new TreeMap<Integer, BeanForlBarData>();
		
		// 得到用户自身所使用的地址列表 --经过排序的
		List<Integer> listPlaces = getAddressList(userPlaceList);
		
		//串口服务 -- 获取串口即时数据
		//Level_First_Serial  first_Level = Level_First_Serial.getInstance();
		//Map<Integer, BeanForPortData> tempDataBean = first_Level.getAddressData();
		// 从数据库获取 最新数据
		Map<Integer, BeanForPortData> tempDataBean = mainService.getNewestMinRec(userPlaceList);
		
		//	显示温度类型
		tempType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));
			
		// 根据地址列表, 封装要显示的数据
		userPlaceList = "";
		recTimeStr = "";
		int len = listPlaces.size();
		int realTemp=0;//真实的温度值.摄氏时不变。华氏时要做转换
		for (int i = 0; i < len; i++) {
			
			// 过滤不正确的仪器ID
			equipmentId = listPlaces.get(i);//euipmentId
			if (equipmentId == 0) {continue;}
			
			// 并把排序后的结果赋值给userPlaceList,这样页面就能按仪器顺序正确显示
			userPlaceList += equipmentId + ((i+1) == len? "" : ",");
			
			// 得到选择仪器信息
			equipData = null;
			equipData = commonDataUnit.getEquipByID(equipmentId);
			
			// 获取单个数据集合
			serialPortDataBean = null;
			serialPortDataBean = tempDataBean == null ? null : 
					tempDataBean.containsKey(equipmentId) ? tempDataBean.get(equipmentId) : null;
			serialPortDataBean = serialPortDataBean == null? null : serialPortDataBean.getMark()==0? 
					null: serialPortDataBean;
					
			// 数据bean
			serialBarDataBean = null ;
			serialBarDataBean = new BeanForlBarData(); 
			
			// 封装数据  到 页面数据
			label = null; temp = null; humi = null; colorTemp = null; colorHumi = null; state = null;
			serialBarDataBean.setEquipmentId(equipmentId);										// 主键--已存在bean里面
			label = commonDataUnit.getEquiMapStrById(equipmentId);
//			label = FunctionUnit.substringByByte( 			
//					commonDataUnit.getEquiMapStrById(equipmentId), 34, "...");					// 标签 -- 现在在页面中处理
			serialBarDataBean.setLabel(label);
			temp = serialPortDataBean == null ? "-" : equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY?"-" :
				getTempHumiStr(serialPortDataBean.getTemp(), tempType);	// 温度
			realTemp = temp.equals("-")?0:(int) (Float.parseFloat(temp)*100);
			serialBarDataBean.setTemp(temp);
			serialBarDataBean.setAppt(tempType == 1 ? "℃" : "F"); 		//温度显示后缀(1:摄氏 2:华氏)
			humi = serialPortDataBean == null ? "-" : equipData.getEquitype()== EquipData.TYPE_TEMP_ONLY?"-" :
				getTempHumiStr(serialPortDataBean.getHumi(), 0);		// 湿度
			serialBarDataBean.setHumi(humi);
			serialBarDataBean.setApph("%RH");							// 湿度显示后缀
			if ( serialPortDataBean == null ){	
				colorTemp = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF); 		// 没有数据则显示低温报警颜色
				colorHumi = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF); 		// 没有数据则显示低温报警颜色
				state = "暂无数据";
				power = equipData == null? "暂无电量" : equipData.getShowPower() == 1 ? "暂无电量" : "";
			}else{
				colorTemp = equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY?
						commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF) :
						getColorString(realTemp, 1, equipData );
						
				colorHumi = equipData.getEquitype()== EquipData.TYPE_TEMP_ONLY ?
						commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF):	
					getColorString(serialPortDataBean.getHumi(), 2, equipData );
						
				state = getStateStr(tempType, realTemp,serialPortDataBean.getHumi(), equipData );
				power = equipData.getShowPower() == 1 ? getPowerString
						(serialPortDataBean.getPowerV(),equipData.getPowerType()) : "";
				serialBarDataBean.setStateInt(serialPortDataBean.getState());			//暂无用处(保留)
				//最新更新记录数据的时间
				if ( recTimeStr.length() < 5 ){
					recTimeStr = FunctionUnit.getTime_Long_Str(serialPortDataBean.getRecTime());
				}
			}
			serialBarDataBean.setPower(power);												// 电量
			serialBarDataBean.setColorTemp(colorTemp);										// 温度颜色		
			serialBarDataBean.setColorHumi(colorHumi);										// 湿度颜色		
			serialBarDataBean.setStateStr(state);											// 状态
			//保存数据
			barData.put(equipmentId, serialBarDataBean); 
		}//end for
	}
	
	public static class WorkPlaceEntity {
		String name;
		List<BeanForlBarData> barDatas = CollectionUtil.newArrayList();
		public WorkPlaceEntity(String name) {
			super();
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<BeanForlBarData> getBarDatas() {
			return barDatas;
		}
		public void addData(BeanForlBarData beanForlBarData) {
			barDatas.add(beanForlBarData);
		}
	}
	
	public void fillBarFlashData2(){
		BeanForPortData serialPortDataBean;			// 从串口产生的数据格式
		BeanForlBarData serialBarDataBean;			// 封装到页面的数据格式
		EquipData equipData ; 						// 仪器信息
		int tempType;								// 显示温度类型(1:摄氏	2:华氏)
		int equipmentId;
		String temp, humi, colorTemp,colorHumi ,label, state, power;
		
		barData = null;		//清理缓存
		barData = new TreeMap<Integer, BeanForlBarData>();
		workPlaceBarDatas = CollectionUtil.newTreeMap();
		
		// 得到用户自身所使用的地址列表 --经过排序的
		List<Integer> listPlaces = getAddressList(userPlaceList);
		
		//串口服务 -- 获取串口即时数据
		//Level_First_Serial  first_Level = Level_First_Serial.getInstance();
		//Map<Integer, BeanForPortData> tempDataBean = first_Level.getAddressData();
		// 从数据库获取 最新数据
		Map<Integer, BeanForPortData> tempDataBean = mainService.getNewestMinRec(userPlaceList);
		
		//	显示温度类型
		tempType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE));
			
		// 根据地址列表, 封装要显示的数据
		userPlaceList = "";
		recTimeStr = "";
		int len = listPlaces.size();
		int realTemp=0;//真实的温度值.摄氏时不变。华氏时要做转换
		
		Set<Integer> workPlaceIdSet = new HashSet<Integer>();
		
		for (int i = 0; i < len; i++) {
			
			// 过滤不正确的仪器ID
			equipmentId = listPlaces.get(i);//euipmentId
			if (equipmentId == 0) {continue;}
			
			// 并把排序后的结果赋值给userPlaceList,这样页面就能按仪器顺序正确显示
			userPlaceList += equipmentId + ((i+1) == len? "" : ",");
			
			// 得到选择仪器信息
			equipData = null;
			equipData = commonDataUnit.getEquipByID(equipmentId);
			if (equipData != null) {
				workPlaceIdSet.add(equipData.getPlaceId());
			}
			
			// 获取单个数据集合
			serialPortDataBean = null;
			serialPortDataBean = tempDataBean == null ? null : 
					tempDataBean.containsKey(equipmentId) ? tempDataBean.get(equipmentId) : null;
			serialPortDataBean = serialPortDataBean == null? null : serialPortDataBean.getMark()==0? 
					null: serialPortDataBean;
					
			// 数据bean
			serialBarDataBean = null ;
			serialBarDataBean = new BeanForlBarData(); 
			
			// 封装数据  到 页面数据
			label = null; temp = null; humi = null; colorTemp = null; colorHumi = null; state = null;
			serialBarDataBean.setEquipmentId(equipmentId);										// 主键--已存在bean里面
			label = commonDataUnit.getEquiMapStrById(equipmentId);
//			label = FunctionUnit.substringByByte( 			
//					commonDataUnit.getEquiMapStrById(equipmentId), 34, "...");					// 标签 -- 现在在页面中处理
			serialBarDataBean.setLabel(label);
			temp = serialPortDataBean == null ? "-" : equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY?"-" :
				getTempHumiStr(serialPortDataBean.getTemp(), tempType);	// 温度
			realTemp = temp.equals("-")?0:(int) (Float.parseFloat(temp)*100);
			serialBarDataBean.setTemp(temp);
			serialBarDataBean.setAppt(tempType == 1 ? "℃" : "F"); 		//温度显示后缀(1:摄氏 2:华氏)
			humi = serialPortDataBean == null ? "-" : equipData.getEquitype()== EquipData.TYPE_TEMP_ONLY?"-" :
				getTempHumiStr(serialPortDataBean.getHumi(), 0);		// 湿度
			serialBarDataBean.setHumi(humi);
			serialBarDataBean.setApph("%RH");							// 湿度显示后缀
			if ( serialPortDataBean == null ){	
				colorTemp = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF); 		// 没有数据则显示低温报警颜色
				colorHumi = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF); 		// 没有数据则显示低温报警颜色
				state = "暂无数据";
				power = equipData == null? "暂无电量" : equipData.getShowPower() == 1 ? "暂无电量" : "";
			}else{
				colorTemp = equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY?
						commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF) :
						getColorString(realTemp, 1, equipData );
						
				colorHumi = equipData.getEquitype()== EquipData.TYPE_TEMP_ONLY ?
						commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF):	
					getColorString(serialPortDataBean.getHumi(), 2, equipData );
						
				state = getStateStr(tempType, realTemp,serialPortDataBean.getHumi(), equipData );
				power = equipData.getShowPower() == 1 ? getPowerString
						(serialPortDataBean.getPowerV(),equipData.getPowerType()) : "";
				serialBarDataBean.setStateInt(serialPortDataBean.getState());			//暂无用处(保留)
				//最新更新记录数据的时间
				if ( recTimeStr.length() < 5 ){
					recTimeStr = FunctionUnit.getTime_Long_Str(serialPortDataBean.getRecTime());
				}
			}
			serialBarDataBean.setPower(power);												// 电量
			serialBarDataBean.setColorTemp(colorTemp);										// 温度颜色		
			serialBarDataBean.setColorHumi(colorHumi);										// 湿度颜色		
			serialBarDataBean.setStateStr(state);											// 状态
			//保存数据
			//barData.put(equipmentId, serialBarDataBean); 
			appendBarData(workPlaceBarDatas, equipData.getPlaceId(), serialBarDataBean, equipData.getPlaceStr());
		}//end for
		
	}
	
	private void appendBarData(Map<Integer, WorkPlaceEntity> workPlaceBarData, int workPlace, BeanForlBarData beanForlBarData, String placeName) {
		if (!workPlaceBarData.containsKey(workPlace)) {
			workPlaceBarData.put(workPlace, new WorkPlaceEntity(placeName));
		}
		WorkPlaceEntity entity = workPlaceBarData.get(workPlace);
		entity.addData(beanForlBarData);
	}
	
	/**
	 * @describe:	获取电量字符串	
	 * @param powerV powerV 电量
	 * @param type 电量类型(电量类型[1:1.5v,2:3.6v])
	 * @return:
	 * @date:2009-12-28
	 */
	private String getPowerString(float powerV,int type) {
		String rsOut= "电量不足";
		if (type == 1){//正常区域:1200<Power<2000
			if ((powerV > 1200) && (powerV < Level_Second_Serial.POWER_1_5)){
				rsOut = "电量正常";
			}
		}else if (type == 2){//正常区域:power>3000
			if (powerV > Level_Second_Serial.POWER_3_6){
				rsOut = "电量正常";
			}			
		}
		return rsOut;
	}

	/**
	 * @describe: 温度或者湿度字符串/100 + 偏差
	 * @param tempOrHuim int类型温度或者湿度数据 
	 * @param type 温度时显示(1:摄氏  2:华氏	 0:代表湿度)
	 * @date:2009-11-5
	 */
	public String getTempHumiStr(float tempOrHuim, float type){
		// F=(C*9/5)+32	℃=用摄氏表示的温度  F=用华氏表示的温度
		String outStr ;
		float floatValue;
		if (type == 2){
			floatValue = tempOrHuim / (float) 100;
			floatValue = floatValue * 9/5 + 32;
		}else{
			floatValue = tempOrHuim / (float) 100;
		}
		outStr = FunctionUnit.FLOAT_DATA_FORMAT.format(floatValue);
		return outStr;
	}
	
	/**
	 * @describe: 获取标签颜色字符串	
	 * @param tempHumi : 温湿度值
	 * @param type (类型1:温度 2:湿度)
	 * @param equipData ：仪器信息
	 * @return: 标签颜色
	 * @date:2009-11-5
	 */
	public String getColorString(float tempHumi, int type, EquipData equipData){
		String outStr = "";
		float tmpInt=0, tmpDown=0, tmpUp = 0;

		tmpInt = tempHumi /(float) 100 ;
		tmpDown = type == 1 ? equipData.getTempDown() : equipData.getHumiDown();
		tmpUp = type == 1 ? equipData.getTempUp() : equipData.getHumiUp();
		
		if( tmpInt < tmpDown ){
			// 低于范围时显示的颜色
			outStr = commonDataUnit.getSysArgsByKey(BeanForSysArgs.LOW_COLORDEF);	
		}else if ( tmpInt > tmpUp ){
			// 高于范围时显示的颜色
			outStr=  commonDataUnit.getSysArgsByKey(BeanForSysArgs.HIGH_COLORDEF);
		}else{
			// 正常颜色
			outStr = commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF);
		}
		return outStr;
	}
	
	/**
	 * @describe: 获取状态字符串. 如: 温度, 湿度异常
	 * @param type 温度时显示(1:摄氏  2:华氏)
	 * @param temp 温度
	 * @param humi 湿度
	 * @param equipData 仪器信息
	 * @return: 运行状态结果
	 * @date:2009-11-5
	 */
	public String getStateStr( int type, float temp, float humi, EquipData equipData){
		float tmpFloat;
		int errorFlag = 0;
		String outStr = "";
		int equitype = equipData.getEquitype();// 1,'温湿度';2,'单温度';3,'单湿度'
		
		if (equitype!=3){
			// 温度加上偏差比最低温度界限还低,则这个是低温报警
			tmpFloat =  equipData.getTempDown() - temp/(float) 100 ;  
			if ( tmpFloat > 0){ 	// 温度偏低
				//tmpFloat = type == 1 ? tmpFloat : (tmpFloat * 9/5 + 32); 
				//outStr = "温度偏低:"+FunctionUnit.FLOAT_DATA_FORMAT.format(tmpFloat)+(type==1?"℃":"F");
				outStr = "温度偏低";
				errorFlag += 1 ;
			}
			// 温度加上偏差比最高温度界限还高,则这个是高温报警
			tmpFloat = temp/(float) 100 - equipData.getTempUp();  
			if ( tmpFloat > 0){ 	// 温度偏高
				//tmpFloat = type == 1 ? tmpFloat : (tmpFloat * 9/5 + 32); 
				//outStr = "温度偏高:"+FunctionUnit.FLOAT_DATA_FORMAT.format(tmpFloat)+(type==1?"℃":"F");
				outStr = "温度偏高";
				errorFlag += 1 ;
			}
		}
		
		if (equitype!=2){
			// 湿度加上偏差比最低湿度界限还低,则这个是低湿报警
			tmpFloat =  equipData.getHumiDown() - humi/(float) 100 ;  
			if (tmpFloat > 0){	// 湿度偏低
				errorFlag += 1 ;
				//outStr += "湿度偏低:" + FunctionUnit.FLOAT_DATA_FORMAT.format(tmpFloat) + "%";			
				outStr += " 湿度偏低";			
			}
			// 湿度加上偏差比最高湿度界限还高,则这个是高湿报警
			tmpFloat =  (humi/(float) 100 - equipData.getHumiUp());  
			if (tmpFloat > 0){	// 湿度偏高
				errorFlag += 1 ;
				// outStr += "湿度偏高:" + FunctionUnit.FLOAT_DATA_FORMAT.format(tmpFloat) + "%";			
				outStr += " 湿度偏高";			
			}
		}
		
		if (errorFlag == 0){
			outStr = "运行正常";
		}
		return outStr;
	}
	
	// flash 温度动态数据
	public String tempData(){
		dataStr = flashData(1); // (1:温度 2:湿度)
		return SUCCESS;
	}
	
	// flash 温度动态配置	
	public String tempConfig(){
		configStr = flashConfig(1); // (1:温度 2:湿度)
		return SUCCESS;
	}
	
	// flash 湿度动态数据	
	public String humiData(){
		dataStr = flashData(2); // (1:温度 2:湿度)
		return SUCCESS;
	}
	// flash 湿度动态配置	
	public String humiConfig(){
		configStr = flashConfig(2); // (1:温度 2:湿度)
		return SUCCESS;
	}
	
	/**
	 * @describe: 获取flash数据
	 * @param type (1:温度 2:湿度)
	 * @date:2009-12-5
	 */
	public String flashData(int type){
		
		List<Integer> equipmentIds = null;						// 仪器主键列表	
		Map<Integer,EquipData> equipDataList = new HashMap<Integer, EquipData>(); // 仪器信息列表
		
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // 温度显示类型(温度显示格式(1:摄氏 2:华氏))
		
		// 获取session - falsh需要显示的个人地址列表
		ActionContext ctx = ActionContext.getContext();
		userPlaceList = (String) ctx.getSession().get("user_flash_list");				
		// 获取 页面仪器主键列表
		equipmentIds = getAddressList(userPlaceList);
		
		// 加载数据区域
		List<String> headers = new ArrayList<String>();   // 横坐标列表
		Map<Integer, List<String>> graphs = new HashMap<Integer, List<String>>();
		List<String> graph = null;		
		EquipData  equipData = null; 
		long templong = -1;
		float floatVal;
		int equipId;		
		
		// 查询数据库, 获取目标数据
		List<BeanForPortData> dataLists = mainService.selectMinAllRec(userPlaceList);
		
		for (BeanForPortData bean : dataLists) {
			equipId = bean.getEquipmentId();
			// 横坐标值
			if (templong != bean.getRecLong()){
				templong = bean.getRecLong();
				headers.add(FunctionUnit.getTime_Short_Str(bean.getRecTime()));
			}
			// 获取仪器属性数据--主要用到(仪器类型和偏差值)
			if (!equipDataList.containsKey(equipId)){
				equipDataList.put(equipId,commonDataUnit.getEquipByID(equipId));			
			}
			// 创建或者取回 数据集
			if (graphs.containsKey(equipId)){
				graph = graphs.get(equipId);
			}else{
				graph = new ArrayList<String>();
			}
			equipData = equipDataList.get(equipId);
			if (type == 1){
				// 加载Flash数据区域--根据仪器类型添加数据
				if ((bean.getState() != 1) ||(bean.getMark() == 0) || // 过滤错误帧和空数据
						(equipData.getEquitype() == EquipData.TYPE_HUMI_ONLY) ){
					graph.add("");								// 温度值-单湿度时候不显示温度
				}else{
					floatVal = bean.getTemp() / (float) 100 ;
					if (tempType == 2){floatVal = floatVal * 9/5 + 32;}// 显示为华氏时是数值
					graph.add(String.valueOf(floatVal));		// 温度值
				}
			}else if (type == 2){
				if ( (bean.getState() != 1) ||(bean.getMark() == 0) || // 过滤错误帧和空数据
						(equipData.getEquitype() == EquipData.TYPE_TEMP_ONLY)){
					graph.add("");								// 湿度值-单温度时候不显示湿度
				}else{
					floatVal = bean.getHumi() / (float) 100 ;
					graph.add(String.valueOf(floatVal));		// 湿度值
				}
			}// end (type == 2)
			// 把数据保存到到集合中
			graphs.put(equipId, graph);
		}
		
		//--------------flash最大最小界限,标题--------------------//
		Map<Integer, String> equipLabels = new HashMap<Integer, String>();	// 仪器标签列表				
		float highdata = 10000, lowdata = 10000;
		for (int i = 0; i < equipmentIds.size(); i++) {
			equipId = equipmentIds.get(i);
			if (equipDataList.containsKey(equipId)){
				equipLabels.put(equipId, commonDataUnit.getEquiMapStrById(equipId));//仪器标签
				equipData = null; 	equipData = equipDataList.get(equipId);
				if (type == 1){
					floatVal = equipData.getTempUp();	
					highdata = highdata==10000?floatVal:highdata > floatVal ? floatVal : highdata;		// 高温值--取最小 的高温值
					floatVal = equipData.getTempDown();	
					lowdata = lowdata==10000?floatVal:lowdata < floatVal? floatVal : lowdata;			// 低温值--取最大 的低温值
				}else if (type == 2){
					floatVal = equipData.getHumiUp();	
					highdata = highdata==10000?floatVal:highdata > floatVal ? floatVal : highdata;		// 高湿值--取最小 的高湿值
					floatVal = equipData.getHumiDown();	
					lowdata = lowdata==10000?floatVal:lowdata < floatVal? floatVal : lowdata;			// 低湿值--取最大 的低湿值
				}
			}
		}
		
		// flash数据格式定义
		BeanForFlashData bean = new BeanForFlashData();
		bean.guide_start_value = String.valueOf(lowdata);				// 导向开始值-最小值		
		bean.guide_end_value = String.valueOf(highdata);				// 导向结束值-最大值
		bean.balloon_text_append = type == 2?"%RH":tempType==1?"℃":"F";	// 曲线节点上,气球显示的信息的后缀		
		bean.titles = equipLabels;										// flash曲线标题集合
		bean.headers = headers;											// flash中series元素(x轴
		bean.graphs = graphs;											// flash曲线数据集合
		
		return bean.toString();	// 返回温湿度数据字符
	}
	
	/**
	 * @describe: 获取flash配置 -有上下限线条, 没有放大功能
	 * @param type (1:温度 2:湿度)
	 * @date:2009-12-5
	 */
	public String flashConfig(int type){
		int tempType = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)); // 温度显示类型(温度显示格式(1:摄氏 2:华氏))
		
		// 温湿度 -- 加载配置区域
		BeanForFlashSetting configbean = new BeanForFlashSetting();
		configbean.data_type = "xml";								// flahs数据格式
		int reload_data_interval = Integer.parseInt(commonDataUnit.
				getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
		configbean.reload_data_interval = reload_data_interval;		// Falsh刷新间隔
		configbean.zoomable_use = false;							// 不使用放大功能
		if (type == 1){
			// 温度曲线
			configbean.label_name = "温度实时走势曲线";	// 头标签
			configbean.y_value_append = tempType == 1 ? "℃" : "F";		// Y轴附加标签
			configbean.legend_value_append = tempType == 1 ? "℃" : "F";
		}else if (type == 2){
			// 湿度曲线
			configbean.label_name = "湿度实时走势曲线";	// 头标签
			configbean.y_value_append = "%";
			configbean.legend_value_append = "%RH";
		}
		return configbean.toString();							// 返回配置字符
	}
	
	/**
	 * @describe: 根据输入 得到地址int(equipmentId) 数组	
	 * @param placeListStr 目标字符串
	 * @return:
	 * @date:2009-11-4
	 */
	public int[] getAddressArray(String placeListStr){
		String [] strArray = placeListStr.split(",");
		int[] intArray = new int[strArray.length];
		for (int i = 0; i < strArray.length; i++) {
			intArray[i] = Integer.parseInt(strArray[i].trim());
		}
		return intArray;
	}
	
	/**
	 * @describe: 根据仪器的顺序号. 输入安装顺序排列的地址List(equipmentId )
	 * @param placeListStr 目标字符串
	 * @date:2009-11-4
	 */
	public List<Integer> getAddressList(String placeListStr){
		List<Integer> intList = new ArrayList<Integer>();
		
		if (placeListStr == null){return intList;}
		if ( placeListStr.trim().length() == 0 ){return intList;}
		
		String [] equipmentIds = placeListStr.split(",");
		int equipmentId, runcount = 0;
		
		for (EquipData equipData : commonDataUnit.getEquiList()) {
			if( runcount == equipmentIds.length){break;}
			for (int i = 0; i < equipmentIds.length; i++) {
				equipmentId = Integer.parseInt(equipmentIds[i].trim());
				if (equipmentId == 0 ){continue;}
				if (equipData.getEquipmentId() ==  equipmentId){
					intList.add(equipmentId);
					runcount++;
					break;
				}
			}// end for内
		} // end for外
		
		return intList;
	}
	
	// ----------------以下为get，set方法----------------
	@JSON(deserialize=false,serialize=false)
	public String getAlarmPlayFile() {
		alarmPlayFile = commonDataUnit.getWarnPlayFile();
		return alarmPlayFile;
	}
	/**
	 * @describe: 获取声音开启状态 	
	 * @return: (1:不播放  2:播放)
	 * @date:2010-1-6
	 */	
	public int getPalyFalg() {
		// 保证有报警数据
		if (mainService.selectUndoRec().size()> 0){
			// 先判断系统有没有开启声卡播放
			palyFalg = commonDataUnit.getWarnOpenFlag();
			palyFalg =palyFalg == 2? 2:1;
		}else{
			palyFalg = 1;
		}
		return palyFalg;
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
	
	// ----------------普通 get，set方法----------------
	/**
	 * @describe: 填充地址列表,根据用户的保存记录,选择相应仪器checkbox
	 * @date:2009-11-30
	 */
	@JSON(deserialize=false,serialize=false)  
	public Map<String, String> getAreaToEqList() {
		return areaToEqList;
	}
	public String getUserPlaceList() {
		return userPlaceList;
	}
	public void setUserPlaceList(String userPlaceList) {
		this.userPlaceList = userPlaceList;
	}

	@JSON(deserialize=false,serialize=false) 
	public int getTagFlag() {
		return tagFlag;
	}

	public void setTagFlag(int tagFlag) {
		this.tagFlag = tagFlag;
	}

	public int getFalshTime() {
		return falshTime;
	}

	public void setFalshTime(int falshTime) {
		this.falshTime = falshTime;
	}
	
	@JSON(deserialize=false,serialize=false) 
	public int getMaxSelector() {
		return maxSelector;
	}

	public void setMaxSelector(int maxSelector) {
		this.maxSelector = maxSelector;
	}

	public Map<Integer, BeanForlBarData> getBarData() {
		return barData;
	}

	public void setBarData(Map<Integer, BeanForlBarData> barData) {
		this.barData = barData;
	}

	public String getRecTimeStr() {
		return recTimeStr;
	}

	public void setRecTimeStr(String recTimeStr) {
		this.recTimeStr = recTimeStr;
	}

	public String getDataStr() {
		return dataStr;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public String getConfigStr() {
		return configStr;
	}
	public Map<Integer, WorkPlaceEntity> getWorkPlaceBarDatas() {
		return workPlaceBarDatas;
	}
	public void setWorkPlaceBarDatas(Map<Integer, WorkPlaceEntity> workPlaceBarDatas) {
		this.workPlaceBarDatas = workPlaceBarDatas;
	}
	

}

