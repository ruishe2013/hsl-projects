package com.aifuyun.snow.world.biz.query;

import com.zjuh.sweet.query.Query;

public class SearchOrderQuery extends Query {

	private static final long serialVersionUID = -7077576553399209873L;

	String fromCity;
	
	String fromAddr;
	
	String arriveCity;
	
	String arriveAddr;

	public String getFromCity() {
		return fromCity;
	}

	public void setFromCity(String fromCity) {
		this.fromCity = fromCity;
	}

	public String getFromAddr() {
		return fromAddr;
	}

	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}

	public String getArriveAddr() {
		return arriveAddr;
	}

	public void setArriveAddr(String arriveAddr) {
		this.arriveAddr = arriveAddr;
	}

	public String getArriveCity() {
		return arriveCity;
	}

	public void setArriveCity(String arriveCity) {
		this.arriveCity = arriveCity;
	}

}
