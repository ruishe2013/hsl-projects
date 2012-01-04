package com.htc.model.seriaPort;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gnu.io.*;

/**
 * �����ļ����λ��.<br>
 * rxtxSerial.dll ����%java_home%\bin��<br>
 * RXTXcomm.jar   ����%java_home%\lib��<br>
 * 
 * ���� : ����ģ���õ��Ĵ���ͨ����<br>
 * ��ʹ�ý���:<br>
 * ��ʹ�÷���: <br>
 * 		1:init(���ں�,������,��������);��ʼ��<br>
 * 		2:setParam(��ȡ�������ݼ��,ͨ��û����ɵ�ʱ��,���Դ���);�������ӵ�һЩ����<br>
 * 		3:openPort();�򿪴���<br>
 * 		4:closePort();�رմ���<br>
 * 		5:writeByte(����ָ��);д�������� - ����AT���ָ��<br>
 * 		6:getReceiveMsg();��ȡ���ջ����<br>
 */

//public class SimCard_Serial implements SerialPortEventListener  {
public class SimCard_Serial {
	
	private Log log = LogFactory.getLog(SimCard_Serial.class);
	/**
	 * ��ʱ�洢���ݵĻ��峤��
	 */
	public static final int PACKET_LENGTH = 500;
	
	private SerialPort serialPort;					
	private CommPortIdentifier identifier;
	private OutputStream out;
	private InputStream in;
	private StringBuffer buffer4SimCard = new StringBuffer();		// �����������
	
	//��Ҫ���õĲ���
	String portName; 					// ���ںţ��磺com1
	int baudrate;						// ������
	String appname = "sms_port"; 		// ���ڳ�����
	
	//	��һ��Ҫ�趨�Ĳ���(��Ĭ��ֵ)
	int timeOut; 						// �ӳ�ʱ��(������)
	int dataBits; 						// ����λ
	int stopBits; 						// ֹͣλ
	int parity; 						// ��ż����
	
	boolean portIsOpenFlag = false;		// �����Ƿ�򿪱�ʶ (��Ҫ)
	
	/****************************************������������****************************************/
	// ���췽��
	public SimCard_Serial() {
		// �ӳ�ʱ�� 	������		����λ					 ֹͣλ				��ż����				���ں�	���ڳ�����
		init(60, 	9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, "COM1", "sms_port");
	}

	/**
	 * @describe: ��ʼ����
	 * @param timeOut �ȴ�ʱ��(������)
	 * @param baudrate������
	 * @param dataBits ����λ
	 * @param stopBits ֹͣλ
	 * @param parity ��ż����
	 * @param portName ���ںţ��磺com1
	 * @param appname ���ڳ�����
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
	 * ��ʼ�� �മ�ں� �� ������
	 * @param portName ���ںţ��磺com1
	 * @param baudrate ������
	 * @param appname ���ڳ�����
	 */
	public void init(String portName, int baudrate, String appname) {
		this.portName = portName.toUpperCase();
		this.baudrate = baudrate;
		this.appname = appname;
	}	
	/****************************************���ڲ���:��,�رմ���,��������****************************************/	
	/**
	 * @describe: �򿪴���
	 * @param portStr ���ں�. ��: COM3
	 * @param baudrate ������
	 * @param appName ����ռ�ó��������
	 * @return: true:�򿪴������� false:�򿪴����쳣
	 */
	public boolean openPort() {
		boolean rsBool = true;
		try {
			identifier = CommPortIdentifier.getPortIdentifier(portName);		//��ȡ���ڶ���
			if (identifier != null) {
				serialPort = (SerialPort) identifier.open(appname, timeOut);	// ��ͨѶ�˿�
				
				//	ʹ�ü���ģʽ��������,��������,������
//				serialPort.notifyOnDataAvailable(true);							// ���ô��������ݵ��¼�true��Ч,false��Ч
//				serialPort.notifyOnBreakInterrupt(true); 						// �����ж��¼�true��Ч,false��Ч
//				/* ע��һ��SerialPortEventListener�¼������������¼� */
//				serialPort.addEventListener(this); 								// �����¼�����
				
//				/* ��ȡ�˿ڵ�����,��������� */
				in = serialPort.getInputStream();
				out = serialPort.getOutputStream();
				/* ���ô��ڳ�ʼ�������������ǲ����ʣ�����λ��ֹͣλ��У�� */
				serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
				serialPort.setDTR(true);
				serialPort.setRTS(true);
			}
		} catch (PortInUseException e) {
			rsBool = false;
			log.info("SimCard��Ϣ:" + portName+ ": �����Ѿ���ռ��...");
		} catch (Exception e) {
			rsBool = false;
			log.info("SimCard��Ϣ:" + portName+ ": ��ʼ��������ϸ��Ϣ:" + e.toString());
		}
		return rsBool;
	}	
	
	/**
	 * �رմ��ڣ��ͷ���Դ
	 * @return: true: �رճɹ�   false:�ر�ʧ��
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
				log.info("�رմ���ʱ����:" + e.getMessage() + e.toString());
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
	 * @describe: д�������� - ����AT���ָ��
	 * @param sendmsg ���͵�����
	 */
	public void writeByte(String sendmsg) throws Exception{
		if (out == null){ return;}
		out.write(sendmsg.getBytes());
		out.flush();		
	}	
	
	/**
	 * @describe: ��ȡ��������
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
	
	/****************************************�¼����� ��ȡ���������****************************************/	
	// ���ڼ����¼�--ʹ�ü���ģʽ��������,��������,������
	public void serialEvent(SerialPortEvent event) {
		int newData = 0;
		switch (event.getEventType()) {
		case SerialPortEvent.DATA_AVAILABLE:// DATA_AVAILABLE - �����ݵ���
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
		case SerialPortEvent.BI:// BI - ͨѶ�ж�.
			System.out.println("\n--- BREAK RECEIVED ---\n");
			break;
		}
	}		
	
	/**
	 * ��ȡ���Ż��������
	 */
	public String getReceiveMsg(){
		readPackData();
		return buffer4SimCard.toString();
	}
	
	/**
	 * ������Ż��������
	 */
	public void clearReceiveMsg(){
		clearReceiveMsg(buffer4SimCard.length());
	}
	/**
	 * ������Ż��������,ɾ���� �������һ��λ��ǰ�������
	 */
	public void clearReceiveMsg(int endIndex){
		buffer4SimCard.delete(0, endIndex);
	}
	
	/**
	 * ��û�д������Ϣ�Żػ������
	 */
	public void backToPool(String undealStr){
		buffer4SimCard.append(undealStr);
	}
	
	/****************************************��������****************************************/
	/**
	 * �����Ƿ�򿪱�ʶ
	 */
	public boolean isPortIsOpenFlag() {
		return portIsOpenFlag;
	}
	
	/**
	 * �����Ƿ�򿪱�ʶ
	 */
	public void setPortIsOpenFlag(boolean portIsOpenFlag) {
		this.portIsOpenFlag = portIsOpenFlag;
	}
	/****************************************���߷���****************************************/		
	/**
	 * @describe: �о�ȫ����������	
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

