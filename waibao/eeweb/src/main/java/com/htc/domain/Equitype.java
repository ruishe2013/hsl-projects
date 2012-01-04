package com.htc.domain;

import java.io.Serializable;

public class Equitype implements Serializable {
	
	private static final long serialVersionUID = -6868936615006194313L;
	
	private int tyepId;
	private String typename;
	
	
	public int getTyepId() {
		return tyepId;
	}
	public void setTyepId(int tyepId) {
		this.tyepId = tyepId;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	
}
