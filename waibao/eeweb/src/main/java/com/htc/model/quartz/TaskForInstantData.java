package com.htc.model.quartz;


import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.htc.common.*;
import com.htc.domain.EquipData;
import com.htc.model.seriaPort.*;

/**
 * @ TaskForInstantData.java
 * ���� : ��ʱ������ҵ����.
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
//public class TaskForInstantData  implements Job {
public class TaskForInstantData  extends QuartzJobBean {
	
	private Log log = LogFactory.getLog(TaskForInstantData.class);
	private CommonDataUnit commonDataUnit;
	private Level_First_Serial first_Level;
	//ע��service -- spring ioc
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}

	// ��Ҫһ���յĹ��캯�����������ڵ��ȵ�ʱ�������������캯����ʼ������
	public TaskForInstantData() {
	}

	// ��ʱ������ҵ
	//public void execute(JobExecutionContext context) throws JobExecutionException {
	public void executeInternal(JobExecutionContext ctx) throws JobExecutionException {	
		
		// Ԥ����һ��:�ж��Ƿ������������(��ҪӦ����:��ͣ����ʱ,�����ִֹ�д�������Ľ���,��Ȼ���ڹر�ʱ,��Ȼ�д���ͨ��,���������)
		// log.info("��-init:��ʼ�µ�����...");	
		if (!first_Level.isCanDoTask()){
			log.info("��-init:��ʱ������ͣ...");	
			return;
		}
		// Ԥ���ڶ���:��֤�ڴ���������ִ������
		if (!first_Level.isRunningFlag()){
			log.info("��-init:��ʱ����û�п�ʼ,ԭ��:����û�д�...");	
			return;
		}

		// Ԥ��������:��֤ǰ�治�������ڲɼ����ݵ�����
		if (commonDataUnit.isEqDoingData() == false){
			// ���ڲɼ�����
			commonDataUnit.setEqDoingData(true);
		}else{
			log.info("��-0:����ʱ,ǰ�������û�н���...");	
			// ���߳�������,�˳�
			return;
		}
		
		// Ԥ���������,����ɼ�����
		//long start = System.currentTimeMillis();
		// ���ڹ�����
		//Level_First_Serial first_Level = Level_First_Serial.getInstance();
		
		int equipmentId;		// ����ID
		int address;			// ����Ӳ����ַ
		int runequiCount = 0;	// �����е���������
		int allEqCount = 0;		// ȫ����������
			
		// 1:��ʱ���ݲɼ�
		try {
			// ��ȡȫ����������Ϣ -> ��ȡ���� ����  ->  ���Ͷ�ȡ��ʪ�ȵĴ�������
			//for (int i = 0; i < 2000; i++) {
				for (EquipData equipData : commonDataUnit.getEquiMap().values()) {
					allEqCount ++;
					if (equipData == null){continue;}
					equipmentId = equipData.getEquipmentId();
					address = equipData.getAddress();
					if (first_Level.writeReadSerial(address, equipmentId, Level_Second_Serial.READ_USEFULL_DATA, equipData.getConndata()) 
							!= Level_Final_Serial.Serial_right_Frame ){
						runequiCount++;
					};
				}// end for
			//}
			
			if (allEqCount == runequiCount){//��������ȫ����֡,�����Ǵ���û�в��
				// ��鴮���Ƿ���������
				// first_Level.checkRunningPort(); // ��ʱ�����
				if (allEqCount > 0){
					log.info("��ʱ������ҵ-ͨ���쳣-ȫ���Ƕ�֡��ʧ֡...");
				}else{
					log.info("��ʱ������ҵ-ʧ��--��Ϊ�����б�Ϊ��...");
				}
			}
		}catch (Exception e) {
			log.error("��ʱ���ݲɼ�ʱ����:" + e.getMessage()+e.toString());
		}
		//start = System.currentTimeMillis() - start;
		//log.info("��-1:��ʱ������ҵ����һ��>...>...>��ʱ"+ start +"����");
		
		// 2: �˶�ʱ�� 
		if (runequiCount !=0 ){
			try {
				// 0��0�ֵ�0��1����У׼ʱ��
				if (FunctionUnit.getCurTime(Calendar.HOUR_OF_DAY) == 0){
					if (FunctionUnit.getCurTime(Calendar.MINUTE) < 2){
						//start = System.currentTimeMillis();
						try {
							// �˶�ʱ��
							first_Level.writeTime(2);
						} catch (Exception e) {
							log.error("�˶�ʱ������ʱ����:" + e.getMessage()+e.toString());
						}
						//start = System.currentTimeMillis() - start;
						//log.info("��-2:�˶�ʱ�� ��ҵ����һ��>...>...>��ʱ"+ start +"����");								
					}
				}
			} catch (Exception e) {
				log.error("�ж�ʱ����0��0�ֵ�0��5����ʱ����-[�˶�ʱ������]:" + e.getMessage()+e.toString());
			}
		}//end
		
		// �ɼ����ݽ���
		commonDataUnit.setEqDoingData(false);
	}

}
