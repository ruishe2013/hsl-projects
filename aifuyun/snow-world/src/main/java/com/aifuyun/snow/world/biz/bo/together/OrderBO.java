package com.aifuyun.snow.world.biz.bo.together;

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
	
}
