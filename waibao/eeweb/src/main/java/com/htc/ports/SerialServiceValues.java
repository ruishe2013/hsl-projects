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
	 * 正确的侦
	 */
	private static final int Serial_right_Frame = 1;
	
	/**
	 * 丢失的侦
	 */
	private static final int Serial_Lost_Frame = 2;
	/**
	 * 错误的侦
	 */
	private static final int Serial_wrong_Frame = 3;
	
	/**
	 * 地址不对的侦
	 */
	private static final int Serial_wrong_Address = 4;
	/**
	 * 数据错误的侦
	 */
	private static final int Serial_wrong_Date = 5;
	/**
	 * 长度不对的侦
	 */
	private static final int Serial_data_len = 11;

}
