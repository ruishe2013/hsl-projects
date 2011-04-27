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
	 * ����ƴ����
	 * @param togetherOrderDO
	 * @return
	 */
	long createOrder(OrderDO togetherOrderDO);
	
	/**
	 * ����id ��ѯƴ��
	 * @param id
	 * @return
	 */
	OrderDO queryById(long id);

	/**
	 * ����״̬
	 * @param id
	 * @param orderStatus
	 */
	void updateStatus(long id, OrderStatusEnum orderStatus);
	
	void updateOrder(OrderDO newOrderDO);
	
	List<OrderDO> queryRecentOrders(OrderQuery orderQuery);
	
	List<OrderDO> queryRecentTypeOrders(OrderQuery orderQuery);
	
}
