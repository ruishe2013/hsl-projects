package com.htc.ports;

public class SerialServiceValues implements PortServiceValues {

	@Override
	public int getSerial_right_Frame() {
		return Serial_right_Frame;
	}
	
	public int getSerial_Lost_Frame() {
		return Serial_Lost_Frame;
	}
	
	public int getSerial_wrong_Frame() {
		return Serial_wrong_Frame;
	}
	
	
	public int getSerial_wrong_Address() {
		return Serial_wrong_Address;
	}
	
	public int getSerial_wrong_Date() {
		return Serial_wrong_Date;
	}
	
	public int getSerial_data_len() {
		return Serial_data_len;
	}
	
	/**
	 * ��ȷ����
	 */
	private static final int Serial_right_Frame = 1;
	
	/**
	 * ��ʧ����
	 */
	private static final int Serial_Lost_Frame = 2;
	/**
	 * �������
	 */
	private static final int Serial_wrong_Frame = 3;
	
	/**
	 * ��ַ���Ե���
	 */
	private static final int Serial_wrong_Address = 4;
	/**
	 * ���ݴ������
	 */
	private static final int Serial_wrong_Date = 5;
	/**
	 * ���Ȳ��Ե���
	 */
	private static final int Serial_data_len = 11;

}
