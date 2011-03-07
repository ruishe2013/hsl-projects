package com.aifuyun.search.build.scheduler;

import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import com.aifuyun.search.SearchException;

public class BuilderScheduler {
	
	// 默认 凌晨1点开始执行
	private String fullDumpCron = "0 0 1 * * ?";
	
	// 默认10分钟一次
	private String incrDumpCron = "0/10/20/30/40/50 * * * * ?"; //"0 0/10/20/30/40/50 * * * ?";
	
	private Scheduler scheduler;
	
	private String coreName;
	
	public BuilderScheduler(String coreName) {
		super();
		this.coreName = coreName;
	}


	public void init() throws SchedulerException, ParseException {
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		
		scheduler.getContext().put("coreName", coreName);
		
		JobDetail fullDumpDetail = new JobDetail("fullDetail1", "dumpGroup", FullBuilderJob.class);
		CronTrigger fullDumpTrigger = new CronTrigger("fullTrigger", "dumpGroup", fullDumpCron);

		JobDetail incrDumpDetail = new JobDetail("incrDetail", "dumpGroup", IncrBuilderJob.class);
		CronTrigger incrDumpTrigger = new CronTrigger("incrTrigger", "dumpGroup", incrDumpCron);
		
		scheduler.scheduleJob(fullDumpDetail, fullDumpTrigger);
		scheduler.scheduleJob(incrDumpDetail, incrDumpTrigger);
	}
	
	
	public void startup() {
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			throw new SearchException(e);
		}
	}
	
	public void shutdown() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			throw new SearchException(e);
		}
	}

	public String getFullDumpCron() {
		return fullDumpCron;
	}

	public void setFullDumpCron(String fullDumpCron) {
		this.fullDumpCron = fullDumpCron;
	}

	public String getIncrDumpCron() {
		return incrDumpCron;
	}

	public void setIncrDumpCron(String incrDumpCron) {
		this.incrDumpCron = incrDumpCron;
	}
	
}
