package com.aifuyun.snow.world.dal.daointerface.together;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;

public interface OrderUserDAO {
	
	void updateStatus(long id, OrderUserStatusEnum orderUserStatusEnum);
	
	List<OrderUserDO> queryByOrderAndUserId(long orderId, long userId);
	
	long create(OrderUserDO orderUserDO);
	
	void delete(long id);
	
	OrderUserDO queryById(long id);
	
	void update(OrderUserDO orderUserDO);
	
	List<OrderUserDO> queryByOrderIdAndRole(long orderId, int role);
	
	List<OrderUserDO> queryByOrderId(long orderId);
	
	List<OrderDO> queryOrdersByUserIdAndRole(long userId, int role);
	
	List<OrderUserDO> queryByOrderIdAndRoleAndStatus(long orderId, int role, int status);
}
