package com.aifuyun.snow.world.biz.ao.user.impl;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.OrderResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.biz.bo.together.OrderBO;
import com.aifuyun.snow.world.biz.bo.together.OrderUserBO;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.lang.StringUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

public class UserAOImpl extends BaseAO implements UserAO {

	private UserBO userBO;
	
	private OrderBO orderBO;
	
	private OrderUserBO orderUserBO;
	
	private int defaultSelectedYear = BirthYearEnum.YEAR_80S.getValue();
	
	@Override
	public Result modifyPersonalInfo(BaseUserDO inputBaseUser) {
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
			
			if (inputBaseUser.getId() != baseUserDO.getId()) {
				result.setResultCode(UserResultCodes.CAN_NOT_MODIFY_OTHERS_INFO);
				return result;
			}
			
			assignProperties(baseUserDO, inputBaseUser);
			
			userBO.update(baseUserDO);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("修改个人信息失败", e);
		}
		return result;
	}
	
	private void assignProperties(BaseUserDO destBaseUserDO, BaseUserDO srcBaseUserDO) {
		destBaseUserDO.setBirthYear(srcBaseUserDO.getBirthYear());
		destBaseUserDO.setCareer(srcBaseUserDO.getCareer());
		destBaseUserDO.setEmail(srcBaseUserDO.getEmail());
		destBaseUserDO.setPhone(srcBaseUserDO.getPhone());
		destBaseUserDO.setQq(srcBaseUserDO.getQq());
		destBaseUserDO.setRealName(srcBaseUserDO.getRealName());
		destBaseUserDO.setSex(srcBaseUserDO.getSex());
	}

	@Override
	public Result viewModifyPersonalInfo() {
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
			
			BirthYearEnum[] years = BirthYearEnum.values();
			
			int selectedYear = getSelectedYear(baseUserDO);
			
			result.getModels().put("years", years);
			result.getModels().put("selectedYear", selectedYear);
			result.getModels().put("user", baseUserDO);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("确认个人信息失败", e);
		}
		return result;
	}

	private int getSelectedYear(BaseUserDO baseUserDO) {
		if (baseUserDO == null) {
			return defaultSelectedYear;
		}
		if (BirthYearEnum.inRange(baseUserDO.getBirthYear())) {
			return baseUserDO.getBirthYear();
		}
		return defaultSelectedYear;
	}
	
	@Override
	public Result registerUser(BaseUserDO userDO) {
		Result result = new ResultSupport(false);
		try {
			if (userBO.isUserInSensitivityList(userDO.getUsername())) {
				result.setResultCode(UserResultCodes.SENSITIVITY_USER);
				return result;
			}
			
			if (isUserExist(userDO.getUsername())) {
				result.setResultCode(UserResultCodes.USERNAME_HAS_EXIST);
				return result;
			}
			
			String encryptedPassword = userBO.encryptPassword(userDO.getPassword());
			userDO.setPassword(encryptedPassword);
			
			long id = userBO.create(userDO);
			
			result.setDefaultModel(id);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("注册用户失败", e);
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
			if (orderUserDO == null) {
				orderUserDO = assignDefaultFromUser(baseUserDO);
			}
			
			BirthYearEnum[] years = BirthYearEnum.values();
			
			int selectedYear = getSelectedYear(orderUserDO);
			
			boolean isUserInfoEmpty = isUserInfoEmpty(baseUserDO);
			
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
	
	private int getSelectedYear(OrderUserDO orderUserDO) {
		if (orderUserDO == null) {
			return defaultSelectedYear;
		}
		if (BirthYearEnum.inRange(orderUserDO.getBirthYear())) {
			return orderUserDO.getBirthYear();
		}
		return defaultSelectedYear;
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
	 * 用户信息是否完整
	 * @param baseUserDO
	 * @return
	 */
	private boolean isUserInfoEmpty(BaseUserDO baseUserDO) {
		if (StringUtil.isNotEmpty(baseUserDO.getRealName())) {
			return false;
		}
		if (baseUserDO.getBirthYear() > 0) {
			return false;
		}
		if (StringUtil.isNotEmpty(baseUserDO.getCareer())) {
			return false;
		}
		if (StringUtil.isNotEmpty(baseUserDO.getPhone())) {
			return false;
		}
		if (baseUserDO.getSex() > 0 ) {
			return false;
		}
		return true;
	}

	private boolean isUserExist(String username) {
		return null != userBO.queryByUsernameIgnoreDeletedFlag(username);
	}

	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

	public void setDefaultSelectedYear(int defaultSelectedYear) {
		this.defaultSelectedYear = defaultSelectedYear;
	}

	public void setOrderBO(OrderBO orderBO) {
		this.orderBO = orderBO;
	}

	public void setOrderUserBO(OrderUserBO orderUserBO) {
		this.orderUserBO = orderUserBO;
	}

}
