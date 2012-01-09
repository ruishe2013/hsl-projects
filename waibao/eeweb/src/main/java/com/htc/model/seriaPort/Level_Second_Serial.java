package com.htc.model.seriaPort;

import gnu.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.htc.bean.BeanForPortData;
import com.htc.bean.BeanForSetData;
import com.htc.bean.BeanForSysArgs;
import com.htc.common.FunctionUnit;
import com.htc.common.CommonDataUnit;
import com.htc.domain.Data4Access;
import com.htc.domain.EquipData;
import com.htc.domain.Record;
import com.htc.domain.TLog;
import com.htc.model.ServiceAccess;
import com.htc.model.MainService;

/**
 * ��ʹ�÷���: <br>
 * 		getInstance->initialize->openPort-><br>
 * 		writeByte/readByte/setMac/setAddr/readInfo/readPower/writeTime<br>
 * 		->closePort->endTask<br>
 * @ Level_Four_SerialService.java
 * ���� : ���ڲ��� -- �ڶ���: ���ڹ��ܷ�װ .
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-7     YANGZHONLI       create
 */
public class Level_Second_Serial {
	
	//public static Level_Second_Serial second_Level = new Level_Second_Serial();
	
	private Log log = LogFactory.getLog(Level_Second_Serial.class);
	
	/*-------------���ڶ�д��������,�����ж��Ƿ�����ݱ��浽���ݿ�------------*/
	/**
	 * 	���ڶ�д��,�����浽���ݿ�.���Դ�����ͨ���õ�.
	 */
	public static final int READ_TEST_DATA = 1;
	/**
	 * ���ڶ�д��,���浽���ݿ�.ϵͳ�����������õ�.
	 */
	public static final int READ_USEFULL_DATA = 2;
	/**
	 * ��������������.û�ж�ȡ��ʪ��,���Բ����浽���ݿ�
	 */
	public static final int READ_USEFULL_MAC= 3;
	
	/*-----------------------�������---------------------------------------*/
	/**
	 * ��ȡ����. (1.5V�ĵ��),��������:1200< V < 2000.<br>
	 */
	public static final int POWER_1_5 = 2000;
	/**
	 * ��ȡ����. (3.6V�ĵ��),��������: > 3000.<br>
	 */
	public static final int POWER_3_6 = 3000;
	
	//��Ҫ���õĲ���
	int baudrate;					// ������
	
	//����Ҫ�趨�Ĳ���(��Ĭ��ֵ)
	int timeOut; 					// �ӳ�ʱ��(������)
	int dataBits; 					// ����λ
	int stopBits; 					// ֹͣλ
	int parity; 					// ��ż����
	int funCode; 					// ������	
	//int appendMillsec; 				// ���㷢�ͼ����---���Ӻ�����
	int bytes;						// ���㷢�ͼ����---�����ֽ���	

	// �Զ�����
	int frameInterval; 				// ���ݲ����ʣ����ݱ��ʺ����������Զ����÷��ͼ��
	
	// ��ʱ������
	private Map<Integer, Record> currentNewRecords;						// ��ǰ���µļ�¼,�����������浽���ݿ���
	
	private Map<Integer, PoweredValue> poweredTemperature;						// 
	private Map<Integer, PoweredValue> poweredHumidity;						// 
	
	private Map<Integer, BeanForPortData> addressData;					// ��������"�õ���ʱ���ݼ�
	
	//private Map<Integer, BeanForPortData> tempFlashData;				// "ʵʱ����"Flash���ݵĵ�Ԫ����
	//private List<Map<Integer,BeanForPortData>> currentFlashData;		// "ʵʱ����"�õ���ʱ���ݼ�-ʱ��һ��(������:���ݶ���)
	private static String SMSAPPNAME = "HtcSerialData";	
	
	private Level_Final_Serial final_Level;
	private CommonDataUnit commonDataUnit;
	private MainService mainService;
	private ServiceAccess serviceAccess;	
	//ע��service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setFinal_Level(Level_Final_Serial final_Level) {
		this.final_Level = final_Level;
	}
	public void setServiceAccess(ServiceAccess serviceAccess) {
		this.serviceAccess = serviceAccess;
	}
	
	// ���췽��
	public Level_Second_Serial() {
		timeOut = 60;						// �ӳ�ʱ��(������)
		dataBits = SerialPort.DATABITS_8;	// ����λ
		stopBits = SerialPort.STOPBITS_1;	// ֹͣλ
		parity = SerialPort.PARITY_NONE;	// ��ż����
		funCode = 3;						//	��ȡ��ǰ�Ĵ�����һ������������ֵ
		//appendMillsec = 150;				//	���Ӻ����� 150
		bytes = 20;							//  �������ֽ���
		currentNewRecords = new ConcurrentHashMap<Integer, Record>();
		poweredTemperature = new ConcurrentHashMap<Integer, PoweredValue>();
		poweredHumidity = new ConcurrentHashMap<Integer, PoweredValue>();
		addressData = new ConcurrentHashMap<Integer, BeanForPortData>();
		//currentFlashData = new CopyOnWriteArrayList<Map<Integer,BeanForPortData>>();
		//final_Level = new Level_Final_Serial();
	}

