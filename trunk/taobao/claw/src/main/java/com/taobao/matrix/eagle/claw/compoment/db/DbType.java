package com.taobao.matrix.eagle.claw.compoment.db;

public enum DbType {

	MYSQL(1, "mysql"),
	ORACLE(2, "oracle")
	;
	
	private final int type;
	
	private final String name;
	
	private DbType(int type, String name) {
		this.type = type;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
}
