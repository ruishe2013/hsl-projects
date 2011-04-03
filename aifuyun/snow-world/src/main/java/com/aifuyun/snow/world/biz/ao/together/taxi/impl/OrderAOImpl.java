package com.aifuyun.snow.world.biz.ao.together.taxi.impl;

import java.util.Collection;
import java.util.List;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.biz.bo.together.OrderUserBO;
import com.aifuyun.snow.world.biz.query.UserOrderQuery;
import com.aifuyun.snow.world.biz.resultcodes.CommonResultCodes;
import com.aifuyun.snow.world.biz.resultcodes.OrderResultCodes;
import com.aifuyun.snow.world.biz.resultcodes.UserResultCodes;
import com.aifuyun.snow.world.common.SnowUtil;
import com.aifuyun.snow.world.common.cache.CacheContants;
import com.aifuyun.snow.world.dal.dataobject.area.CityDO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.UserInfoHolder;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.lang.CollectionUtil;
import com.zjuh.sweet.lang.StringUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

/**
 * @author pister
 *
 */
public class OrderAOImpl extends BaseAO implements OrderAO {

	private OrderBO orderBO;
	
	private OrderUserBO orderUserBO;
	
	
	@Override
	public Result viewCreateOrder(int type) {
		Result result = new ResultSupport(false);
		try {
			OrderTypeEnum selectedOrderType = OrderTypeEnum.valueOf(type);
			if (selectedOrderType == null) {
				// Ĭ��ѡ��˳�ᳵ
				selectedOrderType = OrderTypeEnum.TAXI;
			}
			
			CityDO city = getSelectedCity(0);
			if (city != null) {
				OrderDO order = new OrderDO();
				order.setCityId(city.getId());
				order.setArriveCityId(city.getId());
				order.setFromCity(city.getName());
				order.setArriveCity(city.getName());
				
				// ��λ����ʱ����Ϊ4��
				order.setTotalSeats(4);		
				result.getModels().put("order", order);
			}
			
			
			result.getModels().put("orderTypes", OrderTypeEnum.values());
			result.getModels().put("selectedOrderType", selectedOrderType);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("�������ƴ��ҳ����", e);
		}
		return result;
	}

