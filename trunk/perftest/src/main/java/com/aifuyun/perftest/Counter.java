package com.aifuyun.perftest;

import java.util.concurrent.atomic.AtomicLong;

public class Counter {
	
	private AtomicLong success;
	
	private AtomicLong failed;
	
	public Counter() {
		reset();
	}

	public void reset() {
		success = new AtomicLong(0);
		failed = new AtomicLong(0);
	}
	
	public void successIncr() {
		success.incrementAndGet();
	}
	
	public void failedIncr() {
		failed.incrementAndGet();
	}
	
	public long getSuccess() {
		return success.get();
	}
	
	public long getFailed() {
		return failed.get();
	}
	
	public long getTotal() {
		return success.get() + failed.get();
	}
	
}
