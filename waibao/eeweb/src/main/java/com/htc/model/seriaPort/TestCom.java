package com.htc.model.seriaPort;

import gnu.io.*;
/**
 * @ TestCom.java
 * 作用 : 串口测试用
 * 注意事项 : 无
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class TestCom {

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		
		// 从1到3号地址轮流发送
//		int rs = 0;
//		for (int i = 1; i < 300; i++) {
//			rs = (i % 3) + 1;
//			runScript(1,9600,rs,2,50, 1, 1);
//		}
		
		// 2号地址连续发送100次
		//runScript(100,9600,2,38, 20, 3, 2);
		Level_Final_Serial endFinal = new Level_Final_Serial();
		System.out.println(endFinal.getFrameInterval(70, 20, 9600));

	}

	/**
	 * @describe: 测试串口运行状况.	
	 * @param runTimes 运行次数
	 * @param baudRate 波特率
	 * @param address 发送地址(一个地址)
	 * @param appendMillsec 附加毫秒数
	 * @param bytes	发送的数据字节长度
	 * @param port	端口号
	 * @param onOFF 是否在控制台显示温湿度数据(1：开启 2：关闭)
	 * @date:2009-11-7
	 */
	@SuppressWarnings("static-access")
	public static void runScript(int runTimes, int baudRate, int address, int appendMillsec, int bytes, int port, int onOFF) throws Exception {
		// 得到串口实例
		Level_Final_Serial final_Level = new Level_Final_Serial();
		final_Level.setPortName(port);//设置串口号
		//int runTimes = 256;

		try {

			// 初始化串口
			int frameInterval = final_Level.getFrameInterval(appendMillsec, bytes, baudRate);

			if (final_Level.initialize(2000, baudRate, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE)) {
				System.out.print("success!");
				System.out.println("  间隔:" + frameInterval + "毫秒");
			} else {
				System.out.println("failed!");
				return;
			}

			// crc校验
			char[] rs = {(char)address, 3, 0, 0, 0, 3, 0, 0};
			rs = CalcCRC.getCrc16(rs);

			int tag = 0;
			int tagC = 0;
			int tagE = 0;
			int tagW = 0;
			int tagL = 0;
			for (int ii = 0; ii < runTimes; ii++) {

				// 发送数据
				try {
					for (int i = 0; i < 1; i++) {
						final_Level.writePort(rs);
					}
					tagW = tagW + 1;
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 根据波特率，数据倍率和数据量，自动设置发送间隔，
				try {
					Thread.sleep(frameInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// 读取数据
				char[] rsByte = final_Level.readPackData();
				if (rsByte == null) {
					tagC += 1;
					System.out.println("地址:" + address + "写入:" + tagW + "帧, 通讯正常:" + tag + "帧, 通讯故障:" + tagE + "帧, 丢帧:" + tagC+"帧, 字节不足帧:"+tagL);
					continue;
				}

				// 对接收的数据进行crc校验
				if (rsByte.length != Level_Final_Serial.Serial_data_len){
					tagL = tagL + 1;
					// 显示读取的数据
					System.out.println("-----------start-----------");
					for (int i = 0; i < rsByte.length; i++) {
						System.out.print((byte) rsByte[i] + " ");
					}
					System.out.println();
					System.out.println(bytesToHexString(rsByte));
					System.out.println("-----------end-----------");
					System.out.println("地址:" + address + "写入:" + tagW + "帧, 通讯正常:" + tag + "帧, 通讯故障:" + tagE + "帧, 丢帧:" + tagC+"帧, 字节不足帧:"+tagL);
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

					// 显示读取的数据
					for (int i = 0; i < rsByte.length; i++) {
						System.out.print((byte) rsByte[i] + " ");
					}
					System.out.println();
					System.out.println(bytesToHexString(rsByte));
					System.out.println("-----------end-----------");
					System.out.println("地址:" + address + "写入:" + tagW + "帧, 通讯正常:" + tag + "帧, 通讯故障:" + tagE + "帧, 丢帧:" + tagC+"帧, 字节不足帧:"+tagL);
				}

			}// end for ii
			System.out.println("地址:" + address + "总共写入:" + tagW + "帧, 通讯正常:" + tag + "帧, 通讯故障:" + tagE + "帧, 丢帧:" + tagC+"帧, 字节不足帧:"+tagL);
		} finally {
			// 关闭串口
			final_Level.closePort();
			System.out.println("----运行结束---!");
		}
	}

	/**
	 * @describe:	把char类型转换成16进制字符串
	 * @param bArray  char类型数组
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
