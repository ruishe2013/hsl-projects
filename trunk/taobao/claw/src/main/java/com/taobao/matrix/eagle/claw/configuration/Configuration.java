package com.taobao.matrix.eagle.claw.configuration;

import java.util.Map;

public class Configuration {
	
	private String rangeFormat;
	
	private TypedAttributes storeEngineConfig;
	
	private Map<String, DbConfig> dbs;
	
	private DumperConfig dumperConfig;

	public String getRangeFormat() {
		return rangeFormat;
	}

	public void setRangeFormat(String rangeFormat) {
		this.rangeFormat = rangeFormat;
	}

	public TypedAttributes getStoreEngineConfig() {
		return storeEngineConfig;
	}

	public void setStoreEngineConfig(TypedAttributes storeEngineConfig) {
		this.storeEngineConfig = storeEngineConfig;
	}

	public Map<String, DbConfig> getDbs() {
		return dbs;
	}

	public void setDbs(Map<String, DbConfig> dbs) {
		this.dbs = dbs;
	}

	public DumperConfig getDumperConfig() {
		return dumperConfig;
	}

	public void setDumperConfig(DumperConfig dumperConfig) {
		this.dumperConfig = dumperConfig;
	}
	
}
