package com.aifuyun.snow.world.biz.ao.together;

import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public interface OrderAO {

	/**
	 * 创建出租车拼单
	 * @param orderDO
	 * @return
	 */
	Result createTaxiOrder(OrderDO orderDO);
	
}
