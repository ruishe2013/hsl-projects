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
	 * �������⳵ƴ��
	 * @param orderDO
	 * @return
	 */
	Result createTaxiOrder(OrderDO orderDO);
	
	/**
	 * ��������Ϣ
	 * @param inputCreator
	 * @param orderId
	 * @param saveToUserInfo
	 * @return
	 */
	Result fillCreatorInfo(OrderUserDO inputCreator, long orderId, boolean saveToUserInfo);
	
	/**
	 * ���ȷ��ƴ��
	 * @param orderId
	 * @return
	 */
	Result viewConfirmOrder(long orderId);
	
	/**
	 * ȷ��ƴ��
	 * @param orderId
	 * @return
	 */
	Result confirmOrder(long orderId);
	
	/**
	 * ����û���Ϣ
	 * @return
	 */
	Result viewPersonalInfoForOrder(long orderId);
	
}
