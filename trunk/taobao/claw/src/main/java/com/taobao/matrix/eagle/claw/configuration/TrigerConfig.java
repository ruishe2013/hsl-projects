package com.taobao.matrix.eagle.claw.configuration;

import java.util.ArrayList;
import java.util.List;

public class TrigerConfig extends TypedAttributes {
	
	private List<String> columns = new ArrayList<String>();

	public List<String> getColumns() {
		return columns;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

}
