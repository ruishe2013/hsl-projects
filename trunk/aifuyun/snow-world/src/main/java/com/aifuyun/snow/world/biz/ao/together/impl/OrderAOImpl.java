package com.aifuyun.snow.world.biz.ao.together.impl;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.OrderResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.biz.ao.together.OrderAO;
import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.biz.bo.together.OrderUserBO;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.misc.UserInfoHolder;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
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
