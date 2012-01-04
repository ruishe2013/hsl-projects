package com.htc.model.seriaPort;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * �����ļ����λ��.<br>
 * rxtxSerial.dll ����%java_home%\bin��<br>
 * RXTXcomm.jar   ����%java_home%\lib��<br>
 * ��ʹ�÷���: <br>
 * getInstance->setAppname,init->initialize->isPortOpen->readPackData->writePort->ClosePort<br>
 * @ SerialBean.java ���� : ���ڲ��� -- ���һ��: ����:��ʼ������->��д���� ע������ : �� VERSION DATE BY
 * CHANGE/COMMENT 1.0 2009-11-4 YANGZHONLI create
 */
public class Level_Final_Serial implements SerialPortEventListener {
	private Log log = LogFactory.getLog(Level_Final_Serial.class);

	/**
	 * ��ȷ����
	 */
	public static final int Serial_right_Frame = 1;
	/**
	 * ��ʧ����
	 */
	public static final int Serial_Lost_Frame = 2;
	/**
	 * �������
	 */
	public static final int Serial_wrong_Frame = 3;
	/**
	 * ��ַ���Ե���
	 */
	public static final int Serial_wrong_Address = 4;
	/**
	 * ���ݴ������
	 */
	public static final int Serial_wrong_Date = 5;
	/**
	 * ���Ȳ��Ե���
	 */
	public static final int Serial_data_len = 11;
	/**
	 * ���ݰ�����
	 */
	public static final int PACKET_LENGTH = 500;

	private SerialPort serialPort;
	private CommPortIdentifier identifier;
	
	//public static Level_Final_Serial final_Level;
	String PortName;					
	OutputStream out;
	InputStream in;

	String appname = "HtcSerialBean"; // ���ڳ�����
	int timeOut; // �ӳ�ʱ��(������)
	int baudrate; // ������
	int dataBits; // ����λ
	int stopBits; // ֹͣλ
	int parity; // ��ż����

	boolean portIsOpenFlag; // �����Ƿ�򿪱�ʶ (��Ҫ)

	// ������,�ȴ�ʱ��,������,����λ,ֹͣλ,��ż����
	// timeOut:("SerialBean",3000, 9600,
	// SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)
	/**
	 * @describe: ��ʼ����
	 * @param timeOut �ȴ�ʱ��
	 * @param baudrate������
	 * @param dataBits ����λ
	 * @param stopBits ֹͣλ
	 * @param parity ��ż����
	 * @date:2009-11-5
	 */
	public void init(int timeOut, int baudrate, int dataBits, int stopBits, int parity) {
		this.timeOut = timeOut;
		this.baudrate = baudrate;
		this.dataBits = dataBits;
		this.stopBits = stopBits;
		this.parity = parity;
	}

	/**
	 * @describe : ��ȡ�����ַ���
	 * @date:2009-11-5
	 */
	public String getPortName() {
		return PortName;
	}

	/**
	 * @describe: ���� ���ڳ�����
	 * @date:2010-3-2
	 */
	public void setAppname(String appname) {
		this.appname = appname;
	}

	/**
	 * @describe: ���ô����ַ���
	 * @date:2009-11-5
	 */
	public void setPortName(int portName) {
		this.PortName = "COM" + portName;
	}
	public void setPortName(String portName) {
		this.PortName = portName;
	}

	/**
	 * ��ʼ������,Ҫ��com�ں�
	 */
	public Level_Final_Serial() {
		portIsOpenFlag = false;
	}

	/**
	 * @describe: ���ش����Ƿ��
	 * @return true : �� false: û�д�
	 * @date:2009-11-5 
	 */
	public boolean isPortOpen() {
		return portIsOpenFlag;
	}

