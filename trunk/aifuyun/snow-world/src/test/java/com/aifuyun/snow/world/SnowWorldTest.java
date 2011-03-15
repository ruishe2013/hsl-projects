package com.aifuyun.snow.world;

import java.text.ParseException;

import junit.framework.Assert;

import com.aifuyun.snow.world.biz.ao.together.taxi.OrderAO;
import com.aifuyun.snow.world.biz.ao.user.UserAO;
import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.dal.daointerface.together.OrderDAO;
import com.aifuyun.snow.world.dal.daointerface.together.OrderUserDAO;
import com.aifuyun.snow.world.dal.daointerface.user.BaseUserDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.aifuyun.snow.world.dal.dataobject.user.BaseUserDO;
import com.zjuh.sweet.author.LoginContext;
import com.zjuh.sweet.result.Result;
import com.zjuh.sweet.test.BaseTest;

public abstract class SnowWorldTest extends BaseTest {
	
	protected UserAO userAO;

	protected BaseUserDAO baseUserDAO;
	
	protected OrderDAO orderDAO;
	
	protected OrderAO orderAO;
	
	protected OrderUserDAO orderUserDAO;
	
	protected long createTempUser(String username) {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(username);
		baseUserDO.setPassword("test_password");
		
		long userId = baseUserDAO.create(baseUserDO);
		Assert.assertTrue(userId > 0L);
		return userId;
	}
	
	protected long createDeletedUser(String username) {
		long userId = createTempUser(username);
		baseUserDAO.delete(userId);
		BaseUserDO inDb = baseUserDAO.queryById(userId);
		Assert.assertNull(inDb);
		return userId;
	}
	
	protected OrderUserDO createTempOrderUserDO() {
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
	
	protected long getNotExistUser(String username) {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(username);
		baseUserDO.setPassword("pass1234");
		Result result = userAO.registerUser(baseUserDO);
		Assert.assertTrue(result.isSuccess());
		long userId =  (Long)result.getDefaultModel();
		baseUserDAO.delete(userId);
		BaseUserDO inDb = baseUserDAO.queryById(userId);
		Assert.assertNull(inDb);
		return userId;
	}
	
	protected long createUser(String username) {
		BaseUserDO baseUserDO = new BaseUserDO();
		baseUserDO.setUsername(username);
		baseUserDO.setPassword("pass1234");
		Result result = userAO.registerUser(baseUserDO);
		Assert.assertTrue(result.isSuccess());
		return (Long)result.getDefaultModel();
	}
	
	protected long createOrderByCreator(long creatorId, String creatorUsername) throws ParseException {
		OrderDO orderDO = createNormalTogetherOrderDO();
		orderDO.setCreatorId(creatorId);
		orderDO.setCreatorUsername(creatorUsername);
		long orderId = orderDAO.create(orderDO);
		Assert.assertTrue(orderId > 0L);
		return orderId;
	}
	
	protected OrderDO createNormalTogetherOrderDO() throws ParseException {
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
	
	protected long getNotExistOrderId() throws ParseException {
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
	
	public void setLogin(long userId, String username) {
		LoginContext.setUser(userId, username);
	}
	
	public void setLogout() {
		LoginContext.removeUser();
	}

	public final void setUserAO(UserAO userAO) {
		this.userAO = userAO;
	}

	public final void setBaseUserDAO(BaseUserDAO baseUserDAO) {
		this.baseUserDAO = baseUserDAO;
	}

	public final void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}

	public final void setOrderAO(OrderAO orderAO) {
		this.orderAO = orderAO;
	}

	public final void setOrderUserDAO(OrderUserDAO orderUserDAO) {
		this.orderUserDAO = orderUserDAO;
	}
	
}
