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
 * 作用 : 统计作业任务:日报,月报(每月一号),删除过期的及时数据(数据保存12个月),备份数据(备份前一个月的数据)
 * 注意事项 : 无
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
	//注册service -- spring ioc
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
	
	//需要一个空的构造函数，调度器在调度的时候可以用这个构造函数初始化对象
	public TaskForStateDailyMonth() {
	}

	//public void execute(JobExecutionContext context) throws JobExecutionException {
	public void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		
		// 预备第一步:判断是否可以运行任务(主要应用于:暂停串口时,必须禁止执行串口任务的进入,不然串口关闭时,任然有串口通信,这样会出错)
		if (!first_Level.isCanDoTask()){
			log.info("②-init:即时任务暂停...");	
			return;
		}
		// 预备第二步:保证在串口运行下执行任务
		//if (!Level_First_Serial.getInstance().isRunningFlag()){
		if (!first_Level.isRunningFlag()){
			log.info("②-0:日统计没有开始,原因:串口没有打开...");	
			return;
		}
		// 预备工作完成,进入采集任务
		//long start = System.currentTimeMillis();
		
		//MainService mainService = new MainService();
		Date formTime = null;
		Date endTime = null;
		boolean rsbool = true; 
		// 一:日报-每天都做
		try {
			// 获取搜索最后保存的时间和当天的0点
			formTime = mainService.getHisRecLastTime(MainService.TYPE_DAILY);
			if (formTime == null){// 系统第一次做日报
				// 方案一: 从trecord中获取最早的记录时间
				formTime = mainService.getRecordEearlyTime();
				formTime = FunctionUnit.nextTime(formTime, 0, FunctionUnit.Calendar_END_HOUR).getTime();
				// 方案二: 从当天的前一天开始
				//formTime = FunctionUnit.nextTime(new Date(), -1, FunctionUnit.Calendar_END_DAY).getTime();
			}else{
				formTime = FunctionUnit.nextTime(formTime, 1, FunctionUnit.Calendar_END_HOUR).getTime();
			}
			if (formTime!= null){
				endTime = FunctionUnit.nextTime(new Date(), 0, FunctionUnit.Calendar_END_DAY).getTime();
				HisRecord hisRecord = new HisRecord();
				hisRecord.setStattype(1);//1:代表日报类型	 2:代表月报类型
				hisRecord.setAlarmStartFrom(formTime);
				hisRecord.setAlarmStartTo(endTime);
				// 添加日报数据
				mainService.insertHisRecord(hisRecord);
			}
		} catch (Exception e) {
			rsbool = false;
			log.error("添加日报数据时出错:" + e.toString());
		}
		//start = System.currentTimeMillis() - start;
		//log.info("②-1:日报统计作业>...>...>用时"+ start +"毫秒");
		
		// 二:备份数据(备份前一天的数据)-每天都做
		if (!rsbool){return;}
		//start = System.currentTimeMillis();
		formTime = null;		endTime = null;
		try {
			formTime = setSysService.getBackUpLastTime();
			if (formTime == null){// 系统第一次做备份
				// 方案一: 如果备份列表为空,则开始时间从trecord中获取最早的记录时间
				formTime = mainService.getRecordEearlyTime();
				formTime = FunctionUnit.nextTime(formTime, 0, FunctionUnit.Calendar_END_MINUTE).getTime();
				// 方案二: 如果备份列表为空,则从当天的前一天开始
				//formTime = FunctionUnit.nextTime(new Date(), -1, FunctionUnit.Calendar_END_DAY).getTime();
			}else{
				formTime = FunctionUnit.nextTime(formTime, 0, FunctionUnit.Calendar_END_DAY).getTime();
			}
			if (formTime!= null){
				endTime = FunctionUnit.nextTime(new Date(), 0, FunctionUnit.Calendar_END_DAY).getTime();
				// 保证备份列表或者trecord有数据
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
			log.error("备份Excel数据时出错:" + e.toString());
		}
		//start = System.currentTimeMillis() - start;
		//log.info("②-2:备份Excel数据>...>...>用时"+ start +"毫秒");
		
		// 三:删除过期的及时数据(保存12个月的及时数据)-每天都做
		if (!rsbool){return;}
		//start = System.currentTimeMillis();
		endTime = null;
		String endRecTime = "";	
		try {
			endTime = FunctionUnit.nextTime(new Date(), -12+1, FunctionUnit.Calendar_END_MONTH).getTime();
			endRecTime = FunctionUnit.getDateToStr(endTime, FunctionUnit.Calendar_END_SECOND, FunctionUnit.UN_SHOW_CHINESE);
			// 测试用
//			endRecTime = mainService.getRecordEearlyTime();
//			if (endRecTime == null){return;}
			mainService.deleteRecordBatch(endRecTime);
		} catch (Exception e) {
			rsbool = false;
			log.error("删除过期的及时数据时出错:" + e.getMessage()+e.toString());
		}	
		//start = System.currentTimeMillis() - start;
		//log.info("②-3:删除过期的及时数据>...>...>用时"+ start +"毫秒");		
		
		// 判断是否是月初(是否是1号)-每月月初
		try {
			if (FunctionUnit.getCurTime(Calendar.DATE) == 1){
				rsbool = rsbool? true : false;
			}else{
				rsbool = false;
			}
		} catch (Exception e) {
			rsbool = false;
			log.error("判断是否是月初时出错-[添加月报表时间判断]:" + e.getMessage()+e.toString());
		}	
		
		// 四:月报(最后的保存时间和前一个月的时间内的数据)
		if (!rsbool){return;}
		//start = System.currentTimeMillis();
		formTime = null;
		try {
			formTime = mainService.getHisRecLastTime(MainService.TYPE_MONTH);
			if (formTime == null){// 系统第一次做月报表
				// 方案一: 如果月报表为空,则开始时间从日报表中获取最早的记录时间
				formTime = mainService.getHisRecEearlyTime(MainService.TYPE_DAILY);
				formTime = FunctionUnit.nextTime(formTime, 0, FunctionUnit.Calendar_END_DAY).getTime();
				// 方案二: 如果月报表为空,则从当前的前一月开始
				//formTime = FunctionUnit.nextTime(new Date(), -1, FunctionUnit.Calendar_END_MONTH).getTime();
			}else{
				formTime = FunctionUnit.nextTime(formTime, 1, FunctionUnit.Calendar_END_DAY).getTime();
			}
			if (formTime!= null){
				endTime = FunctionUnit.nextTime(new Date(), 0, FunctionUnit.Calendar_END_MONTH).getTime();
				HisRecord hisRecord = new HisRecord();
				hisRecord.setStattype(2);//1:代表日报类型	 2:代表月报类型
				hisRecord.setAlarmStartFrom(formTime);
				hisRecord.setAlarmStartTo(endTime);
				//添加月报数据
				mainService.insertHisRecord(hisRecord);		
			}
		} catch (Exception e) {
			rsbool = false;
			log.error("添加月报数据时出错:" + e.getMessage()+e.toString());
		}	
		//start = System.currentTimeMillis() - start;
		//log.info("②-4:月报统计作业>...>...>用时"+ start +"毫秒");		
	}
	
}
