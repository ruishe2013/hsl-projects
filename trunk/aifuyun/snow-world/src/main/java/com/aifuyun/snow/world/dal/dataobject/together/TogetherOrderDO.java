package com.aifuyun.snow.world.dal.dataobject.together;


import java.util.Date;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.TogetherTypeEnum;

/**
 * 拼车单
 * 
 * @author pister
 *
 */
public class TogetherOrderDO extends BaseDO {

	private static final long serialVersionUID = -1043735610343203644L;
	
	private long id;
	
	/**
	 * 创建者id
	 */
	private long creatorId;
	
	/**
	 * 创建者用户名
	 */
	private String creatorUsername;
	
	/**
	 * 出发城市
	 */
	private String fromCity;
	
	/**
	 * 出发地
	 */
	private String fromAddr;
	
	/**
	 * 达到城市
	 */
	private String arriveCity;
	
	/**
	 * 达到地
	 */
	private String arriveAddr;
	
	/**
	 * 出发时间
	 */
	private Date fromTime;
	
	/**
	 * （预计）达到时间
	 */
	private Date arriveTime;
	
	/**
	 * 总用座位数
	 */
	private int totalSeats;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 类型
	 */
	private int type;

	public TogetherTypeEnum getTogetherTypeEnum() {
		return TogetherTypeEnum.valueOf(type);
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorUsername() {
		return creatorUsername;
	}

	public void setCreatorUsername(String creatorUsername) {
		this.creatorUsername = creatorUsername;
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

	public String getArriveCity() {
		return arriveCity;
	}

	public void setArriveCity(String arriveCity) {
		this.arriveCity = arriveCity;
	}

	public String getArriveAddr() {
		return arriveAddr;
	}

	public void setArriveAddr(String arriveAddr) {
		this.arriveAddr = arriveAddr;
	}

	public Date getFromTime() {
		return fromTime;
	}

	public void setFromTime(Date fromTime) {
		this.fromTime = fromTime;
	}

	public Date getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(Date arriveTime) {
		this.arriveTime = arriveTime;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
