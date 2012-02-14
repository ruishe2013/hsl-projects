package com.htc.model.seriaPort;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

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
 * 类使用方法: <br>
 * getInstance->setAppname,init->initialize->isPortOpen->readPackData->writePort->ClosePort<br>
 * @ SerialBean.java 作用 : 串口操作 -- 最后一层: 步骤:初始化串口->读写数据 注意事项 : 无 VERSION DATE BY
 * CHANGE/COMMENT 1.0 2009-11-4 YANGZHONLI create
 */
public class Level_Final_Serial implements SerialPortEventListener {
	private Log log = LogFactory.getLog(Level_Final_Serial.class);

	/**
	 * 正确的侦
	 */
	public static final int Serial_right_Frame = 1;
	/**
	 * 丢失的侦
	 */
	public static final int Serial_Lost_Frame = 2;
	/**
	 * 错误的侦
	 */
	public static final int Serial_wrong_Frame = 3;
	/**
	 * 地址不对的侦
	 */
	public static final int Serial_wrong_Address = 4;
	/**
	 * 数据错误的侦
	 */
	public static final int Serial_wrong_Date = 5;
	/**
	 * 长度不对的侦
	 */
	public static final int Serial_data_len = 11;
	/**
	 * 数据包长度
	 */
	public static final int PACKET_LENGTH = 500;

	private SerialPort serialPort;
	private CommPortIdentifier identifier;
	
	//public static Level_Final_Serial final_Level;
	String PortName;					
	OutputStream out;
	InputStream in;

	String appname = "HtcSerialBean"; // 串口程序名
	int timeOut; // 延迟时间(毫秒数)
	int baudrate; // 波特率
	int dataBits; // 数据位
	int stopBits; // 停止位
	int parity; // 奇偶检验

	boolean portIsOpenFlag; // 串口是否打开标识 (重要)

	// 程序名,等待时间,波特率,数据位,停止位,奇偶检验
	// timeOut:("SerialBean",3000, 9600,
	// SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
	/**
	 * @describe: 初始化类
	 * @param timeOut 等待时间
	 * @param baudrate波特率
	 * @param dataBits 数据位
	 * @param stopBits 停止位
	 * @param parity 奇偶检验
	 * @date:2009-11-5
	 */
	private void init(int timeOut, int baudrate, int dataBits, int stopBits, int parity) {
		this.timeOut = timeOut;
		this.baudrate = baudrate;
		this.dataBits = dataBits;
		this.stopBits = stopBits;
		this.parity = parity;
	}

	/**
	 * @describe: 设置 串口程序名
	 * @date:2010-3-2
	 */
	public void setAppname(String appname) {
		this.appname = appname;
	}

	/**
	 * @describe: 设置串口字符串
	 * @date:2009-11-5
	 */
	public void setPortName(int portName) {
		this.PortName = "COM" + portName;
	}
	public void setPortName(String portName) {
		this.PortName = portName;
	}

	public Level_Final_Serial() {
		portIsOpenFlag = false;
	}
	
	/**
	 * @describe: 返回串口是否打开
	 * @return true : 打开 false: 没有打开
	 * @date:2009-11-5 
	 */
	public boolean isPortOpen() {
		return portIsOpenFlag;
	}

	/**
	 * @describe: 获取需要帧之间需要间隔的时间(毫秒) 功能公式(1*12(位)*数据长度*1000/波特率 + 附加毫秒数)
	 * @param appendMillsec 附加毫秒数
	 * @param dataLen 数据区数据长度
	 * @param baudrate 波特率
	 * @return 得到合适的帧发送,间隔毫秒数
	 * @date:2009-11-5
	 */
	public static int getFrameInterval(int appendMillsec, int dataLen, int baudrate) {
		int rsInt = (int) Math.ceil(1 * 12 * (dataLen + 4) * 1000 / (float) baudrate) + appendMillsec;
		return rsInt;
	}

//	/**
//	 * @describe: 获取SerialBeanl类单例
//	 * @date:2009-11-5
//	 */
//	public static Level_Final_Serial getInstance() {
//		if (final_Level == null) {
//			portInit();
//		}
//		return final_Level;
//	}
//
//	public static void portInit() {
//		if (final_Level != null) {
//			final_Level.closePort();
//		}
//		final_Level = new Level_Final_Serial();
//	}

	/**
	 * @describe: 列举并得到需要用串口
	 * @date:2009-11-5
	 */
	private CommPortIdentifier getCommPort() throws Exception {
		CommPortIdentifier portIdRs = null;
		portIdRs = CommPortIdentifier.getPortIdentifier(PortName);
		return portIdRs;
		
//		Enumeration en = CommPortIdentifier.getPortIdentifiers();
//		boolean findFlag = false;
//
//		while (en.hasMoreElements()) {
//			portIdRs = (CommPortIdentifier) en.nextElement();
//			if (portIdRs.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//				if (( PortName.toLowerCase() ).equals( (portIdRs.getName()).toLowerCase() ) ) {
//					findFlag = true;
//					break;
//				}
//			}
//		}
//
//		portIdRs = findFlag ? portIdRs : null;
//		return portIdRs;
	}
	
