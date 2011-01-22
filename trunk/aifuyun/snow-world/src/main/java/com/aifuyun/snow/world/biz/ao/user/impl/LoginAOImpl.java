package com.aifuyun.snow.world.biz.ao.user.impl;

import javax.servlet.http.HttpSession;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.user.LoginAO;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.splist.core.SplistContext;
import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.author.SessionConstants;
import com.zjuh.sweet.lang.StringUtil;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultCode;
import com.zjuh.sweet.result.ResultCodeTypeEnum;
import com.zjuh.sweet.result.ResultSupport;

public class LoginAOImpl extends BaseAO implements LoginAO {

	private UserBO userBO;
	
	public Result handleLogin(BaseUserDO inputUser) {
		Result result = new ResultSupport(false);
		try {
			BaseUserDO userDO = userBO.queryByUsernameIgnoreDeletedFlag(inputUser.getUsername());
			if (userDO == null) {
				result.setResultCode(ResultCode.create("该用户不存在！", ResultCodeTypeEnum.CURRENT_TARGET));
				return result;
			}
			
			String encryptedPassword = userBO.encryptPassword(inputUser.getPassword());
			if (!StringUtil.equals(encryptedPassword, userDO.getPassword())) {
				result.setResultCode(ResultCode.create("密码错误！", ResultCodeTypeEnum.CURRENT_TARGET));
				return result;
			}
			
			HttpSession session = SplistContext.getSession();
			session.setAttribute(SessionConstants.USER_ID_KEY, userDO.getId());
			session.setAttribute(SessionConstants.USER_NAME_KEY, userDO.getUsername());
			
			result.setSuccess(true);
		} catch (Exception e) {
			result.setResultCode(ResultCode.create("系统错误！", ResultCodeTypeEnum.COMMON_TARGET));
			log.error("处理登录错误", e);
		}		
		return result;
	}

	public Result handleLogout() {
		Result result = new ResultSupport(false);
		try {
			if (!LoginContext.isUserLogin()) {
				result.setResultCode(ResultCode.create("用户未登录！"));
				return result;
			}
			
			HttpSession session = SplistContext.getSession();
			session.removeAttribute(SessionConstants.USER_ID_KEY);
			session.removeAttribute(SessionConstants.USER_NAME_KEY);
			result.setSuccess(true);
		} catch (Exception e) {
			result.setResultCode(ResultCode.create("系统错误！", ResultCodeTypeEnum.COMMON_TARGET));
			log.error("处理登录错误", e);
		}		
		return result;
	}

	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

}
