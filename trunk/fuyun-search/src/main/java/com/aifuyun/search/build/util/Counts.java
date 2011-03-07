package com.aifuyun.search.build.util;

public class Counts {
	
	private int success;
	
	private int failed;
	
	public String reportToString() {
		return "success: " + success + ", failed: " + failed + ", total: " + getTotal();
	}
	
	public String toString() {
		return reportToString();
	}
	
	public void incrSuccess() {
		success ++;
	}
	
	public void incrFailed() {
		failed ++;
	}
	
	public void reset() {
		success = 0;
		failed = 0;
	}

	public int getTotal() {
		return success + failed;
	}
	
	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

}
