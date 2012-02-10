package com.htc.model.seriaPort;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForSetData;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.htc.common.FunctionUnit;
import com.htc.domain.EquipData;
import com.htc.domain.TLog;
import com.htc.model.MainService;
import com.htc.model.quartz.SerialPortQuartz;

/**
 * 类使用方法: <br>
 * 		第一种:getInstance->InitForTempHumi->beginTask->writeReadSerial->endTask<br>
 * 		第二种:getInstance->InitConfig->setMac/setAddr/readInfo/readPower/writeTime<br>
 * @ SerialPortSet.java
 * 作用 : 串口操作. - 第一层: 串口功能的调用. 
 * 		读取数据 和 设置硬件相关信息 服务
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class Level_First_Serial{
	
	private Log log = LogFactory.getLog(Level_First_Serial.class);

	
	//********************设置仪器属性用的常量**************************//
	/**
	 * 设置仪器默认温度值
	 */
	public static final int SET_DEFAULT_TEMP = 1;
	/**
	 * 设置仪器默认湿度值	
	 */
	public static final int SET_DEFAULT_HUMI = 2;
	/**
	 * 设置仪器默认露点值
	 */
	public static final int SET_DEFAULT_DEWPOINT = 3;
	/**
	 * 设置仪器记录历史数据的间隔,默认值60秒
	 */
	public static final int SET_REC_INTERVAL = 4;
	/**
	 * 设置仪器能记录的最大历史数据量。默认值5000条
	 */
	public static final int SET_MAX_STORE_NUMBER = 5;
	/**
	 * 设置仪器湿度的最大报警值
	 */
	public static final int SET_HUMI_MAX_VALUE = 6;
	/**
	 * 设置仪器湿度的最小报警值
	 */
	public static final int SET_HUMI_MIN_VALUE = 7;
	/**
	 * 设置仪器温度的最大报警值
	 */
	public static final int SET_TEMP_MAX_VALUE = 8;
	/**
	 * 设置仪器温度的最小报警值
	 */
	public static final int SET_TEMP_MIN_VALUE = 9;
	/**
	 * 设置仪器露点最大报警值
	 */
	public static final int SET_DEWPOINT_MAX_VALUE = 10;
	/**
	 * 设置仪器露点最小报警值
	 */
	public static final int SET_DEWPOINT_MIN_VALUE = 11;
	/**
	 * 是否能做任务
	 * 判断是否可以运行任务(主要应用于:暂停串口时,必须禁止执行串口任务的进入,不然串口关闭时,任然有串口通信,这样会出错)
	 */
	private boolean canDoTask = true;
	/**
	 * 串口运行状态[true:正在运行 	false: 没有运行]
	 */
	private boolean runningFlag = false;
	
	// 需要设置的参数
	private int flashTime;						// 任务间隔,单位:秒(停顿时间,这可以有足够时间接收全部数据)
	private String portStr = "COM1";			// 串口号 --如:COM1
	private int baudrate;						// 波特率
	private int DEFAULT_AUTO_SEND_TIME = 5;		// 通讯失败后,默认的重复次数
	private int appendMillsec = 100;			// 通信时附加毫秒数
	
	//private static Level_First_Serial first_Level = new Level_First_Serial();	// 单例
	private CommonDataUnit commonDataUnit;
	private Level_Second_Serial second_Level;
	private MainService mainService;
	private SerialPortQuartz serialPortQuartz;
	//注册service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setSecond_Level(Level_Second_Serial second_Level) {
		this.second_Level = second_Level;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setSerialPortQuartz(SerialPortQuartz serialPortQuartz) {
		this.serialPortQuartz = serialPortQuartz;
	}
	
	/**
	 *构造函数 
	 */
	public Level_First_Serial(){
		//second_Level = new Level_Second_Serial();
		// runningFlag = false;
	}

	/**
	 * @describe: 获取程序单例
	 * @date:2009-11-5
	 */
