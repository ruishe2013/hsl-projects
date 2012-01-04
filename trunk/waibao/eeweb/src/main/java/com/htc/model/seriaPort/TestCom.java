package com.htc.model.seriaPort;

import gnu.io.*;
/**
 * @ TestCom.java
 * ���� : ���ڲ�����
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class TestCom {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		
		// ��1��3�ŵ�ַ��������
//		int rs = 0;
//		for (int i = 1; i < 300; i++) {
//			rs = (i % 3) + 1;
//			runScript(1,9600,rs,2,50, 1, 1);
//		}
		
		// 2�ŵ�ַ��������100��
		//runScript(100,9600,2,38, 20, 3, 2);
		Level_Final_Serial endFinal = new Level_Final_Serial();
		System.out.println(endFinal.getFrameInterval(70, 20, 9600));

	}

	/**
	 * @describe: ���Դ�������״��.	
	 * @param runTimes ���д���
	 * @param baudRate ������
	 * @param address ���͵�ַ(һ����ַ)
	 * @param appendMillsec ���Ӻ�����
	 * @param bytes	���͵������ֽڳ���
	 * @param port	�˿ں�
	 * @param onOFF �Ƿ��ڿ���̨��ʾ��ʪ������(1������ 2���ر�)
	 * @date:2009-11-7
	 */
	@SuppressWarnings("static-access")
	public static void runScript(int runTimes, int baudRate, int address, int appendMillsec, int bytes, int port, int onOFF) throws Exception {
		// �õ�����ʵ��
		Level_Final_Serial final_Level = new Level_Final_Serial();
		final_Level.setPortName(port);//���ô��ں�
		//int runTimes = 256;

		try {

			// ��ʼ������
			final_Level.init(2000, baudRate, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			int frameInterval = final_Level.getFrameInterval(appendMillsec, bytes, baudRate);

			if (final_Level.initialize()) {
				System.out.print("success!");
				System.out.println("  ���:" + frameInterval + "����");
			} else {
				System.out.println("failed!");
				return;
			}

			// crcУ��
			char[] rs = {(char)address, 3, 0, 0, 0, 3, 0, 0};
			rs = CalcCRC.getCrc16(rs);

			int tag = 0;
			int tagC = 0;
			int tagE = 0;
			int tagW = 0;
			int tagL = 0;
			for (int ii = 0; ii < runTimes; ii++) {

				// ��������
				try {
					for (int i = 0; i < 1; i++) {
						final_Level.writePort(rs);
					}
					tagW = tagW + 1;
				} catch (Exception e) {
					e.printStackTrace();
				}

				// ���ݲ����ʣ����ݱ��ʺ����������Զ����÷��ͼ����
				try {
					Thread.sleep(frameInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// ��ȡ����
				char[] rsByte = final_Level.readPackData();
				if (rsByte == null) {
					tagC += 1;
					System.out.println("��ַ:" + address + "д��:" + tagW + "֡, ͨѶ����:" + tag + "֡, ͨѶ����:" + tagE + "֡, ��֡:" + tagC+"֡, �ֽڲ���֡:"+tagL);
					continue;
				}

				// �Խ��յ����ݽ���crcУ��
				if (rsByte.length != Level_Final_Serial.Serial_data_len){
					tagL = tagL + 1;
					// ��ʾ��ȡ������
					System.out.println("-----------start-----------");
					for (int i = 0; i < rsByte.length; i++) {
						System.out.print((byte) rsByte[i] + " ");
					}
					System.out.println();
					System.out.println(bytesToHexString(rsByte));
					System.out.println("-----------end-----------");
					System.out.println("��ַ:" + address + "д��:" + tagW + "֡, ͨѶ����:" + tag + "֡, ͨѶ����:" + tagE + "֡, ��֡:" + tagC+"֡, �ֽڲ���֡:"+tagL);
					continue;
				} 
				
				boolean rsOut = CalcCRC.checkCrc16(rsByte);

				if (onOFF == 1) {
					int humi = (rsByte[3] << 8) + rsByte[4];
					int temp = ((rsByte[5] << 8) + rsByte[6] - 27315);
					int dew = ((rsByte[7] << 8) + rsByte[8] - 27315);
					System.out.println("temp:" + temp + "  humi:" + humi + "  dew:" + dew);
				}

				if (rsOut) {
					tag = tag + 1;
				} else {
					tagE = tagE + 1;

					// ��ʾ��ȡ������
					for (int i = 0; i < rsByte.length; i++) {
						System.out.print((byte) rsByte[i] + " ");
					}
					System.out.println();
					System.out.println(bytesToHexString(rsByte));
					System.out.println("-----------end-----------");
					System.out.println("��ַ:" + address + "д��:" + tagW + "֡, ͨѶ����:" + tag + "֡, ͨѶ����:" + tagE + "֡, ��֡:" + tagC+"֡, �ֽڲ���֡:"+tagL);
				}

			}// end for ii
			System.out.println("��ַ:" + address + "�ܹ�д��:" + tagW + "֡, ͨѶ����:" + tag + "֡, ͨѶ����:" + tagE + "֡, ��֡:" + tagC+"֡, �ֽڲ���֡:"+tagL);
		} finally {
			// �رմ���
			final_Level.closePort();
			System.out.println("----���н���---!");
		}
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
