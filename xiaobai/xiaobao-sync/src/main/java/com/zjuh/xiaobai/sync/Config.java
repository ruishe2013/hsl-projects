package com.zjuh.xiaobai.sync;

public class Config {
	
	private String logDir;
	
	private int scanDelay;
	
	private int reConnectDb;
	
	private int startDelay;
	
	private DbConfig srcDbConfig;
	
	private DbConfig destDbConfig;

	public String getLogDir() {
		return logDir;
	}

	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}

	public int getScanDelay() {
		return scanDelay;
	}

	public void setScanDelay(int scanDelay) {
		this.scanDelay = scanDelay;
	}

	public int getReConnectDb() {
		return reConnectDb;
	}

	public void setReConnectDb(int reConnectDb) {
		this.reConnectDb = reConnectDb;
	}

	public int getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(int startDelay) {
		this.startDelay = startDelay;
	}

	public DbConfig getSrcDbConfig() {
		return srcDbConfig;
	}

	public void setSrcDbConfig(DbConfig srcDbConfig) {
		this.srcDbConfig = srcDbConfig;
	}

	public DbConfig getDestDbConfig() {
		return destDbConfig;
	}

	public void setDestDbConfig(DbConfig destDbConfig) {
		this.destDbConfig = destDbConfig;
	}

}
