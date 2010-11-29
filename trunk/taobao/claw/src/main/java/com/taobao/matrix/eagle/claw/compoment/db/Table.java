package com.taobao.matrix.eagle.claw.compoment.db;

public class Table {

	private Database database;
	
	private String name;
	
	private String orderBy;
	
	private String rangeFormat;
	
	private int rangeStart;
	
	private int rangeEnd;

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public int getRangeStart() {
		return rangeStart;
	}

	public void setRangeStart(int rangeStart) {
		this.rangeStart = rangeStart;
	}

	public int getRangeEnd() {
		return rangeEnd;
	}

	public void setRangeEnd(int rangeEnd) {
		this.rangeEnd = rangeEnd;
	}
	
}
