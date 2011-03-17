package com.aifuyun.perftest;

public class RandomUrlTestJobThread extends TestThread {

	@Override
	protected TestJob getTestJob() {
		return new RandomUrlTestJob();
	}

}
