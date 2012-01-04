package com.htc.model.seriaPort;

import gnu.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForPortData;
import com.htc.bean.BeanForSetData;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.FunctionUnit;
import com.htc.common.CommonDataUnit;
import com.htc.domain.Data4Access;
import com.htc.domain.EquipData;
import com.htc.domain.Record;
import com.htc.domain.TLog;
import com.htc.model.ServiceAccess;
import com.htc.model.MainService;

/**
 * 类使用方法: <br>
 * 		getInstance->initialize->openPort-><br>
 * 		writeByte/readByte/setMac/setAddr/readInfo/readPower/writeTime<br>
 * 		->closePort->endTask<br>
 * @ Level_Four_SerialService.java
 * 作用 : 串口操作 -- 第二层: 串口功能封装 .
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-7     YANGZHONLI       create
 */
public class Level_Second_Serial {
	
	//public static Level_Second_Serial second_Level = new Level_Second_Serial();
	
	private Log log = LogFactory.getLog(Level_Second_Serial.class);
	
	/*-------------串口读写保存类型,用以判断是否把数据保存到数据库------------*/
	/**
	 * 	串口读写后,不保存到数据库.测试串口连通性用的.
	 */
	public static final int READ_TEST_DATA = 1;
	/**
	 * 串口读写后,保存到数据库.系统运行正常后用的.
	 */
	public static final int READ_USEFULL_DATA = 2;
	/**
	 * 仪器属性设置用.没有读取温湿度,所以不保存到数据库
	 */
	public static final int READ_USEFULL_MAC= 3;
	
	/*-----------------------电量相关---------------------------------------*/
	/**
	 * 读取电量. (1.5V的电池),正常区域:1200< V < 2000.<br>
	 */
	public static final int POWER_1_5 = 2000;
	/**
	 * 读取电量. (3.6V的电池),正常区域: > 3000.<br>
	 */
	public static final int POWER_3_6 = 3000;
	
	//需要设置的参数
	int baudrate;					// 波特率
	
	//不需要设定的参数(有默认值)
	int timeOut; 					// 延迟时间(毫秒数)
	int dataBits; 					// 数据位
	int stopBits; 					// 停止位
	int parity; 					// 奇偶检验
	int funCode; 					// 功能码	
	//int appendMillsec; 				// 计算发送间隔用---附加毫秒数
	int bytes;						// 计算发送间隔用---发送字节数	

	// 自动计算
	int frameInterval; 				// 根据波特率，数据倍率和数据量，自动设置发送间隔
	
	// 临时数据区
	private Map<Integer, Record> currentNewRecords;						// 当前最新的记录,用来批量保存到数据库用
	private Map<Integer, BeanForPortData> addressData;					// 总览画面"用的临时数据集
	
	//private Map<Integer, BeanForPortData> tempFlashData;				// "实时曲线"Flash数据的单元数据
	//private List<Map<Integer,BeanForPortData>> currentFlashData;		// "实时曲线"用的临时数据集-时间一致(短日期:数据对象)
	private static String SMSAPPNAME = "HtcSerialData";	
	
	private Level_Final_Serial final_Level;
	private CommonDataUnit commonDataUnit;
	private MainService mainService;
	private ServiceAccess serviceAccess;	
	//注册service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setFinal_Level(Level_Final_Serial final_Level) {
		this.final_Level = final_Level;
	}
	public void setServiceAccess(ServiceAccess serviceAccess) {
		this.serviceAccess = serviceAccess;
	}
	
	// 构造方法
	public Level_Second_Serial() {
		timeOut = 60;						// 延迟时间(毫秒数)
		dataBits = SerialPort.DATABITS_8;	// 数据位
		stopBits = SerialPort.STOPBITS_1;	// 停止位
		parity = SerialPort.PARITY_NONE;	// 奇偶检验
		funCode = 3;						//	读取当前寄存器内一个或多个二进制值
		//appendMillsec = 150;				//	附加毫秒数 150
		bytes = 20;							//  发送是字节数
		currentNewRecords = new ConcurrentHashMap<Integer, Record>();
		addressData = new ConcurrentHashMap<Integer, BeanForPortData>();
		//currentFlashData = new CopyOnWriteArrayList<Map<Integer,BeanForPortData>>();
		//final_Level = new Level_Final_Serial();
	}

