package com.taobao.matrix.eagle.claw.configuration;

public class TableConfig {

	private String db;
	
	private String name;
	
	private String range;
	
	private String orderBy;
	
	private String rangeFormat;

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getRangeFormat() {
		return rangeFormat;
	}

	public void setRangeFormat(String rangeFormat) {
		this.rangeFormat = rangeFormat;
	}
	
}
