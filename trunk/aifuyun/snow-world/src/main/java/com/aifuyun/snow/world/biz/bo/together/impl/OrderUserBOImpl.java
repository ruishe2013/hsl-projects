package com.aifuyun.snow.world.biz.bo.together.impl;

import java.util.List;

import com.aifuyun.snow.world.biz.bo.together.OrderUserBO;
import com.aifuyun.snow.world.dal.daointerface.together.OrderUserDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;

public class OrderUserBOImpl implements OrderUserBO {

	private OrderUserDAO orderUserDAO;
	
	@Override
	public long create(OrderUserDO orderUserDO) {
		return orderUserDAO.create(orderUserDO);
	}

	@Override
	public void update(OrderUserDO orderUserDO) {
		orderUserDAO.update(orderUserDO);
	}

	@Override
	public OrderUserDO queryOrderCreator(long orderId) {
		List<OrderUserDO> creators = orderUserDAO.queryByOrderIdAndRole(orderId, OrderUserRoleEnum.CREATOR.getValue());
		if (creators == null || creators.isEmpty()) {
			return null;
		}
		return creators.get(0);
	}

	public void setOrderUserDAO(OrderUserDAO orderUserDAO) {
		this.orderUserDAO = orderUserDAO;
	}

}
