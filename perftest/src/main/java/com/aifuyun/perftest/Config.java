package com.aifuyun.perftest;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author longyi E-mail:longyi@taobao.com
 * @since 2011-3-17 ����09:10:16
 * @version 1.0
 */
public class Config implements Serializable {
	
	private static final long serialVersionUID = 6431550507003456354L;

	/**
	 * �߳���
	 */
	private int threadCount = 100;
	
	/**
	 * Ŀ��url
	 */
	private Set<String> urls = new HashSet<String>();
	
	/**
	 * ����ʱ��(��)
	 */
	private int onTimes = 10;
	
	/**
	 * ����ʱ��(��)
	 */
	private int reportDelay = 1;
	
	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
	
	public void addUrl(String url) {
		this.urls.add(url);
	}

	public int getOnTimes() {
		return onTimes;
	}

	public void setOnTimes(int onTimes) {
		this.onTimes = onTimes;
	}

	public int getReportDelay() {
		return reportDelay;
	}

	public void setReportDelay(int reportDelay) {
		this.reportDelay = reportDelay;
	}


}
