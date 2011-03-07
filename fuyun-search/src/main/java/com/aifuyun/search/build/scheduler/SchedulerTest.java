package com.aifuyun.search.build.scheduler;

import java.text.ParseException;

import org.quartz.SchedulerException;

public class SchedulerTest {

	/**
	 * @param args
	 * @throws SchedulerException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws SchedulerException, ParseException {
		BuilderScheduler builderScheduler = new BuilderScheduler("search1");
		
		builderScheduler.init();
		builderScheduler.startup();
		System.out.println("here");
	}
	
}
