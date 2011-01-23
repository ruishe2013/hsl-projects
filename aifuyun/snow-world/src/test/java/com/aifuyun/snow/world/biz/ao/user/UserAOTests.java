package com.aifuyun.snow.world.biz.ao.user;

import junit.framework.Assert;

import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.test.BaseTest;

public class UserAOTests extends BaseTest {

	private UserAO userAO;

	public void testRegister() {
		
		final String BASE_USER_NAME = "temp_user_for_unit";
		
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(BASE_USER_NAME);
		baseUserDO.setPassword("hello_world");
		
		Result result = userAO.registerUser(baseUserDO);
		Assert.assertTrue(result.isSuccess());
		Long id = result.getDefaultModel();
		Assert.assertNotNull(id);
		Assert.assertTrue(id > 0);
		
		String username = this.queryField("select username from sw_base_user where id =? and deleted = 0", "username", String.class, id);
		Assert.assertEquals(BASE_USER_NAME, username);
		
		
		result = userAO.registerUser(baseUserDO);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.USERNAME_HAS_EXIST, result.getResultCode());
		
		baseUserDO.setUsername("ª∆À…¡¢");
		result = userAO.registerUser(baseUserDO);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.SENSITIVITY_USER, result.getResultCode());
	}
	
	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}
	
}
