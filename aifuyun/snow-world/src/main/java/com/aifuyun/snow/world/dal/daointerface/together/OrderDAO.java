package com.aifuyun.snow.world.dal.daointerface.together;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

/**
 * @author pister
 *
 */
public interface OrderDAO {

	long create(OrderDO orderDO);
	
	void delete(long id);
	
	OrderDO queryById(long id);
	
	void update(OrderDO orderDO);
	
	void updateStatus(long id, OrderStatusEnum orderStatus);
	
	List<OrderDO> queryRecentOrders(int cityId);
	
}
