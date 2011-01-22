package com.aifuyun.snow.world.biz.ao.user.impl;

import com.aifuyun.snow.world.biz.ao.BaseAO;
import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.biz.bo.user.UserBO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.result.ResultSupport;

public class UserAOImpl extends BaseAO implements UserAO {

	private UserBO userBO;
	
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
			
			userBO.create(userDO);
			
			result.setSuccess(true);
		} catch (Exception e) {
			log.error("×¢²áÓÃ»§Ê§°Ü", e);
		}
		return result;
	}
	
	private boolean isUserExist(String username) {
		return null != userBO.queryByUsernameIgnoreDeletedFlag(username);
	}

	public void setUserBO(UserBO userBO) {
		this.userBO = userBO;
	}

}
