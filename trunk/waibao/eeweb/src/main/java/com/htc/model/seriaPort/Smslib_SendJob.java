package com.htc.model.seriaPort;

import java.util.*;

/**
 * 	������--���ڲ��� 
 * 
 * ���� : smslib ���Ͷ���,�Զ�ƥ�䴮�� <br>
 * ��ʹ�ý���:	��ȡʵ��:<code>getInstance</code>
 * 				��������:<code>startService</code>�Ѿ��������Ž����¼�<br>
 *				��������:<code>stopService</code><br>
 *				��ӡ������Ϣ:<code>smsInfo</code><br>
 *				
 *				���Ͷ���: <code>sendMessage</code><br>
 * ע������ : ��
 */
public class Smslib_SendJob{
	
	private static Smslib_SendJob smsSendJob = new Smslib_SendJob(); //���൥��
	/**
	 * ���ڶ�̬���Զ��Ŵ��ں�
	 */
	private Smslib_Serial smslib_test = new Smslib_Serial();
	/**
	 * ���Ͷ��ŵķ���
	 */
	private Smslib_Service smsService = new Smslib_Service();
	/**
	 * ���ŷ���ģ��ר�õĶ˿���
	 */
	private static String SMSAPPNAME = "sms_port";
	/**
	 * ������
	 */
	private int baudrate = 9600;
	/**
	 * �����ַ�(��:COM1)
	 */
	private String comStr = "com1";	
	/**
	 * ����ģ�����б�־
	 */
	private boolean sms_run_flag = false;
	
	/*---------------------------���Ժ��ʵĴ��ں�-------------------------------*/
	/**
	 * @describe: ��ȡSerialBeanl�൥��
	 * @date:2009-11-5
	 */
	public static Smslib_SendJob getInstance() {
		return smsSendJob;
	}	
	
	/**
	 * @describe: ���ò�����	�� �����ַ�
	 * @param baudrate: ������
	 * @param comStr: �����ַ�
	 * @date:2010-3-2
	 */
	private void initial(int baudrate, String comStr) {
		this.baudrate = baudrate;
		this.comStr = comStr;
	}

	/**
	 * @describe: ��̬����ʺ϶���ģ���-�����ַ�(��:COM1)
	 * @param passPort: ���ü��Ĵ��ں�
	 * @date:2009-11-22
	 */
	private String getRightComStr(String passPort){
		String rsCom = null;
		
		//��ȡfinal_Serialʵ��--ɨ��˿�����,���������
		List<String> portList = Level_Final_Serial.getAllComPorts();
		if (portList.size() <= 0){
			// û�з����κδ���
		}else{
			// ���ɨ�������ͨ��
			for (String portStr : portList) {
				// ��������Ҫ��⴮��,������
				if (passPort.toUpperCase().equals(portStr.toUpperCase())){continue;}
				// ���Դ��ڵ��Ƿ��ʺ϶���ģ��
				if (testSms(portStr)){
					rsCom = portStr;
					break;
				}
			}
		}
		return rsCom;
	}
	
