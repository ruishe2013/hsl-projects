package com.htc.model.seriaPort;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.htc.model.tool.ShortMessageUnit;

/**
 * ���� : ����ģ��ĵ�Ԫ��<br>
 * ��ʹ�ý���:<br>
 * 1:openPort:�򿪴���<br>
 * 2:closePort:�رմ���<br>
 * 3:sendMessage:���Ͷ���<br>
 * 4:receiveMessge:�������һ������<br>
 * 5:delMessage:ɾ������<br>
 * 6:simCardInfo:��ѯ����ģ�����Ϣ<br>
 */
public class SimCard_Unit{
	private Log log = LogFactory.getLog(SimCard_Unit.class);
	private static int MAX_SEND_LEN = 70;			// ����������ķ�������(70��)
	private static int SMS_SEND_INTERVAL =5*1000;	// ���ŷ����ȴ����(5000����)
	private static String SMSAPPNAME = "sms_port";	// ���ŷ���ģ��ר�õĶ˿���:sms_port
	private static String RESPONSE_OK = "OK";		// ����ģ�鷵�صĳɹ��ַ�
	private static String RESPONSE_ERROR = "ERROR";	// ����ģ�鷵�صĴ����ַ�
	
	private SimCard_Serial simCard_Serial; 			// ����ͨ�Ŵ��ڴ�����
	private CommonDataUnit commonDataUnit;
	int waitMillTime = 500;							// ���ݲ�����,���ݱ��ʺ�������,�Զ����÷��ͼ��
	int retryTime = 3;								// ͨ��û����ɵ�ʱ��,���Դ���	
	
