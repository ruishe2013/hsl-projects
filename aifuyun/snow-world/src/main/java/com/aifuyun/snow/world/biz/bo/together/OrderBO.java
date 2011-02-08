package com.aifuyun.snow.world.biz.bo.together;

import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;

/**
 * @author pister
 *
 */
public interface OrderBO {

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
	
}
