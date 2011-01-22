package com.aifuyun.snow.world.biz.ao.user;

import junit.framework.Assert;

import com.aifuyun.snow.world.BaseTest;
import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

public class UserAOTests extends BaseTest {

	private UserAO userAO;

	public void testRegister() {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername("temp_user_for_unit");
		baseUserDO.setPassword("hello_world");
		
		Result result = userAO.registerUser(baseUserDO);
		Assert.assertTrue(result.isSuccess());
		result = userAO.registerUser(baseUserDO);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.USERNAME_EXIST, result.getResultCode());
		
		baseUserDO.setUsername("ª∆À…¡¢");
		result = userAO.registerUser(baseUserDO);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.SENSITIVITY_USER, result.getResultCode());
	}
	
	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}
	
}
