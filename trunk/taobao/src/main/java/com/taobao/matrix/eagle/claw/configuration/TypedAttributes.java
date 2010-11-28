package com.taobao.matrix.eagle.claw.configuration;

import java.util.HashMap;
import java.util.Map;

public class TypedAttributes {
	
	private String type;
	
	private Map<String, String> attributes = new HashMap<String, String>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
