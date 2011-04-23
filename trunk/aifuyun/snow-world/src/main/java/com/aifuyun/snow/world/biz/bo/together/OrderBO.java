package com.aifuyun.snow.world.biz.bo.together;

import java.util.List;

import com.aifuyun.snow.world.biz.query.OrderQuery;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

/**
 * @author pister
 *
 */
public interface OrderBO {

	void delete(long id);
	
	/**
	 * 创建拼车单
	 * @param togetherOrderDO
	 * @return
	 */
	long createOrder(OrderDO togetherOrderDO);
	
	/**
	 * 根据id 查询拼单
	 * @param id
	 * @return
	 */
	OrderDO queryById(long id);

	/**
	 * 更新状态
	 * @param id
	 * @param orderStatus
	 */
	void updateStatus(long id, OrderStatusEnum orderStatus);
	
	List<OrderDO> queryRecentOrders(OrderQuery orderQuery);
	
	List<OrderDO> queryRecentTypeOrders(OrderQuery orderQuery);
	
}
