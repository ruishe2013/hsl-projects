package com.aifuyun.snow.world.biz.ao.together.impl;

import java.util.List;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.together.OrderAO;
import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.biz.bo.together.OrderUserBO;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.biz.query.OrderQuery;
import com.aifuyun.snow.world.biz.resultcodes.CommonResultCodes;
import com.aifuyun.snow.world.biz.resultcodes.OrderResultCodes;
import com.aifuyun.snow.world.biz.resultcodes.UserResultCodes;
import com.aifuyun.snow.world.common.SnowUtils;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderStatusEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.UserInfoHolder;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
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
	
	private UserBO userBO;
	
	@Override
	public Result handleForIndex(int cityId) {
		Result result = new ResultSupport(false);
		try {
			List<OrderDO> recentOrders = orderBO.queryRecentOrders(cityId);
			
			result.getModels().put("recentOrders", recentOrders);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("查看最近拼车失败", e);
		}
		return result;
	}

	@Override
	public Result viewMyOrders(OrderQuery orderQuery) {
		Result result = new ResultSupport(false);
		try {
			final long userId = this.getLoginUserId();
			List<OrderDO> orders = orderUserBO.queryOrdersByUserIdAndRole(userId, orderQuery.getOrderUserRole());
			result.getModels().put("orders", orders);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("查看我的订单失败", e);
		}
		return result;
	}

	@Override
	public Result confirmOrder(long orderId) {
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
			
			orderBO.updateStatus(orderId, OrderStatusEnum.PROGRESSING);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("确认订单失败", e);
		}
		return result;
	}
	
	@Override
	public Result viewPersonalInfoForOrder(long orderId) {
		Result result = new ResultSupport(false);
		try {
			long userId = this.getLoginUserId();
			if (userId <= 0L) {
				result.setResultCode(CommonResultCodes.USER_NOT_LOGIN);
				return result;
			}
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
			
			if (order.getCreatorId() != userId) {
				result.setResultCode(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER);
				return result;
			}
			
			OrderUserDO orderUserDO = orderUserBO.queryOrderCreator(orderId);
			boolean creatorExist = false;
			if (orderUserDO == null) {
				orderUserDO = assignDefaultFromUser(baseUserDO);
			} else {
				creatorExist = true;
			}
			
			int selectedYear = SnowUtils.getSelectedYear(orderUserDO);
			boolean isUserInfoEmpty = isUserInfoEmpty(baseUserDO);
			
			BirthYearEnum[] years = BirthYearEnum.values();
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
	}
	
	private void copyUserHolderInfo(UserInfoHolder dest, OrderUserDO src) {
		dest.setBirthYear(src.getBirthYear());
		dest.setCareer(src.getCareer());
		dest.setEmail(src.getEmail());
		dest.setPhone(src.getPhone());
		dest.setQq(src.getQq());
		dest.setRealName(src.getRealName());
		dest.setSex(src.getSex());
	}

	@Override
	public Result createTaxiOrder(OrderDO orderDO) {
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

	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

}
