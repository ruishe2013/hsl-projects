package com.aifuyun.perftest;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.aifuyun.perftest.Timer.Wakeuper;


public class MainController implements Wakeuper {
	
	private long startTime;
	
	private Counter counter;
	
	private List<TestThread> testThreads;
	
	private ExecutorService executorService;
	
	private ErrorHandler errorHandler;
	
	private PrintWriter reportOut;
	
	private volatile boolean finished;
	
	private int onTimes;
	
	private int reportDelay;
	
	private CountDownLatch threadStop = null;
	
	public synchronized void init(Config config) {
		if (reportOut == null) {
			reportOut = new PrintWriter(System.out);
		}
		counter = new Counter();
		
		if (executorService != null) {
			if (!executorService.isShutdown()) {
				executorService.shutdown();
			}
		}
		executorService = Executors.newFixedThreadPool(config.getThreadCount() + 1);
		
		this.onTimes = config.getOnTimes();
		this.reportDelay = config.getReportDelay();
		
		errorHandler = new ErrorHandler(){

			@Override
			public boolean processError(Throwable t) {
				t.printStackTrace(reportOut);
				return true;
			}
		};
		
		finished = false;
		threadStop = new CountDownLatch(config.getThreadCount());
		testThreads = new ArrayList<TestThread>();
		for (int i = 0; i < config.getThreadCount(); ++i) {
			TestThread testThread = createTestThread();
			testThread.setCounter(counter);
			testThread.setErrorHandler(errorHandler);
			testThread.setThreadStop(threadStop);
			testThread.setUrls(new ArrayList<String>(config.getUrls()));
			testThreads.add(testThread);
		}
		
		
	}
	
	protected TestThread createTestThread() {
		return new RandomUrlTestJobThread();
	}
	
	private class ReportThread implements Runnable {

		@Override
		public void run() {
			while (!finished) {
				try {
					Thread.sleep(reportDelay * 1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				int qps = computeQps();
				reportOut.println("qps: " + qps + " @" + new Date());
				reportOut.flush();
			}
		}
		
	}
	
	protected int computeQps() {
		long nowTime = System.currentTimeMillis();
		long escape = (nowTime - startTime) / 1000;
		if (escape == 0) {
			return 0;
		}
		int qps = (int)(counter.getSuccess() / escape);
		return qps;
	}
	
	public void testPerf() {
		startTime = System.currentTimeMillis();
		new Timer(onTimes, this).start();
		reportOut.println("test perf start. @" + new Date());
		for (TestThread tt : testThreads) {
			executorService.submit(tt);
		}
		executorService.submit(new ReportThread());
		executorService.shutdown();
		
		try {
			threadStop.await();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void onWakeUp() {
		finished = true;
		for (TestThread tt : testThreads) {
			tt.stop();
		}
	}

	public PrintWriter getReportOut() {
		return reportOut;
	}

	public void setReportOut(PrintWriter reportOut) {
		this.reportOut = reportOut;
	}
	
	
}
