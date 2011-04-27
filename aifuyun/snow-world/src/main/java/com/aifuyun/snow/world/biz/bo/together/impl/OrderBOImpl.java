package com.aifuyun.snow.world.biz.bo.together.impl;

import java.util.List;

import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.biz.query.OrderQuery;
import com.aifuyun.snow.world.dal.daointerface.together.OrderDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

public class OrderBOImpl implements OrderBO {

	private OrderDAO orderDAO;
	
	@Override
	public void delete(long id) {
		orderDAO.delete(id);
	}

	public List<OrderDO> queryRecentOrders(OrderQuery orderQuery) {
		return orderDAO.queryRecentOrders(orderQuery);
	}

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

	@Override
	public void updateStatus(long id, OrderStatusEnum orderStatus) {
		orderDAO.updateStatus(id, orderStatus);
	}
	
	@Override
	public void updateOrder(OrderDO newOrderDO) {
		 orderDAO.update(newOrderDO);
	}

	public List<OrderDO> queryRecentTypeOrders(OrderQuery orderQuery) {
		return orderDAO.queryRecentTypeOrders(orderQuery);
	}
	
}
