package com.aifuyun.snow.world.biz.ao.user.impl;

import java.util.Date;

import javax.servlet.http.HttpSession;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.user.LoginAO;
import com.aifuyun.snow.world.biz.resultcodes.CommonResultCodes;
import com.aifuyun.snow.world.biz.resultcodes.UserResultCodes;
import com.aifuyun.snow.world.common.IpUtil;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.ExtUserDO;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.author.SessionConstants;
import com.zjuh.sweet.lang.StringUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;
import com.zjuh.sweet.result.ResultTypeEnum;

public class LoginAOImpl extends BaseAO implements LoginAO {

	public Result handleLogin(BaseUserDO inputUser) {
		Result result = new ResultSupport(false);
		try {
			// 先尝试用户名登陆
			BaseUserDO userDO = userBO.queryByUsernameIgnoreDeletedFlag(inputUser.getUsername());
			String email = inputUser.getEmail();
			if (userDO == null && StringUtil.isNotEmpty(email)) {
				// 再尝试用email登陆
				userDO = userBO.queryByEmailIgnoreDeletedFlag(email);
			}
			
			if (userDO == null) {
				result.setResultCode(UserResultCodes.USER_NOT_EXIST);
				result.setResultTypeEnum(ResultTypeEnum.CURRENT_TARGET);
				return result;
			}
			
			String encryptedPassword = userBO.encryptPassword(inputUser.getPassword());
			if (!StringUtil.equals(encryptedPassword, userDO.getPassword())) {
				result.setResultCode(UserResultCodes.PASSWORD_INCORRET);
				result.setResultTypeEnum(ResultTypeEnum.CURRENT_TARGET);
				return result;
			}
			
			HttpSession session = SplistContext.getSession();
			session.setAttribute(SessionConstants.USER_ID_KEY, userDO.getId());
			session.setAttribute(SessionConstants.USER_NAME_KEY, userDO.getUsername());
			
			handleUpadate(userDO);
			
			result.setSuccess(true);
		} catch (Exception e) {
			result.setResultCode(CommonResultCodes.SYSTEM_ERROR);
			result.setResultTypeEnum(ResultTypeEnum.COMMON_TARGET);
			log.error("处理登录错误", e);
		}		
		return result;
	}
	
	private void handleUpadate(BaseUserDO userDO) {
		ExtUserDO extUser = userBO.queryExtUser(userDO.getId());
		if (extUser == null) {
			extUser = new ExtUserDO();
			extUser.setLastLoginDate(new Date());
			extUser.setLastLoginIp(IpUtil.getRemoteIpAddress());
			extUser.setUserId(userDO.getId());
			extUser.setUsername(userDO.getUsername());
			userBO.createExtUser(extUser);
		} else {
			extUser.setLastLoginDate(new Date());
			extUser.setLastLoginIp(IpUtil.getRemoteIpAddress());
			userBO.updateExtUser(extUser);
		}
	}

	public Result handleLogout() {
		Result result = new ResultSupport(false);
		try {
			if (!LoginContext.isUserLogin()) {
				result.setResultCode(UserResultCodes.USER_NOT_LOGIN);
				return result;
			}
			
			HttpSession session = SplistContext.getSession();
			session.removeAttribute(SessionConstants.USER_ID_KEY);
			session.removeAttribute(SessionConstants.USER_NAME_KEY);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setResultCode(CommonResultCodes.SYSTEM_ERROR);
			result.setResultTypeEnum(ResultTypeEnum.COMMON_TARGET);
			log.error("处理登录错误", e);
		}		
		return result;
	}

}