//	/**
//	 * @describe:  ��ȡ������
//	 * @date:2009-11-5
//	 */
//	public static Level_Second_Serial getInstance() {
//		return second_Level;
//	}

	//****************.��ʼ��.��������״̬.�򿪺͹رն˿�. start**********************************//

	/**
	 * @describe: ��ʼ������ʵ��
	 * @param portStr ���ں�. ��: COM1
	 * @param baudrate ������
	 * @param appendMillsec ���㷢�ͼ����---���Ӻ�����
	 * @date:2009-11-5
	 */
	public void initialize(String portStr, int baudrate, int appendMillsec) {
		final_Level.setAppname(SMSAPPNAME);
		final_Level.setPortName(portStr);
		final_Level.init(timeOut, baudrate, dataBits, stopBits, parity);
		this.baudrate = baudrate;
		this.frameInterval = Level_Final_Serial.getFrameInterval(appendMillsec, bytes, baudrate);
	}
	
	/**
	 * @describe:	���ش����Ƿ��	
	 * @return: true : ��    false : �ر�
	 * @date:2009-11-7
	 */
	public boolean isPortOpen() {
		return final_Level.isPortOpen();
	}
	
	/**
	 * @throws Exception 
	 * @describe: �򿪴���
	 * @date:2009-11-5
	 */
	public boolean openPort() {
		if (final_Level.initialize()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @describe: �رմ��ڣ��ͷ���Դ
	 * @date:2009-11-5
	 */
	public void closePort() {
		final_Level.closePort();
	}
	
	//****************.��ʼ��.��������״̬.�򿪺͹رն˿�. end  ****************************************//
	
	//----------------֡��ʽ���� start--------------//
//	 ��ַ��	������	�Ĵ�����ַ	�Ĵ������ݳ���	CRC���ֽ�	CRC���ֽ�
//	 1���ֽ�	1���ֽ�	2���ֽ�		2���ֽ�			1���ֽ�		1���ֽ�
	
//	 ���������£�
//	 	03	��ȡ����	��ȡ��ǰ�Ĵ�����һ������������ֵ
//	 	06	���õ�һ�Ĵ���	�����õĶ�����ֵд�뵥һ�Ĵ���
//	 �Ĵ�����ַ����
//	 	0x0000:��ǰʪ��ֵ�����ڣ�ʵ��ֵ*100��
//	 	0x0001: ��ǰ�¶�ֵ�����ڣ�ʵ��ֵ*100 + 27315��
//	 	0x0002:��ǰ¶��ֵ�����ڣ�ʵ��ֵ*100 + 27315��
//	 	0x0003:��ǰ�󱸵�ص�ѹ�����ڣ�ʵ��ֵ*1000���� ����ֵ1200
//	 	0x0005:��ǰ��ʷ���ݵĸ�����
	
//	�ɶ���д����
//		0x0010:�¶ȱ궨������ʵ��ֵ*100��
//		0x0011ʪ�ȱ궨������ʵ��ֵ*100��
//		0x0012:¶��궨������ʵ��ֵ*100��
//		0x0013:��¼ʱ��������ı�����Ĭ��ֵ60��
//		0x0014:��¼���ݸ�������趨ֵ��Ĭ��ֵ5000��
//		0x0015: ʪ�ȸ߱�������ֵ
//		0x0016: ʪ�ȵͱ�������ֵ
//		0x0017: �¶ȸ߱�������ֵ
//		0x0018: �¶ȵͱ�������ֵ
//		0x0019: ¶��߱�������ֵ
//		0x001a: ¶��ͱ�������ֵ
	
	//----------------֡��ʽ���� end--------------//
	
	/**
	 * @describe: ����������ַ,���ͻ�ȡ������ʪ�ȵĴ�������<br>
	 * @param address ������ַ
	 * @param funCode ������
	 * @param memHigh �Ĵ�����ַ:��λ
	 * @param menLow  �Ĵ�����ַ:��λ
	 * @param dataHigh ���ݳ���:��λ
	 * @param dataLow  ���ݳ���:��λ
	 * @date:2009-11-5
	 */
	public void writeByte(int address, int funCode, int memHigh, int menLow, int dataHigh, int dataLow) throws Exception{
		char[] rs = {(char)address, (char)funCode, (char)memHigh, (char)menLow, (char)dataHigh, (char)dataLow, 0, 0};
		rs = CalcCRC.getCrc16(rs);
		//System.out.println(FunctionUnit.bytesToHexString(rs));
		final_Level.writePort(rs);
		//ֹͣ���Ա�֤���ݷ��ͳɹ�
		Thread.sleep(frameInterval);
	}	
	
	//****************************��ȡ ��������ʪ������ start *******************************************//
	/**
	 * @describe: ����������ַ,���ͻ�ȡ������ʪ�ȵĴ�������-��Ҫ�Ƿ�����ȡ��ʪ������<br>
	 * ��ַ��	������	�Ĵ�����ַ	�Ĵ�������	CRC���ֽ�	CRC���ֽ�<br>
	 * 1���ֽ�	1���ֽ�	2���ֽ�		2���ֽ�		1���ֽ�		1���ֽ�<br>
	 * �Ĵ�����ַ����
	 * 0x0000:��ǰʪ��ֵ�����ڣ�ʵ��ֵ*100��<br>
	 * 0x0001: ��ǰ�¶�ֵ�����ڣ�ʵ��ֵ*100 + 27315��<br>
	 * 0x0002:��ǰ¶��ֵ�����ڣ�ʵ��ֵ*100 + 27315��<br>
	 * 0x0003:��ǰ�󱸵�ص�ѹ�����ڣ�ʵ��ֵ*1000���� ����ֵ1200<br>
	 * 0x0005:��ǰ��ʷ���ݵĸ�����<br>
	 * @param address ������ַ
	 * @date:2009-11-5
	 */
	public void writeByteForGet(int address,int memHigh, int menLow, int dataHigh, int dataLow) throws Exception{
		writeByte(address, 0x03, memHigh, menLow, dataHigh, dataLow);
	}
	
	/**
	 * @describe:����������ַ���ͻ�ȡ �¶�,ʪ�Ⱥ�¶�������
	 * @param address������ַ
	 * @param dataLen(3:�¶�,ʪ��,¶��4������)(4:�¶�,ʪ��,¶��,��ѹ4������)
	 * @date:2009-11-24
	 */
	public void writeToGetTempHumi(int address,int dataLen) throws Exception{
		writeByteForGet(address, 0, 0, 0, dataLen);
	}
	
	
	/**
	 * @describe: .��ȡ��ʪ������	.�����û�������		.�����õ�ַ���
	 * @param address ������ַ
	 * @param equipmentId ��������ID
	 * @param readType	���ڶ�д��������.ֻ��READ_USEFULL_DATAʱ�Ű����ݱ��ֵ����ݿ�.
	 * @param dataLen(3:�¶�,ʪ��,¶��4������)(4:�¶�,ʪ��,¶��,��ѹ4������)
	 * @return ����֡�����.   ֻ�з���Serial_right_Frame(ֵΪ1),Ϊ�����ɹ���֡,������Ϊʧ��֡
	 * @date:2009-11-5
	 */
	public int readByte(int address, int equipmentId, int readType, int dataLen) throws Exception{
		
		if ( final_Level == null){
			return -1;
		}
		
		// ��ȡ����
		char[] rsByte = final_Level.readPackData();
		if (rsByte != null){
			 //System.out.println(FunctionUnit.bytesToHexString(rsByte));
		}
		int rsState = checkReturn(rsByte, address);
		
		//���˲�������,���õ����ݽ��в���
		if (readType == Level_Second_Serial.READ_USEFULL_DATA){
			//֡�������,�����ݴ������ݿ�
			if ( rsState == Level_Final_Serial.Serial_right_Frame ) {
				updateDataBean(address, equipmentId, rsByte, rsState, dataLen);
			}else{
				if ((rsByte != null)&&(rsByte.length >= 11 )){//��ȷ��������1������
					// ����dataLen,��ʾ ��֡��¼ 
					int humi = 0,temp = 0, dew = 0, power = 0;
					if ( dataLen >= 1)
						humi = (rsByte[3] << 8) + rsByte[4];
					if ( dataLen >= 2)
						temp = ((rsByte[5] << 8) + rsByte[6] - 27315);
					if ( dataLen >= 3)
						dew = ((rsByte[7] << 8) + rsByte[8] - 27315);
					if ( dataLen >= 4)
						power = (rsByte[9] << 8) + rsByte[10];
					
					String str = "�¶�:" + temp + "  ʪ��:" + humi + "  ¶��:" + dew + "��ѹ: " + power;
					str = "��֡��ַ:"+ address +" ����֡����:" + FunctionUnit.bytesToHexString(rsByte) +	"����:" + str;
					log.info(str);
					//MainService mainService = new MainService();
					mainService.packTlog(TLog.SERIAL_LOG, str);
				}
			}
		}
		return rsState;
	}
	
	/**
	 * @describe: ����ȡ��ֵ: address:-1(�����ͷ�ֽ�)
	 * @param rsByte ������char����	
	 * @param address �����жϵ�ַ�Ƿ�һ��. ����Ҫʱ,��ֵΪ -1
	 * @return: ����CRC�����. ֻ�з���Serial_right_Frame(ֵΪ1),Ϊ�����ɹ���֡,������Ϊʧ��֡
	 * @date:2009-11-5
	 */
	public int checkReturn(char[] rsByte, int address){
		boolean rsOut = true;
		int rsState = Level_Final_Serial.Serial_right_Frame;
		
		//֡�ж�
		if (rsByte == null) {
			//�ж��Ƿ�֡
			rsState = Level_Final_Serial.Serial_Lost_Frame;
		}else{
			if (address != -1){
				//����ַͷ			
				rsOut = rsByte[0] == address;
			}
			if (rsOut == false) {
				rsState = Level_Final_Serial.Serial_wrong_Address;
			}else{
				//�Խ��յ����ݽ���crcУ�飬����Ƿ�ͨѶ����
				rsOut = CalcCRC.checkCrc16(rsByte);
				if (rsOut == false) {
					rsState = Level_Final_Serial.Serial_wrong_Frame;
				}
			}
		}
		return rsState;
	}
	
	//****************************��ȡ ��������ʪ������ end   *******************************************//
	
	//****************************���� ��  ��ȡ ����Ӳ������ start*****************************************//
	/**
	 * @describe: ������������<br>
	 * funCode:������<br>
	 * �·�����ͨѶ��ַ������:			֡ͷ��0xFB��	������(0x66)	�������ֽڸ���	������	CRC���ֽ�	CRC���ֽ�<br>
	 *									�·�����ΪҪ������ͨѶ��ַ��ģ��Ļ��ţ�6���ֽڣ���ͨѶ��ַ��1���ֽڣ�<br>
	 *									����:��0x55,0x55��ʾ���óɹ���0xff��0xff��ʾ���ò��ɹ�	<br>
	 * �����ţ�ͨѶ��ַ������汾�ŵ�����:	֡ͷ��0xFB��	������(0x67)	�������ֽڸ���	������	CRC���ֽ�	CRC���ֽ�<br>
	 *									�·�����Ϊģ��Ļ��ţ�6���ֽڣ�<br>
	 *									����:���ţ�6���ֽڣ���ͨѶ��ַ��1���ֽڣ�������汾�ţ�2���ֽڣ�<br>
	 * �·�����ģ����ŵ�����:			֡ͷ��0xFB��	������(0x68)	�������ֽڸ���	������	CRC���ֽ�	CRC���ֽ�<br>
	 *									�·�����ΪҪ�����õĻ��ţ�6���ֽڣ�<br>
	 *									����:0x55,0x55��ʾ���óɹ���0xff��0xff��ʾ���ò��ɹ� <br>
	 * @describe: ������������
	 * @param serialSetDataBean װ�� ���� ������bean
	 * @param funCode ������
	 * @param address ��ַ
	 * @date:2009-11-5
	 */
	public void sendMsgForMac(BeanForSetData serialSetDataBean, int funCode, int address) throws Exception{
		char [] rsout = reverseDecToHex(serialSetDataBean);
		if (address == -1){
			char[]	rs = {0xFB, (char) funCode, 6, rsout[0], rsout[1], rsout[2], rsout[3],rsout[4], rsout[5], 0, 0};
			 rs = CalcCRC.getCrc16(rs);
			 //System.out.println("send:"+FunctionUnit.bytesToHexString(rs));
			 final_Level.writePort(rs);	
			 rs = null;
		}else{
			char[]	rs = {0xFB, (char) funCode, 7, rsout[0], rsout[1], rsout[2], rsout[3],rsout[4], rsout[5], (char) address, 0, 0};
			rs = CalcCRC.getCrc16(rs);
			//System.out.println(FunctionUnit.bytesToHexString(rs));
			final_Level.writePort(rs);
			rs = null;
		}
		//ֹͣ���Ա�֤���ݷ��ͳɹ�
		Thread.sleep(frameInterval);
	}
	
	/**
	 * @describe: .��ȡ������Ϣ,���������ݴ洢
	 * @return ͨѶ����,����nullֵ;��������char��������
	 * @date:2009-11-5
	 */
	public char[] readByteNoDB() throws Exception{
		// ��ȡ����
		char[] rsByte = final_Level.readPackData();
		if (rsByte != null){
			//System.out.println("rece:" + FunctionUnit.bytesToHexString(rsByte));
		}
		int rsState = checkReturn(rsByte, -1);
		rsByte = rsState == Level_Final_Serial.Serial_right_Frame ? rsByte : null;
		return rsByte;
	}
	
	/**
	 * @describe: �·�У׼ʱ�������:	<br>
	 * 			֡ͷ��0xFB��	������	(0x65)	�������ֽڸ���	������	CRC���ֽ�	CRC���ֽ�<br>
	 * 			������Ϊ�꣬�£��գ�ʱ���֣��빲�����ֽ�;<br>
	 * 			�޷���<br>
	 *			�·�У��ʱ��	funCode:0x65<br>
 	 * @date:2009-11-5
	 */
	public void writeTime() throws Exception{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		char year,month,day,hour,minute,second ;
		year = (char) Integer.parseInt(String.valueOf(calendar.get(Calendar.YEAR)).substring(2, 4));
		month = (char) (calendar.get(Calendar.MONTH) + 1);
		day = (char) calendar.get(Calendar.DAY_OF_MONTH);
		hour = (char) calendar.get(Calendar.HOUR_OF_DAY);
		minute = (char) calendar.get(Calendar.MINUTE);
		second = (char) calendar.get(Calendar.SECOND);
		char[]	rs = {0xFB, 0x65, 6, year, month, day, hour, minute, second, 0, 0};
		rs = CalcCRC.getCrc16(rs);
		final_Level.writePort(rs);
		 
		//ֹͣ���Ա�֤���ݷ��ͳɹ�
		Thread.sleep(frameInterval);
	}
	
	/**
	 * @describe: ���õ�ַ	funCode:0x66
	 * @date:2009-11-5
	 */
	public void setAddr(BeanForSetData serialSetDataBean, int address) throws Exception{
		sendMsgForMac(serialSetDataBean, 0x66, address);
	}
	
	/**
	 * @describe: ͨ�����ţ���ͨѶ��ַ������汾��	funCode:0x67
	 * @date:2009-11-5
	 */
	public void readInfo(BeanForSetData serialSetDataBean) throws Exception{
		sendMsgForMac(serialSetDataBean, 0x67, -1);
	}
	
	/**
	 * @describe: ���û�����	funCode:0x68
	 * @date:2009-11-5
	 */
	public void setMac(BeanForSetData serialSetDataBean) throws Exception{
		sendMsgForMac(serialSetDataBean, 0x68, -1);
	}
	
	/**
	 * @describe: ��ȡ����. (1.5V�ĵ��)<br>
	 * 			��������:0< V < 2000.<br>
	 * 			v < 1200 : ��������<br>
	 * 			v <=0 and v >= 2000 : �����þ�<br>
	 * ��ַ��	������	�Ĵ�����ַ	�Ĵ�������	CRC���ֽ�	CRC���ֽ�<br>
	 * 1���ֽ�	1���ֽ�	2���ֽ�		2���ֽ�		1���ֽ�		1���ֽ�<br>
	 * �Ĵ�����ַ:00 03	�Ĵ�������:00 01<br>
	 * �󱸵�ص�ѹ�����ڣ�ʵ��ֵ*1000���� ����ֵ:1200  �޵�:2000.<br>
	 * @param addr ������ַ
	 * @throws Exception
	 * @date:2009-11-5
	 */
	public void readPower(int addr) throws Exception{
		writeByteForGet(addr, 0, 3, 0, 1);
	}
	
	/**
	 * @describe ��ȡǰ��ʷ���ݵĸ�����<br>
	 * ��ַ��	������	�Ĵ�����ַ	�Ĵ�������	CRC���ֽ�	CRC���ֽ�<br>
	 * 1���ֽ�	1���ֽ�	2���ֽ�		2���ֽ�		1���ֽ�		1���ֽ�<br>
	 * �Ĵ�����ַ: 0x0005:��ǰ��ʷ���ݵĸ�����
	 * ������:03
	 * @param addr ������ַ
	 * @date:2009-11-13
	 */
	public void readCurrentStoreNumber(int addr) throws Exception{
		writeByteForGet(addr, 0, 5, 0, 1);
	}
	
	//****************************���ò���****************************//
	
	/**
	 * @describe: ����������ַ,��������������������<br>
	 * ��ַ��	������	�Ĵ�����ַ	�Ĵ�������	CRC���ֽ�	CRC���ֽ�<br>
	 * 1���ֽ�	1���ֽ�	2���ֽ�		2���ֽ�		1���ֽ�		1���ֽ�<br>
	 * @param address ������ַ
	 * @param memHigh �Ĵ�����ַ:��λ
	 * @param menLow  �Ĵ�����ַ:��λ
	 * @param dataHigh ��������:��λ
	 * @param dataLow  ��������:��λ
	 * @date:2009-11-5
	 */
	public void writeByteForSet(int address, int memHigh, int menLow,int dataHigh, int dataLow) throws Exception{
		writeByte(address, 0x06, memHigh, menLow, dataHigh, dataLow);
	}	
	
	/**
	 * @describe:	��������Ĭ���¶�ֵ
	 * ������:06
	 * �Ĵ�����ַ:0x0010
	 * ��Ҫ���͵��¶�ֵ:ʵ��ֵ*100 + 27315
	 * @param addr	������ַ
	 * @param temp	ʵ���¶�ֵ
	 * @throws Exception:
	 * @date:2009-11-24
	 */
	public void setDefaultTemp(int addr, int temp) throws Exception{
		//int tempInt = (int) (temp * 100 + 27315);
		writeByteForSet(addr,0x00,0x10,temp&0xFFFF>>8,temp&0xFF);
	}
	
	/**
	 * @describe: ��������Ĭ��ʪ��ֵ	
	 * ������:06
	 * �Ĵ�����ַ:0x0011
	 * ��Ҫ���͵�ʪ��ֵ:ʵ��ֵ*100
	 * @param addr ������ַ
	 * @param humi ʵ��ʪ��ֵ	
	 * @throws Exception:
	 * @date:2009-11-24
	 */
	public void setDefaultHumi(int addr, int humi) throws Exception{
		//int humiInt = (int) (humi * 100);
		writeByteForSet(addr,0x00,0x11,humi&0xFFFF>>8,humi&0xFF);
	}
	
	/**
	 * @describe:	��������Ĭ��¶��ֵ
	 * ������:06
	 * �Ĵ�����ַ:0x0012
	 * ��Ҫ���͵�¶��ֵ:ʵ��ֵ*100 + 27315
	 * @param addr	������ַ
	 * @param dewPoint	ʵ��¶��ֵ
	 * @throws Exception:
	 * @date:2009-11-24
	 */
	public void setDefaultDewPoint(int addr, int dewPoint) throws Exception{
		//int dewPointInt = (int) (dewPoint * 100 +27315);
		writeByteForSet(addr,0x00,0x12,dewPoint&0xFFFF>>8,dewPoint&0xFF);
	}
	
	/**
	 * @describe: ����������¼��ʷ���ݵļ��,Ĭ��ֵ60(Ӳ����������д��)
	 * ������:06
	 * �Ĵ�����ַ 0x0013
	 * ��¼ʱ��������ı���.
	 * @param addr ������ַ
	 * @param interval: ��¼ʱ����
	 * @date:2009-11-14
	 */
	public void setRecInterval(int addr, int interval) throws Exception{
		writeByteForSet(addr,0x00,0x13,interval&0xFFFF>>8,interval&0xFF);
	}
	
	/**
	 * @describe: ���������ܼ�¼�������������Ĭ��ֵ5000(Ӳ����������д��)
	 * ������:06
	 * �Ĵ�����ַ 0x0014
	 * ��¼���ݸ�������趨ֵ.
	 * @param addr  ������ַ
	 * @param interval: �趨�����ֵ
	 * @date:2009-11-14
	 */
	public void setMaxStoreNumber(int addr, int maxNumber) throws Exception{
		writeByteForSet(addr,0x00,0x14,maxNumber&0xFFFF>>8,maxNumber&0xFF);
	}
	
	/**
	 * @describe:	��������ʪ�ȵ����ֵ,�������ֵ�ͱ���
	 * ������:06
	 * �Ĵ�����ַ 0x0015
	 * ʪ�ȸ߱�������ֵ
	 * @param addr ������ַ
	 * @param value ʪ�ȵ����ֵ
	 * @date:2009-11-14
	 */
	public void setHumiMaxValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x15,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	��������ʪ�ȵ���Сֵ,�������ֵ�ͱ���
	 * ������:06
	 * �Ĵ�����ַ 0x0016
	 * ʪ�ȵͱ�������ֵ
	 * @param addr ������ַ
	 * @param value ʪ�ȵ���Сֵ
	 * @date:2009-11-14
	 */
	public void setHumiMinValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x16,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	���������¶ȵ����ֵ,�������ֵ�ͱ���
	 * ������:06
	 * �Ĵ�����ַ 0x0017
	 * �¶ȸ߱�������ֵ
	 * @param addr ������ַ
	 * @param value �¶ȵ����ֵ
	 * @date:2009-11-14
	 */
	public void setTempMaxValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x17,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	���������¶ȵ���Сֵ,�������ֵ�ͱ���
	 * ������:06
	 * �Ĵ�����ַ 0x0018
	 * �¶ȵͱ�������ֵ
	 * @param addr ������ַ
	 * @param value �¶ȵ���Сֵ
	 * @date:2009-11-14
	 */
	public void setTempMinValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x18,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	��������¶��߱�������ֵ
	 * ������:06
	 * �Ĵ�����ַ 0x0019
	 * @param addr ������ַ
	 * @param value ¶��߱�������ֵ
	 * @date:2009-11-14
	 */
	public void setDewPointMaxValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x19,value&0xFFFF>>8,value&0xFF);
	}
	
	/**
	 * @describe:	��������¶��ͱ�������ֵ
	 * ������:06
	 * �Ĵ�����ַ 0x001a
	 * @param addr ������ַ
	 * @param value ¶��ͱ�������ֵ
	 * @date:2009-11-14
	 */
	public void setDewPointMinValue(int addr, int value) throws Exception{
		writeByteForSet(addr,0x00,0x1a,value&0xFFFF>>8,value&0xFF);
	}
	
	//****************************���� ��  ��ȡ ����Ӳ������  end****************************************//
	
	//****************************���ݲ����� start **************************************************//
	
	/**
	 * @describe: ��������bean,address��ʱ����
	 * @param address ������ַ
	 * @param equipmentId ��������ID
	 * @param rsByte ������ʪ�����ݵ�char����
	 * @param state ����֡�����.  ֻ�з���Serial_right_Frame(ֵΪ1),Ϊ�����ɹ���֡,������Ϊʧ��֡.
	 * @param dataLen(3:�¶�,ʪ��,¶��4������)(4:�¶�,ʪ��,¶��,��ѹ4������)
	 * @date:2009-11-5
	 */
	public void updateDataBean(int address,int equipmentId, char[] rsByte, int state, int dataLen){
		EquipData equip = commonDataUnit.getEquipByID(equipmentId);
//		if (addressData == null){
//			addressData = new HashMap<Integer, BeanForPortData>();  
//		}
		
		if (state == Level_Final_Serial.Serial_right_Frame){
			Date tempDate = new Date();
			
			// ����dataLen,��ʾ ��֡��¼ 
			int humi = 0,temp = 0, dew = 0, power = 0;
			if ( dataLen >= 1)
				humi = (int) ((rsByte[3] << 8) + rsByte[4] + equip.getHumiDev() * 100);
			if ( dataLen >= 2)
				temp = (int) (((rsByte[5] << 8) + rsByte[6] - 27315) + equip.getTempDev() * 100);
			if ( dataLen >= 3)
				dew = ((rsByte[7] << 8) + rsByte[8] - 27315);
			if ( dataLen >= 4)
				power = (rsByte[9] << 8) + rsByte[10];
			
			BeanForPortData dataBean =  new BeanForPortData();
			dataBean.setAddress(address);
			dataBean.setEquipmentId(equipmentId);
			dataBean.setState(state); //������������״̬
			dataBean.setTemp(temp);							
			dataBean.setHumi(humi);
			dataBean.setDewPoint(dew);
			dataBean.setPowerV(power);
			dataBean.setRecTime(new Date());
			dataBean.setRecLong(tempDate.getTime());
			dataBean.setMark(1);
			
			// ���浽��ʱ����
			saveToFlash(dataBean);
			
			//������ʱ�����б��flash��������
//			if (addressData.containsKey(equipmentId)){
//				addressData.remove(equipmentId);
//			}
//			addressData.put(equipmentId, dataBean);//�˿ڲ����ú���������
//			fillToTempFlash(equipmentId, dataBean);//flash��ʾ��
			
//			System.out.println("������ַ:" + equipmentId + "��ַ:" + address + " �¶�:"+ temp + 
//			" ʪ��:" + humi + " ¶��:" + dew+"��ѹ: " + power);
			
			//�������ݿ�  - [�¶�-ʪ��-eqID-��¼ʱ��]
			Record record = new Record();
			record.setTemperature(FunctionUnit.FLOAT_DATA_FORMAT.format( temp / (float) 100) );
			record.setHumidity(FunctionUnit.FLOAT_DATA_FORMAT.format( humi / (float) 100) );
			record.setEquipmentId(equipmentId);
			record.setRecTime(tempDate);
			record.setRecLong(tempDate.getTime());
			
			// ��access���ݿ⴫�ݵ�����
			record.setDsrsn(equip.getDsrsn());
			record.setLabel(equip.getPlaceStr()+"-"+equip.getMark());
			record.setEquitype(equip.getEquitype());
			record.setShowAccess(equip.getShowAccess());
			
			// �����ݼ�¼���з�Χ���,�Ա㷢�ֲ���������, ��ʱ����
			commonDataUnit.checkDataWarn(record, equip);
			// ����һ: �����ݲ��뼴ʱ���ݱ�(����)
			//MainService.getInstance().insertRecord(record);
			// ������: �����ݲ�����ʱ����,Ȼ�������������ݿ�
			saveToBatchInsert(record);
		}
	}
	
	/**
	 * @describe: flash��ʱ�����������
	 * @param equipmentId ��������ID
	 * @param dataBean ������ʪ�����ݵ����ݽṹ
	 * @date:2009-11-5
	 */
