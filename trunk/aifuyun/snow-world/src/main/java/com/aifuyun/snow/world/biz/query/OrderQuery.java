package com.aifuyun.snow.world.biz.query;

import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.zjuh.sweet.query.Query;

public class OrderQuery extends Query {

	private static final long serialVersionUID = -7175758511016468277L;

	private int orderUserRole = OrderUserRoleEnum.CREATOR.getValue();

	private int orderStatus;
	
	private int cityId;
	
	private int type;
	
	public int getOrderUserRole() {
		return orderUserRole;
	}

	public void setOrderUserRole(int orderUserRole) {
		this.orderUserRole = orderUserRole;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