//	/**
//	 * @describe:  获取程序单例
//	 * @date:2009-11-5
//	 */
//	public static Level_Second_Serial getInstance() {
//		return second_Level;
//	}

	//****************.初始化.串口运行状态.打开和关闭端口. start**********************************//

	/**
	 * @describe: 初始化串口实例
	 * @param portStr 串口号. 如: COM1
	 * @param baudrate 波特率
	 * @param appendMillsec 计算发送间隔用---附加毫秒数
	 * @date:2009-11-5
	 */
	public void initialize(String portStr, int baudrate, int appendMillsec) {
		final_Level.setAppname(SMSAPPNAME);
		final_Level.setPortName(portStr);
		final_Level.init(timeOut, baudrate, dataBits, stopBits, parity);
		this.baudrate = baudrate;
		this.frameInterval = Level_Final_Serial.getFrameInterval(appendMillsec, bytes, baudrate);
	}
	
	/**
	 * @describe:	返回串口是否打开	
	 * @return: true : 打开    false : 关闭
	 * @date:2009-11-7
	 */
	public boolean isPortOpen() {
		return final_Level.isPortOpen();
	}
	
	/**
	 * @throws Exception 
	 * @describe: 打开串口
	 * @date:2009-11-5
	 */
	public boolean openPort() {
		if (final_Level.initialize()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @describe: 关闭串口，释放资源
	 * @date:2009-11-5
	 */
	public void closePort() {
		final_Level.closePort();
	}
	
	//****************.初始化.串口运行状态.打开和关闭端口. end  ****************************************//
	
	//----------------帧格式定义 start--------------//
//	 地址码	功能码	寄存器地址	寄存器数据长度	CRC低字节	CRC高字节
//	 1个字节	1个字节	2个字节		2个字节			1个字节		1个字节
	
//	 功能码如下：
//	 	03	读取数据	读取当前寄存器内一个或多个二进制值
//	 	06	重置单一寄存器	把设置的二进制值写入单一寄存器
//	 寄存器地址如下
//	 	0x0000:当前湿度值，等于（实际值*100）
//	 	0x0001: 当前温度值，等于（实际值*100 + 27315）
//	 	0x0002:当前露点值，等于（实际值*100 + 27315）
//	 	0x0003:当前后备电池电压。等于（实际值*1000）。 境界值1200
//	 	0x0005:当前历史数据的个数。
	
//	可读可写对象：
//		0x0010:温度标定量，（实际值*100）
//		0x0011湿度标定量，（实际值*100）
//		0x0012:露点标定量，（实际值*100）
//		0x0013:记录时间间隔，秒的倍数。默认值60。
//		0x0014:记录数据个数最大设定值。默认值5000；
//		0x0015: 湿度高报警门限值
//		0x0016: 湿度低报警门限值
//		0x0017: 温度高报警门限值
//		0x0018: 温度低报警门限值
//		0x0019: 露点高报警门限值
//		0x001a: 露点低报警门限值
	
	//----------------帧格式定义 end--------------//
	
	/**
	 * @describe: 根据仪器地址,发送获取仪器温湿度的串口命令<br>
	 * @param address 仪器地址
	 * @param funCode 功能码
	 * @param memHigh 寄存器地址:高位
	 * @param menLow  寄存器地址:低位
	 * @param dataHigh 数据长度:高位
	 * @param dataLow  数据长度:低位
	 * @date:2009-11-5
	 */
	public void writeByte(int address, int funCode, int memHigh, int menLow, int dataHigh, int dataLow) throws Exception{
		char[] rs = {(char)address, (char)funCode, (char)memHigh, (char)menLow, (char)dataHigh, (char)dataLow, 0, 0};
		rs = CalcCRC.getCrc16(rs);
		//System.out.println(FunctionUnit.bytesToHexString(rs));
		final_Level.writePort(rs);
		//停止，以保证数据发送成功
		Thread.sleep(frameInterval);
	}	
	
	//****************************读取 仪器的温湿度数据 start *******************************************//
	/**
	 * @describe: 根据仪器地址,发送获取仪器温湿度的串口命令-主要是发生获取温湿度命令<br>
	 * 地址码	功能码	寄存器地址	寄存器长度	CRC低字节	CRC高字节<br>
	 * 1个字节	1个字节	2个字节		2个字节		1个字节		1个字节<br>
	 * 寄存器地址如下
	 * 0x0000:当前湿度值，等于（实际值*100）<br>
	 * 0x0001: 当前温度值，等于（实际值*100 + 27315）<br>
	 * 0x0002:当前露点值，等于（实际值*100 + 27315）<br>
	 * 0x0003:当前后备电池电压。等于（实际值*1000）。 境界值1200<br>
	 * 0x0005:当前历史数据的个数。<br>
	 * @param address 仪器地址
	 * @date:2009-11-5
	 */
	public void writeByteForGet(int address,int memHigh, int menLow, int dataHigh, int dataLow) throws Exception{
		writeByte(address, 0x03, memHigh, menLow, dataHigh, dataLow);
	}
	
	/**
	 * @describe:根据仪器地址发送获取 温度,湿度和露点的命令
	 * @param address仪器地址
	 * @param dataLen(3:温度,湿度,露点4个数据)(4:温度,湿度,露点,电压4个数据)
	 * @date:2009-11-24
	 */
	public void writeToGetTempHumi(int address,int dataLen) throws Exception{
		writeByteForGet(address, 0, 0, 0, dataLen);
	}
	
	
	/**
	 * @describe: .读取温湿度数据	.读设置机器码结果		.读设置地址结果
	 * @param address 仪器地址
	 * @param equipmentId 仪器主键ID
	 * @param readType	串口读写保存类型.只有READ_USEFULL_DATA时才把数据保持到数据库.
	 * @param dataLen(3:温度,湿度,露点4个数据)(4:温度,湿度,露点,电压4个数据)
	 * @return 返回帧检查结果.   只有返回Serial_right_Frame(值为1),为正常成功的帧,其他都为失败帧
	 * @date:2009-11-5
	 */
	public int readByte(int address, int equipmentId, int readType, int dataLen) throws Exception{
		
		if ( final_Level == null){
			return -1;
		}
		
		// 读取数据
		char[] rsByte = final_Level.readPackData();
		if (rsByte != null){
			 //System.out.println(FunctionUnit.bytesToHexString(rsByte));
		}
		int rsState = checkReturn(rsByte, address);
		
		//过滤测试数据,对用的数据进行操作
		if (readType == Level_Second_Serial.READ_USEFULL_DATA){
			//帧检测无误,把数据存入数据库
			if ( rsState == Level_Final_Serial.Serial_right_Frame ) {
				updateDataBean(address, equipmentId, rsByte, rsState, dataLen);
			}else{
				if ((rsByte != null)&&(rsByte.length >= 11 )){//正确的数据有1个数据
					// 根据dataLen,显示 丢帧记录 
					int humi = 0,temp = 0, dew = 0, power = 0;
					if ( dataLen >= 1)
						humi = (rsByte[3] << 8) + rsByte[4];
					if ( dataLen >= 2)
						temp = ((rsByte[5] << 8) + rsByte[6] - 27315);
					if ( dataLen >= 3)
						dew = ((rsByte[7] << 8) + rsByte[8] - 27315);
					if ( dataLen >= 4)
						power = (rsByte[9] << 8) + rsByte[10];
					
					String str = "温度:" + temp + "  湿度:" + humi + "  露点:" + dew + "电压: " + power;
					str = "丢帧地址:"+ address +" 返回帧内容:" + FunctionUnit.bytesToHexString(rsByte) +	"解析:" + str;
					log.info(str);
					//MainService mainService = new MainService();
					mainService.packTlog(TLog.SERIAL_LOG, str);
				}
			}
		}
		return rsState;
	}
	
	/**
	 * @describe: 检查读取的值: address:-1(不检查头字节)
	 * @param rsByte 待检查的char数组	
	 * @param address 用来判断地址是否一致. 不需要时,赋值为 -1
	 * @return: 返回CRC检查结果. 只有返回Serial_right_Frame(值为1),为正常成功的帧,其他都为失败帧
	 * @date:2009-11-5
	 */
	public int checkReturn(char[] rsByte, int address){
		boolean rsOut = true;
		int rsState = Level_Final_Serial.Serial_right_Frame;
		
		//帧判断
		if (rsByte == null) {
			//判断是否丢帧
			rsState = Level_Final_Serial.Serial_Lost_Frame;
		}else{
			if (address != -1){
				//检测地址头			
				rsOut = rsByte[0] == address;
			}
			if (rsOut == false) {
				rsState = Level_Final_Serial.Serial_wrong_Address;
			}else{
				//对接收的数据进行crc校验，检查是否通讯故障
				rsOut = CalcCRC.checkCrc16(rsByte);
				if (rsOut == false) {
					rsState = Level_Final_Serial.Serial_wrong_Frame;
				}
			}
		}
		return rsState;
	}
	
	//****************************读取 仪器的温湿度数据 end   *******************************************//
	
	//****************************配置 或  读取 仪器硬件参数 start*****************************************//
	/**
	 * @describe: 发送设置命令<br>
	 * funCode:功能码<br>
	 * 下发设置通讯地址的命令:			帧头（0xFB）	功能码(0x66)	数据区字节个数	数据区	CRC低字节	CRC高字节<br>
	 *									下发数据为要被设置通讯地址的模块的机号（6个字节），通讯地址（1个字节）<br>
	 *									返回:，0x55,0x55表示设置成功；0xff，0xff表示设置不成功	<br>
	 * 读机号，通讯地址，软件版本号的命令:	帧头（0xFB）	功能码(0x67)	数据区字节个数	数据区	CRC低字节	CRC高字节<br>
	 *									下发数据为模块的机号（6个字节）<br>
	 *									返回:机号（6个字节），通讯地址（1个字节），软件版本号（2个字节）<br>
	 * 下发设置模块机号的命令:			帧头（0xFB）	功能码(0x68)	数据区字节个数	数据区	CRC低字节	CRC高字节<br>
	 *									下发数据为要被设置的机号（6个字节）<br>
	 *									返回:0x55,0x55表示设置成功；0xff，0xff表示设置不成功 <br>
	 * @describe: 发送设置命令
	 * @param serialSetDataBean 装有 机号 的数据bean
	 * @param funCode 功能码
	 * @param address 地址
	 * @date:2009-11-5
	 */
	public void sendMsgForMac(BeanForSetData serialSetDataBean, int funCode, int address) throws Exception{
		char [] rsout = reverseDecToHex(serialSetDataBean);
		if (address == -1){
			char[]	rs = {0xFB, (char) funCode, 6, rsout[0], rsout[1], rsout[2], rsout[3],rsout[4], rsout[5], 0, 0};
			 rs = CalcCRC.getCrc16(rs);
			 //System.out.println("send:"+FunctionUnit.bytesToHexString(rs));
			 final_Level.writePort(rs);	
			 rs = null;
		}else{
			char[]	rs = {0xFB, (char) funCode, 7, rsout[0], rsout[1], rsout[2], rsout[3],rsout[4], rsout[5], (char) address, 0, 0};
			rs = CalcCRC.getCrc16(rs);
			//System.out.println(FunctionUnit.bytesToHexString(rs));
			final_Level.writePort(rs);
			rs = null;
		}
		//停止，以保证数据发送成功
		Thread.sleep(frameInterval);
	}
	
	/**
	 * @describe: .读取返回信息,不进行数据存储
	 * @return 通讯错误,返回null值;其他返回char类型数组
	 * @date:2009-11-5
	 */
	public char[] readByteNoDB() throws Exception{
		// 读取数据
		char[] rsByte = final_Level.readPackData();
		if (rsByte != null){
			//System.out.println("rece:" + FunctionUnit.bytesToHexString(rsByte));
		}
		int rsState = checkReturn(rsByte, -1);
		rsByte = rsState == Level_Final_Serial.Serial_right_Frame ? rsByte : null;
		return rsByte;
	}
	
	/**
	 * @describe: 下发校准时间的命令:	<br>
	 * 			帧头（0xFB）	功能码	(0x65)	数据区字节个数	数据区	CRC低字节	CRC高字节<br>
	 * 			数据区为年，月，日，时，分，秒共六个字节;<br>
	 * 			无返回<br>
	 *			下发校验时间	funCode:0x65<br>
 	 * @date:2009-11-5
	 */
	public void writeTime() throws Exception{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		char year,month,day,hour,minute,second ;
		year = (char) Integer.parseInt(String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4));
		month = (char) (calendar.get(Calendar.MONTH) + 1);
		day = (char) calendar.get(Calendar.DAY_OF_MONTH);
		hour = (char) calendar.get(Calendar.HOUR_OF_DAY);
		minute = (char) calendar.get(Calendar.MINUTE);
		second = (char) calendar.get(Calendar.SECOND);
		char[]	rs = {0xFB, 0x65, 6, year, month, day, hour, minute, second, 0, 0};
		rs = CalcCRC.getCrc16(rs);
		final_Level.writePort(rs);
		 
		//停止，以保证数据发送成功
		Thread.sleep(frameInterval);
	}
	
	/**
	 * @describe: 设置地址	funCode:0x66
	 * @date:2009-11-5
	 */
	public void setAddr(BeanForSetData serialSetDataBean, int address) throws Exception{
		sendMsgForMac(serialSetDataBean, 0x66, address);
	}
	
	/**
	 * @describe: 通过机号，读通讯地址和软件版本号	funCode:0x67
	 * @date:2009-11-5
	 */
	public void readInfo(BeanForSetData serialSetDataBean) throws Exception{
		sendMsgForMac(serialSetDataBean, 0x67, -1);
	}
	
	/**
	 * @describe: 设置机器码	funCode:0x68
	 * @date:2009-11-5
	 */
	public void setMac(BeanForSetData serialSetDataBean) throws Exception{
		sendMsgForMac(serialSetDataBean, 0x68, -1);
	}
	
	/**
	 * @describe: 读取电量. (1.5V的电池)<br>
	 * 			正常区域:0< V < 2000.<br>
	 * 			v < 1200 : 电量不足<br>
	 * 			v <=0 and v >= 2000 : 电量用尽<br>
	 * 地址码	功能码	寄存器地址	寄存器长度	CRC低字节	CRC高字节<br>
	 * 1个字节	1个字节	2个字节		2个字节		1个字节		1个字节<br>
	 * 寄存器地址:00 03	寄存器长度:00 01<br>
	 * 后备电池电压：等于（实际值*1000）。 警戒值:1200  无电:2000.<br>
	 * @param addr 仪器地址
	 * @throws Exception
	 * @date:2009-11-5
	 */
	public void readPower(int addr) throws Exception{
		writeByteForGet(addr, 0, 3, 0, 1);
	}
	
	/**
	 * @describe 读取前历史数据的个数。<br>
	 * 地址码	功能码	寄存器地址	寄存器长度	CRC低字节	CRC高字节<br>
	 * 1个字节	1个字节	2个字节		2个字节		1个字节		1个字节<br>
	 * 寄存器地址: 0x0005:当前历史数据的个数。
	 * 功能码:03
	 * @param addr 仪器地址
	 * @date:2009-11-13
	 */
	public void readCurrentStoreNumber(int addr) throws Exception{
		writeByteForGet(addr, 0, 5, 0, 1);
	}
	
	//****************************设置参数****************************//
	
	/**
	 * @describe: 根据仪器地址,发送设置仪器属性命令<br>
	 * 地址码	功能码	寄存器地址	寄存器长度	CRC低字节	CRC高字节<br>
	 * 1个字节	1个字节	2个字节		2个字节		1个字节		1个字节<br>
	 * @param address 仪器地址
	 * @param memHigh 寄存器地址:高位
	 * @param menLow  寄存器地址:低位
	 * @param dataHigh 属性数据:高位
	 * @param dataLow  属性数据:低位
	 * @date:2009-11-5
	 */
	public void writeByteForSet(int address, int memHigh, int menLow,int dataHigh, int dataLow) throws Exception{
		writeByte(address, 0x06, memHigh, menLow, dataHigh, dataLow);
	}	
	
	/**
	 * @describe:	设置仪器默认温度值
	 * 功能码:06
	 * 寄存器地址:0x0010
	 * 需要发送的温度值:实际值*100 + 27315
	 * @param addr	仪器地址
	 * @param temp	实际温度值
	 * @throws Exception:
	 * @date:2009-11-24
	 */
	public void setDefaultTemp(int addr, int temp) throws Exception{
		//int tempInt = (int) (temp * 100 + 27315);
		writeByteForSet(addr,0x00,0x10,temp&0xFFFF>>8,temp&0xFF);
	}
	
	/**
	 * @describe: 设置仪器默认湿度值	
	 * 功能码:06
	 * 寄存器地址:0x0011
	 * 需要发送的湿度值:实际值*100
	 * @param addr 仪器地址
	 * @param humi 实际湿度值	
	 * @throws Exception:
	 * @date:2009-11-24
	 */
	public void setDefaultHumi(int addr, int humi) throws Exception{
		//int humiInt = (int) (humi * 100);
		writeByteForSet(addr,0x00,0x11,humi&0xFFFF>>8,humi&0xFF);
	}
	
	/**
	 * @describe:	设置仪器默认露点值
	 * 功能码:06
	 * 寄存器地址:0x0012
	 * 需要发送的露点值:实际值*100 + 27315
	 * @param addr	仪器地址
	 * @param dewPoint	实际露点值
	 * @throws Exception:
	 * @date:2009-11-24
	 */
	public void setDefaultDewPoint(int addr, int dewPoint) throws Exception{
		//int dewPointInt = (int) (dewPoint * 100 +27315);
		writeByteForSet(addr,0x00,0x12,dewPoint&0xFFFF>>8,dewPoint&0xFF);
	}
	
	/**
	 * @describe: 设置仪器记录历史数据的间隔,默认值60(硬件代码里面写的)
	 * 功能码:06
	 * 寄存器地址 0x0013
	 * 记录时间间隔，秒的倍数.
	 * @param addr 仪器地址
	 * @param interval: 记录时间间隔
	 * @date:2009-11-14
	 */
	public void setRecInterval(int addr, int interval) throws Exception{
		writeByteForSet(addr,0x00,0x13,interval&0xFFFF>>8,interval&0xFF);
	}
	
	/**
	 * @describe: 设置仪器能记录的最大数据量。默认值5000(硬件代码里面写的)
	 * 功能码:06
	 * 寄存器地址 0x0014
	 * 记录数据个数最大设定值.
	 * @param addr  仪器地址
	 * @param interval: 设定的最大值
	 * @date:2009-11-14
	 */
	public void setMaxStoreNumber(int addr, int maxNumber) throws Exception{
		writeByteForSet(addr,0x00,0x14,maxNumber&0xFFFF>>8,maxNumber&0xFF);
	}
	
	/**
	 * @describe:	设置仪器湿度的最大值,超过这个值就报警
	 * 功能码:06
	 * 寄存器地址 0x0015
	 * 湿度高报警门限值
	 * @param addr 仪器地址
	 * @param value 湿度的最大值
	 * @date:2009-11-14
	 */
	public void setHumiMaxValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x15,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	设置仪器湿度的最小值,低于这个值就报警
	 * 功能码:06
	 * 寄存器地址 0x0016
	 * 湿度低报警门限值
	 * @param addr 仪器地址
	 * @param value 湿度的最小值
	 * @date:2009-11-14
	 */
	public void setHumiMinValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x16,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	设置仪器温度的最大值,超过这个值就报警
	 * 功能码:06
	 * 寄存器地址 0x0017
	 * 温度高报警门限值
	 * @param addr 仪器地址
	 * @param value 温度的最大值
	 * @date:2009-11-14
	 */
	public void setTempMaxValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x17,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	设置仪器温度的最小值,低于这个值就报警
	 * 功能码:06
	 * 寄存器地址 0x0018
	 * 温度低报警门限值
	 * @param addr 仪器地址
	 * @param value 温度的最小值
	 * @date:2009-11-14
	 */
	public void setTempMinValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x18,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	设置仪器露点高报警门限值
	 * 功能码:06
	 * 寄存器地址 0x0019
	 * @param addr 仪器地址
	 * @param value 露点高报警门限值
	 * @date:2009-11-14
	 */
	public void setDewPointMaxValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x19,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	设置仪器露点低报警门限值
	 * 功能码:06
	 * 寄存器地址 0x001a
	 * @param addr 仪器地址
	 * @param value 露点低报警门限值
	 * @date:2009-11-14
	 */
	public void setDewPointMinValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x1a,value&0xFFFF>>8,value&0xFF);
	}
	
	//****************************配置 或  读取 仪器硬件参数  end****************************************//
	
	//****************************数据操作区 start **************************************************//
	
	/**
	 * @describe: 更新数据bean,address暂时不用
	 * @param address 仪器地址
	 * @param equipmentId 仪器主键ID
	 * @param rsByte 含有温湿度数据的char数组
	 * @param state 返回帧检查结果.  只有返回Serial_right_Frame(值为1),为正常成功的帧,其他都为失败帧.
	 * @param dataLen(3:温度,湿度,露点4个数据)(4:温度,湿度,露点,电压4个数据)
	 * @date:2009-11-5
	 */
	public void updateDataBean(int address,int equipmentId, char[] rsByte, int state, int dataLen){
		EquipData equip = commonDataUnit.getEquipByID(equipmentId);
//		if (addressData == null){
//			addressData = new HashMap<Integer, BeanForPortData>();  
//		}
		
		if (state == Level_Final_Serial.Serial_right_Frame){
			Date tempDate = new Date();
			
			// 根据dataLen,显示 丢帧记录 
			int humi = 0,temp = 0, dew = 0, power = 0;
			if ( dataLen >= 1)
				humi = (int) ((rsByte[3] << 8) + rsByte[4] + equip.getHumiDev() * 100);
			if ( dataLen >= 2)
				temp = (int) (((rsByte[5] << 8) + rsByte[6] - 27315) + equip.getTempDev() * 100);
			if ( dataLen >= 3)
				dew = ((rsByte[7] << 8) + rsByte[8] - 27315);
			if ( dataLen >= 4)
				power = (rsByte[9] << 8) + rsByte[10];
			
			BeanForPortData dataBean =  new BeanForPortData();
			dataBean.setAddress(address);
			dataBean.setEquipmentId(equipmentId);
			dataBean.setState(state); //设置仪器运行状态
			dataBean.setTemp(temp);							
			dataBean.setHumi(humi);
			dataBean.setDewPoint(dew);
			dataBean.setPowerV(power);
			dataBean.setRecTime(new Date());
			dataBean.setRecLong(tempDate.getTime());
			dataBean.setMark(1);
			
			// 保存到临时区域
			saveToFlash(dataBean);
			
			//存入临时数据列表和flash数据容器
//			if (addressData.containsKey(equipmentId)){
//				addressData.remove(equipmentId);
//			}
//			addressData.put(equipmentId, dataBean);//端口测试用和总览画面
//			fillToTempFlash(equipmentId, dataBean);//flash显示用
			
//			System.out.println("仪器地址:" + equipmentId + "地址:" + address + " 温度:"+ temp + 
//			" 湿度:" + humi + " 露点:" + dew+"电压: " + power);
			
			//存入数据库  - [温度-湿度-eqID-记录时间]
			Record record = new Record();
			record.setTemperature(FunctionUnit.FLOAT_DATA_FORMAT.format( temp / (float) 100) );
			record.setHumidity(FunctionUnit.FLOAT_DATA_FORMAT.format( humi / (float) 100) );
			record.setEquipmentId(equipmentId);
			record.setRecTime(tempDate);
			record.setRecLong(tempDate.getTime());
			
			// 向access数据库传递的数据
			record.setDsrsn(equip.getDsrsn());
			record.setLabel(equip.getPlaceStr()+"-"+equip.getMark());
			record.setEquitype(equip.getEquitype());
			record.setShowAccess(equip.getShowAccess());
			
			// 对数据记录进行范围检查,以便发现不正常数据, 及时报警
			commonDataUnit.checkDataWarn(record, equip);
			// 方案一: 把数据插入即时数据表(单条)
			//MainService.getInstance().insertRecord(record);
			// 方案二: 把数据插入临时缓存,然后批量插入数据库
			saveToBatchInsert(record);
		}
	}
	
	/**
	 * @describe: flash临时数据容器填充
	 * @param equipmentId 仪器主键ID
	 * @param dataBean 含有温湿度数据的数据结构
	 * @date:2009-11-5
	 */
