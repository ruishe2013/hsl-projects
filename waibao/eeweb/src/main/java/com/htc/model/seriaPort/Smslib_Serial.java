package com.htc.model.seriaPort;

import gnu.io.*;

/**
 * ������--���ڲ���
 * 
 * ���� : ���ڲ���:��װ�ײ�
 * ��ʹ�ý���:
 * 		��ȡʵ����getInstance
 * 		�򿪴��ڣ�openPort
 * 		��д���ݣ�writeByte->readByte
 * 		�رմ��ڣ�closePort
 * ע������ : ��
 */
public class Smslib_Serial {
	
	private Level_Final_Serial final_Serial;
	
	//��Ҫ���õĲ���
	int baudrate;					// ������
	
	//	��һ��Ҫ�趨�Ĳ���(��Ĭ��ֵ)
	int timeOut; 					// �ӳ�ʱ��(������)
	int dataBits; 					// ����λ
	int stopBits; 					// ֹͣλ
	int parity; 					// ��ż����
	int funCode; 					// ������	
	int dataLen;					// ���ݳ���
	int appendMillsec; 				// ���㷢�ͼ����---���Ӻ�����
	int bytes;						// ���㷢�ͼ����---�����ֽ���	

	// �Զ�����--���ͼ��
	int frameInterval; 				// ���ݲ����ʣ����ݱ��ʺ����������Զ����÷��ͼ��
	
	// ���췽��
	public Smslib_Serial() {
		final_Serial = new Level_Final_Serial();
		timeOut = 60;						// �ӳ�ʱ��(������)
		dataBits = SerialPort.DATABITS_8;	// ����λ
		stopBits = SerialPort.STOPBITS_1;	// ֹͣλ
		parity = SerialPort.PARITY_NONE;	// ��ż����
		funCode = 3;						//	��ȡ��ǰ�Ĵ�����һ������������ֵ
		dataLen = 4;						//	���� ��Ҫ��ȡ4������
		appendMillsec = 38;					//	���Ӻ�����(��Ҫ�Լ����Ե���)
		bytes = 20;							//  �������ֽ���
	}

	/**
	 * @describe: �򿪴���
	 * @param portStr ���ں�. ��: COM3
	 * @param baudrate ������
	 * @param appName ����ռ�ó��������
	 * @return: true:�򿪴������� false:�򿪴����쳣
	 */
	public boolean openPort(String portStr, int baudrate, String appName) {
		boolean rsBool = false;
		
		// ��ʼ������
		// final_Serial.init(timeOut, baudrate, dataBits, stopBits, parity);
		final_Serial.setAppname(appName.toUpperCase());
		final_Serial.setPortName(portStr);
		// �򿪴���
		if (final_Serial.initialize(timeOut, baudrate, dataBits, stopBits, parity)) {
			rsBool = true;
			// ����֮֡��ķ��ͼ��
			this.frameInterval = getFrameInterval(appendMillsec, bytes, baudrate);		
		}
		return rsBool;
	}	
	
	
	/**
	 * @describe: д�������� - ����AT���ָ��
	 * @param rs ���͵�����
	 */
	public void writeByte(char[] rs) throws Exception{
		final_Serial.writePort(rs);
		// ��ӡ���͵Ĵ�������-16������ʾ
		// System.out.println(bytesToHexString(rs));
		
		//�ȴ�һ��ʱ��, �Ա�֤����,���㹻��ʱ�䷢�ͺͽ���
		Thread.sleep(frameInterval);			
	}	
	
	/**
	 * @describe: .���������� - �Է���AT���ָ��,����OK���ǳɹ�
	 * @return: true:�ɹ� false:ʧ�� 
	 */
	public boolean readByte() throws Exception{
		boolean rsbool = false;
		String rsStr = "";
		
		// ��ȡ����
		char[] rsByte = final_Serial.readPackData();
		if (rsByte != null){
			// ��ӡ�յ��Ĵ�������-16������ʾ
			for (char c : rsByte) {
				rsStr += c; 
			}
			if (rsStr.indexOf("OK")>0){
				rsbool = true;
			}
		}		
		
		return rsbool;
	}
	
	/**
	 * @describe: �رմ��ڣ��ͷ���Դ
	 * @date:2009-11-5
	 */
	public void closePort() {
		final_Serial.closePort();
	}	
	
	//---------------���߷���---------------//
	/**
	 * @describe: ��ȡ��Ҫ֮֡����Ҫ�����ʱ��(����) ���ܹ�ʽ(1*12(λ)*���ݳ���*1000/������ + ���Ӻ�����)--�����Լ��ĳ���̬����
	 * @param appendMillsec	���Ӻ�����
	 * @param dataLen	���������ݳ���
	 * @param baudrate	������
	 * @return �õ����ʵ�֡����,���������
	 * @date:2009-11-5
	 */
	public static int getFrameInterval(int appendMillsec, int dataLen, int baudrate) {
		int rsInt = (int) Math.ceil(1 * 12 * (dataLen + 4) * 1000 / (float) baudrate) + appendMillsec;
		return rsInt;
	}
	
	/**
	 * @describe:	��char����ת����16�����ַ���
	 * @param bArray  char��������
	 * @date:2009-11-7
	 */
	public static final String bytesToHexString(char[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase() + " ");
		}
		return sb.toString();
	}
	
}

