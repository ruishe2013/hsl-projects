package com.aifuyun.snow.world.biz.ao.together;

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
	 * ȡ��ƴ���������˷���
	 * @param orderId
	 * @return
	 */
	Result cancelOrder(long orderId);
	
	/**
	 * ȷ��ƴ��
	 * @param orderId
	 * @return
	 */
	Result confirmTogetherOrder(long orderId);
	
	/**
	 * �û������˳�
	 * @param id
	 * @return
	 */
	Result exitOrder(long id);
	
	/**
	 * �Ӷ������Ƴ��û�
	 * @param id
	 * @return
	 */
	Result removeUserFromOrder(long id);
	
	/**
	 * ��׼�û�����
	 * @param id 
	 * @param argee �Ƿ�ͬ����׼����
	 * @return
	 */
	Result confirmUserJoin(long id, boolean agree);
	
	/**
	 * ����ƴ��
	 * @param orderId
	 * @return
	 */
	Result joinOrder(OrderUserDO inputJoiner, long orderId, boolean saveToUserInfo);
	
	/**
	 * �������ƴ����ҳ��
	 * @param orderId
	 * @return
	 */
	Result viewOrderDetail(long orderId);
	
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
	 * ȷ�����ƴ��
	 * @param orderId
	 * @return
	 */
	Result confirmFinishOrder(long orderId);
	
	/**
	 * ����û���Ϣ
	 * @return
	 */
	Result viewPersonalInfoForOrder(long orderId, boolean join);
	
	/**
	 * �鿴�ҵ�ƴ��
	 * @return
	 * 
	 */
	Result viewMyOrders(UserOrderQuery userOrderQuery);
	
	Result viewCreateTaxiOrder();
	
}
