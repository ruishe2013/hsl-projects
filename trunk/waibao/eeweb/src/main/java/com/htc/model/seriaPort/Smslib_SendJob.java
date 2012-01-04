package com.htc.model.seriaPort;

import java.util.*;

/**
 * 	废弃类--现在不用 
 * 
 * 作用 : smslib 发送短信,自动匹配串口 <br>
 * 类使用介绍:	获取实例:<code>getInstance</code>
 * 				启动服务:<code>startService</code>已经启动短信接收事件<br>
 *				结束服务:<code>stopService</code><br>
 *				打印网络信息:<code>smsInfo</code><br>
 *				
 *				发送短信: <code>sendMessage</code><br>
 * 注意事项 : 无
 */
public class Smslib_SendJob{
	
	private static Smslib_SendJob smsSendJob = new Smslib_SendJob(); //本类单例
	/**
	 * 用于动态测试短信串口号
	 */
	private Smslib_Serial smslib_test = new Smslib_Serial();
	/**
	 * 发送短信的服务
	 */
	private Smslib_Service smsService = new Smslib_Service();
	/**
	 * 短信发送模块专用的端口名
	 */
	private static String SMSAPPNAME = "sms_port";
	/**
	 * 波特率
	 */
	private int baudrate = 9600;
	/**
	 * 串口字符(如:COM1)
	 */
	private String comStr = "com1";	
	/**
	 * 短信模块运行标志
	 */
	private boolean sms_run_flag = false;
	
	/*---------------------------测试合适的串口号-------------------------------*/
	/**
	 * @describe: 获取SerialBeanl类单例
	 * @date:2009-11-5
	 */
	public static Smslib_SendJob getInstance() {
		return smsSendJob;
	}	
	
	/**
	 * @describe: 设置波特率	和 串口字符
	 * @param baudrate: 波特率
	 * @param comStr: 串口字符
	 * @date:2010-3-2
	 */
	private void initial(int baudrate, String comStr) {
		this.baudrate = baudrate;
		this.comStr = comStr;
	}

	/**
	 * @describe: 动态检测适合短信模块的-串口字符(如:COM1)
	 * @param passPort: 不用检测的串口号
	 * @date:2009-11-22
	 */
	private String getRightComStr(String passPort){
		String rsCom = null;
		
		//获取final_Serial实例--扫描端口数量,并逐个测试
		List<String> portList = Level_Final_Serial.getAllComPorts();
		if (portList.size() <= 0){
			// 没有发现任何串口
		}else{
			// 逐个扫描测试连通性
			for (String portStr : portList) {
				// 碰到不需要检测串口,则跳过
				if (passPort.toUpperCase().equals(portStr.toUpperCase())){continue;}
				// 测试串口的是否适合短信模块
				if (testSms(portStr)){
					rsCom = portStr;
					break;
				}
			}
		}
		return rsCom;
	}
	
	/**
	 * @describe: 测试串口的是否适合短信模块
	 * @param portStr: 串口号. 如:COM3
	 * @return: null:失败 其他:成功 
	 */
	private boolean testSms(String portStr){
		boolean rsBool = false;
		try {
			//System.out.println("测试串口" + portStr);
			// ①  打开端口
			rsBool = smslib_test.openPort(portStr, baudrate, SMSAPPNAME);
			if (rsBool){
				// ②  写串口
				String atCommand = "AT\r";		// 发送AT指令(加换行符号\r) 
				char[] atOrder = atCommand.toCharArray();			
				smslib_test.writeByte(atOrder);
				
				// ③  读串口(根据得到的数据,判断返回数据的连通性{返回字符包含OK表示成功})
				rsBool = smslib_test.readByte();
				if (rsBool){
					System.out.println("找到" + portStr + ":短信模块串口");
				}
				// ④ 关闭串口
				smslib_test.closePort();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsBool;
	}
	
	/**
	 * @describe: 为发送和接收短信做好准备
	 * @return: true:成功 false:失败
	 * @date:2010-3-2
	 */
	private boolean readyToSendMsg(){
		boolean rsbool = false;
		rsbool = smsService.initial(comStr, baudrate, "0000");
		if (rsbool) rsbool = smsService.startService();
		return rsbool;
	}	
	
	/*---------------------------开启,关闭服务 发送短信-------------------------------*/
	/**
	 * @describe: 测试短信模块(串口号自动检测,不需要设置)
	 * @param baudrate: 波特率
	 * @param passPort: 不用检测的串口号
	 * @return: true:连接成功,并进入运行状态 false:失败 
	 * @date:2010-3-3
	 */
	public boolean startService(int baudrate, String passPort){
		boolean rsbool = false;
		String comName = getRightComStr(passPort); 		// 获取合适短信模块的 串口字符
		//String comName = "COM5";
		if (comName != null){
			smsSendJob.initial(baudrate, comName);		// 设置波特率和串口字符
			rsbool = smsSendJob.readyToSendMsg();		// 准备 - ok
			// 在知道是短信模块的前提下,如果通讯异常,就关闭串口
			if (rsbool == false){smslib_test.closePort();}
			sms_run_flag = rsbool;						// 设置短信模块运行标志
		}else{
			System.out.println("没有找到合适的串口号");
		}
		return rsbool;
	}	
	
	/**
	 * @describe: 停止服务
	 * @return: true:成功 false:失败
	 * @date:2010-3-1
	 */	
	public boolean stopService(){
		sms_run_flag = false;
		return smsService.stopService();
	}	
	
	/**
	 * @describe: 给指定的一组手机号码,发送短信
	 * @param phoneList 手机号码列表
	 * @param message 信息内容
	 * @return: true:成功 false:失败
	 * @date:2010-3-2
	 */
	public boolean sendMessage(List<String> phoneList, String message){
		boolean rsbool = false;
		if ( (phoneList != null) && (phoneList.size() > 0) ){
			rsbool = smsService.sendMessage(phoneList, message);
		}
		return rsbool;
	}
	
	/**
	 * @describe: 给指定的一个手机号码,发送短信
	 * @param phoneList 手机号码列表
	 * @param message 信息内容
	 * @return: true:成功 false:失败
	 * @date:2010-3-2
     */
	public boolean sendMessage(String phone, String message){
		boolean rsbool = false;
		if ( (phone != null) && (phone.length() > 0) ){
			rsbool = smsService.sendMessage(phone, message);
		}
		return rsbool;
	}
	
	/**
	 * @describe: 打印sms信息	
	 * @date:2010-3-2
	 */
	public void printSmsInof() throws Exception{
		smsService.smsInfo();
	}
	
	/**
	 * @describe:	判断  短信模块运行标志<br>
	 * <code>true</code>: 正在运行<br>
	 * <code>false</code>: 没有运行<br>
	 * @date:2010-3-3
	 */
	public boolean isSms_run_flag() {
		return sms_run_flag;
	}

	public static void main(String[] args) throws Exception{
		Smslib_SendJob smsSendJob = Smslib_SendJob.getInstance();	// 运行实例
		if (smsSendJob.startService(9600,"")){
			//smsSendJob.printSmsInof();
			List<String> phoneList = new ArrayList<String>();
			phoneList.add("10086");
			String message = "11"; // 给10086发一条查询余额的短信						
			smsSendJob.sendMessage(phoneList, message);
			// 接收短信在SmsService中已经注册,InboundNotification中process会处理
			// 收短信后,默认会删除收到的短信,也可以通过setRec_msg_remove(boolean)修改
			Thread.sleep(30 * 1000);  						// 一分钟后,关闭短信服务
			smsSendJob.stopService();
		}
	}
	
}
