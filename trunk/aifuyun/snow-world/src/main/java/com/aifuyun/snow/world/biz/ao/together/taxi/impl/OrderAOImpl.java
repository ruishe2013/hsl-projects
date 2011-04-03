package com.aifuyun.snow.world.biz.ao.together.taxi.impl;

import java.util.Collection;
import java.util.Date;
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
import com.zjuh.sweet.lang.DateUtil;
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
	
	private int defaultCreateTimeDelay = 3600 * 2; // 两个小时
	
	@Override
	public Result viewCreateOrder(int type) {
		Result result = new ResultSupport(false);
		try {
			OrderTypeEnum selectedOrderType = OrderTypeEnum.valueOf(type);
			if (selectedOrderType == null) {
				// 默认选择顺丰车
				selectedOrderType = OrderTypeEnum.TAXI;
			}
			
			CityDO city = getSelectedCity(0);
			if (city != null) {
				OrderDO order = new OrderDO();
				order.setCityId(city.getId());
				order.setArriveCityId(city.getId());
				order.setFromCity(city.getName());
				order.setArriveCity(city.getName());
				
				// 座位数暂时设置为4个
				order.setTotalSeats(4);		
				result.getModels().put("order", order);
			}
			
			
			Date defaultCreateDate = DateUtil.addSecond(new Date(), defaultCreateTimeDelay);
			
			int fromMinute = SnowUtil.getRecentMinute(defaultCreateDate, 5);
			int fromHour = DateUtil.getHour(defaultCreateDate);
			
			result.getModels().put("fromMinute", fromMinute);
			result.getModels().put("fromHour", fromHour);
			result.getModels().put("defaultCreateDate", defaultCreateDate);
			result.getModels().put("orderTypes", OrderTypeEnum.values());
			result.getModels().put("selectedOrderType", selectedOrderType);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("浏览创建拼车页出错", e);
		}
		return result;
	}

	@Override
	public Result cancelOrder(long orderId) {
		Result result = new ResultSupport(false);
		try {
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				// 拼车不存在
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			final long userId = this.getLoginUserId();
			if (order.getCreatorId() != userId) {
				// 不能编辑其他人的拼车
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			
			if (OrderStatusEnum.HAS_CONFIRM == order.getOrderStatusEnum()) {
				// 该订单已经确认了，不能取消。
				result.setResultCode(OrderResultCodes.CAN_NOT_CANCEL_BY_CONFIRMED);
				return result;
			}
			
			orderBO.delete(orderId);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("取消拼车失败", e);
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
				// 该订单已经确认了，不需要再确认。
				result.setResultCode(OrderResultCodes.ORDER_HAS_BEEN_CONFIRMED);
				return result;
			}
			
			if (OrderStatusEnum.WAIT_CONFIRM != order.getOrderStatusEnum()) {
				// 只能确认待确认的订单
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
			log.error("确认订单失败", e);
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
				// 未加入该拼车
				result.setResultCode(OrderResultCodes.YOU_ARE_NOT_JOIN_YET);
				return result;
			}
			
			long orderId = orderUser.getOrderId();
			long userId = orderUser.getUserId();
			
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				// 拼车单不存在
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			if (userId != loginUserId) {
				// 不能让别人退出
				result.setResultCode(OrderResultCodes.YOU_EXIT_WRONG_ORDER);
				return result;
			}
			
			if (OrderStatusEnum.HAS_CONFIRM == order.getOrderStatusEnum()) {
				// 该订单已经确认了，不能退出。
				result.setResultCode(OrderResultCodes.CAN_NOT_EXIT_ORDER_FOR_CONFIRMED);
				return result;
			}
			
			orderUserBO.delete(id);
			
			result.getModels().put("order", order);
 			result.setSuccess(true);
		} catch (Exception e) {
			log.error("退出拼车失败", e);
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
				// 该用户还未加入拼车
				result.setResultCode(OrderResultCodes.USER_NOT_JOIN_YET);
				return result;
			}
			
			long orderId = orderUser.getOrderId();
			long userId = orderUser.getUserId();
			
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				// 拼车单不存在
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			if (order.getCreatorId() != loginUserId) {
				// 创建者不是本人
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			if (userId == loginUserId) {
				// 不能移除本人
				result.setResultCode(OrderResultCodes.CANNOT_REMOVE_SELF);
				return result;
			}
			
			if (OrderStatusEnum.HAS_CONFIRM == order.getOrderStatusEnum()) {
				// 该订单已经确认了，不能移除。
				result.setResultCode(OrderResultCodes.CAN_NOT_REMOVE_USER_FOR_CONFIRMED);
				return result;
			}
			
			orderUserBO.delete(id);
			
			result.getModels().put("order", order);
 			result.setSuccess(true);
		} catch (Exception e) {
			log.error("移除用户失败", e);
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
				// 该用户还未加入拼车
				result.setResultCode(OrderResultCodes.USER_NOT_JOIN_YET);
				return result;
			}
			if (OrderUserStatusEnum.WAIT_CONFIRM.getValue() != orderUser.getStatus()) {
				// 只能操作未确认的用户
				result.setResultCode(OrderResultCodes.ONLY_OPERATE_CONFIRM_JOIN);
				return result;
			}
			
			long orderId = orderUser.getOrderId();
			long userId = orderUser.getUserId();
			
			OrderDO order = orderBO.queryById(orderId);
			if (order == null) {
				// 拼车单不存在
				result.setResultCode(OrderResultCodes.ORDER_NOT_EXIST);
				return result;
			}
			if (order.getCreatorId() != loginUserId) {
				// 创建者不是本人
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			if (OrderStatusEnum.WAIT_CONFIRM.getValue() != order.getStatus()) {
				// 只能操作待确认的拼车单
				result.setResultCode(OrderResultCodes.ONLY_OPERATE_WAIT_CONFIRM);
				return result;
			}
			if (userId == loginUserId) {
				// 不能确认本人
				result.setResultCode(OrderResultCodes.CANNOT_CONFIRM_SELF);
				return result;
			}
			BaseUserDO joinUser = userBO.queryById(userId);
			if (joinUser == null) {
				// 用户不存在
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
			log.error("确认加入拼车失败", e);
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
			
			// 是否创建者本人
			boolean isCreatorSelf = (order.getCreatorId() == userId) ? true : false;
			
			if (isCreatorSelf) {
				result.setResultCode(OrderResultCodes.ORDER_CREATED_BY_YOURSELF);
				return result;
			}
			
			if (!canJoin(orderId, userId, result)) {
				return result;
			}
			
			List<OrderUserDO> confirmedJoiners = this.orderUserBO.queryOrderJoinersByStatus(orderId, OrderUserStatusEnum.CONFIRM_PASSED.getValue());
			// 剩余座位数
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
			log.error("加入拼车失败", e);
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
			
			// 是否创建者本人
			boolean isCreatorSelf = (order.getCreatorId() == userId) ? true : false;
			
			boolean showContactInfo = this.isUserLogin();
			
			// 创建人
			OrderUserDO creator = orderUserBO.queryOrderCreator(orderId);
			
			// 所有参加人
			List<OrderUserDO> joiners = orderUserBO.queryOrderJoiners(orderId);
			List<OrderUserDO> confirmedJoiners = this.orderUserBO.queryOrderJoinersByStatus(orderId, OrderUserStatusEnum.CONFIRM_PASSED.getValue());
			
			OrderUserDO joinedOrderUser = getJoinedOrderUser(userId, joiners);
			OrderUserStatusEnum userJoinStatus = null;
			if (joinedOrderUser != null) {
				userJoinStatus = joinedOrderUser.getStatusEnum();
			}
			boolean hasBeenConfirmJoin = hasBeenConfirmJoin(userJoinStatus);
			
			// 剩余座位数
			int joinersCount = confirmedJoiners.size();
			boolean showConfirmOrderBtn = false;
			if (isCreatorSelf) {
				// 创建者的权限
				if (!confirmedJoiners.isEmpty()) {
					showConfirmOrderBtn = true;
				}
				
				// 待确认的参加者
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
			log.error("查看拼车detail失败", e);
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
			log.error("查看我的订单失败", e);
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
			
			// 主动失效缓存
			cacheManager.delete(CacheContants.RECENT_CITY_ORDERS, order.getCityId());
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("确认订单失败", e);
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
				// 加入
				if (isCreator) {
					result.setResultCode(OrderResultCodes.ORDER_CREATED_BY_YOURSELF);
					return result;
				}
				actionEvent = "joinOrder";
			} else {
				// 创建
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
			log.error("确认个人信息失败", e);
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
	 * 用户信息是否已经非空
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
	 * 用户信息是否完整
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
			log.error("浏览确认订单失败", e);
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
			log.error("填充创建者信息失败", e);
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
			
			// 出发城市id
			CityDO fromCityDO = this.cityBO.queryByName(orderDO.getFromCity());
			if (fromCityDO != null) {
				orderDO.setCityId(fromCityDO.getId());
			}
			long orderId = orderBO.createOrder(orderDO);
			
			result.getModels().put("orderId", orderId);			
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("创建拼的失败", e);
		}
		return result;
	}

	public void setOrderBO(OrderBO orderBO) {
		this.orderBO = orderBO;
	}

	public void setOrderUserBO(OrderUserBO orderUserBO) {
		this.orderUserBO = orderUserBO;
	}

	public int getDefaultCreateTimeDelay() {
		return defaultCreateTimeDelay;
	}

	public void setDefaultCreateTimeDelay(int defaultCreateTimeDelay) {
		this.defaultCreateTimeDelay = defaultCreateTimeDelay;
	}

}
