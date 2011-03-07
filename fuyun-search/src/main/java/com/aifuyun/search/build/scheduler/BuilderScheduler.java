package com.aifuyun.search.build.scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aifuyun.search.SearchException;
import com.aifuyun.search.util.ResouceUtil;

public class BuilderScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(BuilderScheduler.class);
	
	private String scheduleConfig = "schedule-config.properties";
	
	// 默认 凌晨1点开始执行
	private String fullDumpCron = "0 0 1 * * ?";
	
	// 默认10分钟一次
	private String incrDumpCron = "0 0/10/20/30/40/50 * * * ?";
	
	private Scheduler scheduler;
	
	private String coreName;
	
	public static void main(String[] a) {
		String abc = "0 0 1 * * ?\r\n0/10/20/30/40/50 * * * * ?";
		String[] parts = abc.split("[\r\n]+");
		System.out.println(parts.length);
		System.out.println(parts[0]);
		System.out.println(parts[1]);
	}
	
	protected void loadCronExpr() {
		try {
			String content = ResouceUtil.getResourceContentString(scheduleConfig);
			if (content == null) {
				return;
			}
			BufferedReader reader = new BufferedReader(new StringReader(content));
			fullDumpCron = reader.readLine();
			incrDumpCron = reader.readLine();
		} catch (IOException e) {
			log.error("load cron express failed", e);
		}
	}
	
	public BuilderScheduler(String coreName) {
		super();
		this.coreName = coreName;
	}


	public void init() throws SchedulerException, ParseException {
		loadCronExpr();
		
		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.getContext().put("coreName", coreName);
		
		if (log.isInfoEnabled()) {
			log.info("fullDumpCron: " + fullDumpCron);
			log.info("incrDumpCron: " + incrDumpCron);
		}
		
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
