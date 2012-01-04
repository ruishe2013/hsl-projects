package com.htc.model.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.CommonDataUnit;
import com.htc.domain.PhoneList;
import com.htc.domain.SmsRecord;
import com.htc.model.MainService;
import com.htc.model.seriaPort.SimCard_Unit;
import com.htc.model.tool.ShortMessageUnit;

/**
 * @ TaskForInstantData.java
 * ���� : ��ȡ������ҵ����.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
//public class TaskForInstantData  implements Job {
public class TaskForSms  extends QuartzJobBean {
	
	private Log log = LogFactory.getLog(TaskForSms.class);
	private CommonDataUnit commonDataUnit;
	private SimCard_Unit simCard_Unit;
	private MainService mainService;
	
	private static int WAIT_MILLTIME = 5 * 1000; 	// ��ͼ�ж϶����Ƿ��������, ���εȴ�ʱ��(5��)
	private static int RETRY_TIMES = 3; 			// ��ͼ�ж϶����Ƿ��������, �ظ�����(3��)
	
	//ע��service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setSimCard_Unit(SimCard_Unit simCard_Unit) {
		this.simCard_Unit = simCard_Unit;
	}
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}	
	
	// ��Ҫһ���յĹ��캯�����������ڵ��ȵ�ʱ�������������캯����ʼ������
	public TaskForSms() {
	}

	// ���ն�����ҵ
	public void executeInternal(JobExecutionContext ctx) throws JobExecutionException {	
		// Ԥ����һ��: ����ģ�鴮�ڶ�Ӧ�Ĵ�����û�д�
		if (simCard_Unit.isRunFlag() == false){return;}
		// Ԥ���ڶ���: ���ű����Ƿ��(1:�ر� 2:��)
		if (Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.OPEN_SHORT_MESSAGE))==1){return;}
		// Ԥ��������:��֤ǰ�治�������ڴ�����ŵ�����
		if (commonDataUnit.isSmsDoingData() == false){				// ���ڴ����������
			commonDataUnit.setSmsDoingData(true);
		}else{														// �������ڴ���,�˳�
			log.info("��-0:����ʱ,ǰ��Ķ��Ŵ�������û�н���...");	
			return;
		}
		//log.info("��:�������ģ������...");	
		// Ԥ���������,������ʽ����.  
		
		// *******************ע��:   Ҫ���Ӷ��Ž��մ�����:ȡ��72��ע��,Ȼ����88�к����дҵ���߼�*********************
		
		// �� ��ȡ�������������,�ȵ����洦�� 
		//perReadMemory();	// Ϊ�˰���Ϣ��ʧ�������,Ԥ��������
		String newMemory = simCard_Unit.freeAndReturnMemory();
		
		// �ڴ�����Ҫ���͵Ķ����ж�
		String centerNo = commonDataUnit.getSysArgsByKey(BeanForSysArgs.SMS_CENTER_NUMBER);//�������ĺ���
		SmsRecord smsRecord = commonDataUnit.pollSimCard(); 
		while (smsRecord != null) {
			//���Ͷ���
			sendMessage(centerNo, smsRecord);
			// �Ѷ��ŷ�����Ϣ���浽���ݿ�
			mainService.insertSmsRec(smsRecord);	
			// ��һ����Ҫ���͵Ķ���
			smsRecord = commonDataUnit.pollSimCard();
		}
		
		// �۴�������Ϣ-1.����յ���Ч����Ϣ->2.���յĶ��ű��浽���ݿ�->3.��ѯ��Ϣ->4.���Ͷ���->5.���͵Ķ��ű��浽���ݿ�
//		if (!newMemory.equals("")){
//			// 	����һ�����ն�����ʾ�Ĵ�����Ϣ����
//			//	\r\n+CMT: ,24\r\n0891683108501705F0040D91683137185940F80008015031904243230454C854C8\r\n
//			// System.out.println("newMemory:"+newMemory);
//			List<SmsRecord> smsList = packSmsRecord(newMemory);
//			for (SmsRecord smsrecord : smsList) {
//					// ��������Ч�Ľ�������,Ҫ������д�����ҵ��
//				System.out.println("���պ���:" + smsrecord.getSmsphone());
//				System.out.println("��������:" + smsrecord.getSmscontent());
//				System.out.println("����ʱ��:" + smsrecord.getSmsrectime().toLocaleString());
//				mainService.insertSmsRec(smsrecord);	
//			}
//		}
		
		// ���ÿ������б�־
		commonDataUnit.setSmsDoingData(false);
	}
	
	/**
	 * Ϊ�˰���Ϣ��ʧ�������,Ԥ��������
	 */
	public void perReadMemory(){
		String memorystr = "";
		boolean isEndFlag = false;
		int retrytime = RETRY_TIMES; // û����β,�ȴ�����
		if (!memorystr.equals("")){//ֻ�л��治Ϊ��ʱ,�Ŵ���
			while(!isEndFlag){
				try {
					if (retrytime < 1){break;}//�����ȴ�����,����
					memorystr = simCard_Unit.freeAndReturnMemory(2);//��ȡ,����ɾ������
					isEndFlag =	memorystr.substring(memorystr.length()-2, memorystr.length()).equals("\r\n");// �жϻ�����������������ַ��Ƿ���\r\n
					if (isEndFlag){break;}
					Thread.sleep(WAIT_MILLTIME);// �������һ������û�з�������,�ȴ�һ��ʱ��
					retrytime--;//�ȴ�������һ
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}		
		//return memorystr;
	}
	
	public void sendMessage(String centerNo, SmsRecord smsRecord){
		//���Ͷ���
		simCard_Unit.sendMessage(centerNo, smsRecord.getSmsphone(), smsRecord.getSmscontent());		
	}
	
	/**
	 * �ѽ��յ��Ķ����ַ�,�����ɶ��Ž��ն���,�����к������Ч�Լ��
	 */
	public List<SmsRecord> packSmsRecord(String smsStr){
		List<SmsRecord> smsList = new ArrayList<SmsRecord>();
		try {
			if(null!=smsStr && smsStr.length()>0){
				SmsRecord smsbean = new SmsRecord();
				int head_begin_index,head_middle_index,head_end_index;
				int smslen=0, sms_center_len=0, sms_body_len=0,tem_len=0;
				String head_str="",body_str="",temp_str="";
				
				// smsStr��:+CMT:,30\r0891683108501705F0040D91683137185940F80008015070412442230A53CD53CD590D590D53D1
				temp_str = smsStr.replaceAll(" ", "").replaceAll("\\n", "");			// ȥ�����пո�ͻ���
				while (true) {
					// ****** ��ȡ�����г�������������ֽ���
					tem_len = temp_str.length();
					head_begin_index = temp_str.indexOf("+CMT");						// �ҵ�+CMTλ��
					if(head_begin_index < 0) {break;}
					head_end_index = temp_str.indexOf("\r",head_begin_index);			// �ҵ����з���λ��
					if(head_end_index < 0) {break;}
					head_str = temp_str.substring(head_begin_index,head_end_index);		// ��ȡ"+CMT"��"\r"֮����ַ���.��:+CMT: ,30\r
					head_middle_index = head_str.lastIndexOf(",");						// ��ȡhead_str�����һ��","
					if(head_middle_index < 0){break;}
					head_middle_index = head_begin_index + head_middle_index + 1;
					sms_body_len = Integer.parseInt(temp_str.substring(head_middle_index, head_end_index));	//�õ������г�������������ֽ���
					
					// ******��ȡ�������ĵ���ռ���ֽ���
					if ( tem_len < (head_end_index + 4) ){break;}						// �ж��Ƿ����㹻�ĳ��Ȼ�ȡĿ���ֽ���
					sms_center_len = Integer.parseInt(temp_str.substring(head_end_index+1, head_end_index+3),16);//�õ������ж������ĵ��ֽ���
					// ******��ȡ�ĳ���Ӧ��=CMTͷ������+�������ĳ�����ʾ����+(�������ĳ���+�������ݳ���)*2
					smslen = (head_end_index + 1) + 2 + (sms_center_len + sms_body_len)*2;// �ж��Ƿ����㹻����ֽ�������ʾ�������� 
					if (tem_len < smslen){break;}
					body_str = temp_str.substring(head_end_index+1, smslen);//һ��������������
					// ******����һ�����Ŷ���
					smsbean = ShortMessageUnit.receiveMessage(body_str);
					if(smsbean != null){
						for (PhoneList phone : commonDataUnit.getPhoneList()) {			// ���е绰�����б�
							if (smsbean.getSmsphone().indexOf(phone.getPhone()) >= 0){	// �������к���,ȷ���Ƿ�����Ч�ǵ绰����
								smsbean.setSmsphone(phone.getPhone());
								smsList.add(smsbean);
								break;
							}
						}	
					}
					// ������һ��ѭ��
					temp_str = temp_str.substring(smslen);
				}// end for while
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return smsList;
	}
	
//	public static void main(String args[]) {
//		TaskForSms dd = new TaskForSms();
//		String smsStr = "\r\n+CMT: ,26\r\n0891683108501705F0040D91683137185940F800080150311180242306597D202854C8\r\n";
//		List<SmsRecord>  list = dd.packSmsRecord(smsStr);
//		System.out.println("�����������" + list.size());
//		for (SmsRecord recevice : list) {
//			System.out.println("��������:" + recevice.getSmsphone());
//			System.out.println("��������:" + recevice.getSmscontent());
//			System.out.println("����ʱ��:" + recevice.getSmsrectime().toLocaleString());
//		}
//	}

}

