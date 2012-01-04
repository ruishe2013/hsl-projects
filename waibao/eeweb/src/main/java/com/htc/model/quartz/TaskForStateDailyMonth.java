package com.htc.model.quartz;

import java.util.*;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.htc.bean.BeanForSysArgs;
import com.htc.common.*;
import com.htc.model.seriaPort.*;
import com.htc.model.tool.ExcelBackUp;
import com.htc.domain.HisRecord;
import com.htc.model.MainService;
import com.htc.model.SetSysService;

/**
 * @ TaskForStateDailyData.java
 * ���� : ͳ����ҵ����:�ձ�,�±�(ÿ��һ��),ɾ�����ڵļ�ʱ����(���ݱ���12����),��������(����ǰһ���µ�����)
 * ע������ : ��
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
//public class TaskForStateDailyMonth implements Job {
public class TaskForStateDailyMonth extends QuartzJobBean {
	
	private Log log = LogFactory.getLog(TaskForStateDailyMonth.class);
	private MainService mainService;
	private CommonDataUnit commonDataUnit;
	private Level_First_Serial first_Level;
	private SetSysService setSysService;
	//ע��service -- spring ioc
	public void setMainService(MainService mainService) {
		this.mainService = mainService;
	}
	public void setCommonDataUnit(CommonDataUnit commonDataUnit) {
		this.commonDataUnit = commonDataUnit;
	}
	public void setFirst_Level(Level_First_Serial first_Level) {
		this.first_Level = first_Level;
	}
	public void setSetSysService(SetSysService setSysService) {
		this.setSysService = setSysService;
	}
	
	//��Ҫһ���յĹ��캯�����������ڵ��ȵ�ʱ�������������캯����ʼ������
	public TaskForStateDailyMonth() {
	}

	//public void execute(JobExecutionContext context) throws JobExecutionException {
	public void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		
		// Ԥ����һ��:�ж��Ƿ������������(��ҪӦ����:��ͣ����ʱ,�����ִֹ�д�������Ľ���,��Ȼ���ڹر�ʱ,��Ȼ�д���ͨ��,���������)
		if (!first_Level.isCanDoTask()){
			log.info("��-init:��ʱ������ͣ...");	
			return;
		}
		// Ԥ���ڶ���:��֤�ڴ���������ִ������
		//if (!Level_First_Serial.getInstance().isRunningFlag()){
		if (!first_Level.isRunningFlag()){
			log.info("��-0:��ͳ��û�п�ʼ,ԭ��:����û�д�...");	
			return;
		}
		// Ԥ���������,����ɼ�����
		//long start = System.currentTimeMillis();
		
		//MainService mainService = new MainService();
		Date formTime = null;
		Date endTime = null;
		boolean rsbool = true; 
		// һ:�ձ�-ÿ�춼��
		try {
			// ��ȡ������󱣴��ʱ��͵����0��
			formTime = mainService.getHisRecLastTime(MainService.TYPE_DAILY);
			if (formTime == null){// ϵͳ��һ�����ձ�
				// ����һ: ��trecord�л�ȡ����ļ�¼ʱ��
				formTime = mainService.getRecordEearlyTime();
				formTime = FunctionUnit.nextTime(formTime, 0, FunctionUnit.Calendar_END_HOUR).getTime();
				// ������: �ӵ����ǰһ�쿪ʼ
				//formTime = FunctionUnit.nextTime(new Date(), -1, FunctionUnit.Calendar_END_DAY).getTime();
			}else{
				formTime = FunctionUnit.nextTime(formTime, 1, FunctionUnit.Calendar_END_HOUR).getTime();
			}
			if (formTime!= null){
				endTime = FunctionUnit.nextTime(new Date(), 0, FunctionUnit.Calendar_END_DAY).getTime();
				HisRecord hisRecord = new HisRecord();
				hisRecord.setStattype(1);//1:�����ձ�����	 2:�����±�����
				hisRecord.setAlarmStartFrom(formTime);
				hisRecord.setAlarmStartTo(endTime);
				// ����ձ�����
				mainService.insertHisRecord(hisRecord);
			}
		} catch (Exception e) {
			rsbool = false;
			log.error("����ձ�����ʱ����:" + e.toString());
		}
		//start = System.currentTimeMillis() - start;
		//log.info("��-1:�ձ�ͳ����ҵ>...>...>��ʱ"+ start +"����");
		
		// ��:��������(����ǰһ�������)-ÿ�춼��
		if (!rsbool){return;}
		//start = System.currentTimeMillis();
		formTime = null;		endTime = null;
		try {
			formTime = setSysService.getBackUpLastTime();
			if (formTime == null){// ϵͳ��һ��������
				// ����һ: ��������б�Ϊ��,��ʼʱ���trecord�л�ȡ����ļ�¼ʱ��
				formTime = mainService.getRecordEearlyTime();
				formTime = FunctionUnit.nextTime(formTime, 0, FunctionUnit.Calendar_END_MINUTE).getTime();
				// ������: ��������б�Ϊ��,��ӵ����ǰһ�쿪ʼ
				//formTime = FunctionUnit.nextTime(new Date(), -1, FunctionUnit.Calendar_END_DAY).getTime();
			}else{
				formTime = FunctionUnit.nextTime(formTime, 0, FunctionUnit.Calendar_END_DAY).getTime();
			}
			if (formTime!= null){
				endTime = FunctionUnit.nextTime(new Date(), 0, FunctionUnit.Calendar_END_DAY).getTime();
				// ��֤�����б����trecord������
				ExcelBackUp.storeToXls(formTime, endTime, commonDataUnit.getSysArgsByKey(BeanForSysArgs.BACKUP_PATH),
						 commonDataUnit.area2Equipment(),
						 Integer.parseInt(commonDataUnit.getSysArgsByKey(BeanForSysArgs.TEMP_SHOW_TYPE)),
						 commonDataUnit.getEquiMap(),
						 commonDataUnit.getSysArgsByKey(BeanForSysArgs.NORMAL_COLORDEF),
						 commonDataUnit.getSysArgsByKey(BeanForSysArgs.HIGH_COLORDEF),
						 commonDataUnit.getSysArgsByKey(BeanForSysArgs.HIGH_COLORDEF),
						 setSysService, mainService);
			}
		} catch (Exception e) {
			log.error("����Excel����ʱ����:" + e.toString());
		}
		//start = System.currentTimeMillis() - start;
		//log.info("��-2:����Excel����>...>...>��ʱ"+ start +"����");
		
		// ��:ɾ�����ڵļ�ʱ����(����12���µļ�ʱ����)-ÿ�춼��
		if (!rsbool){return;}
		//start = System.currentTimeMillis();
		endTime = null;
		String endRecTime = "";	
		try {
			endTime = FunctionUnit.nextTime(new Date(), -12+1, FunctionUnit.Calendar_END_MONTH).getTime();
			endRecTime = FunctionUnit.getDateToStr(endTime, FunctionUnit.Calendar_END_SECOND, FunctionUnit.UN_SHOW_CHINESE);
			// ������
//			endRecTime = mainService.getRecordEearlyTime();
//			if (endRecTime == null){return;}
			mainService.deleteRecordBatch(endRecTime);
		} catch (Exception e) {
			rsbool = false;
			log.error("ɾ�����ڵļ�ʱ����ʱ����:" + e.getMessage()+e.toString());
		}	
		//start = System.currentTimeMillis() - start;
		//log.info("��-3:ɾ�����ڵļ�ʱ����>...>...>��ʱ"+ start +"����");		
		
		// �ж��Ƿ����³�(�Ƿ���1��)-ÿ���³�
		try {
			if (FunctionUnit.getCurTime(Calendar.DATE) == 1){
				rsbool = rsbool? true : false;
			}else{
				rsbool = false;
			}
		} catch (Exception e) {
			rsbool = false;
			log.error("�ж��Ƿ����³�ʱ����-[����±���ʱ���ж�]:" + e.getMessage()+e.toString());
		}	
		
		// ��:�±�(���ı���ʱ���ǰһ���µ�ʱ���ڵ�����)
		if (!rsbool){return;}
		//start = System.currentTimeMillis();
		formTime = null;
		try {
			formTime = mainService.getHisRecLastTime(MainService.TYPE_MONTH);
			if (formTime == null){// ϵͳ��һ�����±���
				// ����һ: ����±���Ϊ��,��ʼʱ����ձ����л�ȡ����ļ�¼ʱ��
				formTime = mainService.getHisRecEearlyTime(MainService.TYPE_DAILY);
				formTime = FunctionUnit.nextTime(formTime, 0, FunctionUnit.Calendar_END_DAY).getTime();
				// ������: ����±���Ϊ��,��ӵ�ǰ��ǰһ�¿�ʼ
				//formTime = FunctionUnit.nextTime(new Date(), -1, FunctionUnit.Calendar_END_MONTH).getTime();
			}else{
				formTime = FunctionUnit.nextTime(formTime, 1, FunctionUnit.Calendar_END_DAY).getTime();
			}
			if (formTime!= null){
				endTime = FunctionUnit.nextTime(new Date(), 0, FunctionUnit.Calendar_END_MONTH).getTime();
				HisRecord hisRecord = new HisRecord();
				hisRecord.setStattype(2);//1:�����ձ�����	 2:�����±�����
				hisRecord.setAlarmStartFrom(formTime);
				hisRecord.setAlarmStartTo(endTime);
				//����±�����
				mainService.insertHisRecord(hisRecord);		
			}
		} catch (Exception e) {
			rsbool = false;
			log.error("����±�����ʱ����:" + e.getMessage()+e.toString());
		}	
		//start = System.currentTimeMillis() - start;
		//log.info("��-4:�±�ͳ����ҵ>...>...>��ʱ"+ start +"����");		
	}
	
}