//	public void fillToTempFlash(int equipmentId, BeanForPortData dataBean){
//		if( (tempFlashData != null) && (tempFlashData.containsKey(equipmentId)) ){
//			//������������Flash���ݻ����,Ȼ������Flash��ʱ����
//			fillToRealFlash(tempFlashData);
//			tempFlashData.clear();
//		}
//		if (tempFlashData == null){
//			tempFlashData = new HashMap<Integer, BeanForPortData>();
//		}
//		//�����ݲ���Flash��ʱ����
//		tempFlashData.put(dataBean.getEquipmentId(), dataBean);
//	}
	
	/**
	 * @describe:	����ʱFalsh���ݲ��붯̬Flash�����б�
	 * @param tempFlashData:
	 * @date:2009-12-3
	 */
//	private synchronized void fillToRealFlash(Map<Integer, BeanForPortData> tempFlashData) {
//		// ��ȡ - ʵʱflash�����ʾ�ĵ���
//		int FLASH_FILL_DATA_MAX = Integer.parseInt(
//				CommonDataUnit.getSysArgsByKey(BeanForSysArgs.MAX_RS_COUNT_LINE));
//		int tempLen = currentFlashData.size() - FLASH_FILL_DATA_MAX;
//		// ɾ����ǰ��1�����߼�������,�ﵽ���µ�Ч��
//		for (int i = 0; i <= tempLen; i++) {
//			currentFlashData.remove(0);
//		}
//		// ���������
//		Map<Integer, BeanForPortData> flashData = new HashMap<Integer, BeanForPortData>();
//		Set<Entry<Integer, BeanForPortData>> entrys = tempFlashData.entrySet();
//		int equipId ;
//		BeanForPortData bean;
//        for(Entry<Integer, BeanForPortData> entry: entrys){
//            equipId = entry.getKey();
//            bean = null;	bean = entry.getValue();
//            flashData.put(equipId, bean);
//        }
//		currentFlashData.add(flashData);
//	}
	
	/**
	 * @describe:	���������
	 * @param address ������ַ - ����,��ʱ����
	 * @param equipmentId ��������ID
	 * @date:2010-3-22
	 */
	public void saveEmptyData(int address,int equipmentId){
		BeanForPortData record = new BeanForPortData();
		record.setMark(0); // 0������,1������
		record.setAddress(address);
		record.setEquipmentId(equipmentId);
		saveToFlash(record);
	}
	
	/**
	 * @describe:	���ݲ�����ʱ����,�ȵ���һ���ظ�EquipmentIdʱ,Ȼ��ѻ�������������������ݿ�(������ʵʱ������)
	 * @param record:
	 * @date:2010-3-22
	 */
	private void saveToFlash(BeanForPortData record) {
		if (addressData.containsKey(record.getEquipmentId())){
			// �������޸ĵ���ʱ���ݿ�,�ṩ��������ʵʱ������
			//MainService.getInstance().updateMinRecord(addressData);
			mainService.updateMinRecord(addressData);
			// ������
			addressData.clear();			
		}
		//�����ݲ��뻺��
		addressData.put(record.getEquipmentId(), record);		
	}

	static class PoweredValue {
		int power = 1;
		String value;
		public PoweredValue(String value) {
			super();
			this.value = value;
		}
	}
	
	/**
	 * @describe: ���ݲ�����ʱ����,�ȵ���һ���ظ�EquipmentIdʱ,Ȼ��ѻ�������������������ݿ�
	 * @param record ������ʪ����Ϣ�����ݵ�Ԫ
	 * @date:2009-11-8
	 */
	public void saveToBatchInsert(Record record ){
		if (currentNewRecords.containsKey(record.getEquipmentId() ) ){
			try {
				long currentMinute = 0;
				int equipId = record.getEquipmentId();
				// ͬһ���ӵ���������ƽ��������,Ȼ����뻺��,֮��ȴ���һ��ʱ��,�ٴ������ݿ�
				if (FunctionUnit.getTypeTime(record.getRecTime(), Calendar.MINUTE) ==
					FunctionUnit.getTypeTime(currentNewRecords.get(equipId).getRecTime(), Calendar.MINUTE) ){
					String statStr = "";
					
				//	getStatFloatAvg();
					
					PoweredValue temperatureValue = poweredTemperature.get(equipId);
					statStr = FunctionUnit.getStatFloatAvg(temperatureValue.value, temperatureValue.power, record.getTemperature(), 1);
					temperatureValue.value = statStr;
					temperatureValue.power ++;
				//	statStr = FunctionUnit.getStatFloat(record.getTemperature(), 
				//			currentNewRecords.get(equipId).getTemperature(), FunctionUnit.STAT_AVG, 1);
					record.setTemperature(statStr);
					
					PoweredValue humidityValue = poweredHumidity.get(equipId);
					statStr = FunctionUnit.getStatFloatAvg(humidityValue.value, humidityValue.power, record.getHumidity(), 1);
					humidityValue.value = statStr;
					humidityValue.power ++;
/*					statStr = FunctionUnit.getStatFloat(record.getHumidity(), 
							currentNewRecords.get(equipId).getHumidity(), FunctionUnit.STAT_AVG, 0);*/
					record.setHumidity(statStr);
				}else{
					// ��Access�洢���ܿ���������´���
					if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_ACCESS_STORE)) == 2){
						//if(commonDataUnit.isAccessLinkState()){ // ��ʱ����������ͨ��
							// Ϊ�˴ﵽÿ��10������access���ݿ⴫��һ������,������÷�����ȡ�������ж�
							currentMinute = (new Date()).getTime();
							if (commonDataUnit.isAccessDataEmpty()) {// access���ݿ� �� ����Ϊ��
								serviceAccess.insertBatch(fillAccessData(currentNewRecords, 1));
								commonDataUnit.setAccessTimeFalg(currentMinute);
								// �ȵ� ͨѶ�����ݺ���Ҫ��ʾ�����ݸ������ʱ, �����ñ�־, �Ա�֤���ݵ�������
								if (commonDataUnit.getAccessLinkCount() == currentNewRecords.size()){
									//log.info("access��Ϣ-����:�����Ѿ��ռ���-------------");
									commonDataUnit.setAccessDataEmpty(false);
								}else{
									//log.info("access��Ϣ-����:���ݻ�û���ռ���-------------");
								}
							}else{
								if ( (currentMinute - commonDataUnit.getAccessTimeFalg()) >= 1000 * 60 * 10 ){ // ���10����
									// log.info("access��Ϣ:�������ݵ�access-------------");
									// ���� access ���ݿ��� ��Ϊ��(������)
									if (commonDataUnit.isAccessDataEmpty()){
										commonDataUnit.setAccessDataEmpty(false);
									}
									commonDataUnit.setAccessTimeFalg(currentMinute);
									serviceAccess.updataBatch(fillAccessData(currentNewRecords, 2));
								}
							}
						//}
					}
					
					//�����ݲ��뼴ʱ���ݱ�(����)
					//MainService.getInstance().insertRecordBatch(currentNewRecords);
					mainService.insertRecordBatch(currentNewRecords);
					// ������
					currentNewRecords.clear();
					
					poweredTemperature.clear();
					poweredHumidity.clear();
				}
				
