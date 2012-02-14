package com.htc.model.seriaPort;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 串口文件存放位置.<br>
 * rxtxSerial.dll 放在%java_home%\bin下<br>
 * RXTXcomm.jar   放在%java_home%\lib下<br>
 * 
 * 作用 : 短信模块用到的串口通信类<br>
 * 类使用介绍:<br>
 * 类使用方法: <br>
 * 		1:init(串口号,波特率,串口名称);初始化<br>
 * 		2:setParam(读取串口数据间隔,通信没有完成的时候,重试次数);调整附加的一些参数<br>
 * 		3:openPort();打开串口<br>
 * 		4:closePort();关闭串口<br>
 * 		5:writeByte(发送指令);写串口命令 - 发送AT这个指令<br>
 * 		6:getReceiveMsg();读取接收缓冲池<br>
 */

//public class SimCard_Serial implements SerialPortEventListener  {
public class SimCard_Serial {
	
	private Log log = LogFactory.getLog(SimCard_Serial.class);
	/**
	 * 暂时存储数据的缓冲长度
	 */
	public static final int PACKET_LENGTH = 500;
	
	private SerialPort serialPort;					
	private CommPortIdentifier identifier;
	private OutputStream out;
	private InputStream in;
	private StringBuffer buffer4SimCard = new StringBuffer();		// 输入流缓冲池
	
	//需要设置的参数
	String portName; 					// 串口号：如：com1
	int baudrate;						// 波特率
	String appname = "sms_port"; 		// 串口程序名
	
	//	不一定要设定的参数(有默认值)
	int timeOut; 						// 延迟时间(毫秒数)
	int dataBits; 						// 数据位
	int stopBits; 						// 停止位
	int parity; 						// 奇偶检验
	
	boolean portIsOpenFlag = false;		// 串口是否打开标识 (重要)
	
	/****************************************基本参数配置****************************************/
	// 构造方法
	public SimCard_Serial() {
		// 延迟时间 	波特率		数据位					 停止位				奇偶检验				串口号	串口程序名
		init(60, 	9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, "COM1", "sms_port");
	}

	/**
	 * @describe: 初始化类
	 * @param timeOut 等待时间(毫秒数)
	 * @param baudrate波特率
	 * @param dataBits 数据位
	 * @param stopBits 停止位
	 * @param parity 奇偶检验
	 * @param portName 串口号：如：com1
	 * @param appname 串口程序名
	 */
	public void init(int timeOut, int baudrate, int dataBits, int stopBits, int parity,String portName, String appname) {
		this.timeOut = timeOut;
		this.baudrate = baudrate;
		this.dataBits = dataBits;
		this.stopBits = stopBits;
		this.parity = parity;
		this.portName = portName.toUpperCase();
		this.appname = appname;		
	}	
	
	/**
	 * 初始化 类串口号 和 波特率
	 * @param portName 串口号：如：com1
	 * @param baudrate 波特率
	 * @param appname 串口程序名
	 */
	public void init(String portName, int baudrate, String appname) {
		this.portName = portName.toUpperCase();
		this.baudrate = baudrate;
		this.appname = appname;
	}	
	/****************************************串口操作:打开,关闭串口,发送数据****************************************/	
	/**
	 * @describe: 打开串口
	 * @param portStr 串口号. 如: COM3
	 * @param baudrate 波特率
	 * @param appName 串口占用程序的命名
	 * @return: true:打开串口正常 false:打开串口异常
	 */
	public boolean openPort() {
		boolean rsBool = true;
		try {
			identifier = CommPortIdentifier.getPortIdentifier(portName);		//获取串口对象
			if (identifier != null) {
				serialPort = (SerialPort) identifier.open(appname, timeOut);	// 打开通讯端口
				
				//	使用监听模式处理数据,会用问题,不采用
//				serialPort.notifyOnDataAvailable(true);							// 设置串口有数据的事件true有效,false无效
//				serialPort.notifyOnBreakInterrupt(true); 						// 设置中断事件true有效,false无效
//				/* 注册一个SerialPortEventListener事件来监听串口事件 */
//				serialPort.addEventListener(this); 								// 增加事件监听
				
//				/* 获取端口的输入,输出流对象 */
				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				/* 设置串口初始化参数，依次是波特率，数据位，停止位和校验 */
				serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
				serialPort.setDTR(true);
				serialPort.setRTS(true);
			}
		} catch (PortInUseException e) {
			rsBool = false;
			log.info("SimCard信息:" + portName+ ": 串口已经被占用...");
		} catch (Exception e) {
			rsBool = false;
			log.info("SimCard信息:" + portName+ ": 初始化串口详细信息:" + e.toString());
		}
		return rsBool;
	}	
	
