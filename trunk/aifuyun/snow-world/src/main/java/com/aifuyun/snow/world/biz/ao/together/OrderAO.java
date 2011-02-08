package com.aifuyun.snow.world.biz.ao.together;

import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
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
	
	/**
	 * 创建者信息
	 * @param inputCreator
	 * @param orderId
	 * @param saveToUserInfo
	 * @return
	 */
	Result fillCreatorInfo(OrderUserDO inputCreator, long orderId, boolean saveToUserInfo);
	
	/**
	 * 浏览确认拼单
	 * @param orderId
	 * @return
	 */
	Result viewConfirmOrder(long orderId);
	
	/**
	 * 确认拼单
	 * @param orderId
	 * @return
	 */
	Result confirmOrder(long orderId);
	
	/**
	 * 浏览用户信息
	 * @return
	 */
	Result viewPersonalInfoForOrder(long orderId);
	
}
