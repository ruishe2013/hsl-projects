package com.aifuyun.snow.world.dal.daointerface.together;

import java.util.List;

import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;

public interface OrderUserDAO {
	
	long create(OrderUserDO orderUserDO);
	
	void delete(long id);
	
	OrderUserDO queryById(long id);
	
	void update(OrderUserDO orderUserDO);
	
	List<OrderUserDO> queryByOrderIdAndRole(long orderId, int role);
	
	List<OrderUserDO> queryByOrderId(long orderId);

}
