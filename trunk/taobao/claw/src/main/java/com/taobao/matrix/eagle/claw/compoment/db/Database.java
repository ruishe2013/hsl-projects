package com.taobao.matrix.eagle.claw.compoment.db;

import javax.sql.DataSource;

public abstract class Database {
	
	private String id;
	
	private DataSource dataSource;
	
	private DbType dbType;

	protected abstract QueryGenerator getQueryGenerator();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public DbType getDbType() {
		return dbType;
	}

	public void setDbType(DbType dbType) {
		this.dbType = dbType;
	}

}
