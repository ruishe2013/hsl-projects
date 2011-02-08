package com.aifuyun.snow.world.biz.ao.user;

import java.text.ParseException;

import junit.framework.Assert;

import com.aifuyun.snow.world.SnowWorldTest;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.OrderResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.dal.daointerface.together.OrderDAO;
import com.aifuyun.snow.world.dal.daointerface.user.BaseUserDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

public class UserAOTests extends SnowWorldTest {

	private UserAO userAO;

	private BaseUserDAO baseUserDAO;
	
	private OrderDAO orderDAO;
	
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
	

	private long createOrderDO(long userId, String username) throws ParseException {
		OrderDO togetherOrderDO = new OrderDO();
		togetherOrderDO.setArriveAddr("arrive addr");
		togetherOrderDO.setArriveCity("arrive city");
		togetherOrderDO.setArriveTime(DateTimeUtil.parseDate("2010-12-01 19:30:00"));
		togetherOrderDO.setDescription("this is a description");
		togetherOrderDO.setFromAddr("from addr");
		togetherOrderDO.setFromCity("from city");
		togetherOrderDO.setFromTime(DateTimeUtil.parseDate("2010-12-01 17:30:00"));
		togetherOrderDO.setTotalSeats(4);
		
		togetherOrderDO.setCreatorId(userId);
		togetherOrderDO.setCreatorUsername(username);
		togetherOrderDO.setType(OrderTypeEnum.TAXI.getValue());
		
		long id = orderDAO.create(togetherOrderDO);
		Assert.assertTrue(id > 0);
		return id;
	}
	
	public void testViewPersonalInfoForOrder() throws ParseException {
		this.setLogout();
		Result result = userAO.viewPersonalInfoForOrder(0);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(CommonResultCodes.USER_NOT_LOGIN, result.getResultCode());
		
		String username = "demo_user_temp";
		long notExistUserId = createDeletedUser(username);
		this.setLogin(notExistUserId, username);
		result = userAO.viewPersonalInfoForOrder(0);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.USER_NOT_EXIST, result.getResultCode());
		
		username = "demo_exist_user";
		long existUserId = createTempUser(username);
		this.setLogin(existUserId, username);
		long orderId = createOrderDO(existUserId, username);
		result = userAO.viewPersonalInfoForOrder(orderId);
		Assert.assertTrue(result.isSuccess());
		OrderUserDO orderUser = (OrderUserDO)result.getModels().get("orderUser");
		Assert.assertNotNull(orderUser);
		OrderDO order = (OrderDO)result.getModels().get("order");
		Assert.assertNotNull(order);
		Assert.assertTrue((Boolean)result.getModels().get("isUserInfoEmpty"));
		
		
		orderId = createOrderDO(existUserId + 1, username);
		result = userAO.viewPersonalInfoForOrder(orderId);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER, result.getResultCode());
		
		orderDAO.delete(orderId);
		result = userAO.viewPersonalInfoForOrder(orderId);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.ORDER_NOT_EXIST, result.getResultCode());
		
		
		
		
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

	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

}
