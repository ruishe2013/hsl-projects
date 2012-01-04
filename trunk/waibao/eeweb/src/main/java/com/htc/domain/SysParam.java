package com.htc.domain;

import java.io.Serializable;

public class SysParam implements Serializable {

	private static final long serialVersionUID = -8179248682328823217L;

	private String argsKey;
	private String argsValue;
	
	public SysParam(){}
	
	public String getArgsKey() {
		return argsKey;
	}
	public void setArgsKey(String argsKey) {
		this.argsKey = argsKey;
	}
	public String getArgsValue() {
		return argsValue;
	}
	public void setArgsValue(String argsValue) {
		this.argsValue = argsValue;
	}
	
}