	/**
	 * 关闭串口，释放资源
	 * @return: true: 关闭成功   false:关闭失败
	 * @date:2010-5-8
	 */
	public boolean closePort() {
		boolean rsBool = true;
		if (out != null) {
			try {
				out.close();		in.close();
				out = null;			in = null;
			} catch (IOException e) {
				rsBool = false;
				log.info("关闭串口时出错:" + e.getMessage() + e.toString());
			}
		}
		if (serialPort != null) {
			serialPort.notifyOnDataAvailable(false);
			serialPort.notifyOnBreakInterrupt(false);
			serialPort.removeEventListener();
			serialPort.close();
			serialPort = null;
		}
		return rsBool;
	}	
	
	/**
	 * @describe: 写串口命令 - 发送AT这个指令
	 * @param sendmsg 发送的数据
	 */
	public void writeByte(String sendmsg) throws Exception{
		if (out == null){ return;}
		out.write(sendmsg.getBytes());
		out.flush();		
	}	
	
	/**
	 * @describe: 读取串口数据
	 */
	public void readPackData() {
		byte[] readBuffer = new byte[PACKET_LENGTH];
		int numBytes = 0;
		if (in == null){ return ;}
		try {
			while (in.available() > 0) {
				numBytes = in.read(readBuffer);
				for (int i = 0; i < numBytes; i++) {
					buffer4SimCard.append((char) (readBuffer[i]));
				}// end for
			}// end while
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/****************************************事件监听 获取缓冲池内容****************************************/	
	// 串口监听事件--使用监听模式处理数据,会用问题,不采用
	public void serialEvent(SerialPortEvent event) {
		int newData = 0;
		switch (event.getEventType()) {
		case SerialPortEvent.DATA_AVAILABLE:// DATA_AVAILABLE - 有数据到达
			while (newData != -1) {
				try {
					newData = in.read();
					if (newData == -1) {break;}
					if ('\r' == (char) newData) {
						buffer4SimCard.append('\n');
					} else {
						buffer4SimCard.append((char) newData);
					}
				} catch (IOException ex) {
					System.err.println(ex);
					return;
				}
			}
			break;
		case SerialPortEvent.BI:// BI - 通讯中断.
			System.out.println("\n--- BREAK RECEIVED ---\n");
			break;
		}
	}		
	
	/**
	 * 获取短信缓冲池内容
	 */
	public String getReceiveMsg(){
		readPackData();
		return buffer4SimCard.toString();
	}
	
	/**
	 * 清除短信缓冲池内容
	 */
	public void clearReceiveMsg(){
		clearReceiveMsg(buffer4SimCard.length());
	}
	/**
	 * 清除短信缓冲池内容,删除到 设置最后一个位置前面的内容
	 */
	public void clearReceiveMsg(int endIndex){
		buffer4SimCard.delete(0, endIndex);
	}
	
	/**
	 * 把没有处理的信息放回缓冲池中
	 */
	public void backToPool(String undealStr){
		buffer4SimCard.append(undealStr);
	}
	
	/****************************************属性数据****************************************/
	/**
	 * 串口是否打开标识
	 */
	public boolean isPortIsOpenFlag() {
		return portIsOpenFlag;
	}
	
	/**
	 * 串口是否打开标识
	 */
	public void setPortIsOpenFlag(boolean portIsOpenFlag) {
		this.portIsOpenFlag = portIsOpenFlag;
	}
	/****************************************工具方法****************************************/		
	/**
	 * @describe: 列举全部串口名称	
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAllComPorts(){
		List<String> comList = new ArrayList<String>();
		Enumeration en = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier portIdRs = null;
		
		while (en.hasMoreElements()) {
			portIdRs = (CommPortIdentifier) en.nextElement();
			if (portIdRs.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				comList.add(portIdRs.getName());
			}
		}
		return comList;
	}
	
}

