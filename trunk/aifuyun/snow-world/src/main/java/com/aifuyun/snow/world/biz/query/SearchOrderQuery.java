package com.aifuyun.snow.world.biz.query;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.zjuh.sweet.query.Query;

public class SearchOrderQuery extends Query {

	private static final long serialVersionUID = -7077576553399209873L;

	private String fromCity;

	private String fromAddr;

	private String arriveCity;

	private String arriveAddr;

	private boolean ignoreStartFromTime = false;

	private long fromTime;

	private long arriveTime;

	private long minArriveTime, maxArriveTime;

	private List<OrderStatusEnum> excludeStatus;

	public long getFromTime() {
		return fromTime;
	}

	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public long getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(long arriveTime) {
		this.arriveTime = arriveTime;
	}

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

	public boolean isIgnoreStartFromTime() {
		return ignoreStartFromTime;
	}

	public void setIgnoreStartFromTime(boolean ignoreStartFromTime) {
		this.ignoreStartFromTime = ignoreStartFromTime;
	}

	public long getMinArriveTime() {
		return minArriveTime;
	}

	public void setMinArriveTime(long minArriveTime) {
		this.minArriveTime = minArriveTime;
	}

	public long getMaxArriveTime() {
		return maxArriveTime;
	}

	public void setMaxArriveTime(long maxArriveTime) {
		this.maxArriveTime = maxArriveTime;
	}

	public List<OrderStatusEnum> getExcludeStatus() {
		return excludeStatus;
	}

	public void setExcludeStatus(List<OrderStatusEnum> excludeStatus) {
		this.excludeStatus = excludeStatus;
	}

}
