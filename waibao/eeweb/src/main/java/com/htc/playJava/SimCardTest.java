package com.htc.playJava;

import gnu.io.*;
import java.io.*;
import java.util.*;

/*
 * Created on 2005-4-18 10:36:39
 * @author zxub
 */
public class SimCardTest implements SerialPortEventListener {

	private Enumeration portList;
	private String portName;
	private CommPortIdentifier portId;// 串口管理器,负责打开,调用,和初始化串口等管理工作
	private SerialPort serialPort = null;
	private int getReplyInterval;
	private int commandDelay;
	private String replyString;
	private InputStream inputStream = null;
	private OutputStream outputStream = null;
	private int Baudrate;
	private String sendMode;
	private String message;
	private int msgCount = 0;
	public boolean errFlag = false;

	public SimCardTest() {
		getSysConfig();

	}

	public void getSysConfig() {
//		OperaXML ox = new OperaXML();
//		ox.read("config.xml");
//		this.portName = ox.getNodeValue("Config1/PortName");
//		this.Baudrate =
//			Integer.parseInt(ox.getNodeValue("Config1/BAUDRATE"));
//		this.sendMode = ox.getNodeValue("Config1/SendMode");
//		this.getReplyInterval = Integer.parseInt(ox
//				.getNodeValue("Config1/GetReplyInterval"));
//		this.commandDelay = Integer.parseInt(ox
//				.getNodeValue("Config1/CommandDelay"));
//		ox.close();
		 this.portName = "COM2";
		 this.Baudrate = 9600;
		 this.sendMode = "0";
		 this.getReplyInterval = 1000;
		 this.commandDelay = 1000;
	}

