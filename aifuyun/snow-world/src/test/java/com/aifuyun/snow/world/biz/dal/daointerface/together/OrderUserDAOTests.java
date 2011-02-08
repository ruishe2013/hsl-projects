package com.aifuyun.snow.world.biz.dal.daointerface.together;

import java.util.List;

import junit.framework.Assert;

import com.aifuyun.snow.world.dal.daointerface.together.OrderUserDAO;
import com.aifuyun.snow.world.dal.dataobject.enums.BirthYearEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.OrderUserRoleEnum;
import com.aifuyun.snow.world.dal.dataobject.enums.SexEnum;
import com.aifuyun.snow.world.dal.dataobject.together.OrderUserDO;
import com.zjuh.sweet.test.BaseTest;

public class OrderUserDAOTests extends BaseTest {
	
	private OrderUserDAO orderUserDAO;

	public void testCreate() {
		OrderUserDO orderUserDO = new OrderUserDO();
		orderUserDO.setBirthYear(BirthYearEnum.YEAR_60S.getValue());
		orderUserDO.setCareer("nothing");
		orderUserDO.setEmail("my email");
		orderUserDO.setOrderId(1);
		orderUserDO.setOrderType(0);
		orderUserDO.setPhone("139");
		orderUserDO.setQq("644");
		orderUserDO.setRealName("dahuang");
		orderUserDO.setRole(OrderUserRoleEnum.CREATOR.getValue());
		orderUserDO.setSex(SexEnum.MALE.getValue());
		orderUserDO.setUserId(2);
		orderUserDO.setUsername("abc");
		
		long id = orderUserDAO.create(orderUserDO);
		Assert.assertTrue(id > 0L);
		String career = this.queryField("select career from sw_order_user where id = ?", "career", String.class, id);
		Assert.assertEquals("nothing", career);
	}
	
	public void testQueryById() {
		long id = createOrderUser("hsl", 1, 2);
		OrderUserDO inDb = orderUserDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals("hsl", inDb.getUsername());
	}
	
	public void testDelete() {
		long id = createOrderUser("hsl", 1, 2);
		OrderUserDO inDb = orderUserDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals("hsl", inDb.getUsername());
		orderUserDAO.delete(id);
		inDb = orderUserDAO.queryById(id);
		Assert.assertNull(inDb);
	}
	
	public void testUpdate() {
		long id = createOrderUser("hsl", 1, 2);
		OrderUserDO inDb = orderUserDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals("hsl", inDb.getUsername());
		inDb.setUsername("newName");
		orderUserDAO.update(inDb);
		OrderUserDO newInDb = orderUserDAO.queryById(id);
		Assert.assertNotNull(newInDb);
		Assert.assertEquals("newName", newInDb.getUsername());
	}
	
	public void testQueryByOrderIdAndRole() {
		long orderId = 990L;
		createOrderUser("hsl", orderId, 2, OrderUserRoleEnum.CREATOR.getValue());
		createOrderUser("hsl222", orderId, 3);
		List<OrderUserDO> orders = orderUserDAO.queryByOrderIdAndRole(orderId, OrderUserRoleEnum.CREATOR.getValue());
		Assert.assertNotNull(orders);
		Assert.assertTrue(orders.size() >= 1);
	}
	
	public void testQueryByOrderId() {
		long orderId = 991L;
		createOrderUser("hsl", orderId, 2, OrderUserRoleEnum.CREATOR.getValue());
		createOrderUser("hsl222", orderId, 3);
		List<OrderUserDO> orders = orderUserDAO.queryByOrderId(orderId);
		Assert.assertNotNull(orders);
		Assert.assertTrue(orders.size() >= 2);
	}
	
	
	private long createOrderUser(String username, long orderId, long userId, int role) {
		OrderUserDO orderUserDO = new OrderUserDO();
		orderUserDO.setBirthYear(BirthYearEnum.YEAR_60S.getValue());
		orderUserDO.setCareer("nothing");
		orderUserDO.setEmail("my email");
		orderUserDO.setOrderId(orderId);
		orderUserDO.setOrderType(0);
		orderUserDO.setPhone("139");
		orderUserDO.setQq("644");
		orderUserDO.setRealName("dahuang");
		orderUserDO.setRole(OrderUserRoleEnum.CREATOR.getValue());
		orderUserDO.setSex(SexEnum.MALE.getValue());
		orderUserDO.setUserId(userId);
		orderUserDO.setUsername(username);
		long id = orderUserDAO.create(orderUserDO);
		Assert.assertTrue(id > 0L);
		return id;
	}
	
	private long createOrderUser(String username, long orderId, long userId) {
		return createOrderUser(username, orderId, userId, OrderUserRoleEnum.JOINER.getValue());
	}
	
	public void setOrderUserDAO(OrderUserDAO orderUserDAO) {
		this.orderUserDAO = orderUserDAO;
	}

}
