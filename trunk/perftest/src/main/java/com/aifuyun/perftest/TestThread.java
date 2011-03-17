package com.aifuyun.perftest;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class TestThread implements Runnable {

	private ErrorHandler errorHandler;
	
	private volatile boolean stopped = false;
	
	private Counter counter;

	private List<String> urls;
	
	private CountDownLatch threadStop;
	
	public synchronized void stop() {
		stopped = true;
	}

	protected abstract TestJob getTestJob();

	@Override
	public void run() {
		try {
			TestJob testJob = getTestJob();
			while (!stopped) {
				try {
					testJob.doTest(urls);
					counter.successIncr();
				} catch (Exception e) {
					counter.failedIncr();
					if (errorHandler.processError(e)) {
						continue;
					} else {
						break;
					}
				}
			}
		} finally {
			threadStop.countDown();
		}
	}

	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}

	public void setErrorHandler(ErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	public Counter getCounter() {
		return counter;
	}

	public void setCounter(Counter counter) {
		this.counter = counter;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	public CountDownLatch getThreadStop() {
		return threadStop;
	}

	public void setThreadStop(CountDownLatch threadStop) {
		this.threadStop = threadStop;
	}

	
}
