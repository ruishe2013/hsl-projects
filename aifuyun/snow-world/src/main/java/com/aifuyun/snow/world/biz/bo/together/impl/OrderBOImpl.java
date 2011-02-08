package com.aifuyun.snow.world.biz.bo.together.impl;

import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.dal.daointerface.together.OrderDAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

public class OrderBOImpl implements OrderBO {

	private OrderDAO orderDAO;
	
	@Override
	public long createOrder(OrderDO orderDO) {
		return orderDAO.create(orderDO);
	}

	public OrderDO queryById(long id) {
		return orderDAO.queryById(id);
	}
	
	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

}