//				// �����ݲ��뼴ʱ���ݱ�(����),Ȼ��������--������--��ʼ
//				MainService mainService = new MainService();
//				mainService.insertRecordBatch(currentNewRecords);
//				// ��Access�洢���ܿ���������´���
//				if (Integer.parseInt(CommonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_ACCESS_STORE)) == 2){
//					if(CommonDataUnit.isAccessLinkState()){ // ��ʱ����������ͨ��
//						if (CommonDataUnit.isAccessDataEmpty()) {// access���ݿ� �� ����Ϊ��
//							AccessService.getInstance().insertBatch(fillAccessData(currentNewRecords, 1));
//							CommonDataUnit.setAccessDataEmpty(false);
//						}else{
//							//���� access ���ݿ��� ��Ϊ��(������)
//							if (CommonDataUnit.isAccessDataEmpty()){
//								CommonDataUnit.setAccessDataEmpty(false);
//							}
//							AccessService.getInstance().updataBatch(fillAccessData(currentNewRecords, 2));
//						}	
//					}
//				}
//				currentNewRecords.clear();
//				// �����ݲ��뼴ʱ���ݱ�(����),Ȼ��������--������--���� 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//�����ݲ��뻺��
		currentNewRecords.put(record.getEquipmentId(), record);
		poweredTemperature.put(record.getEquipmentId(), new PoweredValue(record.getTemperature()));
		poweredHumidity.put(record.getEquipmentId(), new PoweredValue(record.getHumidity()));
	}
	
	/**
	 * @describe:	��װ��access���ݿ���Ҫ�� ����
	 * @param recordMap ��װ���ݶ���
	 * @param type ��װ����: 1:������ 2:�޸���
	 * @return:
	 * @date:2010-3-18
	 */
	public List<Data4Access> fillAccessData(Map<Integer, Record> recordMap, int type){
		List<Data4Access>  datas = new ArrayList<Data4Access>();
		Data4Access data4Access = null;
		Record record = null;
		String tempstr ;
		
		for (Entry<Integer, Record> rec : recordMap.entrySet()) {
			record = rec.getValue();
			if (record.getShowAccess() == 1){ // ֻ������Ҫ��ʾ��access�е�����
				data4Access = new Data4Access(); 
				
				tempstr =FunctionUnit.getDateToStr(record.getRecTime(),
						FunctionUnit.Calendar_END_SECOND, FunctionUnit.UN_SHOW_CHINESE);			
				
				data4Access.setStrDSRSN(record.getDsrsn());// ���� ���
				data4Access.setStrDateTime(tempstr);// ��¼ʱ��
				data4Access.setStrTemp(record.getTemperature());// �¶�
				data4Access.setStrHUM(record.getHumidity());// ʪ��
				
				data4Access.setStrAirPress("");// ѹ��
				if(type == 1){ // ��������ʱ,��Ҫ�ṩ��������,�޸�ʱֻҪһ����
					data4Access.setStrDSRName(record.getLabel());// ������ǩ
					//1,'��ʪ��';2,'���¶�';3,'��ʪ��'
					tempstr = record.getEquitype() == 1?"TH":record.getEquitype()==2?"T":"H";
					data4Access.setStrEquipmentType(tempstr);
				}
				datas.add(data4Access);
			}
		}
		return datas;
	}
	
	/**
	 * @describe: ������� - �˿ڲ����ú���������
	 * @date:2009-11-5
	 */
