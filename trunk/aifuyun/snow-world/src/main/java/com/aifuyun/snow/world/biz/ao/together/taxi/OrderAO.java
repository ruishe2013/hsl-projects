package com.aifuyun.snow.world.biz.ao.together.taxi;

import com.aifuyun.snow.world.biz.query.UserOrderQuery;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public interface OrderAO {

	/**
	 * 取消拼车（创建人发起）
	 * @param orderId
	 * @return
	 */
	Result cancelOrder(long orderId);
	
	/**
	 * 确认拼车
	 * @param orderId
	 * @return
	 */
	Result confirmTogetherOrder(long orderId);
	
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
	Result createOrder(OrderDO orderDO);
	
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
	 * 确认完成拼单
	 * @param orderId
	 * @return
	 */
	Result confirmFinishOrder(long orderId);
	
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
	Result viewMyOrders(UserOrderQuery userOrderQuery);
	
	Result viewCreateOrder(int type);
	
	Result viewCreateOrder(long orderId, int type);
	
}
