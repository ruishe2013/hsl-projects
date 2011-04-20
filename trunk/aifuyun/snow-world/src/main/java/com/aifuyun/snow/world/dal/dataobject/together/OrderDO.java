package com.aifuyun.snow.world.dal.dataobject.together;


import java.util.Date;

import com.aifuyun.snow.world.common.SnowConstants;
import com.aifuyun.snow.world.dal.dataobject.BaseDO;
import com.aifuyun.snow.world.dal.dataobject.enums.CarTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.CarOwnerTypeEnum;

/**
 * 拼车单
 * 
 * @author pister
 *
 */
public class OrderDO extends BaseDO {

	private static final long serialVersionUID = -1043735610343203644L;
	
	private long id;
	
	/**
	 * 所在城市id
	 */
	private int cityId;
	
	/**
	 * 到达城市id
	 */
	private int arriveCityId;
	
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
	 * 空余座位数
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
	
	/**
	 * 汽车类型
	 */
	private int carType;
	
	/**
	 * 状态
	 */
	private int status;
	
	/**
	 * 途径
	 */
	private String approach;
	
	/**
	 * 发起人是否有车
	 */
	private int creatorCarOwnerType;
	
	/**
	 * 车牌号
	 */
	private String carNo;
	
	/**
	 * 上班周期
	 */
	private String fromWeek;
	
	/**
	 * 下班出发时间
	 */
	private Date afterWorkFromTime;
	
	
	public void setFromWeekByArray(String[] fromWeekArray) {
		this.fromWeek = "";
		if (fromWeekArray != null && fromWeekArray.length > 0) {
			for (int i = 0; i < fromWeekArray.length; i++) {
				if (i != fromWeekArray.length - 1) {
					this.fromWeek += fromWeekArray[i] + SnowConstants.SPIIT;
					continue;
				}
				this.fromWeek += fromWeekArray[i];
			}
		}
	}
	
	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public CarOwnerTypeEnum getCreatorCarOwnerTypeEnum() {
		return CarOwnerTypeEnum.valueOf(creatorCarOwnerType);
	}
	
	public int getCreatorCarOwnerType() {
		return creatorCarOwnerType;
	}

	public void setCreatorCarOwnerType(int creatorCarOwnerType) {
		this.creatorCarOwnerType = creatorCarOwnerType;
	}

	public OrderStatusEnum getOrderStatusEnum() {
		return OrderStatusEnum.valueOf(status);
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public OrderTypeEnum getTogetherTypeEnum() {
		return OrderTypeEnum.valueOf(type);
	}
	
	public boolean isSfcAndPassenger() {
		if (type != OrderTypeEnum.SFC.getValue()) {
			return false;
		}
		if (creatorCarOwnerType != CarOwnerTypeEnum.PASSENGER.getValue()) {
			return false;
		}
		return true;
	}
	
	public boolean isWork() {
		return type == OrderTypeEnum.WORK.getValue();
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

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getArriveCityId() {
		return arriveCityId;
	}

	public void setArriveCityId(int arriveCityId) {
		this.arriveCityId = arriveCityId;
	}

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}
	
	public String getFromWeek() {
		return fromWeek;
	}

	public Date getAfterWorkFromTime() {
		return afterWorkFromTime;
	}

	public void setAfterWorkFromTime(Date afterWorkFromTime) {
		this.afterWorkFromTime = afterWorkFromTime;
	}

	public void setFromWeek(String fromWeek) {
		this.fromWeek = fromWeek;
	}

	public int getCarType() {
		return carType;
	}

	public void setCarType(int carType) {
		this.carType = carType;
	}

	public CarTypeEnum getCarTypeEnum() {
		return CarTypeEnum.valueOf(carType);
	}

}