//	public void clearBarData(){
//		addressData.clear();
//	}	
	
	/**
	 * @describe: �쳣����,���Map�е�key=equipmentId��ֵ
	 * @param address ������ַ - ����,��ʱ����
	 * @param equipmentId ��������ID
	 * @param readType readType ���ڶ�д��������.ֻ��READ_USEFULL_DATAʱ��ɾ����Ӧ��ʱ����
	 * @date:2009-11-5
	 */
//	public void removeDataBean(int address,int equipmentId,int readType){
//		//���˲�������,���õ����ݽ��в���
//		if (readType == Level_Second_Serial.READ_USEFULL_DATA){
//			
//			//�����ʱ���ݼ� --"ʵʱ����"��"�˿ڲ���"�õ���ʱ���ݼ�
//			if (addressData != null){
//				//�ж����ݼ��Ƿ����keyֵ���еĻ����Ƴ�
//				if (addressData.containsKey(equipmentId)){
//					addressData.remove(equipmentId);
//				}
//			}
//			
//			//���Null��������ݼ� -- "ʵʱ����"�õ���ʱ���ݼ�
//			BeanForPortData dataBean = new BeanForPortData();
//			dataBean.setState(2);// ֡״̬:����֡
//			dataBean.setEquipmentId(equipmentId);
//			dataBean.setRecTime(new Date());
//			fillToTempFlash(equipmentId,dataBean);
//		}
//	}
	
	/**
	 * @describe: ����"��������"�õ���ʱ���ݼ�
	 * @date:2009-11-8
	 */
