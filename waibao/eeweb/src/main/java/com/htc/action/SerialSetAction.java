package com.htc.action;

import java.util.*;

import com.htc.bean.BeanForSetData;
import com.htc.common.FunctionUnit;
import com.htc.model.seriaPort.*;

/**
 * @ SerialSetAction.java
 * 作用 : 串口 - 读取和设置硬件相关信息action.
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
@SuppressWarnings("serial")
public class SerialSetAction extends AbstractActionForOEM {
	
	// 页面显示
	private BeanForSetData serialData;				// 页面显示Bean
	private int baudRateNo = 9600;					// 波特率  默认: 9600 
	private Map<Integer, String>  baudRatelList;	// 波特率		-- 下拉框 
	
	// 页面 <s:checkbox/>
	private boolean agreeAddrsFalg;					// 是否修改地址
	private boolean addFlag;						// 是否 递增流水号和地址
	private boolean showPowerFlag;					// 是否显示电量信息
	private boolean showVision;						// 是否显示版本信息
	
	private int configId;							// 更改仪器其他常量 时用的  - 需要修改的ID
	private int configvalue;						// 更改仪器其他常量  时用的 - 需要修改的值
	private int configaddress;						// 更改仪器其他常量  时用的 - 需要修改的地址
	
	// 页面是否显示提示信息
	private int showTipMsg = 0;					// 0:不显示  1:显示
	
	private Level_First_Serial first_Level;
	//注册service -- spring ioc
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}

	//构造函数
	public SerialSetAction() {
	}

	//第一次显示用到
	public String execute(){
		showTipMsg = 0;
		return SUCCESS;
	}
	
	// 功能0：串口设置
	public String portSet(){
		showTipMsg = 1; // jsp页面-显示提示信息
		String messageInfo;
		//判断 串口调试  是否在运行
		//if (Level_First_Serial.getInstance().isRunningFlag() == true){
		if (first_Level.isRunningFlag() == true){
			messageInfo = getText("serialport.set.stop.running");
			addActionMessage(messageInfo);
			return SUCCESS;
		}else{
			// 测试串口是否可用 -- 自动搜索
			//if (Level_First_Serial.getInstance().initConfig(baudRateNo)){		// 连接正常 
			if (first_Level.initConfig(baudRateNo)){		// 连接正常 
				messageInfo = getText("serialport.port.is.usefull");
				addActionMessage(messageInfo);
			}else{														// 连接错误
				messageInfo = getText("serialport.port.not.usefull");
				addFieldError("configError", messageInfo);
			}
			return SUCCESS;
		}
	}
	
	/**
	 * @describe: 前置处理. 打开串口,初始化端口号和波特率
	 * @return true: 准备就绪		false: 串口有问题 
	 * @date:2009-11-6
	 */
	public boolean preAction(){
		showTipMsg = 1; // jsp页面-显示提示信息
		
		boolean rsBool = false;
		String messageInfo;
		//if (Level_First_Serial.getInstance().isRunningFlag() == true){
		if (first_Level.isRunningFlag() == true){
			messageInfo = getText("serialport.set.stop.running");
			addActionMessage(messageInfo);			
		}else{
			//Level_First_Serial.getInstance().initConfigVoid(baudRateNo);
			first_Level.initConfigVoid(baudRateNo);
			//rsBool = Level_First_Serial.getInstance().openPort();
			rsBool = first_Level.openPort();
			if (!rsBool){
				messageInfo = getText("serialport.set.port.error");
				addFieldError("configError", messageInfo);
			}
		}
		return rsBool;
	}
	
	//功能1：设置机器码,(页面增量显示)流水号加1, 设置地址,(页面增量显)地址加1
	public String setMacAddr(){
		boolean rsBool = false;
		String messageInfo;
		
		if (preAction()){// 检测准备情况
			//Level_First_Serial first_Level = Level_First_Serial.getInstance();
			
			//1:发送修改机号 命令
			rsBool = first_Level.setMac(serialData);
			if (rsBool){
				messageInfo = getText("serialport.set.mac.succes");
				addActionMessage(messageInfo);
				
				//2:发送修改地址 命令
				if ( agreeAddrsFalg == true ){
					rsBool =  first_Level.setAddr(serialData, Integer.parseInt(serialData.getAddress()) );
					if (rsBool){
						messageInfo = getText("serialport.set.addr.succes");
						addActionMessage(messageInfo);
					}else{
						messageInfo = getText("serialport.set.addr.failure");
						addFieldError("addressError", messageInfo);
					}//end if rsBool
				}//end if 2:agreeAddrs
				
				// 显示区信息
				if ( rsBool ) { 
					// 通过机号，读取软件版本号,通讯地址(用来读取电量)
					serialData = first_Level.readInfo(serialData);
					showMessInfo(first_Level);
				}
				
				//地址加1	和		流水号加1
				if ( rsBool && addFlag){
					// 地址
					serialData.setAddress(String.valueOf( Integer.parseInt(serialData.getAddress()) + 1) );
					// 流水号
					serialData.setMCNo(fillStringTo4(serialData.getMCNo()));
				}
			}else{
				messageInfo = getText("serialport.set.mac.failure");
				addFieldError("mcError", messageInfo);
			}//end if 1:rsBool
			
		}
		
		return SUCCESS;
	}
	
	public String fillStringTo4(String str){
		StringBuffer strbuf = new StringBuffer();
		str = String.valueOf((Integer.parseInt(str)+1)%10000);
		int len = str.length();
		for (int i = len; i < 4; i++) {
			strbuf.append("0");
		}
		strbuf.append(str);
		return strbuf.toString();
	}	
	
	//功能2：通过机号，读通讯地址和软件版本号
	public String readInfo(){
		String messageInfo;
		
		if (preAction()){// 检测准备情况
			//Level_First_Serial first_Level = Level_First_Serial.getInstance();
			BeanForSetData tempBean = new BeanForSetData();
			serialData = first_Level.readInfo(tempBean);	// 读取信息
			if (serialData != null){
				showMessInfo(first_Level);
				messageInfo = getText("serialport.set.read.succes");
				addActionMessage(messageInfo);
			}else{
				messageInfo = getText("serialport.set.read.failure");
				addFieldError("readError", messageInfo);
			}//end if
		}//end preAction
		return SUCCESS;
	}
	
	//功能3：下发校验时间
	public String checkTime(){
		boolean rsBool = false;
		String messageInfo;
		
		if (preAction()){// 检测准备情况
			//Level_First_Serial first_Level = Level_First_Serial.getInstance();
			rsBool =  first_Level.writeTime(1);	// 下发校验时间
			if (rsBool){
				messageInfo = getText("serialport.set.time.succes");
				addActionMessage(messageInfo);
			}else{
				messageInfo = getText("serialport.set.time.failure");
				addFieldError("timeError", messageInfo);
			}
		}
		return SUCCESS;
	}
	
	//功能4：设置仪器默认属性(温度,湿度,露点等...)
	public String updateEqConfig(){
		boolean rsBool = false;
		String messageInfo = "";
		// System.out.println(configaddress + ":" + configId + ":" + configvalue);
		
		if (preAction()){// 检测准备情况
			//Level_First_Serial first_Level = Level_First_Serial.getInstance();
			
			if (configId == Level_First_Serial.SET_DEFAULT_TEMP){
				messageInfo = "默认温度";
				rsBool = first_Level.setDefaultTemp(configaddress, String.valueOf(configvalue));
			}else if (configId == Level_First_Serial.SET_DEFAULT_HUMI){
				messageInfo = "默认湿度";
				rsBool = first_Level.setDefaultHumi(configaddress, String.valueOf(configvalue));
			}else if (configId == Level_First_Serial.SET_DEFAULT_DEWPOINT){
				messageInfo = "默认露点";
				rsBool = first_Level.setDefaultDewPoint(configaddress, String.valueOf(configvalue));
			}else if (configId == Level_First_Serial.SET_REC_INTERVAL){
				messageInfo = "记录间隔";
				rsBool = first_Level.setRecInterval(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_MAX_STORE_NUMBER){
				messageInfo = "历史记录容量";
				rsBool = first_Level.setMaxStoreNumber(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_HUMI_MAX_VALUE){
				messageInfo = "高湿报警";
				rsBool = first_Level.setHumiMaxValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_HUMI_MIN_VALUE){
				messageInfo = "低湿报警";
				rsBool = first_Level.setHumiMinValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_TEMP_MAX_VALUE){
				messageInfo = "高温报警";
				rsBool = first_Level.setTempMaxValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_TEMP_MIN_VALUE){
				messageInfo = "低温报警";
				rsBool = first_Level.setTempMinValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_DEWPOINT_MAX_VALUE){
				messageInfo = "高露点报警 ";
				rsBool = first_Level.setDewPointMaxValue(configaddress, configvalue);
			}else if (configId == Level_First_Serial.SET_DEWPOINT_MIN_VALUE){
				messageInfo = "低露点报警";
				rsBool = first_Level.setDewPointMinValue(configaddress, configvalue);
			}
			
			if (rsBool){
				messageInfo = "设置" + messageInfo + "成功.";
				addActionMessage(messageInfo);
			}else{
				messageInfo = "设置" + messageInfo + "失败.";
				addFieldError("timeError", messageInfo);
				messageInfo = "先检查: 点击【串口号测试】查看串口连接状态";
				addActionMessage(messageInfo);
				messageInfo = "再检查: 输入地址所对应的仪器是否连接正常";
				addActionMessage(messageInfo);
			}
		}
		return SUCCESS;
	}
	
	
	
	/**
	 * @describe: 显示区信息 -- 主要是设置 电量和版本号
	 * @param first_Level 硬件信息服务类
 	 * @date:2009-11-6
	 */
	public void showMessInfo(Level_First_Serial first_Level) {
		
		// 读取电量 -- 是通过通讯地址获取电量的,因此之前必须要获取通讯地址
		if (serialData != null){
			
			// 电量
			if (showPowerFlag == true){
				int tempInt;
				tempInt = first_Level.readPower( Integer.parseInt( serialData.getAddress() ) );
				if (tempInt == -1){//电量读取失败
					serialData.setPowerVal("读取失败");
				}else{
					serialData.setPowerVal(FunctionUnit.FLOAT_DATA_FORMAT.format
							(tempInt / (float) 1000) + "v");
				}
			}
			
			// 版本号  -- serialData对象中已经有版本信息,这里只是在没有选中复选框时,把值设置为null值,
			if (showVision == false){
				serialData.setVersion(null);
			}
			
		}
		
	}
	
	// ----------------以下为get，set方法----------------	
	public Map<Integer, String> getBaudRatelList() {
		baudRatelList = getMapSel("gprsbaudRate");
		return baudRatelList;
	}
	
	// ----------------普通 get，set方法----------------
	public BeanForSetData getSerialData() {
		return serialData;
	}

	public void setSerialData(BeanForSetData serialData) {
		this.serialData = serialData;
	}

	public int getBaudRateNo() {
		return baudRateNo;
	}

	public void setBaudRateNo(int baudRateNo) {
		this.baudRateNo = baudRateNo;
	}

	public boolean isAgreeAddrsFalg() {
		return agreeAddrsFalg;
	}

	public void setAgreeAddrsFalg(boolean agreeAddrsFalg) {
		this.agreeAddrsFalg = agreeAddrsFalg;
	}

	public boolean isAddFlag() {
		return addFlag;
	}

	public void setAddFlag(boolean addFlag) {
		this.addFlag = addFlag;
	}

	public boolean isShowPowerFlag() {
		return showPowerFlag;
	}

	public void setShowPowerFlag(boolean showPowerFlag) {
		this.showPowerFlag = showPowerFlag;
	}

	public boolean isShowVision() {
		return showVision;
	}

	public void setShowVision(boolean showVision) {
		this.showVision = showVision;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getConfigvalue() {
		return configvalue;
	}

	public void setConfigvalue(int configvalue) {
		this.configvalue = configvalue;
	}

	public int getConfigaddress() {
		return configaddress;
	}

	public void setConfigaddress(int configaddress) {
		this.configaddress = configaddress;
	}

	public int getShowTipMsg() {
		return showTipMsg;
	}

	public void setShowTipMsg(int showTipMsg) {
		this.showTipMsg = showTipMsg;
	}
	
}
