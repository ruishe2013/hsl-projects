package com.aifuyun.snow.world.biz.ao.together;

import java.text.ParseException;
import java.util.List;

import junit.framework.Assert;

import com.aifuyun.snow.world.SnowWorldTest;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.OrderResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.UserResultCodes;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderTypeEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public class OrderAOTests extends SnowWorldTest {
	
	public void testConfirmOrder() throws ParseException {
		String username = "hello_test_name1235";
		long userId = createUser(username);
		setLogin(userId, username);
		long orderId = createOrderByCreator(userId , username);
		// TODO 完成确认发起拼车的单元测试
	}
	
	public void testViewPersonalInfoForOrder() throws ParseException {
		this.setLogout();
		Result result = orderAO.viewPersonalInfoForOrder(0);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(CommonResultCodes.USER_NOT_LOGIN, result.getResultCode());
		
		String username = "demo_user_temp";
		long notExistUserId = createDeletedUser(username);
		this.setLogin(notExistUserId, username);
		result = orderAO.viewPersonalInfoForOrder(0);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(UserResultCodes.USER_NOT_EXIST, result.getResultCode());
		
		username = "demo_exist_user";
		long existUserId = createTempUser(username);
		this.setLogin(existUserId, username);
		long orderId = createOrderDO(existUserId, username);
		result = orderAO.viewPersonalInfoForOrder(orderId);
		Assert.assertTrue(result.isSuccess());
		OrderUserDO orderUser = (OrderUserDO)result.getModels().get("orderUser");
		Assert.assertNotNull(orderUser);
		OrderDO order = (OrderDO)result.getModels().get("order");
		Assert.assertNotNull(order);
		Assert.assertTrue((Boolean)result.getModels().get("isUserInfoEmpty"));
		Assert.assertFalse((Boolean)result.getModels().get("creatorExist"));
		
		OrderUserDO creator = createTempOrderUserDO();
		orderAO.fillCreatorInfo(creator, orderId, false);
		result = orderAO.viewPersonalInfoForOrder(orderId);
		Assert.assertTrue(result.isSuccess());
		Assert.assertTrue((Boolean)result.getModels().get("creatorExist"));
		
		orderId = createOrderDO(existUserId + 1, username);
		result = orderAO.viewPersonalInfoForOrder(orderId);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER, result.getResultCode());
		
		orderDAO.delete(orderId);
		result = orderAO.viewPersonalInfoForOrder(orderId);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.ORDER_NOT_EXIST, result.getResultCode());
		
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
	
	public void testViewConfirmOrder() throws ParseException {
		String username = "hello_test_name1234";
		long userId = createUser(username);
		setLogin(userId, username);
		
		long orderNotExist = getNotExistOrderId();
		Result result = orderAO.viewConfirmOrder(orderNotExist);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.ORDER_NOT_EXIST, result.getResultCode());
		
		long orderId = createOrderByCreator(userId + 1 , "another_user");
		result = orderAO.viewConfirmOrder(orderId);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER, result.getResultCode());
		
		setLogin(userId, username);
		orderId = createOrderByCreator(userId , username);
		result = orderAO.viewConfirmOrder(orderId);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.CANNOT_FIND_ORDER_CREATOR, result.getResultCode());
		
		username = "hello_test_name1235";
		userId = createUser(username);
		setLogin(userId, username);
		orderId = createOrderByCreator(userId , username);
		
		OrderUserDO creator = createTempOrderUserDO();
		Result resultFillCreator = orderAO.fillCreatorInfo(creator, orderId, false);
		Assert.assertTrue(resultFillCreator.isSuccess());
		
		result = orderAO.viewConfirmOrder(orderId);
		Assert.assertTrue(result.isSuccess());
		OrderDO order = (OrderDO)result.getModels().get("order");
		Assert.assertNotNull(order);
		OrderUserDO creator2 = (OrderUserDO)result.getModels().get("creator");
		Assert.assertNotNull(creator2);
	}
	
	public void testFillCreatorInfo() throws ParseException {
		final String username = "hello_test_name1236";
		long userId = createUser(username);
		setLogin(userId, username);
		long orderNotExist = getNotExistOrderId();
		Result result = orderAO.fillCreatorInfo(null, orderNotExist, false);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.ORDER_NOT_EXIST, result.getResultCode());
		
		long orderId = createOrderByCreator(userId+1, "another_user");
		result = orderAO.fillCreatorInfo(null, orderId, false);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(OrderResultCodes.CANNOT_EDIT_OTHERS_ORDER, result.getResultCode());
		
		OrderUserDO creator = createTempOrderUserDO();
		// test insert
		orderId = createOrderByCreator(userId, username);
		result = orderAO.fillCreatorInfo(creator, orderId, false);
		Assert.assertTrue(result.isSuccess());
		List<OrderUserDO> inDB = orderUserDAO.queryByOrderIdAndRole(orderId, OrderUserRoleEnum.CREATOR.getValue());
		Assert.assertNotNull(inDB);
		Assert.assertTrue(inDB.size() == 1);
		OrderUserDO creatorInDb = inDB.get(0);
		Assert.assertEquals("phone!!", creatorInDb.getPhone());
		
		// test update
		creator = createTempOrderUserDO();
		creator.setPhone("1333");
		result = orderAO.fillCreatorInfo(creator, orderId, false);
		Assert.assertTrue(result.isSuccess());
		inDB = orderUserDAO.queryByOrderIdAndRole(orderId, OrderUserRoleEnum.CREATOR.getValue());
		Assert.assertNotNull(inDB);
		Assert.assertTrue(inDB.size() == 1);
		creatorInDb = inDB.get(0);
		Assert.assertEquals("1333", creatorInDb.getPhone());
		
		// test for updateUser
		
		creator = createTempOrderUserDO();
		creator.setPhone("12345!!");
		result = orderAO.fillCreatorInfo(creator, orderId, true);
		Assert.assertTrue(result.isSuccess());
		inDB = orderUserDAO.queryByOrderIdAndRole(orderId, OrderUserRoleEnum.CREATOR.getValue());
		Assert.assertNotNull(inDB);
		Assert.assertTrue(inDB.size() == 1);
		creatorInDb = inDB.get(0);
		Assert.assertEquals("12345!!", creatorInDb.getPhone());
		
		BaseUserDO userInDb = baseUserDAO.queryById(userId);
		Assert.assertNotNull(userInDb);
		Assert.assertEquals("12345!!", userInDb.getPhone());
	}
	
	public void testCreateTaxiOrder() throws ParseException {
		// 未登陆的情况
		this.setLogout();
		OrderDO orgObject = createNormalTogetherOrderDO();
		Result result = orderAO.createTaxiOrder(orgObject);
		Assert.assertFalse(result.isSuccess());
		Assert.assertEquals(CommonResultCodes.USER_NOT_LOGIN, result.getResultCode());
		
		setLogin(999, "test_demo_user");
		result = orderAO.createTaxiOrder(createNormalTogetherOrderDO());
		Assert.assertTrue(result.isSuccess());
		long orderId = (Long)result.getModels().get("orderId");
		Assert.assertTrue(orderId > 0L);
		
		OrderDO inDb = orderDAO.queryById(orderId);
		
		Assert.assertNotNull(inDb);
		Assert.assertEquals(orgObject.getArriveAddr(), inDb.getArriveAddr());
		
	}
	
}
