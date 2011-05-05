package com.zjuh.xiaobai.sync;

import java.util.Map;

public class DbConfig {
	
	private String id;
	
	private String ip;
	
	private String dbName;
	
	private String user;
	
	private String pwd;
	
	private Map<String, TableConfig> tableConfigs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Map<String, TableConfig> getTableConfigs() {
		return tableConfigs;
	}

	public void setTableConfigs(Map<String, TableConfig> tableConfigs) {
		this.tableConfigs = tableConfigs;
	}

}