	// ********************************************************************
	// 列出所有串口
	public void listSerialPort() {
		// CommPortIdentifier类的getPortIdentifiers方法可以找到系统所有的串口，
		// 每个串口对应一个CommPortIdentifier类的实例。
		portList = null;
		portId = null;
		portList = CommPortIdentifier.getPortIdentifiers();
		returnStateInfo("串口列表：");
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			/* 如果端口类型是串口，则打印出其端口信息 */
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				returnStateInfo(portId.getName());
			}
		}
		returnStateInfo("串口列表显示结束！");
	}

	// ********************************************************************

	// ********************************************************************
	public void getSerialPort() {
		if (this.errFlag == true)
			return;
		returnStateInfo("检查连接情况...");
		if (this.portName == "") {
			returnStateInfo("串口号为空，请检查配置文件！");
			this.errFlag = true;
			return;
			// System.out.println("Portname is null, get err, the program now
			// exit!");
			// System.exit(0);
		}
		portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if ((portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
					&& portId.getName().equalsIgnoreCase(this.portName)) {
				try {
					this.serialPort = (SerialPort) portId.open("SendSms", 2000);
				} catch (PortInUseException e) {
					returnStateInfo("获取" + this.portName + "时出错！原因："
							+ e.getMessage());
					this.errFlag = true;
					return;
				}
			}
		}
	}

	public void listenSerialPort() {
		if (this.errFlag == true)
			return;
		if (this.serialPort == null) {
			returnStateInfo("不存在" + this.portName + "，请检查相关配置！");
			this.errFlag = true;
			return;
		}
		// 设置输入输出流
		try {
			outputStream = (OutputStream) this.serialPort.getOutputStream();
			inputStream = (InputStream) this.serialPort.getInputStream();
		} catch (IOException e) {
			returnStateInfo(e.getMessage());
		}
		try {
			// 监听端口
			this.serialPort.notifyOnDataAvailable(true);
			this.serialPort.notifyOnBreakInterrupt(true);
			this.serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			this.serialPort.close();
			returnStateInfo(e.getMessage());
		}
		try {
			this.serialPort.enableReceiveTimeout(20);
		} catch (UnsupportedCommOperationException e) {
		}
		// 设置端口的基本参数
		try {
			this.serialPort.setSerialPortParams(this.Baudrate,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
		}
	} // ********************************************************************

	// ********************************************************************
	// 对串口的读写操作
	public void writeToSerialPort(String msgString) {
		try {
			this.outputStream.write(msgString.getBytes());
			// CTRL+Z=(char)26
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private void waitForRead(int waitTime) {
		try {
			Thread.sleep(waitTime);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String readFromSerialPort(String messageString) {
		int strLength;
		String messageStr;
		String returnString;
		strLength = messageString.length();
		messageStr = messageString.substring(strLength - 4, strLength - 2);
		if (messageStr.equals("OK")) {
			returnString = messageStr;
		} else {
			returnString = messageString;
		}
		messageStr = messageString.substring(strLength - 7, strLength - 2);
		if (messageStr.equals("ERROR")) {
			returnString = messageStr;
		}
		return returnString;
	}

	// ********************************************************************

	// ********************************************************************
	// 操作结束，关闭所用资源
	public void closeSerialPort() {
		if (this.serialPort != null) {
			try {
				this.serialPort.close();
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
		returnStateInfo("已断开连接！");
	}

	public void closeIOStream() {
		if (this.inputStream != null) {
			try {
				this.inputStream.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		if (this.outputStream != null) {
			try {
				this.outputStream.close();
			} catch (IOException e1) {
				System.out.println(e1.getMessage());
			}
		}
		// returnStateInfo("已关闭I/O流！");
	}

	// ********************************************************************

	public void setToNull() {
		if (this.serialPort != null)
			this.serialPort.close();
		this.serialPort = null;
		this.inputStream = null;
		this.outputStream = null;
	}

	// ********************************************************************
	// 监听事件
	public void serialEvent(SerialPortEvent e) {
		StringBuffer inputBuffer = new StringBuffer();
		int newData = 0;
		switch (e.getEventType()) {
		case SerialPortEvent.DATA_AVAILABLE:// DATA_AVAILABLE - 有数据到达
			while (newData != -1) {
				try {
					newData = this.inputStream.read();
					if (newData == -1) {
						break;
					}
					if ('\r' == (char) newData) {
						inputBuffer.append('\n');
					} else {
						inputBuffer.append((char) newData);
					}
				} catch (IOException ex) {
					System.err.println(ex);
					return;
				}
			}
			this.message = this.message + new String(inputBuffer);
			break;
		case SerialPortEvent.BI:// BI - 通讯中断.
			System.out.println("\n--- BREAK RECEIVED ---\n");
		}
	}

	// *****************************************************************

	// *****************************************************************
	// 信息发送
	public void sendmsg(String messageString, String phoneNumber) {
		boolean sendSucc = false;
		getSerialPort();
		listenSerialPort();
		checkConn();
		if (this.errFlag == true)
			return;
		int msglength;
		String sendmessage, tempSendString;
		returnStateInfo("开始发送...");
		switch (Integer.parseInt(this.sendMode)) {
		case 0:// 按PDU方式发送
		{
			this.message = "";
			writeToSerialPort("AT+CMGF=0\r");
			waitForRead(this.commandDelay);
			msglength = messageString.length();
			if (msglength < 8) {
				tempSendString = "000801" + "0"
						+ Integer.toHexString(msglength * 2).toUpperCase()
						+ asc2unicode(new StringBuffer(messageString));
			} else {
				tempSendString = "000801"
						+ Integer.toHexString(msglength * 2).toUpperCase()
						+ asc2unicode(new StringBuffer(messageString));
			}
			// "000801"说明：分为00,08,01,
			// "00",普通GSM类型，点到点方式
			// "08",UCS2编码
			// "01",有效期
			if (phoneNumber.trim().length() > 0) {
				String[] infoReceiver = phoneNumber.split(",");
				int receiverCount = infoReceiver.length;
				if (receiverCount > 0) {
					for (int i = 0; i < receiverCount; i++) {
						sendmessage = "0011000D91" + "68"
								+ changePhoneNumber(infoReceiver[i])
								+ tempSendString;
						this.replyString = readFromSerialPort(this.message);
						if (!this.replyString.equals("ERROR")) {
							this.message = "";
							writeToSerialPort("AT+CMGS=" + (msglength * 2 + 15)
									+ "\r");
							waitForRead(this.commandDelay);
							writeToSerialPort(sendmessage);
							try {
								outputStream.write((char) 26);
							} catch (IOException ioe) {
							}
							getReply();
							if (this.replyString.equals("OK")) {
								returnStateInfo("成功发送到 " + infoReceiver[i]);
							}
							if (this.replyString.equals("ERROR")) {
								System.out.println("发送给 " + infoReceiver[i]
										+ " 时失败！");
							}
						}
					}
				}
			}
			break;
		}
		case 1:// 按文本方式发送，不能发送中文
		{
			this.message = "";
			writeToSerialPort("AT+CMGF=1\r");
			waitForRead(this.commandDelay);
			if (phoneNumber.trim().length() > 0) {
				String[] infoReceiver = phoneNumber.split(",");
				int receiverCount = infoReceiver.length;
				if (receiverCount > 0) {
					for (int i = 0; i < receiverCount; i++) {
						this.replyString = readFromSerialPort(message);
						if (!this.replyString.equals("ERROR")) {
							writeToSerialPort("AT+CMGS=" + infoReceiver[i]
									+ "\r");
							waitForRead(this.commandDelay);
							writeToSerialPort(messageString);
							try {
								outputStream.write((char) 26);
							} catch (IOException ioe) {
							}
							getReply();
							if (this.replyString.equals("OK")) {
								returnStateInfo("成功发送到 " + infoReceiver[i]);
							}
							if (this.replyString.equals("ERROR")) {
								System.out.println("发送给 " + infoReceiver[i]
										+ " 时失败！");
							}
						}
					}
				}
			}
			break;
		}
		default: {
			returnStateInfo("发送方式不对，请检查配置文件！");
			System.exit(0);
			break;
		}
		}
		closeIOStream();
		closeSerialPort();
		message = "";
		returnStateInfo("发送完毕！");
	}

	// *****************************************************************

	// *****************************************************************
	// 读取所有短信
	public void readAllMessage(int readType) {
		getSerialPort();
		listenSerialPort();
		checkConn();
		if (this.errFlag == true)
			return;
		returnStateInfo("开始获取信息，可能要些时间，请等待...");
		String tempAnalyseMessage = "";
		writeToSerialPort("AT+CMGF=0\r");
		waitForRead(this.commandDelay);
		this.message = "";
		writeToSerialPort("AT+CMGL=" + readType + "\r");
		waitForRead(this.commandDelay);
		try {
			getReply();
			StringTokenizer st = new StringTokenizer(this.message.substring(12,
					this.message.length() - 2), "+");
			int stCount = st.countTokens();
			if (stCount > 0) {
				while (st.hasMoreElements()) {
					String tempStr = st.nextToken();
					this.msgCount += 1;
					try {
						returnStateInfo(analyseMessage(tempStr.substring(15,
								tempStr.length()).trim()));
					} catch (Exception e) {
						returnStateInfo("没有符合条件的信息!");
					}

				}
			}

		} catch (Exception e) {
		}
		returnStateInfo("信息获取结束！");
		closeIOStream();
		closeSerialPort();
		this.message = "";
	}

	// *****************************************************************

	// *****************************************************************
	// 读取指定短信
	public void readMessage(int msgIndex) {
		getSerialPort();
		listenSerialPort();
		checkConn();
		if (this.errFlag == true)
			return;
		String[] tempAnalyseMessage = null;
		writeToSerialPort("AT+CMGF=0\r");
		waitForRead(this.commandDelay);
		this.message = "";
		if (msgIndex < 10) {
			writeToSerialPort("AT+CMGR=0" + msgIndex + "\r");
		} else {
			writeToSerialPort("AT+CMGR=" + msgIndex + "\r");
		}
		waitForRead(this.commandDelay);
		try {
			getReply();
			String tempStr = this.message.substring(12,
					this.message.length() - 2);
			try {
				returnStateInfo(analyseMessage(tempStr.substring(15,
						tempStr.length()).trim()));
			} catch (Exception e) {
				returnStateInfo("信息索引有误!");
			}
		} catch (Exception e) {
		}
		closeIOStream();
		closeSerialPort();
		this.message = "";
	}

	// *****************************************************************

	// *****************************************************************
	// 对短信息进行分析
	public String analyseMessage(String msgString) {
		int phoneNumberLength;
		int msgLength;
		String phoneNumber;
		String msgInfo;
		String msgTime;
		phoneNumberLength = Integer.parseInt(msgString.substring(20, 22), 16);
		if (phoneNumberLength % 2 != 0) {
			phoneNumberLength = phoneNumberLength + 1;
		}
		phoneNumber = changePhoneNumber(msgString.substring(24,
				24 + phoneNumberLength));
		phoneNumber = phoneNumber.replaceFirst("86", "").replaceFirst("F", "");
		msgTime = changePhoneNumber(msgString.substring(
				24 + phoneNumberLength + 4, 24 + phoneNumberLength + 5 + 11));
		msgTime = fixInfoTime(new StringBuffer(msgTime));
		// msgTime=msgTime.substring(0,msgTime.length()-3);
		msgLength = Integer.parseInt(msgString.substring(
				24 + phoneNumberLength + 5 + 13,
				24 + phoneNumberLength + 5 + 15), 16);
		msgInfo = msgString.substring((24 + phoneNumberLength + 5 + 15),
				(24 + phoneNumberLength + 5 + 15 + msgLength * 2));
		String analysedMessage = msgTime + " " + phoneNumber + "\n"
				+ unicode2asc(msgInfo) + "\n";
		return (analysedMessage);
	}

	// *****************************************************************

	// *****************************************************************
	// 对短信时间进行处理
	public String fixInfoTime(StringBuffer msgBuffer) {
		// msgBuffer.insert(12, "+");
		for (int i = 1; i < 3; i++) {
			msgBuffer.insert(12 - i * 2, ":");
		}
		msgBuffer.insert(6, " ");// 设置日期与时间之间的连字符号
		for (int i = 1; i < 3; i++) {
			msgBuffer.insert(6 - i * 2, "-");// 设置年、月、日之间的连字符号
		}
		return (new String(msgBuffer));
	}

	// *****************************************************************

	// *****************************************************************
	// 修正号码在内存中的表示，每2位为1组，每组2个数字交换，
	// 若号码个数为奇数，则在末尾补'F'凑成偶数，然后再进行变换，
	// 因为在计算机中，表示数字高低位顺序与我们的习惯相反.
	// 如："8613851872468" --> "683158812764F8"
	public String changePhoneNumber(String phoneNumber) {
		int numberLength = phoneNumber.length();
		if (phoneNumber.length() % 2 != 0) {
			phoneNumber = phoneNumber + "F";
			numberLength += 1;
		}
		char newPhoneNumber[] = new char[numberLength];
		for (int i = 0; i < numberLength; i += 2) {
			newPhoneNumber[i] = phoneNumber.charAt(i + 1);
			newPhoneNumber[i + 1] = phoneNumber.charAt(i);
		}
		return (new String(newPhoneNumber));
	}

	// *****************************************************************

	// *****************************************************************
	// 转换为UNICODE编码
	public String asc2unicode(StringBuffer msgString) {
		StringBuffer msgReturn = new StringBuffer();
		int msgLength = msgString.length();
		if (msgLength > 0) {
			for (int i = 0; i < msgLength; i++) {
				new Integer((int) msgString.charAt(0)).toString();
				msgReturn.append(new StringBuffer());
				String msgCheck = new String(Integer
						.toHexString((int) msgString.charAt(i)));
				if (msgCheck.length() < 4) {
					msgCheck = "00" + msgCheck;
				}
				msgReturn.append(new StringBuffer(msgCheck));
			}
		}
		return (new String(msgReturn).toUpperCase());
	}

	// *****************************************************************

	// *****************************************************************
	// UNICODE编码转换为正常文字
	public String unicode2asc(String msgString) {
		int msgLength = msgString.length();
		char msg[] = new char[msgLength / 4];
		for (int i = 0; i < msgLength / 4; i++) {
			// UNICODE编码转成十六进制数，再转换为正常文字
			msg[i] = (char) Integer.parseInt((msgString.substring(i * 4,
					4 * i + 4)), 16);
		}
		return (new String(msg));
	}

	// *****************************************************************

	// *****************************************************************
	// 不断读取返回信号，当收到OK信号时，停止读取，以执行下面的操作
	public void getReply() {
		this.replyString = readFromSerialPort(this.message);
		while (this.replyString != null) {
			if (this.replyString.equals("OK")
					|| this.replyString.equals("ERROR"))
				return;
			waitForRead(this.getReplyInterval);
			this.replyString = readFromSerialPort(this.message);
		}
	}

	// *****************************************************************

	// *****************************************************************
	// 检查GSM Modem或卡有无连接错误
	public void checkConn() {
		if (this.errFlag == true)
			return;
		this.message = "";
		writeToSerialPort("AT+CSCA?\r");
		waitForRead(this.commandDelay);
		getReply();
		if (this.replyString.equals("ERROR")) {
			returnStateInfo("Modem 或手机卡连接有误，请检查！");
			this.errFlag = true;
			closeIOStream();
			closeSerialPort();
			return;
		}
		returnStateInfo("连接正常！");
	}

	// *****************************************************************

	// *****************************************************************
	// 删除短信
	public void delMessage(int msgIndex) {
		this.message = "";
		getSerialPort();
		listenSerialPort();
		checkConn();
		writeToSerialPort("AT+CMGF=0\r");
		waitForRead(this.commandDelay);
		getReply();
		if (this.replyString.equals("OK")) {
			this.message = "";
			try {
				writeToSerialPort("AT+CMGD=" + msgIndex + "\r");
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
		closeIOStream();
		closeSerialPort();
	}

	// *****************************************************************

	private void returnStateInfo(String errInfo) {
		System.out.println(errInfo);
	}

	public static void main(String[] args) {

		SimCardTest ccon = new SimCardTest();
		 ccon.sendmsg("测试!", "13738195048");
		 ccon.readMessage(3);
		 ccon.delMessage(39);
		 ccon.readAllMessage(4);
		
        Queue<String> queue = new LinkedList<String>();   
        queue.offer("Hello");   
        queue.offer("World!");   
        queue.offer("你好！");   
        System.out.println(queue.size());   
        String str;   
        while((str=queue.poll())!=null){   
            System.out.print(str);   
        }   
        System.out.println();   
        System.out.println(queue.size());

	}
}