	@Override
	public Result cancelOrder(long orderId) {
		Result result = new ResultSupport(false);
		try {
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				// ƴ��������
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			final long userId = this.getLoginUserId();
			if (order.getCreatorId() != userId) {
				// ���ܱ༭�����˵�ƴ��
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			
			if (OrderStatusEnum.HAS_CONFIRM == order.getOrderStatusEnum()) {
				// �ö����Ѿ�ȷ���ˣ�����ȡ����
				result.setResultCode(OrderResultCodes.CAN_NOT_CANCEL_BY_CONFIRMED);
				return result;
			}
			
			orderBO.delete(orderId);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("ȡ��ƴ��ʧ��", e);
		}
		return result;
	}

	@Override
	public Result confirmTogetherOrder(long orderId) {
		Result result = new ResultSupport(false);
		try {
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			final long userId = this.getLoginUserId();
			if (order.getCreatorId() != userId) {
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			
			if (OrderStatusEnum.HAS_CONFIRM == order.getOrderStatusEnum()) {
				// �ö����Ѿ�ȷ���ˣ�����Ҫ��ȷ�ϡ�
				result.setResultCode(OrderResultCodes.ORDER_HAS_BEEN_CONFIRMED);
				return result;
			}
			
			if (OrderStatusEnum.WAIT_CONFIRM != order.getOrderStatusEnum()) {
				// ֻ��ȷ�ϴ�ȷ�ϵĶ���
				result.setResultCode(OrderResultCodes.ORDER_HAS_NOT_WAIT_CONFIRM);
				return result;
			}
			
			List<OrderUserDO> confirmedJoiners = this.orderUserBO.queryOrderJoinersByStatus(orderId, OrderUserStatusEnum.CONFIRM_PASSED.getValue());
			if (CollectionUtil.isEmpty(confirmedJoiners)) {
				result.setResultCode(OrderResultCodes.CAN_NOT_CONFIRM_EMPTY_JOINERS_ORDER);
				return result;
			}
			
			OrderUserDO orderUser = orderUserBO.queryOrderCreator(orderId);
			if (orderUser == null) {
				result.setResultCode(OrderResultCodes.CANNOT_FIND_ORDER_CREATOR);
				return result;
			}
			
			orderBO.updateStatus(orderId, OrderStatusEnum.HAS_CONFIRM);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("ȷ�϶���ʧ��", e);
		}
		return result;
	}

	@Override
	public Result exitOrder(long id) {
		Result result = new ResultSupport(false);
		try {
			long loginUserId = this.getLoginUserId();
			
			OrderUserDO orderUser = orderUserBO.queryById(id);
			if (orderUser == null) {
				// δ�����ƴ��
				result.setResultCode(OrderResultCodes.YOU_ARE_NOT_JOIN_YET);
				return result;
			}
			
			long orderId = orderUser.getOrderId();
			long userId = orderUser.getUserId();
			
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				// ƴ����������
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			if (userId != loginUserId) {
				// �����ñ����˳�
				result.setResultCode(OrderResultCodes.YOU_EXIT_WRONG_ORDER);
				return result;
			}
			
			if (OrderStatusEnum.HAS_CONFIRM == order.getOrderStatusEnum()) {
				// �ö����Ѿ�ȷ���ˣ������˳���
				result.setResultCode(OrderResultCodes.CAN_NOT_EXIT_ORDER_FOR_CONFIRMED);
				return result;
			}
			
			orderUserBO.delete(id);
			
			result.getModels().put("order", order);
 			result.setSuccess(true);
		} catch (Exception e) {
			log.error("�˳�ƴ��ʧ��", e);
		}
		return result;
	}

	@Override
	public Result removeUserFromOrder(long id) {
		Result result = new ResultSupport(false);
		try {
			long loginUserId = this.getLoginUserId();
			
			OrderUserDO orderUser = orderUserBO.queryById(id);
			if (orderUser == null) {
				// ���û���δ����ƴ��
				result.setResultCode(OrderResultCodes.USER_NOT_JOIN_YET);
				return result;
			}
			
			long orderId = orderUser.getOrderId();
			long userId = orderUser.getUserId();
			
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				// ƴ����������
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			if (order.getCreatorId() != loginUserId) {
				// �����߲��Ǳ���
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			if (userId == loginUserId) {
				// �����Ƴ�����
				result.setResultCode(OrderResultCodes.CANNOT_REMOVE_SELF);
				return result;
			}
			
			if (OrderStatusEnum.HAS_CONFIRM == order.getOrderStatusEnum()) {
				// �ö����Ѿ�ȷ���ˣ������Ƴ���
				result.setResultCode(OrderResultCodes.CAN_NOT_REMOVE_USER_FOR_CONFIRMED);
				return result;
			}
			
			orderUserBO.delete(id);
			
			result.getModels().put("order", order);
 			result.setSuccess(true);
		} catch (Exception e) {
			log.error("�Ƴ��û�ʧ��", e);
		}
		return result;
	}

	@Override
	public Result confirmUserJoin(long id, boolean agree) {
		Result result = new ResultSupport(false);
		try {
			long loginUserId = this.getLoginUserId();
			
			OrderUserDO orderUser = orderUserBO.queryById(id);
			if (orderUser == null) {
				// ���û���δ����ƴ��
				result.setResultCode(OrderResultCodes.USER_NOT_JOIN_YET);
				return result;
			}
			if (OrderUserStatusEnum.WAIT_CONFIRM.getValue() != orderUser.getStatus()) {
				// ֻ�ܲ���δȷ�ϵ��û�
				result.setResultCode(OrderResultCodes.ONLY_OPERATE_CONFIRM_JOIN);
				return result;
			}
			
			long orderId = orderUser.getOrderId();
			long userId = orderUser.getUserId();
			
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				// ƴ����������
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			if (order.getCreatorId() != loginUserId) {
				// �����߲��Ǳ���
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			if (OrderStatusEnum.WAIT_CONFIRM.getValue() != order.getStatus()) {
				// ֻ�ܲ�����ȷ�ϵ�ƴ����
				result.setResultCode(OrderResultCodes.ONLY_OPERATE_WAIT_CONFIRM);
				return result;
			}
			if (userId == loginUserId) {
				// ����ȷ�ϱ���
				result.setResultCode(OrderResultCodes.CANNOT_CONFIRM_SELF);
				return result;
			}
			BaseUserDO joinUser = userBO.queryById(userId);
			if (joinUser == null) {
				// �û�������
				result.setResultCode(UserResultCodes.USER_NOT_EXIST);
				return result;
			}
			
			if (!canConfirmJoin(orderId, userId, result)) {
				return result;
			}
			
			OrderUserStatusEnum targetStatus = agree ? OrderUserStatusEnum.CONFIRM_PASSED : OrderUserStatusEnum.CONFIRM_NOT_PASSED;
			orderUserBO.updateStatus(orderUser.getId(), targetStatus);
			
			result.getModels().put("order", order);
			
 			result.setSuccess(true);
		} catch (Exception e) {
			log.error("ȷ�ϼ���ƴ��ʧ��", e);
		}
		return result;
	}

	private boolean canConfirmJoin(long orderId, long userId, Result result) {
		List<OrderUserDO> orderUsers = orderUserBO.queryByOrderAndUserId(orderId, userId);
		if (orderUsers == null || orderUsers.isEmpty()) {
			return true;
		}
		for (OrderUserDO orderUser : orderUsers) {
			if (orderUser.getStatus() == OrderUserStatusEnum.CONFIRM_PASSED.getValue()) {
				result.setResultCode(OrderResultCodes.USER_HAS_BEEN_JOINED);
				return false;
			}
		}
		return true;
	}
	
	private boolean canJoin(long orderId, long userId, Result result) {
		List<OrderUserDO> orderUsers = orderUserBO.queryByOrderAndUserId(orderId, userId);
		if (orderUsers == null || orderUsers.isEmpty()) {
			return true;
		}
		for (OrderUserDO orderUser : orderUsers) {
			if (orderUser.getStatus() == OrderUserStatusEnum.WAIT_CONFIRM.getValue()) {
				result.setResultCode(OrderResultCodes.USER_IN_WAIT_CONFIRM);
				return false;
			}
			if (orderUser.getStatus() == OrderUserStatusEnum.CONFIRM_PASSED.getValue()) {
				result.setResultCode(OrderResultCodes.USER_HAS_BEEN_JOINED);
				return false;
			}
		}
		return true;
	}

	@Override
	public Result joinOrder(OrderUserDO inputJoiner, long orderId, boolean saveToUserInfo) {
		Result result = new ResultSupport(false);
		try {
			long userId = this.getLoginUserId();
			
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			
			// �Ƿ񴴽��߱���
			boolean isCreatorSelf = (order.getCreatorId() == userId) ? true : false;
			
			if (isCreatorSelf) {
				result.setResultCode(OrderResultCodes.ORDER_CREATED_BY_YOURSELF);
				return result;
			}
			
			if (!canJoin(orderId, userId, result)) {
				return result;
			}
			
			List<OrderUserDO> confirmedJoiners = this.orderUserBO.queryOrderJoinersByStatus(orderId, OrderUserStatusEnum.CONFIRM_PASSED.getValue());
			// ʣ����λ��
			int leftSeatCount = order.getTotalSeats() - confirmedJoiners.size();
			leftSeatCount = Math.max(leftSeatCount, 0);
			
			if (leftSeatCount <= 0) {
				result.setResultCode(OrderResultCodes.ORDER_SEAT_IS_FULL);
				return result;
			}
			
			final String username = this.getLoginUsername();
			
			OrderUserDO orderUserDO = inputJoiner;
			orderUserDO.setUserId(userId);
			orderUserDO.setUsername(username);
			orderUserDO.setOrderId(orderId);
			
			orderUserDO.setRole(OrderUserRoleEnum.JOINER.getValue());
			orderUserDO.setStatus(OrderUserStatusEnum.WAIT_CONFIRM.getValue());
			
			if (saveToUserInfo) {
				copyUserInfo(userId, orderUserDO, result);
			}
			
			orderUserBO.create(orderUserDO);
			
 			result.setSuccess(true);
		} catch (Exception e) {
			log.error("����ƴ��ʧ��", e);
		}
		return result;
	}


	@Override
	public Result viewOrderDetail(long orderId) {
		Result result = new ResultSupport(false);
		try {
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			long userId = this.getLoginUserId();
			
			// �Ƿ񴴽��߱���
			boolean isCreatorSelf = (order.getCreatorId() == userId) ? true : false;
			
			boolean showContactInfo = this.isUserLogin();
			
			// ������
			OrderUserDO creator = orderUserBO.queryOrderCreator(orderId);
			
			// ���вμ���
			List<OrderUserDO> joiners = orderUserBO.queryOrderJoiners(orderId);
			List<OrderUserDO> confirmedJoiners = this.orderUserBO.queryOrderJoinersByStatus(orderId, OrderUserStatusEnum.CONFIRM_PASSED.getValue());
			
			OrderUserDO joinedOrderUser = getJoinedOrderUser(userId, joiners);
			OrderUserStatusEnum userJoinStatus = null;
			if (joinedOrderUser != null) {
				userJoinStatus = joinedOrderUser.getStatusEnum();
			}
			boolean hasBeenConfirmJoin = hasBeenConfirmJoin(userJoinStatus);
			
			// ʣ����λ��
			int joinersCount = confirmedJoiners.size();
			boolean showConfirmOrderBtn = false;
			if (isCreatorSelf) {
				// �����ߵ�Ȩ��
				if (!confirmedJoiners.isEmpty()) {
					showConfirmOrderBtn = true;
				}
				
				// ��ȷ�ϵĲμ���
				List<OrderUserDO> waitConfirmJoiners = this.orderUserBO.queryOrderJoinersByStatus(orderId, OrderUserStatusEnum.WAIT_CONFIRM.getValue());
				result.getModels().put("waitConfirmJoiners", waitConfirmJoiners);
			}
			
			boolean showJoiners = false;
			if (isCreatorSelf || hasBeenConfirmJoin) {
				showJoiners = true;
			}
			result.getModels().put("order", order);
			result.getModels().put("isCreatorSelf", isCreatorSelf);
			if (userJoinStatus != null) {
				result.getModels().put("userJoinStatusValue", userJoinStatus.getValue());
			}
			result.getModels().put("confirmedJoiners", confirmedJoiners);
			result.getModels().put("hasBeenJoin", hasBeenConfirmJoin);
			result.getModels().put("showJoiners", showJoiners);
			result.getModels().put("joinersCount", joinersCount);
			result.getModels().put("showConfirmOrderBtn", showConfirmOrderBtn);
			result.getModels().put("joinedOrderUser", joinedOrderUser);
			result.getModels().put("creator", creator);
			result.getModels().put("showContactInfo", showContactInfo);
			
 			result.setSuccess(true);
		} catch (Exception e) {
			log.error("�鿴ƴ��detailʧ��", e);
		}
		return result;
	}
	
	private boolean hasBeenConfirmJoin(OrderUserStatusEnum userJoinStatus) {
		if (userJoinStatus == null) {
			return false;
		}
		return userJoinStatus == OrderUserStatusEnum.CONFIRM_PASSED;
	}
	
	private OrderUserDO getJoinedOrderUser(long userId, Collection<OrderUserDO> joiners) {
		for (OrderUserDO orderUser : joiners) {
			if (orderUser.getUserId() == userId) {
				return orderUser;
			}
		}
		return null;
	}
	
	@Override
	public Result viewMyOrders(UserOrderQuery userOrderQuery) {
		Result result = new ResultSupport(false);
		try {
			final long userId = this.getLoginUserId();
			final int roleValue = userOrderQuery.getRole();
			if (!isRoleValid(roleValue)) {
				result.setResultCode(OrderResultCodes.INVALID_ROLE_VALUE);
				return result;
			}
			userOrderQuery.setUserId(userId);
			List<OrderDO> orders = orderUserBO.queryOrdersByUserIdAndRole(userOrderQuery);
			result.getModels().put("orders", orders);
			result.getModels().put("userOrderQuery", userOrderQuery);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("�鿴�ҵĶ���ʧ��", e);
		}
		return result;
	}
	
	private boolean isRoleValid(int roleValue) {
		if (roleValue == 0) {
			return true;
		}
		OrderUserRoleEnum userRole = OrderUserRoleEnum.valueOf(roleValue);
		if (userRole == null) {
			return false;
		}
		return true;
	}

	@Override
	public Result confirmFinishOrder(long orderId) {
		Result result = new ResultSupport(false);
		try {
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			final long userId = this.getLoginUserId();
			if (order.getCreatorId() != userId) {
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			
			if (!OrderStatusEnum.NOT_EFFECT.equals(order.getOrderStatusEnum())) {
				result.setResultCode(OrderResultCodes.ORDER_HAS_BEEN_PROCESSED);
				return result;
			}
			
			OrderUserDO orderUser = orderUserBO.queryOrderCreator(orderId);
			if (orderUser == null) {
				result.setResultCode(OrderResultCodes.CANNOT_FIND_ORDER_CREATOR);
				return result;
			}
			
			if (!isUserInfoComplete(orderUser)) {
				result.setResultCode(OrderResultCodes.ORDER_CREATOR_INFO_NOT_COMPLETE);
				return result;
			}
			
			orderBO.updateStatus(orderId, OrderStatusEnum.WAIT_CONFIRM);
			
			// ����ʧЧ����
			cacheManager.delete(CacheContants.RECENT_CITY_ORDERS, order.getCityId());
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("ȷ�϶���ʧ��", e);
		}
		return result;
	}
	
	@Override
	public Result viewPersonalInfoForOrder(long orderId, boolean join) {
		Result result = new ResultSupport(false);
		try {
			long userId = this.getLoginUserId();
			BaseUserDO baseUserDO = userBO.queryById(userId);
			if (baseUserDO == null) {
				result.setResultCode(UserResultCodes.USER_NOT_EXIST);
				return result;
			}
			
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			
			List<OrderUserDO> orderUsers = orderUserBO.queryByOrderAndUserId(orderId, userId);
			OrderUserDO orderUserDO = null;
			if (orderUsers != null && !orderUsers.isEmpty()) {
				orderUserDO = orderUsers.get(0);
			}
			
			boolean creatorExist = false;
			if (orderUserDO == null) {
				orderUserDO = assignDefaultFromUser(baseUserDO);
			} else {
				creatorExist = true;
			}
			
			boolean isCreator = (order.getCreatorId() == userId);
			String actionEvent = null;
			if (join) {
				// ����
				if (isCreator) {
					result.setResultCode(OrderResultCodes.ORDER_CREATED_BY_YOURSELF);
					return result;
				}
				actionEvent = "joinOrder";
			} else {
				// ����
				if (order.getCreatorId() != userId) {
					result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
					return result;
				}
				
				actionEvent = "fillCreatorInfo";
			}
			
			 
			int selectedYear = SnowUtil.getSelectedYear(orderUserDO);
			boolean isUserInfoEmpty = isUserInfoEmpty(baseUserDO);
			
			BirthYearEnum[] years = BirthYearEnum.values();
			
			result.getModels().put("actionEvent", actionEvent);
			result.getModels().put("creatorExist", creatorExist);
			result.getModels().put("isUserInfoEmpty", isUserInfoEmpty);
			result.getModels().put("years", years);
			result.getModels().put("selectedYear", selectedYear);
			result.getModels().put("orderUser", orderUserDO);
			result.getModels().put("order", order);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("ȷ�ϸ�����Ϣʧ��", e);
		}
		return result;
	}
	

	private OrderUserDO assignDefaultFromUser(BaseUserDO baseUserDO) {
		OrderUserDO ret = new OrderUserDO();
		ret.setBirthYear(baseUserDO.getBirthYear());
		ret.setCareer(baseUserDO.getCareer());
		ret.setEmail(baseUserDO.getEmail());
		ret.setPhone(baseUserDO.getPhone());
		ret.setQq(baseUserDO.getQq());
		ret.setRealName(baseUserDO.getRealName());
		ret.setUserId(baseUserDO.getId());
		ret.setUsername(baseUserDO.getUsername());
		ret.setSex(baseUserDO.getSex());		
		return ret;
	}
	
	/**
	 * �û���Ϣ�Ƿ��Ѿ��ǿ�
	 * @param userInfoHolder
	 * @return
	 */
	private boolean isUserInfoEmpty(UserInfoHolder userInfoHolder) {
		if (StringUtil.isNotEmpty(userInfoHolder.getRealName())) {
			return false;
		}
		if (userInfoHolder.getBirthYear() > 0) {
			return false;
		}
		if (StringUtil.isNotEmpty(userInfoHolder.getCareer())) {
			return false;
		}
		if (StringUtil.isNotEmpty(userInfoHolder.getPhone())) {
			return false;
		}
		if (userInfoHolder.getSex() > 0 ) {
			return false;
		}
		return true;
	}
	
	/**
	 * �û���Ϣ�Ƿ�����
	 * @param userInfoHolder
	 * @return
	 */
	public boolean isUserInfoComplete(UserInfoHolder userInfoHolder) {
		if (StringUtil.isEmpty(userInfoHolder.getRealName())) {
			return false;
		}
		if (userInfoHolder.getBirthYear() <= 0) {
			return false;
		}
		if (StringUtil.isEmpty(userInfoHolder.getCareer())) {
			return false;
		}
		if (StringUtil.isEmpty(userInfoHolder.getPhone())) {
			return false;
		}
		if (userInfoHolder.getSex() <= 0 ) {
			return false;
		}
		return true;
	}


	@Override
	public Result viewConfirmOrder(long orderId) {
		Result result = new ResultSupport(false);
		try {
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			final long userId = this.getLoginUserId();
			if (order.getCreatorId() != userId) {
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			OrderUserDO creator = orderUserBO.queryOrderCreator(orderId);
			if (creator == null) {
				result.setResultCode(OrderResultCodes.CANNOT_FIND_ORDER_CREATOR);
				return result;
			}
			result.getModels().put("order", order);
			result.getModels().put("creator", creator);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("���ȷ�϶���ʧ��", e);
		}
		return result;
	}

	@Override
	public Result fillCreatorInfo(OrderUserDO inputCreator, long orderId, boolean saveToUserInfo) {
		Result result = new ResultSupport(false);
		try {
			OrderDO orderDO = orderBO.queryById(orderId);
			if (orderDO == null) {
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			final long userId = this.getLoginUserId();
			if (orderDO.getCreatorId() != userId) {
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			final String username = this.getLoginUsername();
			inputCreator.setUserId(userId);
			inputCreator.setUsername(username);
			
			BaseUserDO baseUser = userBO.queryById(userId);
			inputCreator.setEmail(baseUser.getEmail());
			
			insertOrUpdateOrderCreator(orderDO, inputCreator);
			
			if (saveToUserInfo) {
				copyUserInfo(userId, inputCreator, result);
			}
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("��䴴������Ϣʧ��", e);
		}
		return result;
	}
	
	private boolean copyUserInfo(long userId, OrderUserDO orderUserDO, Result result) {
		BaseUserDO baseUserDO = userBO.queryById(userId);
		if (baseUserDO == null) {
			result.setResultCode(UserResultCodes.USER_NOT_EXIST);
			return false;
		}
		copyUserHolderInfo(baseUserDO, orderUserDO);
		userBO.update(baseUserDO);
		return true;
	}
	
	private void insertOrUpdateOrderCreator(OrderDO orderDO, OrderUserDO input) {
		OrderUserDO creator = orderUserBO.queryOrderCreator(orderDO.getId());
		if (creator == null) {
			creator = new OrderUserDO();
			
			updateOrderUser(creator, input, orderDO);
			
			creator.setOrderId(orderDO.getId());
			creator.setOrderType(orderDO.getType());
			creator.setRole(OrderUserRoleEnum.CREATOR.getValue());
			
			orderUserBO.create(creator);
		} else {
			copyUserHolderInfo(creator, input);
			updateOrderUser(creator, input, orderDO);
			orderUserBO.update(creator);
		}
	}
	
	private void updateOrderUser(OrderUserDO dest, OrderUserDO src, OrderDO orderDO) {
		copyUserHolderInfo(dest, src);
		dest.setUsername(orderDO.getCreatorUsername());
		dest.setUserId(orderDO.getCreatorId());
		dest.setEmail(src.getEmail());
	}
	
	private void copyUserHolderInfo(UserInfoHolder dest, OrderUserDO src) {
		dest.setBirthYear(src.getBirthYear());
		dest.setCareer(src.getCareer());
		dest.setPhone(src.getPhone());
		dest.setQq(src.getQq());
		dest.setRealName(src.getRealName());
		dest.setSex(src.getSex());
	}

	@Override
	public Result createOrder(OrderDO orderDO) {
		Result result = new ResultSupport(false);
		try {
			final long userId = this.getLoginUserId();
			if (userId <= 0L) {
				result.setResultCode(CommonResultCodes.USER_NOT_LOGIN);
				return result;
			}
			final String username = this.getLoginUsername();
			orderDO.setCreatorId(userId);
			orderDO.setCreatorUsername(username);
			orderDO.setType(OrderTypeEnum.TAXI.getValue());
			
			// ��������id
			CityDO fromCityDO = this.cityBO.queryByName(orderDO.getFromCity());
			if (fromCityDO != null) {
				orderDO.setCityId(fromCityDO.getId());
			}
			long orderId = orderBO.createOrder(orderDO);
			
			result.getModels().put("orderId", orderId);			
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("����ƴ��ʧ��", e);
		}
		return result;
	}

	public void setOrderBO(OrderBO orderBO) {
		this.orderBO = orderBO;
	}

	public void setOrderUserBO(OrderUserBO orderUserBO) {
		this.orderUserBO = orderUserBO;
	}

}
