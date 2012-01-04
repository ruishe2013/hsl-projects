package com.htc.model.seriaPort;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForSetData;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.htc.common.FunctionUnit;
import com.htc.domain.EquipData;
import com.htc.domain.TLog;
import com.htc.model.MainService;
import com.htc.model.quartz.SerialPortQuartz;

/**
 * ��ʹ�÷���: <br>
 * 		��һ��:getInstance->InitForTempHumi->beginTask->writeReadSerial->endTask<br>
 * 		�ڶ���:getInstance->InitConfig->setMac/setAddr/readInfo/readPower/writeTime<br>
 * @ SerialPortSet.java
 * ���� : ���ڲ���. - ��һ��: ���ڹ��ܵĵ���. 
 * 		��ȡ���� �� ����Ӳ�������Ϣ ����
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-4     YANGZHONLI       create
 */
public class Level_First_Serial{
	
	private Log log = LogFactory.getLog(Level_First_Serial.class);

	
	//********************�������������õĳ���**************************//
	/**
	 * ��������Ĭ���¶�ֵ
	 */
	public static final int SET_DEFAULT_TEMP = 1;
	/**
	 * ��������Ĭ��ʪ��ֵ	
	 */
	public static final int SET_DEFAULT_HUMI = 2;
	/**
	 * ��������Ĭ��¶��ֵ
	 */
	public static final int SET_DEFAULT_DEWPOINT = 3;
	/**
	 * ����������¼��ʷ���ݵļ��,Ĭ��ֵ60��
	 */
	public static final int SET_REC_INTERVAL = 4;
	/**
	 * ���������ܼ�¼�������ʷ��������Ĭ��ֵ5000��
	 */
	public static final int SET_MAX_STORE_NUMBER = 5;
	/**
	 * ��������ʪ�ȵ���󱨾�ֵ
	 */
	public static final int SET_HUMI_MAX_VALUE = 6;
	/**
	 * ��������ʪ�ȵ���С����ֵ
	 */
	public static final int SET_HUMI_MIN_VALUE = 7;
	/**
	 * ���������¶ȵ���󱨾�ֵ
	 */
	public static final int SET_TEMP_MAX_VALUE = 8;
	/**
	 * ���������¶ȵ���С����ֵ
	 */
	public static final int SET_TEMP_MIN_VALUE = 9;
	/**
	 * ��������¶����󱨾�ֵ
	 */
	public static final int SET_DEWPOINT_MAX_VALUE = 10;
	/**
	 * ��������¶����С����ֵ
	 */
	public static final int SET_DEWPOINT_MIN_VALUE = 11;
	/**
	 * �Ƿ���������
	 * �ж��Ƿ������������(��ҪӦ����:��ͣ����ʱ,�����ִֹ�д�������Ľ���,��Ȼ���ڹر�ʱ,��Ȼ�д���ͨ��,���������)
	 */
	private boolean canDoTask = true;
	/**
	 * ��������״̬[true:�������� 	false: û������]
	 */
	private boolean runningFlag = false;
	
	// ��Ҫ���õĲ���
	private int flashTime;						// ������,��λ:��(ͣ��ʱ��,��������㹻ʱ�����ȫ������)
	private String portStr = "COM1";			// ���ں� --��:COM1
	private int baudrate;						// ������
	private int DEFAULT_AUTO_SEND_TIME = 5;		// ͨѶʧ�ܺ�,Ĭ�ϵ��ظ�����
	private int appendMillsec = 100;			// ͨ��ʱ���Ӻ�����
	
	//private static Level_First_Serial first_Level = new Level_First_Serial();	// ����
	private CommonDataUnit commonDataUnit;
	private Level_Second_Serial second_Level;
	private MainService mainService;
	private SerialPortQuartz serialPortQuartz;
	//ע��service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setSecond_Level(Level_Second_Serial second_Level) {
		this.second_Level = second_Level;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setSerialPortQuartz(SerialPortQuartz serialPortQuartz) {
		this.serialPortQuartz = serialPortQuartz;
	}
	
	/**
	 *���캯�� 
	 */
	public Level_First_Serial(){
		//second_Level = new Level_Second_Serial();
		// runningFlag = false;
	}

	/**
	 * @describe: ��ȡ������
	 * @date:2009-11-5
	 */
//	public static Level_First_Serial getInstance() {
//		return first_Level;
//	}
	