	/**
	 * @describe: 列举全部串口名称	
	 * @date:2009-11-22
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

	/**
	 * @describe: 初始化串口详细信息
	 * @return true : 初始化串口成功 false: 初始化串口失败 
	 * @date:2009-11-5
	 */
	public boolean initialize(int timeOut, int baudrate, int dataBits, int stopBits, int parity) {
		// System.out.println("程序名:" + appname + "......");
		
		init(timeOut, baudrate, dataBits, stopBits, parity);
		
		boolean rsBool = false;
		try {
			//获取串口
			identifier = getCommPort();

			if (identifier == null) {
			} else {
				if (identifier.isCurrentlyOwned()){
					log.info(PortName+ ": 串口已经被" + identifier.getCurrentOwner()+ "占用...");
				}else{
					/* open方法打开通讯端口 */
					serialPort = (SerialPort) identifier.open(appname, timeOut);
					
					/* 注册一个SerialPortEventListener事件来监听串口事件 */
					// 设置串口事件
					// serialPort.notifyOnDataAvailable(false); //
					// 设置串口有数据的事件true有效,false无效
					// serialPort.notifyOnBreakInterrupt(true); //
					// 设置中断事件true有效,false无效
					// // 增加监听
					// serialPort.addEventListener(this);
					/* 获取端口的输入,输出流对象 */
					in = serialPort.getInputStream();
					out = serialPort.getOutputStream();
					
					/* 设置串口初始化参数，依次是波特率，数据位，停止位和校验 */
					serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
					serialPort.setDTR(true);
					serialPort.setRTS(true);
					
					rsBool = true;
					portIsOpenFlag = true;
				}//end if else内
			}//end if else外
		} catch (PortInUseException e) {
			log.info(PortName+ ": 串口已经被占用...");
		} catch (Exception e) {
			portIsOpenFlag = false;
			log.info(PortName+ ": 初始化串口详细信息:" + e.toString());
		}

		return rsBool;
	}

	/**
	 * @describe: 读取串口数据
	 * @date:2009-11-5
	 */
	public char[] readPackData() throws Exception {
		byte[] readBuffer = new byte[PACKET_LENGTH];
		char[] msgPack = null;
		int numBytes = 0;
		if (in == null){ return msgPack;}
		
		while (in.available() > 0) {
			numBytes = in.read(readBuffer);
			msgPack = new char[numBytes];
			for (int i = 0; i < numBytes; i++) {
				msgPack[i] = (char) (readBuffer[i] & 0xFF);
			}// end for
		}// end while
		return msgPack;
	}

	/**
	 * @describe: 向串口写数据 char[] bytes
	 * @date:2009-11-5
	 */
	public void writePort(char[] bytes) throws IOException {
		for (char b : bytes) {
			writePort(b);
		}
	}

	/**
	 * @describe: 向串口写数据 char bytes
	 * @date:2009-11-5
	 */
	public void writePort(char b) throws IOException {
		if (out == null){ return;}
		out.write(b);
		out.flush();
	}

	/**
	 * @throws IOException
	 * @describe: 关闭串口,释放资源
	 * @date:2009-11-5
	 */
	public void closePort() {
		if (out != null) {
			try {
				out.close();
				in.close();
				out = null;
				in = null;
			} catch (IOException e) {
				log.info("关闭串口时出错:" + e.getMessage() + e.toString());
			}
		}
		if (serialPort != null) {
			// serialPort.notifyOnDataAvailable(false);
			// serialPort.notifyOnBreakInterrupt(false);
			// serialPort.removeEventListener();
			serialPort.close();
			serialPort = null;
		}
		portIsOpenFlag = false;
	}

	/*
	 * 串口监听事件--暂时不用
	 * 
	 * @see
	 * javax.comm.SerialPortEventListener#serialEvent(javax.comm.SerialPortEvent
	 * )
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:/* Break interrupt,通讯中断 */
		case SerialPortEvent.OE:/* Overrun error，溢位错误 */
		case SerialPortEvent.FE:/* Framing error，传帧错误 */
		case SerialPortEvent.PE:/* Parity error，校验错误 */
		case SerialPortEvent.CD:/* Carrier detect，载波检测 */
		case SerialPortEvent.CTS:/* Clear to send，清除发送 */
		case SerialPortEvent.DSR:/* Data set ready，数据设备就绪 */
		case SerialPortEvent.RI:/* Ring indicator，响铃指示 */
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/* Output buffer isempty，输出缓冲区清空 */
		case SerialPortEvent.DATA_AVAILABLE:/*
											 * Data available at the
											 * serialport，端口有可用数据。读到缓冲数组，输出到终端
											 */
			// readComm(); // 这里可以读串口数据
			// System.out.println((new Date()).toLocaleString());

			// byte[] readBuffer = new byte[20];
			// int numBytes = 0;
			//
			// try {
			// while (in.available() > 0) {
			// numBytes = in.read(readBuffer);
			// for (int i = 0; i < numBytes; i++) {
			// buf.append(readBuffer[i]);
			// }
			// System.out.println("接收:" + numBytes + "个" + buf.toString());
			// }
			// } catch (IOException e) {
			// }

			break;
		}

	}

}