//	public void fillToTempFlash(int equipmentId, BeanForPortData dataBean){
//		if( (tempFlashData != null) && (tempFlashData.containsKey(equipmentId)) ){
//			//把数据真正的Flash数据缓存表,然后清理Flash临时数据
//			fillToRealFlash(tempFlashData);
//			tempFlashData.clear();
//		}
//		if (tempFlashData == null){
//			tempFlashData = new HashMap<Integer, BeanForPortData>();
//		}
//		//把数据插入Flash临时数据
//		tempFlashData.put(dataBean.getEquipmentId(), dataBean);
//	}
	
	/**
	 * @describe:	把临时Falsh数据插入动态Flash数据列表
	 * @param tempFlashData:
	 * @date:2009-12-3
	 */
//	private synchronized void fillToRealFlash(Map<Integer, BeanForPortData> tempFlashData) {
//		// 获取 - 实时flash最多显示的点数
//		int FLASH_FILL_DATA_MAX = Integer.parseInt(
//				CommonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_LINE));
//		int tempLen = currentFlashData.size() - FLASH_FILL_DATA_MAX;
//		// 删除最前的1个或者几个数据,达到更新的效果
//		for (int i = 0; i <= tempLen; i++) {
//			currentFlashData.remove(0);
//		}
//		// 添加新数据
//		Map<Integer, BeanForPortData> flashData = new HashMap<Integer, BeanForPortData>();
//		Set<Entry<Integer, BeanForPortData>> entrys = tempFlashData.entrySet();
//		int equipId ;
//		BeanForPortData bean;
//        for(Entry<Integer, BeanForPortData> entry: entrys){
//            equipId = entry.getKey();
//            bean = null;	bean = entry.getValue();
//            flashData.put(equipId, bean);
//        }
//		currentFlashData.add(flashData);
//	}
	
	/**
	 * @describe:	保存空数据
	 * @param address 仪器地址 - 保留,暂时不用
	 * @param equipmentId 仪器主键ID
	 * @date:2010-3-22
	 */
	public void saveEmptyData(int address,int equipmentId){
		BeanForPortData record = new BeanForPortData();
		record.setMark(0); // 0无数据,1有数据
		record.setAddress(address);
		record.setEquipmentId(equipmentId);
		saveToFlash(record);
	}
	
	/**
	 * @describe:	数据插入临时缓存,等到下一个重复EquipmentId时,然后把缓存的数据批量插入数据库(总览和实时曲线用)
	 * @param record:
	 * @date:2010-3-22
	 */
	private void saveToFlash(BeanForPortData record) {
		if (addressData.containsKey(record.getEquipmentId())){
			// 把数据修改到临时数据库,提供给总览和实时画面用
			//MainService.getInstance().updateMinRecord(addressData);
			mainService.updateMinRecord(addressData);
			// 清理缓存
			addressData.clear();			
		}
		//把数据插入缓存
		addressData.put(record.getEquipmentId(), record);		
	}

	/**
	 * @describe: 数据插入临时缓存,等到下一个重复EquipmentId时,然后把缓存的数据批量插入数据库
	 * @param record 含有温湿度信息的数据单元
	 * @date:2009-11-8
	 */
	public void saveToBatchInsert(Record record ){
		if (currentNewRecords.containsKey(record.getEquipmentId() ) ){
			try {
				long currentMinute = 0;
				int equipId = record.getEquipmentId();
				// 同一分钟的内数据做平均数处理,然后放入缓存,之后等待下一个时间,再存入数据库
				if (FunctionUnit.getTypeTime(record.getRecTime(), Calendar.MINUTE) ==
					FunctionUnit.getTypeTime(currentNewRecords.get(equipId).getRecTime(), Calendar.MINUTE) ){
					String statStr = "";
					statStr = FunctionUnit.getStatFloat(record.getTemperature(), 
							currentNewRecords.get(equipId).getTemperature(), FunctionUnit.STAT_AVG, 1);
					record.setTemperature(statStr);
					statStr = FunctionUnit.getStatFloat(record.getHumidity(), 
							currentNewRecords.get(equipId).getHumidity(), FunctionUnit.STAT_AVG, 0);
					record.setHumidity(statStr);
				}else{
					// 在Access存储功能开启的情况下处理
					if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_ACCESS_STORE)) == 2){
						//if(commonDataUnit.isAccessLinkState()){ // 随时测试数据连通性
							// 为了达到每隔10分钟向access数据库传递一次数据,这里采用分钟数取整方法判断
							currentMinute = (new Date()).getTime();
							if (commonDataUnit.isAccessDataEmpty()) {// access数据库 中 数据为空
								serviceAccess.insertBatch(fillAccessData(currentNewRecords, 1));
								commonDataUnit.setAccessTimeFalg(currentMinute);
								// 等到 通讯的数据和需要显示的数据个数相等时, 才设置标志, 以保证数据的完整性
								if (commonDataUnit.getAccessLinkCount() == currentNewRecords.size()){
									//log.info("access信息-增添:数据已经收集满-------------");
									commonDataUnit.setAccessDataEmpty(false);
								}else{
									//log.info("access信息-增添:数据还没有收集满-------------");
								}
							}else{
								if ( (currentMinute - commonDataUnit.getAccessTimeFalg()) >= 1000 * 60 * 10 ){ // 相差10分钟
									// log.info("access信息:更新数据到access-------------");
									// 设置 access 数据库中 不为空(有数据)
									if (commonDataUnit.isAccessDataEmpty()){
										commonDataUnit.setAccessDataEmpty(false);
									}
									commonDataUnit.setAccessTimeFalg(currentMinute);
									serviceAccess.updataBatch(fillAccessData(currentNewRecords, 2));
								}
							}
						//}
					}
					
					//把数据插入即时数据表(批量)
					//MainService.getInstance().insertRecordBatch(currentNewRecords);
					mainService.insertRecordBatch(currentNewRecords);
					// 清理缓存
					currentNewRecords.clear();
				}
				
