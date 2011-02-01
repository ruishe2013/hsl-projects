package com.aifuyun.snow.world.dal.dataobject.together;


import java.util.Date;

import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.TogetherTypeEnum;

/**
 * ƴ����
 * 
 * @author pister
 *
 */
public class TogetherOrderDO extends BaseDO {

	private static final long serialVersionUID = -1043735610343203644L;
	
	private long id;
	
	/**
	 * ������id
	 */
	private long creatorId;
	
	/**
	 * �������û���
	 */
	private String creatorUsername;
	
	/**
	 * ��������
	 */
	private String fromCity;
	
	/**
	 * ������
	 */
	private String fromAddr;
	
	/**
	 * �ﵽ����
	 */
	private String arriveCity;
	
	/**
	 * �ﵽ��
	 */
	private String arriveAddr;
	
	/**
	 * ����ʱ��
	 */
	private Date fromTime;
	
	/**
	 * ��Ԥ�ƣ��ﵽʱ��
	 */
	private Date arriveTime;
	
	/**
	 * ������λ��
	 */
	private int totalSeats;
	
	/**
	 * ����
	 */
	private String description;
	
	/**
	 * ����
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
