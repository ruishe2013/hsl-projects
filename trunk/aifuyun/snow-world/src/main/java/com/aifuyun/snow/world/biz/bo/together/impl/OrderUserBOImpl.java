package com.aifuyun.snow.world.biz.bo.together.impl;

import java.util.List;

import com.aifuyun.snow.world.biz.bo.together.OrderUserBO;
import com.aifuyun.snow.world.dal.daointerface.together.OrderUserDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;

public class OrderUserBOImpl implements OrderUserBO {

	private OrderUserDAO orderUserDAO;
	
	@Override
	public OrderUserDO queryById(long id) {
		return orderUserDAO.queryById(id);
	}
	
	@Override
	public void updateStatus(long id, OrderUserStatusEnum orderUserStatusEnum) {
		orderUserDAO.updateStatus(id, orderUserStatusEnum);
	}

	public List<OrderUserDO> queryByOrderIdAndRoleAndStatus(long orderId, int role, int status) {
		return orderUserDAO.queryByOrderIdAndRoleAndStatus(orderId, role, status);
	}
	
	@Override
	public List<OrderUserDO> queryOrderJoinersByStatus(long orderId, int status) {
		return orderUserDAO.queryByOrderIdAndRoleAndStatus(orderId, OrderUserRoleEnum.JOINER.getValue(), status);
	}

	@Override
	public List<OrderUserDO> queryByOrderAndUserId(long orderId, long userId) {
		return orderUserDAO.queryByOrderAndUserId(orderId, userId);
	}

	@Override
	public List<OrderUserDO> queryOrderJoiners(long orderId) {
		return orderUserDAO.queryByOrderIdAndRole(orderId, OrderUserRoleEnum.JOINER.getValue());
	}

	@Override
	public List<OrderDO> queryOrdersByUserIdAndRole(long userId, int role) {
		return orderUserDAO.queryOrdersByUserIdAndRole(userId, role);
	}

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