	//ע��service -- spring ioc
	public void setSimCard_Serial(SimCard_Serial simCard_Serial) {
		this.simCard_Serial = simCard_Serial;
	}
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}

	/**
	 * ����ģ�鴮������״̬
	 */
	public boolean isRunFlag() {
		return simCard_Serial.isPortIsOpenFlag();
	}

	// ���췽��
	public SimCard_Unit(){
	}
	
	/**
	 * ���ø��ӵ�һЩ����<br>
	 * @param waitMillTime ��ȡ�������ݼ��<br>
	 * @param retryTime	   ͨ��û����ɵ�ʱ��,���Դ���<br>
	 */
	public void setParam(int waitMillTime, int retryTime){
		this.waitMillTime = waitMillTime;
		this.retryTime = retryTime;
	}
	
	/************************ר��Ϊ���Զ�����Ƶķ���(ֻ��һ������:�򿪴���,���Ͷ���,�رմ���)*************************/
	/**
	 * ר��Ϊ���Զ�����Ƶķ���(ֻ��һ������:�򿪴���,���Ͷ���,�رմ���)
	 * @param excludePortName ���ü��Ĵ��ں�
	 * @param baudrate ������
	 * @param centerNo �������ĺ�
	 * @param phone �ֻ���
	 * @param message ���Ͷ��ŵ����� 
	 * @return: true:�ɹ� false:ʧ�� 
	 */
	public boolean singleForTest(String excludePortName , int baudrate, String centerNo, String phoneNo, String message){
		boolean rsBool = false;	// ���ؽ��
		rsBool = openPort(excludePortName, baudrate, true);
		if (rsBool){
			rsBool = sendMessage(centerNo, phoneNo, message);
			closePort(true);
		}
		return rsBool;
	}
	
	/****************************************������ز���:��,�ر�, ���Դ���****************************************/
	/**
	 * ���ӵ�����ģ�������Ĵ���(�����ɳ����Զ���ȡ)
	 * @param excludePortName ���ü��Ĵ��ں�
	 * @param baudrate ������
	 * @return:  true:���ӳɹ�,����������״̬ false:ʧ�� 
	 */
	public boolean openPort(String excludePortName , int baudrate){
		return openPort(excludePortName , baudrate, false);
	}
	/**
	 * ���ӵ�����ģ�������Ĵ���(�����ɳ����Զ���ȡ)
	 * @param excludePortName ���ü��Ĵ��ں�
	 * @param baudrate ������
	 * @param isTest true:ר��Ϊ������,����������״̬   false:��ʽʹ��
	 * @return:  true:���ӳɹ�,����������״̬ false:ʧ�� 
	 */
	public boolean openPort(String excludePortName , int baudrate, boolean isTest){
		boolean runFlag = false;	// ģ�鴮������״̬
		for (String portName : SimCard_Serial.getAllComPorts()) { 						// ����ϵͳ�д��ڵ����д���
			if (portName.toUpperCase().equals(excludePortName.toUpperCase())){continue;}// ���˲����Ĵ���
			runFlag = testSimCard(portName, baudrate, SMSAPPNAME);						// ����portName�漰�Ĵ����Ƿ��Ƕ���ģ�鴮��
			if (runFlag){break;}
		}
		if (isTest == false){
			simCard_Serial.setPortIsOpenFlag(runFlag);									// ����ģ���Ӧ���ڵ�����״̬
		}
		return runFlag;		
	}
	
	/**
	 * �Ѷ���ģ�������Ĵ��ڶϿ�
	 */
	public boolean closePort(){
		return closePort(false);
	}
	/**
	 * �Ѷ���ģ�������Ĵ��ڶϿ�
	 * @param isTest true:ר��Ϊ������,����������״̬   false:��ʽʹ��
	 */
	public boolean closePort(boolean isTest){
		boolean runFlag = simCard_Serial.closePort();									// ����ģ�鴮������״̬Ϊ:�ر�״̬
		if (isTest == false){
			simCard_Serial.setPortIsOpenFlag(!runFlag);									// ����ģ���Ӧ���ڵ�����״̬		
		}
		return runFlag;
	}
	
	/**
	 * @describe: ���Դ��ڵ��Ƿ��ʺ϶���ģ��
	 * @param portName ���ںţ��磺com1
	 * @param baudrate ������
	 * @param appname ���ڳ�����
	 * @return: true:�ɹ� false:ʧ�� 
	 */	
	private boolean testSimCard(String portName, int baudrate, String appname){
		if (simCard_Serial == null){simCard_Serial = new SimCard_Serial();}// ʹ��springʱ,����ɾ��
		boolean rsBool = false;
		try {
			simCard_Serial.init(portName, baudrate, appname);				// �ٳ�ʼ������
			rsBool = simCard_Serial.openPort();								// �� �򿪴���			
			if (rsBool){
				freeAndReturnMemory();										// ��������										
				// ע��<CR>�ǻس�����<CRLF>�ǻس����з���
				// 1:����������:AT	
				// ���룺AT<CR>		 
				// ��Ӧ��<CRLF>OK<CRLF>˵��GSMģ�����ⲿ�豸���ӳɹ�
				sendATCommand("AT\r");										// �ܷ���ATָ��(�ӻ��з���\r)
				rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// �ݶ�����(���ݵõ�������,�жϷ������ݵ���ͨ��{�����ַ�����OK��ʾ�ɹ�})
				if (rsBool){rsBool = initSimCard();							// ��-1 (�ҵ����ʵĴ���)��ʼ��ģ����Ϣ
				}else{simCard_Serial.closePort();}							// ��-2 (û���ҵ����ʵĴ���)�رմ���
				
				if(rsBool){
					log.info("�ҵ�" + portName + ":����ģ�鴮��,���ɹ���...");
				}else{
					log.info("�ҵ�" + portName + ":���Ƕ���ģ�鴮��...");
				};
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsBool;
	}
	/*****************************������ز���:���Ͷ���, ���ն���, ɾ������*****************************/	
	/**
	 * ���ֻ�����ΪphoneNo����msg�Ķ���Ϣ. ����:true:�ɹ�  false:ʧ��
	 * @param centerNo �������ĺ�
	 * @param phone �ֻ���
	 * @param message ���Ͷ��ŵ����� 
	 */
	public boolean sendMessage(String centerNo, String phoneNo, String message){
		boolean rsbool = true;
		String msg ="";					// ���������,Ҫ���͵Ķ�������
		String sendmsg4At = "";			// ���������,Ҫ���͵���Ϣ
		int msgLen = message.length();	// �������ݵ��г���(̫���ᱻ�ּ��η���)
		int len = 0;					// ���η��͵��ܳ���
		int beginIndex = 0; 			// ��ȡ�ַ����ĵ���ʵλ��
		int subLen = 0;					// ��ʱ����
		int index = 0;
		boolean hasNext = true;
		try {
			subLen = msgLen;
			while (hasNext) {
				// ��ȡ��������
				if (subLen > MAX_SEND_LEN){
					subLen = MAX_SEND_LEN;
				}else{
					hasNext = false;
				}
				msg = message.substring(index, index + subLen);
				index = index + subLen;
				subLen = msgLen - index;
				
				// ���Ͷ�������
				sendmsg4At = ShortMessageUnit.code4sendMessage(centerNo, phoneNo, msg);// ���Ͷ���ATָ����Ҫ���ַ���
				len = sendmsg4At.length();
				beginIndex = Integer.parseInt(sendmsg4At.substring(0, 2),16) * 2 + 2;		// ����������ռ�ĳ���
//				System.out.println("index:"+index+"\nsubLen:"+(index+subLen)+"\nmsg:"+msg);
//				System.out.println("AT+CMGS=" + ((len - beginIndex)/2));
//				System.out.println(sendmsg4At);
//				System.out.println(msg);
				sendATCommand("AT+CMGS=" + ((len - beginIndex)/2) + "\r");
				sendATCommand(sendmsg4At + "\r");											// �� ����ATָ��(���Ͷ�������)
				sendATCommand((char)26 + "\r");												// �� ����ATָ��(���ͽ�������""(��(char)26))
				Thread.sleep(SMS_SEND_INTERVAL);											// �ܷ�����ŵȴ�SMS_SEND_INTERVAL����
				// sendATCommand("AT\r");	
				//rsbool = receiveATResponse(RESPONSE_OK,RESPONSE_ERROR,1)!=null;				// ��ȡģ����Ӧ��� -- ���Կ������ -- ������				
				rsbool = receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;				// ��ȡģ����Ӧ��� -- ��ʽ��				
			}
		} catch (Exception e) {
			rsbool = false;
			log.error("���Ͷ���ʱ����" + e.getMessage());
		}
		return rsbool;
	}
	
	/**
	 * ���ݶ������ɾ������
	 * @throws Exception 
	 */
	public boolean delMessage(int msgIndex) throws Exception {
		sendATCommand("AT+CMGD=" + msgIndex + "\r");						// ����ATָ��(�ӻ��з���\r)
		boolean rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// ��ȡģ����Ӧ���		
		return rsBool;		
	}
	
	/**
	 * �鿴����ģ����ź�
	 */
	public int watchSignal() throws Exception {
		int rsint = 99; // 99����û���ź�
		sendATCommand("AT+CSQ\r");
		String signal = dealResponseStr(); // ����ֵ��: +CSQ: 24,0
		if (signal != null){
			signal = signal.replaceAll(" ", "");
			signal = signal.substring(signal.indexOf(":")+1, signal.indexOf(","));
			rsint = Integer.parseInt(signal);
		}
		return rsint;
	}
	
	
	/*****************************�ײ�simcard����:��ʼ��simCard, �õ�simCard�����Ϣ, ����AT����, ������Ϣ*****************************/
	/**
	 * ������
	 */
	public String freeAndReturnMemory(){
		return freeAndReturnMemory(1);
	}
	
	
	/**
	 * ��ȡ�������ݺ��Ƿ�Ҫ�������ģ�黺��,���ѻ������ݷ���
	 * @param  type����Ϊ�����ֵ<br>
	 *  1:������<br>
	 *  2:��������<br>
	 */
	public String freeAndReturnMemory(int type){
		String rsStr = simCard_Serial.getReceiveMsg();		// �õ�ģ�黺��
		if (type == 1 ){
			simCard_Serial.clearReceiveMsg();				// ����ģ�黺��
		}else if (type == 2){
		}
		return rsStr;
	}
	
	/**
	 * ��û�д������Ϣ�Żػ������
	 */
	public void backToPool(String undealStr){
		simCard_Serial.backToPool(undealStr);
	}
	
	/**
	 * ����ϵͳǰ,����ģ���Ӳ������:<br>
	 * 1:����������:AT<br>
	 * 2:���ò���ʾ����:ATE0<br>
	 * 3:���ö��Ÿ�ʽ:AT+CMGF=0<br>
	 */
	private boolean initSimCard() throws Exception{
		boolean rsBool = false;
		// 1:���ò���ʾ����:ATE0
		// ���룺ATE[<value>]<CR> ������<value>��0��ʾ�����ԣ�1��ʾ���ԡ�
		// ��Ӧ��<CRLF>OK<CRLF>  //��ʾ�ɹ�
		sendATCommand("ATE0\r");									// ����ATָ��(�ӻ��з���\r)
		rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// ��ȡģ����Ӧ���		
		if(rsBool==false){return rsBool;}
		// 2:���ö��Ÿ�ʽ:AT+CMGF=0		
		// ���룺AT+CMGF=[<mode>]<CR> ������<mode>��ȡ0ΪPDUģʽ��ȡ1Ϊ�ı�ģʽ����Textģʽ��
		// ��Ӧ��<CRLF>OK<CRLF>
		sendATCommand("AT+CMGF=0\r");								// ����ATָ��(�ӻ��з���\r)
		rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// ��ȡģ����Ӧ���
		if(rsBool==false){return rsBool;}
		// 3:���ý��յĶ��Ų�������sim����: AT+CNMI=2,2
		int cnmiType = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.SMS_STORE_TYPE));
		if (cnmiType == 1){
			sendATCommand("AT+CNMI=2,2\r");								// ����ATָ��(�ӻ��з���\r)
		}else if (cnmiType == 2){
			sendATCommand("AT+CNMI=2,1\r");								// ����ATָ��(�ӻ��з���\r)
		}
		rsBool=receiveATResponse(RESPONSE_OK,RESPONSE_ERROR)!=null;	// ��ȡģ����Ӧ���
		if(rsBool==false){return rsBool;}
		
		return rsBool;
	}
	
	/**
	 * ����ģ���sim����һЩ��Ϣ<br>
	 * Ŀǰ�е���Ϣ����:<br>
	 * 1: AT+CGMI(�õ�ģ�鳧�̵���Ϣ)		(��:SIEMENS)<br>
	 * 2: AT+CSQ(��ȡsim����ǰ�ź�)		(��:+CSQ: 27,99)<br>
	 * 3: AT+COPS?(��ȡsim��������Ӫ��)	(��:+COPS: 0,0,"China Mobile")<br>
	 * 4: AT+CPMS? ���Ŵ���ص�			(��:+CPMS: "ME",25,25,"SM",20,50,"SM",20,50)<br>
	 * 5: AT+CMGF? ���Ÿ�ʽ				(��:+CMGF: 0)<br>
	 */
	public String simCardInfo(){
		StringBuffer strBuf = new StringBuffer();
		try {
			//  1: AT+CGMI(�õ�ģ�鳧�̵���Ϣ)		(��:SIEMENS)
			sendATCommand("AT+CGMI\r");
			strBuf.append("ģ�鳧��" + dealResponseStr());
			// 2: AT+CSQ(��ȡsim����ǰ�ź�)		(��:+CSQ: 27,99)
			sendATCommand("AT+CSQ\r");
			strBuf.append("�ֻ��ź�" + dealResponseStr());
			// 3: AT+COPS?(��ȡsim��������Ӫ��)	(��:+COPS: 0,0,"China Mobile")
			sendATCommand("AT+COPS?\r");
			strBuf.append("��Ӫ�̼�" + dealResponseStr());
			// 4: AT+CPMS? ���Ŵ���ص�			(��:+CPMS: "ME",25,25,"SM",20,50,"SM",20,50)
			sendATCommand("AT+CPMS?\r");
			strBuf.append("����ص�" + dealResponseStr());
			// 5: AT+CMGF? ���Ÿ�ʽ				(��:+CMGF: 0)
			sendATCommand("AT+CMGF?\r");
			strBuf.append("���Ÿ�ʽ" + dealResponseStr());
			// 6: AT+CNMI? �洢��ʽ
			sendATCommand("AT+CNMI?\r");
			strBuf.append("�洢��ʽ" + dealResponseStr());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strBuf.toString();
	}	
	
	/**
	 * �ӹ�ģ�鷵�ص���Ϣ
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
	 * ����ATָ��
	 * @param commandStr: ATָ���ַ���
	 */
	private void sendATCommand(String commandStr) throws Exception{
		simCard_Serial.writeByte(commandStr);
		Thread.sleep(waitMillTime);
	}
	
	/**
	 * ���ط���ATָ���������һ����Ӧ��Ϣ,��������Ӧ��ֵ<br>
	 * 1: �õ�һ����Ӧ��Ϣ<br>
	 * 2: �ж��Ƿ����correctValue��errorValueֵ,û�з����������һ��<br>
	 * 3: ˯��frameInterval����, �����ȴ����ո���Ĵ��ڷ�����Ϣ,�ٻص���2��<br>
	 * 4: �ظ�2,3 retryTime��.���û�еõ����,����nullֵ.<br>
	 * ע��: frameInterval(Ĭ��100)��retryTime(Ĭ��3),����ͨ��setParam�޸�.<br>
	 * @param correctValue: ��Ӧ��Ϣ�п��ܰ�������ȷֵ<br>
	 * @param errorValue: 	��Ӧ��Ϣ�п��ܰ����Ĵ���ֵ<br>
	 */
	private String receiveATResponse(String correctValue, String errorValue) throws Exception{
		return receiveATResponse(correctValue, errorValue, 0);
	}
	
	/**
	 * ���ط���ATָ���������һ����Ӧ��Ϣ,��������Ӧ��ֵ<br>
	 * 1: �õ�һ����Ӧ��Ϣ<br>
	 * 2: �ж��Ƿ����correctValue��errorValueֵ,û�з����������һ��<br>
	 * 3: ˯��frameInterval����, �����ȴ����ո���Ĵ��ڷ�����Ϣ,�ٻص���2��<br>
	 * 4: �ظ�2,3 retryTime��.���û�еõ����,����nullֵ.<br>
	 * ע��: frameInterval(Ĭ��100)��retryTime(Ĭ��3),����ͨ��setParam�޸�.<br>
	 * @param correctValue: ��Ӧ��Ϣ�п��ܰ�������ȷֵ<br>
	 * @param errorValue: 	��Ӧ��Ϣ�п��ܰ����Ĵ���ֵ<br>
	 * @param type: �Ƿ���ʾ����ֵ:(type=0����ʾ,type=1��ʾ)(������)<br>
	 */
	private String receiveATResponse(String correctValue, String errorValue, int type) throws Exception{
		String recMsg = null;
		boolean findFlag = false;
		int retry_Time = retryTime;
		while (retry_Time>0) {
			recMsg = simCard_Serial.getReceiveMsg();								// �õ�ģ�黺��
			//System.out.println(recMsg);
			if (type == 1){System.out.println(retry_Time +":"+recMsg);}		// ������
			if (recMsg != null){
				if(recMsg.toUpperCase().indexOf(correctValue.toUpperCase())>=0){ 	// �ҵ���ȷ�ķ�����Ϣ
					simCard_Serial.clearReceiveMsg();								// ����ģ�黺��
					findFlag = true;	break;
				}else if(recMsg.toUpperCase().indexOf(errorValue.toUpperCase())>=0){ // �ҵ�����ķ�����Ϣ
					findFlag = false;	break;
				}
			}
			Thread.sleep(waitMillTime);												// �ȴ�һ��ʱ�� ���մ��ڷ��ص���Ϣ	
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
//			System.out.println("�����ź���:" + unit.watchSignal());
//			//String msg = "Test0123����ã�hello.123!!!������";
//			String msg = "������Ϣ\r\n������:������-1��\r\n�¶�:27.77��,���±���\r\nʪ��:42.07%RH,����\r\nʱ��:2010-05-07 16:55";
//			if(unit.sendMessage("13800571500", "13738195048", msg)){//13738195048 - ���Կ�:13634176292
//				System.out.println("���ŷ��ͳɹ�...");
//			}else{
//				System.out.println("���ŷ���ʧ��...");
//			}	
//			unit.closePort();
//		}else{
//			System.out.println("over...");
//		}
//	}	

}