//				// 把数据插入即时数据表(批量),然后清理缓存--测试用--开始
//				MainService mainService = new MainService();
//				mainService.insertRecordBatch(currentNewRecords);
//				// 在Access存储功能开启的情况下处理
//				if (Integer.parseInt(CommonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_ACCESS_STORE)) == 2){
//					if(CommonDataUnit.isAccessLinkState()){ // 随时测试数据连通性
//						if (CommonDataUnit.isAccessDataEmpty()) {// access数据库 中 数据为空
//							AccessService.getInstance().insertBatch(fillAccessData(currentNewRecords, 1));
//							CommonDataUnit.setAccessDataEmpty(false);
//						}else{
//							//设置 access 数据库中 不为空(有数据)
//							if (CommonDataUnit.isAccessDataEmpty()){
//								CommonDataUnit.setAccessDataEmpty(false);
//							}
//							AccessService.getInstance().updataBatch(fillAccessData(currentNewRecords, 2));
//						}	
//					}
//				}
//				currentNewRecords.clear();
//				// 把数据插入即时数据表(批量),然后清理缓存--测试用--结束 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//把数据插入缓存
		currentNewRecords.put(record.getEquipmentId(), record);
	}
	
	/**
	 * @describe:	封装向access数据库需要的 数据
	 * @param recordMap 封装数据对象
	 * @param type 封装类型: 1:插入用 2:修改用
	 * @return:
	 * @date:2010-3-18
	 */
	public List<Data4Access> fillAccessData(Map<Integer, Record> recordMap, int type){
		List<Data4Access>  datas = new ArrayList<Data4Access>();
		Data4Access data4Access = null;
		Record record = null;
		String tempstr ;
		
		for (Entry<Integer, Record> rec : recordMap.entrySet()) {
			record = rec.getValue();
			if (record.getShowAccess() == 1){ // 只保存需要显示到access中的仪器
				data4Access = new Data4Access(); 
				
				tempstr =FunctionUnit.getDateToStr(record.getRecTime(),
						FunctionUnit.Calendar_END_SECOND, FunctionUnit.UN_SHOW_CHINESE);			
				
				data4Access.setStrDSRSN(record.getDsrsn());// 仪器 编号
				data4Access.setStrDateTime(tempstr);// 记录时间
				data4Access.setStrTemp(record.getTemperature());// 温度
				data4Access.setStrHUM(record.getHumidity());// 湿度
				
				data4Access.setStrAirPress("");// 压力
				if(type == 1){ // 插入数据时,需要提供所用数据,修改时只要一部分
					data4Access.setStrDSRName(record.getLabel());// 仪器标签
					//1,'温湿度';2,'单温度';3,'单湿度'
					tempstr = record.getEquitype() == 1?"TH":record.getEquitype()==2?"T":"H";
					data4Access.setStrEquipmentType(tempstr);
				}
				datas.add(data4Access);
			}
		}
		return datas;
	}
	
	/**
	 * @describe: 数据清空 - 端口测试用和总览画面
	 * @date:2009-11-5
	 */
