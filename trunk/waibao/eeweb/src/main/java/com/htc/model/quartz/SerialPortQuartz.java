package com.htc.model.quartz;

import org.quartz.*;
import org.apache.commons.logging.*;

/**
 * @ SerialPortQuartz.java
 * 作用 : 任务类别.即时数据 - 每日统计 - 每月统计 - 数据库备份 - 清除整理即时数据 - 检查电压 - 核对时间
 * 注意事项 : 数据库备份暂时不做.
 * VERSION       DATE            BY       CHANGE/COMMENT
 * 1.0          2009-11-6     YANGZHONLI       create
 */
//	秒  0 - 59   ,  - * / 
//	分  0 - 59   ,  - * / 
//	小时  0 - 23   ,  - * / 
//	日期  1 - 31   ,  - * ? / L W C 
//	月份  1 - 12  或者 JAN-DEC  ,  - * / 
//	星期  1 - 7  或者 SUN-SAT  ,  - * ? / L C # 
//	年（可选） 留空 ,   1970 - 2099   ,  - * /  
public class SerialPortQuartz {
		
	private Log log = LogFactory.getLog(SerialPortQuartz.class);
	private String HTC_GROUP = "HtcGroup";
	
	// 1:即时数据 			任务 cron表达式:每隔10秒,做这个任务
	private String InstantDataCron = "0/10 * * * * ?";
	// 2:每日统计			任务  cron表达式:每天的0:0:0,做这个任务
	private String StateDailyDataCron = "0 0 0 * * ?";
	// 任务开始标志(0: 需要初始化 ,没有开始  1:进行中  )
	public int teskRunFlag = 0;		 
	
	private Scheduler scheduler;
	private JobDetail instantDetail;
	private JobDetail dailyDetail;
	private JobDetail smsDetail;
	//注册service -- spring ioc	
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
	
	//构造函数
	public SerialPortQuartz(){
	}
	
	//开始任务
	public void runTask(){
		try {
			// 1:即时数据作业
			//JobDetail job = new JobDetail("InstantTask", "HtcGroup", TaskForInstantData.class);
			instantDetail.setName("InstantTask");
			instantDetail.setGroup(HTC_GROUP);
			CronTrigger triggerInstant = new CronTrigger("InstantTrigger", HTC_GROUP, "InstantTask", HTC_GROUP, InstantDataCron);
			
			// 2:每日统计作业
			//job = new JobDetail("StateDailyTask", "HtcGroup", TaskForStateDailyMonth.class);
			dailyDetail.setName("StateDailyTask");
			dailyDetail.setGroup(HTC_GROUP);
			CronTrigger triggerDaily = new CronTrigger("StateDailyTrigger", HTC_GROUP, "StateDailyTask", HTC_GROUP, StateDailyDataCron);
			
			// 3:短信模块任务
			smsDetail.setName("smsTask");
			smsDetail.setGroup(HTC_GROUP);
			CronTrigger triggerSms = new CronTrigger("SmsTrigger", HTC_GROUP, "smsTask", HTC_GROUP, InstantDataCron);
			
			if (teskRunFlag == 0){ // 第一次运行任务
				teskRunFlag = 1;
				
				//log.info("数据采集作业,进入准备状态... ");
				scheduler.addJob(instantDetail, true);		
				scheduler.scheduleJob(triggerInstant);
				
				//log.info("统计作业,进入准备状态... ");
				scheduler.addJob(dailyDetail, true);		
				scheduler.scheduleJob(triggerDaily);
				
				//log.info("短信作业,进入准备状态... ");
				scheduler.addJob(smsDetail, true);		
				scheduler.scheduleJob(triggerSms);				
				
				log.info("启动调度作业 ...");
				scheduler.start();
				
			}else{// 停止任务后,有继续任务
				//scheduler.unscheduleJob("InstantTrigger", HTC_GROUP);//移除触发器				
				//scheduler.unscheduleJob("InstantTrigger", HTC_GROUP);//移除触发器				
				// 重新加载即时任务
				scheduler.rescheduleJob("InstantTrigger", HTC_GROUP, triggerInstant); 
				// 重新加载日任务
				scheduler.rescheduleJob("StateDailyTrigger", HTC_GROUP, triggerDaily);
				// 重新加载短信任务
				scheduler.rescheduleJob("SmsTrigger", HTC_GROUP, triggerSms);
				// 唤醒任务
				scheduler.resumeAll();
				log.info("恢复作业调度...");
				
			}
		} catch (Exception e) {
			log.error("调度作业初始化失败。异常信息:" + e.getMessage()+e.toString());
		}
	}
	
	/**
	 * @describe: 暂停所有任务
	 * @date:2010-4-8
	 */
	public void pauseAllTask(){
		try {
			log.info("暂停作业调度...");
			scheduler.pauseAll();
		} catch (SchedulerException e) {
			log.error("关闭调度作业失败。异常信息:" + e.getMessage()+e.toString());
		}
	}
	
	//结束任务
	public void endTask(){
		try {
			if (scheduler == null){return;}
			if (scheduler.isShutdown() == false){
				scheduler.shutdown(true);
				teskRunFlag = 0;
				log.info("关闭调度作业...");
			}
		} catch (SchedulerException e) {
			log.error("关闭调度作业失败。异常信息:" + e.getMessage()+e.toString());
		}
	}
	
	//1: 设置  即时数据任务 的任务间隔
	/**
	 * @describe: 设置  即时数据任务 的任务间隔
	 * @param timeInterval: 任务间隔.单位是秒
	 * @date:2009-11-6
	 */
	public void setInstantDataCron(int timeInterval) {
		if (timeInterval < 60 ){//一份钟内
			InstantDataCron = "0/" + timeInterval + " * * * * ?";
		}else if ( (timeInterval >= 60) ||(timeInterval < 3600) ){//1小时内
			InstantDataCron = "0 0/" + timeInterval + " * * * ?";
		}else if (timeInterval >= 3600){//大于1小时
			InstantDataCron = "0 0 0/" + timeInterval + " * * ?";
		}
	}
	
	//2:每日统计
	public void setStateDailyDataCron(String stateDailyDataCron) {
		StateDailyDataCron = stateDailyDataCron;
	}

/*	public static void main(String[] args) throws Exception {
		SerialPortQuartz example = new SerialPortQuartz();
		example.runTask();
	}*/
	
}