	/**
	 * @describe: ��ȡ��Ҫ֮֡����Ҫ�����ʱ��(����) ���ܹ�ʽ(1*12(λ)*���ݳ���*1000/������ + ���Ӻ�����)
	 * @param appendMillsec ���Ӻ�����
	 * @param dataLen ���������ݳ���
	 * @param baudrate ������
	 * @return �õ����ʵ�֡����,���������
	 * @date:2009-11-5
	 */
	public static int getFrameInterval(int appendMillsec, int dataLen, int baudrate) {
		int rsInt = (int) Math.ceil(1 * 12 * (dataLen + 4) * 1000 / (float) baudrate) + appendMillsec;
		return rsInt;
	}

//	/**
//	 * @describe: ��ȡSerialBeanl�൥��
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
	 * @describe: �оٲ��õ���Ҫ�ô���
	 * @date:2009-11-5
	 */
	public CommPortIdentifier getCommPort() throws Exception {
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
	 * @describe: �о�ȫ����������	
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
	 * @describe: ��ʼ��������ϸ��Ϣ
	 * @return true : ��ʼ�����ڳɹ� false: ��ʼ������ʧ�� 
	 * @date:2009-11-5
	 */
	public boolean initialize() {
		// System.out.println("������:" + appname + "......");
		boolean rsBool = false;
		try {
			//��ȡ����
			identifier = getCommPort();

			if (identifier == null) {
			} else {
				if (identifier.isCurrentlyOwned()){
					log.info(PortName+ ": �����Ѿ���" + identifier.getCurrentOwner()+ "ռ��...");
				}else{
					/* open������ͨѶ�˿� */
					serialPort = (SerialPort) identifier.open(appname, timeOut);
					
					/* ע��һ��SerialPortEventListener�¼������������¼� */
					// ���ô����¼�
					// serialPort.notifyOnDataAvailable(false); //
					// ���ô��������ݵ��¼�true��Ч,false��Ч
					// serialPort.notifyOnBreakInterrupt(true); //
					// �����ж��¼�true��Ч,false��Ч
					// // ���Ӽ���
					// serialPort.addEventListener(this);
					/* ��ȡ�˿ڵ�����,��������� */
					in = serialPort.getInputStream();
					out = serialPort.getOutputStream();
					
					/* ���ô��ڳ�ʼ�������������ǲ����ʣ�����λ��ֹͣλ��У�� */
					serialPort.setSerialPortParams(baudrate, dataBits, stopBits, parity);
					serialPort.setDTR(true);
					serialPort.setRTS(true);
					
					rsBool = true;
					portIsOpenFlag = true;
				}//end if else��
			}//end if else��
		} catch (PortInUseException e) {
			log.info(PortName+ ": �����Ѿ���ռ��...");
		} catch (Exception e) {
			portIsOpenFlag = false;
			log.info(PortName+ ": ��ʼ��������ϸ��Ϣ:" + e.toString());
		}

		return rsBool;
	}

	/**
	 * @describe: ��ȡ��������
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
	 * @describe: �򴮿�д���� char[] bytes
	 * @date:2009-11-5
	 */
	public void writePort(char[] bytes) throws IOException {
		for (char b : bytes) {
			writePort(b);
		}
	}

	/**
	 * @describe: �򴮿�д���� char bytes
	 * @date:2009-11-5
	 */
	public void writePort(char b) throws IOException {
		if (out == null){ return;}
		out.write(b);
		out.flush();
	}

	/**
	 * @throws IOException
	 * @describe: �رմ���,�ͷ���Դ
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
				log.info("�رմ���ʱ����:" + e.getMessage() + e.toString());
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
	 * ���ڼ����¼�--��ʱ����
	 * 
	 * @see
	 * javax.comm.SerialPortEventListener#serialEvent(javax.comm.SerialPortEvent
	 * )
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {
		case SerialPortEvent.BI:/* Break interrupt,ͨѶ�ж� */
		case SerialPortEvent.OE:/* Overrun error����λ���� */
		case SerialPortEvent.FE:/* Framing error����֡���� */
		case SerialPortEvent.PE:/* Parity error��У����� */
		case SerialPortEvent.CD:/* Carrier detect���ز���� */
		case SerialPortEvent.CTS:/* Clear to send��������� */
		case SerialPortEvent.DSR:/* Data set ready�������豸���� */
		case SerialPortEvent.RI:/* Ring indicator������ָʾ */
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/* Output buffer isempty�������������� */
		case SerialPortEvent.DATA_AVAILABLE:/*
											 * Data available at the
											 * serialport���˿��п������ݡ������������飬������ն�
											 */
			// readComm(); // ������Զ���������
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
			// System.out.println("����:" + numBytes + "��" + buf.toString());
			// }
			// } catch (IOException e) {
			// }

			break;
		}

	}

}
