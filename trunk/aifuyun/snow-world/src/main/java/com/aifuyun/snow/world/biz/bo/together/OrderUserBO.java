package com.aifuyun.snow.world.biz.bo.together;

import java.util.List;

import com.aifuyun.snow.world.biz.query.UserOrderQuery;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;

public interface OrderUserBO {
	
	List<OrderUserDO> queryOrderJoinersByStatus(long orderId, int status);
	
	OrderUserDO queryOrderCreator(long orderId);

	long create(OrderUserDO orderUserDO);
	
	void update(OrderUserDO orderUserDO);
	
	void delete(long id);
	
	OrderUserDO queryById(long id);
	
	void updateStatus(long id, OrderUserStatusEnum orderUserStatusEnum);
	
	List<OrderDO> queryOrdersByUserIdAndRole(UserOrderQuery userOrderQuery);
	
	List<OrderUserDO> queryByOrderAndUserId(long orderId, long userId);
	
	List<OrderUserDO> queryOrderJoiners(long orderId);
}