//	public void clearBarData(){
//		addressData.clear();
//	}	
	
	/**
	 * @describe: 异常数据,清除Map中的key=equipmentId的值
	 * @param address 仪器地址 - 保留,暂时不用
	 * @param equipmentId 仪器主键ID
	 * @param readType readType 串口读写保存类型.只有READ_USEFULL_DATA时才删除相应临时数据
	 * @date:2009-11-5
	 */
//	public void removeDataBean(int address,int equipmentId,int readType){
//		//过滤测试数据,对用的数据进行操作
//		if (readType == Level_Second_Serial.READ_USEFULL_DATA){
//			
//			//清空临时数据集 --"实时曲线"和"端口测试"用的临时数据集
//			if (addressData != null){
//				//判断数据集是否包含key值，有的话，移除
//				if (addressData.containsKey(equipmentId)){
//					addressData.remove(equipmentId);
//				}
//			}
//			
//			//添加Null对象给数据集 -- "实时曲线"用的临时数据集
//			BeanForPortData dataBean = new BeanForPortData();
//			dataBean.setState(2);// 帧状态:错误帧
//			dataBean.setEquipmentId(equipmentId);
//			dataBean.setRecTime(new Date());
//			fillToTempFlash(equipmentId,dataBean);
//		}
//	}
	
	/**
	 * @describe: 返回"总览画面"用的临时数据集
	 * @date:2009-11-8
	 */
