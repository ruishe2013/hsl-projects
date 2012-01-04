package com.htc.domain;

import java.io.Serializable;

public class Power implements Serializable {
	
	private static final long serialVersionUID = -7782211038002612083L;
	
	private int powerId;
	private String powerName;
	
	public int getPowerId() {
		return powerId;
	}
	public void setPowerId(int powerId) {
		this.powerId = powerId;
	}
	public String getPowerName() {
		return powerName;
	}
	public void setPowerName(String powerName) {
		this.powerName = powerName;
	}
}