	/**
	 * @describe: ���Դ��ڵ��Ƿ��ʺ϶���ģ��
	 * @param portStr: ���ں�. ��:COM3
	 * @return: null:ʧ�� ����:�ɹ� 
	 */
	private boolean testSms(String portStr){
		boolean rsBool = false;
		try {
			//System.out.println("���Դ���" + portStr);
			// ��  �򿪶˿�
			rsBool = smslib_test.openPort(portStr, baudrate, SMSAPPNAME);
			if (rsBool){
				// ��  д����
				String atCommand = "AT\r";		// ����ATָ��(�ӻ��з���\r) 
				char[] atOrder = atCommand.toCharArray();			
				smslib_test.writeByte(atOrder);
				
				// ��  ������(���ݵõ�������,�жϷ������ݵ���ͨ��{�����ַ�����OK��ʾ�ɹ�})
				rsBool = smslib_test.readByte();
				if (rsBool){
					System.out.println("�ҵ�" + portStr + ":����ģ�鴮��");
				}
				// �� �رմ���
				smslib_test.closePort();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsBool;
	}
	
	/**
	 * @describe: Ϊ���ͺͽ��ն�������׼��
	 * @return: true:�ɹ� false:ʧ��
	 * @date:2010-3-2
	 */
	private boolean readyToSendMsg(){
		boolean rsbool = false;
		rsbool = smsService.initial(comStr, baudrate, "0000");
		if (rsbool) rsbool = smsService.startService();
		return rsbool;
	}	
	
	/*---------------------------����,�رշ��� ���Ͷ���-------------------------------*/
	/**
	 * @describe: ���Զ���ģ��(���ں��Զ����,����Ҫ����)
	 * @param baudrate: ������
	 * @param passPort: ���ü��Ĵ��ں�
	 * @return: true:���ӳɹ�,����������״̬ false:ʧ�� 
	 * @date:2010-3-3
	 */
	public boolean startService(int baudrate, String passPort){
		boolean rsbool = false;
		String comName = getRightComStr(passPort); 		// ��ȡ���ʶ���ģ��� �����ַ�
		//String comName = "COM5";
		if (comName != null){
			smsSendJob.initial(baudrate, comName);		// ���ò����ʺʹ����ַ�
			rsbool = smsSendJob.readyToSendMsg();		// ׼�� - ok
			// ��֪���Ƕ���ģ���ǰ����,���ͨѶ�쳣,�͹رմ���
			if (rsbool == false){smslib_test.closePort();}
			sms_run_flag = rsbool;						// ���ö���ģ�����б�־
		}else{
			System.out.println("û���ҵ����ʵĴ��ں�");
		}
		return rsbool;
	}	
	
	/**
	 * @describe: ֹͣ����
	 * @return: true:�ɹ� false:ʧ��
	 * @date:2010-3-1
	 */	
	public boolean stopService(){
		sms_run_flag = false;
		return smsService.stopService();
	}	
	
	/**
	 * @describe: ��ָ����һ���ֻ�����,���Ͷ���
	 * @param phoneList �ֻ������б�
	 * @param message ��Ϣ����
	 * @return: true:�ɹ� false:ʧ��
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
	 * @describe: ��ָ����һ���ֻ�����,���Ͷ���
	 * @param phoneList �ֻ������б�
	 * @param message ��Ϣ����
	 * @return: true:�ɹ� false:ʧ��
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
	 * @describe: ��ӡsms��Ϣ	
	 * @date:2010-3-2
	 */
	public void printSmsInof() throws Exception{
		smsService.smsInfo();
	}
	
	/**
	 * @describe:	�ж�  ����ģ�����б�־<br>
	 * <code>true</code>: ��������<br>
	 * <code>false</code>: û������<br>
	 * @date:2010-3-3
	 */
	public boolean isSms_run_flag() {
		return sms_run_flag;
	}

	public static void main(String[] args) throws Exception{
		Smslib_SendJob smsSendJob = Smslib_SendJob.getInstance();	// ����ʵ��
		if (smsSendJob.startService(9600,"")){
			//smsSendJob.printSmsInof();
			List<String> phoneList = new ArrayList<String>();
			phoneList.add("10086");
			String message = "11"; // ��10086��һ����ѯ���Ķ���						
			smsSendJob.sendMessage(phoneList, message);
			// ���ն�����SmsService���Ѿ�ע��,InboundNotification��process�ᴦ��
			// �ն��ź�,Ĭ�ϻ�ɾ���յ��Ķ���,Ҳ����ͨ��setRec_msg_remove(boolean)�޸�
			Thread.sleep(30 * 1000);  						// һ���Ӻ�,�رն��ŷ���
			smsSendJob.stopService();
		}
	}
	
}