//	public Map<Integer, BeanForPortData> getAddressData() {
//		return addressData;
//	}
	
	/**
	 * @describe: 返回"实时曲线"用的临时数据集
	 * @date:2009-11-8
	 */
//	public List<Map<Integer, BeanForPortData>> getCurrentFlashData() {
//		return currentFlashData;
//	}

	/**
	 * @describe: 获取equipmentId对应的临时数据("端口测试"用的临时数据集)
	 * @param equipmentId 仪器主键ID
	 * @date:2009-11-8
	 */
//	public BeanForPortData getDataWithAddress(int equipmentId) {
//		if (addressData != null){
//			return addressData.containsKey(equipmentId)?addressData.get(equipmentId):null;
//		}else{
//			return null;
//		}
//	}
	
	//****************************数据操作区 end ****************************************************//
	
	/**
	 * @describe: 代码复用片段-(10进制的字符串，转化成16进制的数字.如12->0x18)
	 * @param serialSetDataBean 硬件属性的数据结构
	 * @date:2009-11-5
	 */
	public static char[] reverseDecToHex(BeanForSetData serialSetDataBean){
		char [] rs = new char[6];
		rs[0] = (char) Integer.parseInt(serialSetDataBean.getMCType().substring(0, 2), 16);
		rs[1] = (char) Integer.parseInt(serialSetDataBean.getMCType().substring(2, 4), 16);
		rs[2] = (char) Integer.parseInt(serialSetDataBean.getMCOrder().substring(0, 2), 16);
		rs[3] = (char) Integer.parseInt(serialSetDataBean.getMCOrder().substring(2, 4), 16);
		rs[4] = (char) Integer.parseInt(serialSetDataBean.getMCNo().substring(0, 2), 16);
		rs[5] = (char) Integer.parseInt(serialSetDataBean.getMCNo().substring(2, 4), 16);		
		return rs;
	}
	
}

