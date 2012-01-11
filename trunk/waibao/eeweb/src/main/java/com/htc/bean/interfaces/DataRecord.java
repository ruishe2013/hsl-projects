package com.htc.bean.interfaces;

import java.io.Serializable;

public class DataRecord implements Serializable {

	private static final long serialVersionUID = -254906787908496313L;
	
	/**
	 * 仪表编号
	 */
	private String dsrsn;
	
	/**
	 * 状态
	 */
	private String state;

	/**
	 * 仪表地址
	 */
	private String address;
	
	/**
	 * 区域名
	 */
	private String placeName;
	
	/**
	 * 记录时间
	 */
	private String recordTime;
	
	/**
	 * 温度
	 */
	private String temperature;
	
	/**
	 * 湿度
	 */
	private String humidity;

	public String getDsrsn() {
		return dsrsn;
	}

	public void setDsrsn(String dsrsn) {
		this.dsrsn = dsrsn;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	
}
