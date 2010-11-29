package com.taobao.matrix.eagle.claw.configuration;

import java.util.ArrayList;
import java.util.List;

public class DispatcherConfig extends TypedAttributes {

	private List<TableConfig> tables = new ArrayList<TableConfig>();

	public List<TableConfig> getTables() {
		return tables;
	}

	public void setTables(List<TableConfig> tables) {
		this.tables = tables;
	}
	
}
