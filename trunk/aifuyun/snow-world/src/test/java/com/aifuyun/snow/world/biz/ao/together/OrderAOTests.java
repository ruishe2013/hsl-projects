package com.aifuyun.snow.world.biz.ao.together;

import java.text.ParseException;
import java.util.List;

import junit.framework.Assert;

import com.aifuyun.snow.world.SnowWorldTest;
import com.aifuyun.snow.world.biz.ao.resultcode.CommonResultCodes;
import com.aifuyun.snow.world.biz.ao.resultcode.OrderResultCodes;
import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.dal.daointerface.together.OrderDAO;
import com.aifuyun.snow.world.dal.daointerface.together.OrderUserDAO;
import com.aifuyun.snow.world.dal.daointerface.user.BaseUserDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.result.Result;

/**
 * @author pister
 *
 */
public class OrderAOTests extends SnowWorldTest {
	
	private OrderAO orderAO;
	
	private UserAO userAO;
	
	private OrderDAO orderDAO;
	
	private OrderUserDAO orderUserDAO;
	
	private BaseUserDAO baseUserDAO;
	
	public void testFillCreatorInfo() throws ParseException {
		final String username = "hello_test_name1234";
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
	
	private OrderUserDO createTempOrderUserDO() {
		OrderUserDO ret = new OrderUserDO();
		ret.setBirthYear(BirthYearEnum.YEAR_80S.getValue());
		ret.setCareer("test");
		ret.setEmail("jack");
		ret.setPhone("phone!!");
		ret.setQq("123465");
		ret.setRealName("hsl");
		ret.setSex(SexEnum.MALE.getValue());
		return ret;
	}
	
	private long createOrderByCreator(long creatorId, String creatorUsername) throws ParseException {
		OrderDO orderDO = createNormalTogetherOrderDO();
		orderDO.setCreatorId(creatorId);
		orderDO.setCreatorUsername(creatorUsername);
		long orderId = orderDAO.create(orderDO);
		Assert.assertTrue(orderId > 0L);
		return orderId;
	}
	
	private long getNotExistOrderId() throws ParseException {
		OrderDO orderDO = createNormalTogetherOrderDO();
		orderDO.setCreatorId(1);
		orderDO.setCreatorUsername("asdsad");
		long orderId = orderDAO.create(orderDO);
		Assert.assertTrue(orderId > 0L);
		orderDAO.delete(orderId);
		OrderDO inDb = orderDAO.queryById(orderId);
		Assert.assertNull(inDb);
		return orderId;
	}
	
	private long createUser(String username) {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(username);
		baseUserDO.setPassword("pass1234");
		Result result = userAO.registerUser(baseUserDO);
		Assert.assertTrue(result.isSuccess());
		return (Long)result.getDefaultModel();
	}
	
	public void testCreateTaxiOrder() throws ParseException {
		// Î´µÇÂ½µÄÇé¿ö
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
	
	private OrderDO createNormalTogetherOrderDO() throws ParseException {
		OrderDO togetherOrderDO = new OrderDO();
		togetherOrderDO.setArriveAddr("arrive addr");
		togetherOrderDO.setArriveCity("arrive city");
		togetherOrderDO.setArriveTime(DateTimeUtil.parseDate("2010-12-01 19:30:00"));
		togetherOrderDO.setDescription("this is a description");
		togetherOrderDO.setFromAddr("from addr");
		togetherOrderDO.setFromCity("from city");
		togetherOrderDO.setFromTime(DateTimeUtil.parseDate("2010-12-01 17:30:00"));
		togetherOrderDO.setTotalSeats(4);
		return togetherOrderDO;
	}

	public void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	public void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}

	public void setOrderUserDAO(OrderUserDAO orderUserDAO) {
		this.orderUserDAO = orderUserDAO;
	}

	public void setBaseUserDAO(BaseUserDAO baseUserDAO) {
		this.baseUserDAO = baseUserDAO;
	}

}
