package com.aifuyun.snow.world.biz.ao.user;

import junit.framework.Assert;

import com.aifuyun.snow.world.SnowWorldTest;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.dal.daointerface.user.BaseUserDAO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

public class UserAOTests extends SnowWorldTest {

	private UserAO userAO;

	private BaseUserDAO baseUserDAO;
	
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
		
		baseUserDO.setUsername("»ÆËÉÁ¢");
		result = userAO.registerUser(baseUserDO);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.SENSITIVITY_USER, result.getResultCode());
	}
	
	public void testConfirmPersonalInfo() {
		this.setLogout();
		Result result = userAO.confirmPersonalInfo();
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(CommonResultCodes.USER_NOT_LOGIN, result.getResultCode());
		
		String username = "demo_user_temp";
		long notExistUserId = createDeletedUser(username);
		this.setLogin(notExistUserId, username);
		result = userAO.confirmPersonalInfo();
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.USER_NOT_EXIST, result.getResultCode());
		
		username = "demo_exist_user";
		long existUserId = createTempUser(username);
		this.setLogin(existUserId, username);
		result = userAO.confirmPersonalInfo();
		Assert.assertTrue(result.isSuccess());
		BaseUserDO user = (BaseUserDO)result.getModels().get("user");
		Assert.assertNotNull(user);
		
		
	}
	
	private long createTempUser(String username) {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(username);
		baseUserDO.setPassword("test_password");
		long userId = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(userId > 0L);
		return userId;
	}
	
	private long createDeletedUser(String username) {
		long userId = createTempUser(username);
		baseUserDAO.delete(userId);
		BaseUserDO inDb = baseUserDAO.queryById(userId);
		Assert.assertNull(inDb);
		return userId;
	}
	
	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}

	public void setBaseUserDAO(BaseUserDAO baseUserDAO) {
		this.baseUserDAO = baseUserDAO;
	}
	
}
