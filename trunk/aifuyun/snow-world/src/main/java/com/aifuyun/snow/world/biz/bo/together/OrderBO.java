package com.aifuyun.snow.world.biz.bo.together;

import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

/**
 * @author pister
 *
 */
public interface OrderBO {

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
	
}