	/**
	 * @describe: ������������,���������н��<br>	
	 * @return  0:��������<br> 
	 * 			1:û�п��õ�����(˵��û���������)<br>
	 * 			2:�������ӳ���<br>
	 * @date:2010-1-29
	 */
	public int startRunSerial(){
		int rsint = 0;
		boolean bool = true;
		// ��ȡ���������б����
		if (bool){ bool = commonDataUnit.getEquiList().size() >0;}
		if (!bool){
			rsint = 1;	// ����������
		}else{
			// 1: ������
			int baudRateNo = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.BAUDRATE_NUMBER));
			// 2: ˢ��ʱ��( ��λΪ��)
			int flashTime = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
			// 3: ����ͨ�Ŵ���ʱ, �ط��Ĵ���(Ĭ��5��)
			DEFAULT_AUTO_SEND_TIME = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.SERI_RETRY_TIME));
			// 4: ����ͨ��ʱ,һ��ͨ�Ÿ��ӵĺ�����(��Ҫ���ںͱ��Ӳ������,�赱�ĵ������ֵ,��С��֡��Ƶ��)(Ĭ��100)
			appendMillsec = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.SERI_ADDITION_TIME));		
			// 5: ���Դ����Ƿ����
			if (initForTempHumi(baudRateNo, flashTime) == 1){
				// ���ô������ݲɼ���־,false:û�п�ʼ�ɼ�����
				commonDataUnit.setEqDoingData(false);
				beginTask();
			}else{
				rsint = 2;
			}			
		}
		return rsint;
	}
	
	/**
	 * @describe:�رմ��ں� ��ͣ����
	 * @return: true: ��ͣ�ɹ� false: ��ͣʧ��
	 * @date:2010-1-29
	 */
	public boolean endRunSerial(){
		boolean rsBool = true;
		if (runningFlag){ //�����������вŹر�
			rsBool = pauseAllTask();// ��ͣ����
		}
		return rsBool;
	}
	
	//****************.��������״̬.��ʼ�͹ر�����.���,�򿪺͹رն˿�.**********************************//
	/**
	 * @describe: Quartz���ȷ�ʽ:��ʼ�������.
	 * @date:2009-11-5
	 */
	public boolean beginTask() {
		mainService.packTlog(TLog.SERIAL_LOG, "����[" + portStr+ "]��ʼ����...");
		
		boolean rsBool = true;
		
		rsBool = openPort();
//		rsBool = true;	// --������
//		flashTime = 10;	// --������
		if (rsBool){
//			SerialPortQuartz serialPortQuartz = SerialPortQuartz.getInstance();
			// ����"��ʱ��������"��������
			serialPortQuartz.setInstantDataCron(flashTime);
			//	Quartz���ȷ�ʽ:��ʼ�������.
			serialPortQuartz.runTask();
			//��������״̬
			runningFlag = true;
			canDoTask = true;
		}else{
			// �ڶമ�ڻ�����, ���ֲ���û�гɹ���,Ҫ�رմ���,����Ӱ�����������������ڵĲ���
			closePort();
		}
		return rsBool;
	}
	
	/**
	 * @describe: Quartz���ȷ�ʽ:��ͣ����,�رմ���.
	 * @return: true: ��ͣ�ɹ�  false: ��ͣʧ��
	 * @date:2009-11-5
	 */
	public boolean pauseAllTask() {
		boolean rsBool = true;
		
		// ������������ͣ״̬,���ܲɼ�����
		canDoTask = false;
		// ˢ��ʱ��( ��λΪ��)
		int flashTime = Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.DATA_FLASHTIME));
		// ���Դ���
		int reTryTime = 6;  
		
		// ����������ڲɼ�����,��ȴ��ɼ����
		while(commonDataUnit.isEqDoingData()){
			if (reTryTime == 0){break;}
			try {
				Thread.sleep(flashTime * 500);
			} catch (InterruptedException e) {
				log.error("��ͣ����,ʱ��ͣ��ʱ����:" + e.getMessage());
			}
			reTryTime --;
		}
		if (commonDataUnit.isEqDoingData()){ // ��ͣʧ��,Ϊ�˱�֤������������,���ûָ������־
			mainService.packTlog(TLog.SERIAL_LOG, "����[" + portStr+ "]��ͣ����ʧ��...");
			canDoTask = true;
			rsBool =false;
		}else{								 // ��ͣ�ɹ�
			mainService.packTlog(TLog.SERIAL_LOG, "����[" + portStr+ "]��ͣ����...");
			// Quartz���ȷ�ʽ:�����������.
			serialPortQuartz.pauseAllTask();
			// ���ô�������״̬Ϊ:�ر�
			runningFlag = false;			
			// �رմ���
			closePort();
		}
		return rsBool;
	}
	
	/**
	 * @describe: Quartz���ȷ�ʽ:��������,�رմ���.
	 * @date:2009-11-5
	 */
	public void endTask() {
		// MainService mainService = new MainService();
		mainService.packTlog(TLog.SERIAL_LOG, "����[" + portStr+ "]��������...");
		
		// Quartz���ȷ�ʽ:�����������.
		serialPortQuartz.endTask();
		// ��������״̬
		runningFlag = false;
		// �رմ���
		closePort();
	}
	
	/**
	 * @describe: ��鴮���Ƿ���������,�Լ�������û�л�λ��
	 * @date:2009-11-22
	 */
	public void checkRunningPort(){
		//��ȡfinal_Serialʵ��--ɨ��˿�����,���������
		List<String> portList = Level_Final_Serial.getAllComPorts();
		if (portList.size() <= 0){
			// û�з����κδ���
		}else{
			// ����ɨ��,�鿴���ʵĴ���,���������������,�ȹر�
			boolean openFlag = second_Level.isPortOpen();
			if (openFlag){
				closePort();
			}
			log.info("�л����ں�,����ɨ�迪ʼ...");
			// ����ɨ�������ͨ��
			for (String str : portList) {
				this.portStr = str;	// �˿��ַ���
				// �ڶ�ȡ��ʪ������֮ǰ,����Ԥ������,�鿴������ͨ״̬ 
				if (testForTempHumi() == 1){
					break;
				}
			}// end for
			log.info("�л����ں�,����ɨ�����...");
		}
	}
	
	/**
	 * @describe: �򿪶˿�	
	 * @return: true : �ɹ�    false : ʧ��
	 * @date:2009-11-7
	 */
	public boolean openPort() {
		
		boolean rsBool = true;
		// ��ʼ������
		second_Level.initialize(portStr, baudrate, appendMillsec);
		// �򿪴���
		rsBool = second_Level.openPort();
		
		return rsBool;
	}
	
	/**
	 * @describe: �رն˿�
	 * @date:2009-11-5
	 */
	public void closePort() {
		second_Level.closePort();
	}
	
	//****************************��ȡ ��������ʪ������*******************************************//
	/**
	 * @describe: ��ȡ ��������ʪ������, ��ʼ�����ڲ���, ��������״̬.
	 * @param baudrate ������
	 * @return -1��ʾ���ɹ�. 1��ʾ�ɹ�
	 * @date:2009-11-6
	 */
	public int initForTempHumi(int baudrate, int flashTime) {
		int rsInt = -1;
		this.flashTime = flashTime; 	// ������,��λ:��
		this.baudrate = baudrate; 		// ������
		
		//��ȡfinal_Serialʵ��--ɨ��˿�����,���������
		List<String> portList = Level_Final_Serial.getAllComPorts();
		if (portList.size() > 0){
			for (String str : portList) {
				this.portStr = str;	// �˿��ַ���
				// �ڶ�ȡ��ʪ������֮ǰ,����Ԥ������,�鿴������ͨ״̬ 
				if (testForTempHumi() == 1){
					rsInt = 1 ;
					break;
				}
			}// end for
		}
		return rsInt;
	}
	
	/**
	 * @describe: �ڶ�ȡ��ʪ������֮ǰ,����Ԥ������,�鿴������ͨ״̬	
	 * @return: ����1������Գɹ���-1��ʾ����ʧ��(����û������������û�к��ʵĴ���)
	 * @date:2009-11-7
	 */
	public int testForTempHumi(){
		int rsFlag = -1;
		int address, equipmentId;
		
		// ������ǰ���õ������б�, ���Դ��ڵ���ͨ״̬
		for (EquipData equipData : commonDataUnit.getEquiMap().values()) {
			address = equipData.getAddress();
			equipmentId = equipData.getEquipmentId();
			
			// 1: �򿪶˿�
			if (openPort() == true){
				// 2: ���ڶ�д
				int dataLen = 3; //(3:�¶�,ʪ��,¶��4������)(4:�¶�,ʪ��,¶��,��ѹ4������) 
				rsFlag = writeReadSerial(address, equipmentId, Level_Second_Serial.READ_TEST_DATA, dataLen);
				// 3: �رմ���
				closePort();
			}
			// �õ���ȷ��֡,˵���ҵ�Ŀ�괮��
			if (rsFlag  == Level_Final_Serial.Serial_right_Frame){
				rsFlag = 1;
				break;
			}else{
				rsFlag = -1;
			}
		}// end for
		
		return rsFlag;
	}
	
	/**
	 * @describe: ��ȡ������ʪ������,Ȼ�󱣴浽������.	
	 * @param address ������ַ
	 * @param equipmentId ��������ID
	 * @param readType ���ڶ�д��������.ֻ��READ_USEFULL_DATAʱ�Ű����ݱ��ֵ����ݿ�,����ɾ����Ӧ��ʱ����.
	 * @param dataLen(3:�¶�,ʪ��,¶��4������)(4:�¶�,ʪ��,¶��,��ѹ4������) 
	 * @return: ����֡�����. ֻ�з���Serial_right_Frame(ֵΪ1),Ϊ�����ɹ���֡,������Ϊʧ��֡
	 * @date:2009-11-7
	 */
	public int writeReadSerial(int address,int equipmentId, int readType, int dataLen) {
		int runFlag = 0;
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		
		while (sendTimes > 0){
			try {
				
//				char[] rsByte = {1,3,6,20,201,114,117,110,159,152,219};			// -- ������
//				second_Level.updateDataBean(address, equipmentId, rsByte, 1);	// -- ������
//				runFlag = 1;													// -- ������
				
				// 1 �жϴ����Ƿ��;û�д�,����һ�δ򿪴��ڵĲ���;�ɹ��� ����"��ȡ������ʪ������"�Ĵ�������
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if (openFlag){
					second_Level.writeToGetTempHumi(address, dataLen);
					runFlag = second_Level.readByte(address,equipmentId, readType, dataLen);
				}else{ // ���ڷ��ʴ���,�����дӷ�
					sendTimes = 0;
					break;
				}
			} catch (Exception e) {
				// closePort();
				e.printStackTrace();
				
				log.error("address:"+address+" equipmentId:"+equipmentId + 
						" �Ӵ��ڶ�ȡ������ʪ������ʱ����"+e.getMessage());
				//MainService mainService = new MainService();
				mainService.packTlog(TLog.ERROR_LOG, "�������ݶ�ȡʧ��"+"��ַ:"+address+
						"  ������Ϣ:"+e.getMessage());
			}
			// ͨѶ����,����ѭ��,�����ط�
			if (runFlag == Level_Final_Serial.Serial_right_Frame){
				break;
			}
			sendTimes --;
		}
		
		// �������DEFAULT_AUTO_SEND_TIME���ط���,���Ƕ�֡,��ɾ����ʱ����,�ﵽ��̬��ʾЧ��.
		if (sendTimes ==  0){
			// ֻ��readType = READ_USEFULL_DATAʱ�ż��������
			if (readType == Level_Second_Serial.READ_USEFULL_DATA){
				second_Level.saveEmptyData(address, equipmentId);
			}
		}
		
		return runFlag;
	}
	
	//****************************���� ��  ��ȡ ����Ӳ������*******************************************//
	
	/**
	 * @describe: ����Ӳ������ʱ,�Զ���ʼ������, �������κζ���
	 * @param baudrate ������
	 * @date:2009-11-5
	 */
	public void initConfigVoid(int baudrate) {
		this.baudrate = baudrate; 	//������
	}
	
	/**
	 * @describe: ɨ�贮��. ����Ӳ������ʱ, ��ʼ�����ڲ���
	 * @param baudrate ������
	 * @return true: ��������   false: ���Ӵ���
	 * @date:2009-11-7
	 */
	public boolean initConfig(int baudrate){
		boolean linkState = false ;
		this.baudrate = baudrate; 			// ������

		//��ȡfinal_Serialʵ��--ɨ��˿�����,���������
		List<String> portList = Level_Final_Serial.getAllComPorts();
		if (portList.size() > 0){
			for (String str : portList) {
				this.portStr = str;			// �˿��ַ���
				
				// �򿪶˿�
				if (openPort() == true){
					// ���Ϳ����ݵ�Bean. �൱�ڷ����˹㲥��Ϣ.
					// ��ֻ����һ������,������. �����˹���, ����޷�Ԥ�� 
					// ��ͨѶ��ַ������汾��	-- ����Ӳ������,���Դ�����ͨ״̬
					// �رմ���
					linkState = readInfo(new BeanForSetData()) != null;
					if (linkState == true){break;}
					//closePort();//����Ҫ�رմ�����,��readInfo���Ѿ��ر�
				};
			}// end for
		}
		return linkState;
	}
	
	/**
	 * @describe: 1: ���û�����
	 * @param serialSetDataBean Ӳ������
	 * @return true:�ɹ� 	false:ʧ��
	 * @date:2009-11-5
	 */
	public boolean  setMac(BeanForSetData serialSetDataBean){
		boolean rsBool = false;
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		
		while (sendTimes > 0){
			try {
				// �жϴ����Ƿ��;û�д�,����һ�δ򿪴��ڵĲ���;�ɹ��� ����"���û�����"�Ĵ�������
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.setMac(serialSetDataBean);//����
					int dataLen = 4; // (3:�¶�,ʪ��,¶��4������)(4:�¶�,ʪ��,¶��,��ѹ4������)
					rsBool = Level_Final_Serial.Serial_right_Frame == 
						second_Level.readByte(-1, -1, Level_Second_Serial.READ_USEFULL_MAC, dataLen);//����
				}
			} catch (Exception e) {
				//throw e;
				rsBool = false;
				log.error("���û�����ʱ����:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			// ͨѶ����,����ѭ��,�����ط�
			if (rsBool){
				break;
			}		
			sendTimes --;
		}
		return rsBool;
	}
	
	/**
	 * @describe: 2:���õ�ַ
	 * @param serialSetDataBean	 Ӳ������
	 * @param address �µ�ַ
	 * @return true:�ɹ� 	false:ʧ��
	 * @date:2009-11-5
	 */
	public boolean setAddr(BeanForSetData serialSetDataBean, int address) {
		boolean rsBool = false;
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		
		while (sendTimes > 0){
			try {
				// �жϴ����Ƿ��;û�д�,����һ�δ򿪴��ڵĲ���;�ɹ��� ����"���õ�ַ"�Ĵ�������
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.setAddr(serialSetDataBean, address);//����
					int dataLen = 4; // (3:�¶�,ʪ��,¶��4������)(4:�¶�,ʪ��,¶��,��ѹ4������)
					rsBool = Level_Final_Serial.Serial_right_Frame == 
						second_Level.readByte(-1, -1, Level_Second_Serial.READ_USEFULL_MAC, dataLen);//����
				}
			} catch (Exception e) {
				//throw e;
				rsBool = false;
				log.error("���õ�ַʱ����:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			// ͨѶ����,����ѭ��,�����ط�
			if (rsBool){
				break;
			}		
			sendTimes --;
		}
		return rsBool;
	}
	
	/**
	 * @describe: 3:ͨ�����ţ���ͨѶ��ַ������汾��
	 * @param serialSetDataBean Ӳ������
	 * @date:2009-11-5
	 */
	public BeanForSetData readInfo(BeanForSetData serialSetDataBean) {
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		BeanForSetData rsData = null;
		char [] rs = null;
		
		while (sendTimes > 0){
			try {
				// �жϴ����Ƿ��;û�д�,����һ�δ򿪴��ڵĲ���;�ɹ��� ����"��ͨѶ��ַ������汾��"�Ĵ�������
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.readInfo(serialSetDataBean);//����
					rs = second_Level.readByteNoDB(); //����
				}
			} catch (Exception e) {
				//throw e;
				rsData = null;
				log.error("��ͨѶ��ַ������汾��ʱ����--��ȡ��������:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			// ͨѶ����,����ѭ��,�����ط�
			if (rs != null){
				try {
					//System.out.println("����ǰ"+ FunctionUnit.bytesToHexString(rs));
					rsData = rs.length == 14 ? fillDataScript(rs) : null;
					//System.out.println(rsData.toString());
					//System.out.println("������" + rsData.getMCNo());
				} catch (Exception e) {
					log.error("��ͨѶ��ַ������汾��ʱ����--��������:" + e.getMessage()+"-=-"+e.toString());
				}
				break;
			}		
			sendTimes --;
		}		
		return rsData;
	}
	
	/**
	 * @describe: ��char��������SerialSetDataBean���ݽṹ
	 * @date:2009-11-5
	 */
	public BeanForSetData fillDataScript(char [] fillChar)throws Exception{
		String tempStr;
		BeanForSetData ssdBean = new BeanForSetData();
		//fillChar:FC 67 09    11 00(���ͺ�)  10 01(����) 88 21(��ˮ��)  	00(��ַ)       01 00(����汾��)	 38 BA(CRC) 
		tempStr = String.valueOf( FunctionUnit.reverseHexToDec((int)fillChar[3]) * 100 + FunctionUnit.reverseHexToDec((int)fillChar[4]) );
		ssdBean.setMCType(fillStringTo4(tempStr,4));	//���ͺ�
		tempStr = String.valueOf( FunctionUnit.reverseHexToDec((int)fillChar[5]) * 100 + FunctionUnit.reverseHexToDec((int)fillChar[6]) );
		ssdBean.setMCOrder(fillStringTo4(tempStr,4));//����
		tempStr = String.valueOf( FunctionUnit.reverseHexToDec((int)fillChar[7]) * 100 + FunctionUnit.reverseHexToDec((int)fillChar[8]) );
		ssdBean.setMCNo(fillStringTo4(tempStr,4));//��ˮ��
		tempStr = String.valueOf((int)fillChar[9]);
		ssdBean.setAddress(tempStr);//��ַ
		tempStr = String.valueOf((int)fillChar[10] + "." + (int)fillChar[11]);
		ssdBean.setVersion(tempStr);//����汾��
		return  ssdBean;
	}
	
	/**
	 * @describe: ��ʽ���ַ���,��֤������sizeλ����
	 * ����sizeλ,ǰ�油��. ��:str=21,siez=21 -> 0021	
	 * @param str ��Ҫ��ʽ���ַ���
	 * @param size ����
	 * @date:2009-11-14
	 */
	public String fillStringTo4(String str,int size){
		StringBuffer strbuf = new StringBuffer();
		int len = str.length();
		for (int i = len; i < size; i++) {
			strbuf.append("0");
		}
		strbuf.append(str);
		return strbuf.toString();
	}
	
	/**
	 * @describe 4:��ȡ����(1.5V�ĵ��)<br>
	 * 			��������: 1200 < v < 2000.<br>
	 * 			��������: 0 < v <= 1200 <br>
	 * 			�����þ�: v <=0 and v >= 2000<br> 
	 * @param addr Ҫ��ȡ����������ID
	 * @return ����-1��ʾû�ж�ȡ�ɹ�
	 * @date:2009-11-5
	 */
	public int readPower(int addr) {
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		int rsInt = -1;
		char [] rs = null;
		
		while (sendTimes > 0){
			try {
				// �жϴ����Ƿ��;û�д�,����һ�δ򿪴��ڵĲ���;�ɹ��� ����"��ȡ����"�Ĵ�������
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.readPower(addr);//����
					rs = second_Level.readByteNoDB(); //����
					//System.out.println(SerialService.bytesToHexString(rs));
				}
			} catch (Exception e) {
				// throw e;
				rsInt = -1;
				log.error("��ȡ����ʱ����:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
				
			// ͨѶ����,����ѭ��,�����ط�
			if (rs != null){
				rsInt = rs.length == 7 ? (rs[3] << 8) + rs[4] : -1;
				break;
			}		
			sendTimes --;
		}		
		return rsInt;
	}
	
	/**
	 * @describe:	5:�·�У��ʱ��
	 * @param type:1(ִ�������,�رմ���) type:2(ִ�������,���رմ���)
	 * @return:  true:�ɹ� 	false:ʧ��
	 * @date:2010-3-24
	 */
	public boolean writeTime(int type){
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		boolean rsBool = true;
		
		while (sendTimes > 0){
			try {
				// �жϴ����Ƿ��;û�д�,����һ�δ򿪴��ڵĲ���;�ɹ��� ����"�·�У��ʱ��"�Ĵ�������
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.writeTime();//����,������
				}
			} catch (Exception e) {
				//throw e;
				rsBool = false;
				log.error("�·�У��ʱ��ʱ����:" + e.getMessage()+e.toString());
			}finally{
				if (type == 1){
					closePort();
				}
			}
			// ͨѶ����,����ѭ��,�����ط�
			if (rsBool){
				break;
			}		
			sendTimes --;
		}			
		return rsBool;
	}
	
	/**
	 * @describe: 6: ����������ǰ�����ŵ���ʷ���ݵĸ���
	 * @param addr ������ַ
	 * @return ����-1��ʾû�ж�ȡ�ɹ� 
	 * @date:2009-11-24
	 */
	public int readCurrentStoreNumber(int addr){
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		
		int histroyCount = -1 ;
		char [] rs = null;
		while (sendTimes > 0){
			try {
				// �жϴ����Ƿ��;û�д�,����һ�δ򿪴��ڵĲ���;�ɹ��� ����"�·�У��ʱ��"�Ĵ�������
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					second_Level.readCurrentStoreNumber(addr);//����
					rs = second_Level.readByteNoDB(); //����
				}
			} catch (Exception e) {
				//throw e;
				log.error("����������ǰ�����ŵ���ʷ���� ʱ����--��ȡ��������:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			
			// ͨѶ����,����ѭ��,�����ط�
			if (rs != null){
				try {
					//System.out.println(FunctionUnit.bytesToHexString(rs));
					// �±�Ϊ4��5��Ԫ�ؾ�����Ҫ������
					histroyCount = rs.length == 7 ? (rs[4]<<8 + rs[5]) : -1;
					//System.out.println(rsData.toString());
				} catch (Exception e) {
					log.error("����������ǰ�����ŵ���ʷ����ʱ����--��������:" + e.getMessage()+"-=-"+e.toString());
				}
				break;
			}
			
			sendTimes --;
		}
		
		return histroyCount;
	}
	
	/**
	 * @describe: ������������(�ɶ���д����),����д�����	
	 * @param addr	������ַ
	 * @param type	д�����	����
	 * @param value	Ҫд���ֵ
	 * @param errorString �쳣ʱ��ʾ���ַ���
	 * @return: true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setInfoByAddress(int addr, int value, int type, String errorString){
		int sendTimes = DEFAULT_AUTO_SEND_TIME;
		boolean rsBool = true;
		
		while (sendTimes > 0){
			try {
				// �жϴ����Ƿ��;û�д�,����һ�δ򿪴��ڵĲ���;�ɹ��� ����"�·�У��ʱ��"�Ĵ�������
				boolean openFlag = second_Level.isPortOpen() ? true : openPort();
				if(openFlag){
					
					//����
					if (type == SET_DEFAULT_TEMP){
						second_Level.setDefaultTemp(addr, value);		// ��������Ĭ���¶�ֵ
					}else if(type == SET_DEFAULT_HUMI){
						second_Level.setDefaultHumi(addr, value);		// ��������Ĭ��ʪ��ֵ 
					}else if(type == SET_DEFAULT_DEWPOINT){
						second_Level.setDefaultDewPoint(addr, value);	// ��������Ĭ��¶��ֵ 
					}else if(type == SET_REC_INTERVAL){
						second_Level.setRecInterval(addr, value);		// ����������¼��ʷ���ݵļ��
					}else if(type == SET_MAX_STORE_NUMBER){
						second_Level.setMaxStoreNumber(addr, value);	// ���������ܼ�¼�����������
					}else if(type == SET_HUMI_MAX_VALUE){
						second_Level.setHumiMaxValue(addr, value);		// ��������ʪ�ȵ���󱨾�ֵ
					}else if(type == SET_HUMI_MIN_VALUE){
						second_Level.setHumiMinValue(addr, value);		// ��������ʪ�ȵ���С����ֵ
					}else if(type == SET_TEMP_MAX_VALUE){
						second_Level.setTempMaxValue(addr, value);		// ���������¶ȵ���󱨾�ֵ
					}else if(type == SET_TEMP_MIN_VALUE){
						second_Level.setTempMinValue(addr, value);		// ���������¶ȵ���С����ֵ
					}else if(type == SET_DEWPOINT_MAX_VALUE){
						second_Level.setDewPointMaxValue(addr, value);	// ��������¶����󱨾�ֵ
					}else if(type == SET_DEWPOINT_MIN_VALUE){
						second_Level.setDewPointMinValue(addr, value);	// ��������¶����С����ֵ
					}
					
					int dataLen = 4; // (3:�¶�,ʪ��,¶��4������)(4:�¶�,ʪ��,¶��,��ѹ4������)
					//����
					rsBool = Level_Final_Serial.Serial_right_Frame == 
						second_Level.readByte(-1, -1, Level_Second_Serial.READ_USEFULL_MAC, dataLen);
				}
			} catch (Exception e) {
				//throw e;
				rsBool = false;
				log.error( errorString + "ʱ����:" + e.getMessage()+e.toString());
			}finally{
				closePort();
			}
			
			// ͨѶ����,����ѭ��,�����ط�
			if (rsBool){
				break;
			}
			
			sendTimes --;
		}
		return rsBool;
	}
	
	/**
	 * @describe:	1- ��������Ĭ���¶�ֵ
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setDefaultTemp(int addr, String temp){
		int value = (int) (Float.valueOf(temp) * 100 + 27315);
		boolean rsBool = setInfoByAddress(addr, value, SET_DEFAULT_TEMP, "��������Ĭ���¶�ֵ");
		return rsBool;
	}
	
	/**
	 * @describe:	2- ��������Ĭ��ʪ��ֵ	
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setDefaultHumi(int addr, String humi){
		int value = (int) (Float.valueOf(humi) + 0);
		boolean rsBool = setInfoByAddress(addr, value, SET_DEFAULT_HUMI, "��������Ĭ��ʪ��ֵ	");
		return rsBool;
	}
	
	/**
	 * @describe:	3- ��������Ĭ��¶��ֵ
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setDefaultDewPoint(int addr, String dewPoint){
		int value = (int) (Float.valueOf(dewPoint) * 100 + 27315);
		boolean rsBool = setInfoByAddress(addr, value, SET_DEFAULT_DEWPOINT, "��������Ĭ��¶��ֵ");
		return rsBool;
	}
	
	/**
	 * @describe:	4- ����������¼��ʷ���ݵļ��
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setRecInterval(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_REC_INTERVAL, "����������¼��ʷ���ݵļ��");
		return rsBool;
	}
	
	/**
	 * @describe:	5- ���������ܼ�¼�����������
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setMaxStoreNumber(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_MAX_STORE_NUMBER, "������������¼������");
		return rsBool;
	}
	
	/**
	 * @describe:	6- ��������ʪ�ȵ���󱨾�ֵ
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setHumiMaxValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_HUMI_MAX_VALUE, "��������ʪ�ȵ���󱨾�ֵ");
		return rsBool;
	}
	
	/**
	 * @describe:	7- ��������ʪ�ȵ���С����ֵ
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setHumiMinValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_HUMI_MIN_VALUE, "��������ʪ�ȵ���С����ֵ");
		return rsBool;
	}
	
	/**
	 * @describe:	8- ���������¶ȵ���󱨾�ֵ
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setTempMaxValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_TEMP_MAX_VALUE, "���������¶ȵ���󱨾�ֵ");
		return rsBool;
	}
	
	/**
	 * @describe:	9- ���������¶ȵ���С����ֵ
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setTempMinValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_TEMP_MIN_VALUE, "���������¶ȵ���С����ֵ");
		return rsBool;
	}
	
	/**
	 * @describe:	10-��������¶����󱨾�ֵ
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setDewPointMaxValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_DEWPOINT_MAX_VALUE, "��������¶����󱨾�ֵ");
		return rsBool;
	}
	
	/**
	 * @describe:	11- ��������¶����С����ֵ
	 * @param addr	������ַ
	 * @param value	Ҫд���ֵ
	 * @return:		true:���óɹ� 	false:����ʧ��
	 * @date:2009-11-24
	 */
	public boolean setDewPointMinValue(int addr, int value){
		boolean rsBool = setInfoByAddress(addr, value, SET_DEWPOINT_MIN_VALUE, "��������¶����С����ֵ");
		return rsBool;
	}
	
	//---------����Ϊget,set����----------//
	public boolean isRunningFlag() {
		return runningFlag;
	}
	/**
	 * @describe: �ж��Ƿ������������(��ҪӦ����:��ͣ����ʱ,�����ִֹ�д�������Ľ���,��Ȼ���ڹر�ʱ,��Ȼ�д���ͨ��,���������)	
	 * @date:2010-4-9
	 */
	public boolean isCanDoTask() {
		return canDoTask;
	}	
	
	/**
	 * @describe: ����"��������"��
	 * @date:2009-11-8
	 */
//	public Map<Integer, BeanForPortData> getAddressData() {
//		return second_Level.getAddressData();
//	}
	
	/**
	 * @describe: ����"ʵʱ����"�õ���ʱ���ݼ�
	 * @date:2009-11-8
	 */
//	public List<Map<Integer, BeanForPortData>> getCurrentFlashData() {
//		return second_Level.getCurrentFlashData();
//	}
	
	/**
	 * @describe: �˿ڲ����ú����������������
	 * @date:2009-11-8
	 */
//	public void clearBarData(){
//		second_Level.clearBarData();
//	}
	
	/**
	 * @describe: ��ȡequipmentId��Ӧ����ʱ����("�˿ڲ���"�õ���ʱ���ݼ�)
	 * @param equipmentId
	 * @date:2009-11-8
	 */
//	public  BeanForPortData getDataWithAddress(int equipmentId) {
//		return second_Level.getDataWithAddress(equipmentId);
//	}

	public String getPortStr() {
		return portStr;
	}
	
}
