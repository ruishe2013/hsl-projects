package com.aifuyun.snow.world.biz.query.search;

public class SortField {
	
	private String name;
	
	private FieldOrder fieldOrder;

	public SortField() {
		super();
	}

	public SortField(String name, FieldOrder fieldOrder) {
		super();
		this.name = name;
		this.fieldOrder = fieldOrder;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FieldOrder getFieldOrder() {
		return fieldOrder;
	}

	public void setFieldOrder(FieldOrder fieldOrder) {
		this.fieldOrder = fieldOrder;
	}

}