//	public Map<Integer, BeanForPortData> getAddressData() {
//		return addressData;
//	}
	
	/**
	 * @describe: ����"ʵʱ����"�õ���ʱ���ݼ�
	 * @date:2009-11-8
	 */
//	public List<Map<Integer, BeanForPortData>> getCurrentFlashData() {
//		return currentFlashData;
//	}

	/**
	 * @describe: ��ȡequipmentId��Ӧ����ʱ����("�˿ڲ���"�õ���ʱ���ݼ�)
	 * @param equipmentId ��������ID
	 * @date:2009-11-8
	 */
//	public BeanForPortData getDataWithAddress(int equipmentId) {
//		if (addressData != null){
//			return addressData.containsKey(equipmentId)?addressData.get(equipmentId):null;
//		}else{
//			return null;
//		}
//	}
	
	//****************************���ݲ����� end ****************************************************//
	
	/**
	 * @describe: ���븴��Ƭ��-(10���Ƶ��ַ�����ת����16���Ƶ�����.��12->0x18)
	 * @param serialSetDataBean Ӳ�����Ե����ݽṹ
	 * @date:2009-11-5
	 */
	public static char[] reverseDecToHex(BeanForSetData serialSetDataBean){
		char [] rs = new char[6];
		rs[0] = (char) Integer.parseInt(serialSetDataBean.getMCType().substring(0, 2), 16);
		rs[1] = (char) Integer.parseInt(serialSetDataBean.getMCType().substring(2, 4), 16);
		rs[2] = (char) Integer.parseInt(serialSetDataBean.getMCOrder().substring(0, 2), 16);
		rs[3] = (char) Integer.parseInt(serialSetDataBean.getMCOrder().substring(2, 4), 16);
		rs[4] = (char) Integer.parseInt(serialSetDataBean.getMCNo().substring(0, 2), 16);
		rs[5] = (char) Integer.parseInt(serialSetDataBean.getMCNo().substring(2, 4), 16);		
		return rs;
	}
	
}

