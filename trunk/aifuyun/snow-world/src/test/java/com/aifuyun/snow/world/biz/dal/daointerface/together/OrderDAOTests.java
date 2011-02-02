package com.aifuyun.snow.world.biz.dal.daointerface.together;

import junit.framework.Assert;

import com.aifuyun.snow.world.common.DateTimeUtil;
import com.aifuyun.snow.world.dal.daointerface.together.OrderDAO;
import com.aifuyun.snow.world.dal.dataobject.together.OrderDO;
import com.zjuh.sweet.test.BaseTest;

public class OrderDAOTests extends BaseTest {

	private OrderDAO orderDAO;

	public void testCreate() {
		OrderDO orderDO = createMockOrderDO();
		long id = orderDAO.create(orderDO);
		Assert.assertTrue(id > 0L);
		long idIndb = this.queryField("select id from sw_order where id = ?", "id", Long.class, id);
		Assert.assertEquals(id, idIndb);
	}
	
	public void testQueryById() {
		OrderDO orderDO = createMockOrderDO();
		long id = orderDAO.create(orderDO);
		Assert.assertTrue(id > 0L);
		OrderDO inDb = orderDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(orderDO.getId(), inDb.getId());
		Assert.assertEquals(orderDO.getArriveAddr(), inDb.getArriveAddr());
		Assert.assertEquals(orderDO.getCreatorId(), inDb.getCreatorId());
		Assert.assertEquals(orderDO.getDescription(), inDb.getDescription());
	}
	
	public void testDelete() {
		OrderDO orderDO = createMockOrderDO();
		long id = orderDAO.create(orderDO);
		Assert.assertTrue(id > 0L);
		OrderDO inDb = orderDAO.queryById(id);
		Assert.assertNotNull(inDb);
		orderDAO.delete(id);
		inDb = orderDAO.queryById(id);
		Assert.assertNull(inDb);
	}
	
	public void testUpdate() {
		OrderDO orderDO = createMockOrderDO();
		long id = orderDAO.create(orderDO);
		Assert.assertTrue(id > 0L);
		OrderDO inDb = orderDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(orderDO.getArriveAddr(), inDb.getArriveAddr());
		String newAddr = "hello12344124!!!";
		inDb.setArriveAddr(newAddr);
		orderDAO.update(inDb);
		inDb = orderDAO.queryById(id);
		Assert.assertNotNull(inDb);
		Assert.assertEquals(newAddr, inDb.getArriveAddr());
	}
	
	private OrderDO createMockOrderDO() {
		try {
			OrderDO togetherOrderDO = new OrderDO();
			togetherOrderDO.setArriveAddr("arrive addr");
			togetherOrderDO.setArriveCity("arrive city");
			togetherOrderDO.setArriveTime(DateTimeUtil.parseDate("2010-12-01 19:30:00"));
			togetherOrderDO.setDescription("this is a description");
			togetherOrderDO.setFromAddr("from addr");
			togetherOrderDO.setFromCity("from city");
			togetherOrderDO.setCreatorUsername("abcd");
			togetherOrderDO.setCreatorId(999L);
			togetherOrderDO.setFromTime(DateTimeUtil.parseDate("2010-12-01 17:30:00"));
			togetherOrderDO.setTotalSeats(4);
			return togetherOrderDO;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setOrderDAO(OrderDAO orderDAO) {
		this.orderDAO = orderDAO;
	}
	
}
