package com.aifuyun.snow.world.biz.query.search;

import java.util.ArrayList;
import java.util.List;

public class SearchQuery {

	private List<SortField> sortFields = new ArrayList<SortField>();
	
	private int rows = 20;
	
	private int startRow = 0;
	
	private String q;

	public List<SortField> getSortFields() {
		return sortFields;
	}

	public void setSortFields(List<SortField> sortFields) {
		this.sortFields = sortFields;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
}
