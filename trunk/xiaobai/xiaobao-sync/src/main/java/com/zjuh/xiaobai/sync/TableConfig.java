package com.zjuh.xiaobai.sync;

import java.util.Map;

public class TableConfig {
	
	private String name;
	
	private Map<String, String> columnNameMapping;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, String> getColumnNameMapping() {
		return columnNameMapping;
	}

	public void setColumnNameMapping(Map<String, String> columnNameMapping) {
		this.columnNameMapping = columnNameMapping;
	}
}
