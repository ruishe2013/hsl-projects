package com.aifuyun.perftest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author longyi E-mail:longyi@taobao.com
 * @since 2011-3-17 ÉÏÎç09:40:22
 * @version 1.0
 */
public class Timer {
	
	private int seconds;
	
	private Wakeuper wakeuper;
	
	private ScheduledExecutorService scheduledExecutorService;
	
	public Timer(int seconds, Wakeuper wakeuper) {
		super();
		this.seconds = seconds;
		this.wakeuper = wakeuper;
		scheduledExecutorService = Executors.newScheduledThreadPool(1);
	}

	public void start() {
		scheduledExecutorService.schedule(new ScheduledThread(), seconds, TimeUnit.SECONDS);
		scheduledExecutorService.shutdown();
	}
	
	private class ScheduledThread implements Runnable {

		@Override
		public void run() {
			wakeuper.onWakeUp();
		}
		
	}
	
	public static interface Wakeuper {
		void onWakeUp();
	}

}