//	public static Level_First_Serial getInstance() {
//		return first_Level;
//	}
	
	/**
	 * @describe: 启动串口运行,并返回运行结果<br>	
	 * @return  0:运行正常<br> 
	 * 			1:没有可用的仪器(说明没有添加仪器)<br>
	 * 			2:串口连接出错<br>
	 * @date:2010-1-29
	 */
	public int startRunSerial(){
		int rsint = 0;
		boolean bool = true;
		// 获取可用仪器列表个数
		if (bool){ bool = commonDataUnit.getEquiList().size() >0;}
		if (!bool){
			rsint = 1;	// 无仪器增加
		}else{
			// 1: 波特率
			String baudRate = commonDataUnit.getSysArgsByKey(BeanForSysArgs.BAUDRATE_NUMBER);
			int baudRateNo = Integer.parseInt(baudRate);
			// 2: 刷新时间( 单位为秒)
			int flashTime = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
			// 3: 串口通信错误时, 重发的次数(默认5次)
			DEFAULT_AUTO_SEND_TIME = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.SERI_RETRY_TIME));
			// 4: 串口通信时,一次通信附加的毫秒数(主要用在和别的硬件兼容,设当的调节这个值,减小丢帧的频率)(默认100)
			appendMillsec = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.SERI_ADDITION_TIME));		
			// 5: 测试串口是否可用
			if (initForTempHumi(baudRateNo, flashTime) == 1){
				// 重置串口数据采集标志,false:没有开始采集数据
				commonDataUnit.setEqDoingData(false);
				beginTask();
			}else{
				rsint = 2;
			}			
		}
		return rsint;
	}
	
	
	/**
	 * @describe:关闭串口和 暂停任务
	 * @return: true: 暂停成功 false: 暂停失败
	 * @date:2010-1-29
	 */
	public boolean endRunSerial(){
		boolean rsBool = true;
		if (runningFlag){ //串口正在运行才关闭
			rsBool = pauseAllTask();// 暂停任务
		}
		return rsBool;
	}
	
	//****************.串口运行状态.开始和关闭任务.检查,打开和关闭端口.**********************************//
	/**
	 * @describe: Quartz调度方式:开始任务调用.
	 * @date:2009-11-5
	 */
	public boolean beginTask() {
		mainService.packTlog(TLog.SERIAL_LOG, "串口[" + portStr+ "]开始运行...");
		
		boolean rsBool = true;
		
		rsBool = openPort();
//		rsBool = true;	// --测试用
//		flashTime = 10;	// --测试用
		if (rsBool){
//			SerialPortQuartz serialPortQuartz = SerialPortQuartz.getInstance();
			// 设置"即时数据任务"的任务间隔
			serialPortQuartz.setInstantDataCron(flashTime);
			//	Quartz调度方式:开始任务调用.
			serialPortQuartz.runTask();
			//串口运行状态
			runningFlag = true;
			canDoTask = true;
		}else{
			// 在多串口环境中, 发现测试没有成功的,要关闭串口,以免影响其他程序对这个串口的操作
			closePort();
		}
		return rsBool;
	}
	
	/**
	 * @describe: Quartz调度方式:暂停任务,关闭串口.
	 * @return: true: 暂停成功  false: 暂停失败
	 * @date:2009-11-5
	 */
	public boolean pauseAllTask() {
		boolean rsBool = true;
		
		// 设置任务处于暂停状态,不能采集数据
		canDoTask = false;
		// 刷新时间( 单位为秒)
		int flashTime = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
		// 重试次数
		int reTryTime = 6;  
		
		// 如果串口正在采集数据,则等待采集完成
		while(commonDataUnit.isEqDoingData()){
			if (reTryTime == 0){break;}
			try {
				Thread.sleep(flashTime * 500);
			} catch (InterruptedException e) {
				log.error("暂停任务,时间停顿时出错:" + e.getMessage());
			}
			reTryTime --;
		}
		if (commonDataUnit.isEqDoingData()){ // 暂停失败,为了保证程序正常运行,设置恢复任务标志
			mainService.packTlog(TLog.SERIAL_LOG, "串口[" + portStr+ "]暂停运行失败...");
			canDoTask = true;
			rsBool =false;
		}else{								 // 暂停成功
			mainService.packTlog(TLog.SERIAL_LOG, "串口[" + portStr+ "]暂停运行...");
			// Quartz调度方式:结束任务调用.
			serialPortQuartz.pauseAllTask();
			// 设置串口运行状态为:关闭
			runningFlag = false;			
			// 关闭串口
			closePort();
		}
		return rsBool;
	}
	
	/**
	 * @describe: Quartz调度方式:结束任务,关闭串口.
	 * @date:2009-11-5
	 */
	public void endTask() {
		// MainService mainService = new MainService();
		mainService.packTlog(TLog.SERIAL_LOG, "串口[" + portStr+ "]结束运行...");
		
		// Quartz调度方式:结束任务调用.
		serialPortQuartz.endTask();
		// 串口运行状态
		runningFlag = false;
		// 关闭串口
		closePort();
	}
	
	/**
	 * @describe: 检查串口是否连接正常,以及串口有没有换位置
	 * @date:2009-11-22
	 */
	public void checkRunningPort(){
		//获取final_Serial实例--扫描端口数量,并逐个测试
		List<String> portList = Level_Final_Serial.getAllComPorts();
		if (portList.size() <= 0){
			// 没有发现任何串口
		}else{
			// 重新扫描,查看合适的串口,如果串口正在运行,先关闭
			boolean openFlag = second_Level.isPortOpen();
			if (openFlag){
				closePort();
			}
			log.info("切换串口后,重新扫描开始...");
			// 重新扫描测试连通性
			for (String str : portList) {
				this.portStr = str;	// 端口字符串
				// 在读取温湿度数据之前,进行预读测试,查看串口连通状态 
				if (testForTempHumi() == 1){
					break;
				}
			}// end for
			log.info("切换串口后,重新扫描结束...");
		}
	}
	
	/**
	 * @describe: 打开端口	
	 * @return: true : 成功    false : 失败
	 * @date:2009-11-7
	 */
	public boolean openPort() {
		
		boolean rsBool = true;
		// 初始化数据
		second_Level.initialize(portStr, baudrate, appendMillsec);
		// 打开串口
		rsBool = second_Level.openPort();
		
		return rsBool;
	}
	
	/**
	 * @describe: 关闭端口
	 * @date:2009-11-5
	 */
	public void closePort() {
		second_Level.closePort();
	}
	
	//****************************读取 仪器的温湿度数据*******************************************//
	/**
	 * @describe: 读取 仪器的温湿度数据, 初始化串口参数, 设置配置状态.
	 * @param baudrate 波特率
	 * @return -1表示不成功. 1表示成功
	 * @date:2009-11-6
	 */
	public int initForTempHumi(int baudrate, int flashTime) {
		int rsInt = -1;
		this.flashTime = flashTime; 	// 任务间隔,单位:秒
		this.baudrate = baudrate; 		// 波特率
		
		//获取final_Serial实例--扫描端口数量,并逐个测试
		List<String> portList = Level_Final_Serial.getAllComPorts();
		if (portList.size() > 0){
			for (String str : portList) {
				this.portStr = str;	// 端口字符串
				// 在读取温湿度数据之前,进行预读测试,查看串口连通状态 
				if (testForTempHumi() == 1){
					rsInt = 1 ;
					break;
				}
			}// end for
		}
		return rsInt;
	}
	
	/**
	 * @describe: 在读取温湿度数据之前,进行预读测试,查看串口连通状态	
	 * @return: 返回1代表测试成功，-1表示测试失败(串口没有连接仪器或没有合适的串口)
	 * @date:2009-11-7
	 */
	public int testForTempHumi(){
		int rsFlag = -1;
		int address, equipmentId;
		
		// 搜索当前可用的仪器列表, 测试串口的连通状态
		for (EquipData equipData : commonDataUnit.getEquiMap().values()) {
			address = equipData.getAddress();
			equipmentId = equipData.getEquipmentId();
			
			// 1: 打开端口
			if (openPort() == true){
				// 2: 串口读写
				int dataLen = 3; //(3:温度,湿度,露点4个数据)(4:温度,湿度,露点,电压4个数据) 
				rsFlag = writeReadSerial(address, equipmentId, Level_Second_Serial.READ_TEST_DATA, dataLen);
				// 3: 关闭串口
				closePort();
			}
			// 得到正确的帧,说明找到目标串口
			if (rsFlag  == Level_Final_Serial.Serial_right_Frame){
				rsFlag = 1;
				break;
			}else{
				rsFlag = -1;
			}
		}// end for
		
		return rsFlag;
	}
	
	/**
	 * @describe: 读取串口温湿度数据,然后保存到数据中.	
	 * @param address 仪器地址
	 * @param equipmentId 仪器主键ID
	 * @param readType 串口读写保存类型.只有READ_USEFULL_DATA时才把数据保持到数据库,或者删除相应临时数据.
	 * @param dataLen(3:温度,湿度,露点4个数据)(4:温度,湿度,露点,电压4个数据) 
	 * @return: 返回帧检查结果. 只有返回Serial_right_Frame(值为1),为正常成功的帧,其他都为失败帧
	 * @date:2009-11-7
	 */
	public int writeReadSerial(int address,int equipmentId, int readType, int dataLen) {
		int runFlag = 0;
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		
		while (sendTimes > 0){
			try {
				
//				char[] rsByte = {1,3,6,20,201,114,117,110,159,152,219};			// -- 测试用
//				second_Level.updateDataBean(address, equipmentId, rsByte, 1);	// -- 测试用
//				runFlag = 1;													// -- 测试用
				
				// 1 判断串口是否打开;没有打开,则做一次打开串口的操作;成功后 发送"读取串口温湿度数据"的串口命令
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if (openFlag){
					second_Level.writeToGetTempHumi(address, dataLen);
					runFlag = second_Level.readByte(address,equipmentId, readType, dataLen);
				}else{ // 串口访问错误,不进行从发
					sendTimes = 0;
					break;
				}
			} catch (Exception e) {
				// closePort();
				e.printStackTrace();
				
				log.error("address:"+address+" equipmentId:"+equipmentId + 
						" 从串口读取串口温湿度数据时出错"+e.getMessage());
				//MainService mainService = new MainService();
				mainService.packTlog(TLog.ERROR_LOG, "串口数据读取失败"+"地址:"+address+
						"  错误信息:"+e.getMessage());
			}
			// 通讯正常,跳出循环,不再重发
			if (runFlag == Level_Final_Serial.Serial_right_Frame){
				break;
			}
			sendTimes --;
		}
		
		// 如果经过DEFAULT_AUTO_SEND_TIME次重发后,还是丢帧,则删除临时数据,达到动态显示效果.
		if (sendTimes ==  0){
			// 只有readType = READ_USEFULL_DATA时才加入空数据
			if (readType == Level_Second_Serial.READ_USEFULL_DATA){
				second_Level.saveEmptyData(address, equipmentId);
			}
		}
		
		return runFlag;
	}
	
	//****************************配置 或  读取 仪器硬件参数*******************************************//
	
	/**
	 * @describe: 配置硬件参数时,自动初始化参数, 不返回任何东西
	 * @param baudrate 波特率
	 * @date:2009-11-5
	 */
	public void initConfigVoid(int baudrate) {
		this.baudrate = baudrate; 	//波特率
	}
	
	/**
	 * @describe: 扫描串口. 配置硬件参数时, 初始化串口参数
	 * @param baudrate 波特率
	 * @return true: 连接正常   false: 连接错误
	 * @date:2009-11-7
	 */
	public boolean initConfig(int baudrate){
		boolean linkState = false ;
		this.baudrate = baudrate; 			// 波特率

		//获取final_Serial实例--扫描端口数量,并逐个测试
		List<String> portList = Level_Final_Serial.getAllComPorts();
		if (portList.size() > 0){
			for (String str : portList) {
				this.portStr = str;			// 端口字符串
				
				// 打开端口
				if (openPort() == true){
					// 发送空内容的Bean. 相当于发送了广播消息.
					// 若只接了一个仪器,则正常. 若接了过个, 结果无法预测 
					// 读通讯地址和软件版本号	-- 配置硬件参数,测试串口连通状态
					// 关闭串口
					linkState = readInfo(new BeanForSetData()) != null;
					if (linkState == true){break;}
					//closePort();//不需要关闭串口了,在readInfo中已经关闭
				};
			}// end for
		}
		return linkState;
	}
	
	/**
	 * @describe: 1: 设置机器码
	 * @param serialSetDataBean 硬件属性
	 * @return true:成功 	false:失败
	 * @date:2009-11-5
	 */
	public boolean  setMac(BeanForSetData serialSetDataBean){
		boolean rsBool = false;
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		
		while (sendTimes > 0){
			try {
				// 判断串口是否打开;没有打开,则做一次打开串口的操作;成功后 发送"设置机器码"的串口命令
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.setMac(serialSetDataBean);//发送
					int dataLen = 4; // (3:温度,湿度,露点4个数据)(4:温度,湿度,露点,电压4个数据)
					rsBool = Level_Final_Serial.Serial_right_Frame == 
						second_Level.readByte(-1, -1, Level_Second_Serial.READ_USEFULL_MAC, dataLen);//接收
				}
			} catch (Exception e) {
				//throw e;
				rsBool = false;
				log.error("设置机器码时出错:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			// 通讯正常,跳出循环,不再重发
			if (rsBool){
				break;
			}		
			sendTimes --;
		}
		return rsBool;
	}
	
	/**
	 * @describe: 2:设置地址
	 * @param serialSetDataBean	 硬件属性
	 * @param address 新地址
	 * @return true:成功 	false:失败
	 * @date:2009-11-5
	 */
	public boolean setAddr(BeanForSetData serialSetDataBean, int address) {
		boolean rsBool = false;
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		
		while (sendTimes > 0){
			try {
				// 判断串口是否打开;没有打开,则做一次打开串口的操作;成功后 发送"设置地址"的串口命令
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.setAddr(serialSetDataBean, address);//发送
					int dataLen = 4; // (3:温度,湿度,露点4个数据)(4:温度,湿度,露点,电压4个数据)
					rsBool = Level_Final_Serial.Serial_right_Frame == 
						second_Level.readByte(-1, -1, Level_Second_Serial.READ_USEFULL_MAC, dataLen);//接收
				}
			} catch (Exception e) {
				//throw e;
				rsBool = false;
				log.error("设置地址时出错:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			// 通讯正常,跳出循环,不再重发
			if (rsBool){
				break;
			}		
			sendTimes --;
		}
		return rsBool;
	}
	
	/**
	 * @describe: 3:通过机号，读通讯地址和软件版本号
	 * @param serialSetDataBean 硬件属性
	 * @date:2009-11-5
	 */
	public BeanForSetData readInfo(BeanForSetData serialSetDataBean) {
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		BeanForSetData rsData = null;
		char [] rs = null;
		
		while (sendTimes > 0){
			try {
				// 判断串口是否打开;没有打开,则做一次打开串口的操作;成功后 发送"读通讯地址和软件版本号"的串口命令
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.readInfo(serialSetDataBean);//发送
					rs = second_Level.readByteNoDB(); //接收
				}
			} catch (Exception e) {
				//throw e;
				rsData = null;
				log.error("读通讯地址和软件版本号时出错--读取串口数据:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			// 通讯正常,跳出循环,不再重发
			if (rs != null){
				try {
					//System.out.println("分析前"+ FunctionUnit.bytesToHexString(rs));
					rsData = rs.length == 14 ? fillDataScript(rs) : null;
					//System.out.println(rsData.toString());
					//System.out.println("分析后" + rsData.getMCNo());
				} catch (Exception e) {
					log.error("读通讯地址和软件版本号时出错--解析数据:" + e.getMessage()+"-=-"+e.toString());
				}
				break;
			}		
			sendTimes --;
		}		
		return rsData;
	}
	
	/**
	 * @describe: 把char数组填充成SerialSetDataBean数据结构
	 * @date:2009-11-5
	 */
	public BeanForSetData fillDataScript(char [] fillChar)throws Exception{
		String tempStr;
		BeanForSetData ssdBean = new BeanForSetData();
		//fillChar:FC 67 09    11 00(类型号)  10 01(批次) 88 21(流水号)  	00(地址)       01 00(软件版本号)	 38 BA(CRC) 
		tempStr = String.valueOf( FunctionUnit.reverseHexToDec((int)fillChar[3]) * 100 + FunctionUnit.reverseHexToDec((int)fillChar[4]) );
		ssdBean.setMCType(fillStringTo4(tempStr,4));	//类型号
		tempStr = String.valueOf( FunctionUnit.reverseHexToDec((int)fillChar[5]) * 100 + FunctionUnit.reverseHexToDec((int)fillChar[6]) );
		ssdBean.setMCOrder(fillStringTo4(tempStr,4));//批次
		tempStr = String.valueOf( FunctionUnit.reverseHexToDec((int)fillChar[7]) * 100 + FunctionUnit.reverseHexToDec((int)fillChar[8]) );
		ssdBean.setMCNo(fillStringTo4(tempStr,4));//流水号
		tempStr = String.valueOf((int)fillChar[9]);
		ssdBean.setAddress(tempStr);//地址
		tempStr = String.valueOf((int)fillChar[10] + "." + (int)fillChar[11]);
		ssdBean.setVersion(tempStr);//软件版本号
		return  ssdBean;
	}
	
	/**
	 * @describe: 格式化字符串,保证数组是size位数。
	 * 不到size位,前面补零. 如:str=21,siez=21 -> 0021	
	 * @param str 需要格式的字符串
	 * @param size 长度
	 * @date:2009-11-14
	 */
	public String fillStringTo4(String str,int size){
		StringBuffer strbuf = new StringBuffer();
		int len = str.length();
		for (int i = len; i < size; i++) {
			strbuf.append("0");
		}
		strbuf.append(str);
		return strbuf.toString();
	}
	
	/**
	 * @describe 4:读取电量(1.5V的电池)<br>
	 * 			正常区域: 1200 < v < 2000.<br>
	 * 			电量不足: 0 < v <= 1200 <br>
	 * 			电量用尽: v <=0 and v >= 2000<br> 
	 * @param addr 要读取电量的仪器ID
	 * @return 返回-1表示没有读取成功
	 * @date:2009-11-5
	 */
	public int readPower(int addr) {
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		int rsInt = -1;
		char [] rs = null;
		
		while (sendTimes > 0){
			try {
				// 判断串口是否打开;没有打开,则做一次打开串口的操作;成功后 发送"读取电量"的串口命令
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.readPower(addr);//发送
					rs = second_Level.readByteNoDB(); //接收
					//System.out.println(SerialService.bytesToHexString(rs));
				}
			} catch (Exception e) {
				// throw e;
				rsInt = -1;
				log.error("读取电量时出错:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
				
			// 通讯正常,跳出循环,不再重发
			if (rs != null){
				rsInt = rs.length == 7 ? (rs[3] << 8) + rs[4] : -1;
				break;
			}		
			sendTimes --;
		}		
		return rsInt;
	}
	
	/**
	 * @describe:	5:下发校验时间
	 * @param type:1(执行命令后,关闭串口) type:2(执行命令后,不关闭串口)
	 * @return:  true:成功 	false:失败
	 * @date:2010-3-24
	 */
	public boolean writeTime(int type){
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		boolean rsBool = true;
		
		while (sendTimes > 0){
			try {
				// 判断串口是否打开;没有打开,则做一次打开串口的操作;成功后 发送"下发校验时间"的串口命令
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.writeTime();//发送,不接收
				}
			} catch (Exception e) {
				//throw e;
				rsBool = false;
				log.error("下发校验时间时出错:" + e.getMessage()+e.toString());
			}finally{
				if (type == 1){
					closePort();
				}
			}
			// 通讯正常,跳出循环,不再重发
			if (rsBool){
				break;
			}		
			sendTimes --;
		}			
		return rsBool;
	}
	
	/**
	 * @describe: 6: 返回仪器当前保存着的历史数据的个数
	 * @param addr 仪器地址
	 * @return 返回-1表示没有读取成功 
	 * @date:2009-11-24
	 */
	public int readCurrentStoreNumber(int addr){
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		
		int histroyCount = -1 ;
		char [] rs = null;
		while (sendTimes > 0){
			try {
				// 判断串口是否打开;没有打开,则做一次打开串口的操作;成功后 发送"下发校验时间"的串口命令
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.readCurrentStoreNumber(addr);//发送
					rs = second_Level.readByteNoDB(); //接收
				}
			} catch (Exception e) {
				//throw e;
				log.error("返回仪器当前保存着的历史数据 时出错--读取串口数据:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			
			// 通讯正常,跳出循环,不再重发
			if (rs != null){
				try {
					//System.out.println(FunctionUnit.bytesToHexString(rs));
					// 下标为4和5的元素就是所要的数据
					histroyCount = rs.length == 7 ? (rs[4]<<8 + rs[5]) : -1;
					//System.out.println(rsData.toString());
				} catch (Exception e) {
					log.error("返回仪器当前保存着的历史数据时出错--解析数据:" + e.getMessage()+"-=-"+e.toString());
				}
				break;
			}
			
			sendTimes --;
		}
		
		return histroyCount;
	}
	
	/**
	 * @describe: 对仪器的属性(可读可写对象),进行写入操作	
	 * @param addr	仪器地址
	 * @param type	写入操作	类型
	 * @param value	要写入的值
	 * @param errorString 异常时显示的字符串
	 * @return: true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setInfoByAddress(int addr, int value, int type, String errorString){
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		boolean rsBool = true;
		
		while (sendTimes > 0){
			try {
				// 判断串口是否打开;没有打开,则做一次打开串口的操作;成功后 发送"下发校验时间"的串口命令
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					
					//发送
					if (type == SET_DEFAULT_TEMP){
						second_Level.setDefaultTemp(addr, value);		// 设置仪器默认温度值
					}else if(type == SET_DEFAULT_HUMI){
						second_Level.setDefaultHumi(addr, value);		// 设置仪器默认湿度值 
					}else if(type == SET_DEFAULT_DEWPOINT){
						second_Level.setDefaultDewPoint(addr, value);	// 设置仪器默认露点值 
					}else if(type == SET_REC_INTERVAL){
						second_Level.setRecInterval(addr, value);		// 设置仪器记录历史数据的间隔
					}else if(type == SET_MAX_STORE_NUMBER){
						second_Level.setMaxStoreNumber(addr, value);	// 设置仪器能记录的最大数据量
					}else if(type == SET_HUMI_MAX_VALUE){
						second_Level.setHumiMaxValue(addr, value);		// 设置仪器湿度的最大报警值
					}else if(type == SET_HUMI_MIN_VALUE){
						second_Level.setHumiMinValue(addr, value);		// 设置仪器湿度的最小报警值
					}else if(type == SET_TEMP_MAX_VALUE){
						second_Level.setTempMaxValue(addr, value);		// 设置仪器温度的最大报警值
					}else if(type == SET_TEMP_MIN_VALUE){
						second_Level.setTempMinValue(addr, value);		// 设置仪器温度的最小报警值
					}else if(type == SET_DEWPOINT_MAX_VALUE){
						second_Level.setDewPointMaxValue(addr, value);	// 设置仪器露点最大报警值
					}else if(type == SET_DEWPOINT_MIN_VALUE){
						second_Level.setDewPointMinValue(addr, value);	// 设置仪器露点最小报警值
					}
					
					int dataLen = 4; // (3:温度,湿度,露点4个数据)(4:温度,湿度,露点,电压4个数据)
					//接收
					rsBool = Level_Final_Serial.Serial_right_Frame == 
						second_Level.readByte(-1, -1, Level_Second_Serial.READ_USEFULL_MAC, dataLen);
				}
			} catch (Exception e) {
				//throw e;
				rsBool = false;
				log.error( errorString + "时出错:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			
			// 通讯正常,跳出循环,不再重发
			if (rsBool){
				break;
			}
			
			sendTimes --;
		}
		return rsBool;
	}
	
	/**
	 * @describe:	1- 设置仪器默认温度值
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setDefaultTemp(int addr, String temp){
		int value = (int) (Float.valueOf(temp) * 100 + 27315);
		boolean rsBool = setInfoByAddress(addr, value, SET_DEFAULT_TEMP, "设置仪器默认温度值");
		return rsBool;
	}
	
	/**
	 * @describe:	2- 设置仪器默认湿度值	
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setDefaultHumi(int addr, String humi){
		int value = (int) (Float.valueOf(humi) + 0);
		boolean rsBool = setInfoByAddress(addr, value, SET_DEFAULT_HUMI, "设置仪器默认湿度值	");
		return rsBool;
	}
	
	/**
	 * @describe:	3- 设置仪器默认露点值
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setDefaultDewPoint(int addr, String dewPoint){
		int value = (int) (Float.valueOf(dewPoint) * 100 + 27315);
		boolean rsBool = setInfoByAddress(addr, value, SET_DEFAULT_DEWPOINT, "设置仪器默认露点值");
		return rsBool;
	}
	
	/**
	 * @describe:	4- 设置仪器记录历史数据的间隔
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setRecInterval(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_REC_INTERVAL, "设置仪器记录历史数据的间隔");
		return rsBool;
	}
	
	/**
	 * @describe:	5- 设置仪器能记录的最大数据量
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setMaxStoreNumber(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_MAX_STORE_NUMBER, "设置仪器最大记录数据量");
		return rsBool;
	}
	
	/**
	 * @describe:	6- 设置仪器湿度的最大报警值
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setHumiMaxValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_HUMI_MAX_VALUE, "设置仪器湿度的最大报警值");
		return rsBool;
	}
	
	/**
	 * @describe:	7- 设置仪器湿度的最小报警值
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setHumiMinValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_HUMI_MIN_VALUE, "设置仪器湿度的最小报警值");
		return rsBool;
	}
	
	/**
	 * @describe:	8- 设置仪器温度的最大报警值
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setTempMaxValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_TEMP_MAX_VALUE, "设置仪器温度的最大报警值");
		return rsBool;
	}
	
	/**
	 * @describe:	9- 设置仪器温度的最小报警值
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setTempMinValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_TEMP_MIN_VALUE, "设置仪器温度的最小报警值");
		return rsBool;
	}
	
	/**
	 * @describe:	10-设置仪器露点最大报警值
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setDewPointMaxValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_DEWPOINT_MAX_VALUE, "设置仪器露点最大报警值");
		return rsBool;
	}
	
	/**
	 * @describe:	11- 设置仪器露点最小报警值
	 * @param addr	仪器地址
	 * @param value	要写入的值
	 * @return:		true:设置成功 	false:设置失败
	 * @date:2009-11-24
	 */
	public boolean setDewPointMinValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_DEWPOINT_MIN_VALUE, "设置仪器露点最小报警值");
		return rsBool;
	}
	
	//---------以下为get,set方法----------//
	public boolean isRunningFlag() {
		return runningFlag;
	}
	/**
	 * @describe: 判断是否可以运行任务(主要应用于:暂停串口时,必须禁止执行串口任务的进入,不然串口关闭时,任然有串口通信,这样会出错)	
	 * @date:2010-4-9
	 */
	public boolean isCanDoTask() {
		return canDoTask;
	}	
	
	/**
	 * @describe: 返回"总览画面"用
	 * @date:2009-11-8
	 */
//	public Map<Integer, BeanForPortData> getAddressData() {
//		return second_Level.getAddressData();
//	}
	
	/**
	 * @describe: 返回"实时曲线"用的临时数据集
	 * @date:2009-11-8
	 */
//	public List<Map<Integer, BeanForPortData>> getCurrentFlashData() {
//		return second_Level.getCurrentFlashData();
//	}
	
	/**
	 * @describe: 端口测试用和总览画面数据清空
	 * @date:2009-11-8
	 */
//	public void clearBarData(){
//		second_Level.clearBarData();
//	}
	
	/**
	 * @describe: 获取equipmentId对应的临时数据("端口测试"用的临时数据集)
	 * @param equipmentId
	 * @date:2009-11-8
	 */
//	public  BeanForPortData getDataWithAddress(int equipmentId) {
//		return second_Level.getDataWithAddress(equipmentId);
//	}

	public String getPortStr() {
		return portStr;
	}
	
}
