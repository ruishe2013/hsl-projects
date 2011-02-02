package com.aifuyun.snow.world.biz.ao.user;

import junit.framework.Assert;

import com.aifuyun.snow.world.SnowWorldTest;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.dal.daointerface.user.BaseUserDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

public class UserAOTests extends SnowWorldTest {

	private UserAO userAO;

	private BaseUserDAO baseUserDAO;
	
	public void testModifyPersonalInfo() {
		String username = "demo_exist_user1";
		long userId = createTempUser(username);
		this.setLogin(userId, username);
		String newPhone = "123456789";
		BaseUserDO inputBaseUser = fillBaseUserDO(userId, newPhone);
		Result result = userAO.modifyPersonalInfo(inputBaseUser);
		Assert.assertTrue(result.isSuccess());
		BaseUserDO inDb = baseUserDAO.queryById(userId);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(newPhone, inDb.getPhone());
		
		this.setLogout();
		inputBaseUser = fillBaseUserDO(userId, newPhone);
		result = userAO.modifyPersonalInfo(inputBaseUser);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(CommonResultCodes.USER_NOT_LOGIN, result.getResultCode());

		this.setLogin(userId, username);
		inputBaseUser = fillBaseUserDO(userId+1, newPhone);
		result = userAO.modifyPersonalInfo(inputBaseUser);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.CAN_NOT_MODIFY_OTHERS_INFO, result.getResultCode());
		
	}
	
	private BaseUserDO fillBaseUserDO(long id, String newPhone) {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setId(id);
		baseUserDO.setBirthYear(BirthYearEnum.YEAR_80S.getValue());
		baseUserDO.setCareer("test");
		baseUserDO.setEmail("jack");
		baseUserDO.setPhone(newPhone);
		baseUserDO.setQq("123465");
		baseUserDO.setRealName("hsl");
		baseUserDO.setSex(SexEnum.MALE.getValue());
		return baseUserDO;
	}
	
	public void testViewModifyPersonalInfo() {
		this.setLogout();
		Result result = userAO.viewModifyPersonalInfo();
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(CommonResultCodes.USER_NOT_LOGIN, result.getResultCode());
		
		String username = "demo_user_temp";
		long notExistUserId = createDeletedUser(username);
		this.setLogin(notExistUserId, username);
		result = userAO.viewModifyPersonalInfo();
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.USER_NOT_EXIST, result.getResultCode());
		
		username = "demo_exist_user";
		long existUserId = createTempUser(username);
		this.setLogin(existUserId, username);
		result = userAO.viewModifyPersonalInfo();
		Assert.assertTrue(result.isSuccess());
		BaseUserDO user = (BaseUserDO)result.getModels().get("user");
		Assert.assertNotNull(user);
		
		BirthYearEnum[] years = (BirthYearEnum[])result.getModels().get("years");
		Assert.assertNotNull(years);
		
		Integer selectedYear = (Integer)result.getModels().get("selectedYear");
		Assert.assertNotNull(selectedYear);
		Assert.assertTrue(selectedYear > 0);
	}
	
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
		Boolean userInfoCompeleted = (Boolean)result.getModels().get("userInfoCompeleted");
		Assert.assertNotNull(user);
		Assert.assertNotNull(userInfoCompeleted);
		Assert.assertFalse(userInfoCompeleted);
		
		
		username = "fullinfo_exist_user";
		long fullInfoUserId = createFullInfoUser(username);
		this.setLogin(fullInfoUserId, username);
		result = userAO.confirmPersonalInfo();
		Assert.assertTrue(result.isSuccess());
		user = (BaseUserDO)result.getModels().get("user");
		userInfoCompeleted = (Boolean)result.getModels().get("userInfoCompeleted");
		Assert.assertNotNull(user);
		Assert.assertNotNull(userInfoCompeleted);
		Assert.assertTrue(userInfoCompeleted);
	}
	
	private long createTempUser(String username) {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(username);
		baseUserDO.setPassword("test_password");
		
		long userId = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(userId > 0L);
		return userId;
	}
	
	private long createFullInfoUser(String username) {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(username);
		baseUserDO.setPassword("test_password");
		
		baseUserDO.setRealName("MyName");
		baseUserDO.setBirthYear(BirthYearEnum.YEAR_80S.getValue());
		baseUserDO.setCareer("programmer");
		baseUserDO.setEmail("test email");
		baseUserDO.setPhone("133333333");
		baseUserDO.setQq("55555555");
		baseUserDO.setSex(SexEnum.MALE.getValue());
		
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
