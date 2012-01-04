package com.htc.model.seriaPort;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.htc.model.tool.ShortMessageUnit;

/**
 * 作用 : 短信模块的单元类<br>
 * 类使用介绍:<br>
 * 1:openPort:打开串口<br>
 * 2:closePort:关闭串口<br>
 * 3:sendMessage:发送短信<br>
 * 4:receiveMessge:接收最近一条短信<br>
 * 5:delMessage:删除短信<br>
 * 6:simCardInfo:查询短信模块的信息<br>
 */
public class SimCard_Unit{
	private Log log = LogFactory.getLog(SimCard_Unit.class);
	private static int MAX_SEND_LEN = 70;			// 单条短信最长的发送字数(70个)
	private static int SMS_SEND_INTERVAL =5*1000;	// 短信发完后等待间隔(5000毫秒)
	private static String SMSAPPNAME = "sms_port";	// 短信发送模块专用的端口名:sms_port
	private static String RESPONSE_OK = "OK";		// 短信模块返回的成功字符
	private static String RESPONSE_ERROR = "ERROR";	// 短信模块返回的错误字符
	
	private SimCard_Serial simCard_Serial; 			// 短信通信串口处理类
	private CommonDataUnit commonDataUnit;
	int waitMillTime = 500;							// 根据波特率,数据倍率和数据量,自动设置发送间隔
	int retryTime = 3;								// 通信没有完成的时候,重试次数	
	
