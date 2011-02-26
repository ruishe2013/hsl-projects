package com.aifuyun.snow.world.biz.ao.together;

import com.aifuyun.snow.world.biz.query.OrderQuery;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public interface OrderAO {

	/**
	 * 用户主动退出
	 * @param id
	 * @return
	 */
	Result exitOrder(long id);
	
	/**
	 * 从订单从移除用户
	 * @param id
	 * @return
	 */
	Result removeUserFromOrder(long id);
	
	/**
	 * 批准用户加入
	 * @param id 
	 * @param argee 是否同意批准加入
	 * @return
	 */
	Result confirmUserJoin(long id, boolean agree);
	
	/**
	 * 加入拼车
	 * @param orderId
	 * @return
	 */
	Result joinOrder(OrderUserDO inputJoiner, long orderId, boolean saveToUserInfo);
	
	/**
	 * 浏览加入拼车单页面
	 * @param orderId
	 * @return
	 */
	Result viewOrderDetail(long orderId);
	
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
	Result viewPersonalInfoForOrder(long orderId, boolean join);
	
	/**
	 * 查看我的拼单
	 * @return
	 * 
	 */
	// TODO 单元测试待完成
	Result viewMyOrders(OrderQuery orderQuery);
	
	/**
	 * 查看最近拼车
	 * @param cityId
	 * @return
	 */
	Result handleForIndex(int cityId);
	
}
