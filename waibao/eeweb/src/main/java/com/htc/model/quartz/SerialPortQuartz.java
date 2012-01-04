package com.htc.model.quartz;

import org.quartz.*;
import org.apache.commons.logging.*;

/**
 * @ SerialPortQuartz.java
 * ���� : �������.��ʱ���� - ÿ��ͳ�� - ÿ��ͳ�� - ���ݿⱸ�� - �������ʱ���� - ����ѹ - �˶�ʱ��
 * ע������ : ���ݿⱸ����ʱ����.
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
//	��  0 - 59   ,  - * / 
//	��  0 - 59   ,  - * / 
//	Сʱ  0 - 23   ,  - * / 
//	����  1 - 31   ,  - * ? / L W C 
//	�·�  1 - 12  ���� JAN-DEC  ,  - * / 
//	����  1 - 7  ���� SUN-SAT  ,  - * ? / L C # 
//	�꣨��ѡ�� ���� ,   1970 - 2099   ,  - * /  
public class SerialPortQuartz {
		
	private Log log = LogFactory.getLog(SerialPortQuartz.class);
	private String HTC_GROUP = "HtcGroup";
	
	// 1:��ʱ���� 			���� cron���ʽ:ÿ��10��,���������
	private String InstantDataCron = "0/10 * * * * ?";
	// 2:ÿ��ͳ��			����  cron���ʽ:ÿ���0:0:0,���������
	private String StateDailyDataCron = "0 0 0 * * ?";
	// ����ʼ��־(0: ��Ҫ��ʼ�� ,û�п�ʼ  1:������  )
	public int teskRunFlag = 0;		 
	
	private Scheduler scheduler;
	private JobDetail instantDetail;
	private JobDetail dailyDetail;
	private JobDetail smsDetail;
	//ע��service -- spring ioc	
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}
	public void setInstantDetail(JobDetail instantDetail) {
		this.instantDetail = instantDetail;
	}
	public void setDailyDetail(JobDetail dailyDetail) {
		this.dailyDetail = dailyDetail;
	}
	public void setSmsDetail(JobDetail smsDetail) {
		this.smsDetail = smsDetail;
	}
	
	//���캯��
	public SerialPortQuartz(){
	}
	
	//��ʼ����
	public void runTask(){
		try {
			// 1:��ʱ������ҵ
			//JobDetail job = new JobDetail("InstantTask", "HtcGroup", TaskForInstantData.class);
			instantDetail.setName("InstantTask");
			instantDetail.setGroup(HTC_GROUP);
			CronTrigger triggerInstant = new CronTrigger("InstantTrigger", HTC_GROUP, "InstantTask", HTC_GROUP, InstantDataCron);
			
			// 2:ÿ��ͳ����ҵ
			//job = new JobDetail("StateDailyTask", "HtcGroup", TaskForStateDailyMonth.class);
			dailyDetail.setName("StateDailyTask");
			dailyDetail.setGroup(HTC_GROUP);
			CronTrigger triggerDaily = new CronTrigger("StateDailyTrigger", HTC_GROUP, "StateDailyTask", HTC_GROUP, StateDailyDataCron);
			
			// 3:����ģ������
			smsDetail.setName("smsTask");
			smsDetail.setGroup(HTC_GROUP);
			CronTrigger triggerSms = new CronTrigger("SmsTrigger", HTC_GROUP, "smsTask", HTC_GROUP, InstantDataCron);
			
			if (teskRunFlag == 0){ // ��һ����������
				teskRunFlag = 1;
				
				//log.info("���ݲɼ���ҵ,����׼��״̬... ");
				scheduler.addJob(instantDetail, true);		
				scheduler.scheduleJob(triggerInstant);
				
				//log.info("ͳ����ҵ,����׼��״̬... ");
				scheduler.addJob(dailyDetail, true);		
				scheduler.scheduleJob(triggerDaily);
				
				//log.info("������ҵ,����׼��״̬... ");
				scheduler.addJob(smsDetail, true);		
				scheduler.scheduleJob(triggerSms);				
				
				log.info("����������ҵ ...");
				scheduler.start();
				
			}else{// ֹͣ�����,�м�������
				//scheduler.unscheduleJob("InstantTrigger", HTC_GROUP);//�Ƴ�������				
				//scheduler.unscheduleJob("InstantTrigger", HTC_GROUP);//�Ƴ�������				
				// ���¼��ؼ�ʱ����
				scheduler.rescheduleJob("InstantTrigger", HTC_GROUP, triggerInstant); 
				// ���¼���������
				scheduler.rescheduleJob("StateDailyTrigger", HTC_GROUP, triggerDaily);
				// ���¼��ض�������
				scheduler.rescheduleJob("SmsTrigger", HTC_GROUP, triggerSms);
				// ��������
				scheduler.resumeAll();
				log.info("�ָ���ҵ����...");
				
			}
		} catch (Exception e) {
			log.error("������ҵ��ʼ��ʧ�ܡ��쳣��Ϣ:" + e.getMessage()+e.toString());
		}
	}
	
	/**
	 * @describe: ��ͣ��������
	 * @date:2010-4-8
	 */
	public void pauseAllTask(){
		try {
			log.info("��ͣ��ҵ����...");
			scheduler.pauseAll();
		} catch (SchedulerException e) {
			log.error("�رյ�����ҵʧ�ܡ��쳣��Ϣ:" + e.getMessage()+e.toString());
		}
	}
	
	//��������
	public void endTask(){
		try {
			if (scheduler == null){return;}
			if (scheduler.isShutdown() == false){
				scheduler.shutdown(true);
				teskRunFlag = 0;
				log.info("�رյ�����ҵ...");
			}
		} catch (SchedulerException e) {
			log.error("�رյ�����ҵʧ�ܡ��쳣��Ϣ:" + e.getMessage()+e.toString());
		}
	}
	
	//1: ����  ��ʱ�������� ��������
	/**
	 * @describe: ����  ��ʱ�������� ��������
	 * @param timeInterval: ������.��λ����
	 * @date:2009-11-6
	 */
	public void setInstantDataCron(int timeInterval) {
		if (timeInterval < 60 ){//һ������
			InstantDataCron = "0/" + timeInterval + " * * * * ?";
		}else if ( (timeInterval >= 60) ||(timeInterval < 3600) ){//1Сʱ��
			InstantDataCron = "0 0/" + timeInterval + " * * * ?";
		}else if (timeInterval >= 3600){//����1Сʱ
			InstantDataCron = "0 0 0/" + timeInterval + " * * ?";
		}
	}
	
	//2:ÿ��ͳ��
	public void setStateDailyDataCron(String stateDailyDataCron) {
		StateDailyDataCron = stateDailyDataCron;
	}

/*	public static void main(String[] args) throws Exception {
		SerialPortQuartz example = new SerialPortQuartz();
		example.runTask();
	}*/
	
}