	//注册service -- spring ioc
	public void setSimCard_Serial(SimCard_Serial simCard_Serial) {
		this.simCard_Serial = simCard_Serial;
	}
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}

	/**
	 * 返回模块串口运行状态
	 */
	public boolean isRunFlag() {
		return simCard_Serial.isPortIsOpenFlag();
	}

	// 构造方法
	public SimCard_Unit(){
	}
	
	/**
	 * 设置附加的一些参数<br>
	 * @param waitMillTime 读取串口数据间隔<br>
	 * @param retryTime	   通信没有完成的时候,重试次数<br>
	 */
	public void setParam(int waitMillTime, int retryTime){
		this.waitMillTime = waitMillTime;
		this.retryTime = retryTime;
	}
	
	/************************专门为测试短信设计的方法(只走一遍流程:打开串口,发送短信,关闭串口)*************************/
	/**
	 * 专门为测试短信设计的方法(只走一遍流程:打开串口,发送短信,关闭串口)
	 * @param excludePortName 不用检测的串口号
	 * @param baudrate 波特率
	 * @param centerNo 短信中心号
	 * @param phone 手机号
	 * @param message 发送短信的内容 
	 * @return: true:成功 false:失败 
	 */
	public boolean singleForTest(String excludePortName , int baudrate, String centerNo, String phoneNo, String message){
		boolean rsBool = false;	// 返回结果
		rsBool = openPort(excludePortName, baudrate, true);
		if (rsBool){
			rsBool = sendMessage(centerNo, phoneNo, message);
			closePort(true);
		}
		return rsBool;
	}
	
	/****************************************串口相关操作:打开,关闭, 测试串口****************************************/
	/**
	 * 连接到短信模块相连的串口(串口由程序自动获取)
	 * @param excludePortName 不用检测的串口号
	 * @param baudrate 波特率
	 * @return:  true:连接成功,并进入运行状态 false:失败 
	 */
	public boolean openPort(String excludePortName , int baudrate){
		return openPort(excludePortName , baudrate, false);
	}
	/**
	 * 连接到短信模块相连的串口(串口由程序自动获取)
	 * @param excludePortName 不用检测的串口号
	 * @param baudrate 波特率
	 * @param isTest true:专门为测试用,不设置运行状态   false:正式使用
	 * @return:  true:连接成功,并进入运行状态 false:失败 
	 */
	public boolean openPort(String excludePortName , int baudrate, boolean isTest){
		boolean runFlag = false;	// 模块串口运行状态
		for (String portName : SimCard_Serial.getAllComPorts()) { 						// 遍历系统中存在的所有串口
			if (portName.toUpperCase().equals(excludePortName.toUpperCase())){continue;}// 过滤不检测的串口
			runFlag = testSimCard(portName, baudrate, SMSAPPNAME);						// 测试portName涉及的串口是否是短信模块串口
			if (runFlag){break;}
		}
		if (isTest == false){
			simCard_Serial.setPortIsOpenFlag(runFlag);									// 设置模块对应串口的运行状态
		}
		return runFlag;		
	}
	
	/**
	 * 把短信模块相连的串口断开
	 */
	public boolean closePort(){
		return closePort(false);
	}
	/**
	 * 把短信模块相连的串口断开
	 * @param isTest true:专门为测试用,不设置运行状态   false:正式使用
	 */
	public boolean closePort(boolean isTest){
		boolean runFlag = simCard_Serial.closePort();									// 设置模块串口运行状态为:关闭状态
		if (isTest == false){
			simCard_Serial.setPortIsOpenFlag(!runFlag);									// 设置模块对应串口的运行状态		
		}
		return runFlag;
	}
	
	/**
	 * @describe: 测试串口的是否适合短信模块
	 * @param portName 串口号：如：com1
	 * @param baudrate 波特率
	 * @param appname 串口程序名
	 * @return: true:成功 false:失败 
	 */	
	private boolean testSimCard(String portName, int baudrate, String appname){
		if (simCard_Serial == null){simCard_Serial = new SimCard_Serial();}// 使用spring时,可以删除
		boolean rsBool = false;
		try {
			simCard_Serial.init(portName, baudrate, appname);				// ①初始化参数
			rsBool = simCard_Serial.openPort();								// ② 打开串口			
			if (rsBool){
				freeAndReturnMemory();										// ③清理缓冲										
				// 注：<CR>是回车符，<CRLF>是回车换行符。
				// 1:测试连接性:AT	
				// 输入：AT<CR>		 
				// 响应：<CRLF>OK<CRLF>说明GSM模块与外部设备连接成功
				sendATCommand("AT\r");										// ④发送AT指令(加换行符号\r)
				rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// ⑤读串口(根据得到的数据,判断返回数据的连通性{返回字符包含OK表示成功})
				if (rsBool){rsBool = initSimCard();							// ⑥-1 (找到合适的串口)初始化模块信息
				}else{simCard_Serial.closePort();}							// ⑥-2 (没有找到合适的串口)关闭串口
				
				if(rsBool){
					log.info("找到" + portName + ":短信模块串口,并成功打开...");
				}else{
					log.info("找到" + portName + ":不是短信模块串口...");
				};
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsBool;
	}
	/*****************************短信相关操作:发送短信, 接收短信, 删除短信*****************************/	
	/**
	 * 给手机号码为phoneNo发送msg的短消息. 返回:true:成功  false:失败
	 * @param centerNo 短信中心号
	 * @param phone 手机号
	 * @param message 发送短信的内容 
	 */
	public boolean sendMessage(String centerNo, String phoneNo, String message){
		boolean rsbool = true;
		String msg ="";					// 经过处理的,要发送的短信内容
		String sendmsg4At = "";			// 经过编码后,要发送的信息
		int msgLen = message.length();	// 短信内容的中长度(太长会被分几次发送)
		int len = 0;					// 单次发送的总长度
		int beginIndex = 0; 			// 截取字符串的的其实位置
		int subLen = 0;					// 临时变量
		int index = 0;
		boolean hasNext = true;
		try {
			subLen = msgLen;
			while (hasNext) {
				// 截取短信内容
				if (subLen > MAX_SEND_LEN){
					subLen = MAX_SEND_LEN;
				}else{
					hasNext = false;
				}
				msg = message.substring(index, index + subLen);
				index = index + subLen;
				subLen = msgLen - index;
				
				// 发送短信内容
				sendmsg4At = ShortMessageUnit.code4sendMessage(centerNo, phoneNo, msg);// 发送短信AT指令需要的字符串
				len = sendmsg4At.length();
				beginIndex = Integer.parseInt(sendmsg4At.substring(0, 2),16) * 2 + 2;		// 短信中心所占的长度
//				System.out.println("index:"+index+"\nsubLen:"+(index+subLen)+"\nmsg:"+msg);
//				System.out.println("AT+CMGS=" + ((len - beginIndex)/2));
//				System.out.println(sendmsg4At);
//				System.out.println(msg);
				sendATCommand("AT+CMGS=" + ((len - beginIndex)/2) + "\r");
				sendATCommand(sendmsg4At + "\r");											// ② 发送AT指令(发送短信内容)
				sendATCommand((char)26 + "\r");												// ③ 发送AT指令(发送结束符号""(即(char)26))
				Thread.sleep(SMS_SEND_INTERVAL);											// ④发完短信等待SMS_SEND_INTERVAL毫秒
				// sendATCommand("AT\r");	
				//rsbool = receiveATResponse(RESPONSE_OK,RESPONSE_ERROR,1)!=null;				// 读取模块响应结果 -- 可以看到结果 -- 测试用				
				rsbool = receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;				// 读取模块响应结果 -- 正式用				
			}
		} catch (Exception e) {
			rsbool = false;
			log.error("发送短信时出错" + e.getMessage());
		}
		return rsbool;
	}
	
	/**
	 * 根据短信序号删除短信
	 * @throws Exception 
	 */
	public boolean delMessage(int msgIndex) throws Exception {
		sendATCommand("AT+CMGD=" + msgIndex + "\r");						// 发送AT指令(加换行符号\r)
		boolean rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// 读取模块相应结果		
		return rsBool;		
	}
	
	/**
	 * 查看短信模块的信号
	 */
	public int watchSignal() throws Exception {
		int rsint = 99; // 99代表没有信号
		sendATCommand("AT+CSQ\r");
		String signal = dealResponseStr(); // 返回值如: +CSQ: 24,0
		if (signal != null){
			signal = signal.replaceAll(" ", "");
			signal = signal.substring(signal.indexOf(":")+1, signal.indexOf(","));
			rsint = Integer.parseInt(signal);
		}
		return rsint;
	}
	
	
	/*****************************底层simcard操作:初始化simCard, 得到simCard相关信息, 发送AT命令, 接收信息*****************************/
	/**
	 * 清理缓冲
	 */
	public String freeAndReturnMemory(){
		return freeAndReturnMemory(1);
	}
	
	
	/**
	 * 提取缓冲内容后是否要清理短信模块缓存,并把缓存内容返回
	 * @param  type可以为下面的值<br>
	 *  1:清理缓存<br>
	 *  2:不清理缓存<br>
	 */
	public String freeAndReturnMemory(int type){
		String rsStr = simCard_Serial.getReceiveMsg();		// 得到模块缓存
		if (type == 1 ){
			simCard_Serial.clearReceiveMsg();				// 清理模块缓存
		}else if (type == 2){
		}
		return rsStr;
	}
	
	/**
	 * 把没有处理的信息放回缓冲池中
	 */
	public void backToPool(String undealStr){
		simCard_Serial.backToPool(undealStr);
	}
	
	/**
	 * 运行系统前,设置模块的硬件配置:<br>
	 * 1:测试连接性:AT<br>
	 * 2:设置不显示回显:ATE0<br>
	 * 3:设置短信格式:AT+CMGF=0<br>
	 */
	private boolean initSimCard() throws Exception{
		boolean rsBool = false;
		// 1:设置不显示回显:ATE0
		// 输入：ATE[<value>]<CR> 参数：<value>—0表示不回显；1表示回显。
		// 响应：<CRLF>OK<CRLF>  //表示成功
		sendATCommand("ATE0\r");									// 发送AT指令(加换行符号\r)
		rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// 读取模块相应结果		
		if(rsBool==false){return rsBool;}
		// 2:设置短信格式:AT+CMGF=0		
		// 输入：AT+CMGF=[<mode>]<CR> 参数：<mode>—取0为PDU模式，取1为文本模式，即Text模式。
		// 响应：<CRLF>OK<CRLF>
		sendATCommand("AT+CMGF=0\r");								// 发送AT指令(加换行符号\r)
		rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// 读取模块相应结果
		if(rsBool==false){return rsBool;}
		// 3:设置接收的短信不保存在sim卡上: AT+CNMI=2,2
		int cnmiType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.SMS_STORE_TYPE));
		if (cnmiType == 1){
			sendATCommand("AT+CNMI=2,2\r");								// 发送AT指令(加换行符号\r)
		}else if (cnmiType == 2){
			sendATCommand("AT+CNMI=2,1\r");								// 发送AT指令(加换行符号\r)
		}
		rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// 读取模块相应结果
		if(rsBool==false){return rsBool;}
		
		return rsBool;
	}
	
	/**
	 * 返回模块和sim卡的一些信息<br>
	 * 目前有的信息如下:<br>
	 * 1: AT+CGMI(得到模块厂商的信息)		(如:SIEMENS)<br>
	 * 2: AT+CSQ(获取sim卡当前信号)		(如:+CSQ: 27,99)<br>
	 * 3: AT+COPS?(获取sim的网络运营商)	(如:+COPS: 0,0,"China Mobile")<br>
	 * 4: AT+CPMS? 短信储存地点			(如:+CPMS: "ME",25,25,"SM",20,50,"SM",20,50)<br>
	 * 5: AT+CMGF? 短信格式				(如:+CMGF: 0)<br>
	 */
	public String simCardInfo(){
		StringBuffer strBuf = new StringBuffer();
		try {
			//  1: AT+CGMI(得到模块厂商的信息)		(如:SIEMENS)
			sendATCommand("AT+CGMI\r");
			strBuf.append("模块厂商" + dealResponseStr());
			// 2: AT+CSQ(获取sim卡当前信号)		(如:+CSQ: 27,99)
			sendATCommand("AT+CSQ\r");
			strBuf.append("手机信号" + dealResponseStr());
			// 3: AT+COPS?(获取sim的网络运营商)	(如:+COPS: 0,0,"China Mobile")
			sendATCommand("AT+COPS?\r");
			strBuf.append("运营商家" + dealResponseStr());
			// 4: AT+CPMS? 短信储存地点			(如:+CPMS: "ME",25,25,"SM",20,50,"SM",20,50)
			sendATCommand("AT+CPMS?\r");
			strBuf.append("储存地点" + dealResponseStr());
			// 5: AT+CMGF? 短信格式				(如:+CMGF: 0)
			sendATCommand("AT+CMGF?\r");
			strBuf.append("短信格式" + dealResponseStr());
			// 6: AT+CNMI? 存储方式
			sendATCommand("AT+CNMI?\r");
			strBuf.append("存储方式" + dealResponseStr());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}	
	
	/**
	 * 加工模块返回的信息
	 * @throws Exception 
	 */
	private String dealResponseStr() throws Exception{
		String tempStr = "";
		tempStr = receiveATResponse(RESPONSE_OK,RESPONSE_ERROR);
		if (tempStr == null){return tempStr;}
		tempStr = tempStr.replaceAll("\\r", "").replaceAll("\\n", "").replaceAll("OK", "") + "\n";
		return tempStr;
	}
	
	/**
	 * 发送AT指令
	 * @param commandStr: AT指令字符串
	 */
	private void sendATCommand(String commandStr) throws Exception{
		simCard_Serial.writeByte(commandStr);
		Thread.sleep(waitMillTime);
	}
	
	/**
	 * 返回发送AT指令后的最近的一个响应信息,并返回相应的值<br>
	 * 1: 得到一个响应信息<br>
	 * 2: 判断是否包含correctValue或errorValue值,没有发现则进入下一步<br>
	 * 3: 睡眠frameInterval毫秒, 用来等待接收更多的串口返回信息,再回到第2步<br>
	 * 4: 重复2,3 retryTime次.如果没有得到结果,返回null值.<br>
	 * 注意: frameInterval(默认100)和retryTime(默认3),可以通过setParam修改.<br>
	 * @param correctValue: 响应信息中可能包含的正确值<br>
	 * @param errorValue: 	响应信息中可能包含的错误值<br>
	 */
	private String receiveATResponse(String correctValue, String errorValue) throws Exception{
		return receiveATResponse(correctValue, errorValue, 0);
	}
	
	/**
	 * 返回发送AT指令后的最近的一个响应信息,并返回相应的值<br>
	 * 1: 得到一个响应信息<br>
	 * 2: 判断是否包含correctValue或errorValue值,没有发现则进入下一步<br>
	 * 3: 睡眠frameInterval毫秒, 用来等待接收更多的串口返回信息,再回到第2步<br>
	 * 4: 重复2,3 retryTime次.如果没有得到结果,返回null值.<br>
	 * 注意: frameInterval(默认100)和retryTime(默认3),可以通过setParam修改.<br>
	 * @param correctValue: 响应信息中可能包含的正确值<br>
	 * @param errorValue: 	响应信息中可能包含的错误值<br>
	 * @param type: 是否显示返回值:(type=0不显示,type=1显示)(测试用)<br>
	 */
	private String receiveATResponse(String correctValue, String errorValue, int type) throws Exception{
		String recMsg = null;
		boolean findFlag = false;
		int retry_Time = retryTime;
		while (retry_Time>0) {
			recMsg = simCard_Serial.getReceiveMsg();								// 得到模块缓存
			//System.out.println(recMsg);
			if (type == 1){System.out.println(retry_Time +":"+recMsg);}		// 测试用
			if (recMsg != null){
				if(recMsg.toUpperCase().indexOf(correctValue.toUpperCase())>=0){ 	// 找到真确的返回信息
					simCard_Serial.clearReceiveMsg();								// 重置模块缓存
					findFlag = true;	break;
				}else if(recMsg.toUpperCase().indexOf(errorValue.toUpperCase())>=0){ // 找到错误的返回信息
					findFlag = false;	break;
				}
			}
			Thread.sleep(waitMillTime);												// 等待一段时间 接收串口返回的信息	
			retry_Time--;
		}
		recMsg = findFlag? recMsg : null;
		return recMsg;
	}
	
//	public static void main(String[] args) throws Exception{
//		SimCard_Unit unit = new SimCard_Unit();
//		unit.setCommonDataUnit(new CommonDataUnit());
//		if (unit.openPort("com1", 9600)){
//			System.out.println(unit.simCardInfo());
//			System.out.println("解析信号量:" + unit.watchSignal());
//			//String msg = "Test0123，你好，hello.123!!!。。。";
//			String msg = "报警信息\r\n仪器名:测试区-1号\r\n温度:27.77℃,低温报警\r\n湿度:42.07%RH,正常\r\n时间:2010-05-07 16:55";
//			if(unit.sendMessage("13800571500", "13738195048", msg)){//13738195048 - 测试卡:13634176292
//				System.out.println("短信发送成功...");
//			}else{
//				System.out.println("短信发送失败...");
//			}	
//			unit.closePort();
//		}else{
//			System.out.println("over...");
//		}
//	}	

}
